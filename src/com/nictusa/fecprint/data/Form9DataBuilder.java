package com.nictusa.fecprint.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Form9DataBuilder {
	
	DataBuilder core = null;

	public Form9DataBuilder(DataBuilder base) {
		this.core = base;
	}


	public void buildData(Map<RecordType,Vector<Record>> recordBuckets)
	{
		if(recordBuckets.get(RecordType.F93) != null)
		{
			Vector<Record> v93 = recordBuckets.get(RecordType.F93);
			Vector<Record> vExpanded93 = new Vector<Record>();
			
			for(int i = 0 ; i < v93.size(); i++)
			{				
				Record rF93 = v93.get(i);
				if(rF93.getChildRecordBuckets() != null && rF93.getChildRecordBuckets().get(RecordType.F94) != null)
				{
					Vector<Record> v94 = rF93.getChildRecordBuckets().get(RecordType.F94);
					Record newF93 = rF93;
					do
					{				
						if(newF93 == null)
						{
							newF93 = new Record(-1, RecordType.F93, RecordHelper.getDataSliceIfNotEmpty(0,2,rF93.getData()));
						}
						RecordHelper.expandArrayTo(25, newF93.getData());
						for(int j = 0 ; j < 3 && v94.size() > 0; j++)
						{
							Record rF94 = v94.get(0);
							RecordHelper.expandArrayTo(16, rF94.getData());
							newF93.getData().addAll(rF94.getData());
							newF93.addToMergedRecords(v94.remove(0));
						}
						
						vExpanded93.add(newF93);
						newF93 = null;						
						
					}while(v94.size() > 0);
				}
				else
				{
					vExpanded93.add(rF93);
				}
			}
			
			if(vExpanded93.size() > 0)
			{
				recordBuckets.remove(RecordType.F93);
				recordBuckets.put(RecordType.F93,vExpanded93);
			}
		}
	}
}
