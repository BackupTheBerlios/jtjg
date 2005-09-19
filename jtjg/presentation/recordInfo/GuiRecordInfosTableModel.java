package presentation.recordInfo;

import java.awt.*;

import javax.swing.table.*;


import model.*;

/**
 * @author Reinhard Achleitner
 * @version 09.02.2005
 *  
 */
public class GuiRecordInfosTableModel extends DefaultTableModel {

	private String[] colNames = new String[]{"Titel", "Datum", "Sender", "Engine"};

	private Container parent;
	
	/**
	 *  
	 */
	public GuiRecordInfosTableModel(Container parent) {
		super();
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (colNames == null) {
			return 0;
		}
		return colNames.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		return colNames[column];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	public int getRowCount() {
		if (parent == null || !parent.isShowing() && !BORecordInfos.isLoaded())
		{
			return 0;
		}
		
		return BORecordInfos.getInfos().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int column) {
		BORecordInfo info = (BORecordInfo) BORecordInfos.getInfos().elementAt(row);
		switch (column) {
			case 0 :
				return info.getTitle();
			case 1 :
				return info.getTime();
			case 2 :
				return info.getChannel();
			case 3 :
				return info.getEngine();
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}