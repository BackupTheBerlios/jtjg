package presentation.settings;
/*
GuiBoxSettingsTableModel.java by Geist Alexander 

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

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.BOBox;
import service.SerAlertDialog;
import control.ControlMain;
import control.settings.ControlSettingsTabMain;

public class GuiBoxSettingsTableModel extends AbstractTableModel  {
	
    ControlSettingsTabMain control;
	
	public GuiBoxSettingsTableModel(ControlSettingsTabMain ctrl) {
		control = ctrl;
	}

	public int getColumnCount() {
		return 4;	
	}	

	public int getRowCount() {
		return ControlMain.getSettingsMain().getBoxList().size();
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
		BOBox box = (BOBox)ControlMain.getSettingsMain().getBoxList().get(rowIndex);
		if (columnIndex == 0) {
			return box.getDboxIp();
		} else if (columnIndex == 1) {
			return box.getLogin();
		} else if (columnIndex == 2) {
			return box.getPassword();
		}
		return new Boolean(box.isStandard());
	}
			
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		BOBox box = (BOBox)ControlMain.getSettingsMain().getBoxList().get(rowIndex);
		if (columnIndex == 0) {
			box.setDboxIp((String)aValue);
		}
		if (columnIndex == 1) {
			box.setLogin((String)aValue);
		}
		if (columnIndex == 2) {
			box.setPassword((String)aValue);
		}
		//nur eine Checkbox darf selektiert sein!!
		if (columnIndex == 3) {
			if (((Boolean)aValue).booleanValue()) {
				ArrayList boxList = ControlMain.getSettingsMain().getBoxList();
				for (int i=0; i<boxList.size(); i++) { 
					BOBox boxx = (BOBox)boxList.get(i);
					boxx.setStandard(false);
					this.fireTableDataChanged();
				}
			}
			box.setStandard(true);
		}
	}

	public String getColumnName( int columnIndex ) {
		if (columnIndex == 0) {
			return "Box-IP"; 
		} else if (columnIndex == 1) {
			return "Login";
		} else if (columnIndex == 2) {
			return ControlMain.getProperty("label_password");
		}
		return "Standard";
	}
	
	/*
	 * Wenn die 1. Box angelegt wird, diese als Standard deklarieren
	 */
	public void addRow(BOBox box) {
		if (ControlMain.getSettingsMain().getBoxList().size()==0) {
			box.setStandard(true);
		}
		ControlMain.getSettingsMain().addBox(box);
		fireTableDataChanged();
		
//		if (ControlMain.getSettingsMain().getBoxList().size()==1) {
//		    ControlMain.setActiveBox(box);
//		    ControlMain.newBoxSelected((BOBox)ControlMain.getSettingsMain().getBoxList().get(0));
//		    ControlMain.getControl().getView().getMainTabPane().tabProgramm=null;
//		    ControlMain.getControl().getView().getMainTabPane().reInitTimerPanel();
//		} else {
//		    this.refreshIpComboBox();
//		}
	}
	
	public void removeRow(int rowNumber) {
		try {
			ControlMain.getSettingsMain().removeBox(rowNumber);
			fireTableDataChanged();
			control.refreshIpComboBox();
		} catch (ArrayIndexOutOfBoundsException ex) {SerAlertDialog.alert(ControlMain.getProperty("msg_selectRow"), control.getMainView());}
	}
	
	public boolean isCellEditable (int row, int col) {
	    return true;
	}

	public Class getColumnClass (int col) {
       if (col==3) {
          return Boolean.class;
       }
       return String.class;
	}
}
