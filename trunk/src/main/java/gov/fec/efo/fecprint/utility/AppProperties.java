/**
 * 
 */
package gov.fec.efo.fecprint.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Milind
 *
 */
public class AppProperties {

	private static final String MRU_DIR = "MRU_DIR";
	public static final String PDF_TEMPLATE_DIR = "PDF_TEMPLATE_DIR";
	public static final String MAPPINGS_DIR = "MAPPINGS_DIR";
	public static final String OUTPUT_DIR = "OUTPUT_DIR"; 
	public static final String OUTPUT_CHUNK_SIZE = "OUTPUT_CHUNK_SIZE";
	public static final String MAX_PDF_THREADS = "MAX_PDF_THREADS"; 
	public static final String DELETE_PAGE_FILES_ON_CONCATE = "DELETE_PAGE_FILES_ON_CONCATE";
	public static final String FEC_SERVICES_HOST = "FEC_SERVICES_HOST";
	public static final String FEC_SERVICES_PORT = "FEC_SERVICES_PORT";
	public static final String FEC_SERVICES_PROTOCOL = "FEC_SERVICES_PROTOCOL";
	public static final String VERSION = "VERSION";
	
	private static Properties prop = null; 
	private static Log logger;
	
	static 
	{
		try {
			logger = LogFactory.getLog(Class.forName("gov.fec.efo.fecprint.utility.AppProperties"));
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	}
	static
	{
		FileInputStream instream = null;
		prop = new Properties();
		try {
			File fl = null;
			if(System.getProperty("configfilepathname") != null)
			{
				fl = new File(System.getProperty("configfilepathname"));
			}
			else if(SystemUtils.IS_OS_WINDOWS)
					fl = new File("fecprint.properties");
			else
				fl = new File("fecprint_unix.properties");
			logger.info("Properties path is " + fl.getAbsolutePath());
			instream = new FileInputStream(fl);
			/*try
			{
				instream = new FileInputStream(fl);
			}
			catch(FileNotFoundException eNotFound)
			{
				if(System.getProperty("configfilepathname") != null)
				{
					fl = new File(System.getProperty("configfilepathname"));
				}
				else
				{
					throw eNotFound;
				}
				logger.info("Properties path is " + fl.getAbsolutePath());
				instream = new FileInputStream(fl);
			}*/
			prop.load(instream);
			Enumeration<Object> en = prop.keys(); 
			while(en.hasMoreElements())
			{
				Object okey = en.nextElement();
				String parsedVal = parseVariableAndPutValue((String)(prop.get(okey)));
				prop.put(okey, parsedVal);
			}
		} catch (IOException e) {
			
			logger.error("Error loading application properties",e);
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
	}
	
	public static String getMruDirectory()
	{
		return prop.getProperty(MRU_DIR) != null ? prop.getProperty(MRU_DIR) : "C:/temp";
	}
	
	public static String getPdfTemplateDirectory()
	{
		return prop.getProperty(PDF_TEMPLATE_DIR) != null ? prop.getProperty(PDF_TEMPLATE_DIR) : "./resources";
	}
	
	public static String getOutputDirectory()
	{
		return prop.getProperty(OUTPUT_DIR) != null ? prop.getProperty(OUTPUT_DIR) : "C:/temp";
	}
	
	public static String getMappingsDirectory()
	{
		return prop.getProperty(MAPPINGS_DIR) != null ? prop.getProperty(MAPPINGS_DIR) : "./mapping";
	}
	
	public static int getOutputChunkSize()
	{
		return prop.getProperty(OUTPUT_CHUNK_SIZE) != null ? Integer.parseInt(prop.getProperty(OUTPUT_CHUNK_SIZE)) : 100;
	}
	
	public static int getMaxPdfThreadsAllowed()
	{
		return prop.getProperty(MAX_PDF_THREADS) != null ? Integer.parseInt(prop.getProperty(MAX_PDF_THREADS)) : 3;
	}
	
	public static String getFECServicesProtocol()
	{
		return prop.getProperty(FEC_SERVICES_PROTOCOL) != null ? prop.getProperty(FEC_SERVICES_PROTOCOL) : "https";
	}
	
	public static String getFECServicesHost()
	{
		return prop.getProperty(FEC_SERVICES_HOST) != null ? prop.getProperty(FEC_SERVICES_HOST) : "webforms.fec.gov";
	}
	
	public static String getFECServicesPort()
	{
		return prop.getProperty(FEC_SERVICES_PORT) != null ? prop.getProperty(FEC_SERVICES_PORT) : "";
	}
	
	public static boolean deletePagePdfFilesAfterConcatination()
	{
		return prop.getProperty(DELETE_PAGE_FILES_ON_CONCATE) != null ? BooleanUtils.toBoolean(prop.getProperty(DELETE_PAGE_FILES_ON_CONCATE)) : true;
	}
	
	public static String getVersion()
	{
		return prop.getProperty(VERSION) != null ? prop.getProperty(VERSION) : "8.2";
	}
	
	public static String parseVariableAndPutValue(String initialVal)	
	{
		int u = -1;
		if((u = initialVal.indexOf('%')) != -1)
		{
			String varName = initialVal.substring(u +1 , initialVal.indexOf('%', u + 1));
			String varVal = System.getenv(varName);
			if(varVal != null)
				return varVal;
			else
			{
				varVal = System.getProperty(varName);
				if(varVal != null)
					return varVal;
				else
					return initialVal;
			}
		}
		else
			return initialVal;
	}

}
