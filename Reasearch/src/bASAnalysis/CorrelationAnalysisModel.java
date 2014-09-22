package bASAnalysis;

import java.util.ArrayList;

public class CorrelationAnalysisModel {
	
	private String pumpWavelength;
	private String typeTest;
	private ArrayList<String> dopantList;
	private ArrayList<String> peakWaveLength;
	
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
	public String getTypeTest() {
		return typeTest;
	}
	public void setTypeTest(String typeTest) {
		this.typeTest = typeTest;
	}
}
