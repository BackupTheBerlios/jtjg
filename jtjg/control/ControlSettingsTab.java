package control;
/*
ControlSettingsTab.java by Geist Alexander 

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
import model.BOSettings;
import presentation.GuiMainView;
import presentation.settings.GuiSettingsTab;


public class ControlSettingsTab extends ControlTab  {

	GuiMainView mainView;
	BOSettings settings;

	public ControlSettingsTab(GuiMainView view) {
		this.setMainView(view);
	}
	
	public void run() {
	    GuiSettingsTab[] settingTabs = this.getMainView().getTabSettings().getOptionPanels();
        for (int i=0; i<settingTabs.length; i++) {
            new Thread(settingTabs[i].getControl()).start();
        }
    };
	
	/**
	 * @return Returns the mainView.
	 */
	public GuiMainView getMainView() {
		return mainView;
	}
	/**
	 * @param mainView The mainView to set.
	 */
	public void setMainView(GuiMainView mainView) {
		this.mainView = mainView;
	}
}
