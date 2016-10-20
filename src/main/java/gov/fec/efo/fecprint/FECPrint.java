/**
 * 
 */
package gov.fec.efo.fecprint;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.fec.efo.fecprint.gui.FECPrintMainWindow;
import gov.fec.efo.fecprint.pdf.PDFConcatenate;
import gov.fec.efo.fecprint.utility.AppProperties;
import gov.fec.efo.fecprint.utility.ThreadSafeSimpleDateFormat;
import gov.fec.efo.fecprint.utility.Utility;

/**
 * @author Milind
 *
 */
public class FECPrint {

	private final static Log logger = LogFactory.getLog(FECPrint.class);

	public static void main(String[] args) {
		try {

			String fileName = null;
			long startImagingAt = 1;
			String timeStamp = null;
			String gotoTranId = null;
			boolean showWindow = true;
			boolean paginate = false;
			boolean stampAsElectronicallyFiled = false;

			// Program has arguments. No arguments implies the default run mode,
			// which is showing the Java swing window without any view. Used
			// while fecprint is used as a desktop application.
			if (args.length > 0) {
				if (StringUtils.isBlank(args[args.length - 1]) == false) {
					if (args[args.length - 1].equals("-h")) {
						usageInfo();
					} else if (args[args.length - 1].equals("-v")) {
						versionInfo();
					} else {
						fileName = args[args.length - 1].trim();
					}
				}

				logger.info("Started at " + new Date());

				logger.info("No of Program arguments " + args.length);
				for (int r = 0; r < args.length; r++) {
					logger.info("Program argument #" + r + " = " + args[r]);
				}

				// Get all the arguments passed to the program
				for (int i = 0; i < args.length - 1; i++) {
					if (StringUtils.isBlank(args[i]))
						continue;

					if (args[i].startsWith("imageno=")) {
						startImagingAt = Long.parseLong(args[i].substring(args[i].indexOf("=") + 1));
					} else if (args[i].startsWith("timestamp=")) {
						timeStamp = args[i].substring(args[i].indexOf("=") + 1);
						ThreadSafeSimpleDateFormat informat = new ThreadSafeSimpleDateFormat("yyyyMMddHHmm");
						ThreadSafeSimpleDateFormat outformat = new ThreadSafeSimpleDateFormat("MM/dd/yyyy HH : mm");
						timeStamp = outformat.format(informat.parse(timeStamp));
					} else if (args[i].startsWith("goto=")) {
						gotoTranId = args[i].substring(args[i].indexOf("=") + 1);
					} else if (args[i].equals("silent")) {
						showWindow = false;
					} else if (args[i].equals("stampEF")) {
						stampAsElectronicallyFiled = true;
					}
					/*
					 * else if(args[i].equals("paginate")) { paginate = true;
					 * showWindow = false; }
					 */
					else {
						logger.error("Unknown argument passed to the program : " + args[i]);
						usageInfo();
					}
				}
			}

			if (fileName != null) {

				// No UI mode. Used for running as a server process or
				// pagination. Simply generates the pdf/page numbers and quits.
				if (showWindow == false) {

					File inputFile = new File(fileName);
					if (inputFile.exists() == false) {
						System.out.println("Data file not found : " + inputFile.getAbsolutePath());
						logger.error("Data file not found : " + inputFile.getAbsolutePath());
						usageInfo();
					}

					logger.info("Processing file " + inputFile.getAbsolutePath());
					System.out.println("Processing file " + inputFile.getAbsolutePath());

					if (paginate == false) // PDF generation on the server
					{
						logger.info("PDF generation on the server");
						File opDir = new File(AppProperties.getOutputDirectory() + File.separator
								+ inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.')));
						if (opDir.exists()) {
							FileUtils.deleteQuietly(opDir);
						}
						opDir.mkdir();
						int totalPages = 0;
						String ss = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'))
								+ "_allpages.pdf";
						{
							FECPrintHelper helper = new FECPrintHelper(inputFile, startImagingAt, timeStamp);
							totalPages = helper.getTotalPages();
							helper.setStampAsElectronicallyFiled(stampAsElectronicallyFiled);
							int[][] ranges = { { 1, Integer.MAX_VALUE } };
							Thread pageGenThread = helper.generatePdf(ranges, opDir, false, true, null);
							pageGenThread.join();
							
							if (!helper.isStatus()) {
								logger.error("One or more stamper threads failed");
								System.exit(1);
							}
						}
						Utility.freeUpMemory("Completed seperate page generation.Getting ready for concatenation");
						PDFConcatenate concatenator = new PDFConcatenate(opDir.getAbsolutePath(), ss, 1, totalPages);
						concatenator.addPDFConcatenateTaskListener(concatenator);
						concatenator.start();
						concatenator.join();
						
						if (!concatenator.isStatus()) {
							logger.error("One or more stamper threads failed");
							System.exit(1);
						}
					}
					/*
					 * else //Pagination. No pdf's are generated.Only page nos.
					 * { logger.info("Pagination"); PrintWriter outimgno = null;
					 * try { DataBuilder dataBuilder = new
					 * DataBuilder(inputFile.getAbsolutePath()); int tot =
					 * dataBuilder.getTotalLines(); long imagenumbers[] = new
					 * long[tot]; Pagination.getAllImageNumbers(dataBuilder.
					 * getRecordBuckets(),imagenumbers); File fileImageNumbers =
					 * new File(AppProperties.getOutputDirectory() +
					 * File.separator + "imagenumbers_" +
					 * inputFile.getName().substring(0,inputFile.getName().
					 * lastIndexOf('.')) + ".txt"); outimgno = new
					 * PrintWriter(fileImageNumbers); logger.info(
					 * "Image numbers output file is at :" +
					 * fileImageNumbers.getAbsolutePath()); System.out.println(
					 * "Image numbers output file is at " +
					 * fileImageNumbers.getAbsolutePath()); for(int z = 1 ; z <
					 * tot; z++) { if(imagenumbers[z] != 0) {
					 * outimgno.println(imagenumbers[z] - 1 + startImagingAt); }
					 * else { outimgno.println(); } } outimgno.flush(); }
					 * finally { if(outimgno != null) { outimgno.close(); } } }
					 */
					System.exit(0);
				}
				// UI mode. A Java window will pop up and show the view for
				// given data file
				else {
					logger.info("UI mode");
					File inputFile = new File(fileName);
					if (inputFile.exists() == false) {
						System.out.println("Data file not found : " + inputFile.getAbsolutePath());
						usageInfo();
					}
					FECPrintMainWindow.enter(inputFile, gotoTranId);
				}
			} else {
				logger.info("UI mode.Blank window.");
				FECPrintMainWindow.enter(null, null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace(System.err);
			System.exit(1);
		} finally {
			logger.info("Ending at " + new Date());
			System.out.println("Ending at " + new Date());
		}

	}

	private static void usageInfo() {
		System.out.println();
		System.out.println("The JAVA version of FECPrint may be invoked programmatically or run from command line. ");
		System.out.println("");
		System.out.println("USAGE/SYNTAX :");
		System.out.println("FECPrint <absolute data file name>");
		System.out.println("");
		System.out.println("DESCRIPTION:");
		System.out.println(
				"	Minimum JAVA requirement for FECPrint is JAVA5 and available at: http://www.java.com/en/download   ");

		System.out.println("");
		System.out.println("OPTIONS:");

		System.out.println("	-v	Use exclusively to obtain version information.");
		System.out.println("	-h	Displays help/usage information.");
		System.exit(1);
	}

	private static void versionInfo() {
		System.out.println();
		System.out.println("8.1 Build 1");
		System.out.println();
		System.exit(1);
	}

	public void generatePDF(String fileName) throws Exception {

		File inputFile = new File(fileName);
		if (inputFile.exists() == false) {
			logger.error("Data file not found : " + inputFile.getAbsolutePath());
			throw new IOException("Data file not found : " + inputFile.getAbsolutePath());
		}

		logger.info("Processing file " + inputFile.getAbsolutePath());

		File opDir = new File(AppProperties.getOutputDirectory() + File.separator
				+ inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.')));

		if (opDir.exists()) {
			FileUtils.deleteQuietly(opDir);
		}
		opDir.mkdir();
		int totalPages = 0;

		String ss = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.')) + "_allpages.pdf";

		FECPrintHelper helper = new FECPrintHelper(inputFile, 1, null);
		totalPages = helper.getTotalPages();
		helper.setStampAsElectronicallyFiled(false);
		int[][] ranges = { { 1, Integer.MAX_VALUE } };
		Thread pageGenThread = helper.generatePdf(ranges, opDir, false, true, null);
		pageGenThread.join();

		Utility.freeUpMemory("Completed seperate page generation.Getting ready for concatenation");
		PDFConcatenate concatenator = new PDFConcatenate(opDir.getAbsolutePath(), ss, 1, totalPages);
		concatenator.addPDFConcatenateTaskListener(concatenator);
		concatenator.start();
		concatenator.join();
	}
}
