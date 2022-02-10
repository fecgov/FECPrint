package gov.fec.efo.fecprint.utility;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageCount {

	private static final Logger logger = LoggerFactory.getLogger(PageCount.class);

	public static void main(String[] args) throws Exception {

		PdfReader reader = null;
		try {
			RandomAccessFileOrArray file = new RandomAccessFileOrArray(args[0], false, true);
			reader = new PdfReader(file, null);
			System.out.println(reader.getNumberOfPages());
			System.exit(0);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.exit(-1);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				System.exit(-1);
			}
		}

	}
}
