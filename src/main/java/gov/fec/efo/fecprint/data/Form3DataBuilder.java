package gov.fec.efo.fecprint.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Form3DataBuilder {
	
	DataBuilder core = null;
	public Form3DataBuilder(DataBuilder base)
	{
		this.core = base;
	}

	public void buildData(Map<RecordType,Vector<Record>> recordBuckets)
	{
		if(recordBuckets.get(RecordType.F3S) != null)
		{
			Record summary = recordBuckets.get(RecordType.F3S).get(0);
			RecordHelper.expandArrayTo(35, summary.getData());
			Record form = core.getCoverPage();
			ArrayList<String> summaryFromCover = RecordHelper.getDataSliceIfNotEmpty(24,93,form.getData());			
			summary.getData().addAll(summaryFromCover);
		}
		
		if(recordBuckets.get(RecordType.F3Z1) != null && recordBuckets.get(RecordType.F3Z2) != null)
		{
			/*if((recordBuckets.get(RecordType.F3Z1).size()% 2) == 0)
			{
				Record dummyF3Z1 = new Record(-1, RecordType.F3Z1, RecordHelper.getDataSliceIfNotEmpty(0,6,recordBuckets.get(RecordType.F3Z1).get(0).getData()));
				recordBuckets.get(RecordType.F3Z1).add(dummyF3Z1);
			}*/
			recordBuckets.get(RecordType.F3Z1).addAll(recordBuckets.get(RecordType.F3Z2));
			recordBuckets.remove(RecordType.F3Z2);
		}
		
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
