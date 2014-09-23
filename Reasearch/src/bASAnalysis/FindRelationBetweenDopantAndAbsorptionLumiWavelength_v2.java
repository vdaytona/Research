package bASAnalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import commonTool.CorrelationCoefficientCalculator;
import commonTool.CsvUtils;
import commonTool.FindCombination;

public class FindRelationBetweenDopantAndAbsorptionLumiWavelength_v2 {

	private static Integer wavelengthResolutoin = 100;
	private static Integer wavelengthStart = 200;
	private static Integer wavelengthEnd = 2000;
	private static ArrayList<String> waveLength = new ArrayList<>();
	private static ArrayList<String> composition = new ArrayList<>();

	public static void main(String args[]) {

		// 0. set waveLength resolution
		for (int i = wavelengthStart; i <= wavelengthEnd; i = i
				+ wavelengthResolutoin) {
			waveLength.add(i + "");
		}

		// 1. read rawData From csv
		List<List<String>> rawData = readRawData();

		// 2. read composition from csv
		composition = getComposition(new ArrayList<>(rawData.get(0)));

		// 3. put the data into model
		AbsorptionLumiRecordList recordList = transferDataIntoRecordList(rawData);

		// 4. Correlation Analysis
		ArrayList<CorrelationAnalysisRecordList> correlaitonAnalysisRecordList = analysisCorrelaiton(
				recordList, composition);

		// 5. Output result

		outputResult(correlaitonAnalysisRecordList);
		System.out.println("over");

	}

	public static List<List<String>> readRawData() {
		List<List<String>> rawData = CsvUtils.read(new File(
				"./src/bASAnalysis/Ding_CSV.csv"));
		/*
		 * for (List<String> listString : rawData) {
		 * System.out.println(listString.size()); for (String ele : listString)
		 * { System.out.print(ele + ", "); } System.out.println(); }
		 */
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
		for (int i = 1; i < input.size(); i++) {
			AbsorptionLumiRecord record = new AbsorptionLumiRecord();
			record.setReferenceTitle(input.get(i).get(titleCol));
			record.setNumber(input.get(i).get(numberCol));
			record.setMaterialType(input.get(i).get(typeCol));
			for (int j = compositionColStart; j <= compositionColEnd; j++) {
				record.getCompositionList().add(input.get(0).get(j));
				record.getCompositionContent().add(input.get(i).get(j));
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

	public static ArrayList<CorrelationAnalysisRecordList> analysisCorrelaiton(
			AbsorptionLumiRecordList input, ArrayList<String> composition) {

		ArrayList<CorrelationAnalysisRecordList> analysisRecordList = new ArrayList<>();

		// 1. build correlation analysis record model
		ArrayList<CorrelationAnalysisModel> modelList = new ArrayList<>();
		for (AbsorptionLumiRecord oneRecord : input.getRecordList()) {
			CorrelationAnalysisModel newModel = new CorrelationAnalysisModel();
			newModel.setTypeTest(oneRecord.getTestType());
			ArrayList<String> componentList = new ArrayList<>();
			for (String compositionObj : oneRecord.getCompositionContent()) {
				if (compositionObj.equals("N/A")) {
					componentList.add("0");
				} else {
					componentList.add("1");
				}
			}
			newModel.setDopantList(componentList);

			newModel.setPeakWaveLength(getZeroWavelengthList());
			if (oneRecord.getTestType().equals("A")) {
				for (String peakWavelength : oneRecord
						.getAbsorptionWavelength()) {
					newModel.getPeakWaveLength()
							.set(findWavelengthIndex(Integer
									.valueOf(peakWavelength)),
									"1");
				}
			} else {
				newModel.setPumpWavelength(oneRecord.getPumpWavelength());
				for (String peakWavelength : oneRecord
						.getLuminescenceWavelength().keySet()) {
					newModel.getPeakWaveLength()
							.set(findWavelengthIndex(Integer
									.valueOf(peakWavelength)),
									"1");

				}
			}
			modelList.add(newModel);
		}

//		for (CorrelationAnalysisModel model : modelList) {
//			System.out.println(model.getTypeTest());
//			System.out.println(model.getDopantList().toString());
//			System.out.println(model.getPeakWaveLength().toString());
//		}

		// 2. getCombination of component
		// return the index (in component list) of the component combination
		ArrayList<ArrayList<String>> componentCombination = getComponentCombination();

		// 3. split correlation calculation group
		String[] testTypeList = new String[] { "A", "L" };
		for (String testType : testTypeList) {
			CorrelationAnalysisRecordList oneAnalysisRecordList = new CorrelationAnalysisRecordList();
			oneAnalysisRecordList.setTestType(testType);
			for (ArrayList<String> oneCombination : componentCombination) {
				CorrelationAnalysisRecord record = new CorrelationAnalysisRecord();
				record.setTestType(testType);
				record.setCompositoin(getCompositionName(oneCombination));
				for (CorrelationAnalysisModel model : modelList) {
					if (model.getTypeTest().equals(testType)) {
						ArrayList<String> wavelengthMatrix = new ArrayList<>();
						wavelengthMatrix.add(ifHasComposition(oneCombination,
								model) ? "1" : "0");
						for (String wavelength : model.getPeakWaveLength()) {
							wavelengthMatrix.add(wavelength);
						}
						record.getDopantWaveLengthMartrix().add(
								wavelengthMatrix);
					}
				}
				if (record.getDopantWaveLengthMartrix().size() > 0) {
					oneAnalysisRecordList.getRecordList().add(record);
				}
			}
			if (oneAnalysisRecordList.getRecordList().size() > 0) {
				analysisRecordList.add(oneAnalysisRecordList);
			}
			
		}
//		for (CorrelationAnalysisRecordList record : analysisRecordList) {
//			for (CorrelationAnalysisRecord oneRecord : record.getRecordList()) {
//				System.out.println(oneRecord.getTestType());
//				System.out.println(oneRecord.getCompositoin().toString());
//				System.out.println(oneRecord.getDopantWaveLengthMartrix().toString());
//			}
//		}

		// 4. calculate correlation coefficient
		for (CorrelationAnalysisRecordList recordList : analysisRecordList) {
			for (CorrelationAnalysisRecord record : recordList.getRecordList()) {
				ArrayList<String> componentList = new ArrayList<>();
				for (int i = 0; i < record.getDopantWaveLengthMartrix().size(); i++) {
					componentList.add(record.getDopantWaveLengthMartrix()
							.get(i).get(0));
				}
				for (int i = 1; i < record.getDopantWaveLengthMartrix().get(0)
						.size(); i++) {
					ArrayList<String> waveLengthList = new ArrayList<>();
					for (int j = 0; j < record.getDopantWaveLengthMartrix()
							.size(); j++) {
						waveLengthList.add(record.getDopantWaveLengthMartrix()
								.get(j).get(i));
					}
//					System.out.println(componentList.toString());
//					System.out.println(waveLengthList.toString());
					Double correlaitonCoef = calculateCorrCoef(componentList,
							waveLengthList);
					record.getCorrelaitonCoef().add(correlaitonCoef);
				}
			}
		}

		return analysisRecordList;
	}

	public static ArrayList<String> getZeroWavelengthList() {
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < waveLength.size()-1; i++) {
			result.add("0");
		}
		return result;
	}

	public static Integer findWavelengthIndex(Integer waveLength) {
		return (waveLength - wavelengthStart) / wavelengthResolutoin;
	}

	public static ArrayList<ArrayList<String>> getComponentCombination() {
		Integer combinationNumber = 3;
		ArrayList<String> componentList = new ArrayList<>();
		for (int i = 0; i < composition.size(); i++) {
			componentList.add(i + "");
		}
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		for (int i = 1; i <= combinationNumber; i++) {
			ArrayList<String> rawList = FindCombination.combine(componentList,
					i);
			for (String obj : rawList) {
				ArrayList<String> oneList = new ArrayList<>();
				for (String index : obj.split(" ")) {
					oneList.add(index);
				}
				result.add(oneList);
			}
		}
		
		return result;
	}

	public static ArrayList<String> getCompositionName(
			ArrayList<String> indexList) {
		ArrayList<String> result = new ArrayList<>();
		for (String index : indexList) {
			result.add(composition.get(Integer.valueOf(index)));
		}
		return result;
	}

	public static Boolean ifHasComposition(ArrayList<String> combinationIndex,
			CorrelationAnalysisModel model) {
		Integer sumIndex = 0;
		for (String index : combinationIndex) {
			if (model.getDopantList().get(Integer.valueOf(index)).equals("1")) {
				sumIndex++;
			}
		}
		return sumIndex == combinationIndex.size() ? true : false;
	}

	public static Double calculateCorrCoef(ArrayList<String> input1,
			ArrayList<String> input2) {
		CorrelationCoefficientCalculator ccc = new CorrelationCoefficientCalculator();
		Double result = ccc.getCorrelationCoefficientString(input1, input2);
		return result;
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

	public static void outputResult(
			ArrayList<CorrelationAnalysisRecordList> input) {
		List<String> header = new ArrayList<>();
		header.add("Type");
		header.add("Composition");
		for(int i = 0; i < waveLength.size()-1 ; i ++) {
			header.add(waveLength.get(i)+"-"+waveLength.get(i+1));
		}
		List<List<String>> content = new ArrayList<>();
		System.out.println("reslult");
		for (CorrelationAnalysisRecordList recordList : input) {
			System.out.println(recordList.getTestType());
			for (CorrelationAnalysisRecord record : recordList.getRecordList()) {
				List<String> newContent = new ArrayList<>();
				newContent.add(recordList.getTestType());
				newContent.add(record.getCompositoin().toString());
				System.out.println(record.getCompositoin().toString());
				for (Double obj : record.getCorrelaitonCoef()) {
					System.out.print(obj + " ");
					newContent.add(obj + " ");
				}
				content.add(newContent);
				System.out.println();
			}
			
		}
		System.out.println(Calendar.getInstance().getTimeInMillis());
		CsvUtils.write(new File("./src/bASAnalysis/result"+Calendar.getInstance().getTimeInMillis()+".csv"), content,header);
	}

}
