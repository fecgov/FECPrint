package com.nictusa.fecprint.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Form24DataBuilder {

	public void buildData(Map<RecordType,Vector<Record>> recordBuckets)
	{
		if(recordBuckets.get(RecordType.SE) != null)
		{
			Vector<Record> vSE = recordBuckets.get(RecordType.SE);
			ArrayList<Record> cvrSE = new ArrayList<Record>();
			
			Vector<Record> vForm = recordBuckets.get(RecordType.F24A);
			if(vForm == null)
			{
				vForm = recordBuckets.get(RecordType.F24N);
			}
			Record form = vForm.get(0);
			
			for(int i = 0 ; vSE.size() > 0 && i < 2; i++)
			{
				RecordHelper.expandArrayTo(44,vSE.get(0).getData());
				cvrSE.add(form.addToMergedRecords(vSE.remove(0)));
			}
			
			if(cvrSE.size() > 0)
			{
				RecordHelper.expandArrayTo(16, form.getData());
				for(int i = 0 ; cvrSE.size() > 0 ; i++ )
				{
					form.getData().addAll(cvrSE.get(0).getData());
					cvrSE.remove(0);					
				}
			}
			
			if(recordBuckets.get(RecordType.SE).size() == 0)
			{
				recordBuckets.remove(RecordType.SE);
			}
		}
	}
}
