package gov.fec.efo.fecprint.paginate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.fec.efo.fecprint.data.BaseRecordType;
import gov.fec.efo.fecprint.data.DataBuilder;
import gov.fec.efo.fecprint.data.Record;
import gov.fec.efo.fecprint.data.RecordType;
import gov.fec.efo.fecprint.utility.AppProperties;

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
		String imgnofile = null;
		String outfile = null;
		PrintWriter outimgno = null;
		RandomAccessFile startImgNoFile = null;
		FileLock fLock = null;
		
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
					if (args[i].startsWith("imgnofile=")) 
					{
						imgnofile = args[i].substring(args[i].indexOf("=") + 1);
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
				
				File imagenoFile = new File(imgnofile);
				if (imagenoFile.exists() == false) 
				{
					//System.out.println("File having starting image number not found : " + imagenoFile.getAbsolutePath());
					usageInfo();
				}				
	
				/*System.out.println("Processing file " + inputFile.getAbsolutePath());
				System.out.println("Lockfile used " + imagenoFile.getAbsolutePath());
				System.out.println("Output file " + outputFile.getAbsolutePath());
				System.out.println("Started at " + new Date());*/			
			
				DataBuilder dataBuilder = new DataBuilder(inputFile.getAbsolutePath());
				int tot = dataBuilder.getTotalLines();
				long imagenumbers[] = new long[tot];
				Pagination.getAllImageNumbers(dataBuilder.getRecordBuckets(),
						imagenumbers);
				
				startImgNoFile = new RandomAccessFile(imagenoFile,"rws");
				fLock = startImgNoFile.getChannel().lock();
				if(fLock != null)
				{
					String line = startImgNoFile.readLine();
					if(line != null && StringUtils.isBlank(line) == false && line.trim().length() == 11)
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
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			returnValue = 1;
		} 
		finally 
		{
			try
			{
				//System.out.println("Ended at " + new Date());
				if (outimgno != null) 
				{
					outimgno.close();
				}
				if(fLock != null && fLock.isValid())
				{
					fLock.release();
				}
				if(startImgNoFile != null)
				{
					startImgNoFile.close();			 
				}				
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				returnValue = 1;
			}
			
		}
		System.exit(returnValue);
		
	}

	private static void usageInfo() 
	{
		System.out
				.println("java -Xms512m -Xmx1024m -classpath <FECPrint jar path> gov.fec.efo.fecprint.paginate.Pagination imgnofile=<lockfile pathname> outfile=<image number out file pathname> <datafile pathname>");
		System.exit(1);
	}

}
