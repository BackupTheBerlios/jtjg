package model;
/*
BOPlaybackOption.java by Geist Alexander 

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

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import service.SerAlertDialog;
import control.ControlMain;

public class BOPlaybackOption {

    public String name = "vlc";
    public String playbackPlayer; 
    public String playbackOption = "http://$ip:31339/0,$pmt,$vPid,$aPid";
	public boolean standard = false;
	public boolean logOutput = true;

	public int getModelPlayerIndex() {
		ArrayList playerList = ControlMain.getSettingsPlayback().getPlaybackPlayer();
		for (int i=0; i<playerList.size(); i++) {
			String player = (String)playerList.get(i);
			if (player.equals(this.getPlaybackPlayer())) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * @return Returns the playbackOption.
	 */
	public String getPlaybackOption() {
		if (playbackOption == null) {
			return "";
		}
		return playbackOption;
	}
	/**
	 * @param playbackOption
	 *            The playbackOption to set.
	 */
	public void setPlaybackOption(String execString) {
			this.playbackOption = execString;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		if (name == null) {
			return "";
		}
		return name;
	}
	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		if (this.name == null || !this.name.equals(name)) {
			this.name = name;
			this.setSettingsChanged(true);
		}
	}
	/**
	 * @return Returns the standard.
	 */
	public boolean isStandard() {
		return standard;
	}
	/**
	 * @param standard
	 *            The standard to set.
	 */
	public void setStandard(boolean standard) {
		if (this.standard != standard) {
			this.standard = standard;
			this.setSettingsChanged(true);
		}
	}

	/**
	 * @param settingsChanged
	 *            The settingsChanged to set.
	 */
	public void setSettingsChanged(boolean settingsChanged) {
		if (ControlMain.getSettings() != null) {
			ControlMain.getSettings().setSettingsChanged(settingsChanged);
		}
	}
	/**
	 * @param printOutput
	 *            The printOutput to set.
	 */
	public void setLogOutput(boolean printOutput) {
		if (this.logOutput != printOutput) {
			this.logOutput = printOutput;
			this.setSettingsChanged(true);
		}
	}
	/**
	 * @return Returns the printOutput.
	 */
	public boolean isLogOutput() {
		return logOutput;
	}

	/*
	 * Rückgabe der Stardard-Playbackoption oder Start eines Abfragedialogs
	 */
	public static BOPlaybackOption detectPlaybackOption() {
		if (getPlaybackSettings().getPlaybackOptions() != null && getPlaybackSettings().getPlaybackOptions().size() > 0) {
            if (getPlaybackSettings().getPlaybackOptions().size() == 1) {
                return (BOPlaybackOption)getPlaybackSettings().getPlaybackOptions().get(0);
            }
            BOPlaybackOption option;
			if (getPlaybackSettings().isAlwaysUseStandardPlayback()) {
				option = getPlaybackSettings().getStandardPlaybackOption();
			} else {
				option = startPlaybackOptionsQuestDialog();
			}
			return option;
		}
		SerAlertDialog.alert(ControlMain.getProperty("msg_playbackOptionError"), ControlMain.getControl().getView());
		return null;
	}

	private static BOPlaybackOption startPlaybackOptionsQuestDialog() {
		ArrayList options = getPlaybackSettings().getPlaybackOptions();
		JList list = new JList(options.toArray());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		BOPlaybackOption def = getPlaybackSettings().getStandardPlaybackOption();
		if (def != null) {
			list.setSelectedValue(def, true);
		}
		int ret = JOptionPane.showConfirmDialog(ControlMain.getControl().getView(), new Object[]{
				ControlMain.getProperty("msg_choosePlayback2"), new JScrollPane(list)}, ControlMain.getProperty("msg_choose"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (ret == JOptionPane.OK_OPTION) {
			BOPlaybackOption opt = (BOPlaybackOption) list.getSelectedValue();
			return opt;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}

	private static BOSettingsPlayback getPlaybackSettings() {
		return ControlMain.getSettings().getPlaybackSettings();
	}

	/**
	 * @return Returns the playbackPlayer.
	 */
	public String getPlaybackPlayer() {
		return playbackPlayer;
	}
	/**
	 * @param playbackPlayer The playbackPlayer to set.
	 */
	public void setPlaybackPlayer(String playbackPlayer) {
		this.playbackPlayer = playbackPlayer;
	}
}