package gov.fec.efo.fecprint.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RecordHelper {
	
	private static Log logger = null;
	static 
	{
		try {
			logger = LogFactory.getLog(Class.forName("gov.fec.efo.fecprint.data.RecordHelper"));
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	}
	
	public static ArrayList<String> getDataSliceIfNotEmpty(int start, int end, ArrayList<String> record)
	{
		ArrayList<String> slice = new ArrayList<String>();
		boolean bFound = false;
		for(int z = start, sz = record.size(); z <= end && z < sz ; z++)
		{
			if(!bFound)
			{
				bFound = !StringUtils.isBlank(record.get(z));
			}
			slice.add(record.get(z));
		}
		if(bFound)
		{
			expandArrayTo(end - start + 1,slice);
			return slice;
		}
		else
		{
			return  null;
		}
	}
	
	public static void expandArrayTo(int neededSize, ArrayList arr)
	{
		if(arr != null)
		{
			if(arr.size() < neededSize)
			{
				for(int t = arr.size() ; t < neededSize ; t++)
				{
					arr.add(null);
				}
			}
			else if(arr.size() > neededSize)
			{
				while(arr.size() > neededSize)
					arr.remove(neededSize);
			}
		}
	}
	
	public static RecordType getRecordType(String name, RecordType formtype) throws IllegalArgumentException
	{
		RecordType t = null;
		try
		{
			t = RecordType.valueOf(name.toUpperCase());
		}
		catch(IllegalArgumentException eInvalidVal)
		{
			try
			{
				t = RecordType.valueOf(name.replace('/', '_').toUpperCase());
			}
			catch(IllegalArgumentException eInvalidValAgain)
			{
				if(formtype != null && name.equalsIgnoreCase(formtype.getType().name()))
				{
					return formtype;
				}
				else
				{
					logger.info("Unknown record " + name, eInvalidValAgain);
					throw new IllegalArgumentException("Unknown record " + name);
				}				
			}
		}
		return t;
	}
	
	public static void mergeSC2RecordWithParent(Vector<Record> vSC, RecordType childType)
	{		
		if(vSC != null && vSC.size() > 0)
		{
			for(int y = 0 ; y < vSC.size(); y++)
			{
				Record rSC = vSC.get(y);
				Map<RecordType, Vector<Record>> children = rSC.getChildRecordBuckets();
				if(children != null && children.get(childType) != null)
				{
					RecordHelper.expandArrayTo(38, rSC.getData());
					Vector<Record> vSC2 = children.get(childType);						
					for(int z = 0 ; z < 4 && vSC2.size() > 0; z++)
					{
						Record rSC2 = vSC2.get(0);							
						RecordHelper.expandArrayTo(17, rSC2.getData());
						rSC.getData().addAll(rSC2.getData());
						rSC.addToMergedRecords(vSC2.remove(0));
					}
					addDummySCRecordForAdditionalSC2(rSC, vSC2);
				}
			}
		}
	}
	
	public static void addDummySCRecordForAdditionalSC2(Record parentSC, Vector<Record> children)
	{		
		if(children.size() > 0)
		{
			Vector<Record> vDummies = new Vector<Record>();	
			do
			{				
				Record dummy = new Record(-1, parentSC.getType(), getDataSliceIfNotEmpty(0, 3, parentSC.getData()));
				RecordHelper.expandArrayTo(38, dummy.getData());
				for(int z = 0 ; z < 4 && children.size() > 0; z++)
				{
					Record rSC2 = children.get(0);							
					RecordHelper.expandArrayTo(17, rSC2.getData());
					dummy.getData().addAll(rSC2.getData());
					dummy.addToMergedRecords(children.remove(0));
				}
				vDummies.add(dummy);
			}while(children.size() > 0);				
			parentSC.getChildRecordBuckets().put(parentSC.getType(), vDummies);
		}
	}


}
