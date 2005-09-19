package presentation;
/*
GuiAudioPidOptionsDialog.java by Geist Alexander 

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

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import model.BOPid;
import model.BOPids;
import control.ControlMain;

public class GuiAudioPidOptionsDialog {
    private BOPids pids;
    private JList pidList;
    
    public GuiAudioPidOptionsDialog(BOPids pids) {
        this.setPids(pids);
    }

    public BOPid startPidsQuestDialog() {
		int res = JOptionPane.showOptionDialog(
		        ControlMain.getControl().getView(),
					new Object[]{
				        "selektiere Tonspur", 
				        new JScrollPane(getPidList()),
					}, 
					"", 
					0,
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					new String[]{"OK", "Abbrechen"},
					"OK"
				);
		if (res == 0) {
		    return (BOPid)pidList.getSelectedValue();
		}
		return null;
	}
    
    private JList getPidList() {
        if (pidList==null) {
            pidList = new JList(pids.getAPids().toArray());
            pidList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
            pidList.setSelectedIndex(0);
        }
        return pidList;
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
     * @param pidList The pidList to set.
     */
    public void setPidList(JList pidList) {
        this.pidList = pidList;
    }
}
