package presentation.timer;
/*
GuiNeutrinoRecordTimerTableModel.java by Geist Alexander 

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

import model.BOTimer;
import control.ControlMain;
import control.timer.ControlTimerTab;

public class GuiNeutrinoRecordTimerTableModel extends GuiRecordTimerTableModel
{

	public GuiNeutrinoRecordTimerTableModel(ControlTimerTab ctrl){
		this.setControl(ctrl);
	}

	public int getColumnCount() {
		return 6;
	}

	public int getRowCount() {
		if (this.getControl().getTimerList() != null) {
			return this.getControl().getTimerList().getRecordTimerList().size();
		}
		return 0;
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
		BOTimer timer;
        try {
            timer = (BOTimer)this.getControl().getTimerList().getRecordTimerList().get(rowIndex);
        } catch (IndexOutOfBoundsException e) {
            this.fireTableDataChanged();
            return "";
        }
		if (columnIndex == 0) {
			return new Boolean(timer.getLocalTimer().isLocal());
		} else if (columnIndex == 1) {
		    return timer.getSenderName();
		} else if (columnIndex == 2) {
		    return timer.getStartTime();
		} else if (columnIndex == 3) {
		    return timer.getStopTime();
		} else if (columnIndex == 4) {
			return control.convertShortEventRepeat(timer.getEventRepeatId());
		} 
		return timer.getLocalTimer().getDescription();
	}

	public String getColumnName( int columnIndex ) {
		if (columnIndex == 0) {
			return " ";
		} else if (columnIndex == 1) {
			return ControlMain.getProperty("sender");
		} else if (columnIndex == 2) {
			return ControlMain.getProperty("start");
		} else if (columnIndex == 3) {
			return ControlMain.getProperty("end");
		} else if (columnIndex == 4) {
			return ControlMain.getProperty("repeat");
		} 
		return ControlMain.getProperty("title");
	}

	public boolean isCellEditable (int row, int col) {
	    return false;
	}

		
    
    public Class getColumnClass (int col) {
        if (col==0) {
            return Boolean.class;
        }
        return String.class;
    }
}
