/**
 * 
 */
package gov.fec.efo.fecprint.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;

import gov.fec.efo.fecprint.utility.AppProperties;
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
	
	protected final Log logger = LogFactory.getLog(PDFConcatenate.class);
	private boolean status = true;
	
	public boolean isStatus() {
		return status;
	}

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
			
			if (purgePages) {
				logger.debug("Will purge individual pages after concatenation completes");
			} else {
				logger.debug("Will NOT purge individual pages after concatenation completes");
			}
			
			document = new Document();			
			PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(opDir + File.separator + opFileName));
			copy.setFullCompression();			
			document.open();
			
			logger.info("Pages to concatenate: " + (endPg - startPg + 1));
			
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
				
				if(pg%1000 == 0)
				{
					fireListenersTaskProgress(pg);					
				}
			}
			logger.debug("Completed concatinating all the pages");
			success = true;
		}
		catch(Exception e)
		{
			logger.error("Error in concatenation thread: " + e.getMessage(), e);
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
	
	
	public void addPDFConcatenateTaskListener(PDFConcatenateTaskListener listener)
	{
		listeners.add(listener);
	}
	
	private void fireListenersTaskCompleted(boolean status)
	{
		for(int i = 0; i < listeners.size(); i++)
		{
			listeners.get(i).concateCompleted(status);
		}
	}
	
	private void fireListenersTaskProgress(int pageProcessed)
	{
		for(int i = 0; i < listeners.size(); i++)
		{
			listeners.get(i).concateProgress(pageProcessed);
		}
	}

	public void concateCompleted(boolean status) {
		
		if (!status) {
			logger.error("FAILED Callback received : concateCompleted");
			this.status = status; // any one status failing should set this to false, nothing sets it to true
			return;
		}
		logger.debug("Callback received : concateCompleted");
		
	}

	public void concateProgress(int pageProcessedSoFar) {
		logger.info("Concatenated " + pageProcessedSoFar + " pages so far.");
	}
}
