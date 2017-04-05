package gov.fec.efo.fecprint.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadSafeSimpleDateFormat {

	private DateFormat df;

	public ThreadSafeSimpleDateFormat(String format) {
		this.df = new SimpleDateFormat(format);
	}

	public synchronized String format(Date date) {
		return df.format(date);
	}

	public synchronized Date parse(String string) throws ParseException {
		return df.parse(string);
	}

}
