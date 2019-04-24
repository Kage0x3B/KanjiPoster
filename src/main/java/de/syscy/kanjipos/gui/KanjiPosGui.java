package de.syscy.kanjipos.gui;

import de.syscy.kanjipos.KanjiPos;
import de.syscy.kanjipos.util.FontUtil;
import lombok.Getter;
import org.w3c.dom.Document;
import org.xhtmlrenderer.event.DefaultDocumentListener;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.swing.NaiveUserAgent;
import org.xhtmlrenderer.util.GeneralUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.StringReader;
import java.util.Properties;

public class KanjiPosGui extends JFrame {
	private @Getter KanjiPos kanjiPos;
	private XHTMLPanel renderPanel;

	public KanjiPosGui() {
		super();

		kanjiPos = new KanjiPos();

		setTitle("KanjiPos");

		renderPanel = new XHTMLPanel();
		setupDocumentListener(renderPanel);
		setupUserAgentCallback(renderPanel);
		FSScrollPane renderPanelScroll = new FSScrollPane(renderPanel);
		renderPanelScroll.setMinimumSize(new Dimension(200, 100));

		JPanel optionsPanel = new OptionsPanel(this);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, renderPanelScroll, optionsPanel);
		splitPane.setDividerLocation(0.8);
		splitPane.setResizeWeight(1);

		add(splitPane);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(600, 400));
		pack();
	}

	private void setupUserAgentCallback(XHTMLPanel panel) {
		UserAgentCallback userAgent = new NaiveUserAgent();
		FontUtil.setUserAgentCallback(userAgent);

		panel.getSharedContext().setUserAgentCallback(userAgent);
	}

	private void setupDocumentListener(final XHTMLPanel panel) {
		panel.addDocumentListener(new DefaultDocumentListener() {
			public void documentStarted() {
				panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				super.documentStarted();
			}

			public void documentLoaded() {
				panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void onLayoutException(Throwable t) {
				panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				panel.setDocument(getErrorDocument(t.getCause() + "; " + t.getMessage()).getDocument());
			}

			public void onRenderException(Throwable t) {
				panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				panel.setDocument(getErrorDocument(t.getCause() + "; " + t.getMessage()).getDocument());
			}
		});
	}

	public void startRender(Properties properties) {
		Document document = kanjiPos.buildDocument(properties);

		try {
			File uriFile = new File("temp.html"); //Doesn't have to exist
			renderPanel.setDocument(document, uriFile.toURI().toString());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private XMLResource getErrorDocument(String reason) {
		reason = GeneralUtil.escapeHTML(reason);

		String notFound = "<html><h1>Fehler</h1><p>Poster konnte nicht generiert werden, ueberpruefen Sie die Einstellungen und versuchen Sie es erneut.</p><p>Fehlermeldung: " + reason + "</p></html>";

		return XMLResource.load(new StringReader(notFound));
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			KanjiPosGui gui = new KanjiPosGui();
			gui.setVisible(true);
		});
	}
}