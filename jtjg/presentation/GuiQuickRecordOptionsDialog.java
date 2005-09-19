package presentation;
/*
GuiQuickRecordOptionsDialog.java by Geist Alexander 

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

import java.util.Date;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

import model.BOPid;
import model.BOPids;
import model.BOQuickRecordOptions;
import control.ControlMain;

public class GuiQuickRecordOptionsDialog {
    private BOPids pids;
    private JList pidList;
    private JSpinner minsSpinner;
    
    public GuiQuickRecordOptionsDialog(BOPids pids) {
        this.setPids(pids);
    }

    public BOQuickRecordOptions startPidsQuestDialog() {
		int res = JOptionPane.showOptionDialog(
	        ControlMain.getControl().getView(), 
			new Object[]{
		        ControlMain.getProperty("label_selectPids"), 
		        new JScrollPane(getPidList()),
		        ControlMain.getProperty("msg_stopTime"), 
		        getMinsSpinner()
			}, 
			"", 
			0,
			JOptionPane.QUESTION_MESSAGE, 
			null, 
			new String[]{ControlMain.getProperty("button_ok"), ControlMain.getProperty("button_cancel")},
			ControlMain.getProperty("button_ok")
		);
		if (res == 0) {
		    return buildRecordOptions();
		}
		return null;
	}
    
    private BOQuickRecordOptions buildRecordOptions() {
        //Pids
        Object[] pidArray = getPidList().getSelectedValues();
		BOPids newPidList = new BOPids();
		for (int i=0; i<pidArray.length; i++) {
		    BOPid pid = (BOPid)pidArray[i];
			switch (pid.getId()) {
				case 0: newPidList.setVPid(pid);
				break;
				case 1: newPidList.getAPids().add(pid);
				break;
				case 2: newPidList.setVtxtPid(pid);
				break;
			}
		}
		//Stoptime
		Integer value = (Integer)minsSpinner.getValue();
		long millis = new Date().getTime();
		Date stopTime = new Date(millis + value.intValue()*60000);
		//PMT 
        newPidList.setPmtPid(this.getPids().getPmtPid());
		return new BOQuickRecordOptions(newPidList, stopTime);
    }
    
    private JList getPidList() {
        if (pidList==null) {
            pidList = new JList(pids.getAllPids().toArray());
            pidList.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            pidList.setSelectedIndices(pids.getIndices());
        }
		return pidList;
    }
    /**
     * @return Returns the minsSpinner.
     */
    public JSpinner getMinsSpinner() {
        if (minsSpinner==null) {
            SpinnerNumberModel model = new SpinnerNumberModel(60, 1, 300, 1);
            minsSpinner = new JSpinner(model);	   
        }
        return minsSpinner;
    }
    /**
     * @return Returns the pids.
     */
    public BOPids getPids() {
        return pids;
    }
    /**
     * @param pids The pids to set.
     */
    public void setPids(BOPids pids) {
        this.pids = pids;
    }
    /**
     * @param minsSpinner The minsSpinner to set.
     */
    public void setMinsSpinner(JSpinner minsSpinner) {
        this.minsSpinner = minsSpinner;
    }
    /**
     * @param pidList The pidList to set.
     */
    public void setPidList(JList pidList) {
        this.pidList = pidList;
    }
}
