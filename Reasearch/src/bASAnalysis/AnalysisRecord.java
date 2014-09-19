package bASAnalysis;

import java.util.ArrayList;

public class AnalysisRecord {
	
	private String testType;
	private String pumpWavelength;
	private ArrayList<String> dopantList;
	private ArrayList<String> peakWaveLength;
	
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getPumpWavelength() {
		return pumpWavelength;
	}
	public void setPumpWavelength(String pumpWavelength) {
		this.pumpWavelength = pumpWavelength;
	}
	public ArrayList<String> getDopantList() {
		return dopantList;
	}
	public void setDopantList(ArrayList<String> dopantList) {
		this.dopantList = dopantList;
	}
	public ArrayList<String> getPeakWaveLength() {
		return peakWaveLength;
	}
	public void setPeakWaveLength(ArrayList<String> peakWaveLength) {
		this.peakWaveLength = peakWaveLength;
	}
}
