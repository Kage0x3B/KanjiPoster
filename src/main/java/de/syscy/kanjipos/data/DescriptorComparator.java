package de.syscy.kanjipos.data;

import java.util.Arrays;

public class DescriptorComparator implements IEntryComparator {
	@Override
	public int compare(PosterEntry o1, PosterEntry o2) {
		if(o1.has("descriptorStart") && o2.has("descriptorStart") && o1.has("descriptorEnd") && o2.has("descriptorEnd")) {
			String o1DescriptorStart = o1.get("descriptorStart");
			String o2DescriptorStart = o2.get("descriptorStart");
			String o1DescriptorEnd = o1.get("descriptorEnd");
			String o2DescriptorEnd = o2.get("descriptorEnd");

			int o1FirstNumber = extractNumber(o1DescriptorStart);
			int o2FirstNumber = extractNumber(o2DescriptorStart);
			int firstNumberComparison = Integer.compare(o1FirstNumber, o2FirstNumber);

			if(firstNumberComparison != 0) {
				return firstNumberComparison;
			}

			char o1LastImportantLetter = o1DescriptorStart.charAt(o1DescriptorStart.length() - 1);
			char o2LastImportantLetter = o2DescriptorStart.charAt(o2DescriptorStart.length() - 1);
			int lastImportantLetterComparison = Character.compare(o1LastImportantLetter, o2LastImportantLetter);

			if(lastImportantLetterComparison != 0) {
				return lastImportantLetterComparison;
			}

			String[] o1EndSplit = o1DescriptorEnd.split("\\.");
			String[] o2EndSplit = o2DescriptorEnd.split("\\.");

			int firstEndNumberCompare = compareNumberStrings(o1EndSplit[0], o2EndSplit[0]);

			if(firstEndNumberCompare != 0) {
				return firstEndNumberCompare;
			}

			int secondEndNumberCompare = compareNumberStrings(o1EndSplit[1], o2EndSplit[1]);

			if(secondEndNumberCompare != 0) {
				return secondEndNumberCompare;
			}
		}

		return 0;
	}

	private int extractNumber(String string) {
		String numberChars = "";

		for(int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);

			if(!Character.isDigit(c)) {
				break;
			}

			numberChars += c;
		}

		return Integer.parseInt(numberChars.trim());
	}

	private int compareNumberStrings(String string1, String string2) {
		int number1 = Integer.parseInt(string1.trim());
		int number2 = Integer.parseInt(string2.trim());

		return Integer.compare(number1, number2);
	}
}