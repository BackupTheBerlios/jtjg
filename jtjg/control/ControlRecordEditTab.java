package control;
/*

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2, or (at your option)
 any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.  

 */

/**
 * Kontrollklasse für die Aufnahme Infos
 * @author Reinhard Achleitner
 */
import java.awt.AWTKeyStroke;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import model.BOAfterRecordOptions;
import model.BOFileWrapper;
import model.BOPlaybackOption;

import org.apache.log4j.Logger;

import presentation.GuiMainView;
import presentation.recordInfo.BaseTreeNode;
import presentation.recordInfo.GuiTabRecordEdit;
import service.SerExternalProcessHandler;
import service.SerHelper;

/**
 * Controlklasse des Programmtabs.
 */
public class ControlRecordEditTab extends ControlTab implements MouseListener, ListSelectionListener {

	GuiMainView mainView;

	private File directory;
	private javax.swing.Timer fileInfoTimer;
	private javax.swing.Timer availableFileTimer;
	private GuiTabRecordEdit guiTabRecordEdit;
	

	private static final int REFRESH_TIME = 1000; // Refresh time of fileinfos
	private static final int REFRESH_TIME_READALLFILES = 30000; // Refresh time of fileinfos
	private static final int BITRATECALCSTART = 15; // seconds after record start the birate will be calculated


	private AbstractAction refreshAction;

	private boolean currentlyDirectoryRefresh;

	private String currentSavePath = "";

	public ControlRecordEditTab(GuiMainView view) {
		this.setMainView(view);

		if (availableFileTimer == null) {
			refreshAction = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					if (guiTabRecordEdit.isShowing()) {
						reloadAvailableFiles();
					}
				}
			};
			availableFileTimer = new javax.swing.Timer(REFRESH_TIME_READALLFILES, refreshAction);

		}
		availableFileTimer.start();
		
	}

	/**
	 * loads all available files in the store directory and shows them in a table
	 *  
	 */
	public void reloadAvailableFiles() {

		if (currentlyDirectoryRefresh) {
			return;
		}

		currentSavePath = ControlMain.getSettingsPath().getSavePath();
		DefaultTreeModel model = guiTabRecordEdit.getTreeModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		final JTree tree = guiTabRecordEdit.getTree();
		final Enumeration enNodes = tree.getExpandedDescendants(new TreePath(root));
		final TreePath[] selNodes = tree.getSelectionPaths();

		root.removeAllChildren();
		root.setUserObject("Dateistruktur wird geladen...");
		model.reload();

		Thread t = new Thread(new Runnable() {
			public void run() {
				currentlyDirectoryRefresh = true;
				DefaultTreeModel model = guiTabRecordEdit.getTreeModel();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

				String savePath = ControlMain.getSettingsPath().getSavePath();
				if (savePath != null && savePath.length() > 0) {
					File f = new File(savePath);

					createStructure(root, f);
					root.setUserObject(new BOFileWrapper(ControlMain.getSettingsPath().getSavePath()));
					if (guiTabRecordEdit != null) {
						model.reload();
					}

					if (enNodes != null) {
						while (enNodes.hasMoreElements()) {
							TreePath element = (TreePath) enNodes.nextElement();
							Object[] path = element.getPath();
							/*
							 * for (int i = 1; i < path.length; i++) { DefaultMutableTreeNode node = (DefaultMutableTreeNode) path[path.l];
							 * if (node != null) { expandNode(tree, node.getUserObject().toString(), root); } }
							 */
							BaseTreeNode node = (BaseTreeNode) path[path.length - 1];
							expandNode(tree, node, root);
						}
					}

					if (selNodes != null) {
						for (int i = 0; i < selNodes.length; i++) {
							BaseTreeNode node = (BaseTreeNode) selNodes[i].getLastPathComponent();
							selectNode(tree, node, root);
						}
					}

				} else {
					root.setUserObject(ControlMain.getProperty("err_noStorePath"));
				}
				currentlyDirectoryRefresh = false;
			}
		});

		t.start();

	}
	/**
	 * @param string
	 * @param root
	 */
	private void expandNode(JTree tree, BaseTreeNode nodeToExpand, DefaultMutableTreeNode root) {
		int iCount = root.getChildCount();
		for (int i = 0; i < iCount; i++) {
			BaseTreeNode node = (BaseTreeNode) root.getChildAt(i);
			if (node.getIdent().equals(nodeToExpand.getIdent())) {
				tree.expandPath(new TreePath(node.getPath()));

			} else if (node.getChildCount() > 0) {
				expandNode(tree, nodeToExpand, node);
			}
		}
	}

	/**
	 * @param string
	 * @param root
	 */
	private void selectNode(JTree tree, BaseTreeNode nodeToExpand, DefaultMutableTreeNode root) {
		int iCount = root.getChildCount();
		for (int i = 0; i < iCount; i++) {
			BaseTreeNode node = (BaseTreeNode) root.getChildAt(i);
			if (node.getIdent().equals(nodeToExpand.getIdent())) {
				tree.addSelectionPath(new TreePath(node.getPath()));

			} else if (node.getChildCount() > 0) {
				selectNode(tree, nodeToExpand, node);
			}
		}
	}

	/**
	 * @param root
	 * @param f
	 */
	private void createStructure(DefaultMutableTreeNode parent, File f) {
		File[] files = f.listFiles();

		for (int i = 0; i < files.length; i++) {

			if (!files[i].getName().startsWith(".")) {

				if (files[i].isDirectory()) {

					BaseTreeNode node = new BaseTreeNode(new BOFileWrapper(files[i]));
					node.setIdent("Directory:" + files[i].getAbsolutePath());
					parent.add(node);
					createStructure(node, files[i]);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see control.ControlTab#getMainView()
	 */
	public GuiMainView getMainView() {
		return mainView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see control.ControlTab#setMainView(presentation.GuiMainView)
	 */
	public void setMainView(GuiMainView view) {
		mainView = view;

	}

	public void setRecordView(GuiTabRecordEdit tabRecordEdit) {
		guiTabRecordEdit = tabRecordEdit;
		Thread t = new Thread(new Runnable() {
			public void run() {
				reloadAvailableFiles();
			}
		});
		t.start();

		//		register refresh key
		KeyStroke stroke = (KeyStroke) AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_F5, 0);

		guiTabRecordEdit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, "F5");
		guiTabRecordEdit.getActionMap().put("F5", refreshAction);

	}

	
	
	/**
	 * @param file
	 * @return
	 */
	private String getEnd2(File file) {

		String fileName = file.getAbsolutePath();
		int end = fileName.lastIndexOf(".");
		if (end > -1) {
			return fileName.substring(end + 1);
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof JTable) {
			if (e.getClickCount() == 2) {
				File file = (File) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 2);
				openFile(file);
			}
		}
	}

	/**
	 * @param file
	 */
	private void openFile(File file) {
		if (SerHelper.isVideo(file.getName()) || SerHelper.isAudio(file.getName())) {
			ArrayList player = ControlMain.getSettingsPlayback().getPlaybackOptions();

			if (player.size() > 0) {
				BOPlaybackOption play = BOPlaybackOption.detectPlaybackOption();
				if (play != null) {
					String exec = play.getPlaybackPlayer() + " " + play.getPlaybackPlayer();
					exec = getExecStringWithoutParam(exec);

					if (file.getAbsolutePath().indexOf(" ") > -1) {
						exec += " \"" + file.getAbsolutePath() + "\"";
					} else {
						exec += " " + file.getAbsolutePath();
					}
					SerExternalProcessHandler.startProcess(play.getName(), exec, play.isLogOutput());
				}
			}

		} else {
			try {

				if (file.length() > 50000) {
					JOptionPane.showMessageDialog(guiTabRecordEdit, ControlMain.getProperty("err_fileTolarge"));
					return;
				}

				//read the file
				FileInputStream in = new FileInputStream(file.getAbsoluteFile());
				BufferedInputStream inS = new BufferedInputStream(in);
				byte[] bytes = new byte[inS.available()];
				inS.read(bytes);
				String text = new String(bytes);
				JTextArea textArea = new JTextArea();
				textArea.setWrapStyleWord(true);
				textArea.setLineWrap(true);
				textArea.setEditable(false);
				textArea.setText(text);
				JScrollPane scroll = new JScrollPane(textArea);
				scroll.setPreferredSize(new Dimension(400, 400));
				JOptionPane.showMessageDialog(guiTabRecordEdit, scroll, "File", JOptionPane.INFORMATION_MESSAGE);

			} catch (Exception e) {
				Logger.getLogger("ControlProgramTab").error(e.getMessage());
			}
		}
	}

	/**
	 * @param exec
	 * @return
	 */
	private String getExecStringWithoutParam(String exec) {
		StringTokenizer tok = new StringTokenizer(exec);
		return tok.nextToken().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (e.getSource() instanceof JTable) {
			if (SwingUtilities.isRightMouseButton(e)) {
				if (e.getSource() instanceof JTable) {
					showPopup((JTable) e.getSource(), e);
				}
			}
		} else {
			TreePath p = guiTabRecordEdit.getTree().getClosestPathForLocation(e.getX(), e.getY());
			if (p != null) {
				if (e.isControlDown() || e.isShiftDown()) {
					guiTabRecordEdit.getTree().addSelectionPath(p);
				} else if (SwingUtilities.isLeftMouseButton(e)) {
					guiTabRecordEdit.getTree().setSelectionPath(p);
				}
				if (SwingUtilities.isRightMouseButton(e)) {
					if (e.getSource() instanceof JTree) {
						showTreePopup((JTree) e.getSource(), e);
					}
				}
			}
		}

	}

	/**
	 * @param tree
	 */
	private void showPopup(final JTable table, MouseEvent e) {

		JPopupMenu m = new JPopupMenu();
		int count = table.getSelectedRowCount();
		if (count == 1) {
			final File file = (File) guiTabRecordEdit.getTableModel().getValueAt(table.getSelectedRow(), 2);

			m.add(new JMenuItem(new AbstractAction(ControlMain.getProperty("button_rename")) {
				public void actionPerformed(ActionEvent e) {
					renameSelected(file);
				}
			}));

			String name = file.getName();
			if (SerHelper.isVideo(name) || SerHelper.isAudio(name)) {
				m.add(new JMenuItem(new AbstractAction(ControlMain.getProperty("button_playback")) {
					public void actionPerformed(ActionEvent e) {
						openFile(file);
					}
				}));
			} else {
				if (file != null && !file.isDirectory()) {
					m.add(new JMenuItem(new AbstractAction(ControlMain.getProperty("open")) {
						public void actionPerformed(ActionEvent e) {
							openFile(file);
						}
					}));
				}
			}

			m.add(new JMenuItem(new AbstractAction(ControlMain.getProperty("button_delete")) {
				public void actionPerformed(ActionEvent e) {
					deleteSelectedFromTable(table);
				}
			}));

		} else if (count > 0) {
			m.add(new JMenuItem(new AbstractAction(ControlMain.getProperty("button_delete")) {
				public void actionPerformed(ActionEvent e) {
					deleteSelectedFromTable(table);
				}
			}));
		}

		if (count > 0) {
			m.addSeparator();

			m.add(new JMenuItem(new AbstractAction("Demultiplex/Multiplex") {
				public void actionPerformed(ActionEvent e) {

					ArrayList l = new ArrayList();
					int[] aiSel = table.getSelectedRows();
					for (int i = 0; i < aiSel.length; i++) {
                        int fileIndex = guiTabRecordEdit.fileTableSorter.modelIndex(aiSel[i]);
                        
						File file = (File) guiTabRecordEdit.getTableModel().getValueAt(fileIndex, 2);
						l.add(file);
					}
					
                    BOAfterRecordOptions options=ControlMain.getSettingsRecord().getAfterRecordOptions();
					new ControlMuxxerView((BOAfterRecordOptions)SerHelper.serialClone(options), l);
				}
			}));
		}

		if (m.getComponentCount() > 0) {
			m.show(table, e.getX(), e.getY());
		}
	}
	/**
	 * @param table
	 */
	protected void deleteSelectedFromTable(JTable table) {
		if (askToDelete()) {

			ArrayList toDelete = new ArrayList();
			int[] aiRows = table.getSelectedRows();
			for (int i = 0; i < aiRows.length; i++) {
				toDelete.add(table.getModel().getValueAt(aiRows[i], 2));
			}
			deleteFiles(toDelete);
			reloadAvailableFiles();
		}
	}

	/**
	 * @return
	 */
	private boolean askToDelete() {
		int res = JOptionPane.showOptionDialog(guiTabRecordEdit, ControlMain.getProperty("msg_deleteFiles"), ControlMain
				.getProperty("button_cancel"), 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{
				ControlMain.getProperty("button_delete"), ControlMain.getProperty("button_cancel")}, "");
		return res == 0;
	}

	private void deleteFiles(Object[] files) {
		ArrayList l = new ArrayList(Arrays.asList(files));
		deleteFiles(l);
	}

	/**
	 * delete all the given files
	 * 
	 * @param toDelete
	 */
	private void deleteFiles(ArrayList toDelete) {
		Iterator iter = toDelete.iterator();

		while (iter.hasNext()) {
			File element = (File) iter.next();
			if (element.delete()) {
				Logger.getLogger("ControlProgramTab").info(element.getAbsolutePath() + " " + ControlMain.getProperty("msg_deleted"));
			} else {
				Logger.getLogger("ControlProgramTab").error(element.getAbsolutePath() + " " + ControlMain.getProperty("msg_cantdelete"));
			}
		}
	}

	/**
	 * not activated yet
	 * 
	 * @param tree
	 */
	private void showTreePopup(JTree table, MouseEvent e) {

		JPopupMenu m = new JPopupMenu();
		int count = table.getSelectionCount();
		if (count > 0) {

			m.add(new JMenuItem(new AbstractAction(ControlMain.getProperty("button_delete")) {
				public void actionPerformed(ActionEvent e) {
					deleteSelectedOfDirectoryTree();
				}
			}));

		}
		if (m.getComponentCount() > 0) {
			m.show(table, e.getX(), e.getY());
		}
	}

	/**
	 * @param absoluteFile
	 */
	private void startMuxxi(File file) {

		String execMuxxi = "C:\\Programme\\D-Box\\DVDAuthorMuxxi\\Muxxi.exe -i \"" + file.getAbsolutePath() + "\" -out DVD";
		SerExternalProcessHandler.startProcess("Muxxi", execMuxxi, true);
	}

	protected void renameSelected(File f) {
		String newName = JOptionPane.showInputDialog(guiTabRecordEdit, ControlMain.getProperty("msg_rename"), f.getName());
		if (newName != null && newName.length() > 0) {
			File newFile = new File(f.getParent(), newName);
			f.renameTo(newFile);
		}
		reloadAvailableFiles();
	}

	/**
	 * @param nodeToDelete
	 */
	private void deleteChildNodes(BaseTreeNode nodeToDelete, HashSet filesToDelete) {

		if (nodeToDelete.getChildCount() > 0) {
			Enumeration childs = nodeToDelete.children();
			while (childs.hasMoreElements()) {
				BaseTreeNode element = (BaseTreeNode) childs.nextElement();
				deleteChildNodes(element, filesToDelete);
			}

		}

		// delete file
		Object obj = nodeToDelete.getUserObject();
		if (obj instanceof BOFileWrapper) {
			File f = ((BOFileWrapper) obj).getAbsoluteFile();
			filesToDelete.add(f);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			if (e.getSource() instanceof JList) {

			} else {
				int row = guiTabRecordEdit.getFileTable().getSelectedRow();
				if (row > -1) {
					File info = (File) guiTabRecordEdit.getFileTable().getModel().getValueAt(row, 2);

					String values = SerHelper.createFileInfo(info);

					guiTabRecordEdit.setFileInfo(values);
				} else {
					guiTabRecordEdit.setFileInfo("");
				}
			}
		}
	}

	/**
	 *  
	 */
	protected void deleteSelectedOfDirectoryTree() {

		if (askToDelete()) {

			ArrayList filesToDelete = new ArrayList();
			ArrayList dirToDelete = new ArrayList();

			// Delete all selected files
			JTree tree = guiTabRecordEdit.getTree();
			TreePath[] sel = tree.getSelectionPaths();
			for (int i = 0; i < sel.length; i++) {
				DefaultMutableTreeNode nodeToDelete = (DefaultMutableTreeNode) sel[i].getLastPathComponent();
				Object obj = nodeToDelete.getUserObject();
				if (obj instanceof BOFileWrapper) {
					File dirToDel = ((BOFileWrapper) obj).getAbsoluteFile();
					addAllFiles(dirToDel, filesToDelete, dirToDelete);
				}
			}

			// Delete files

			deleteFiles(filesToDelete);

			// Delete directories
			deleteFiles(dirToDelete);

			reloadAvailableFiles();
		}

	}

	/**
	 * @param dirToDel
	 * @param filesToDelete
	 * @param dirToDelete
	 */
	private void addAllFiles(File dirToDel, ArrayList filesToDelete, ArrayList dirToDelete) {
		dirToDelete.add(0, dirToDel);

		File[] files = dirToDel.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					addAllFiles(files[i], filesToDelete, dirToDelete);
				} else {
					filesToDelete.add(files[i]);
				}
			}
		}
	}
}