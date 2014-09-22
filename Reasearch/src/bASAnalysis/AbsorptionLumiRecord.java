package bASAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

public class AbsorptionLumiRecord {
	
	private String referenceTitle;
	private String number;
	private ArrayList<String> compositionList = new ArrayList<>();;
	private ArrayList<String> compositionContent = new ArrayList<>();;
	private String materialType;
	private String testType;
	private ArrayList<String> absorptionWavelength = new ArrayList<>();;
	private String pumpWavelength;
	private HashMap<String, String> luminescenceWavelength = new HashMap<>(); // key: wavelength; value: FWHM 
	
	public AbsorptionLumiRecord(){
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getReferenceTitle() {
		return referenceTitle;
	}

	public void setReferenceTitle(String referenceTitle) {
		this.referenceTitle = referenceTitle;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public ArrayList<String> getAbsorptionWavelength() {
		return absorptionWavelength;
	}

	public void setAbsorptionWavelength(ArrayList<String> absorptionWavelength) {
		this.absorptionWavelength = absorptionWavelength;
	}

	public HashMap<String, String> getLuminescenceWavelength() {
		return luminescenceWavelength;
	}

	public void setLuminescenceWavelength(
			HashMap<String, String> luminescenceWavelength) {
		this.luminescenceWavelength = luminescenceWavelength;
	}
	
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

	public ArrayList<String> getCompositionList() {
		return compositionList;
	}

	public void setCompositionList(ArrayList<String> compositionList) {
		this.compositionList = compositionList;
	}

	public ArrayList<String> getCompositionContent() {
		return compositionContent;
	}

	public void setCompositionContent(ArrayList<String> compositionContent) {
		this.compositionContent = compositionContent;
	}

}
