package presentation.program;
/*
GuiIpListComboModel.java by Geist Alexander 

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

import javax.swing.DefaultComboBoxModel;

import model.BOBox;
import control.ControlMain;

/**
 * ComboBoxModel der JCombobox IP-Auswahl im Programm-Tab
 */
public class GuiIpListComboModel extends DefaultComboBoxModel { //implements ComboBoxModel {
	
	public Object getElementAt(int index) {
		BOBox box = (BOBox)ControlMain.getSettingsMain().getBoxList().get(index);
		return box.getDboxIp();
	}

	public int getSize() {
		return ControlMain.getSettingsMain().getBoxList().size();
	}
}
