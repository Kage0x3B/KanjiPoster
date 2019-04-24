package de.syscy.kanjipos.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import de.syscy.kanjipos.KanjiPos;
import org.joor.Reflect;
import org.xhtmlrenderer.css.constants.CSSName;
import org.xhtmlrenderer.css.constants.IdentValue;
import org.xhtmlrenderer.css.sheet.FontFaceRule;
import org.xhtmlrenderer.css.style.CalculatedStyle;
import org.xhtmlrenderer.css.style.FSDerivedValue;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.TrueTypeUtil;
import org.xhtmlrenderer.util.FontUtil;
import org.xhtmlrenderer.util.SupportedEmbeddedFontTypes;
import org.xhtmlrenderer.util.XRLog;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LoggingFontResolver extends ITextFontResolver {
	private final SharedContext sharedContext;

	public LoggingFontResolver(SharedContext sharedContext) {
		super(sharedContext);

		this.sharedContext = sharedContext;
	}

	@Override
	public void importFontFaces(List fontFaces) {
		for(Iterator i = fontFaces.iterator(); i.hasNext(); ) {
			FontFaceRule rule = (FontFaceRule) i.next();
			CalculatedStyle style = rule.getCalculatedStyle();

			FSDerivedValue src = style.valueByName(CSSName.SRC);
			if(src == IdentValue.NONE) {
				KanjiPos.LOGGER.warning("Invalid font face...");

				continue;
			}

			KanjiPos.LOGGER.info("Importing font face " + src.asString());

			byte[] font1 = sharedContext.getUac().getBinaryResource(src.asString());
			if(font1 == null) {
				XRLog.exception("Could not load font " + src.asString());
				continue;
			}

			byte[] font2 = null;
			FSDerivedValue metricsSrc = style.valueByName(CSSName.FS_FONT_METRIC_SRC);
			if(metricsSrc != IdentValue.NONE) {
				font2 = sharedContext.getUac().getBinaryResource(metricsSrc.asString());
				if(font2 == null) {
					XRLog.exception("Could not load font metric data " + src.asString());
					continue;
				}
			}

			boolean embedded = style.isIdent(CSSName.FS_PDF_FONT_EMBED, IdentValue.EMBED);
			String encoding = style.getStringProperty(CSSName.FS_PDF_FONT_ENCODING);
			String fontFamily = null;
			IdentValue fontWeight = null;
			IdentValue fontStyle = null;

			String uri = src.asString();

			String fontName = (FontUtil.isEmbeddedBase64Font(uri)) ? SupportedEmbeddedFontTypes.getExtension(uri) : uri;

			try {
				BaseFont font = BaseFont.createFont(fontName, encoding, embedded, false, font1, font2);

				String[] fontFamilyNames = TrueTypeUtil.getFamilyNames(font);
				Arrays.stream(fontFamilyNames).forEach(KanjiPos.LOGGER::info);
			} catch(IOException | DocumentException ex) {
				ex.printStackTrace();
			}

			Reflect.on(this).call("addFontFaceFont", null, null, fontStyle, src.asString(), encoding, embedded, font1, font2);
		}
	}
}