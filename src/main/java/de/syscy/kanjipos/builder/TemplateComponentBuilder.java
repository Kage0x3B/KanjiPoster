package de.syscy.kanjipos.builder;

import de.syscy.kanjipos.KanjiPos;
import de.syscy.kanjipos.builder.component.TableComponent;
import de.syscy.kanjipos.data.PosterEntry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
public class TemplateComponentBuilder implements IComponentBuilder {
	private final Properties properties;

	private final @Getter(lazy = true) String template = readTemplate();

	@Override
	public TableComponent.TableDataComponent buildComponent(PosterEntry posterEntry) {
		if(posterEntry.equals(PosterEntry.EMPTY)) {
			return new TableComponent.TableDataComponent();
		}

		TableComponent.TableDataComponent dataComponent = new TableComponent.TableDataComponent();

		StringBuilder kanjiBuilder = new StringBuilder();

		kanjiBuilder.append(getTemplate());

		PlaceholderUtil.processPlaceholders(kanjiBuilder, posterEntry.getData().keySet(), posterEntry::get);

		dataComponent.addText(kanjiBuilder.toString());

		return dataComponent;
	}

	private String readTemplate() {
		File templateFile = new File(properties.getProperty("kanjiTemplate", "kanjiTemplate.html"));
		StringBuilder kanjiBuilder = new StringBuilder();

		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(templateFile))) {
			String line;

			while((line = bufferedReader.readLine()) != null) {
				kanjiBuilder.append(line);
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		return kanjiBuilder.toString();
	}
}