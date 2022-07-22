package gov.fec.efo.fecprint.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Form1DataBuilder {

	public void buildData(Map<RecordType,Vector<Record>> recordBuckets)
	{
		if(recordBuckets.get(RecordType.F1S) != null)
		{
			Vector<Record> vForm = recordBuckets.get(RecordType.F1A);
			if(vForm == null)
			{
				vForm = recordBuckets.get(RecordType.F1N);
			}			
			Record form = vForm.get(0);
			
			Vector<Record> vF1S = recordBuckets.get(RecordType.F1S);
			ArrayList<Record> additionalJF = new ArrayList<Record>();
			ArrayList<Record> additionalAff = new ArrayList<Record>();
			ArrayList<Record> additionalDA = new ArrayList<Record>();
			ArrayList<Record> additionalBank = new ArrayList<Record>();
			
			for(int i = 0 ; i < vF1S.size(); i++)
			{
				
				ArrayList dataJF = RecordHelper.getDataSliceIfNotEmpty(2,3,vF1S.get(i).getData()); //Additional Joint Fundraiser
				if(dataJF != null)
				{
					additionalJF.add(new Record(vF1S.get(i).getLineno(), RecordType.F1S, dataJF));
				}
				
				ArrayList dataAff = RecordHelper.getDataSliceIfNotEmpty(4,17,vF1S.get(i).getData()); //Additional Affiliates
				if(dataAff != null)
				{
					additionalAff.add(new Record(vF1S.get(i).getLineno(), RecordType.F1S, dataAff));
				}
				
				ArrayList dataDA = RecordHelper.getDataSliceIfNotEmpty(18,29,vF1S.get(i).getData()); //Additional Designated Agents
				if(dataDA != null)
				{
					additionalDA.add(new Record(vF1S.get(i).getLineno(), RecordType.F1S, dataDA));
				}
				
				ArrayList dataBank = RecordHelper.getDataSliceIfNotEmpty(30,35,vF1S.get(i).getData()); //Additional Banks
				if(dataBank != null)
				{
					additionalBank.add(new Record(vF1S.get(i).getLineno(), RecordType.F1S, dataBank));
				}
			}			
			vF1S.clear();
			
			if(additionalJF.size() > 0)
			{
				RecordHelper.expandArrayTo(101, form.getData());
				for(int i = 0 ; i < 2 && additionalJF.size() > 0 ; i++ )
				{
					form.getData().addAll(additionalJF.get(0).getData());
					form.addToMergedRecords(additionalJF.remove(0));
				}
			}
			
			while(additionalJF.size() > 0 || additionalAff.size() > 0 || additionalDA.size() > 0 || additionalBank.size() > 0 )
			{
				
				ArrayList<String> recData = new ArrayList<String>();
				Record rec = new Record(-1, RecordType.F1S, recData);
				recData.add("F1S");
				recData.add(form.getData().get(1));
				RecordHelper.expandArrayTo(4, recData);
				if(additionalAff.size() > 0)
				{
					recData.addAll(additionalAff.get(0).getData());
					rec.addToMergedRecords(additionalAff.remove(0));
				}
				else
				{
					RecordHelper.expandArrayTo(18, recData);
				}
				
				if(additionalDA.size() > 0)
				{
					recData.addAll(additionalDA.get(0).getData());
					rec.addToMergedRecords(additionalDA.remove(0));
				}
				else
				{
					RecordHelper.expandArrayTo(30, recData);
				}
				
				if(additionalBank.size() > 0)
				{
					recData.addAll(additionalBank.get(0).getData());
					rec.addToMergedRecords(additionalBank.remove(0));
				}
				else
				{
					RecordHelper.expandArrayTo(36, recData);
				}
				
				if(additionalJF.size() > 0)
				{
					//RecordHelper.expandArrayTo(36, recData);
					for(int i = 0 ; i < 4 && additionalJF.size() > 0 ; i++ )
					{
						recData.addAll(additionalJF.get(0).getData());
						rec.addToMergedRecords(additionalJF.remove(0));	
					}
					//recData.addAll(additionalJF.get(0).getData());
					//rec.addToMergedRecords(additionalJF.remove(0));					
				}
				else
				{
					RecordHelper.expandArrayTo(44, recData);
				}
				
				rec.setLineno(rec.getMergedRecords().get(0).getLineno());
				vF1S.add(rec);
			}
			
			if(vF1S.size() == 0)
			{
				recordBuckets.remove(RecordType.F1S);
			}			
		}
	}
}
