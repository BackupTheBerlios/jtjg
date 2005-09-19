package presentation.recordInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;

import model.BOFileWrapper;
import service.SerHelper;

/**
 * @author Reinhard Achleitner
 * @version 06.12.2004
 *  
 */
public class GuiFileTableModel extends DefaultTableModel implements TreeSelectionListener {

	private ArrayList files = new ArrayList();

	private String[] columns = new String[]{"Name"};

	private JTable table;

	/**
	 *  
	 */
	public GuiFileTableModel(JTable table) {
		super();
		this.table = table;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	public int getRowCount() {
		if (files != null) {
			return files.size();
		} 
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columns.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		return columns[column];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object aValue, int row, int column) {
		try {
			if (aValue != null && aValue.toString().length() > 0) {
				Object[] oneFile = (Object[]) files.get(row);
				File f = (File) oneFile[2];
				File newF = new File(f.getParent(), aValue.toString());
				if (f.renameTo(newF)) {
					oneFile[0] = newF.getName();
					oneFile[2] = newF;
				} else {
					fireTableDataChanged();
				}
			}
		} catch (Exception e) {
			fireTableDataChanged();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int column) {
		if (row < files.size())
		{
			Object[] oneFile = (Object[]) files.get(row);
			return oneFile[column];
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		ArrayList list = new ArrayList();
		if (e.getPath() != null) {
			Object o = e.getPath().getLastPathComponent();
			if (o instanceof BaseTreeNode && ((BaseTreeNode) o).getUserObject() instanceof BOFileWrapper) {
				BOFileWrapper fileWr = (BOFileWrapper) ((BaseTreeNode) o).getUserObject();
				File[] files = fileWr.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						if (!files[i].isDirectory()) {
							Object[] oneFile = new Object[3];
							oneFile[0] = files[i].getName();
							oneFile[1] = SerHelper.calcSize(files[i].length(), "MB");
							oneFile[2] = files[i].getAbsoluteFile();
							list.add(oneFile);
						}
					}
				}
			}
		}
		files = list;

		// Aktuell selektierte Merken
		int[] aiSel = table.getSelectedRows();
		ArrayList toSel = new ArrayList();
		if (aiSel != null) {
			for (int i = 0; i < aiSel.length; i++) {
				toSel.add(getValueAt(aiSel[i],0));
			}
		}
		fireTableDataChanged();
		// Wieder setzen
		
		Iterator iter = toSel.iterator();

		int rowCount = getRowCount();
		while (iter.hasNext()) {
			String element = (String) iter.next();
			for (int i = 0; i < rowCount; i++) {
				
				if (element != null && element.equals(getValueAt(i,0)))
				{
					table.getSelectionModel().addSelectionInterval(i,i);
				}
			}
			
		}
	}

}