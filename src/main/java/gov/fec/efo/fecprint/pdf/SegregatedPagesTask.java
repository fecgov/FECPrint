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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class SegregatedPagesTask extends Thread implements PDFStamperTaskListener 
{

	String opFileName;
	PDFStamperTask pdfTask;
	int startPage;
	int endPage;
	String opDir; 
	PageManager pgMgr;
	BaseRecordType coverPageType;
	HashMap<IntRange,Boolean> mapRangeReady;
	List<IntRange> pdfGenerationRanges;
	List<Boolean> pdfRangeReady;
	List<PDFStamperTaskListener> listPDFStamperTaskListener;
	Vector<SegregatedPagesTaskListener> listeners;
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public SegregatedPagesTask(String opDir, PageManager pgMgr,BaseRecordType coverPageType,String opFile, int startPg, int endPg) 
	{
		super(opFile + " Segregated Pages " + startPg + " to " + endPg);
		this.opDir = opDir;
		this.pgMgr= pgMgr;
		this.coverPageType = coverPageType;
		startPage = startPg;
		endPage = endPg;		
		opFileName = opFile;
		listPDFStamperTaskListener = new ArrayList<PDFStamperTaskListener>();
		listPDFStamperTaskListener.add(this);
		listeners = new Vector<SegregatedPagesTaskListener>();
	}
	
	public void addPDFStamperTaskListener(PDFStamperTaskListener listener)
	{
		listPDFStamperTaskListener.add(listener);
	}
	
	public void addSegregatedPagesTaskListener(SegregatedPagesTaskListener listener)
	{
		listeners.add(listener);
	}

	public void run()
	{
		logger.debug("Starting SegregatedPagesTask");
		boolean success = false;
		try
		{
			boolean purgePages = AppProperties.deletePagePdfFilesAfterConcatination();	
			pdfTask = new PDFStamperTask();			 
			pdfGenerationRanges = pdfTask.startTask(opDir,pgMgr, coverPageType,startPage, endPage,listPDFStamperTaskListener);
			pdfRangeReady = new ArrayList<Boolean>();
			logger.debug("SegregatedPagesTask - Total tasks spooling for " + pdfGenerationRanges.size());
			for(int z = 0 ; z < pdfGenerationRanges.size(); z++)
			{
				pdfRangeReady.add(Boolean.FALSE);
			}
			
			while(pdfTask.hasPendingJobs())
			{
				Thread.sleep(3000);
			}	
			success = true;
		}
		catch(Exception e)
		{
			logger.error("Error in SegregatedPagesTask thread", e);
		}
		finally
		{
			for(int i = 0; i < listeners.size(); i++)
			{
				listeners.get(i).taskCompleted(success);
			}
		}
		logger.debug("Done SegregatedPagesTask");
	}
	
	
	public void stamperTaskCompleted(boolean status, IntRange pageRange) {
		
		logger.debug("Callback received : stamperTaskCompleted" );
		for(int i = 0 ; i < pdfGenerationRanges.size(); i++)
		{
			if(pdfGenerationRanges.get(i).equals(pageRange))
			{
				pdfRangeReady.set(i, Boolean.TRUE);
				logger.debug("Task moved to pdfRangeReady pool");
				break;
			}
		}
	}

	public void stamperTaskProgress(int pageProcessedSoFar) {
		// TODO Auto-generated method stub
		
	}
}
