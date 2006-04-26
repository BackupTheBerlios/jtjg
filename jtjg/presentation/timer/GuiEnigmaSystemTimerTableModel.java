package presentation.timer;
/*
GuiNeutrinoSystemTimerTableModel.java by Geist Alexander 

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
import java.util.GregorianCalendar;

import model.BOTimer;
import service.SerFormatter;
import control.ControlMain;
import control.timer.ControlTimerTab;


public class GuiEnigmaSystemTimerTableModel extends GuiSystemTimerTableModel 
{
	
	public GuiEnigmaSystemTimerTableModel(ControlTimerTab ctrl){
		this.setControl(ctrl);
	}

	public int getColumnCount() {
		return 3;	
	}	

	public int getRowCount() {
		if (this.getControl().getTimerList() != null) {
			return this.getControl().getTimerList().getSystemTimerList().size();
		}
		return 0;
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
		BOTimer timer = (BOTimer)this.getControl().getTimerList().getSystemTimerList().get(rowIndex);
		if (columnIndex == 0) {
			return control.convertShortEventType(timer.getEventTypeId());
		} else if (columnIndex == 1) {
			return timer.getStartTime();
		}
		return control.convertShortEventRepeat(timer.getEventRepeatId());
	}
	
	public void setValueAt(Object value, int row, int col) {
		BOTimer timer = (BOTimer)control.getTimerList().getSystemTimerList().get(row);
		if (col == 0) {
			timer.setEventTypeId(control.convertLongEventType((String)value));
		}
		if (col == 1) {
		    GregorianCalendar newDate = SerFormatter.getDateFromString((String)value, "dd.MM.yy   HH:mm");
			timer.setUnformattedStartTime(newDate.getTimeInMillis());
		} else if (col == 2) {
			timer.setEventRepeatId(control.convertLongEventRepeat((String)value));
			control.selectRepeatDaysForSystemTimer(timer);
		}
		timer.setModifiedId("modify");
    }

	public String getColumnName( int columnIndex ) {
		if (columnIndex == 0) {
			return ControlMain.getProperty("timerType");
		} else if (columnIndex == 1) {
			return ControlMain.getProperty("nextStart");
		} 
		return ControlMain.getProperty("repeat");
	}
	
	public boolean isCellEditable (int row, int col) {
	    BOTimer timer = (BOTimer)control.getTimerList().getSystemTimerList().get(row);
	    if (col==0 && timer.getModifiedId()==null) {
	    	return false;
	    }
	    return true;
	}	
}

