package de.syscy.kanjipos.data.xml;

import de.syscy.kanjipos.data.*;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xhtmlrenderer.resource.XMLResource;
import org.xml.sax.InputSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLDataSource implements IDataSource {
	private final @Getter IPosterData data;

	public XMLDataSource(File xmlFile) {
		IPosterData data;

		try(InputStreamReader reader = new InputStreamReader(new FileInputStream(xmlFile), StandardCharsets.UTF_8)) {
			Document document = XMLResource.load(new InputSource(reader)).getDocument();

			data = new SimplePosterData();

			Element root = (Element) document.getElementsByTagName("entries").item(0);
			NodeList entries = root.getElementsByTagName("entry");

			for(int i = 0; i < entries.getLength(); i++) {
				Element entryElement = (Element) entries.item(i);
				NodeList childNodes = entryElement.getChildNodes();

				PosterEntry entry = new PosterEntry();

				for(int j = 0; j < childNodes.getLength(); j++) {
					if(!(childNodes.item(j) instanceof Element)) {
						continue;
					}

					Element dataElement = (Element) childNodes.item(j);
					entry.set(dataElement.getTagName(), dataElement.getTextContent());
				}

				data.getEntries().add(entry);
			}
		} catch(IOException ex) {
			ex.printStackTrace();

			data = null;
		}

		this.data = data;
	}
}