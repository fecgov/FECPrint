package gov.fec.efo.fecprint.paginate;

import gov.fec.efo.fecprint.data.BaseRecordType;
import gov.fec.efo.fecprint.data.DataBuilder;
import gov.fec.efo.fecprint.data.Record;
import gov.fec.efo.fecprint.data.RecordType;
import gov.fec.efo.fecprint.paginate.PaginationProperties;
import gov.fec.efo.fecprint.utility.AppProperties;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.commons.lang.StringUtils;

public class Pagination {

	//protected final Log logger = LogFactory.getLog(getClass());

	public int paginateRecords(DataBuilder db, int startingImageNumber) {
		int pageNum = 0;
		TreeMap<RecordType, Vector<Record>> recordBuckets = db
				.getRecordBuckets();
		pageNum = paginateRecords(startingImageNumber, recordBuckets);
		return pageNum;
	}

	private int paginateRecords(int startingImageNumber,
			Map<RecordType, Vector<Record>> recordBuckets) {
		
		//logger.debug("Enter paginateRecords");
		if (recordBuckets != null && recordBuckets.size() > 0) {
			Set<RecordType> set = recordBuckets.keySet();
			Iterator<RecordType> it = set.iterator();
			while (it.hasNext()) {
				RecordType type = it.next();
				if (type.equals(RecordType.TEXT)) {
					continue; // Text records are treated separately as they are
								// grouped at page level rather then at record
								// level for display purposes.
				}
				Vector<Record> recs = recordBuckets.get(type);
				if (recs == null || recs.size() == 0) {
					continue;
				}
				PaginationProperties pp = PaginationProperties.valueOf(type
						.getType().name());
				if (pp.getNoOfRecordsPerPage() == -1) {
					for (int w = 0; w < recs.size(); w++) {
						Vector<Record> vTextRecs = new Vector<Record>();
						Record r = recs.get(w);
						r.setPageno(startingImageNumber);
						int summaryPageAdjustments = 0;
						if (r.getType().getType() == BaseRecordType.F3
								&& recordBuckets.containsKey(RecordType.F3S)) {
							summaryPageAdjustments = -2;
						}
						else if (r.getType().getType() == BaseRecordType.F3P
								&& recordBuckets.containsKey(RecordType.F3PS)) {
							summaryPageAdjustments = -5;
						}
						startingImageNumber = startingImageNumber
								+ pp.getPageSpan() + summaryPageAdjustments;

						if (r.getChildRecordBuckets() != null
								&& r.getChildRecordBuckets().size() > 0) {
							if (r.getChildRecordBuckets().get(RecordType.TEXT) != null) {
								vTextRecs.addAll(r.getChildRecordBuckets().get(
										RecordType.TEXT));
							}
							startingImageNumber = paginateRecords(
									startingImageNumber, r
											.getChildRecordBuckets());
						}

						if (vTextRecs.size() > 0) {
							PaginationProperties ppText = PaginationProperties
									.valueOf(RecordType.TEXT.getType().name());
							for (int u = 0; u < vTextRecs.size();) {
								for (int v = 0; v < ppText
										.getNoOfRecordsPerPage()
										&& u < vTextRecs.size(); v++, u++) {
									Record rText = vTextRecs.get(u);
									rText.setPageno(startingImageNumber);
									rText.setIndexOnPage(v);
								}
								startingImageNumber++;
							}
						}
					}
				} else {
					boolean breakedForSF = false;
					for (int x = 0; x < recs.size();) {
						Vector<Record> vTextRecs = new Vector<Record>();
						for (int t = 0; t < pp.getNoOfRecordsPerPage()
								&& x < recs.size(); t++, x++) {
							Record r = recs.get(x);
							if (r.getType() == RecordType.SF) {
								if (x > 0) {
									// Avoid the infinite loop as x does not
									// increment on break. Reset the falf to
									// false as you move to next record
									if (breakedForSF == false) {
										//Field# 2 in SF has been updated in f3xDataBuilder, so that it holds combination of deignated flag and designated name
										if (recs.get(x - 1).getData().get(1).equalsIgnoreCase(recs.get(x).getData().get(1)) == false && t > 0) {
											breakedForSF = true;
											break;
										}
									} else
										breakedForSF = false;
								}
							}
							r.setPageno(startingImageNumber);
							r.setIndexOnPage(t);

							if (r.getChildRecordBuckets() != null
									&& r.getChildRecordBuckets().size() > 0) {
								if (r.getChildRecordBuckets().get(
										RecordType.TEXT) != null) {
									vTextRecs.addAll(r.getChildRecordBuckets()
											.get(RecordType.TEXT));
								}
								startingImageNumber = paginateRecords(startingImageNumber + pp.getPageSpan(), r.getChildRecordBuckets());
								startingImageNumber--; // just to cancel out the
														// extra increment;
							}

						}
						startingImageNumber = startingImageNumber + pp.getPageSpan();

						if (vTextRecs.size() > 0) {
							PaginationProperties ppText = PaginationProperties
									.valueOf(RecordType.TEXT.getType().name());
							for (int u = 0; u < vTextRecs.size();) {
								for (int v = 0; v < ppText
										.getNoOfRecordsPerPage()
										&& u < vTextRecs.size(); v++, u++) {
									Record r = vTextRecs.get(u);
									r.setPageno(startingImageNumber);
									r.setIndexOnPage(v);
								}
								startingImageNumber++;
							}
						}
					}
				}
			}
		}
		
		//logger.debug("Done paginateRecords");
		
		return startingImageNumber;
	}
	
	public static void getAllImageNumbers(TreeMap<RecordType,Vector<Record>> recordBuckets, long[] listImageNumbers)
	{
		Set set = recordBuckets.keySet();
		Iterator it = set.iterator();
		while(it.hasNext())
		{
			
			Vector<Record> recs = recordBuckets.get(it.next());			
			for(int x = 0; x < recs.size(); x++)
			{
				if(recs.get(x).getLineno() != -1 && recs.get(x).getPageno() > 0)
				{
					listImageNumbers[recs.get(x).getLineno() - 1] = recs.get(x).getPageno();
				}
				if(recs.get(x).getMergedRecords() != null && recs.get(x).getMergedRecords().size() > 0)
				{
					Vector<Record> merged = recs.get(x).getMergedRecords();
					for(int y =0 ; y < merged.size(); y++)
					{
						if(merged.get(y).getLineno() != -1 && merged.get(y).getPageno() > 0)
						{
							listImageNumbers[merged.get(y).getLineno() - 1] = recs.get(x).getPageno();
						}
					}
				}
				if(recs.get(x).getChildRecordBuckets() != null && recs.get(x).getChildRecordBuckets().size() > 0)
				{
					getAllImageNumbers(recs.get(x).getChildRecordBuckets(),listImageNumbers);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		//System.out.println("Pagination");

		String fileName = null;
		//String imgnofile = null;
		String outfile = null;
		String imageType = null;
		PrintWriter outimgno = null;
		//RandomAccessFile startImgNoFile = null;
		//FileLock fLock = null;
		
		long startImagingAt = 1;
		int returnValue = 0;
		
		try 
		{

			// Program has arguments. No arguments implies the default run mode,which is showing the Java swing window without any view. Used while
			// fecprint is used as a desktop application.
			if (args.length > 0) 
			{
				if (StringUtils.isBlank(args[args.length - 1]) == false) 
				{
					if (args[args.length - 1].equals("-h")) 
					{
						usageInfo();
					} 
					else 
					{
						fileName = args[args.length - 1].trim();
					}
				}
				// Get all the arguments passed to the program
				for (int i = 0; i < args.length - 1; i++) 
				{
					if (args[i].startsWith("imagetype=")) 
					{
						imageType = args[i].substring(args[i].indexOf("=") + 1);
					} 
					else if (args[i].startsWith("outfile=")) {
						outfile = args[i].substring(args[i].indexOf("=") + 1);
					} 
					else {
						//System.out.println("Unknown argument passed to the program : " + args[i]);
						usageInfo();
					}
				}
			}
	
			if (fileName != null) 
			{
				File inputFile = new File(fileName);
				if (inputFile.exists() == false) 
				{
					//System.out.println("Data file not found : " + inputFile.getAbsolutePath());
					usageInfo();
				}
				
				File outputFile = new File(outfile);
				if (new File(outputFile.getParent()).exists() == false) 
				{
					//System.out.println("Output directory not found : " + outputFile.getParent());
					usageInfo();
				}
				
				//File imagenoFile = new File(imgnofile);
				/*if (imagenoFile.exists() == false) 
				{
					//System.out.println("File having starting image number not found : " + imagenoFile.getAbsolutePath());
					usageInfo();
				}*/				
	
				/*System.out.println("Processing file " + inputFile.getAbsolutePath());
				System.out.println("Lockfile used " + imagenoFile.getAbsolutePath());
				System.out.println("Output file " + outputFile.getAbsolutePath());
				System.out.println("Started at " + new Date());*/			
			
				DataBuilder dataBuilder = new DataBuilder(inputFile.getAbsolutePath());
				startImagingAt = getStartImageNumber(dataBuilder, imageType);
				int tot = dataBuilder.getTotalLines();
				long imagenumbers[] = new long[tot];
				Pagination.getAllImageNumbers(dataBuilder.getRecordBuckets(),
						imagenumbers);
				
				//startImgNoFile = new RandomAccessFile(imagenoFile,"rws");
				//fLock = startImgNoFile.getChannel().lock();
				/*if(fLock != null)
				{
					String line = startImgNoFile.readLine();
					if(line != null && StringUtils.isBlank(line) == false && line.trim().length() == 18)
					{
						startImagingAt = Long.parseLong(line.trim());
					}
					else
					{
						//System.out.println("Strating image number not found or not a 11 digit number : " + imagenoFile.getAbsolutePath());
						usageInfo();
					}
					
					startImgNoFile.seek(0);
					startImgNoFile.writeBytes(new Long(startImagingAt + dataBuilder.getTotalPages()).toString());
					
					outimgno = new PrintWriter(outputFile);
					long ending = startImagingAt + dataBuilder.getTotalPages() - 1;
					outimgno.println("" + startImagingAt + "," + ending + "," + tot + "," + dataBuilder.getTotalPages());
					for (int z = 1; z < tot; z++) 
					{
						if (imagenumbers[z] != 0) 
						{
							outimgno.println(imagenumbers[z] - 1 + startImagingAt);
						} 
						else 
						{
							outimgno.println();
						}
					}
					outimgno.flush();
				}*/
				outimgno = new PrintWriter(outputFile);
				long ending = startImagingAt + dataBuilder.getTotalPages() - 1;
				outimgno.println("" + startImagingAt + "," + ending + "," + tot + "," + dataBuilder.getTotalPages());
				for (int z = 1; z < tot; z++) 
				{
					if (imagenumbers[z] != 0) 
					{
						outimgno.println(imagenumbers[z] - 1 + startImagingAt);
					} 
					else 
					{
						outimgno.println();
					}
				}
				outimgno.flush();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			returnValue = 1;
		} 
		finally 
		{
			//System.out.println("Ended at " + new Date());
			if (outimgno != null) 
			{
				outimgno.close();
			}
			/*if(fLock != null && fLock.isValid())
			{
				fLock.release();
			}
			if(startImgNoFile != null)
			{
				startImgNoFile.close();			 
			}*/
			
		}
		System.exit(returnValue);
		
	}
	
	private static long getStartImageNumber(DataBuilder dataBuilder, String imageType) {
		BaseRecordType formType = dataBuilder.getFormType();
		List<String> formData = dataBuilder.getCoverPage().getData();
		long startingImageNumber = 0;
		//Code to make a webservice HTTP request
		//String imageType = "EFILING";
		String comId = "";
		String reportType = "";
		String covStartDate = "";
		String covEndDate = "";
		String wsURL = AppProperties.getFECServicesImageNumberURL();
		try {
			comId = formData.get(1);
			switch(formType)
			{
				case F1:
					break;
				case F24:
					reportType = formData.get(2);
					break;
				case F3:
					reportType = formData.get(11);
					covStartDate = formData.get(15);
					covEndDate = formData.get(16);		
					break;
				case F3X:
					reportType = formData.get(9);
					covStartDate = formData.get(13);
					covEndDate = formData.get(14);
					break;
				case F3P:
					comId = formData.get(1);
					reportType = formData.get(11);
					covStartDate = formData.get(15);
					covEndDate = formData.get(16);
					break;
				case F4:
					reportType = formData.get(10);
					covStartDate = formData.get(11);
					covEndDate = formData.get(12);
					break;
				case F6:
					break;
				case F7:
					reportType = formData.get(9);
					covStartDate = formData.get(12);
					covEndDate = formData.get(13);
					break;
				case F9:
					covStartDate = formData.get(17);
					covEndDate = formData.get(18);
					break;
				case F99:
					break;
				default:
					break;
			}
			
			//disable ssl as there are no valid certs available on test server
			disableSSL();
	        
			
			URL url = new URL(wsURL);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			//add request header
			connection.setRequestMethod("POST");

			String urlParameters = "imageType="+imageType+"&comId="+comId+"&formType="+formType+"&reportType="+reportType+"&covStartDate="+covStartDate+"&covEndDate="+covEndDate+"&noOfPages="+dataBuilder.getTotalPages();
			
			// Send post request
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			//check response code 
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line=br.readLine()) != null) {
                	startingImageNumber = Long.parseLong(line.trim());
                }
            }
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return startingImageNumber;
	}

	private static void disableSSL() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        @Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
		    }
		};
 
		// Install the all-trusting trust manager
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
		
		    sc.init(null, trustAllCerts, new java.security.SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
		    // Create all-trusting host name verifier
		    HostnameVerifier allHostsValid = new HostnameVerifier() {
		        public boolean verify(String hostname, SSLSession session) {
		            return true;
		        }
		    };
		    
		    // Install the all-trusting host verifier
		    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void usageInfo() 
	{
		System.out
				.println("java -Xms512m -Xmx1024m -classpath <FECPrint jar path> gov.fec.efo.fecprint.paginate.Pagination imagetype=<image type> outfile=<image number out file pathname> <datafile pathname>");
		System.exit(1);
	}

}
