package bASAnalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import commonTool.CsvUtils;
import commonTool.FindCombination;

public class FindRelationBetweenDopantAndAbsorptionLumiWavelength_v2 {

	private static Integer wavelengthResolutoin = 5;
	private static Integer wavelengthStart = 300;
	private static Integer wavelengthEnd = 2000;
	private static ArrayList<String> waveLength = new ArrayList<>();

	public static void main(String args[]) {

		// 0. set waveLength resolution
		for (int i = wavelengthStart; i <= wavelengthEnd; i = i
				+ wavelengthResolutoin) {
			waveLength.add(i + "");
		}

		// 1. read rawData From csv
		List<List<String>> rawData = readRawData();

		// 2. read composition from csv
		ArrayList<String> composition = getComposition(new ArrayList<>(
				rawData.get(0)));

		// 2. put the data into model
		AbsorptionLumiRecordList recordList = transferDataIntoRecordList(rawData);

		// 3. Analysis
		CorrelationAnalysisRecordList correlaitonAnalysisRecordList = analysisCorrelaiton(
				recordList, composition);

		// 4. Output result
	}

	public static List<List<String>> readRawData() {
		List<List<String>> rawData = CsvUtils
				.read(new File(
						"C:\\Users\\purewin7\\workspace\\Reasearch\\src\\bASAnalysis\\Ding_CSV.csv"));
		for (List<String> listString : rawData) {
			for (String ele : listString) {
				System.out.print(ele + ", ");
			}
			System.out.println();
		}
		return rawData;
	}

	public static ArrayList<String> getComposition(ArrayList<String> input) {
		ArrayList<String> result = new ArrayList<>();
		for (int i = 3; i < input.size(); i++) {
			if (!input.get(i).equals("Test")) {
				result.add(input.get(i));
			} else {
				break;
			}
		}
		return result;
	}

	public static AbsorptionLumiRecordList transferDataIntoRecordList(
			List<List<String>> input) {
		// 1. get col. number for composition
		Integer titleCol = 0;
		Integer numberCol = 1;
		Integer typeCol = 2;
		Integer compositionColStart = 3;
		Integer compositionColEnd = 0;
		for (int i = 0; i < input.get(0).size(); i++) {
			if (input.get(0).get(i).equals("Test")) {
				compositionColEnd = i - 1;
			}
		}
		Integer testCol = compositionColEnd + 1;
		Integer pumpWavelengthCol = testCol + 1;
		Integer resultColStart = pumpWavelengthCol + 1;
		ArrayList<String> compositionTag = new ArrayList<>();
		for (int i = compositionColStart; i <= compositionColEnd; i++) {
			compositionTag.add(input.get(0).get(i));
		}

		// 2. put into list
		AbsorptionLumiRecordList recordList = new AbsorptionLumiRecordList();
		for (int i = 0; i < input.get(0).size(); i++) {
			AbsorptionLumiRecord record = new AbsorptionLumiRecord();
			record.setReferenceTitle(input.get(i).get(titleCol));
			record.setNumber(input.get(i).get(numberCol));
			record.setMaterialType(input.get(i).get(typeCol));
			for (int j = compositionColStart; j <= compositionColEnd; j++) {
				record.getCompostoin().put(input.get(0).get(j),
						input.get(i).get(j));
			}
			record.setTestType(input.get(i).get(testCol));
			if (record.getTestType().equals("A")) {
				for (int j = resultColStart; j < input.get(i).size(); j++) {
					record.getAbsorptionWavelength().add(input.get(i).get(j));
				}
			} else {
				record.setPumpWavelength(input.get(i).get(pumpWavelengthCol));
				for (int j = resultColStart; j < input.get(i).size(); j++) {
					if (j < input.get(i).size() - 1) {
						if (input.get(i).get(j + 1).contains("-")) {
							record.getLuminescenceWavelength().put(
									input.get(i).get(j),
									input.get(i)
											.get(j + 1)
											.substring(
													1,
													input.get(i).get(j + 1)
															.length()));
							j++;
						} else {
							record.getLuminescenceWavelength().put(
									input.get(i).get(j), "0");
						}
					} else {
						record.getLuminescenceWavelength().put(
								input.get(i).get(j), "0");
					}
				}
			}
			recordList.getRecordList().add(record);
		}
		return recordList;
	}

	public static CorrelationAnalysisRecordList analysisCorrelaiton(
			AbsorptionLumiRecordList input, ArrayList<String> composition) {

		CorrelationAnalysisRecordList result = new CorrelationAnalysisRecordList();

		// 1. extract all composition combination, and get List
		ArrayList<String> compositionList = new ArrayList<>(input
				.getRecordList().get(0).getCompostoin().keySet());
		ArrayList<String> numberList = new ArrayList<>();
		for (int i = 0; i < compositionList.size(); i++) {
			numberList.add(i + "");
		}
		Integer combinationNumber = 3;
		ArrayList<ArrayList<String>> combinationList = new ArrayList<>();
		for (int i = 1; i <= combinationNumber; i++) {
			ArrayList<String> combinationResult = FindCombination.combine(
					numberList, combinationNumber);
			for (String obj : combinationResult) {
				ArrayList<String> oneList = new ArrayList<>();
				for (String element : obj.split(" ")) {
					oneList.add(element);
				}
				combinationList.add(oneList);
			}
		}

		// 2. build model with composition and absorption/luminescence model
		String[] testType = new String[] { "A", "L" };
		for (String type : testType) {
			for (ArrayList<String> combination : combinationList) {
				ArrayList<String> oneComposition = new ArrayList<>();
				for (String obj : combination) {
					oneComposition.add(composition.get(Integer.valueOf(obj)));
				}
				ArrayList<AnalysisRecord> analysisRecordList = new ArrayList<>();
				for (AbsorptionLumiRecord record : input.getRecordList()) {
					if (ifHasComposition(record, oneComposition)) {
						AnalysisRecord oneAnalysisRecord = new AnalysisRecord();
						oneAnalysisRecord.setDopantList(oneComposition);
						oneAnalysisRecord.setTestType(type);
						if (type.equals("A")) {
							for (String abosrption : record
									.getAbsorptionWavelength()) {
								oneAnalysisRecord.getPeakWaveLength().add(
										fitWavelength(abosrption));
							}
						} else {
							oneAnalysisRecord.setPumpWavelength(record
									.getPumpWavelength());
							for (String Lumi : record
									.getLuminescenceWavelength().keySet()) {
								oneAnalysisRecord.getPeakWaveLength().add(
										fitWavelength(Lumi));
							}
						}
						analysisRecordList.add(oneAnalysisRecord);
					}
				}
				// 3. calculate the correlation coefficient
				if (analysisRecordList.size() > 0) {
					result.getRecordList().add(
							correlationAnalysis(analysisRecordList));
				}
			}
		}
		return result;
	}

	public static CorrelationAnalysisRecord correlationAnalysis(
			ArrayList<AnalysisRecord> input) {
		
		

		return null;
	}

	public static Boolean ifHasComposition(AbsorptionLumiRecord record,
			ArrayList<String> oneComposition) {
		ArrayList<String> recordComposition = new ArrayList<>();
		for (Entry<String, String> obj : record.getCompostoin().entrySet()) {
			if (!obj.getValue().equals("N/A")) {
				recordComposition.add(obj.getKey());
			}
		}
		Integer sameNumber = 0;
		for (String obj : oneComposition) {
			if (recordComposition.contains(obj)) {
				sameNumber++;
			}
		}
		return (oneComposition.size() == sameNumber);
	}

	public static String fitWavelength(String input) {
		Integer inputWavelength = Integer.valueOf(input);
		String result = "";
		for (int i = 0; i < waveLength.size(); i++) {
			if (inputWavelength < Integer.valueOf(waveLength.get(i))) {
				result = waveLength.get(i);
			}
		}
		return result;
	}

	public static void outputResult(CorrelationAnalysisRecordList input) {
		// TODO
	}

}
