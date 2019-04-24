package de.syscy.kanjipos.data;

import de.syscy.kanjipos.AlternativesRegistry;

public interface IDataSource extends AlternativesRegistry.IAlternative {
	IPosterData getData();
}