package presentation.program;
/*
GuiSenderTableModel.java by Geist Alexander 

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

import model.BOSender;
import control.ControlMain;
import control.ControlProgramTab;

public class GuiSenderTableModel extends AbstractTableModel 
{
	
	ControlProgramTab control;

	
	public GuiSenderTableModel(ControlProgramTab ctrl) {
		control = ctrl;
	}

	public int getColumnCount() {
		return 2;	
	}	

	public int getRowCount() {
		if (control.getSelectedBouquet() == null || control.getSelectedBouquet().getSender() == null ) {
			return 0;
		} 
		return control.getSelectedBouquet().getSender().size();
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
		BOSender sender = (BOSender)control.getSelectedBouquet().getSender().get(rowIndex);
		if (columnIndex == 0) {
			return sender.getNummer();
		} 
		return sender.getName();
	}

	public String getColumnName( int columnIndex ) {
		if (columnIndex == 0) {
			return ControlMain.getProperty("nr"); 
		} 
		return ControlMain.getProperty("sender"); 
	}
	
	
	public boolean isCellEditable(int row, int col)  {
		return false;
	}
}
