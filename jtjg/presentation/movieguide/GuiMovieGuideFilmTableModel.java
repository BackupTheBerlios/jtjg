package presentation.movieguide;

import javax.swing.table.AbstractTableModel;

import model.BOMovieGuide;
import control.ControlMain;
import control.ControlMovieGuideTab;


public class GuiMovieGuideFilmTableModel extends AbstractTableModel 
{
	ControlMovieGuideTab control;	    			
	private static final String[] COLUMN_NAME = {ControlMain.getProperty("table_txt_titel")};
	
	public GuiMovieGuideFilmTableModel(ControlMovieGuideTab ctrl){
		this.setControl(ctrl);			
	}

	public int getColumnCount() {
		return 1;	
	}	

	public int getRowCount() {
		int value = 0;
		if (this.getControl().getTitelMap() == null) {
			value = 0;
		}else {
			value =  this.getControl().getTitelMap().size();
		}
		return value;
	}

	public Object getValueAt( int rowIndex, int columnIndex ) {
		Object value = null;
		if (columnIndex == 0) {					
				value= ((BOMovieGuide)this.getControl().getTitelMap().get(rowIndex)).getTitel();
		}
		return value;	
	}
	
	public String getColumnName( int columnIndex ) {				
		String value = null;
		value = COLUMN_NAME[columnIndex];
		return value;	
	}
	
	public boolean isCellEditable (int row, int col) {
	    return false;
	}
	
	public ControlMovieGuideTab getControl() {
		return control;
	}
	
	public void setControl(ControlMovieGuideTab control) {
		this.control = control;
	}		

	public void fireTableDataChanged(int value) {								
		this.getControl().setTitelMapSelected(this.getControl().getSelectedItemJComboBox(),value);
		super.fireTableDataChanged();
	}

}
