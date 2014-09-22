package bASAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

public class AbsorptionLumiRecordList {

	private ArrayList<AbsorptionLumiRecord> recordList = new ArrayList<>();

	public ArrayList<AbsorptionLumiRecord> getRecordList() {
		return recordList;
	}

	public void setRecordList(ArrayList<AbsorptionLumiRecord> recordList) {
		this.recordList = recordList;
	}

	public void setRecordList(HashMap<String, AbsorptionLumiRecord> recordList) {
	}

}
