package com.nictusa.fecprint.gui;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nictusa.fecprint.utility.AppProperties;
import com.nictusa.fecprint.utility.Utility;

public class FECPrintMainWindow extends JFrame implements ActionListener, ChangeListener
{
	static  FECPrintMainWindow l_MainFrame ;
	static Vector<String> mruFiles = new Vector<String>(); 
	static Vector<File> tempDirs = new Vector<File>();
	static String  mostRecentDocumentDir = null;
	private static Log logger;	
	private FilingTabs cl = null;
	private JToolBar toolbar = null;
	private JMenuItem  m_MenuItemPrint = null;
	private JMenuItem  m_MenuItemClose = null;
	
	static 
	{
		try {
			logger = LogFactory.getLog(Class.forName("com.nictusa.fecprint.gui.FECPrintMainWindow"));
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	}
	
	public FECPrintMainWindow(String a_Title) throws Exception
	{
		super(a_Title);
		cl = new FilingTabs(this);
	}
  
  public static void main(String[] args)
  {
	  try {
		enter(null, null);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public static void enter(File dataFileName, String goToTranId) throws Exception
  {
	  try
	  {
	     l_MainFrame = new FECPrintMainWindow("FECPrint");		 
		 Utility.setMainWnd(l_MainFrame);
		 l_MainFrame.showWindow();
		 if(dataFileName != null)
		 {
			 l_MainFrame.cl.openFile(dataFileName);
			 if(goToTranId != null)
			 {
				 l_MainFrame.cl.findAndShowRecord(goToTranId);
			 }
		 }
		 l_MainFrame.updateUIControls();
	  }
	  catch (Exception e)
	  {
	  	 Utility.reportError(e,true);
	  	 throw e;
	  }  
  }
  
  void showWindow() throws Exception
  {

  	   //Create File Menu
	  JMenu file = new JMenu ("File");	  
	  file.setMnemonic(java.awt.event.KeyEvent.VK_F);

	  JMenuItem  m_MenuItemOpen = null;	  
	  
	  m_MenuItemOpen = file.add (new JMenuItem ("Open"));
	  m_MenuItemOpen.setActionCommand("Open");
      m_MenuItemOpen.addActionListener (cl);
      m_MenuItemOpen.setMnemonic(java.awt.event.KeyEvent.VK_O);
      
      m_MenuItemPrint = file.add (new JMenuItem ("Print"));
      m_MenuItemPrint.setActionCommand("Print");
      m_MenuItemPrint.addActionListener (cl);
      m_MenuItemPrint.setMnemonic(java.awt.event.KeyEvent.VK_P);
      
      
      m_MenuItemClose = file.add (new JMenuItem ("Close"));
      m_MenuItemClose.setActionCommand("Close");
      m_MenuItemClose.addActionListener (cl);
      m_MenuItemClose.setMnemonic(java.awt.event.KeyEvent.VK_C);
      
      
      file.addSeparator();
      
      JMenuItem m_MenuItemExit = file.add (new JMenuItem ("Exit"));
      m_MenuItemExit.setActionCommand("Exit");
      m_MenuItemExit.addActionListener (cl);
      m_MenuItemExit.setMnemonic(java.awt.event.KeyEvent.VK_X);
      
      JMenu help = new JMenu ("Help");	  
	  JMenuItem  m_MenuItemHelp = null;	  
	  
	  m_MenuItemHelp = help.add (new JMenuItem ("About FECPrint"));
	  m_MenuItemHelp.setActionCommand("About");
	  m_MenuItemHelp.addActionListener (this);    	
      
	  
	  addMRUFiles_Menu(file);	  
	       
	  	    
    JMenuBar mb = new JMenuBar();
    mb.setBorderPainted(true);
    mb.add (file);
    mb.add(help);
    setJMenuBar (mb);
    
    toolbar = new JToolBar();
    GridBagLayout layout = new GridBagLayout();
    toolbar.setLayout(layout);
    
    GridBagConstraints c = new GridBagConstraints();
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;
    c.ipadx = 10;
    
    JLabel  label = new JLabel("   ");
    layout.setConstraints(label, c);
    toolbar.add(label);

    label = new JLabel("Page");
    layout.setConstraints(label, c);
    toolbar.add(label);
    
    layout.setConstraints(cl.getPagenoField(), c);    
    toolbar.add(cl.getPagenoField());
    
    label = new JLabel("  OF ");
    layout.setConstraints(label, c);
    toolbar.add(label);
    
    
    layout.setConstraints(cl.getTotalPages(), c);
    toolbar.add(cl.getTotalPages());
    
    toolbar.addSeparator();
    
    
    c.ipadx = 1;
    c.ipady = 1;
    JButton gotoButton = new JButton(new ImageIcon("Navigation 2 Go.png"));
    gotoButton.setToolTipText("Go To Page number");
    layout.setConstraints(gotoButton, c);
    toolbar.add(gotoButton);
    gotoButton.setActionCommand("go");
    gotoButton.addActionListener(cl);
    
    label = new JLabel(" ");
    layout.setConstraints(label, c);
    toolbar.add(label);
    
    
    toolbar.addSeparator();
    toolbar.addSeparator();
    toolbar.addSeparator();
    toolbar.addSeparator();
    toolbar.addSeparator();
    toolbar.addSeparator();
    toolbar.addSeparator();
    
    label = new JLabel(" ");
    layout.setConstraints(label, c);
    toolbar.add(label);
    
    
    JButton navigation = new JButton(new ImageIcon("Collapse Left.png"));
    navigation.setToolTipText("First Page");
    layout.setConstraints(navigation, c);
    navigation.setActionCommand("first");
    navigation.addActionListener(cl);
    toolbar.add(navigation);
    
    
    
    navigation = new JButton(new ImageIcon("Navigation 2 Left.png"));
    navigation.setToolTipText("Previous Page");
    layout.setConstraints(navigation, c);
    navigation.setActionCommand("previous");
    navigation.addActionListener(cl);
    toolbar.add(navigation);
    
    
    navigation = new JButton(new ImageIcon("Navigation 2 Right.png"));
    navigation.setToolTipText("Next Page");
    layout.setConstraints(navigation, c);
    navigation.setActionCommand("next");
    navigation.addActionListener(cl);
    toolbar.add(navigation);
    
    navigation = new JButton(new ImageIcon("Collapse Right.png"));
    navigation.setToolTipText("Last Page");
    layout.setConstraints(navigation, c);
    navigation.setActionCommand("last");
    navigation.addActionListener(cl);
    toolbar.add(navigation);
    
    
    c.weightx = 2.0;    
    label = new JLabel(" ");
    layout.setConstraints(label, c);
    toolbar.add(label);
    
    toolbar.setFloatable(false);
    
    enableEvents (AWTEvent.WINDOW_EVENT_MASK);    
	enableEvents (AWTEvent.KEY_EVENT_MASK);    
    
    setLayout(new BorderLayout());
	
	l_MainFrame.add(toolbar,BorderLayout.PAGE_START);   	    
    
	l_MainFrame.add(cl,BorderLayout.CENTER);   
    
    
	ImageIcon icon = new ImageIcon("Printer.png");
	l_MainFrame.setIconImage(icon.getImage());

	
	l_MainFrame.setLocation(0, 0);
	
	l_MainFrame.setPreferredSize(new Dimension(800,600));
	
	l_MainFrame.pack();
	
	l_MainFrame.setExtendedState(MAXIMIZED_BOTH);
    
	l_MainFrame.setVisible(true);	  
	  
  }
  
	public void closeApp() throws Exception
	 {
	 		FileOutputStream out = null;
			try
			{
				saveMRUList();
				
				for(int i = 0 ; i < tempDirs.size(); i++)
				{
					FileUtils.deleteQuietly(tempDirs.get(i));
				}
				
				l_MainFrame.setVisible(false);						 	     
				System.exit(0);
			}
			finally
			{
				try
				{
					if(out != null)
						out.close();
				}
				catch(IOException eIO){}					
			}
			
		}
		
		private void saveMRUList() throws Exception
		{
			FileOutputStream out = null;
			try
			{
				Properties mru = new Properties();
				
				
				int sz = mruFiles.size();
				for(int j =0;j < 4 && j < mruFiles.size();j++)
				{
					mru.put("MRU_" + String.valueOf(j),mruFiles.elementAt(j));
				}				
				
				out = new FileOutputStream(AppProperties.getMruDirectory() + File.separator + "MRU.properties");
				mru.store(out,"Most Recently used files list");
				out.close();
			}
			finally
			{
				try
				{
					if(out != null)
						out.close();
				}
				catch(IOException eIO){}	
			}	
		}
		
	private void addMRUFiles_Menu(JMenu file) throws Exception {
		FileInputStream in = null;
		try {
			File fMru = new File(AppProperties.getMruDirectory()
					+ File.separator + "MRU.properties");
			if (fMru.exists()) {
				Properties mru = new Properties();
				in = new FileInputStream(AppProperties.getMruDirectory()
						+ File.separator + "MRU.properties");
				mru.load(in);

				file.addSeparator();
				String s;
				JMenuItem m_MenuItem = null;
				for (int i = 0; i < mru.size(); i++) {
					s = "MRU_" + String.valueOf(i);

					File flMru = new File((String) mru.get(s));
					if (i == 0) {
						setMostRecentDocumentDir(flMru.getParent());
					}
					if (flMru.exists()) {
						m_MenuItem = file
								.add(new JMenuItem((String) mru.get(s)));
						m_MenuItem.setActionCommand((String) mru.get(s));
						m_MenuItem.addActionListener(cl);
						mruFiles.add(flMru.getAbsolutePath());
					}
				}
			}
		} finally {
			if (in != null)
				in.close();
		}
	}
		  

		public static void addFileToMRU(File inputFile) 
		{
			if(mruFiles.indexOf(inputFile.getAbsolutePath()) != -1)
			{
				mruFiles.remove(inputFile.getAbsolutePath());
				
			}	
			mruFiles.insertElementAt(inputFile.getAbsolutePath(),0);
			setMostRecentDocumentDir(inputFile.getParent());
			
		}

		public static Vector<File> getTempDirs() {
			return tempDirs;
		}

		public static String getMostRecentDocumentDir() {
			return mostRecentDocumentDir;
		}

		public static void setMostRecentDocumentDir(String mostRecentDocumentDir) {
			FECPrintMainWindow.mostRecentDocumentDir = mostRecentDocumentDir;
		}
		
		public void actionPerformed(ActionEvent e)
		{
				try
				{
					if(e.getActionCommand().equals("About"))
					{
						//MessageDialog dlg = new MessageDialog(this,"About FECPrint 8.1","Viewer for FEC Electronic Filing\n\nCopyright \u00a9 2013 Federal Election Commission. All rights reserved.");
						MessageDialog dlg = new MessageDialog(this,"About FECPrint 8.1","Viewer for FEC Electronic Filing\n\nFor technical support, please contact: ELECTRONIC FILING OFFICE, FEC\nDirect dial: 202-694-1642, Toll free: 1-800-424-9530 x 1642");
						
						dlg.setVisible(true);	
					}
				}
				catch(Exception excpt)
				{
					logger.error(excpt);
				}
		}

		public void stateChanged(ChangeEvent arg0) {
			
			updateUIControls();
		}

		private void updateUIControls() {
			if(cl.getSelectedComponent() == null)
			{
				toolbar.setVisible(false);
				m_MenuItemPrint.setEnabled(false);
				m_MenuItemClose.setEnabled(false);
			}
			else
			{
				toolbar.setVisible(true);
				m_MenuItemPrint.setEnabled(true);
				m_MenuItemClose.setEnabled(true);
			}
		}
	
}

