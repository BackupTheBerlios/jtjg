package presentation.program;
/*
GuiEpgTableModel.java by Geist Alexander 

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.table.AbstractTableModel;

import model.BOEpg;
import control.ControlMain;
import control.ControlProgramTab;
/**
 * TableModel des EPG-Tables
 * es wird eine eigene EPG-ArrayList verwaltet!! 
 * Diese EPG-list enthält nur EPG´s die mit dem Datum des DateChoosers übereinstimmen
 */
public class GuiEpgTableModel extends AbstractTableModel 
{
	ControlProgramTab control;
	ArrayList epgList = new ArrayList();
	public int indexRunningEpg = -1;
	
	public GuiEpgTableModel(ControlProgramTab ctrl) {
		this.setControl(ctrl);
	}

	public int getColumnCount() {
		return 4;	
	}	

	public int getRowCount() {
		if (this.getEpgList() == null) {
			return 0;
		}
		return this.getEpgList().size();
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
		BOEpg epg = (BOEpg)this.getEpgList().get(rowIndex);

		if (columnIndex == 0) {
			return epg.getStartdate().getTime();
		} else if (columnIndex == 1) {
			return epg.getEndDate().getTime();
		} else if (columnIndex == 2) {
			return epg.getDuration();
		} else {
			return epg.getTitle();
		}
	}
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

	public String getColumnName( int columnIndex ) {
		if (columnIndex == 0) {
			return ControlMain.getProperty("start");
		} else if (columnIndex == 1) {
			return ControlMain.getProperty("end");
		} else if (columnIndex == 2) {
			return ControlMain.getProperty("duration");
		} else {
			return ControlMain.getProperty("title");
		}
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	/**
	 * create a new EPG-list with equal Date to DateChooser
	 */
	public void createEpgList() {
		this.setIndexRunningEpg(-1);
		this.setEpgList(new ArrayList());
		if (control.getSelectedSender() != null && control.getSelectedSender().getEpg() != null ) {
			ArrayList fullList = control.getSelectedSender().getEpg();
			GregorianCalendar chooserDate = new GregorianCalendar();
			chooserDate.setTime(control.getDateChooserDate());
			
			for (int i=0; i<fullList.size(); i++) {
				BOEpg epg = (BOEpg)fullList.get(i);
				if (checkDate(epg,chooserDate)){
						this.getEpgList().add(epg);
				}
			}
			BOEpg runningEpg = control.getRunnigEpg(this.getEpgList());
			if (runningEpg != null) {
			    this.setIndexRunningEpg(this.getEpgList().indexOf(runningEpg));    
			}
		}
	}
	
	/**
	 * @param epg
	 * @return
	 */
	private boolean checkDate(BOEpg epg,GregorianCalendar chooserDate) {
	
		int day = epg.getStartdate().get(Calendar.DATE);
		int month = epg.getStartdate().get(Calendar.MONTH);					
		if (month == chooserDate.get(Calendar.MONTH))
		{
			if (day == chooserDate.get(Calendar.DATE))
			{
				return true;
			}
			else if (day == chooserDate.get(Calendar.DATE) + 1)
			{
				if (epg.getStartdate().get(Calendar.HOUR_OF_DAY) < 7)
				{
					return true;
				}
			}
		}
		return false;
//		return  (day == chooserDate.get(Calendar.DATE) && 
	//			month == chooserDate.get(Calendar.MONTH));
	}

	/**
	 * update the own EPG-list
	 */
	public void fireTableDataChanged() {
		this.createEpgList();
		super.fireTableDataChanged();
	}
	/**
	 * @return ArrayList
	 */
	public ArrayList getEpgList() {
		return epgList;
	}

	/**
	 * Sets the epgList.
	 * @param epgList The epgList to set
	 */
	public void setEpgList(ArrayList epgList) {
		this.epgList = epgList;
	}

	/**
	 * @return Returns the control.
	 */
	public ControlProgramTab getControl() {
		return control;
	}
	/**
	 * @param control The control to set.
	 */
	public void setControl(ControlProgramTab control) {
		this.control = control;
	}
	/**
	 * @return Returns the indexRunningEpg.
	 */
	public int getIndexRunningEpg() {
		return indexRunningEpg;
	}
	/**
	 * @param indexRunningEpg The indexRunningEpg to set.
	 */
	public void setIndexRunningEpg(int indexRunningEpg) {
		this.indexRunningEpg = indexRunningEpg;
	}
}
