package presentation.movieguide;

import java.util.GregorianCalendar;

import javax.swing.table.AbstractTableModel;
import model.BOMovieGuide;
import control.ControlMain;
import control.ControlMovieGuideTab;
import service.SerFormatter;

public class GuiMovieGuideTimerTableModel extends AbstractTableModel 
{
	ControlMovieGuideTab control;
	private static final String[] COLUMN_NAME = {ControlMain.getProperty("table_txt_datum"),ControlMain.getProperty("table_txt_start"),ControlMain.getProperty("table_txt_ende"),ControlMain.getProperty("table_txt_dauer"),ControlMain.getProperty("table_txt_programm")};
	private static final String DATE_SHORT = "EEEE, dd.MMM.yy";
	private static final String DATE_TIME = "HH:mm";
	
	public GuiMovieGuideTimerTableModel(ControlMovieGuideTab ctrl) {
		this.setControl(ctrl);
	}

	public int getColumnCount() {
		return 5;	
	}	

	public int getRowCount() {
		if (this.getControl().getTimerTableSize() <= 0) {
			return 0;
		} 			
		return this.getControl().getTimerTableSize();			
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
		Object value = null;
		int selectRow = this.getControl().getSelectRowFilmTable().intValue();				
		if (columnIndex == 0) {		
			value = SerFormatter.getFormatGreCal((GregorianCalendar)((BOMovieGuide)this.getControl().getTitelMap().get(selectRow)).getDatum().get(rowIndex),DATE_SHORT);
		}else if (columnIndex == 1) {						
			value = SerFormatter.getFormatGreCal((GregorianCalendar)((BOMovieGuide)this.getControl().getTitelMap().get(selectRow)).getStart().get(rowIndex),DATE_TIME );
		}else if (columnIndex == 2) {	
			value = SerFormatter.getFormatGreCal((GregorianCalendar)((BOMovieGuide)this.getControl().getTitelMap().get(selectRow)).getEnde().get(rowIndex),DATE_TIME );
		}else if (columnIndex == 3) {
			value = ((BOMovieGuide)this.getControl().getTitelMap().get(selectRow)).getDauer().get(rowIndex);
		}else if (columnIndex == 4) {
			value = ((BOMovieGuide)this.getControl().getTitelMap().get(selectRow)).getSender().get(rowIndex);
		} 
		return value;
	}
		
	public String getColumnName( int columnIndex ) {
		String value = null;
		value = COLUMN_NAME[columnIndex];
		return value;		
	}
	
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	public void fireTableDataChanged() {	
		super.fireTableDataChanged();
	}
	/**
	 * @return Returns the control.
	 */
	public ControlMovieGuideTab getControl() {
		return control;
	}
	/**
	 * @param control The control to set.
	 */
	public void setControl(ControlMovieGuideTab control) {
		this.control = control;
	}

}
