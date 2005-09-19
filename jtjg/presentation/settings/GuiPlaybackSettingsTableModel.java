package presentation.settings;
/*
GuiPlaybackSettingsTableModel.java by Geist Alexander 

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

import model.BOPlaybackOption;
import service.SerAlertDialog;
import control.ControlMain;
import control.ControlSettingsTabPlayback;

public class GuiPlaybackSettingsTableModel extends AbstractTableModel  {
	
    ControlSettingsTabPlayback control;
	
	public GuiPlaybackSettingsTableModel(ControlSettingsTabPlayback ctrl) {
		control = ctrl;
	}

	public int getColumnCount() {
		return 4;	
	}	

	public int getRowCount() {
		return ControlMain.getSettingsPlayback().getPlaybackOptions().size();
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
	    BOPlaybackOption playbackOption = (BOPlaybackOption)ControlMain.getSettingsPlayback().getPlaybackOptions().get(rowIndex);
		if (columnIndex == 0) {
			return playbackOption.getName();
		}
		if (columnIndex == 1) {
			return playbackOption.getPlaybackOption();
		}
		if (columnIndex == 2) {
			return new Boolean(playbackOption.isLogOutput());
		}
		return new Boolean(playbackOption.isStandard());
	}
			
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	    BOPlaybackOption playbackOption = (BOPlaybackOption)ControlMain.getSettingsPlayback().getPlaybackOptions().get(rowIndex);
		if (columnIndex == 0) {
		    playbackOption.setName((String)aValue);
		}
		if (columnIndex == 1) {
		    playbackOption.setPlaybackOption((String)aValue);
		}
		if (columnIndex == 2) {
		    playbackOption.setLogOutput(((Boolean)aValue).booleanValue());
		}
		//nur eine Checkbox darf selektiert sein!!
		if (columnIndex == 3) {
			if (((Boolean)aValue).booleanValue()) {
				ArrayList playbackList = ControlMain.getSettingsPlayback().getPlaybackOptions();
				for (int i=0; i<playbackList.size(); i++) { 
				    BOPlaybackOption boxx = (BOPlaybackOption)playbackList.get(i);
					boxx.setStandard(false);
					this.fireTableDataChanged();
				}
			}
			playbackOption.setStandard(((Boolean)aValue).booleanValue());
		}
	}

	public String getColumnName( int columnIndex ) {
		if (columnIndex == 0) {
			return ControlMain.getProperty("name"); 
		}
		if (columnIndex == 1) {
			return ControlMain.getProperty("playbackOption");
		}
		if (columnIndex == 2) {
			return ControlMain.getProperty("logOutput");
		}
		return ControlMain.getProperty("standard");
	}
	
	/*
	 * Wenn die 1. Box angelegt wird, diese als Standard deklarieren
	 * und Logoutput auf aktiv setzen.
	 */
	public void addRow(BOPlaybackOption playbackOption) {
		if (ControlMain.getSettingsPlayback().getPlaybackOptions().size()==0) {
		    playbackOption.setStandard(true);
		    playbackOption.setLogOutput(true);
		}
		ControlMain.getSettingsPlayback().addPlaybackOption(playbackOption);
		fireTableDataChanged();
	}
	
	public void removeRow(int rowNumber) {
		try {
			ControlMain.getSettingsPlayback().removePlaybackOption(rowNumber);
			fireTableDataChanged();
		} catch (ArrayIndexOutOfBoundsException ex) {SerAlertDialog.alert(ControlMain.getProperty("msg_selectRow"), control.getMainView());}
	}
	
	public boolean isCellEditable (int row, int col) {
	    return true;
	}

	public Class getColumnClass (int col) {
       if (col==2 || col==3) {
          return Boolean.class;
       }
       return String.class;
	}
}
