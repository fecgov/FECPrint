package com.nictusa.fecprint.layout;

import java.util.HashMap;

public class PageManagerFactory {

	private static PageManagerFactory singleton;
	private HashMap<String, PageInstanceManager> pageManagers; 
	
	/**
	 * @param args
	 */	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private PageManagerFactory()
	{
		pageManagers = new HashMap<String, PageInstanceManager>();
	}
	
	public static PageManagerFactory getInstance()
	{
		if(singleton == null)
		{
			singleton = new PageManagerFactory();
		}
		return singleton;
		
	}
	
	public PageInstanceManager getPageInstanceManager(String recordName)
	{		
		String normalizedName = recordName.toUpperCase();
		int pageid = -1;
		PageInstanceManager page = null;
		if(normalizedName.equalsIgnoreCase("F5A") || normalizedName.equalsIgnoreCase("F5N"))
		{
			pageid = Form5PageDef.PAGE_ID;
		}
		else if(normalizedName.equalsIgnoreCase("F56"))
		{
			pageid = Form56PageDef.PAGE_ID;
		}
		else if(normalizedName.equalsIgnoreCase("F57"))
		{
			pageid = Form57PageDef.PAGE_ID;
		}
		if((page = pageManagers.get(normalizedName)) == null)
		{
			switch(pageid)
			{
				case Form5PageDef.PAGE_ID :
					page = new Form5PageInstanceManager(normalizedName);
					break;
				case Form56PageDef.PAGE_ID :
					page = new Form56PageInstanceManager(normalizedName);
					break;
				case Form57PageDef.PAGE_ID :
					page = new Form57PageInstanceManager(normalizedName);
					break;
				default:
					//TODO
					;
			}
			
			if(page != null)
			{
				pageManagers.put(normalizedName, page);
			}
		}
		return page;
	}
	
	
	public HashMap<String, PageInstanceManager> getAll()
	{
		return  pageManagers;
	}
	
	

}
