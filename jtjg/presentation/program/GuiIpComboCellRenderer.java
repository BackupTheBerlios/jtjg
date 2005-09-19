package presentation.program;
/*
GuiIpComboCellRenderer.java by Geist Alexander 

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
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
/**
 * ComboBoxModel der JCombobox IP-Auswahl im Programm-Tab
 */
public class GuiIpComboCellRenderer extends BasicComboBoxRenderer { 
	
	 public Component getListCellRendererComponent(
	         JList list,
	         Object value,
	         int index,
	         boolean isSelected,
	         boolean cellHasFocus)
	  {
	  	JLabel label = new JLabel((String)value, SwingConstants.CENTER);

	  	 if (isSelected) {
	  	 	label.setBackground(list.getSelectionBackground());
	  	 	label.setForeground(list.getSelectionForeground());
        }
        else {
        	label.setBackground(list.getBackground());
        	label.setForeground(list.getForeground());
        }

	  	label.setFont(list.getFont());
	  	label.setOpaque(true);

        if (value instanceof Icon) {
        	label.setIcon((Icon)value);
        }
        else {
        	label.setText((value == null) ? "" : value.toString());
        }
        return label;	  	
	  }
}
