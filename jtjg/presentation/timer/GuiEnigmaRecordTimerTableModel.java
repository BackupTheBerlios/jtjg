package presentation.timer;



import model.BOTimer;
import control.ControlMain;
import control.ControlTimerTab;

/*
GuiEnigmaRecordTimerTableModel.java by Geist Alexander, Treito

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
public class GuiEnigmaRecordTimerTableModel extends GuiRecordTimerTableModel 
{
	
	
	
	public GuiEnigmaRecordTimerTableModel(ControlTimerTab ctrl){
		this.setControl(ctrl);
	}

	public int getColumnCount() {
		return 7;	
	}	

	public int getRowCount() {
		if (this.getControl().getTimerList() != null) {
			return this.getControl().getTimerList().getRecordTimerList().size();
		}
		return 0;
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
		BOTimer timer = (BOTimer)this.getControl().getTimerList().getRecordTimerList().get(rowIndex);
		if (columnIndex == 1) {
		    return control.convertShortTimerStatus(timer.getEventTypeId());
		}else if (columnIndex == 0) {
			return new Boolean(timer.getLocalTimer().isLocal());
		} else if (columnIndex == 2) {
			return timer.getSenderName();
		} else if (columnIndex == 3) {
			return timer.getStartTime();
		} else if (columnIndex == 4) {
			return timer.getStopTime();
		} else if (columnIndex == 5) {
			return control.convertShortEventRepeat(timer.getEventRepeatId());
		} 
		return timer.getLocalTimer().getDescription();
	}
	
	

	public String getColumnName( int columnIndex ) {
		if (columnIndex == 1) {
		    return ControlMain.getProperty("state");
		} else if (columnIndex == 0) {
			return " ";
		} else if (columnIndex == 2) {
			return ControlMain.getProperty("sender");
		} else if (columnIndex == 3) {
			return ControlMain.getProperty("start");
		} else if (columnIndex == 4) {
			return ControlMain.getProperty("end");
		} else if (columnIndex == 5) {
			return ControlMain.getProperty("repeat");
		} 
		return ControlMain.getProperty("title");
	}
	
	public boolean isCellEditable (int row, int col) {
	    Class columnClass = getColumnClass(col);
	    return false;
	    
	}
	
	
	public Class getColumnClass (int col) {
        if (col==0) {
            return Boolean.class;
        }
        return String.class;
    }
	
	
}
