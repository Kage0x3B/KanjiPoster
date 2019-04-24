package de.syscy.kanjipos.builder;

import de.syscy.kanjipos.builder.component.PComponent;
import de.syscy.kanjipos.builder.component.TableComponent;
import de.syscy.kanjipos.builder.component.TagComponent;
import de.syscy.kanjipos.data.PosterEntry;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
public class SimpleComponentBuilder implements IComponentBuilder {
	private final Properties properties;

	@Override
	public TableComponent.TableDataComponent buildComponent(PosterEntry posterEntry) {
		TableComponent.TableDataComponent dataComponent = new TableComponent.TableDataComponent();

		for(Map.Entry<String, String> dataEntry : posterEntry.getData().entrySet()) {
			TagComponent div = new PComponent();
			div.addText(dataEntry.getValue());
			div.addClass(dataEntry.getKey());
			dataComponent.add(div);
		}

		return dataComponent;
	}
}