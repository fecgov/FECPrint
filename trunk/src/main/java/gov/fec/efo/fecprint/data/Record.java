package gov.fec.efo.fecprint.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

public class Record {
	
	int lineno;
	int pageno;
	int indexOnPage;
	RecordType type;
	ArrayList<String> data;
	private TreeMap<RecordType,Vector<Record>> childRecordBuckets;
	private Vector<Record> mergedRecords;
	
	public Record()
	{
		lineno = -1;
		indexOnPage = 0;
		data = new ArrayList<String>();		
	}
	
	public Record(int ln, RecordType t, ArrayList<String> d)
	{
		lineno = ln;
		type = t;
		data = d;
		indexOnPage = 0;
	}

	public int getLineno() {
		return lineno;
	}

	public RecordType getType() {
		return type;
	}

	public ArrayList<String> getData() {
		return data;
	}
	
	public void addChildRecords(RecordType t, Vector<Record> v)
	{
		if(childRecordBuckets == null)
		{
			childRecordBuckets = new TreeMap<RecordType,Vector<Record>>();
		}
		mergeTextRecordsFromChild(v);
		childRecordBuckets.put(t,v);
	}
	
	private void mergeTextRecordsFromChild(Vector<Record> v)
	{
		Vector<Record> childTextRecords = new Vector<Record>(); 
		for(int i = 0 ; i < v.size(); i++)
		{
			if(v.get(i).getChildRecordBuckets() != null && v.get(i).getChildRecordBuckets().get(RecordType.TEXT) != null)
			{
				childTextRecords.addAll(v.get(i).getChildRecordBuckets().get(RecordType.TEXT));
				v.get(i).getChildRecordBuckets().remove(RecordType.TEXT);
			}
		}
		
		if(childRecordBuckets == null && childTextRecords.size() > 0)
		{
			childRecordBuckets = new TreeMap<RecordType,Vector<Record>>();			
		}
		
		if(childTextRecords.size() > 0)
		{
			if(childRecordBuckets.get(RecordType.TEXT) == null)
			{
				childRecordBuckets.put(RecordType.TEXT,childTextRecords);
			}
			else
			{
				childRecordBuckets.get(RecordType.TEXT).addAll(childTextRecords);
			}
			
		}
	}

	@Override
	public String toString() {
		return "Record [lineno=" + lineno + ",pageno=" + pageno + ",indexOnPage=" + indexOnPage + ", type=" + type + ", data="
				+ data + "childRecordBuckets=" + childRecordBuckets + "mergedRecords=" + mergedRecords + "]";
	}

	public int getPageno() {
		return pageno;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
		if(mergedRecords != null && mergedRecords.size() > 0)
		{
			for(int i = 0 ; i < mergedRecords.size(); i++)
			{
				mergedRecords.get(i).setPageno(pageno);
			}
		}
	}

	public Vector<Record> getMergedRecords() {
		return mergedRecords;
	}

	public Record addToMergedRecords(Record child)
	{
		if(mergedRecords == null)
		{
			mergedRecords = new Vector<Record>();
		}
		Vector<Record> ttt = new Vector<Record>();
		ttt.add(child);
		mergeTextRecordsFromChild(ttt);
		mergedRecords.add(child);
		return child;
	}

	public TreeMap<RecordType, Vector<Record>> getChildRecordBuckets() {
		return childRecordBuckets;
	}

	public int getIndexOnPage() {
		return indexOnPage;
	}

	public void setIndexOnPage(int indexOnPage) {
		this.indexOnPage = indexOnPage;
	}

	public void setLineno(int lineno) {
		this.lineno = lineno;
	}
	
	
}
