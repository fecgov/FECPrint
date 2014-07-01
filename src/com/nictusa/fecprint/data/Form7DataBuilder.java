package com.nictusa.fecprint.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Vector;

public class Form7DataBuilder {

	DataBuilder core = null;

	public Form7DataBuilder(DataBuilder base) {
		this.core = base;
	}

	public void buildData(Map<RecordType, Vector<Record>> recordBuckets) {
		if (recordBuckets.get(RecordType.F76) != null) 
		{
			mergeF76RecordWithParent(core.getCoverPage(), recordBuckets.get(RecordType.F76));
			
			if(recordBuckets.get(RecordType.F76).size() == 0)
			{
				recordBuckets.remove(RecordType.F76);
			}
		}				
	}
	
	private void mergeF76RecordWithParent(Record form, Vector<Record> vF76)
	{		
		if(vF76 != null && vF76.size() > 0)
		{
			RecordHelper.expandArrayTo(22, form.getData());
			for(int z = 0 ; z < 2 && vF76.size() > 0; z++)
			{
				Record rF76 = vF76.get(0);							
				RecordHelper.expandArrayTo(20, rF76.getData());
				form.getData().addAll(rF76.getData());
				form.addToMergedRecords(vF76.remove(0));
			}
		}
	}

}
