package de.syscy.kanjipos.data;

import de.syscy.kanjipos.AlternativesRegistry;

import java.util.Comparator;

public interface IEntryComparator extends Comparator<PosterEntry>, AlternativesRegistry.IAlternative {

}