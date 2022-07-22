package gov.fec.efo.fecprint.gui;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teamdev.jxbrowser.Browser;
import com.teamdev.jxbrowser.BrowserFactory;
import com.teamdev.jxbrowser.BrowserType;

class PageView extends JPanel
{
	String pdfDir;
	Browser browser;
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	public PageView(String dir)
	{
		super();
		pdfDir = dir;
		setLayout(new BorderLayout());
		//BrowserFactory.setDefaultBrowserType(BrowserType.IE);
		browser = BrowserFactory.createBrowser();		
		browser.navigate(pdfDir + File.separator + "1.pdf");
		add(browser.getComponent());
	}
	
	public void showPage(int pageNo)
	{
		logger.debug("Enter showPage.Showing page " + pageNo);
		browser.navigate(pdfDir + File.separator + pageNo + ".pdf");
		logger.debug("Done showPage.Done showing page " + pageNo);
	}
	
	public void removeView()
	{
		browser.navigate("");
	}
	
}