package presentation.recordInfo;
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA. 
 */

/**
 * shows all available files in the store directory
 * 
 * @author Reinhard Achleitner
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.tree.DefaultTreeModel;

import model.BOFileWrapper;
import presentation.GuiTab;
import presentation.GuiTableSorter;
import control.ControlMain;
import control.ControlRecordEditTab;

public class GuiTabRecordEdit extends GuiTab {

	private ControlRecordEditTab control;

	private JTree fileTree;

	private JTable fileTable;
	private GuiFileTableModel fileTableModel;
    public GuiTableSorter fileTableSorter;

	private JTextArea fileInfo;

	private DefaultTreeModel treeModel;

	public GuiTabRecordEdit(ControlRecordEditTab control) {
		this.setControl(control);
		initialize();
	}

	/**
	 * erzeugt die GUI
	 *  
	 */
	protected void initialize() {
		setLayout(new BorderLayout());

		final JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane splitFilesInfos = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitFilesInfos.setDividerLocation(350);
		add(split, BorderLayout.CENTER);

		treeModel = new DefaultTreeModel(new BaseTreeNode(new BOFileWrapper(ControlMain.getSettingsPath().getSavePath())));

		fileTree = new JTree(treeModel);
		fileTree.addMouseListener(control);
		fileTree.setCellRenderer(new GuiAvailableFilesTreeRenderer());
		split.setLeftComponent(new JScrollPane(fileTree));

		split.setRightComponent(splitFilesInfos);
		fileInfo = new JTextArea();
		fileInfo.setEditable(false);

		JScrollPane p = new JScrollPane(getFileTable());
		p.getViewport().setBackground(Color.white);
		splitFilesInfos.setTopComponent(p);
		splitFilesInfos.setBottomComponent(new JScrollPane(fileInfo));
		fileTree.getSelectionModel().addTreeSelectionListener(fileTableModel);
		
		int splitLoc = ControlMain.getSettingsLayout().getRecordInfoDirectorySplitPos();
		if (splitLoc > 0)
		{
			split.setDividerLocation(splitLoc);
		}
		split.getLeftComponent().addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				int divLoc = split.getDividerLocation();
				ControlMain.getSettingsLayout().setRecordInfoDirectorySplitPos(divLoc);
			}
		});

		addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				control.reloadAvailableFiles();
			}

			public void ancestorRemoved(AncestorEvent event) {
				// TODO Auto-generated method stub

			}

			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * @return
	 */
	public JTable getFileTable() {
		if (fileTable == null) {

			fileTable = new JTable();
			fileTable.setShowHorizontalLines(false);
			fileTable.setShowVerticalLines(false);
			fileTableModel = new GuiFileTableModel(fileTable);
			fileTableSorter = new GuiTableSorter(fileTableModel);
			fileTable.setModel(fileTableSorter);
			fileTableSorter.setTableHeader(fileTable.getTableHeader());
			fileTable.addMouseListener(control);
			fileTable.getSelectionModel().addListSelectionListener(control);
		}
		return fileTable;
	}

	/**
	 * @return ControlProgramTab
	 */
	public ControlRecordEditTab getControl() {
		return control;
	}

	/**
	 * Sets the control.
	 * 
	 * @param control
	 *            The control to set
	 */
	public void setControl(ControlRecordEditTab control) {
		this.control = control;
	}

	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	public GuiFileTableModel getTableModel() {
		return fileTableModel;
	}
	/**
	 * @return
	 */
	public JTree getTree() {
		return fileTree;
	}

	/**
	 * @param fileInfo2
	 */
	public void setFileInfo(String fileInfoText) {
		fileInfo.setText(fileInfoText);

	}

}