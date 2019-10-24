package de.syscy.kanjipos.data.comparator;

import de.syscy.kanjipos.data.IEntryComparator;
import de.syscy.kanjipos.data.PosterEntry;

public class DummyComparator implements IEntryComparator {
	@Override
	public int compare(PosterEntry o1, PosterEntry o2) {
		return 0;
	}
}
