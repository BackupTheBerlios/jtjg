package presentation.recordInfo;
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA. 
 */

/**
 * shows all available files in the store directory
 * 
 * @author Reinhard Achleitner
 */
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import model.BORecordInfos;
import presentation.GuiTab;
import presentation.GuiTableSorter;
import control.ControlRecordInfoTab;

public class GuiTabRecordInfoOverview extends GuiTab {

	private ControlRecordInfoTab control;

	private JTable recordInfoTable;
	private GuiRecordInfosTableModel infosTableModel;
	public GuiTableSorter fileTableSorter;

	public GuiTabRecordInfoOverview(ControlRecordInfoTab control) {
		this.setControl(control);
		initialize();
	}

	/**
	 * erzeugt die GUI
	 *  
	 */
	protected void initialize() {
		setLayout(new BorderLayout());

		JScrollPane p = new JScrollPane(getRecordInfoTable());
		p.getViewport().setBackground(Color.white);
		add(p, BorderLayout.CENTER);
	}
	/**
	 * @return
	 */
	public JTable getRecordInfoTable() {
		if (recordInfoTable == null) {

			recordInfoTable = new JTable();
			//recordInfoTable.setShowHorizontalLines(false);
			recordInfoTable.setShowVerticalLines(false);
			infosTableModel = new GuiRecordInfosTableModel(recordInfoTable);
			//fileTableSorter = new GuiTableSorter(infosTableModel);
			recordInfoTable.setModel(infosTableModel);
			//fileTableSorter.setTableHeader(recordInfoTable.getTableHeader());
			recordInfoTable.addMouseListener(control);
			recordInfoTable.getSelectionModel().addListSelectionListener(control);
			
			recordInfoTable.getColumnModel().getColumn(0).setPreferredWidth(281);
			recordInfoTable.getColumnModel().getColumn(1).setPreferredWidth(186);
			recordInfoTable.getColumnModel().getColumn(2).setPreferredWidth(122);
			recordInfoTable.getColumnModel().getColumn(3).setPreferredWidth(190);
			
			recordInfoTable.addAncestorListener(new AncestorListener()
					{

						public void ancestorAdded(AncestorEvent event) {
							if (!BORecordInfos.isLoaded())
							{
								BORecordInfos.getInfos();
								infosTableModel.fireTableDataChanged();
							}
							
						}

						public void ancestorRemoved(AncestorEvent event) {
							// TODO Auto-generated method stub
							
						}

						public void ancestorMoved(AncestorEvent event) {
							// TODO Auto-generated method stub
							
						}
				
					});
			
		}
		return recordInfoTable;
	}

	/**
	 * @return ControlProgramTab
	 */
	public ControlRecordInfoTab getControl() {
		return control;
	}

	/**
	 * Sets the control.
	 * 
	 * @param control
	 *            The control to set
	 */
	public void setControl(ControlRecordInfoTab control) {
		this.control = control;
	}

	public GuiRecordInfosTableModel getTableModel() {
		return infosTableModel;
	}	
}