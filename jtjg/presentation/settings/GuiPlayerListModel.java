package presentation.settings;
/*
ControlSettings.java by Geist Alexander 

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
import javax.swing.DefaultListModel;

import control.ControlMain;


public class GuiPlayerListModel extends DefaultListModel{

	public void addElement(Object obj) {
		ControlMain.getSettingsPlayback().getPlaybackPlayer().add(obj);
		super.addElement(obj);
	}
	
	public Object remove(int index) {
		ControlMain.getSettingsPlayback().getPlaybackPlayer().remove(index);
		return super.remove(index);
	}
}
