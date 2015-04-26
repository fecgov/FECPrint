/**
 * 
 */
package gov.fec.efo.fecprint.layout;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author Milind
 *
 */
public class Form5PageDef extends PageDef{
	
	public static final int PAGE_ID = 5000;
	
	/*Single occurrence per page - Start*/	
	public static String FORM_TYPE = "FORM_TYPE";
	public static String FILER_FEC_ID_NUMBER = "FILER_FEC_ID_NUMBER";
	public static String ORGANIZATION_NAME = "ORGANIZATION_NAME";	
	public static String STREET_1 = "STREET_1";
	public static String STREET_2 = "STREET_2";
	public static String CITY = "CITY";
	public static String STATE = "STATE";
	public static String ZIP = "ZIP";
	//public static String QUALIFIED_NON_PROFIT_CORPORATION = "QUALIFIED_NON_PROFIT_CORPORATION";
	public static String INDIVIDUAL_EMPLOYER = "INDIVIDUAL_EMPLOYER";
	public static String INDIVIDUAL_OCCUPATION = "INDIVIDUAL_OCCUPATION";
	public static String REPORT_CODE = "REPORT_CODE";
	public static String TOTAL_CONTRIBUTION = "TOTAL_CONTRIBUTION";
	public static String TOTAL_INDEPENDENT_EXPENDITURE = "TOTAL_INDEPENDENT_EXPENDITURE";
	public static String PERSON_COMPLETING_NAME = "PERSON_COMPLETING_NAME";
	public static String DATE_SIGNED = "DATE_SIGNED";
	public static String  CHANGE_OF_ADDRESS = "CHANGE_OF_ADDRESS";
	public static String  _24HOUR_48HOUR_CODE = "24HOUR_48HOUR_CODE";
	public static String COVERING_PERIOD_FROM = "COVERING_PERIOD_FROM";
	
	public static String COVERING_PERIOD_TO_MM = "COVERING_PERIOD_TO_MM";
	public static String COVERING_PERIOD_TO_DD = "COVERING_PERIOD_TO_DD";
	public static String COVERING_PERIOD_TO_YY = "COVERING_PERIOD_TO_YY";
	
	
	public static String ORIGINAL_AMENDMENT_DATE_MM = "ORIGINAL_AMENDMENT_DATE_MM";
	public static String ORIGINAL_AMENDMENT_DATE_DD = "ORIGINAL_AMENDMENT_DATE_DD";
	public static String ORIGINAL_AMENDMENT_DATE_YY = "ORIGINAL_AMENDMENT_DATE_YY";
	/*Single occurrence per page - End*/
	
	
	private static Hashtable<String,Integer> fields;
	static
	{
		fields = new Hashtable<String,Integer>();
		fields.put(FORM_TYPE, 1);
		fields.put("FILER_FEC_ID_NUMBER",2);
		fields.put(ORGANIZATION_NAME,3);
		fields.put(CHANGE_OF_ADDRESS,10);
		fields.put(STREET_1,11);
		fields.put(STREET_2,12);
		fields.put(CITY,13);
		fields.put(STATE,14);
		fields.put(ZIP,15);
		//fields.put(QUALIFIED_NON_PROFIT_CORPORATION, 16);
		fields.put(INDIVIDUAL_OCCUPATION, 16);
		
		fields.put(INDIVIDUAL_EMPLOYER,17);
		fields.put(REPORT_CODE,18);
		fields.put(_24HOUR_48HOUR_CODE,19);
		fields.put(ORIGINAL_AMENDMENT_DATE_MM,20);
		fields.put(ORIGINAL_AMENDMENT_DATE_DD,20);
		fields.put(ORIGINAL_AMENDMENT_DATE_YY,20);
		fields.put(COVERING_PERIOD_FROM,21);		
		fields.put(COVERING_PERIOD_TO_MM,22);
		fields.put(COVERING_PERIOD_TO_DD,22);
		fields.put(COVERING_PERIOD_TO_YY,22);
		fields.put(TOTAL_CONTRIBUTION,23);
		fields.put(TOTAL_INDEPENDENT_EXPENDITURE,24);
		fields.put(PERSON_COMPLETING_NAME,25);
		fields.put(DATE_SIGNED,30);
	}	
	
	public Form5PageDef()
	{
		super();
	}	
	
	public Hashtable<String,Integer> getFieldDefs()
	{
		return fields;
	}
	
	public String getTemplateFileName()
	{
		return "F5.pdf";
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
