package presentation.recordInfo;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import service.SerIconManager;

/**
 * @author Reinhard Achleitner
 * @version 02.12.2004
 *  
 */
public class GuiAvailableFilesTreeRenderer extends DefaultTreeCellRenderer {

	/**
	 *  
	 */
	public GuiAvailableFilesTreeRenderer() {
		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean,
	 *      boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		JLabel lab = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, false, row, hasFocus);
		if (row == 0) {
			lab.setIcon(SerIconManager.getInstance().getIcon("grabber1.gif"));

		} 
		return lab;

	}

}