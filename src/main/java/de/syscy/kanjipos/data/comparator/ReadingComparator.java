package de.syscy.kanjipos.data.comparator;

import de.syscy.kanjipos.data.IEntryComparator;
import de.syscy.kanjipos.data.PosterEntry;
import de.syscy.kanjipos.util.JapaneseCharacter;

public class ReadingComparator implements IEntryComparator {
	@Override
	public int compare(PosterEntry o1, PosterEntry o2) {
		if(o1.has("reading") && o2.has("reading")) {
			String reading1 = o1.get("reading");
			String reading2 = o2.get("reading");

			reading1 = JapaneseCharacter.toHiragana(reading1);
			reading2 = JapaneseCharacter.toHiragana(reading2);

			return reading1.compareTo(reading2);
		}

		return 0;
	}
}