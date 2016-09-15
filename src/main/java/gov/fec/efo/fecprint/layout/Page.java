package gov.fec.efo.fecprint.layout;

import gov.fec.efo.fecprint.data.Record;
import gov.fec.efo.fecprint.paginate.PaginationProperties;
import gov.fec.efo.fecprint.utility.Constants;
import gov.fec.efo.fecprint.utility.Utility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Page {
	
	private Map fieldMapping;	
	private long pageNo;
	private long imageNumber;
	private Record[] recs;
	PaginationProperties pageProperties;
	
	private boolean endOfSchedule;
	private Vector<Record> refToAllRecordsInSchedule = null;
	PageManager pgManager;
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	
	public Page(Map fieldMapping, long pageNo, PaginationProperties pp,PageManager m) {
		super();
		this.fieldMapping = fieldMapping;
		this.pageNo = pageNo;
		recs = new Record[pp.getNoOfRecordsPerPage() > 0 ? pp.getNoOfRecordsPerPage() : 1];
		this.pageProperties = pp;
		endOfSchedule = false;
		pgManager = m;
	}
	
	public long getPageNo() {
		return pageNo;
	}
	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}
	public long getImageNumber() {
		return pgManager.getStartingImageNumer() + pageNo - 1;
	}
	
	public void addRecord(Record r)
	{
		recs[r.getIndexOnPage()] = r;
	}
	
	public Map<String, String> getData()
	{
		Map<String, String> m = new HashMap<String, String>();
		List<String> lJavaScripts = null;
		
		Iterator<String> it = fieldMapping.keySet().iterator();
		while(it.hasNext())
		{
			String fieldName = it.next();
			FieldProperties fieldProps = ((FieldProperties)(fieldMapping.get(fieldName)));
			if(fieldProps.getDataSource() == FieldDataSource.PAGE)
			{
				if(recs.length > 1)
				{
					for(int i = 0 ; i < recs.length ; i++)
					{
						if(recs[i] != null)
						{
							m.put(fieldName + "_" + (i +1),formatForUI(fieldProps,recs[i]));
						}
					}
				}
				else
				{
					m.put(fieldName ,formatForUI(fieldProps,recs[0]));
				}
			}
			else if(fieldProps.getDataSource() == FieldDataSource.COVER)
			{
				String presetKey = "CVR@" + fieldProps.getPos() + "#" + fieldProps.getType(); 
				if(pgManager.getPresetData().get(presetKey) == null)
				{
					String val = formatForUI(fieldProps,pgManager.getDataBuilder().getCoverPage());
					pgManager.getPresetData().put(presetKey,val);
				}
				m.put(fieldName, pgManager.getPresetData().get(presetKey));
				
			}
			else if(fieldProps.getDataSource() == FieldDataSource.TOTAL && isEndOfSchedule())
			{
				double dTot = calculateTotal(fieldProps.getPos() -1);
				if(dTot != 0)
				{
					m.put(fieldName,String.format("%.2f",dTot));
					pgManager.getPresetData().put(recs[0].getType().name() + "_TOTAL", String.format("%.2f",dTot));
				}
				else
				{
					m.put(fieldName,"0.00");
					pgManager.getPresetData().put(recs[0].getType().name() + "_TOTAL", "0.00");
				}
			}
			else if(fieldProps.getDataSource() == FieldDataSource.SUBTOTAL)
			{
				double sTot = calculateSubTotal(fieldProps.getPos() -1);
				if(sTot != 0)
				{
					m.put(fieldName,String.format("%.2f",sTot));
				}
				else
				{
					m.put(fieldName,"0.00");
				}
			}
			else if(fieldProps.getDataSource() == FieldDataSource.JS)
			{
				if(lJavaScripts == null)
				{
					lJavaScripts = new ArrayList<String>();
				}
				lJavaScripts.add(fieldName);
			}
		}
		
		m.put("PAGESTR", "PAGE " + String.valueOf(pageNo) + " / " + String.valueOf(pgManager.getTotalPages()));
		
		if(pageProperties.getPageSpan() > 1)
		{
			for(int smryPageNo = 2; smryPageNo <= pageProperties.getPageSpan();  smryPageNo++)
			{
				m.put("PAGESTR"+ smryPageNo, "PAGE " + String.valueOf(pageNo + smryPageNo -1) +  " / " + String.valueOf(pgManager.getTotalPages()));
			}
		}
		
		m.put("PAGENO", String.valueOf(pageNo));
		m.put("LINE_NO", recs[0].getData().get(0).toUpperCase());
		m.put("TOTALPAGES", String.valueOf(pgManager.getTotalPages()));
		
		if(lJavaScripts != null && lJavaScripts.size() > 0)
		{
			for(int y = 0 ; y < lJavaScripts.size(); y++)
			{
				if(((String)lJavaScripts.get(y)).endsWith("_ITR"))
				{
					for(int g = 0 ; g < recs.length ; g++)
					{
						if(recs[g] != null)
						{
							getValueFromJavaScript(m, lJavaScripts, (String)lJavaScripts.get(y),String.valueOf(g + 1));
						}
					}
				}						
				else
				{
					getValueFromJavaScript(m, lJavaScripts, (String)lJavaScripts.get(y),null);
				}
			}
		}
		
		if(pgManager.isStampAsElectronicallyFiled())
			m.put("EF_STAMP", "[Electronically Filed]");
		
		if(recs.length == 1 && recs[0] != null && recs[0].getLineno() == -1)
		{
			m.put("EXTENDED_PAGE_MESSAGE", "continued");
		}
		else 
		{
			for(int i = 0 ; i < recs.length ; i++)
			{
				if(recs[i] != null && recs[i].getLineno() == -1)
				{
					m.put("EXTENDED_PAGE_MESSAGE_" + (i +1), "continued");
				}
			}
		}
		if(pgManager.getStartingImageNumer() != 1)
		{
			m.put("IMGNO", "Image# " + String.valueOf(getImageNumber()));
			if(pageProperties.getPageSpan() > 1)
			{
				for(int smryPageNo = 2; smryPageNo <= pageProperties.getPageSpan();  smryPageNo++)
				{
					m.put("IMGNO_FOR_PAGE"+ smryPageNo, "Image# " + String.valueOf(getImageNumber() + smryPageNo -1));
				}
			}
		}
		if(pgManager.getTimeStamp() != null)
		{
			m.put("FILING_TIMESTAMP", pgManager.getTimeStamp());
		}
		
		return m;
		
	}

	private void getValueFromJavaScript(Map m, List lJavaScripts, String fldName, String itrSuffixVal) {
		if(lJavaScripts.contains(fldName))
		{
			FieldProperties fieldProps = ((FieldProperties)(fieldMapping.get(fldName)));
			String expression = fieldProps.getExpression();
			if(itrSuffixVal != null)
			{
				fldName = fldName + "_" + itrSuffixVal;
				expression = StringUtils.replace(expression, "_?", "_" + itrSuffixVal);
				
			}
			int delimiterStart = -1;
			int delimiterEnd = -1;
			while((delimiterStart = expression.indexOf('$')) != -1)
			{
				delimiterEnd = expression.indexOf('$', delimiterStart + 1);
				if(delimiterStart != -1 && delimiterEnd != -1)
				{
					String operandName = expression.substring(delimiterStart + 1, delimiterEnd);
					if(m.get(operandName) == null)
					{
						getValueFromJavaScript(m, lJavaScripts, operandName,null);
					}
					String operandValue = m.get(operandName) != null ? m.get(operandName).toString() : null;
					if(operandValue == null)
					{
						operandValue = pgManager.getPresetData().get(operandName);
						if(operandValue == null)
						{
							operandValue = "0.0";
						}
					}
					operandValue = StringUtils.replace(operandValue, "'", "\\'");
					expression = StringUtils.replace(expression, "$" + operandName + "$", operandValue);
					delimiterStart = -1;
					delimiterEnd = -1;
				}
				else
					break;
			}
			Object expressionResult = Utility.evalExpression(expression);
			if(expressionResult != null)
			{
				if(expressionResult instanceof Double)
				{
					Double dbl = (Double)expressionResult;
					m.put(fldName,String.format("%.2f",dbl.doubleValue()));
				}
				else
				{
					m.put(fldName,String.valueOf(expressionResult));
				}
			}
		}
		else
		{
			return;
		}
	}
	public PaginationProperties getPageProperties() {
		return pageProperties;
	}	
	
	private double calculateSubTotal(int fieldPos)
	{
		double d = 0;
		if(recs.length >= 1)
		{
			MemoProperties mp = MemoProperties.valueOf(recs[0].getType().getType().name());
			if(mp.getPositionMemoCode() == -1)
			{
				for(int i = 0 ; i < recs.length ; i++)
				{
					if(recs[i] != null &&recs[i].getData().size() > fieldPos)
					{
						d += NumberUtils.toDouble(recs[i].getData().get( fieldPos));
					}
				}
			}
			else
			{
				for(int i = 0 ; i < recs.length ; i++)
				{
					if(recs[i] != null &&recs[i].getData().size() > fieldPos)
					{
						if(recs[i].getData().size() < mp.getPositionMemoCode() || StringUtils.isBlank(recs[i].getData().get( mp.getPositionMemoCode() - 1)) == true)
						{
							d += NumberUtils.toDouble(recs[i].getData().get( fieldPos));
						}						
					}
				}
			}
		}
		return d;
	}
	
	private double calculateTotal(int fieldPos)
	{
		
		double d = 0;
		if(refToAllRecordsInSchedule != null && refToAllRecordsInSchedule.size() > 0)
		{
			MemoProperties mp = MemoProperties.valueOf(recs[0].getType().getType().name());
			if(mp.getPositionMemoCode() == -1)
			{
				for(int i = 0 ; i < refToAllRecordsInSchedule.size() ; i++)
				{
					if(refToAllRecordsInSchedule.get(i) != null)
					{
						if(refToAllRecordsInSchedule.get(i).getData().size() > fieldPos)
						{
							d += NumberUtils.toDouble(refToAllRecordsInSchedule.get(i).getData().get( fieldPos));							
						}
					}
				}
			}
			else
			{
				for(int i = 0 ; i < refToAllRecordsInSchedule.size() ; i++)
				{
					if(refToAllRecordsInSchedule.get(i) != null)
					{
						if(refToAllRecordsInSchedule.get(i).getData().size() > fieldPos)
						{
							if(refToAllRecordsInSchedule.get(i).getData().size() < mp.getPositionMemoCode() || StringUtils.isBlank(refToAllRecordsInSchedule.get(i).getData().get( mp.getPositionMemoCode() - 1)) == true)
							{
								d += NumberUtils.toDouble(refToAllRecordsInSchedule.get(i).getData().get( fieldPos));
							}
						}
					}
				}
			}
		}
		return d;		
	}
	
	private String formatForUI(FieldProperties fieldProps,Record r)
	{
		if(r.getData().size() < fieldProps.getPos())
			return "";
		else
		{
			try
			{
				String unformatted = r.getData().get(fieldProps.getPos() -1);
				switch(fieldProps.getType())
				{
					case STRING:
						return unformatted == null ? "" : unformatted;
					case DATE :
						return StringUtils.isBlank(unformatted) == true ? "" : pgManager.uiDateFormat.format(pgManager.inputDateFormat.parse(unformatted));
					case DATEDD :
						if(StringUtils.isBlank(unformatted) == true)
							return "";
						else
						{
							
							try
							{
								return pgManager.uiDateFormat_DD.format(pgManager.inputDateFormat.parse(unformatted));
							}
							catch(ParseException eParse)
							{
								logger.debug(eParse);
								return "";
							}							
						} 
					case DATEMM :
						if(StringUtils.isBlank(unformatted) == true)
							return "";
						else
						{
							
							try
							{
								return pgManager.uiDateFormat_MM.format(pgManager.inputDateFormat.parse(unformatted));
							}
							catch(ParseException eParse)
							{
								logger.debug(eParse);
								return "";
							}							
						}
					case DATEYYYY :
						if(StringUtils.isBlank(unformatted) == true)
							return "";
						else
						{
							
							try
							{
								return pgManager.uiDateFormat_YYYY.format(pgManager.inputDateFormat.parse(unformatted));
							}
							catch(ParseException eParse)
							{
								logger.debug(eParse);
								return unformatted;
							}							
						}						
					case TRANID:
						return StringUtils.isBlank(unformatted) == true ? ""  : "Transaction ID : " + unformatted; 
					case ELECTIONTYPE:
						return StringUtils.isBlank(unformatted) == true ? "" : StringUtils.replaceChars(unformatted.substring(0,1),"CRSE","OOOO");
					case ELECTIONYEAR:
						return StringUtils.isBlank(unformatted) == true ? "" : unformatted.substring(1);
					case INDNAME :
					{
						return formatName(fieldProps,unformatted, r);
					}
					case ENTNAME :
					{
						return formatName(fieldProps,unformatted, r);
					}
					case PERCENTAGE:
						if(StringUtils.isBlank(unformatted) == true)
							return "" ;
						else
						{
							try
							{
							 return String.format("%.2f", Double.parseDouble(unformatted) * 100);
							}
							catch(NumberFormatException eNumFormat)
							{
								logger.debug(eNumFormat);
								return unformatted;
							}							
						}
					case MEMOCODE:
						return StringUtils.isBlank(unformatted) == true ? "" :  unformatted.equalsIgnoreCase("X")? "[MEMO ITEM]" : "";
					case TRANSFORM:
						return Constants.lookup(fieldProps.getLookupKey(), unformatted);
					case PHONEPART1 :
						return StringUtils.isBlank(unformatted) == true ? "" : unformatted.length() != 10 ? "" : unformatted.substring(0,3);
					case PHONEPART2 :
						return StringUtils.isBlank(unformatted) == true ? "" : unformatted.length() != 10 ? "" : unformatted.substring(3,6);
					case PHONEPART3 :
						return StringUtils.isBlank(unformatted) == true ? "" : unformatted.length() != 10 ? "" : unformatted.substring(6);
					case ZIP :
					{
						if(StringUtils.isBlank(unformatted) == true)
							return "";
						else 
						{
							if(unformatted.length() == 9)
								return unformatted.substring(0,5) + "-" + unformatted.substring(5);
							else
								return unformatted;
						}
					}
					case FPCID:
					{
						return StringUtils.isBlank(unformatted) == false ? unformatted : r.getData().get(fieldProps.getPos() -1) != null ? r.getData().get(fieldProps.getPos() -1 + 2) : "";
					}
					default : 
						return unformatted;				
				}
			}
			catch(Exception eParse)
			{
				logger.error("Error in buidling the stampable data",eParse);
				logger.error(fieldProps.toString());
				logger.error(r.toString());				
				return "";
			}
		}
	}
	
	private String formatName(FieldProperties fieldProps, String unformatted, Record r)
	{
		switch(fieldProps.getType())
		{
			case INDNAME :
			{
				StringBuffer nm = new StringBuffer();
				int startPos = fieldProps.getPos() - 1;
				ArrayList<String> data = r.getData();
				//if(!StringUtils.isBlank(data.get(startPos))) //Last Name
					nm.append(data.get(startPos)  + ", ");
				
				//if(!StringUtils.isBlank(data.get(startPos + 1))) //First Name
					nm.append(data.get(startPos + 1)  + ", ");
				
				//if(!StringUtils.isBlank(data.get(startPos + 2))) //Middle Name
					nm.append(data.get(startPos + 2)  + ", ");
				
				//if(!StringUtils.isBlank(data.get(startPos + 3))) //Prefix
					nm.append(data.get(startPos + 3) + ", ");
				
				//if(!StringUtils.isBlank(data.get(startPos + 4))) // Suffix
					nm.append(data.get(startPos + 4));
				
				return nm.toString();
			}
			case ENTNAME :
			{
				StringBuffer nm = new StringBuffer();
				if(unformatted != null && (unformatted.equals("IND") || unformatted.equals("CAN")))
				{
					int startPos = fieldProps.getPos() - 1 + 2 ;
					ArrayList<String> data = r.getData();
					
					//if(!StringUtils.isBlank(data.get(startPos))) //Last Name
						nm.append(data.get(startPos)  + ", ");
					
					//if(!StringUtils.isBlank(data.get(startPos + 1))) //First Name
						nm.append(data.get(startPos + 1)  + ", ");
					
					//if(!StringUtils.isBlank(data.get(startPos + 2))) //Middle Name
						nm.append(data.get(startPos + 2)  + ", ");
					
					//if(!StringUtils.isBlank(data.get(startPos + 3))) //Prefix
						nm.append(data.get(startPos + 3)  + ", ");
					
					//if(!StringUtils.isBlank(data.get(startPos + 4))) // Suffix
						nm.append(data.get(startPos + 4));
				}
				else
				{
					nm.append(unformatted != null ? r.getData().get(fieldProps.getPos()): "");
				}
				
				return nm.toString();
			}
			default : return unformatted;				
		}		
	}

	public boolean isEndOfSchedule() {
		return endOfSchedule;
	}

	public void setEndOfSchedule(boolean endOfSchedule) {
		this.endOfSchedule = endOfSchedule;
	}

	public void setRefToAllRecordsInSchedule(
			Vector<Record> refToAllRecordsInSchedule) {
		this.refToAllRecordsInSchedule = refToAllRecordsInSchedule;
	}

	public Record[] getRecs() {
		return recs;
	}
}
