package gov.fec.efo.fecprint.layout;

import java.util.List;

public class Form5PageInstance extends PageInstance{
	
	public Form5PageInstance(PageDef def)
	{
		super(def);
	}
	
	public String doDataProcessing(int index,String data, List<String> rawData)
	{
		String ret = "";
		switch(index)
		{
			case 1 : 
				{
					ret = isReportAnAmendment(data);
				}
				break;
			case 2 :
			{
				ret = data.substring(1);				
			}
			break;
			case 3 : 
			{
				ret = getEntityName(index,data,rawData);
			}
			break;
			case 25 : 
			{
				ret = getIndividualName(index,data,rawData);
			}
			break;
			case 21 :
			{
				ret = getFromattedDate(data);				
			}
			break;
			case 30 :
			{
				ret = getFromattedDate(data);				
			}
			break;
			default:
				ret = data;
				break;
		}
		return ret;
	}
	
	public String doRenderProcessing(int index, String name,String data) {
		switch(index)
		{
		case 22 :
			if(name.endsWith("_YY"))
			{
				data = data.substring(0, 4);
			}
			else if(name.endsWith("_MM"))
			{
				data = data.substring(4, 6);
			}
			else if(name.endsWith("_DD"))
			{
				data = data.substring(6, 8);
			}
			break;
		
		}
		return data;
		
	}

}
