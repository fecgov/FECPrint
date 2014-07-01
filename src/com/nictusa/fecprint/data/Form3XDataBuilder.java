package com.nictusa.fecprint.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.mutable.MutableDouble;

public class Form3XDataBuilder {

	DataBuilder core = null;
	
	private static final int H4_FED_NONFED_TOTAL = 20;
	private static final int H4_FEDERAL_SHARE = 21;	
	private static final int H4_DCS_POSITION = 30;
	private static final int H4_MEMO_TEXT_POSITION = 33;

	public Form3XDataBuilder(DataBuilder base) {
		this.core = base;
	}

	public void buildData(Map<RecordType, Vector<Record>> recordBuckets) {
		if (recordBuckets.get(RecordType.SC_9) != null) {
			RecordHelper.mergeSC2RecordWithParent(recordBuckets
					.get(RecordType.SC_9), RecordType.SC2_9);
		}
		if (recordBuckets.get(RecordType.SC_10) != null) {
			RecordHelper.mergeSC2RecordWithParent(recordBuckets
					.get(RecordType.SC_10), RecordType.SC2_10);
		}
		
		if (recordBuckets.get(RecordType.SF) != null) {
			Collections.sort(recordBuckets.get(RecordType.SF), new SFComparator());
		}

		if (recordBuckets.get(RecordType.H3) != null) {
			Vector<Record> vH3A = recordBuckets.get(RecordType.H3);
			for (int i = 0; i < vH3A.size(); i++) {
				ArrayList<Record> vGV = new ArrayList<Record>();
				ArrayList<Record> vEA = new ArrayList<Record>();
				ArrayList<Record> vDF = new ArrayList<Record>();
				ArrayList<Record> vDC = new ArrayList<Record>();
				ArrayList<Record> vPC = new ArrayList<Record>();
				
				Record rH3A = vH3A.get(i);
				if (rH3A.getChildRecordBuckets() != null) {
					Vector<Record> childRecs = rH3A.getChildRecordBuckets()
							.get(RecordType.H3);
										if (childRecs != null && childRecs.size() > 0) {
						while (childRecs.size() > 0) {
							Record h3Child = childRecs.remove(0);
							if (h3Child.getData().get(
									RecordConstants.H3_EVENT_TYPE_POS).equals(
									"GV")) {
								vGV.add(h3Child);
							} else if (h3Child.getData().get(
									RecordConstants.H3_EVENT_TYPE_POS).equals(
									"EA")) {
								vEA.add(h3Child);
							} else if (h3Child.getData().get(
									RecordConstants.H3_EVENT_TYPE_POS).equals(
									"DF")) {
								vDF.add(h3Child);
							} else if (h3Child.getData().get(
									RecordConstants.H3_EVENT_TYPE_POS).equals(
									"DC")) {
								vDC.add(h3Child);
							} else if (h3Child.getData().get(
									RecordConstants.H3_EVENT_TYPE_POS).equals(
									"PC")) {
								vPC.add(h3Child);
							}
						}

						RecordHelper.expandArrayTo(10, rH3A.getData());

						MutableDouble totalGV = new MutableDouble(); 
						MutableDouble totalEA = new MutableDouble();
						MutableDouble totalDF = new MutableDouble();
						MutableDouble totalDC = new MutableDouble();
						MutableDouble totalPC = new MutableDouble();
						
						int childCount = 0;
						childCount += appendH3Child(rH3A, vGV, totalGV);
						childCount += appendH3Child(rH3A, vEA, totalEA);
						childCount += appendH3Child(rH3A, vDF, totalDF);
						childCount += appendH3Child(rH3A, vDF, totalDF);
						childCount += appendH3Child(rH3A, vDC, totalDC);
						childCount += appendH3Child(rH3A, vDC, totalDC);
						childCount += appendH3Child(rH3A, vPC, totalPC);

						Vector<Record> vDummy = new Vector<Record>();
						while (childCount > 0) {
							childCount = 0;
							Record dummy = new Record(-1, RecordType.H3, RecordHelper.getDataSliceIfNotEmpty(0, 3, rH3A.getData()));
							RecordHelper.expandArrayTo(10, dummy.getData());
							
							childCount += appendH3Child(dummy, vGV, totalGV);
							childCount += appendH3Child(dummy, vEA, totalEA);
							childCount += appendH3Child(dummy, vDF, totalDF);
							childCount += appendH3Child(dummy, vDF, totalDF);
							childCount += appendH3Child(dummy, vDC, totalDC);
							childCount += appendH3Child(dummy, vDC, totalDC);
							childCount += appendH3Child(dummy, vPC, totalPC);
							if (childCount > 0) {
								vDummy.add(dummy);

							}
						}
						if (vDummy.size() > 0) {
							rH3A.addChildRecords(RecordType.H3, vDummy);
						}
						
						rH3A.getData().add(totalGV.toString());
						rH3A.getData().add(totalEA.toString());
						rH3A.getData().add(totalDF.toString());
						rH3A.getData().add(totalDC.toString());
						rH3A.getData().add(totalPC.toString());
					}
				}
			}
		}
		
		if (recordBuckets.get(RecordType.H4) != null) {
			Vector<Record> vH4 = recordBuckets.get(RecordType.H4);
			for(int y= 0; y < vH4.size(); y++)
			{
				Record rH4 = vH4.get(y);
				RecordHelper.expandArrayTo(33, rH4.getData());
				
				if(rH4.getData().get(H4_DCS_POSITION -1).equals("X") == false)
				{
					rH4.getData().add(rH4.getData().get(H4_FED_NONFED_TOTAL -1));
					rH4.getData().add(rH4.getData().get(H4_FEDERAL_SHARE -1));					
					
				}
				else
				{
					String sMemoText = rH4.getData().get(H4_MEMO_TEXT_POSITION -1);
					rH4.getData().remove(H4_MEMO_TEXT_POSITION -1);
					if(sMemoText != null)
					{
						if(sMemoText.indexOf("Federal Memo") == -1)
						{
							rH4.getData().add(sMemoText + " [Federal Memo]");
						}
						else
						{
							rH4.getData().add(sMemoText);
						}
					}
					else
					{
						rH4.getData().add("[Federal Memo]");
					}
					double dRevised = Double.parseDouble(rH4.getData().get(H4_FED_NONFED_TOTAL -1)) - Double.parseDouble(rH4.getData().get(H4_FEDERAL_SHARE -1));
					rH4.getData().add(String.format("%.2f", dRevised));
					rH4.getData().add("");
					
					
				}
			}
		}
	}

	private int appendH3Child(Record parentH3, ArrayList<Record> childs, MutableDouble tot) {
		if (childs.size() > 0) 
		{
			RecordHelper.expandArrayTo(10, childs.get(0).getData());
			tot.add(Double.parseDouble(childs.get(0).getData().get(9)));
			parentH3.getData().addAll(childs.get(0).getData());
			parentH3.addToMergedRecords(childs.remove(0));
			
			return 1;
		} 
		else
		{
			ArrayList<String> dummyArr = new ArrayList<String>();  
			RecordHelper.expandArrayTo(10, dummyArr);			
			parentH3.getData().addAll(dummyArr);			
			return 0;
		}

	}
	
	class SFComparator implements Comparator<Record>
	{

		public int compare(Record o1, Record o2) {
			
			if(o1 == o2)
				return 0;
			
			o1.getData().set(1, o1.getData().get(7) + "###" + o1.getData().get(5));
			o2.getData().set(1, o2.getData().get(7) + "###" + o2.getData().get(5));
			
			int desigantedCompare = o1.getData().get(1).compareToIgnoreCase(o2.getData().get(1));
			if(desigantedCompare == 0)
			{
				return o1.getLineno() < o2.getLineno() ? -1 : 1;				
			}
			else
				return desigantedCompare;			
		}
		
	}
}
