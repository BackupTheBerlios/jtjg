package control.settings;
/*
ControlSettingsTabPlayback.java by Geist Alexander 

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import control.ControlMain;

import model.BOPlaybackOption;
import model.BOSettingsPlayback;
import presentation.GuiMainView;
import presentation.settings.GuiSettingsTabPlayback;
import presentation.settings.GuiTabSettings;
import service.SerAlertDialog;

public class ControlSettingsTabPlayback extends ControlTabSettings implements ActionListener, ItemListener{
    
    GuiTabSettings settingsTab;
    
    public ControlSettingsTabPlayback (GuiTabSettings tabSettings) {
		this.setSettingsTab(tabSettings);
	}
    
    /* (non-Javadoc)
     * @see control.ControlTab#initialize()
     */
    public void run() {
        this.getTab().getCbUseStandardOption().setSelected(this.getSettings().isAlwaysUseStandardPlayback());
        this.initializeAudioSettings();
    }

    public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action == "delete") {
			this.actionRemoveOption();
		}
		if (action == "add") {
			this.actionAddOption();
		}
		if (action == "deletePlayer") {
			this.actionDeletePlayer();
		}
		if (action == "addPlayer") {
			this.openPlayerPathChooser();
		}
		if (action == "askPids") {
			this.getSettings().setAudioOption(0);
		}
		if (action == "useAc3") {
			this.getSettings().setAudioOption(1);
		}
		if (action == "useFirstAudioPid") {
			this.getSettings().setAudioOption(2);
		}
	}
    
    private void initializeAudioSettings() {
		if (this.getSettings().getAudioOption()==0) {
			this.getTab().getRbAskPids().setSelected(true);
		} else if (this.getSettings().getAudioOption()==1) {
			this.getTab().getRbUseAc3().setSelected(true);
		} else if (this.getSettings().getAudioOption()==2) {
			this.getTab().getRbUseFirstAudioPid().setSelected(true);
		}
	}
    
//  Change-Events der der Checkbox
	public void itemStateChanged (ItemEvent event) {
	    JCheckBox checkBox = (JCheckBox)event.getSource();
		while (true) {				
			if (checkBox.getName().equals("useStandard")) {
				this.getSettings().setAlwaysUseStandardPlayback(checkBox.isSelected());
				break;
			}
			break;
		}	
	}
	private void openPlayerPathChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setDialogType(JFileChooser.SAVE_DIALOG);

		fc.setApproveButtonText("Auswählen");
		fc.setApproveButtonToolTipText( "Wiedergabeprogramm auswählen");
		int returnVal = fc.showSaveDialog( null ) ;

		if ( returnVal == JFileChooser.APPROVE_OPTION ) {
			String path = fc.getSelectedFile().toString();
			this.getTab().playerListModel.addElement(path);
		}	
	}
    
    private void actionDeletePlayer() {
    	int index = this.getTab().getJListPlayer().getSelectedIndex();
    	this.getTab().playerListModel.remove(index);
    }
    
    private void actionAddOption() {
    	if (ControlMain.getSettingsPlayback().getPlaybackPlayer().size()>0) {
    		BOPlaybackOption option = new BOPlaybackOption();
    		
    		int selectedPlayer = this.getTab().getJListPlayer().getSelectedIndex();
    		if (selectedPlayer==-1) {
    			this.getTab().getJListPlayer().setSelectedIndex(0);
    		}
    		option.setPlaybackPlayer((String)this.getTab().getJListPlayer().getSelectedValue());
    		this.getTab().getPlaybackSettingsTableModel().addRow(option);	
    	} else {
    		SerAlertDialog.alert("Es muss ein Wiedergabe-Programm angelegt werden!", this.getTab());
    	}
	}

	private void actionRemoveOption() {
		int selectedRow = this.getSettingsTab().getSettingsTabPlayback().getJTablePlaybackSettings().getSelectedRow();
		this.getSettingsTab().getSettingsTabPlayback().getPlaybackSettingsTableModel().removeRow(selectedRow);
	}
 
    public GuiMainView getMainView() {
        return this.getSettingsTab().getControl().getMainView();
    }
    
    /* (non-Javadoc)
     * @see control.ControlTab#getMainView()
     */
    public GuiTabSettings getSettingsTab() {
        return settingsTab;
    }

    /* (non-Javadoc)
     * @see control.ControlTab#setMainView(presentation.GuiMainView)
     */
    public void setSettingsTab(GuiTabSettings tabSettings) {
        settingsTab = tabSettings;
    }
    private BOSettingsPlayback getSettings() {
        return ControlMain.getSettingsPlayback();
    }
    
    private GuiSettingsTabPlayback getTab() {
        return this.getSettingsTab().getSettingsTabPlayback();
    }
}
