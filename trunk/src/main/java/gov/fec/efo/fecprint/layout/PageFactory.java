package gov.fec.efo.fecprint.layout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.fec.efo.fecprint.data.BaseRecordType;
import gov.fec.efo.fecprint.paginate.PaginationProperties;
import gov.fec.efo.fecprint.utility.AppProperties;

public class PageFactory {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private static PageFactory singleton;
	private HashMap<String, Map> pageManagers;
	
	static
	{
		if(singleton == null)
		{
			singleton = new PageFactory();
		}
	}
	
	private PageFactory()
	{
		pageManagers = new HashMap<String, Map>();
	}
	
	public static PageFactory getInstance()
	{
		return singleton;		
	}
	
	public Page getNewPage(BaseRecordType formType, BaseRecordType pageType, long pageNum,PageManager mgr) throws IOException
	{
		String key = formType.name() + "#" + pageType.name();
		if(pageManagers.get(key) == null)
		{
			logger.debug("Getting a mapping file :" + key);
			String dir = AppProperties.getMappingsDirectory();
			if(dir == null)
			{
				dir = "";
			}
			else
			{
				dir = dir + File.separator;
			}
			File fl = new File(dir + formType.name() + File.separator + pageType.name() + ".properties");
			
			if(fl.exists() == false)
			{
				fl = new File(dir + pageType.name() + ".properties"); 
			}
			
			logger.debug("Mapping file path detected :" + fl);
			if(fl.exists())
			{
				Map props = loadProperties(fl);
				pageManagers.put(key, props);
				
			}
			
		}
		Map mapping = pageManagers.get(key);
		return new Page(mapping, pageNum,PaginationProperties.valueOf(pageType.name()),mgr);
	}
	
	private Map loadProperties(File fl) throws IOException
	{
		Properties props = new Properties();
		FileInputStream instream = null;
		Map m = new HashMap();
		try {
			instream = new FileInputStream(fl);
			props.load(instream);
			Enumeration en = props.propertyNames();
			while(en.hasMoreElements())
			{
				String name = (String)(en.nextElement());				
				//m.put(name,Integer.valueOf(props.getProperty(name)));
				m.put(name,FieldProperties.parse(props.getProperty(name)));
			}
			
		} 
		finally
		{
			
			if(instream != null)
				try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e);
				}
		}
		return m;

	}

}
