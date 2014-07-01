package com.nictusa.fecprint.layout;

import org.apache.commons.lang.text.StrTokenizer;

public class FieldProperties {	
	int pos;
	FieldDataSource dataSource = FieldDataSource.PAGE; 
	FieldType type = FieldType.STRING;
	String lookupKey;
	String expression;
	public int getPos() {
		return pos;
	}
	
	public FieldType getType() {
		return type;
	}
	
	public FieldDataSource getDataSource() {
		return dataSource;
	}
	
	public static FieldProperties parse(String definition)
	{
		FieldProperties f = new FieldProperties();
		StrTokenizer tok = new StrTokenizer(definition,",");
		if(tok.hasNext())
		{
			String s = (String)tok.next();
			if(s.startsWith("CVR@"))
			{
				f.dataSource = FieldDataSource.COVER;
				s = s.substring(4);
			}
			else if(s.startsWith("ST@"))
			{
				f.dataSource = FieldDataSource.SUBTOTAL;
				s = s.substring(3);
			}
			else if(s.startsWith("TOT@"))
			{
				f.dataSource = FieldDataSource.TOTAL;
				s = s.substring(4);
			}
			else if(s.startsWith("JS@"))
			{
				f.dataSource = FieldDataSource.JS;
				f.expression = s.substring(3);
				s = "0";
			}
			f.pos = Integer.parseInt(s);
		}
		if(tok.hasNext())
		{
			String tt = (String)tok.next();
			if(tt.contains("LKP/"))
			{
				f.type = FieldType.TRANSFORM;
				f.lookupKey = tt.substring(tt.indexOf("/") + 1);				
			}
			else if(f.dataSource == FieldDataSource.JS)
			{
				f.expression = f.expression + "," + tt; 
			}
			else
			{
				f.type= FieldType.valueOf(tt);
			}
		}
		return f;
	}

	public String getLookupKey() {
		return lookupKey;
	}

	@Override
	public String toString() {
		return "FieldProperties [dataSource=" + dataSource + ", lookupKey="
				+ lookupKey + ", pos=" + pos + ", type=" + type + "]";
	}

	public String getExpression() {
		return expression;
	}

	
}
