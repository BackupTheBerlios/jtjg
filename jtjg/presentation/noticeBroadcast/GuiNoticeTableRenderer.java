package presentation.noticeBroadcast;
/*
GuiNoticeTableRenderer.java by Geist Alexander 

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;


public class GuiNoticeTableRenderer extends JList implements TableCellRenderer {
	public GuiNoticeTableRenderer() {
	    setOpaque(true);
	    setForeground(UIManager.getColor("TableHeader.foreground"));
	    setBackground(UIManager.getColor("TableHeader.background"));
	    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
	    ListCellRenderer renderer = getCellRenderer();
	    ((JLabel)renderer).setHorizontalAlignment(JLabel.CENTER);
	    setCellRenderer(renderer);
	}
 
	public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
	    setFont(table.getFont());
	    String str = (value == null) ? "" : value.toString();
	    BufferedReader br = new BufferedReader(new StringReader(str));
	    String line;
	    Vector v = new Vector();
	    try {
	      while ((line = br.readLine()) != null) {
	        v.addElement(line);
	      }
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    }
	    setListData(v);
	    return this;
	}
}