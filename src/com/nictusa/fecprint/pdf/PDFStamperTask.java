package com.nictusa.fecprint.pdf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.math.IntRange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nictusa.fecprint.data.BaseRecordType;
import com.nictusa.fecprint.layout.PageManager;
import com.nictusa.fecprint.utility.AppProperties;
import com.nictusa.fecprint.utility.Utility;

public class PDFStamperTask 
{
	Vector<PDFStamperThread> threadsScheduled; 
	Vector<PDFStamperThread> threadsRunning;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public PDFStamperTask()
	{
		threadsScheduled = new Vector<PDFStamperThread>();
		threadsRunning = new Vector<PDFStamperThread>();		
	}
	public List startTask(String opDir, PageManager pgMgr,BaseRecordType coverPageType,int startPage,int endPage, List<PDFStamperTaskListener> listener) {

		
		ArrayList<IntRange> rangeList = new ArrayList<IntRange>();  
		if(endPage == -1)
		{
			startPage = 1;
			endPage = pgMgr.getTotalPages();
		}
		
		if(endPage - startPage <= AppProperties.getOutputChunkSize())
		{
			logger.debug("Task smaller then chunk size");
			PDFStamperThread r = new PDFStamperThread(opDir, pgMgr,coverPageType,startPage,endPage,this);
			rangeList.add(new IntRange(startPage,endPage));
			if(listener != null && listener.size() > 0)
			{
				for(int t = 0 ; t < listener.size(); t++)
				{
					r.addPDFStamperTaskListener(listener.get(t));
				}
			}
			threadsScheduled.add(r);
			
		}
		else
		{
			logger.debug("Task bigger then chunk size");
			int totalPageToPrint = endPage - startPage + 1;
			int batchSize = totalPageToPrint/AppProperties.getMaxPdfThreadsAllowed();
			logger.debug("batchSize = " + batchSize);
			int residual = totalPageToPrint%AppProperties.getMaxPdfThreadsAllowed();
			for(int i = 0; i  <= AppProperties.getMaxPdfThreadsAllowed() && startPage < endPage; i++)
			{
				PDFStamperThread r = new PDFStamperThread(opDir, pgMgr,coverPageType,startPage ,startPage + batchSize + residual,this);
				rangeList.add(new IntRange(startPage,startPage + batchSize + residual));
				logger.debug("Task start page = " + startPage );
				if(listener != null && listener.size() > 0)
				{
					for(int t = 0 ; t < listener.size(); t++)
					{
						r.addPDFStamperTaskListener(listener.get(t));
					}
				}
				threadsScheduled.add(r);
				startPage = startPage + batchSize + residual + 1;
				residual =0;
			}
		}
		
		while(threadsScheduled.size() > 0)
		{
			Thread pdfThread = new Thread (threadsScheduled.get(0), threadsScheduled.get(0).opPdfDir.substring(threadsScheduled.get(0).opPdfDir.lastIndexOf(File.separator) + 1) + " Stamper thread " + threadsScheduled.get(0).start + " to " + threadsScheduled.get(0).end);
			threadsRunning.add(threadsScheduled.remove(0));
			pdfThread.start();
		}
		return rangeList;
	}
	
	synchronized void jobCompleted(PDFStamperThread t)
	{
		logger.debug("Callback received : jobCompleted");
		threadsRunning.remove(t);
		if(threadsScheduled.size() > 0)
		{
			Thread pdfThread = new Thread (threadsScheduled.get(0), threadsScheduled.get(0).opPdfDir.substring(threadsScheduled.get(0).opPdfDir.lastIndexOf(File.separator) + 1) + " Stamper thread " + threadsScheduled.get(0).start + " to " + threadsScheduled.get(0).end);
			threadsRunning.add(threadsScheduled.remove(0));
			pdfThread.start();
		}		
	}
	
	public boolean hasPendingJobs()
	{
		return threadsRunning.size() > 0 || threadsScheduled.size() > 0; 
	}
	
	synchronized public void addJob(String opDir, PageManager pgMgr,BaseRecordType coverPageType,int startPage,int endPage,List<PDFStamperTaskListener> listener)
	{
		logger.debug("Adding jobs for page range :" + startPage + " to " + endPage);
		if(endPage == -1)
		{
			startPage = 1;
			endPage = pgMgr.getTotalPages();
		}
		
		if(endPage - startPage <= AppProperties.getOutputChunkSize())
		{
			logger.debug("Task smaller then chunk size");
			PDFStamperThread r = new PDFStamperThread(opDir, pgMgr,coverPageType,startPage,endPage,this);
			if(listener != null && listener.size() > 0)
			{
				for(int t = 0 ; t < listener.size(); t++)
				{
					r.addPDFStamperTaskListener(listener.get(t));
				}
			}
			threadsScheduled.add(r);
			
		}
		else
		{
			logger.debug("Task bigger then chunk size");
			int totalPageToPrint = endPage - startPage + 1;
			int batchSize = totalPageToPrint/AppProperties.getMaxPdfThreadsAllowed(); 
			int residual = totalPageToPrint%AppProperties.getMaxPdfThreadsAllowed();
			for(int i = 0; i  <= AppProperties.getMaxPdfThreadsAllowed() && startPage < endPage; i++)
			{
				PDFStamperThread r = new PDFStamperThread(opDir, pgMgr,coverPageType,startPage ,startPage + batchSize + residual,this);
				if(listener != null && listener.size() > 0)
				{
					for(int t = 0 ; t < listener.size(); t++)
					{
						r.addPDFStamperTaskListener(listener.get(t));
					}
				}
				logger.debug("Task start page = " + startPage );
				threadsScheduled.add(r);				
				startPage = startPage + batchSize + residual + 1;
				residual =0;
			}
		}
		
		if(threadsRunning.size() < AppProperties.getMaxPdfThreadsAllowed())
		{
			if(threadsScheduled.size() > 0)
			{
				Thread pdfThread = new Thread (threadsScheduled.get(0),threadsScheduled.get(0).opPdfDir.substring(threadsScheduled.get(0).opPdfDir.lastIndexOf(File.separator) + 1) + " Stamper thread " + threadsScheduled.get(0).start + " to " + threadsScheduled.get(0).end);
				threadsRunning.add(threadsScheduled.remove(0));
				pdfThread.start();
			}
		}
	}

	class PDFStamperThread extends PDFStamperRealTime implements Runnable{

		int start=1;
		int end=-1;
		PDFStamperTask controller;
		Vector<PDFStamperTaskListener> listeners;
		
		public PDFStamperThread(String opDir, PageManager pgMgr,BaseRecordType coverPageType, int startPage,int endPage, PDFStamperTask controller) {
			super(opDir, pgMgr, coverPageType);
			start = startPage;
			end = endPage;
			this.controller = controller;
			listeners = new Vector<PDFStamperTaskListener>();
		}

		public void run() 
		{
			logger.debug("Starting PDFStamperThread");
			try
			{
			boolean success = false;
			try
			{
				int totalPagesInThisTask = end - start + 1;
				for(int pgNo = start, pagesProcessed = 0; pgNo <= end; pgNo++)
				{
					generatePage(pgNo);
					pagesProcessed++;
					if(pagesProcessed%100000 == 0)
					{
						Utility.freeUpMemory("In PDFStamperThread. Memory cleanup after processing 100000 pages");					
					}
					if(pagesProcessed%100 == 0)
					{
						fireListenersTaskProgress(pagesProcessed);					
					}
				}
				success = true;
			}
			finally
			{
				controller.jobCompleted(this);
				fireListenersTaskCompleted(success, new IntRange(start,end));
				
				listeners.removeAllElements();
				
			}
			}
			catch(Throwable e)
			{
				logger.error("Error is stamper thread", e);
			}
			logger.debug("Done PDFStamperThread");
		}
		
		public void addPDFStamperTaskListener(PDFStamperTaskListener listener)
		{
			listeners.add(listener);
		}
		
		public void removePDFStamperTaskListener(PDFStamperTaskListener listener)
		{
			listeners.remove(listener);
		}
		
		private void fireListenersTaskCompleted(boolean status, IntRange pageRange)
		{
			logger.debug("Start fireListenersTaskCompleted");
			for(int i = 0; i < listeners.size(); i++)
			{
				listeners.get(i).stamperTaskCompleted(status, pageRange);
			}
			logger.debug("Done fireListenersTaskCompleted");
		}
		
		private void fireListenersTaskProgress(int pageProcessed)
		{
			logger.debug("Start fireListenersTaskProgress");
			for(int i = 0; i < listeners.size(); i++)
			{
				listeners.get(i).stamperTaskProgress(pageProcessed);
			}
			logger.debug("Done fireListenersTaskProgress");
		}

	}

}
