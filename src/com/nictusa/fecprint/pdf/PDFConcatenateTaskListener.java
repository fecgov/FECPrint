package com.nictusa.fecprint.pdf;


public interface PDFConcatenateTaskListener {
	
	void concateCompleted(boolean status);	
	
	void concateProgress(int pageProcessedSoFar);
}
