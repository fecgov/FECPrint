/**
 * 
 */
package gov.fec.efo.fecprint.layout;

import java.util.ArrayList;

/**
 * @author Milind
 * 
 */
public abstract class PageInstanceManager {

	protected ArrayList<PageInstance> pages = null;

	protected PageDef pageDef = null;

	private String recName;

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public PageInstanceManager(PageDef def, String normalizedName) {
		pageDef = def;
		pages = new ArrayList<PageInstance>();
		recName = normalizedName;
	}

	public PageInstance addData(ArrayList<String> data) {
		if (pageDef.isHasRepeatedRecords() == false) {
			PageInstance inst = getPagePageInstance();
			inst.addData(data);
			pages.add(inst);
		} else {
			PageInstance inst = null;
			if (pages.size() == 0) {
				inst = getPagePageInstance();
				//inst.addData(data);
				pages.add(inst);
			}
			inst = pages.get(pages.size() - 1);
			if (inst.getRecordsAddedOnPage() < pageDef.getNoOfRepeatedRecords()) {
				inst.addData(data);
			} else {
				inst = getPagePageInstance();
				inst.addData(data);
				pages.add(inst);
			}
		}
		return pages.get(pages.size() - 1);
	}

	public ArrayList<PageInstance> getPages() {
		return pages;
	}

	public PageDef getPageDefinition() {
		return pageDef;
	}
	
	public abstract PageInstance getPagePageInstance();

}
