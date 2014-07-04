package gov.fec.efo.fecprint.pdf;

import org.apache.commons.lang.math.IntRange;

public interface PDFStamperTaskListener {
	
	void stamperTaskCompleted(boolean status, IntRange pageRange);	
	
	void stamperTaskProgress(int pageProcessedSoFar);
}
