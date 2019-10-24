package de.syscy.kanjipos.data.comparator;

import de.syscy.kanjipos.KanjiPos;
import de.syscy.kanjipos.data.IEntryComparator;
import de.syscy.kanjipos.data.PosterEntry;

public class KanjiComparator implements IEntryComparator {
	@Override
	public int compare(PosterEntry o1, PosterEntry o2) {
		if(o1.has("index") && o2.has("index")) {
			String index1 = o1.get("index");
			String index2 = o2.get("index");

			if(index1.contains("N") && index2.contains("N")) {
				index1 = index1.substring(1);
				index2 = index2.substring(1);
			} else if(index1.contains("N")) {
				return 1;
			} else if(index2.contains("N")) {
				return -1;
			}

			return compareNumberStrings(index1, index2);
		}

		return 0;
	}

	private int compareNumberStrings(String string1, String string2) {
		int number1;
		int number2;

		try {
			number1 = Integer.parseInt(string1.trim());
		} catch(NumberFormatException ex) {
			KanjiPos.LOGGER.warning("Invalid number string: " + string1.trim());

			throw ex;
		}

		try {
			number2 = Integer.parseInt(string2.trim());
		} catch(NumberFormatException ex) {
			KanjiPos.LOGGER.warning("Invalid number string: " + string2.trim());

			throw ex;
		}

		return Integer.compare(number1, number2);
	}
}