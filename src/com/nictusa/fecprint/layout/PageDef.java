package com.nictusa.fecprint.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public abstract class PageDef {
	
	boolean hasRepeatedRecords = false;
	
	int noOfRepeatedRecords = 0;
	
	public PageDef() 
	{
		
	}
	
	public PageDef(boolean repeated, int noOfTimes) 
	{
		hasRepeatedRecords = repeated;
		noOfRepeatedRecords = noOfTimes;
	}
	
	
	public boolean isHasRepeatedRecords() {
		return hasRepeatedRecords;
	}

	public void setHasRepeatedRecords(boolean hasRepeatedRecords) {
		this.hasRepeatedRecords = hasRepeatedRecords;
	}


	

	public int getNoOfRepeatedRecords() {
		return noOfRepeatedRecords;
	}

	public void setNoOfRepeatedRecords(int noOfRepeatedRecords) {
		this.noOfRepeatedRecords = noOfRepeatedRecords;
	}	
	
	public abstract Hashtable<String,Integer> getFieldDefs();
	
	public abstract String getTemplateFileName();
	
	public String toString()
	{
		return getFieldDefs().toString();
	}
	
	
}