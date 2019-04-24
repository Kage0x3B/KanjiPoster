package de.syscy.kanjipos.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.pdf.TrueTypeUtil;
import org.xhtmlrenderer.util.SupportedEmbeddedFontTypes;
import org.xhtmlrenderer.util.XRLog;

import java.io.IOException;

@UtilityClass
public class FontUtil {
	private static @Setter UserAgentCallback userAgentCallback;

	public String[] getFontFamilyNames(String uri) {
		byte[] font1 = userAgentCallback.getBinaryResource(uri);

		if(font1 == null) {
			XRLog.exception("Could not load font " + uri);

			return new String[0];
		}

		String encoding = BaseFont.IDENTITY_H;
		String fontName = (org.xhtmlrenderer.util.FontUtil.isEmbeddedBase64Font(uri)) ? SupportedEmbeddedFontTypes.getExtension(uri) : uri;

		try {
			BaseFont font = BaseFont.createFont(fontName, encoding, true, false, font1, null);

			return TrueTypeUtil.getFamilyNames(font);
		} catch(IOException | DocumentException ex) {
			ex.printStackTrace();
		}

		return new String[0];
	}
}