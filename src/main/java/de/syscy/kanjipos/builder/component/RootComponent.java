package de.syscy.kanjipos.builder.component;

import de.syscy.kanjipos.KanjiPos;
import de.syscy.kanjipos.builder.PlaceholderUtil;
import de.syscy.kanjipos.util.FontUtil;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.Properties;

@RequiredArgsConstructor
public class RootComponent extends HtmlComponent {
	private final Properties properties;

	@Override
	public void build(StringBuilder output) {
		output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		output.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");

		output.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
		output.append("<head>");
		output.append("<title>Temp Poster</title>");
		//output.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></link>");
		buildStyle(output, properties);
		output.append("</head>");
		output.append("<body>");

		buildChildren(output);

		output.append("</body>");
		output.append("</html>");
	}

	private void buildStyle(StringBuilder output, Properties properties) {
		output.append("<style>\n");

		File cssFile = new File(properties.getProperty("cssFile", "style.css"));
		StringBuilder cssBuilder = new StringBuilder();

		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(cssFile))) {
			String line;

			while((line = bufferedReader.readLine()) != null) {
				cssBuilder.append(line);
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}

		String fontFilename = properties.getProperty("font");

		if(fontFilename != null) {
			File fontFile = new File(fontFilename);
			String fontUri = fontFile.toURI().toString();

			String[] fontFamilyName = FontUtil.getFontFamilyNames(fontUri);
			properties.put("fontUri", fontFile.toURI().toString());

			KanjiPos.LOGGER.info("Uri: " + fontFile.toURI().toString());
			KanjiPos.LOGGER.info("fontFamily: " + (fontFamilyName.length > 0 ? fontFamilyName[0] : "null"));

			if(fontFamilyName.length > 0) {
				properties.put("fontFamily", fontFamilyName[0]);
			}
		}

		PlaceholderUtil.processPlaceholders(cssBuilder, properties.stringPropertyNames(), properties::getProperty);

		output.append(cssBuilder);
		output.append("</style>");
	}
}