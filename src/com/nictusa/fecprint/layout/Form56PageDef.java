/**
 * 
 */
package com.nictusa.fecprint.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Milind
 *
 */
public class Form56PageDef extends PageDef {
	
	public static final int PAGE_ID = 5006;
	
	/*Single occurrence per page - Start*/	
	public static String CONTRIBUTOR_NAME_1  = "CONTRIBUTOR_NAME_1";
	public static String CONTRIBUTOR_STREET_1 = "CONTRIBUTOR_STREET_1";
	public static String CONTRIBUTOR_CITY_1 = "CONTRIBUTOR_CITY_1";
	public static String CONTRIBUTOR_STATE_1 = "CONTRIBUTOR_STATE_1";
	public static String CONTRIBUTOR_ZIP_1 = "CONTRIBUTOR_ZIP_1";
	public static String CONTRIBUTOR_COMMITTEE_FEC_ID_1 = "CONTRIBUTOR_COMMITTEE_FEC_ID_1";
	public static String CONTRIBUTION_DATE_1  = "CONTRIBUTION_DATE_1";
	public static String CONTRIBUTION_AMOUNT_1 = "CONTRIBUTION_AMOUNT_1";
	public static String CONTRIBUTOR_EMPLOYER_1 = "CONTRIBUTOR_EMPLOYER_1";
	public static String CONTRIBUTOR_OCCUPATION_1 = "CONTRIBUTOR_OCCUPATION_1";
	
	public static String CONTRIBUTOR_NAME_2  = "CONTRIBUTOR_NAME_2";
	public static String CONTRIBUTOR_STREET_2 = "CONTRIBUTOR_STREET_2";
	public static String CONTRIBUTOR_CITY_2 = "CONTRIBUTOR_CITY_2";
	public static String CONTRIBUTOR_STATE_2 = "CONTRIBUTOR_STATE_2";
	public static String CONTRIBUTOR_ZIP_2 = "CONTRIBUTOR_ZIP_2";
	public static String CONTRIBUTOR_COMMITTEE_FEC_ID_2 = "CONTRIBUTOR_COMMITTEE_FEC_ID_2";
	public static String CONTRIBUTION_DATE_2  = "CONTRIBUTION_DATE_2";
	public static String CONTRIBUTION_AMOUNT_2 = "CONTRIBUTION_AMOUNT_2";
	public static String CONTRIBUTOR_EMPLOYER_2 = "CONTRIBUTOR_EMPLOYER_2";
	public static String CONTRIBUTOR_OCCUPATION_2 = "CONTRIBUTOR_OCCUPATION_2";
	
	public static String CONTRIBUTOR_NAME_3  = "CONTRIBUTOR_NAME_3";
	public static String CONTRIBUTOR_STREET_3 = "CONTRIBUTOR_STREET_3";
	public static String CONTRIBUTOR_CITY_3 = "CONTRIBUTOR_CITY_3";
	public static String CONTRIBUTOR_STATE_3 = "CONTRIBUTOR_STATE_3";
	public static String CONTRIBUTOR_ZIP_3 = "CONTRIBUTOR_ZIP_3";
	public static String CONTRIBUTOR_COMMITTEE_FEC_ID_3 = "CONTRIBUTOR_COMMITTEE_FEC_ID_3";
	public static String CONTRIBUTION_DATE_3  = "CONTRIBUTION_DATE_3";
	public static String CONTRIBUTION_AMOUNT_3 = "CONTRIBUTION_AMOUNT_3";
	public static String CONTRIBUTOR_EMPLOYER_3 = "CONTRIBUTOR_EMPLOYER_3";
	public static String CONTRIBUTOR_OCCUPATION_3 = "CONTRIBUTOR_OCCUPATION_3";
	
	public static String CONTRIBUTOR_NAME_4  = "CONTRIBUTOR_NAME_4";
	public static String CONTRIBUTOR_STREET_4 = "CONTRIBUTOR_STREET_4";
	public static String CONTRIBUTOR_CITY_4 = "CONTRIBUTOR_CITY_4";
	public static String CONTRIBUTOR_STATE_4 = "CONTRIBUTOR_STATE_4";
	public static String CONTRIBUTOR_ZIP_4 = "CONTRIBUTOR_ZIP_4";
	public static String CONTRIBUTOR_COMMITTEE_FEC_ID_4 = "CONTRIBUTOR_COMMITTEE_FEC_ID_4";
	public static String CONTRIBUTION_DATE_4  = "CONTRIBUTION_DATE_4";
	public static String CONTRIBUTION_AMOUNT_4 = "CONTRIBUTION_AMOUNT_4";
	public static String CONTRIBUTOR_EMPLOYER_4 = "CONTRIBUTOR_EMPLOYER_4";
	public static String CONTRIBUTOR_OCCUPATION_4 = "CONTRIBUTOR_OCCUPATION_4";
	
	/*Single occurrence per page - End*/
	
	
	private static Hashtable<String,Integer> fields;
	static
	{
		fields = new Hashtable<String,Integer>();
		
		int offsetMultiplier = 0;
		
		fields.put(CONTRIBUTOR_NAME_1,4 );
		fields.put(CONTRIBUTOR_STREET_1,11);		
		fields.put(CONTRIBUTOR_CITY_1,13);
		fields.put(CONTRIBUTOR_STATE_1,14);
		fields.put(CONTRIBUTOR_ZIP_1,15);
		fields.put(CONTRIBUTOR_COMMITTEE_FEC_ID_1,16);
		fields.put(CONTRIBUTION_DATE_1, 17 );
		fields.put(CONTRIBUTION_AMOUNT_1, 18);
		fields.put(CONTRIBUTOR_EMPLOYER_1, 19);
		fields.put(CONTRIBUTOR_OCCUPATION_1, 20);
		
		offsetMultiplier++;
		fields.put(CONTRIBUTOR_NAME_2,4 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_STREET_2,11 + (offsetMultiplier * 20));		
		fields.put(CONTRIBUTOR_CITY_2,13 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_STATE_2,14 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_ZIP_2,15 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_COMMITTEE_FEC_ID_2,16 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTION_DATE_2, 17 + (offsetMultiplier * 20) );
		fields.put(CONTRIBUTION_AMOUNT_2, 18 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_EMPLOYER_2, 19 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_OCCUPATION_2, 20 + (offsetMultiplier * 20));
		
		offsetMultiplier++;
		fields.put(CONTRIBUTOR_NAME_3,4 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_STREET_3,11 + (offsetMultiplier * 20));		
		fields.put(CONTRIBUTOR_CITY_3,13 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_STATE_3,14 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_ZIP_3,15 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_COMMITTEE_FEC_ID_3,16 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTION_DATE_3, 17 + (offsetMultiplier * 20) );
		fields.put(CONTRIBUTION_AMOUNT_3, 18 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_EMPLOYER_3, 19 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_OCCUPATION_3, 20 + (offsetMultiplier * 20));
		
		offsetMultiplier++;
		fields.put(CONTRIBUTOR_NAME_4,4 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_STREET_4,11 + (offsetMultiplier * 20));		
		fields.put(CONTRIBUTOR_CITY_4,13 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_STATE_4,14 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_ZIP_4,15 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_COMMITTEE_FEC_ID_4,16 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTION_DATE_4, 17 + (offsetMultiplier * 20) );
		fields.put(CONTRIBUTION_AMOUNT_4, 18 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_EMPLOYER_4, 19 + (offsetMultiplier * 20));
		fields.put(CONTRIBUTOR_OCCUPATION_4, 20 + (offsetMultiplier * 20));
		
	}

	public Form56PageDef()
	{
		super(true,4);
	}
	
	public Hashtable<String,Integer> getFieldDefs()
	{
		return fields;
	}
	
	public String getTemplateFileName()
	{
		return "F56.pdf";
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
