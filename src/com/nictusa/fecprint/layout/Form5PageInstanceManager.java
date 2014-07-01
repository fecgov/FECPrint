/**
 * 
 */
package com.nictusa.fecprint.layout;

import java.util.ArrayList;

import com.nictusa.fecprint.layout.Form5PageDef;

/**
 * @author Milind
 *
 */
public class Form5PageInstanceManager extends PageInstanceManager{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public Form5PageInstanceManager(String normalizedName)
	{
		super(new Form5PageDef(),normalizedName);
	}
	
	public PageInstance getPagePageInstance()
	{
		return new Form5PageInstance(pageDef);
	}

}
