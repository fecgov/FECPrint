package com.nictusa.fecprint.utility;

import java.util.Hashtable;

public class Constants {
	
	static Hashtable<String, Hashtable<String,String>> lookups = null; 
	
	static
	{
		lookups = new Hashtable<String, Hashtable<String,String>>();
		
		Hashtable<String,String> h = new Hashtable<String,String>();
		h.put("P", "Presidential");
		h.put("S", "Senate");
		h.put("H", "House");		
		lookups.put("OFFCODE", h);
		
		h = new Hashtable<String,String>();
		h.put("Y", "[PERSONAL FUNDS]");
		h.put("N", "");				
		lookups.put("FUNDSCODE", h);
		
		h = new Hashtable<String,String>();
		h.put("P", "Primary");
		h.put("G", "General");				
		h.put("O", "Other");
		h.put("C", "Convention");
		h.put("R", "Runoff");				
		h.put("S", "Special");
		h.put("E", "Recount");
		lookups.put("ELECTIONCODE", h);
		
		h = new Hashtable<String,String>();
		h.put("30P", "Primary");
		h.put("30G", "General");				
		h.put("30R", "Runoff");				
		h.put("30S", "Special");
		h.put("12P", "Primary");
		h.put("12C", "Convention");		
		h.put("12G", "General");				
		h.put("12R", "Runoff");				
		h.put("12S", "Special");
		lookups.put("ELECTIONCODE_FROM_REPORTCODE", h);
		
		h = new Hashtable<String,String>();
		h.put("AIC","AMERICAN INDEPENDENT CONSERVATIVE");
		h.put("AIP","AMERICAN INDEPENDENT PARTY");
		h.put("AMP","AMERICAN PARTY");
		h.put("APF","AMERICAN PEOPLE'S FREEDOM PARTY");
		h.put("CIT","CITIZENS' PARTY");
		h.put("CMD","COMMANDMENTS PARTY");
		h.put("CMP","COMMONWEALTH PARTY OF THE U.S.");
		h.put("COM","COMMUNIST PARTY");
		h.put("CRV","CONSERVATIVE PARTY");
		h.put("CST","CONSTITUTIONAL");
		h.put("D/C","DEMOCRATIC/CONSERVATIVE");
		h.put("DEM","DEMOCRATIC PARTY");
		h.put("DFL","DEMOCRATIC-FARM-LABOR");
		h.put("FLP","FREEDOM LABOR PARTY");
		h.put("GRE","GREEN PARTY");
		h.put("GWP","GEORGE WALLACE PARTY");
		h.put("HRP","HUMAN RIGHTS PARTY");
		h.put("IAP","INDEPENDENT AMERICAN PARTY");
		h.put("ICD","INDEPENDENT CONSERV. DEMOCRATIC");
		h.put("IGD","INDUSTRIAL GOVERNMENT PARTY");
		h.put("IND","INDEPENDENT");
		h.put("LAB","U.S. LABOR PARTY");
		h.put("LBL","LIBERAL PARTY");
		h.put("LBR","LABOR PARTY");
		h.put("LBU","LIBERTY UNION PARTY");
		h.put("LFT","LESS FEDERAL TAXES");
		h.put("LIB","LIBERTARIAN");
		h.put("LRU","LA RAZA UNIDA");
		h.put("NAP","PROHIBITION PARTY");
		h.put("NDP","NATIONAL DEMOCRATIC PARTY");
		h.put("NLP","NATURAL LAW PARTY");
		h.put("NNE","NONE");
		h.put("OTH","OTHER");
		h.put("PAF","PEACE AND FREEDOM");
		h.put("PFD","PEACE FREEDOM PARTY");
		h.put("POP","PEOPLE OVER POLITICS");
		h.put("PPD","PROTEST PROGRESS DIGNITY");
		h.put("PPY","PEOPLE'S PARTY");
		h.put("REF","REFORM PARTY");
		h.put("REP","REPUBLICAN PARTY");
		h.put("RTL","RIGHT TO LIFE");
		h.put("RUP","RAZA UNIDA PARTY");
		h.put("SLP","SOCIALIST LABOR PARTY");
		h.put("SUS","SOCIALIST PARTY U.S.A");
		h.put("SWP","SOCIALIST WORKER'S PARTY");
		h.put("THD","THEO-DEM");
		h.put("TWR","TAXATION WITHOUT REPRESENTATION");
		h.put("TX","TAXPAYERS");
		h.put("UNK","UNKNOWN");
		h.put("USP","U.S. PEOPLE'S PARTY");				
		lookups.put("PTYCODE", h);
	}
	
	
	
	public static String lookup(String lookpupType, String code)
	{
		if(lookups.get(lookpupType) != null)
		{
			if(code != null)
			{
				String transform = lookups.get(lookpupType).get(code);
				return transform != null ?  transform : code;				
			}
		}
		return code;
	}

}
