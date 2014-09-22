package bASAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

public class CorrelationAnalysisRecord {
	
	private String testType;
	private ArrayList<String> compositoin;
	private ArrayList<ArrayList<String>> dopantWaveLengthMartrix;
	private ArrayList<Double> correlaitonCoef;
	
	public ArrayList<String> getCompositoin() {
		return compositoin;
	}
	public void setCompositoin(ArrayList<String> compositoin) {
		this.compositoin = compositoin;
	}
	public ArrayList<ArrayList<String>> getWaveLengthCorrelation() {
		return dopantWaveLengthMartrix;
	}
	public void setWaveLengthCorrelation(
			ArrayList<ArrayList<String>> dopantWaveLengthMartrix) {
		this.dopantWaveLengthMartrix = dopantWaveLengthMartrix;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public ArrayList<ArrayList<String>> getDopantWaveLengthMartrix() {
		return dopantWaveLengthMartrix;
	}
	public void setDopantWaveLengthMartrix(ArrayList<ArrayList<String>> dopantWaveLengthMartrix) {
		this.dopantWaveLengthMartrix = dopantWaveLengthMartrix;
	}
	public ArrayList<Double> getCorrelaitonCoef() {
		return correlaitonCoef;
	}
	public void setCorrelaiton(ArrayList<Double> correlaiton) {
		this.correlaitonCoef = correlaiton;
	}
	
	
}
