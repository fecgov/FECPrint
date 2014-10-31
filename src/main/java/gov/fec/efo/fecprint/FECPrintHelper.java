package gov.fec.efo.fecprint;

import gov.fec.efo.fecprint.data.DataBuilder;
import gov.fec.efo.fecprint.data.Record;
import gov.fec.efo.fecprint.data.RecordType;
import gov.fec.efo.fecprint.gui.PDFGenerationProgressDialog;
import gov.fec.efo.fecprint.layout.Page;
import gov.fec.efo.fecprint.layout.PageManager;
import gov.fec.efo.fecprint.pdf.PDFConcatenate;
import gov.fec.efo.fecprint.pdf.PDFConcatenateTaskListener;
import gov.fec.efo.fecprint.pdf.PDFStamperRealTime;
import gov.fec.efo.fecprint.pdf.PDFStamperTask;
import gov.fec.efo.fecprint.pdf.PDFStamperTaskListener;
import gov.fec.efo.fecprint.pdf.SegregatedPagesTask;
import gov.fec.efo.fecprint.pdf.SegregatedPagesTaskListener;
import gov.fec.efo.fecprint.utility.Utility;

import java.awt.Desktop;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang.math.IntRange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.DocumentException;

public class FECPrintHelper implements PDFStamperTaskListener, PDFConcatenateTaskListener, SegregatedPagesTaskListener{
	
	private DataBuilder dataBuilder = null;
	private PageManager pagesMgr = null;
	private PDFStamperRealTime pdfGenerator = null;
	PDFStamperTask pdfTask;
	int pagesProcessed = 0;
	boolean concatenate = false;
	File outputDirectory = null;
	String outputFileName = null;
	int startChoice= 1;
	int endChoice = 1;
	Frame owner = null;
	boolean silent = true;
	PDFGenerationProgressDialog dlg = null;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public FECPrintHelper (File inputFile,long startingImageNumber,String timeStamp) throws Exception
	{
		buildPages(inputFile.getAbsolutePath(),startingImageNumber,timeStamp);
		Utility.freeUpMemory("Completed buildPages");
	}
	
	private void buildPages(String inputFile, long startingImageNumber, String timeStamp) throws Exception 
	{
		logger.debug("Enter buildPages");
		dataBuilder = new DataBuilder(inputFile);
		pagesMgr = new PageManager(dataBuilder.getTotalPages(),dataBuilder,startingImageNumber, timeStamp);
		TreeMap<RecordType, Vector<Record>> recordBuckets = dataBuilder.getRecordBuckets();			
		buildPages(recordBuckets);
		logger.debug("Done buildPages");
	}
	
	private boolean buildPages(TreeMap<RecordType, Vector<Record>> recordBuckets) throws IOException
	{
		Set<RecordType> set = recordBuckets.keySet();
		Iterator<RecordType> it = set.iterator();
		
		while(it.hasNext())
		{
			Vector<Record> recs = recordBuckets.get(it.next());
			Page page = null;
			for(int x = 0; x < recs.size(); x++)
			{
				Record r = recs.get(x);
				page = pagesMgr.getPage(dataBuilder.getFormType(), r.getType().getType(), r.getPageno());
				page.addRecord(r);
				
				if(r.getChildRecordBuckets() != null && r.getChildRecordBuckets().size() > 0)
				{
					buildPages(r.getChildRecordBuckets());
				}
			}
			if(page != null)
			{
				page.setEndOfSchedule(true);
				page.setRefToAllRecordsInSchedule(recs);				
			}
		}
		return false;
	}
	
	public SegregatedPagesTask generatePdf(int[][] pageRanges, File opDir, boolean concatenate, boolean silent, Frame owner) throws DocumentException, IOException
	{
		logger.debug("Enter generatePdf");
		File opfn = new File(dataBuilder.getDataFileName());
		outputFileName = opfn.getName().substring(0,opfn.getName().lastIndexOf('.')) + "_allpages.pdf";
		
		endChoice = pagesMgr.getTotalPages(); 
		this.concatenate =  concatenate;
		outputDirectory = opDir;
		this.silent = silent;
		this.owner = owner;
		
		if(pageRanges.length > 0 )
		{
			startChoice = pageRanges[0][0];
			if(pageRanges[0][1] < endChoice)
			{
				endChoice = pageRanges[0][1]; 
			}
			
		}
		
		SegregatedPagesTask pdfGen = new SegregatedPagesTask(opDir.getAbsolutePath(),pagesMgr, dataBuilder.getFormType(), outputFileName, startChoice, endChoice);
		pdfGen.addPDFStamperTaskListener(this);
		pdfGen.addSegregatedPagesTaskListener(this);
		pdfGen.start();
		
		if(silent == false && owner != null)
		{
			logger.debug("Showing progress dialog");
			dlg = new PDFGenerationProgressDialog(owner,"Generating " + outputFileName, endChoice - startChoice + 1);
			dlg.setVisible(true);
			dlg.updateProgress(0, "Started generating pages");
		}
		
		logger.debug("Done generatePdf");
		return pdfGen;
	}

	public PageManager getPagesMgr() {
		return pagesMgr;
	}

	public int getTotalPages()
	{		
		return pagesMgr.getTotalPages();
	}

	public DataBuilder getDataBuilder() {
		return dataBuilder;
	}

	public void stamperTaskCompleted(boolean status, IntRange pageRange) 
	{
		logger.debug("Callback received : stamperTaskCompleted");
	}

	public void stamperTaskProgress(int pageProcessedSoFar) {
		
		logger.debug("Callback received : stamperTaskProgress");
		pagesProcessed += 100;
		if(dlg != null)
		{
			logger.debug("Updating progress dialog");
			dlg.updateProgress(pagesProcessed, "Generated " + pagesProcessed + " pages");
		}
		logger.info("Generated " + pagesProcessed + " pages so far.");		
	}

	public void concateCompleted(boolean status) {
		
		logger.debug("Callback received : concateCompleted");
		if(dlg != null)
		{
			logger.debug("Hiding progress dialog");
			dlg.setVisible(false);			
		}
		
		if(silent == false)
		{
			try {
				Desktop desktop = Desktop.getDesktop();
				if(desktop != null)
				{
					File file = new File(outputDirectory.getAbsolutePath() + File.separator +  outputFileName);
					
					FileChannel channel = new RandomAccessFile(file, "rw").getChannel(); 
					for(int t = 0; t < 5; t++)
					{
						// Get an exclusive lock on the whole file 
						FileLock lock = null;//channel.lock(); 
						try 
						{     
							lock = channel.tryLock();     
							// Ok. You get the lock
							break;
						} 
						catch (OverlappingFileLockException e) 
						{     
							// File is open by someone else
							try 
							{
								logger.error("Unable to open PDF file. Attempt# " + t ,e);
								Thread.sleep(5000);
							} 
							catch (InterruptedException e1) 
							{									
								e1.printStackTrace();
							}
							continue;								
						} 
						finally 
						{     
							lock.release(); 
							channel.close();
						}
					}					
					desktop.open(new File(outputDirectory.getAbsolutePath() + File.separator +  outputFileName));
				}
			} 
			catch (IOException e) 
			{
				
				logger.error("Unable to open PDF viewer",e);
			}
		}
		
	}

	public void concateProgress(int pageProcessedSoFar) {
		
		logger.debug("Callback received : concateProgress");
		
		logger.info("Concatenated " + pageProcessedSoFar + " pages so far.");	
		if(dlg != null)
		{
			logger.debug("Updating progress dialog");
			dlg.updateProgress(pageProcessedSoFar, "Concatenated " + pageProcessedSoFar + " pages");
		}
	}

	public void taskCompleted(boolean status) {
		
		logger.debug("Callback received : taskCompleted");
		if(concatenate)
		{
			
			try {
				PDFConcatenate concatenator = new PDFConcatenate(outputDirectory.getAbsolutePath(), outputFileName, startChoice, endChoice);
				concatenator.addPDFConcatenateTaskListener(this);
				concatenator.start();
				
				if(dlg != null)
				{
					logger.debug("Reseting progress dialog");
					dlg.setTitle("Building " + outputFileName);
					dlg.updateProgress(0, "Completed Generation.\nStarting Concatenation");
				}
			} 
			catch (Exception e) 
			{
				logger.error("Failed building the concatenated file", e);
			}			
			
		}
		else
		{
			if(dlg != null)
			{
				logger.debug("Hiding progress dialog");
				dlg.setVisible(false);
			}
		}
		
	}
	
	public void setStampAsElectronicallyFiled(boolean stampAsElectronicallyFiled) {
		pagesMgr.setStampAsElectronicallyFiled(stampAsElectronicallyFiled);
	}
}
