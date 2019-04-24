package de.syscy.kanjipos.data;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;

public interface IPosterData extends Iterable<PosterEntry> {
	List<PosterEntry> getEntries();

	@Override
	default Iterator<PosterEntry> iterator() {
		return getEntries().iterator();
	}

	@Override
	default Spliterator<PosterEntry> spliterator() {
		return getEntries().spliterator();
	}
}