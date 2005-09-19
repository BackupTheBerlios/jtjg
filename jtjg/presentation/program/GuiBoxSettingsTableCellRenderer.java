package presentation.program;
/*
GuiBoxSettingsTableCellRenderer.java by Geist Alexander 

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

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import model.BOBox;
import control.ControlMain;

public class GuiBoxSettingsTableCellRenderer extends DefaultTableCellRenderer {
	
	public GuiBoxSettingsTableCellRenderer() {
		super();
	}
	
	public Component getTableCellRendererComponent(
					JTable table,
					Object value,
					boolean isSelected,
					boolean hasFocus,
					int row,
					int column) 
		{
			BOBox box = (BOBox)ControlMain.getSettingsMain().getBoxList().get(row);
			JCheckBox checkbox = new JCheckBox();
			checkbox.setHorizontalAlignment(SwingConstants.CENTER);
			checkbox.setSelected(box.isStandard());
			return checkbox;
		}
}
