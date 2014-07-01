package com.nictusa.fecprint.gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.DocumentException;
import com.nictusa.fecprint.data.Record;
import com.nictusa.fecprint.utility.Utility;

public class FilingTabs extends JTabbedPane implements ActionListener, ChangeListener, KeyListener
{
	
	JTextField pagenoField;
	JTextField totalPages;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public FilingTabs(FECPrintMainWindow a_Parent)
	{
		super(JTabbedPane.BOTTOM);
		addChangeListener(this);
		setBackground(Color.gray);
		pagenoField = new JTextField("",6);
		pagenoField.setToolTipText("Enter the page number and click 'GO' button");
		pagenoField.addKeyListener(this);
		totalPages = new JTextField("",6);
		totalPages.setEditable(false);
		totalPages.setToolTipText("Total pages for this filing");
		a_Parent.addWindowListener(new WindowListenerFecPrint());
		this.addChangeListener(a_Parent);
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
			try
			{
					if(e.getActionCommand().equals("Open"))
					{
						onOpen();									
					}	
					else if(e.getActionCommand().equals("Print"))
					{
						onPrint();
					}
					else if(e.getActionCommand().equals("Close"))
					{
						onClose(false);
					}
					else if(e.getActionCommand().equals("Exit"))
					{
						onExit();					
					}
					else if(e.getActionCommand().equals("go"))
					{
						if(this.getSelectedComponent() != null)
						{
							FilingView currTab = (FilingView)(this.getSelectedComponent());
							try
							{
								int pageNo = Integer.parseInt(pagenoField.getText());
								currTab.showPage(pageNo, true);
							}
							catch(NumberFormatException eNum)
							{
								logger.debug(eNum);
							}
						}
					}
					else if(e.getActionCommand().equals("first"))
					{
						if(this.getSelectedComponent() != null)
						{
							FilingView currTab = (FilingView)(this.getSelectedComponent());
							try
							{
								currTab.showPage(1, true);
							}
							catch(NumberFormatException eNum)
							{
								logger.debug(eNum);
							}
						}
					}
					else if(e.getActionCommand().equals("previous"))
					{
						if(this.getSelectedComponent() != null)
						{
							FilingView currTab = (FilingView)(this.getSelectedComponent());
							try
							{
								currTab.showPage(currTab.getCurrentPageInView() - 1, true);
							}
							catch(NumberFormatException eNum)
							{
								logger.debug(eNum);
							}
						}
					}
					else if(e.getActionCommand().equals("next"))
					{
						if(this.getSelectedComponent() != null)
						{
							FilingView currTab = (FilingView)(this.getSelectedComponent());
							try
							{
								currTab.showPage(currTab.getCurrentPageInView() + 1, true);
							}
							catch(NumberFormatException eNum)
							{
								logger.debug(eNum);
							}
						}
					}
					else if(e.getActionCommand().equals("last"))
					{
						if(this.getSelectedComponent() != null)
						{
							FilingView currTab = (FilingView)(this.getSelectedComponent());
							try
							{
								currTab.showPage(currTab.getPagesMgr().getTotalPages(), true);
							}
							catch(NumberFormatException eNum)
							{
								logger.debug(eNum);
							}
						}
					}
					else
					{
						openFile(new File(e.getActionCommand()));
					}
				
			}
			catch(Exception eX)
			{
				logger.error(eX);				
			}
	}
	
	public void findAndShowRecord(String transactionId) throws IOException, DocumentException
	{
		if(this.getSelectedComponent() != null)
		{
			FilingView currTab = (FilingView)(this.getSelectedComponent());
			Record recShow = currTab.getdBuilder().findRecord(transactionId);
			if(recShow != null)
			{
				currTab.showPage(recShow.getPageno(), true);
			}
			else
			{
				currTab.showPage(1, true);
			}
		}
	}
	
	public void onClose(boolean a_bIsAlways) throws Exception
	{
		Component selComponent = getSelectedComponent();
		((FilingView)selComponent).removeView();
		remove(selComponent);
	}
	
	public void onExit() throws Exception
	{
		try
		{
			while(getSelectedComponent() != null)
			{
				onClose(true);
				Thread.sleep(3000);
			}
		}
		catch(Exception e)
		{
			logger.warn("Error while closing",e);
		}
		((FECPrintMainWindow)Utility.getMainWnd()).closeApp();
	}
	
	
	public void onOpen() throws Exception
	{
				
		JFileChooser file = new JFileChooser(FECPrintMainWindow.getMostRecentDocumentDir());
		FECFileFilter filter = new FECFileFilter(); 
		filter.addExtension("fec"); 
		filter.setDescription("FEC Filings"); 
		file.setFileFilter(filter); 
		int returnVal = file.showOpenDialog(this);				
		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			File fl = file.getSelectedFile();
			int n = getTabCount();
			for(int i =0 ; i < n ; i++)
			{
				String fl2 =  getComponentAt(i).getName();
				if(fl2 != null && fl.getName().equals(fl2))
					return;
			}			
			openFile(fl);			
		}
	}
	
	public void openFile(File dataFile) throws Exception
	{
		Utility.getMainWnd().setCursor(new Cursor(Cursor.WAIT_CURSOR));
		JSplitPane splitPane = new FilingView(dataFile);
		splitPane.setOneTouchExpandable(true);
		addTab(dataFile.getName(),splitPane);						
		setSelectedComponent(splitPane);
		setToolTipTextAt(getTabCount() - 1, dataFile.getAbsolutePath());
		Utility.getMainWnd().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
	}
	
	public void onPrint() throws Exception
	{
		/*PrintUtilities printUtil = new PrintUtilities(this);
		HashPrintRequestAttributeSet printParams = printUtil.print();
		if(printParams != null)
		{
			PageRanges pageRanges = (PageRanges) printParams.get(PageRanges.class);
			int[][] ranges = {{1, Integer.MAX_VALUE}};;			
			if(pageRanges != null)
			{
				ranges = pageRanges.getMembers();
			}			 
			FilingView selFilingView = (FilingView)getSelectedComponent();		
			selFilingView.generatePrintPreview(ranges);
		}
		*/
		FilingView selFilingView = (FilingView)getSelectedComponent();
		PrintRangeDialog printdlg = new PrintRangeDialog(FECPrintMainWindow.l_MainFrame, selFilingView.getTotalPages());
		printdlg.setVisible(true);
		if(printdlg.getAction() == PrintRangeDialog.ACTION_OK)
		{
			int[][] ranges = {{1, Integer.MAX_VALUE}};	
			if(printdlg.getPageRangeOptionSelected() == PrintRangeDialog.RANGE_USER_DEFINED)
			{
				ranges[0][0] = printdlg.getStartPageNo();
				ranges[0][1] = printdlg.getEndPageNo();
			}
			selFilingView.generatePrintPreview(ranges);
		}
	}
	
	class WindowListenerFecPrint extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{		
			try
			{
				onExit();			
			}
			catch(Exception eX)
			{
				logger.warn("Error in closing the window",eX);
				
			}			
		}
	}

	public JTextField getPagenoField() {
		return pagenoField;
	}

	public JTextField getTotalPages() {
		return totalPages;
	}

	//ChangeListener methods
	public void stateChanged(ChangeEvent arg0) 
	{
		if(this.getSelectedComponent() != null)
		{
			FilingView currTab = (FilingView)(this.getSelectedComponent());
			try
			{
				pagenoField.setText((String.valueOf(currTab.getCurrentPageInView())));
				totalPages.setText(String.valueOf(currTab.getPagesMgr().getTotalPages()));
				
			}
			catch(NumberFormatException eNum)
			{
				logger.debug(eNum);
			}
		}
	}
	
	public void fireStateChanged()
	{
		super.fireStateChanged();
	}

	public void keyPressed(java.awt.event.KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(java.awt.event.KeyEvent arg0) {
		if(arg0.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
		{
			if(this.getSelectedComponent() != null)
			{
				FilingView currTab = (FilingView)(this.getSelectedComponent());
				try
				{
					int pageNo = Integer.parseInt(pagenoField.getText());
					currTab.showPage(pageNo, true);
				}
				catch(NumberFormatException eNum)
				{
					logger.debug(eNum);
				} 
				catch (IOException e) 
				{					
					logger.debug(e);
				} 
				catch (DocumentException e) 
				{					
					logger.debug(e);
				}
			}
		}
		
	}

	public void keyTyped(java.awt.event.KeyEvent arg0) {
		
	}
}
	