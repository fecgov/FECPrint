package com.nictusa.fecprint.layout;

import java.util.List;

public class Form57PageInstance extends PageInstance {

	public Form57PageInstance(PageDef def) {
		super(def);
	}

	public String doDataProcessing(int index, String data, List<String> rawData) {
		String ret = "";

		switch (index) {
		case 3: {
			ret = "Transaction Id : " +  data;
		}
		break;
		case 4: {
			ret = getEntityName(index, data, rawData);
		}
			break;
		case 27: {
			ret = getIndividualName(index, data, rawData);
		}
			break;
		default:
			ret = data;
			break;
		}
		return ret;
	}

}
