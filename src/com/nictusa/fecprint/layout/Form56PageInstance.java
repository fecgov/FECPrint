package com.nictusa.fecprint.layout;

import java.util.List;

public class Form56PageInstance extends PageInstance{
	
	public Form56PageInstance(PageDef def)
	{
		super(def);
	}
	
	public String doDataProcessing(int index,String data, List<String> rawData)
	{
		String ret = "";
		
		switch(index)
		{
			case 16 :			
			{
				ret = getCommitteIDNumber(data);				
			}
			break;
			case 4 : 			
			{
				ret = getEntityName(index,data,rawData);
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
			default:
			break;
		
		}
		return data;		
	}


}
