package de.syscy.kanjipos.builder;

import de.syscy.kanjipos.builder.component.RootComponent;
import de.syscy.kanjipos.builder.component.TableComponent;
import de.syscy.kanjipos.data.IDataSource;
import de.syscy.kanjipos.data.PosterEntry;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Properties;

@RequiredArgsConstructor
public class PosterHtmlBuilder {
	private final IDataSource dataSource;
	private final IComponentBuilder componentBuilder;
	private final Comparator<PosterEntry> entryComparator;

	public String build(Properties properties) {
		StringBuilder output = new StringBuilder();

		RootComponent root = new RootComponent(properties);

		TableComponent kanjiTable = new TableComponent();
		TableComponent.TableRowComponent currentRow = null;

		dataSource.getData().getEntries().sort(entryComparator);

		int fillerEntries = Integer.parseInt(properties.getProperty("fillerEntries", "0"));
		for(int i = 0; i < fillerEntries; i++) {
			dataSource.getData().getEntries().add(PosterEntry.EMPTY);
		}

		int i = 0;

		int entriesPerRow = Integer.parseInt(properties.getProperty("entriesPerRow", "20"));

		for(PosterEntry posterEntry : dataSource.getData()) {
			if(currentRow == null || i % entriesPerRow == 0) {
				currentRow = new TableComponent.TableRowComponent();
				kanjiTable.add(currentRow);
			}

			currentRow.add(componentBuilder.buildComponent(posterEntry));

			i++;
		}

		root.add(kanjiTable);

		root.build(output);

		return output.toString();
	}
}