package de.syscy.kanjipos.gui;

import com.itextpdf.text.DocumentException;
import de.syscy.kanjipos.KanjiPos;
import de.syscy.kanjipos.gui.property.PropertiesEditor;
import de.syscy.kanjipos.gui.property.PropertiesTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class OptionsPanel extends JPanel {
	private static final Map<String, String> PROPERTY_TO_DESCRIPTOR = new HashMap<>();
	private static final Map<String, String> DESCRIPTOR_TO_PROPERTY = new HashMap<>();

	private final PropertiesTreeTableModel treeTableModel;

	public OptionsPanel(KanjiPosGui gui) {
		setLayout(new BorderLayout());

		treeTableModel = new PropertiesTreeTableModel(createDefaultData());

		JButton previewButton = new JButton("Aktualisieren");
		previewButton.addActionListener(e -> {
			Properties properties = new Properties();
			convertProperties(properties, (Map<String, Object>) treeTableModel.getData());

			gui.startRender(properties);
		});
		JButton createPDFButton = new JButton("PDF erstellen");
		createPDFButton.addActionListener(e -> {
			Properties properties = new Properties();

			convertProperties(properties, (Map<String, Object>) treeTableModel.getData());
			properties.setProperty("output", "output.pdf");

			try {
				gui.getKanjiPos().createPoster(properties);
			} catch(IOException | DocumentException ex) {
				ex.printStackTrace();
			}
		});
		JButton saveButton = new JButton("Werte speichern");
		saveButton.addActionListener(e -> {
			Properties properties = new Properties();

			convertProperties(properties, (Map<String, Object>) treeTableModel.getData());

			File applicationDirectory = new File(".");
			JFileChooser fileChooser = new JFileChooser(applicationDirectory);
			fileChooser.setDialogTitle("Werte speichern");
			fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
			fileChooser.setSelectedFile(new File("poster.properties"));
			fileChooser.setFileFilter(new FileNameExtensionFilter("Java properties file", "properties"));

			if(fileChooser.showSaveDialog(OptionsPanel.this) == JFileChooser.APPROVE_OPTION) {
				File saveFile = fileChooser.getSelectedFile();

				try {
					properties.store(new FileOutputStream(saveFile), "KanjiPos Properties");
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		JButton loadButton = new JButton("Werte laden");
		loadButton.addActionListener(e -> {
			File applicationDirectory = new File(".");
			JFileChooser fileChooser = new JFileChooser(applicationDirectory);
			fileChooser.setDialogTitle("Werte laden");
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			fileChooser.setFileFilter(new FileNameExtensionFilter("Java properties file", "properties"));

			if(fileChooser.showOpenDialog(OptionsPanel.this) == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();

				Properties properties = new Properties();
				convertProperties(properties, (Map<String, Object>) treeTableModel.getData());

				try {
					properties.load(new FileReader(selectedFile));
				} catch(IOException ex) {
					ex.printStackTrace();
				}

				applyProperties(properties);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0, 2));
		buttonPanel.add(previewButton);
		buttonPanel.add(createPDFButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(loadButton);

		add(new PropertiesEditor(treeTableModel), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.AFTER_LAST_LINE);
	}

	private void convertProperties(Properties properties, Map<String, Object> map) {
		for(String key : map.keySet()) {
			Object object = map.get(key);

			if(object instanceof Map) {
				convertProperties(properties, (Map<String, Object>) object);
			} else if(object instanceof File) {
				File file = (File) object;

				properties.setProperty(toProp(key), file.getName());
			} else {
				properties.setProperty(toProp(key), String.valueOf(object));
			}
		}
	}

	private void applyProperties(Properties properties) {
		applyProperties(properties, treeTableModel.getRoot());
	}

	private void applyProperties(Properties properties, TreeTableNode node) {
		String descriptor = (String) node.getValueAt(0);
		String property = toProp(descriptor);

		if(property != null && properties.containsKey(property)) {
			String value = properties.getProperty(property);

			Class<?> previousType = node.getUserObject().getClass();

			Object newValue;

			if(previousType == int.class) {
				newValue = Integer.parseInt(value);
			} else if(previousType == float.class) {
				newValue = Float.parseFloat(value);
			} else if(previousType == double.class) {
				newValue = Double.parseDouble(value);
			} else if(previousType == long.class) {
				newValue = Long.parseLong(value);
			} else if(previousType == boolean.class) {
				newValue = Boolean.parseBoolean(value);
			} else if(previousType == File.class) {
				newValue = new File(value);
			} else {
				newValue = value;
			}

			node.setUserObject(newValue);
		}

		for(int i = 0; i < node.getChildCount(); i++) {
			TreeTableNode child = node.getChildAt(i);

			applyProperties(properties, child);
		}
	}

	private static Object createDefaultData() {
		Map<String, Object> root = new LinkedHashMap<>();
		root.put(toDesc("inputData"), "input.xml");
		root.put(toDesc("dataSourceClass"), "XMLDataSource");
		root.put(toDesc("componentBuilderClass"), "TemplateComponentBuilder");
		root.put(toDesc("entryComparatorClass"), "KanjiComparator");

		//Grid
		Map<String, Object> grid = new LinkedHashMap<>();
		grid.put(toDesc("kanjiTemplate"), "kanjiTemplate.html");
		grid.put(toDesc("entriesPerRow"), 20);
		grid.put(toDesc("fillerEntries"), 0);

		//PDF Settings
		Map<String, Object> pdfSettings = new LinkedHashMap<>();
		pdfSettings.put(toDesc("dotsPerPoint"), KanjiPos.DEFAULT_DOTS_PER_POINT);
		pdfSettings.put(toDesc("dotsPerPixel"), KanjiPos.DEFAULT_DOTS_PER_PIXEL);

		Map<String, Object> css = new LinkedHashMap<>();
		css.put(toDesc("cssFile"), "style.css");
		css.put(toDesc("pageSize"), "594mm 840mm");
		css.put(toDesc("pageMargin"), "10pt");
		css.put(toDesc("font"), "SourceHanSerif-Regular.otf");
		css.put(toDesc("baseFontSize"), 3.0);
		css.put(toDesc("textColor"), "blue");
		css.put(toDesc("lineHeight"), 1.5);
		css.put(toDesc("kanjiFontSize"), 10.0);
		css.put(toDesc("kanjiColor"), "black");
		css.put(toDesc("indexColor"), "blue");

		root.put("Gitter", grid);
		root.put("PDF Einstellungen", pdfSettings);
		root.put("CSS", css);

		return root;
	}

	static {
		addProperty("inputData", "Eingabedaten");
		addProperty("dataSourceClass", "Datentyp");
		addProperty("componentBuilderClass", "Komponententyp");
		addProperty("entryComparatorClass", "Eintragtyp");

		//Grid
		addProperty("kanjiTemplate", "Kanji Template");
		addProperty("entriesPerRow", "Felder pro Zeile");
		addProperty("fillerEntries", "Leerstellenf\u00fcller");

		//PDF Settings
		addProperty("dotsPerPoint", "Dots Per Point");
		addProperty("dotsPerPixel", "Dots Per Pixel");

		//CSS
		addProperty("cssFile", "CSS Datei");
		addProperty("pageSize", "Seitengr\u00f6\u00dfe");
		addProperty("pageMargin", "Randabstand");
		addProperty("font", "Schriftdatei");
		addProperty("baseFontSize", "Schriftgr\u00f6\u00dfe");
		addProperty("lineHeight", "Linienh\u00f6he");
		addProperty("kanjiFontSize", "Kanji Gr\u00f6\u00dfe");
		addProperty("textColor", "Text Farbe");
		addProperty("kanjiColor", "Kanji Farbe");
		addProperty("indexColor", "Index Farbe");
	}

	private static String toDesc(String property) {
		return PROPERTY_TO_DESCRIPTOR.get(property);
	}

	private static String toProp(String descriptor) {
		return DESCRIPTOR_TO_PROPERTY.get(descriptor);
	}

	private static void addProperty(String property, String descriptor) {
		PROPERTY_TO_DESCRIPTOR.put(property, descriptor);
		DESCRIPTOR_TO_PROPERTY.put(descriptor, property);
	}
}