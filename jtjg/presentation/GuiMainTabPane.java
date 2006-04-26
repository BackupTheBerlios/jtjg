package presentation;
/*
GuiMainTabPane.java by Geist Alexander 

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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import presentation.about.GuiTabAbout;
import presentation.movieguide.GuiTabMovieGuide;
import presentation.program.GuiTabProgramm;
import presentation.recordInfo.*;
import presentation.settings.GuiTabSettings;
import presentation.start.GuiTabStart;
import presentation.timer.GuiBoxTimerPanel;
import control.*;
import control.about.ControlAboutTab;
import control.record.ControlRecordEditTab;
import control.record.ControlRecordInfoTab;
import control.settings.ControlSettingsTab;
import control.timer.ControlTimerTab;

public class GuiMainTabPane extends JTabbedPane {

	public GuiTabProgramm tabProgramm = null;
	public GuiTabSettings tabSettings = null;
	public GuiTabAbout tabAbout = null;
	public GuiBoxTimerPanel tabTimer=null;
	public GuiTabStart tabStart=null;
	public GuiMainView view;
	public GuiTabMovieGuide tabMovieGuide = null;
	private GuiTabRecordInfo tabRecordInfo;
	private GuiTabRecordEdit tabRecordEdit;
	int index;
	
	public GuiMainTabPane(GuiMainView mainView) {
		super();
		this.setView(mainView);
	}

	
	/**
	 * Aufbau der Tabs "Programm"
	 * Initialisierung des Controls passiert extern, da dieser Tab 
	 * automatisch beim Aufbau der Gui angewählt wird
	 */    
	public GuiTabProgramm getTabProgramm() {
		if (tabProgramm == null) {
		    ControlProgramTab control = new ControlProgramTab(this.getView());
			tabProgramm = new GuiTabProgramm(control);
			new Thread(control).start();
		}
		return tabProgramm;
	}
	/**
	 * Image-spezifische Timer-Gui		
	 */    
	public GuiBoxTimerPanel getTabTimer() {
		if (tabTimer == null && ControlMain.getBoxAccess() != null) {
            ControlTimerTab control = new ControlTimerTab(view);
            tabTimer = new GuiBoxTimerPanel(control);
		}
		return tabTimer;
	}
	
	public void reInitTimerPanel() {
		this.tabTimer=null;
		JPanel panel = (JPanel)this.getComponent(2);
		panel.removeAll();
	}
	
	/**
	 * Aufbau des Tabs Settings		
	 */    
	public GuiTabSettings getTabSettings() {
		if (tabSettings == null) {
			ControlSettingsTab control = new ControlSettingsTab(this.getView());
			tabSettings = new GuiTabSettings(control);
			new Thread(control).start();
		}
		return tabSettings;
	}
	
	/**
	 * @return Returns the tabAbout.
	 */
	public GuiTabAbout getTabAbout() {
		if (tabAbout == null) {
			ControlAboutTab control = new ControlAboutTab(this.getView());
			tabAbout = new GuiTabAbout(control);
			new Thread(control).start();
		}
		return tabAbout;
	}
	
	public GuiTabMovieGuide getTabMovieGuide() {
		if (tabMovieGuide == null) {
			ControlMovieGuideTab control = new ControlMovieGuideTab(this.getView());
			tabMovieGuide = new GuiTabMovieGuide(control);
			new Thread(control).start();
		}
		return tabMovieGuide;
	}
	
	/**
	 * @return Returns the tabRecordInfo.
	 */
	public GuiTabRecordInfo getTabRecordInfo() {
		if (tabRecordInfo == null) {
			ControlRecordInfoTab control = new ControlRecordInfoTab(this.getView());
			tabRecordInfo = new GuiTabRecordInfo(control);
			control.setRecordView(tabRecordInfo);
		}
		return tabRecordInfo;
	}

	/**
	 * @return Returns the tabRecordInfo.
	 */
	public GuiTabRecordEdit getTabRecordEdit() {
		if (tabRecordEdit == null) {
			ControlRecordEditTab control = new ControlRecordEditTab(this.getView());
			tabRecordEdit = new GuiTabRecordEdit(control);
			control.setRecordView(tabRecordEdit);
		}
		return tabRecordEdit;
	}

	
	/**
     * @return Returns the tabStart.
     */
    public GuiTabStart getTabStart() {
        if (tabStart == null) {
			ControlStartTab control = new ControlStartTab(this.getView());
			tabStart = new GuiTabStart(control);
		}
        return tabStart;
    }
	
	/**
	 * @return Returns the tabSettings.
	 */
	public GuiMainView getView() {
		return view;
	}
	/**
	 * @param tabSettings The tabSettings to set.
	 */
	public void setView(GuiMainView view) {
		this.view = view;
	}
	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}
}
