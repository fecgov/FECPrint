package gov.fec.efo.fecprint.utility;
import java.awt.Frame;
import java.util.Date;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

import gov.fec.efo.fecprint.gui.MessageDialog;


public class Utility
{
	private static Frame mainWnd = null;
	private static Log logger;
	private static Runtime rn;
	private static ScriptEngine engine;
		
	static 
	{
		try 
		{
			rn = Runtime.getRuntime();		
			logger = LogFactory.getLog(Class.forName("gov.fec.efo.fecprint.utility.Utility"));
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		} 
	}
	
	public static void freeUpMemory(String message)
	{
		logger.info("************************");		
		logger.info(message);
		logger.info("Total memory " + Runtime.getRuntime().totalMemory()/1000000 + " M");
		logger.info("Available Before GC " + Runtime.getRuntime().freeMemory()/1000000 + " M");
		logger.info("Starting GC at " + new Date());
		System.gc();
		logger.info("Ending GC at " + new Date());
		logger.info("Available After GC " + Runtime.getRuntime().freeMemory()/1000000 + " M");
		logger.info("###########################");
	}
	
	public static void setMainWnd(Frame wnd)
	{
		mainWnd = wnd;
	}
	
	public static Frame getMainWnd()
	{
		return mainWnd;
	}
	
	public static Object evalExpression(String str) 
	{
		
		try
		{
			if(engine == null)
			{
				//create a script engine manager
		        ScriptEngineManager factory = new ScriptEngineManager();
		        // create a JavaScript engine
		        ScriptEngine engine = factory.getEngineByName("JavaScript");
	
				synchronized(engine)
				{
					Object o = engine.eval(str);
					return o;
				}
			}
		}
		catch(ScriptException eS)
		{
			logger.error("Error while evaluation script : " + str ,eS);
		}
		return null;
		
	}
	
  public static void reportError(Exception e,boolean popupMessage)
  {
	  	logger.error(e);	  	
		if (popupMessage)
		{
			MessageDialog dlg = new MessageDialog(Utility.getMainWnd(),e.getClass().getName(),e.getMessage() + "\n Please report this error to techsupport@egov.com");
			dlg.setVisible(true);	
		}
	}
  
}