package presentation.settings;
/*
GuiTabSettings.java by Geist Alexander 

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
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import presentation.GuiTab;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.settings.ControlSettingsTab;
import control.settings.ControlSettingsTabMain;
import control.settings.ControlSettingsTabMovieGuide;
import control.settings.ControlSettingsTabPath;
import control.settings.ControlSettingsTabPlayback;
import control.settings.ControlSettingsTabProxy;
import control.settings.ControlSettingsTabRecord;


public class GuiTabSettings extends GuiTab {

	private ControlSettingsTab control;
    private GuiSettingsTab[] optionPanels;
    private CardLayout registerLayout = new CardLayout();
    private JPanel registerPanel = new JPanel(registerLayout);
    private JList menuList;

	public GuiTabSettings(ControlSettingsTab ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}

	protected void initialize() {
        optionPanels = new GuiSettingsTab[6];
        ControlSettingsTabMain control = new ControlSettingsTabMain(this);
        optionPanels[0] = new GuiSettingsTabMain(control);
        
        ControlSettingsTabRecord control1 = new ControlSettingsTabRecord(this);
        optionPanels[1] = new GuiSettingsTabRecord(control1);
        
        ControlSettingsTabPlayback control2 = new ControlSettingsTabPlayback(this);
        optionPanels[2] = new GuiSettingsTabPlayback(control2);
        
        ControlSettingsTabMovieGuide control3 = new ControlSettingsTabMovieGuide(this);
        optionPanels[3] = new GuiSettingsTabMovieGuide(control3);
        
        ControlSettingsTabPath control4 = new ControlSettingsTabPath(this);
        optionPanels[4] = new GuiSettingsTabPath(control4);
        
        ControlSettingsTabProxy control5 = new ControlSettingsTabProxy(this);
        optionPanels[5] = new GuiSettingsTabProxy(control5);
        
        menuList = new JList(optionPanels);
        menuList.setBackground((Color)UIManager.get("Panel.background"));
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setCellRenderer(new GuiMenuListCellRenderer());
        for (int i = 0; i < optionPanels.length; i++) {
            registerPanel.add(optionPanels[i].getMenuText(),
                              (JPanel) optionPanels[i]);
        }
        menuList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                Object selected = menuList.getSelectedValue();
                registerLayout.show(registerPanel,
                                    ( (GuiSettingsTab) selected).getMenuText());
            }
        });
        menuList.setSelectedValue(optionPanels[0], true);
          
		FormLayout layout = new FormLayout( "f:pref, f:pref:grow", "f:pref:grow");
		PanelBuilder builder = new PanelBuilder(layout,this);
		CellConstraints cc = new CellConstraints();
		
        builder.add(menuList,                   cc.xy(1,1));
		builder.add(registerPanel,		   		cc.xy(2,1));
	}
    
    public GuiSettingsTabMain getSettingsTabMain() {
        return (GuiSettingsTabMain)optionPanels[0];
    }
    public GuiSettingsTabRecord getSettingsTabRecord() {
        return (GuiSettingsTabRecord)optionPanels[1];
    }
    public GuiSettingsTabPlayback getSettingsTabPlayback() {
        return (GuiSettingsTabPlayback)optionPanels[2];
    }
    public GuiSettingsTabMovieGuide getSettingsTabMovieGuide() {
        return (GuiSettingsTabMovieGuide)optionPanels[3];
    }
    public GuiSettingsTabPath getSettingsTabPath() {
        return (GuiSettingsTabPath)optionPanels[4];
    }
    public GuiSettingsTabProxy getSettingsTabProxy() {
        return (GuiSettingsTabProxy)optionPanels[5];
    }
	/**
	 * @return Returns the control.
	 */
	public ControlSettingsTab getControl() {
		return control;
	}
	/**
	 * @param control The control to set.
	 */
	public void setControl(ControlSettingsTab control) {
		this.control = control;
	}
    /**
     * @return Returns the optionPanels.
     */
    public GuiSettingsTab[] getOptionPanels() {
        return optionPanels;
    }
}
