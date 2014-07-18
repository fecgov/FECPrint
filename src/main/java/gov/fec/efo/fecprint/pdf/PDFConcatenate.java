/**
 * 
 */
package gov.fec.efo.fecprint.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import gov.fec.efo.fecprint.data.BaseRecordType;
import gov.fec.efo.fecprint.layout.PageManager;
import gov.fec.efo.fecprint.utility.AppProperties;
import gov.fec.efo.fecprint.utility.Utility;
/**

/**
 * @author Milind
 *
 */
public class PDFConcatenate extends Thread implements PDFConcatenateTaskListener
{

	Vector<PDFConcatenateTaskListener> listeners;
	String opDir,opFileName;
	int startPg,endPg;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public PDFConcatenate(String opDir,String opFileName,int startPg, int endPg) throws DocumentException, IOException
	{
		super(opFileName + " Concatenate Pages");
		this.opDir = opDir;
		this.opFileName = opFileName;
		this.startPg = startPg;
		this.endPg = endPg;
		listeners = new Vector<PDFConcatenateTaskListener>();
	}
	
	public void run()
	{
		Document document = null;
		boolean success = false;
		try
		{
			boolean purgePages = AppProperties.deletePagePdfFilesAfterConcatination();
			logger.debug("AppProperties.deletePagePdfFilesAfterConcatination = " + purgePages);
			document = new Document();			
			PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(opDir + File.separator + opFileName));
			document.open();
			copy.setFullCompression();			
			
			for(int pg = startPg; pg <= endPg; pg++)
			{
				PdfReader reader = new PdfReader(opDir + File.separator + pg + ".pdf");
				int n = reader.getNumberOfPages();
				for (int i = 0; i < n;) 
				{
					copy.addPage(copy.getImportedPage(reader, ++i));
				}
				copy.freeReader(reader);
				reader.close();
				copy.flush();
				
				if(purgePages)
				{
					FileUtils.deleteQuietly(new File(opDir + File.separator + pg + ".pdf"));
				}
				else
				{
					compressPdf(pg);
				}
				
				if(pg%10000 == 0)
				{
					Utility.freeUpMemory("In PDFConcatenate. Memory cleanup after processing 10000 pages");
				}
				
				if(pg%100 == 0)
				{
					fireListenersTaskProgress(pg);					
				}
			}
			logger.debug("Completed concatinating all the pages");
			success = true;
		}
		catch(Exception e)
		{
			logger.error("Error in concatenation thread", e);
		}
		finally
		{
			if(document != null)
			{
				document.close();
			}
			fireListenersTaskCompleted(success);			
		}
	}
	
	public void compressPdf(int pageNo)
	{
		Document document = null;
		try
		{	
			document = new Document();			
			PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(opDir + File.separator + pageNo + "_temp.pdf"));
			document.open();
			copy.setFullCompression();
			
			PdfReader reader = new PdfReader(opDir + File.separator + pageNo + ".pdf");
			int n = reader.getNumberOfPages();
			for (int i = 0; i < n;) 
			{
				copy.addPage(copy.getImportedPage(reader, ++i));
			}
			copy.freeReader(reader);
			reader.close();
			copy.flush();	
			
		}
		catch(Exception e)
		{
			logger.error("Error in compressing file ", e);
		}
		finally
		{
			if(document != null)
			{
				document.close();
			}			
		}
		
		
		
		try 
		{
			FileUtils.copyFile(new File(opDir + File.separator + pageNo + "_temp.pdf"), new File(opDir + File.separator + pageNo + ".pdf"));
			FileUtils.deleteQuietly(new File(opDir + File.separator + pageNo + "_temp.pdf"));
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void addPDFConcatenateTaskListener(PDFConcatenateTaskListener listener)
	{
		listeners.add(listener);
	}
	
	private void fireListenersTaskCompleted(boolean status)
	{
		logger.debug("Enter fireListenersTaskCompleted");
		for(int i = 0; i < listeners.size(); i++)
		{
			listeners.get(i).concateCompleted(status);
		}
		logger.debug("Done fireListenersTaskCompleted");
	}
	
	private void fireListenersTaskProgress(int pageProcessed)
	{
		logger.debug("Enter fireListenersTaskProgress");
		for(int i = 0; i < listeners.size(); i++)
		{
			listeners.get(i).concateProgress(pageProcessed);
		}
		logger.debug("Done fireListenersTaskProgress");
	}

	public void concateCompleted(boolean status) {
		logger.debug("Callback received : concateCompleted");
		
	}

	public void concateProgress(int pageProcessedSoFar) {
		logger.debug("Callback received : concateProgress");		
		logger.info("Concatenated " + pageProcessedSoFar + " pages so far.");
	}
}
