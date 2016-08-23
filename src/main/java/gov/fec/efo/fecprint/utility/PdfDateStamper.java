//package gov.fec.efo.fecprint.utility;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class PdfDateStamper {

	public static void main(String[] args) throws Exception {

		if (args.length < 3) {
			System.out.println("Insufficient arguments");
			System.exit(-1);
		}

		String inputFileName = "";
		String outputFileName = "";
		boolean doStampTime = false;
		String strTimeStamp = "";
		boolean doStampImageNo = false;
		long lImageNo = 0L;

		for (int l = 0; l < args.length; l++) {
			if (l == 0) {
				inputFileName = args[l];
			} else if (args[l].equals("-t")) {
				doStampTime = true;
				File fl = new File(inputFileName);
				//strTimeStamp = fl.getName().substring(16, 18) + "/" + fl.getName().substring(18, 20) + "/" + fl.getName().substring(12, 16) + "  " + fl.getName().substring(20, 22) + " : " + fl.getName().substring(22, 24);
				strTimeStamp = fl.getName().substring(23, 25) + "/" + fl.getName().substring(25, 27) + "/" + fl.getName().substring(19, 23) + "  " + fl.getName().substring(27, 29) + " : " + fl.getName().substring(29, 31);
			} else if (args[l].equals("-i")) {
				doStampImageNo = true;
				File fl = new File(inputFileName);
				lImageNo = Long.parseLong(fl.getName().substring(0, 18));
			} else if (args[l].equals("-o")) {
				l++;
				if (l < args.length) {
					outputFileName = args[l];
				}
			}
		}

		PdfReader reader = new PdfReader(inputFileName);
		int n = reader.getNumberOfPages();

		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(outputFileName));

		for (int i = 1; i <= n; i++) {

			PdfContentByte cb = stamp.getOverContent(i);
			PdfTemplate template = cb.createTemplate(25.0F, 25.0F);
        
			cb.addTemplate(template, 7.0F, 757.0F);
        
			BaseFont bf2 = BaseFont.createFont("Helvetica", "Cp1252", false);
			cb.beginText();
			cb.setFontAndSize(bf2, 10.0F);
			if (doStampTime) {
				cb.showTextAligned(1, strTimeStamp, 550.0F, 775.0F, 0.0F);
			}

			if (doStampImageNo) {
				cb.showTextAligned(1, "Image# " + (lImageNo + i - 1L), 105.0F, 775.0F, 0.0F);
			}
			cb.endText();
		}
		stamp.close();
		System.exit(0);
	}

}
