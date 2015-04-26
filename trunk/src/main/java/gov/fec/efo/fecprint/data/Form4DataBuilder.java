package gov.fec.efo.fecprint.data;

import java.util.Map;
import java.util.Vector;


public class Form4DataBuilder {
	
	DataBuilder core = null;
	public Form4DataBuilder(DataBuilder base)
	{
		this.core = base;
	}

	public void buildData(Map<RecordType,Vector<Record>> recordBuckets)
	{
		if(recordBuckets.get(RecordType.SC_9) != null)
		{
			RecordHelper.mergeSC2RecordWithParent(recordBuckets.get(RecordType.SC_9), RecordType.SC2_9);
		}
		if(recordBuckets.get(RecordType.SC_10) != null)
		{
			RecordHelper.mergeSC2RecordWithParent(recordBuckets.get(RecordType.SC_10), RecordType.SC2_10);
		}
	}
}
