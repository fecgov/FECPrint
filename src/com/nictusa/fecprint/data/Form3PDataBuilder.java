package com.nictusa.fecprint.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Form3PDataBuilder {
	
	DataBuilder core = null;
	public Form3PDataBuilder(DataBuilder base)
	{
		this.core = base;
	}

	public void buildData(Map<RecordType,Vector<Record>> recordBuckets)
	{
		/*if(recordBuckets.get(RecordType.F3PS) != null)
		{
			Record summary = recordBuckets.get(RecordType.F3PS).get(0);
			RecordHelper.expandArrayTo(90, summary.getData());
			Record form = core.getCoverPage();
			ArrayList<String> summaryFromCover = RecordHelper.getDataSliceIfNotEmpty(2,202,form.getData());			
			summary.getData().addAll(summaryFromCover);
		}*/
		
		if(recordBuckets.get(RecordType.SC_11) != null)
		{
			RecordHelper.mergeSC2RecordWithParent(recordBuckets.get(RecordType.SC_11), RecordType.SC2_11);
		}
		if(recordBuckets.get(RecordType.SC_12) != null)
		{
			RecordHelper.mergeSC2RecordWithParent(recordBuckets.get(RecordType.SC_12), RecordType.SC2_12);
		}
	}
}
