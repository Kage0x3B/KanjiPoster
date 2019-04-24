package de.syscy.kanjipos.gui.property;

import de.syscy.kanjipos.gui.property.types.PropertyNode;
import lombok.Getter;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class PropertiesEditor extends JPanel {
	private final @Getter PropertiesTreeTable treeTable;

	private final @Getter PropertiesTreeTableModel treeTableModel;

	public PropertiesEditor(PropertiesTreeTableModel treeTableModel) {

		setLayout(new GridBagLayout());

		this.treeTableModel = treeTableModel;
		treeTable = new PropertiesTreeTable(treeTableModel.getConfig(), treeTableModel);

		// clear the selection, when clicking on an empty area of the table;
		// see here: http://stackoverflow.com/a/43443397
		treeTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath path = treeTable.getPathForLocation(e.getX(), e.getY());
				if (path == null) {

					treeTable.clearSelection();

					ListSelectionModel selectionModel = treeTable.getSelectionModel();
					selectionModel.setAnchorSelectionIndex(-1);
					selectionModel.setLeadSelectionIndex(-1);

					TableColumnModel columnModel = treeTable.getColumnModel();
					columnModel.getSelectionModel().setAnchorSelectionIndex(-1);
					columnModel.getSelectionModel().setLeadSelectionIndex(-1);

				}
			}
		});

		treeTable.expandAll();

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		add(new JScrollPane(treeTable), c);
	}
}
