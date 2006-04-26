package presentation.timer;
/*
GuiNeutrinoRecordTimerTableModel.java by Geist Alexander, Zielke Sven 

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

import javax.swing.table.AbstractTableModel;

import control.timer.ControlTimerTab;

public abstract class GuiRecordTimerTableModel extends AbstractTableModel
{
	ControlTimerTab control;
	
	public abstract int getColumnCount();

	public abstract int getRowCount();

	public abstract Object getValueAt( int rowIndex, int columnIndex );

	public abstract String getColumnName( int columnIndex );
	public abstract boolean isCellEditable (int row, int col);

	public ControlTimerTab getControl() {
		return control;
	}
	
	public void setControl(ControlTimerTab control) {
		this.control = control;
	}
	
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
		this.getControl().getView().enableRecordTimerWeekdays(false);
	}
    
    public abstract Class getColumnClass (int col);
}
