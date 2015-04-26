package gov.fec.efo.fecprint.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.awt.Desktop;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.DocumentException;
import gov.fec.efo.fecprint.FECPrintHelper;
import gov.fec.efo.fecprint.data.BaseRecordType;
import gov.fec.efo.fecprint.data.DataBuilder;
import gov.fec.efo.fecprint.data.Record;
import gov.fec.efo.fecprint.data.RecordType;
import gov.fec.efo.fecprint.layout.Page;
import gov.fec.efo.fecprint.layout.PageManager;
import gov.fec.efo.fecprint.pdf.PDFConcatenate;
import gov.fec.efo.fecprint.pdf.PDFStamperRealTime;
import gov.fec.efo.fecprint.pdf.PDFStamperTask;
import gov.fec.efo.fecprint.utility.AppProperties;
import gov.fec.efo.fecprint.utility.Utility;

public class FilingView extends JSplitPane implements TreeSelectionListener {
	
	private JScrollPane treeView = null;
	private JTree tree = null;
	private ArrayList<TreePath> listPaths= null;
	private PageView pageView = null;
	private DataBuilder dBuilder = null;
	private PageManager pagesMgr = null;
	FECPrintHelper helper = null;
	File opDir  = null;
	private PDFStamperRealTime pdfGenerator = null;
	private int currentPageInView;
	PDFStamperTask pdfTask;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public FilingView (File inputFile) throws Exception
	{
		super(JSplitPane.HORIZONTAL_SPLIT);
		helper = new FECPrintHelper(inputFile,1,null);
		dBuilder = helper.getDataBuilder();
		pagesMgr = helper.getPagesMgr();
		
		if(dBuilder.getTotalPages() > 0)
		{
			opDir = new File(AppProperties.getOutputDirectory() + File.separator + inputFile.getName().substring(0,inputFile.getName().lastIndexOf('.')) + "_" + Calendar.getInstance().getTimeInMillis());
			if(opDir.exists() == false)
			{
				opDir.mkdir();
			}
			FECPrintMainWindow.getTempDirs().add(opDir);
			
			pdfGenerator = new PDFStamperRealTime(opDir.getAbsolutePath(),pagesMgr, dBuilder.getFormType());
			currentPageInView = 1;
			pdfGenerator.generatePage(1);
			pdfTask = new PDFStamperTask();			 
			pdfTask.startTask(opDir.getAbsolutePath(),pagesMgr, dBuilder.getFormType(),1, pagesMgr.getTotalPages() > AppProperties.getOutputChunkSize()? AppProperties.getOutputChunkSize() : pagesMgr.getTotalPages(),null);
			
			
			FECPrintMainWindow.addFileToMRU(inputFile);
			
			DefaultMutableTreeNode top =
	            new DefaultMutableTreeNode(dBuilder.getCoverPage().getType().name());
	        createNodes(top);
	
	        //Create a tree that allows one selection at a time.
	        tree = new JTree(top);
	        tree.getSelectionModel().setSelectionMode
	                (TreeSelectionModel.SINGLE_TREE_SELECTION);
	
	        //Listen for when the selection changes.
	        tree.addTreeSelectionListener(this);
	
	        /*if (false) {
	            tree.putClientProperty("JTree.lineStyle", lineStyle);
	        }*/
	
	        //Create the scroll pane and add the tree to it. 
	        treeView = new JScrollPane(tree);
	        listPaths = new ArrayList<TreePath>(tree.getRowCount());
	        for( int i = 0; i < tree.getRowCount(); ++i )   
			{   
			  tree.expandRow( i );  
			  listPaths.add(tree.getPathForRow(i));			  
			}
	        pageView = new PageView(opDir.getAbsolutePath());
	        
			
			this.setLeftComponent(treeView);
			this.setRightComponent(pageView);
			this.setDividerLocation(200);
		}

		
	}
	
	
	private void createNodes(DefaultMutableTreeNode top) {
		
		logger.debug("Enter createNodes");
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;        
        TreeMap<BaseRecordType,DefaultMutableTreeNode> firstLevel = new TreeMap<BaseRecordType,DefaultMutableTreeNode>();   
        
        TreeMap<RecordType, Vector<Record>> recordBuckets = dBuilder.getRecordBuckets();		
		Set set = recordBuckets.keySet();
		Iterator it = set.iterator();
		it.next(); //Skip the cover record
		
		while(it.hasNext())
		{
			RecordType rt = (RecordType) it.next();
			if(rt.getType().equals(BaseRecordType.TEXT))
					continue;
			if(firstLevel.get(rt.getType()) == null)
			{
				firstLevel.put(rt.getType(), new DefaultMutableTreeNode(new RecordInfo(rt, recordBuckets.get(rt).get(0).getPageno(), rt.getType().name())));
			}
			category = firstLevel.get(rt.getType());
			book = new DefaultMutableTreeNode(new RecordInfo(rt,recordBuckets.get(rt).get(0).getPageno(), rt.name()));
			if(rt.name().equals(rt.getType().name()) == false)
			{
				category.add(book);
			}
		}
		
		set = firstLevel.keySet();
		it = set.iterator();
		while(it.hasNext())
		{
			top.add((DefaultMutableTreeNode)firstLevel.get(it.next()));
		}
		
		logger.debug("Done createNodes");
    }
	
	private class RecordInfo {
	      public RecordType recordType;      
	      public int startPageNo;
	      public String displayName;

	      public RecordInfo(RecordType rt, int startPgNo, String uiName) {
	    	  recordType = rt;          
	    	  startPageNo = startPgNo;
	    	  displayName = uiName;
	      }

	      public String toString() {
	          return displayName;
	      }
	  }
	
	public void valueChanged(TreeSelectionEvent e) {
        
		try
		{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           tree.getLastSelectedPathComponent();

        if (node == null) 
        	return;

        Object nodeInfo = node.getUserObject();
        if(nodeInfo instanceof java.lang.String)
        	showPage(1, false);
        else 
        {
        	RecordInfo rinfo = (RecordInfo)nodeInfo;
        	showPage(rinfo.startPageNo, false); 
        }
		}
		catch (IOException eX) 
        {			
			logger.error(eX);
		}
		/*
        if (node.isLeaf()) 
        {
        	RecordInfo rinfo = (RecordInfo)nodeInfo;
        	TreeMap<RecordType, Vector<Record>> recordBuckets = dBuilder.getRecordBuckets();
            int pageNo = recordBuckets.get(rinfo.recordType).get(0).getPageno();
            showPage(pageNo);           
        } */ catch (DocumentException eDoc) 
        {
        	logger.error(eDoc);
		} 
		
        
    }
	
	public void showPage(int pageNo, boolean syncTreeSelection) throws IOException, DocumentException
	{
		if(pageNo > pagesMgr.getTotalPages())
		{
			pageNo = pagesMgr.getTotalPages();
		}
		else if(pageNo <= 0)
		{
			pageNo = 1;
		}
		boolean generatedNow = pdfGenerator.generatePage(pageNo);
		if(generatedNow && pageNo < pagesMgr.getTotalPages())
		{
			int sp = pageNo + 1;
			int ep = pagesMgr.getTotalPages();			
			if(pagesMgr.getTotalPages() > (pageNo + AppProperties.getOutputChunkSize()))
			{
				ep = pageNo + AppProperties.getOutputChunkSize();
			}
			pdfTask.addJob(opDir.getAbsolutePath(),pagesMgr, dBuilder.getFormType(), sp, ep,null);
		}
		currentPageInView = pageNo;
        pageView.showPage(pageNo);
        if(syncTreeSelection)
        {
        	tree.removeTreeSelectionListener(this);
        	if(pagesMgr.getPage(pageNo) != null)
        	{
	        	RecordType selectedRecordType = pagesMgr.getPage(pageNo).getRecs()[0].getType();
		        for(int t = 0 ; t < listPaths.size(); t++)
		        {
		        	DefaultMutableTreeNode nd = (DefaultMutableTreeNode)(listPaths.get(t).getLastPathComponent());
		        	
		        	if(nd.toString().endsWith(selectedRecordType.name()))
		        	{
		        		tree.expandPath(listPaths.get(t));
		        		tree.makeVisible(listPaths.get(t));
		        		tree.setSelectionRow(t);        		
		        		break;
		        	}
		        }
        	}
	        tree.addTreeSelectionListener(this);
        }
        
        ((FilingTabs)getParent()).fireStateChanged();

	}
	
	public void generatePrintPreview(int[][] pageRanges) throws DocumentException, IOException
	{
		helper.generatePdf(pageRanges, opDir, true, false, Utility.getMainWnd());		
	}

	public PageManager getPagesMgr() {
		return pagesMgr;
	}

	public int getCurrentPageInView() {
		return currentPageInView;
	}
	
	public int getTotalPages()
	{		
		return pagesMgr.getTotalPages();
	}	
	
	public void removeView()
	{
		if(pageView != null)
		{
			pageView.removeView();
			remove(pageView);
		}
		if(treeView != null)
		{
			remove(treeView);
		}
	}


	public DataBuilder getdBuilder() {
		return dBuilder;
	}



}
