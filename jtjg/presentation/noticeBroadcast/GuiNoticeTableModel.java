package presentation.noticeBroadcast;
/*
GuiNoticeTableModel.java by Geist Alexander 

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

import model.BONoticeBroadcast;
import control.ControlMain;
import control.ControlNoticeBroadcastView;

public class GuiNoticeTableModel extends AbstractTableModel  {
	
	ControlNoticeBroadcastView control;
	
	public GuiNoticeTableModel(ControlNoticeBroadcastView ctrl) {
		control = ctrl;
	}

	public int getColumnCount() {
		return 5;	
	}	

	public int getRowCount() {
		return control.getNoticeList().size();
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
		BONoticeBroadcast notice = (BONoticeBroadcast)control.getNoticeList().get(rowIndex);
		if (columnIndex == 0) {
			return notice.getSearchString();
		}
		else if (columnIndex == 1) {
			return new Boolean(notice.isSearchEpg());
		}
		else if (columnIndex == 2) {
			return new Boolean(notice.isSearchMovieGuide());
		}
		else if (columnIndex == 3) {
			return new Boolean(notice.isSearchOnlyTitle());
		}
		return new Boolean(notice.isBuildTimer());
	}
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        BONoticeBroadcast notice = (BONoticeBroadcast)control.getNoticeList().get(rowIndex);
        if (columnIndex == 0) {
            notice.setSearchString((String)aValue);
        }
        else if (columnIndex == 1) {
            notice.setSearchEpg(((Boolean)aValue).booleanValue());
        }
        else if (columnIndex == 2) {
            notice.setSearchMovieGuide(((Boolean)aValue).booleanValue());
        }
        else if (columnIndex == 3) {
            notice.setSearchOnlyTitle(((Boolean)aValue).booleanValue());
        }
        else if (columnIndex == 4) {
            notice.setBuildTimer(((Boolean)aValue).booleanValue());
        }
    }

	public String getColumnName( int columnIndex ) {
		if (columnIndex == 0) {
			return ControlMain.getProperty("name"); 
		}
		else if (columnIndex == 1) {
			return ControlMain.getProperty("searchInEpg");
		}
		else if (columnIndex == 2) {
			return ControlMain.getProperty("searchinMG");
		}
		else if (columnIndex == 3) {
			return ControlMain.getProperty("searchOnlyTitle");
		}
		return ControlMain.getProperty("buildTimer");
	}
		
	public boolean isCellEditable (int row, int col) {
	    return true;
	}

	public Class getColumnClass (int col) {
		if (col==0) {
			return String.class;
		}
		return Boolean.class;
	}
}
