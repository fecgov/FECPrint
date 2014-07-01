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
public class Form57PageDef  extends PageDef {
	
	public static final int PAGE_ID = 5007;
	
	/*Single occurrence per page - Start*/	
	public static String PAYEE_ORGANIZATION_NAME_1 = "PAYEE_ORGANIZATION_NAME_1";
	public static String TRANSACTION_ID_NUMBER_1 = "TRANSACTION_ID_NUMBER_1";
	public static String PAYEE_STREET_1 = "PAYEE_STREET_1";	
	public static String PAYEE_CITY_1 = "PAYEE_CITY_1";
	public static String PAYEE_STATE_1 = "PAYEE_STATE_1";
	public static String PAYEE_ZIP_1 = "PAYEE_ZIP_1";
	public static String EXPENDITURE_AMOUNT_1 = "EXPENDITURE_AMOUNT_1";	
	public static String CALENDAR_YTD_PER_ELECTIONOFFICE_1 = "CALENDAR_YTD_PER_ELECTIONOFFICE_1";
	public static String EXPENDITURE_PURPOSE_DESCRIP_1 = "EXPENDITURE_PURPOSE_DESCRIP_1";
	public static String CATEGORY_CODE_1 = "CATEGORY_CODE_1";
	public static String SO_CANDIDATE_NAME_1 = "SO_CANDIDATE_NAME_1";
	public static String CANDIDATE_OFFICE_1 = "CANDIDATE_OFFICE_1";
	public static String SUPPORT_OPPOSE_CODE_1 = "SUPPORT_OPPOSE_CODE_1";
	
	public static String PAYEE_ORGANIZATION_NAME_2 = "PAYEE_ORGANIZATION_NAME_2";	
	public static String TRANSACTION_ID_NUMBER_2 = "TRANSACTION_ID_NUMBER_2";
	public static String PAYEE_STREET_2 = "PAYEE_STREET_2";	
	public static String PAYEE_CITY_2 = "PAYEE_CITY_2";
	public static String PAYEE_STATE_2 = "PAYEE_STATE_2";
	public static String PAYEE_ZIP_2 = "PAYEE_ZIP_2";
	public static String EXPENDITURE_AMOUNT_2 = "EXPENDITURE_AMOUNT_2";	
	public static String CALENDAR_YTD_PER_ELECTIONOFFICE_2 = "CALENDAR_YTD_PER_ELECTIONOFFICE_2";
	public static String EXPENDITURE_PURPOSE_DESCRIP_2 = "EXPENDITURE_PURPOSE_DESCRIP_2";
	public static String CATEGORY_CODE_2 = "CATEGORY_CODE_2";
	public static String SO_CANDIDATE_NAME_2 = "SO_CANDIDATE_NAME_2";
	public static String CANDIDATE_OFFICE_2 = "CANDIDATE_OFFICE_2";
	public static String SUPPORT_OPPOSE_CODE_2 = "SUPPORT_OPPOSE_CODE_2";
	
	public static String PAYEE_ORGANIZATION_NAME_3 = "PAYEE_ORGANIZATION_NAME_3";
	public static String TRANSACTION_ID_NUMBER_3 = "TRANSACTION_ID_NUMBER_3";
	public static String PAYEE_STREET_3 = "PAYEE_STREET_3";	
	public static String PAYEE_CITY_3 = "PAYEE_CITY_3";
	public static String PAYEE_STATE_3 = "PAYEE_STATE_3";
	public static String PAYEE_ZIP_3 = "PAYEE_ZIP_3";
	public static String EXPENDITURE_AMOUNT_3 = "EXPENDITURE_AMOUNT_3";	
	public static String CALENDAR_YTD_PER_ELECTIONOFFICE_3 = "CALENDAR_YTD_PER_ELECTIONOFFICE_3";
	public static String EXPENDITURE_PURPOSE_DESCRIP_3 = "EXPENDITURE_PURPOSE_DESCRIP_3";
	public static String CATEGORY_CODE_3 = "CATEGORY_CODE_3";
	public static String SO_CANDIDATE_NAME_3 = "SO_CANDIDATE_NAME_3";
	public static String CANDIDATE_OFFICE_3 = "CANDIDATE_OFFICE_3";
	public static String SUPPORT_OPPOSE_CODE_3 = "SUPPORT_OPPOSE_CODE_3";
	
	/*Single occurrence per page - End*/
	
	
	private static Hashtable<String,Integer> fields;
	static
	{
		fields = new Hashtable<String,Integer>();
		int offsetMultiplier = 0;
		
		fields.put(TRANSACTION_ID_NUMBER_1,3);	
		fields.put(PAYEE_ORGANIZATION_NAME_1,4);		
		fields.put(PAYEE_STREET_1,11);		
		fields.put(PAYEE_CITY_1,13);
		fields.put(PAYEE_STATE_1,14);
		fields.put(PAYEE_ZIP_1,15);
		fields.put(EXPENDITURE_AMOUNT_1,19);
		fields.put(CALENDAR_YTD_PER_ELECTIONOFFICE_1,20);	
		fields.put(EXPENDITURE_PURPOSE_DESCRIP_1,22);
		fields.put(CATEGORY_CODE_1,23);
		fields.put(SO_CANDIDATE_NAME_1,27);
		fields.put(CANDIDATE_OFFICE_1,32);
		fields.put(SUPPORT_OPPOSE_CODE_1,25);
		
		offsetMultiplier++;
		fields.put(TRANSACTION_ID_NUMBER_2,3 + (offsetMultiplier * 34));	
		fields.put(PAYEE_ORGANIZATION_NAME_2,4  + (offsetMultiplier * 34));		
		fields.put(PAYEE_STREET_2,11  + (offsetMultiplier * 34));		
		fields.put(PAYEE_CITY_2,13  + (offsetMultiplier * 34));
		fields.put(PAYEE_STATE_2,14  + (offsetMultiplier * 34));
		fields.put(PAYEE_ZIP_2,15  + (offsetMultiplier * 34));
		fields.put(EXPENDITURE_AMOUNT_2,19  + (offsetMultiplier * 34));
		fields.put(CALENDAR_YTD_PER_ELECTIONOFFICE_2,20  + (offsetMultiplier * 34));
		fields.put(EXPENDITURE_PURPOSE_DESCRIP_2,22  + (offsetMultiplier * 34));
		fields.put(CATEGORY_CODE_2,23  + (offsetMultiplier * 34));
		fields.put(SO_CANDIDATE_NAME_2,27  + (offsetMultiplier * 34));
		fields.put(CANDIDATE_OFFICE_2,32 + (offsetMultiplier * 34));
		fields.put(SUPPORT_OPPOSE_CODE_2,25 + (offsetMultiplier * 34));
		
		offsetMultiplier++;
		fields.put(PAYEE_ORGANIZATION_NAME_3,4  + (offsetMultiplier * 34));		
		fields.put(TRANSACTION_ID_NUMBER_3,3 + (offsetMultiplier * 34));	
		fields.put(PAYEE_STREET_3,11  + (offsetMultiplier * 34));		
		fields.put(PAYEE_CITY_3,13  + (offsetMultiplier * 34));
		fields.put(PAYEE_STATE_3,14  + (offsetMultiplier * 34));
		fields.put(PAYEE_ZIP_3,15  + (offsetMultiplier * 34));
		fields.put(EXPENDITURE_AMOUNT_3,19  + (offsetMultiplier * 34));
		fields.put(CALENDAR_YTD_PER_ELECTIONOFFICE_3,20  + (offsetMultiplier * 34));
		fields.put(EXPENDITURE_PURPOSE_DESCRIP_3,22  + (offsetMultiplier * 34));
		fields.put(CATEGORY_CODE_3,23  + (offsetMultiplier * 34));
		fields.put(SO_CANDIDATE_NAME_3,27  + (offsetMultiplier * 34));
		fields.put(CANDIDATE_OFFICE_3,32 + (offsetMultiplier * 34));
		fields.put(SUPPORT_OPPOSE_CODE_3,25 + (offsetMultiplier * 34));
		
		
	}	
	
	public Form57PageDef()
	{
		super(true,3);
	}
	public Hashtable<String,Integer> getFieldDefs()
	{
		return fields;
	}
	
	public String getTemplateFileName()
	{
		return "F57.pdf";
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
