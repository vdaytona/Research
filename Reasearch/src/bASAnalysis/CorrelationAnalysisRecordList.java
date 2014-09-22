package bASAnalysis;

import java.util.ArrayList;

public class CorrelationAnalysisRecordList {
	
	private ArrayList<CorrelationAnalysisRecord> recordList = new ArrayList<>();
	private String testType;

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public ArrayList<CorrelationAnalysisRecord> getRecordList() {
		return recordList;
	}

	public void setRecordList(ArrayList<CorrelationAnalysisRecord> recordList) {
		this.recordList = recordList;
	}
}
