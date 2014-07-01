package com.nictusa.fecprint.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;


public class Form99DataBuilder {
	
	DataBuilder core = null;
	static final int MAX_TEXT_ON_PAGE=4000;
	public static final int MAX_TEXT_ON_LINE=120;
	static final int MAX_LINES_ON_PAGE=50;

	public Form99DataBuilder(DataBuilder base) {
		this.core = base;
	}


	public void buildData(Map<RecordType,Vector<Record>> recordBuckets)
	{
		ArrayList<String> data = core.getCoverPage().getData();
		ArrayList<String> extendedData = new ArrayList<String>();
		for(int i = 15; data.size() > 15; i++)
		{
			String s = data.remove(15);
			
			while(true)
			{
				int linecount = StringUtils.countMatches(s, "\r\n");
				if(s.length() > MAX_TEXT_ON_PAGE  || linecount > MAX_LINES_ON_PAGE)
				{
					int breakAt = s.indexOf(' ', MAX_TEXT_ON_PAGE);
					if(breakAt == -1)
					{
						breakAt = s.length() -1; 
					}
					if(StringUtils.countMatches(s.substring(0,breakAt),"\r\n") > MAX_LINES_ON_PAGE)
					{
						while(breakAt > 0)
						{
							breakAt--;
							if(StringUtils.countMatches(s.substring(0,breakAt),"\r\n") < MAX_LINES_ON_PAGE)
							{
								break;
							}
						}
					}
					extendedData.add(s.substring(0,breakAt));
					s = s.substring(breakAt + 1);
				}
				else
				{
					extendedData.add(s);
					break;
				}
			}
		}
		
		Vector<Record> vRecs = new Vector<Record>();
		
		while(extendedData.size() > 0)
		{
			ArrayList<String> textData = new ArrayList<String>(); 
			
			textData.add(extendedData.remove(0));
			
			Record newF99 = null;
			if(vRecs.size() == 0)
			{
				newF99 = core.getCoverPage();				
			}
			else
			{
				newF99 = new Record(-1, RecordType.F99, new ArrayList<String>());
				newF99.getData().addAll(core.getCoverPage().getData().subList(0, 15));
			}			
			newF99.getData().addAll(textData);
			vRecs.add(newF99);
			textData.clear();
		}
		
		core.getRecordBuckets().get(RecordType.F99).clear();
		core.getRecordBuckets().get(RecordType.F99).addAll(vRecs);
	}
}
