package de.syscy.kanjipos;

import com.itextpdf.text.DocumentException;
import de.syscy.kanjipos.builder.IComponentBuilder;
import de.syscy.kanjipos.builder.PosterHtmlBuilder;
import de.syscy.kanjipos.builder.SimpleComponentBuilder;
import de.syscy.kanjipos.builder.TemplateComponentBuilder;
import de.syscy.kanjipos.data.IDataSource;
import de.syscy.kanjipos.data.IEntryComparator;
import de.syscy.kanjipos.data.KanjiComparator;
import de.syscy.kanjipos.data.xml.XMLDataSource;
import de.syscy.kanjipos.util.FontUtil;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.XMLResource;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

public class KanjiPos {
	public static final Logger LOGGER = Logger.getLogger("KanjiPos");

	public static final float DEFAULT_DOTS_PER_POINT = 20f * 4f / 3f;
	public static final int DEFAULT_DOTS_PER_PIXEL = 20;

	private AlternativesRegistry alternativesRegistry;

	public KanjiPos() {
		alternativesRegistry = new AlternativesRegistry();

		alternativesRegistry.putAlternative(IDataSource.class, XMLDataSource.class);
		alternativesRegistry.putAlternative(IComponentBuilder.class, SimpleComponentBuilder.class);
		alternativesRegistry.putAlternative(IComponentBuilder.class, TemplateComponentBuilder.class);
		alternativesRegistry.putAlternative(IEntryComparator.class, KanjiComparator.class);
	}

	public void createPoster(Properties properties) throws IOException, DocumentException {
		Document document = buildDocument(properties);

		File outputFile = new File((String) properties.getOrDefault("output", "poster_output.pdf"));

		createPDF(properties, document, outputFile);
	}

	public Document buildDocument(Properties properties) {
		File inputDataFile = new File((String) properties.get("inputData"));
		String dataSourceName = properties.getProperty("dataSourceClass", "XMLDataSource");
		IDataSource dataSource = alternativesRegistry.createAlternative(IDataSource.class, dataSourceName, inputDataFile);

		String componentBuilderName = properties.getProperty("componentBuilderClass", "TemplateComponentBuilder");
		IComponentBuilder componentBuilder = alternativesRegistry.createAlternative(IComponentBuilder.class, componentBuilderName, properties);

		String entryComparatorName = properties.getProperty("entryComparatorClass", "KanjiComparator");
		IEntryComparator comparator = alternativesRegistry.createAlternative(IEntryComparator.class, entryComparatorName);

		PosterHtmlBuilder htmlBuilder = new PosterHtmlBuilder(dataSource, componentBuilder, comparator);

		String html = htmlBuilder.build(properties);

		return XMLResource.load(new StringReader(html)).getDocument();
	}

	public void createPDF(Properties properties, Document document, File outputFile) throws IOException, DocumentException {
		try(OutputStream outputStream = new FileOutputStream(outputFile)) {
			float dotsPerPoint = Float.parseFloat(properties.getProperty("dotsPerPoint", String.valueOf(DEFAULT_DOTS_PER_POINT)));
			int dotsPerPixel = Integer.parseInt(properties.getProperty("dotsPerPixel", String.valueOf(DEFAULT_DOTS_PER_PIXEL)));

			ITextRenderer renderer = new ITextRenderer(dotsPerPoint, dotsPerPixel);
			//renderer.getSharedContext().setFontResolver(new LoggingFontResolver(renderer.getSharedContext()));

			ITextUserAgent callback = new ITextUserAgent(renderer.getOutputDevice());
			callback.setSharedContext(renderer.getSharedContext());
			renderer.getSharedContext().setUserAgentCallback(callback);
			FontUtil.setUserAgentCallback(callback);

			renderer.getOutputDevice().setMetadata("Generator", "KanjiPos v1.0 (by Moritz Hein)");

			File uriFile = new File("temp.html"); //Doesn't have to exist

			renderer.setDocument(document, uriFile.toURI().toString());
			renderer.layout();
			renderer.createPDF(outputStream, true);
		}
	}

	public static void main(String[] args) throws IOException, DocumentException {
		File propertiesFile;

		if(args.length > 0) {
			propertiesFile = new File(args[0]);
		} else {
			propertiesFile = new File("poster.properties");
		}

		Properties properties = new Properties();
		properties.load(new FileReader(propertiesFile));

		KanjiPos kanjiPos = new KanjiPos();
		kanjiPos.createPoster(properties);
	}
}