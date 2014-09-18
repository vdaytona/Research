package bASAnalysis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import commonTool.CsvUtils;

public class FindRelationBetweenDopantAndAbsorptionLumiWavelength {

	public static void main(String args[]) {
		// 1. read rawData From csv
		List<List<String>> rawData = readRawData();

		// 2. put the data into model
		AbsorptionLumiRecordList recordList = transferDataIntoRecordList(rawData);

		// 3. Analysis
		CorrelationAnalysisRecordList correlaitonAnalysisRecordList = analysisCorrelaiton(recordList);

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
			AbsorptionLumiRecordList input) {
		
		// 1. extract all composition combination, and get List
		Set<String> compositionList = input.getRecordList().get(0).getCompostoin().keySet();
		Integer combinationNumber = 3;
		ArrayList<ArrayList<String>> combinationList = new ArrayList<>();
		for(int i = 1; i <= combinationNumber; i ++) {
			// TODO
		}
		
		

		// 2. build model with composition and absorption/luminescence model
		

		// 3. calculate the correlation coefficient
		

		// TODO
		return null;
	}

	public static void outputResult(CorrelationAnalysisRecordList input) {
		// TODO
	}

}
