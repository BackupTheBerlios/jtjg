package presentation.timer;
/*
GuiTimerTableSorter.java by Geist Alexander 

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import javax.swing.table.TableModel;

import presentation.GuiTableSorter;

public class GuiTimerTableSorter extends GuiTableSorter {
	
	public GuiTimerTableSorter() {
        this.mouseListener = new MouseHandler();
        this.tableModelListener = new TableModelHandler();
    }

    public GuiTimerTableSorter(TableModel tableModel) {
        this();
        setTableModel(tableModel);
    }
	
	 protected Comparator getComparator(int column) {
    	if (column == 1) {
    		return DATE_COMPARATOR;
    	}
        Class columnType = tableModel.getColumnClass(column);
        Comparator comparator = (Comparator) columnComparators.get(columnType);
        if (comparator != null) {
            return comparator;
        }
        if (Comparable.class.isAssignableFrom(columnType)) {
            return COMPARABLE_COMAPRATOR;
        }
        return LEXICAL_COMPARATOR;
    }
	
	public static final Comparator DATE_COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
        	try {
				String firstString = (String)o1;
				String secondString = (String)o2;
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy   HH:mm");
				Date firstDate = sdf.parse(firstString);
				Date secondDate = sdf.parse(secondString);
				return firstDate.compareTo(secondDate);
			} catch (ParseException e) {
				return 0;
			}
        }
    };
}
