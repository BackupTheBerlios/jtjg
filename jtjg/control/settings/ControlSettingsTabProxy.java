package control.settings;
/*
ControlSettingsTabProxy.java by Geist Alexander 

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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import control.ControlMain;

import model.BOSettingsProxy;
import presentation.GuiMainView;
import presentation.settings.GuiSettingsTabProxy;
import presentation.settings.GuiTabSettings;

public class ControlSettingsTabProxy extends ControlTabSettings implements KeyListener, ItemListener{
    
    GuiTabSettings settingsTab;
    
    public ControlSettingsTabProxy (GuiTabSettings tabSettings) {
		this.setSettingsTab(tabSettings);
	}
    
    /* (non-Javadoc)
     * @see control.ControlTab#initialize()
     */
    public void run() {
        this.getTab().getCbUseProxy().setSelected(this.getSettings().isUse());
        this.getTab().getTfHost().setText(this.getSettings().getHost());
        this.getTab().getTfPort().setText(this.getSettings().getPort());
    }
    
//  Change-Events der der Checkbox
	public void itemStateChanged (ItemEvent event) {
	    JCheckBox checkBox = (JCheckBox)event.getSource();
		while (true) {				
			if (checkBox.getName().equals("useProxy")) {
				this.getSettings().setUse(checkBox.isSelected());
				break;
			}
			break;
		}	
	}
    
    public void keyTyped(KeyEvent event) {
    }

    public void keyPressed(KeyEvent event) {
    }

    public void keyReleased(KeyEvent event) {
        JTextField tf = (JTextField) event.getSource();
        while (true) {
            if (tf.getName().equals("host")) {
                this.getSettings().setHost(tf.getText());
                break;
            }
            if (tf.getName().equals("user")) {
                String pw = new String(this.getTab().getTfPassword().getPassword());
                this.getSettings().setUserpass(tf.getText(), pw);
                break;
            }
            if (tf.getName().equals("password")) {
                String pw = new String(((JPasswordField)tf).getPassword());
                this.getSettings().setUserpass(tf.getText(), pw);
                break;
            }
            if (tf.getName().equals("port")) {
                this.getSettings().setPort(tf.getText());
                break;
            }
            break;
        }
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
    private BOSettingsProxy getSettings() {
        return ControlMain.getSettingsProxy();
    }
    
    private GuiSettingsTabProxy getTab() {
        return this.getSettingsTab().getSettingsTabProxy();
    }
}
