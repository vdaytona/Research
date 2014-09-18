package bASAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

public class CorrelationAnalysisRecord {
	
	public static Integer spectralSplit;
	public ArrayList<String> compositoin;
	public HashMap<String,String> waveLengthCorrelation;
	
	public static Integer getSpectralSplit() {
		return spectralSplit;
	}
	public static void setSpectralSplit(Integer spectralSplit) {
		CorrelationAnalysisRecord.spectralSplit = spectralSplit;
	}
	public ArrayList<String> getCompositoin() {
		return compositoin;
	}
	public void setCompositoin(ArrayList<String> compositoin) {
		this.compositoin = compositoin;
	}
	public HashMap<String, String> getWaveLengthCorrelation() {
		return waveLengthCorrelation;
	}
	public void setWaveLengthCorrelation(
			HashMap<String, String> waveLengthCorrelation) {
		this.waveLengthCorrelation = waveLengthCorrelation;
	}
	
	
}
