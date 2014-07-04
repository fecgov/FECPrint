package gov.fec.efo.fecprint.data;

import java.util.Map;
import java.util.Vector;

public class Form6DataBuilder {

	DataBuilder core = null;

	public Form6DataBuilder(DataBuilder base) {
		this.core = base;
	}

	public void buildData(Map<RecordType, Vector<Record>> recordBuckets) {
		if (recordBuckets.get(RecordType.F65) != null) 
		{
			mergeF65RecordWithParent(core.getCoverPage(), recordBuckets.get(RecordType.F65));
			
			if(recordBuckets.get(RecordType.F65).size() == 0)
			{
				recordBuckets.remove(RecordType.F65);
			}
		}				
	}
	
	private void mergeF65RecordWithParent(Record form, Vector<Record> vF65)
	{		
		if(vF65 != null && vF65.size() > 0)
		{
			RecordHelper.expandArrayTo(24, form.getData());
			for(int z = 0 ; z < 5 && vF65.size() > 0; z++)
			{
				Record rF65 = vF65.get(0);							
				RecordHelper.expandArrayTo(20, rF65.getData());
				form.getData().addAll(rF65.getData());
				form.addToMergedRecords(vF65.remove(0));
			}
		}
	}

}
