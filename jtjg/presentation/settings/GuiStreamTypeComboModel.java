package presentation.settings;
/*
GuiStreamTypeComboModel.java by Geist Alexander 

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

/**
 * ComboBoxModel der JCombobox Stream-Typ
 */
public class GuiStreamTypeComboModel extends DefaultComboBoxModel { 
	
	String[] streamTypes;
	
	public GuiStreamTypeComboModel(String[] types) {
		streamTypes = types;
	}
	
	public Object getElementAt(int index) {
		return streamTypes[index];
	}

	public int getSize() {
		if (streamTypes!=null) {
			return streamTypes.length;
		}
		return 0;
	}
	/**
	 * @param streamType The streamType to set.
	 */
	public void setStreamType(String[] types) {
		this.streamTypes = types;
		this.removeAllElements();
		for (int i=0; i<streamTypes.length; i++) {
			this.addElement(streamTypes[i]);
		}
	}
}
