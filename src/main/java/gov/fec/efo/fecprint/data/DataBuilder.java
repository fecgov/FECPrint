/**
 * 
 */
package gov.fec.efo.fecprint.data;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.fec.efo.fecprint.paginate.Pagination;
import gov.fec.efo.fecprint.utility.Utility;

/**
 * @author Milind
 *
 */
public class DataBuilder {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String dataFileName;
	private TreeMap<RecordType,Vector<Record>> recordBuckets;	
	private BaseRecordType formType;
	private Record coverPage;	
	private int totalPages;	
	private int totalLines = 0;
	
	public static final char FIELD_DELIMITER = 28;

	public DataBuilder(String flName) throws Exception
	{
		dataFileName = flName;		
		recordBuckets = new TreeMap<RecordType,Vector<Record>>();
		totalPages = 0;
/*
		FileReader reader = null;
		List lineList = null;
		
		
			
			try
			{
				reader = new FileReader(dataFileName);
				lineList = IOUtils.readLines(reader);				
			}
			finally
			{
				if(reader != null)
				{
					reader.close();
				}
			}
			
			String line;
			int lineNo  = 0;
			totalLines = lineList.size();
			logger.debug("Total lines in data file = " + totalLines);
*/

			BufferedReader in = new BufferedReader(new FileReader(dataFileName));
			String line = null;
			int lineNo = 0;
			try {
			while ((line = in.readLine()) != null) { 

//while(lineList.size() > 0) {

				lineNo++;
/*
				if(lineNo%100000 == 0)
				{
					Utility.freeUpMemory("In Databuilder. Memory cleanup after processing " + lineNo + " lines.");					
				}
				line = (String)lineList.remove(0);
*/				
				if(StringUtils.isBlank(line)  || line.startsWith("#") == true)
					continue;
				StrTokenizer tok = new StrTokenizer(line,FIELD_DELIMITER,'"');
				tok.setEmptyTokenAsNull(false);
				tok.setIgnoreEmptyTokens(false);
				ArrayList<String> lst = new ArrayList<String>();
//tok.size());
				while(tok.hasNext())
				{
					lst.add((String)tok.next());
				}

				RecordType t = RecordHelper.getRecordType(lst.get(0),null);
				
				if(recordBuckets.get(t) == null)
				{
					trimToSize();
					if(lineNo > 3)
					{
						recordBuckets.put(t, new Vector()); //totalLines - lineNo + 1));
					}
					else
					{
						recordBuckets.put(t, new Vector());
					}
				}				
				
				Vector<Record> v = recordBuckets.get(t);
				Record newRec = new Record(lineNo, t, lst);
				v.add(newRec);
				if(t.getTranIdPos() == -1 && t.getType().ordinal() < BaseRecordType.LASTFORM.ordinal())
				{
					formType = t.getType();
					coverPage = newRec;
					
					logger.info("This is a " + formType.name());
					
					if(formType.equals(BaseRecordType.F99))
					{
						logger.debug("Reading F99 text");
						String lines = getF99Text(in); //lineList);
						RecordHelper.expandArrayTo(15, newRec.getData());
						newRec.getData().add(lines);		
					}
				}
			}

}
                        finally
                        {
                                if(in != null)
                                {
                                        in.close();
                                }
                        }
			
			Utility.freeUpMemory("Done Building records");
			
			recordBuckets.remove(RecordType.HDR);
			
			associateParentChildRecords();
			
			buildPrintReadyData();
			
			logger.debug("buildPrintReadyData");
			
			Pagination pagination = new Pagination();
			totalPages = pagination.paginateRecords(this, 1) - 1;
			
			
			////////////////////Generate Log/////////////
			/*Set set = recordBuckets.keySet();
			Iterator it = set.iterator();
			while(it.hasNext())
			{
				logger.debug("");
				Vector<Record> recs = recordBuckets.get(it.next());			
				for(int x = 0; x < recs.size(); x++)
				{
					logger.debug(recs.get(x));
				}
			}
			
			for(int i = 0; i < getRecordCount();i++)
			{
				System.out.println(records.get(i).getPageno());
			}*/
			//////////////End Log///////////////
		
	}
	
	private void trimToSize()
	{
		Set set = recordBuckets.keySet();
		Iterator it = set.iterator();
		while(it.hasNext())
		{
			Vector<Record> recs = recordBuckets.get(it.next());
			recs.trimToSize();
		}
	}
	
	private void associateParentChildRecords() throws Exception
	{
		logger.info("Enter associateParentChildRecords");
		if(recordBuckets.get(RecordType.TEXT) != null)
		{
			RecordType coverRecordType = RecordHelper.getRecordType(coverPage.getData().get(0), null);
			do
			{
				Record r = recordBuckets.get(RecordType.TEXT).get(0);
				RecordType parentType = RecordHelper.getRecordType(r.getData().get( RecordConstants.BACK_REF_SCH_FORM_NAME_POS),coverRecordType);				
				Vector<Record> vParent = recordBuckets.get(parentType);
				
				for(int j = 0; j < vParent.size() && recordBuckets.get(RecordType.TEXT).size() > 0; j++)
				{
					Record rP = vParent.get(j);
					if(rP.getType().getTranIdPos() != -1)
					{
						if(StringUtils.isBlank(r.getData().get(RecordConstants.BACK_REF_ID_POS)))
						{
							Vector<Record> childrens = extractMatchingRecords(recordBuckets.get(RecordType.TEXT), "", RecordConstants.BACK_REF_ID_POS, rP.getType());
							rP.addChildRecords(RecordType.TEXT, childrens);
							break;
						}
						else if(rP.getData().get(RecordConstants.TRAN_ID_POS).equals(r.getData().get(RecordConstants.BACK_REF_ID_POS)))
						{
							Vector<Record> childrens = extractMatchingRecords(recordBuckets.get(RecordType.TEXT), rP.getData().get(RecordConstants.TRAN_ID_POS), RecordConstants.BACK_REF_ID_POS);
							rP.addChildRecords(RecordType.TEXT, childrens);
							break;
						}
					}
					else
					{
						if(rP.getType().name().equals(r.getData().get(RecordConstants.BACK_REF_SCH_FORM_NAME_POS)))
						{
							Vector<Record> childrens = extractMatchingRecords(recordBuckets.get(RecordType.TEXT), rP.getType().name(), RecordConstants.BACK_REF_SCH_FORM_NAME_POS);
							rP.addChildRecords(RecordType.TEXT, childrens);
							break;
						}
						else if(rP.getType().getType().name().equals(r.getData().get(RecordConstants.BACK_REF_SCH_FORM_NAME_POS)))
						{
							Vector<Record> childrens = extractMatchingRecords(recordBuckets.get(RecordType.TEXT), rP.getType().getType().name(), RecordConstants.BACK_REF_SCH_FORM_NAME_POS);
							rP.addChildRecords(RecordType.TEXT, childrens);
							break;

						}
					}
					
				}
			}while(recordBuckets.get(RecordType.TEXT).size() > 0);
		}
		
		if(recordBuckets.get(RecordType.SI) != null)
		{
			Vector<Record> v = recordBuckets.get(RecordType.SI);
			for(int i = 0; i < v.size(); i++)
			{
				Record r = v.get(i);
				String s = r.getData().get( RecordConstants.SI_ACC_NAME_POS);
				Vector<Record> childrens = extractMatchingRecords(recordBuckets.get(RecordType.SASI1), s, RecordConstants.SA_ACC_NAME_POS);
				r.addChildRecords(RecordType.SASI1, childrens);
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SBSI2), s, RecordConstants.SB_ACC_NAME_POS);
				r.addChildRecords(RecordType.SBSI2, childrens);
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SBSI3), s, RecordConstants.SB_ACC_NAME_POS);
				r.addChildRecords(RecordType.SBSI3, childrens);
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SBSI4), s, RecordConstants.SB_ACC_NAME_POS);
				r.addChildRecords(RecordType.SBSI4, childrens);
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SBSI5), s, RecordConstants.SB_ACC_NAME_POS);
				r.addChildRecords(RecordType.SBSI5, childrens);
			}
		}
		
		if(recordBuckets.get(RecordType.SL) != null)
		{
			Vector<Record> v = recordBuckets.get(RecordType.SL);
			for(int i = 0; i < v.size(); i++)
			{
				Record r = v.get(i);
				String s = r.getData().get( RecordConstants.SL_ACC_NAME_POS);
				
				Vector<Record> childrens = extractMatchingRecords(recordBuckets.get(RecordType.SASL1A), s, RecordConstants.SA_ACC_NAME_POS);
				r.addChildRecords(RecordType.SASL1A, childrens);
				
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SASL2), s, RecordConstants.SA_ACC_NAME_POS);
				r.addChildRecords(RecordType.SASL2, childrens);
				
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SBSL4A), s, RecordConstants.SB_ACC_NAME_POS);
				r.addChildRecords(RecordType.SBSL4A, childrens);
				
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SBSL4B), s, RecordConstants.SB_ACC_NAME_POS);
				r.addChildRecords(RecordType.SBSL4B, childrens);
				
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SBSL4C), s, RecordConstants.SB_ACC_NAME_POS);
				r.addChildRecords(RecordType.SBSL4C, childrens);
				
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SBSL4D), s, RecordConstants.SB_ACC_NAME_POS);
				r.addChildRecords(RecordType.SBSL4D, childrens);
				
				childrens = extractMatchingRecords(recordBuckets.get(RecordType.SBSL5), s, RecordConstants.SB_ACC_NAME_POS);
				r.addChildRecords(RecordType.SBSL5, childrens);
			}
			
			recordBuckets.remove(RecordType.SASL1A);
			recordBuckets.remove(RecordType.SASL2);
			recordBuckets.remove(RecordType.SBSL4A);
			recordBuckets.remove(RecordType.SBSL4B);
			recordBuckets.remove(RecordType.SBSL4C);
			recordBuckets.remove(RecordType.SBSL4D);
			recordBuckets.remove(RecordType.SBSL5);			
		}
		
		if(recordBuckets.get(RecordType.H3) != null)
		{
			Vector<Record> v = recordBuckets.get(RecordType.H3);
			for(int i = 0; i < v.size(); i++)
			{
				Record r = v.get(i);
				if(r.getData().get(RecordConstants.H3_EVENT_TYPE_POS).equals("AD"))
				{
					String s = r.getData().get( RecordConstants.TRAN_ID_POS);
				
					Vector<Record> childrens = extractMatchingH3ChildRecords(recordBuckets.get(RecordType.H3), s);
					r.addChildRecords(RecordType.H3, childrens);
				}
			}
		}
		
		extractMatchingRecords(RecordType.SC_9, RecordType.SC2_9);
		extractMatchingRecords(RecordType.SC_10, RecordType.SC2_10);
		extractMatchingRecords(RecordType.SC_11, RecordType.SC2_11);
		extractMatchingRecords(RecordType.SC_12, RecordType.SC2_12);		
		
		extractMatchingRecords(RecordType.F93, RecordType.F94);
		
		logger.info("Done associateParentChildRecords");
		
		
	}
	
	private void extractMatchingRecords(RecordType parentType, RecordType childType) throws Exception
	{
		if(recordBuckets.get(parentType) != null)
		{
			Vector<Record> v = recordBuckets.get(parentType);
			for(int i = 0; i < v.size(); i++)
			{
				Record r = v.get(i);
				String s = r.getData().get( RecordConstants.TRAN_ID_POS);			
				Vector<Record> childrens = extractMatchingRecords(recordBuckets.get(childType), s, RecordConstants.BACK_REF_ID_POS);
				if(childrens != null && childrens.size() > 0)
					r.addChildRecords(childType, childrens);			
			}
			if(recordBuckets.get(childType) != null && recordBuckets.get(childType).size() > 0)
			{
				throw new Exception("Found " + childType.name() + " records that could be associated with a parent record");
				
			}
			else
			{
				recordBuckets.remove(childType);
			}
		}
		
		                                            
	}
	
	private Vector<Record> extractMatchingRecords(Vector<Record> v, String backRefId, int backRefIdPos)	
	{
		Vector<Record> vExtracted = new Vector<Record>();
		if(v != null)
		{
			for(int i = 0; i < v.size(); i++)
			{
				if(v.get(i).getData().get(backRefIdPos).equals(backRefId))
				{
					vExtracted.add(v.remove(i));
					i--;
				}
			}
		}
		return vExtracted;
	}
	
	private Vector<Record> extractMatchingRecords(Vector<Record> v, String backRefId, int backRefIdPos, RecordType parentType)	
	{
		Vector<Record> vExtracted = new Vector<Record>();
		RecordType coverRecordType = RecordHelper.getRecordType(coverPage.getData().get(0), null);
		if(v != null)
		{
			for(int i = 0; i < v.size(); i++)
			{
				if(v.get(i).getData().get(backRefIdPos).equals(backRefId))
				{
					RecordType backRefType = RecordHelper.getRecordType(v.get(i).getData().get( RecordConstants.BACK_REF_SCH_FORM_NAME_POS),coverRecordType);
					if(backRefType.equals(parentType))
					{
						vExtracted.add(v.remove(i));
						i--;
					}
					
				}
			}
		}
		return vExtracted;
	}
	
	
	private Vector<Record> extractMatchingH3ChildRecords(Vector<Record> v, String backRefId)	
	{
		Vector<Record> vExtracted = new Vector<Record>();
		if(v != null)
		{
			for(int i = 0; i < v.size(); i++)
			{
				if(v.get(i).getData().get(RecordConstants.BACK_REF_ID_POS).equals(backRefId) && v.get(i).getData().get(RecordConstants.TRAN_ID_POS).equals(backRefId) == false)
				{
					vExtracted.add(v.remove(i));
					i--;
				}
			}
		}
		return vExtracted;
	}
	
//	private String getF99Text(List input) throws IOException
	private String getF99Text(BufferedReader br) throws IOException
	{
		String line;
		StringBuffer lines = new StringBuffer("");		
/*
		while(input.size() > 0)
		{
*/
		while ((line = br.readLine()) != null) {
//			line = (String)input.remove(0);
			if(line.equals("[BEGINTEXT]") || line.equals("[ENDTEXT]"))
			{
				continue;
			}
			else
			{
				if(line.length() > Form99DataBuilder.MAX_TEXT_ON_LINE)
				{
					while(line.length() > Form99DataBuilder.MAX_TEXT_ON_LINE)
					{
						int breakAt = line.lastIndexOf(' ', Form99DataBuilder.MAX_TEXT_ON_LINE);
						if(breakAt == -1)
						{
							break;
						}
						lines.append(line.substring(0,breakAt) + "\r\n");
						line = line.substring(breakAt + 1);
					}
				}
				
				lines.append(line + "\r\n");
				
			}
			
		}
		return lines.toString();
	}
	
	private void buildPrintReadyData()
	{
		logger.info("Enter buildPrintReadyData");
		switch(formType)
		{
			case F1:
				new Form1DataBuilder().buildData(recordBuckets);
				break;
			case F24:
				new Form24DataBuilder().buildData(recordBuckets);
				break;
			case F3:
				new Form3DataBuilder(this).buildData(recordBuckets);
				break;
			case F3X:
				new Form3XDataBuilder(this).buildData(recordBuckets);
				break;
			case F3P:
				new Form3PDataBuilder(this).buildData(recordBuckets);
				break;
			case F4:
				new Form4DataBuilder(this).buildData(recordBuckets);
				break;
			case F6:
				new Form6DataBuilder(this).buildData(recordBuckets);
				break;
			case F7:
				new Form7DataBuilder(this).buildData(recordBuckets);
				break;
			case F9:
				new Form9DataBuilder(this).buildData(recordBuckets);
				break;
			case F99:
				new Form99DataBuilder(this).buildData(recordBuckets);
				break;
		}
		logger.info("Done buildPrintReadyData");
	}

	public BaseRecordType getFormType() {
		return formType;
	}

	public Record getCoverPage() {
		return coverPage;
	}

	public TreeMap<RecordType, Vector<Record>> getRecordBuckets() {
		return recordBuckets;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public int getTotalLines() {
		return totalLines;
	}
	
	public Record findRecord(String transactionId)
	{
		TreeMap<RecordType, Vector<Record>> recordBuckets = getRecordBuckets();
		if (recordBuckets != null && recordBuckets.size() > 0) {
			Set<RecordType> set = recordBuckets.keySet();
			Iterator<RecordType> it = set.iterator();
			while (it.hasNext()) 
			{
				RecordType rt = it.next();
				if(rt.getTranIdPos() == -1)
				{
					continue;
				}
				
				Vector<Record> recs = recordBuckets.get(rt);
				if(recs != null && recs.size() > 0)
				{
					for(int r = 0 ; r < recs.size(); r++)
					{
						List<String> l = recs.get(r).getData();
						if(l.size() > RecordConstants.TRAN_ID_POS)
						{
							if(StringUtils.isBlank(l.get(RecordConstants.TRAN_ID_POS)) == false && l.get(RecordConstants.TRAN_ID_POS).equalsIgnoreCase(transactionId))
							{
								return recs.get(r);
							}
						}
					}
				}
			}
		}
		return null;
	}

}
