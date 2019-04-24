package de.syscy.kanjipos.builder;

import de.syscy.kanjipos.AlternativesRegistry;
import de.syscy.kanjipos.builder.component.TableComponent;
import de.syscy.kanjipos.data.PosterEntry;

public interface IComponentBuilder extends AlternativesRegistry.IAlternative {
	TableComponent.TableDataComponent buildComponent(PosterEntry posterEntry);
}