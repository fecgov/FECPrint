/**
 * 
 */
package gov.fec.efo.fecprint.layout;

import java.util.ArrayList;

import gov.fec.efo.fecprint.layout.Form5PageDef;

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
