package com.nictusa.fecprint.layout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nictusa.fecprint.data.BaseRecordType;
import com.nictusa.fecprint.data.DataBuilder;
import com.nictusa.fecprint.utility.ThreadSafeSimpleDateFormat;

public class PageManager {
	
	private Page[] pages;
	private Map<String,String> presetData = null;
	private boolean stampAsElectronicallyFiled = false;
	
	private long startingImageNumer = 1;
	private String timeStamp = null;
	
	public final ThreadSafeSimpleDateFormat inputDateFormat = new ThreadSafeSimpleDateFormat("yyyyMMdd"); 
	public final ThreadSafeSimpleDateFormat uiDateFormat = new ThreadSafeSimpleDateFormat("MM/dd/yyyy");
	public final ThreadSafeSimpleDateFormat uiDateFormat_DD = new ThreadSafeSimpleDateFormat("dd");
	public final ThreadSafeSimpleDateFormat uiDateFormat_MM = new ThreadSafeSimpleDateFormat("MM");
	public final ThreadSafeSimpleDateFormat uiDateFormat_YYYY = new ThreadSafeSimpleDateFormat("yyyy");	
	
	DataBuilder dataBuilder;
	
	public PageManager(int totalPages,DataBuilder db, long startingImgNo, String tmStamp)
	{
		pages = new Page[totalPages];
		dataBuilder = db;
		startingImageNumer = startingImgNo;
		presetData = new HashMap<String,String>();
		timeStamp = tmStamp;
		
	}
	
	public Page getPage(BaseRecordType formType, BaseRecordType pageType,int pageNum) throws IOException
	{
		if(pages[pageNum - 1] == null)
		{
			pages[pageNum -1] = PageFactory.getInstance().getNewPage(formType, pageType, pageNum,this);
		}
		return pages[pageNum - 1];
	}
	
	public Page getPage(int pageNo)
	{
		if(pageNo <= pages.length )
			return pages[pageNo -1];
		else
			return null;
	}
	
	public int getTotalPages()
	{
		return pages.length;
	}

	public DataBuilder getDataBuilder() {
		return dataBuilder;
	}

	public void setDataBuilder(DataBuilder dataBuilder) {
		this.dataBuilder = dataBuilder;
	}

	public long getStartingImageNumer() {
		return startingImageNumer;
	}

	public void setStartingImageNumer(long startingImageNumer) {
		startingImageNumer = startingImageNumer;
	}

	public Map<String, String> getPresetData() {
		return presetData;
	}

	public boolean isStampAsElectronicallyFiled() {
		return stampAsElectronicallyFiled;
	}

	public void setStampAsElectronicallyFiled(boolean stampAsElectronicallyFiled) {
		this.stampAsElectronicallyFiled = stampAsElectronicallyFiled;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
}
