/**
 * 
 */
package com.nictusa.fecprint.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.nictusa.fecprint.data.BaseRecordType;
import com.nictusa.fecprint.data.RecordType;
import com.nictusa.fecprint.layout.Page;
import com.nictusa.fecprint.layout.PageManager;
import com.nictusa.fecprint.utility.AppProperties;

/**
 * @author Milind
 * 
 */
public class PDFStamperRealTime {

	String opPdfDir;
	PageManager pageManager = null;	
	BaseRecordType formType;
	static private HashMap<String, String> formPages = new HashMap<String, String>();
	
	protected final Log logger = LogFactory.getLog(getClass());

	

	public PDFStamperRealTime(String opDir, PageManager pgMgr, BaseRecordType coverPageType) {
		opPdfDir = opDir;
		pageManager = pgMgr;
		formType = coverPageType;
	}

	public boolean generatePage(int pageNo) throws IOException, DocumentException 
	{
		String outputFilePath = opPdfDir + File.separator + pageNo + ".pdf";
		File fPdfPath = new File(outputFilePath);
		if(fPdfPath.exists())
		{
			return false;
		}
		else
		{
			stampContent(pageManager.getPage(pageNo),fPdfPath,pageNo);
			return true;
		}
	}

	private void stampContent(Page page, File outputFilePath,int pageNo) throws IOException,DocumentException 
	{
		if(page != null)
		{
			PdfStamper stamp;
			
			String flTemplate = getTemplate(formType.name(), page.getPageProperties().getType().name());		
	
			PdfReader reader = new PdfReader(new RandomAccessFileOrArray(flTemplate),null);
			int n = reader.getNumberOfPages();
			FileOutputStream baos = new FileOutputStream(outputFilePath);
			stamp = new PdfStamper(reader, baos);
			stamp.setFullCompression();
			AcroFields form = stamp.getAcroFields();
			Map data = page.getData();
			java.util.Iterator<String> it = data.keySet().iterator();
			while (it.hasNext()) {
				String fieldName = it.next();
	
				try
				{
					form.setField(fieldName, (String) data.get(fieldName));
				}
				catch(Exception e)
				{
					logger.error("Error stamping data on pdf template",e);
				}
	
			}
			stamp.setFormFlattening(true);		
			stamp.close();
			reader.close();
			
			if(n > 1)
			{
				logger.debug("Detected multiple pagesin pdf template. Splitting now");
				File originalFl = new File(opPdfDir + File.separator + "Original" + pageNo + ".pdf");
				FileUtils.moveFile(outputFilePath, originalFl);			
				reader = new PdfReader(new RandomAccessFileOrArray(originalFl.getAbsolutePath()),null);
				Document document;
				PdfCopy copy;			
				for (int i = 1; i <= n; i++,pageNo++) 
				{
					document = new Document();
					copy = new PdfCopy(document, new FileOutputStream(opPdfDir + File.separator + pageNo + ".pdf"));
					document.open();				
					copy.addPage(copy.getImportedPage(reader, i));
					document.close();
				}
				reader.close();
				
				if (page.getPageProperties().getType() == BaseRecordType.F3 && 
						pageManager.getDataBuilder().getRecordBuckets().containsKey(RecordType.F3S)) 
				{
					FileUtils.deleteQuietly(new File(opPdfDir + File.separator +  "3.pdf"));
					FileUtils.deleteQuietly(new File(opPdfDir + File.separator +  "4.pdf"));
				}
				else if (page.getPageProperties().getType() == BaseRecordType.F3P && 
						pageManager.getDataBuilder().getRecordBuckets().containsKey(RecordType.F3PS)) 
				{
					FileUtils.deleteQuietly(new File(opPdfDir + File.separator +  "3.pdf"));
					FileUtils.deleteQuietly(new File(opPdfDir + File.separator +  "4.pdf"));
					FileUtils.deleteQuietly(new File(opPdfDir + File.separator +  "5.pdf"));
					FileUtils.deleteQuietly(new File(opPdfDir + File.separator +  "6.pdf"));
					FileUtils.deleteQuietly(new File(opPdfDir + File.separator +  "7.pdf"));
				}
			}
		}
		
	}

	private String getTemplate(String formType, String pageType) throws IOException {
		
		String key = formType + File.separator + pageType;
		if (formPages.get(key) == null) 
		{
			synchronized (this) 
			{
				String dir = AppProperties.getPdfTemplateDirectory();			
				dir = dir + File.separator;
				
				File fl = new File(dir + formType + File.separator + pageType + ".pdf");	
				if (fl.exists() == false) 
				{
					fl = new File(dir + pageType + ".pdf");
				}
				formPages.put(key, fl.getAbsolutePath());
				logger.debug("Loaded tempalte from " + fl.getAbsolutePath());
			}
		}
		return formPages.get(key);
	}

	
}
