package com.nictusa.fecprint.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageInstance {
	private int pageNo;
	private String imageNo;
	private ArrayList<String> data;
	private PageDef reference;
	private int recordsAddedOnPage;

	public PageInstance(PageDef ref) {
		this.pageNo = -1;
		this.imageNo = "";
		reference = ref;
		recordsAddedOnPage = 0;
		data = new ArrayList<String>();
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getImageNo() {
		return imageNo;
	}

	public void setImageNo(String imageNo) {
		this.imageNo = imageNo;
	}

	public int getRecordsAddedOnPage() {
		return recordsAddedOnPage;
	}

	public void incrementRecordsAddedOnPage() {
		this.recordsAddedOnPage++;
	}

	public ArrayList<String> getData() {
		return data;
	}

	public PageDef getReference() {
		return reference;
	}

	public void setReference(PageDef reference) {
		this.reference = reference;
	}

	public void addData(List<String> aData) {
		if (reference.isHasRepeatedRecords() == true) {
			incrementRecordsAddedOnPage();
		}
		String sData;
		for (int i = 0; i < aData.size(); i++) {
			sData = aData.get(i);
			sData = doDataProcessing(i + 1, sData, aData);
			data.add(sData);
		}
	}

	public String doDataProcessing(int index, String data, List<String> rawData) {
		return data;
	}
	
	public String doRenderProcessing(int index, String name,String data) {
		return data;
	}

	public String getEntityName(int index, String data, List<String> rawData) {
		String ret = "";
		if (data.equals("IND") || data.equals("CAN")) {
			StringBuffer name = new StringBuffer();
			// Prefix
			if (rawData.get(index + 5 - 1) != null) {
				name.append(rawData.get(index + 5 - 1) + " ");
			}
			// First Name
			name.append(rawData.get(index + 3 - 1) + " ");
			// Middle Name
			if (rawData.get(index + 4 - 1) != null) {
				name.append(rawData.get(index + 4 - 1) + " ");
			}
			// Last Name
			name.append(rawData.get(index + 2 - 1));
			if (rawData.get(index + 6 - 1) != null) {
				name.append(rawData.get(index + 6 - 1) + " ");
			}
			ret = name.toString();
		} else {
			ret = rawData.get(index + 1 - 1);
		}
		return ret;
	}

	public String getIndividualName(int index, String data, List<String> rawData) {
		String ret = "";
		StringBuffer name = new StringBuffer();
		// Prefix
		if (rawData.get(index + 3 - 1) != null) {
			name.append(rawData.get(index + 3 - 1) + " ");
		}
		// First Name
		name.append(rawData.get(index + 1 - 1) + " ");
		// Middle Name
		if (rawData.get(index + 2 - 1) != null) {
			name.append(rawData.get(index + 2 - 1) + " ");
		}
		// Last Name
		name.append(rawData.get(index));
		if (rawData.get(index + 4 - 1) != null) {
			name.append(rawData.get(index + 4 - 1) + " ");
		}
		ret = name.toString();

		return ret;
	}

	public String getFromattedDate(String rawDate) {
		if (rawDate.length() > 0) {
			StringBuffer ret = new StringBuffer();
			ret.append(rawDate.substring(4, 6) + "/" + rawDate.substring(6, 8)
					+ "/" + rawDate.substring(0, 4));
			return ret.toString();
		} else
			return rawDate;
	}

	public String getCommitteIDNumber(String rawData) {
		if (rawData.length() > 0) {
			return rawData.substring(1);
		} else
			return rawData;

	}

	public String isReportAnAmendment(String formName) {
		if (formName.length() > 0) {
			if (formName.endsWith("A")) {
				return "Y";
			} else {
				return "N";
			}
		} else
			return formName;
	}
}