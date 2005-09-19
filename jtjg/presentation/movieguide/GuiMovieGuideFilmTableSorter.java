package presentation.movieguide;
/*
GuiMovieGuideFilmTableSorter.java by Geist Alexander 

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
import java.util.Comparator;
import java.util.GregorianCalendar;

import javax.swing.table.TableModel;

import presentation.GuiTableSorter;
import service.SerFormatter;


public class GuiMovieGuideFilmTableSorter extends GuiTableSorter {
	
	public GuiMovieGuideFilmTableSorter() {
        this.mouseListener = new MouseHandler();
        this.tableModelListener = new TableModelHandler();
    }

    public GuiMovieGuideFilmTableSorter(TableModel tableModel) {
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
            GregorianCalendar firstDate = SerFormatter.getDateFromString((String)o1, "dd.MM.yy   HH:mm");
            GregorianCalendar secondDate = SerFormatter.getDateFromString((String)o1, "dd.MM.yy   HH:mm");
            return SerFormatter.compareDates(firstDate, secondDate);
        }
    };
}
