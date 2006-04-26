package presentation.timer;
/*
 * GuiNeutrinoSystemTimerTableModel.java by Geist Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 */
import model.BOTimer;
import control.ControlMain;
import control.timer.ControlTimerTab;

public class GuiNeutrinoSystemTimerTableModel extends GuiSystemTimerTableModel {

	public GuiNeutrinoSystemTimerTableModel(ControlTimerTab ctrl) {
		this.setControl(ctrl);
	}

	public int getColumnCount() {
		return 5;
	}

	public int getRowCount() {
		if (this.getControl().getTimerList() != null) {
			return this.getControl().getTimerList().getSystemTimerList().size();
		}
		return 0;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		BOTimer timer = (BOTimer) this.getControl().getTimerList().getSystemTimerList().get(rowIndex);
		if (columnIndex == 0) {
			return control.convertShortEventType(timer.getEventTypeId());
		} else if (columnIndex == 1) {
			if (timer.getSenderName() != null && timer.getSenderName().equals("0")) {
				return "";
			}
			return timer.getSenderName();
		} else if (columnIndex == 2) {
			return timer.getStartTime();
		} else if (columnIndex == 3) {
			return control.convertShortEventRepeat(timer.getEventRepeatId());
		} else if (columnIndex == 4) {
			return timer.getProcessName();
		}
		return "";

	}

	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return ControlMain.getProperty("timerType");
		} else if (columnIndex == 1) {
			return ControlMain.getProperty("sender");
		} else if (columnIndex == 2) {
			return ControlMain.getProperty("nextStart");
		} else if (columnIndex == 3) {
			return ControlMain.getProperty("repeat");
		} else {
			return ControlMain.getProperty("process");
		}

	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

}