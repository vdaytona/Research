package test;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import commonTool.CsvUtils;

public class test {

	public static void main(String[] args) {
		File file = new File("C:\\Users\\purewin7\\Desktop\\Hanna");
		String test[];
		test = file.list();
		ArrayList<File> fileName = new ArrayList<>();
		fileName.add(new File("C:\\Users\\purewin7\\Desktop\\1.csv"));
		fileName.add(new File("C:\\Users\\purewin7\\Desktop\\2.csv"));
		fileName.add(new File("C:\\Users\\purewin7\\Desktop\\3.csv"));

		List<String> header = new ArrayList<>();
		header.add("Title");
		List<List<List<String>>> titleList = new ArrayList<>();

		Integer splitNumber = 3;
		Integer amountPerPerson = (test.length % 3 == 0) ? test.length
				/ splitNumber : test.length / splitNumber + 1;

		for (int i = 0; i < 3; i++) {
			List<List<String>> title = new ArrayList<>();
			for (int j = 0; j < amountPerPerson
					&& (i * amountPerPerson + j) < test.length; j++) {
				List<String> oneTitle = new ArrayList<>();
				oneTitle.add(test[i * amountPerPerson + j].substring(0, test[i
						* amountPerPerson + j].indexOf(".pdf")));
				title.add(oneTitle);
			}
			titleList.add(title);
		}
		for (int i = 0; i < 3; i++) {
			CsvUtils.write(fileName.get(i), titleList.get(i), header);
		}

	}

}
