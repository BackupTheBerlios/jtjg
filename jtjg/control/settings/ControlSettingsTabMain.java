package control.settings;
/*
ControlSettingsTabMain.java by Alexander Geist

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import model.BOBox;
import model.BOLookAndFeelHolder;
import model.BOSettingsMain;
import presentation.GuiMainView;
import presentation.program.GuiIpListComboModel;
import presentation.settings.GuiSettingsTabMain;
import presentation.settings.GuiTabSettings;
import presentation.settings.GuiThemesComboModel;
import service.SerAlertDialog;
import service.SerExternalProcessHandler;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;


import control.ControlMain;
import control.ControlMainView;

public class ControlSettingsTabMain extends ControlTabSettings implements KeyListener, ActionListener, ItemListener {

	GuiTabSettings			settingsTab;
	public final String[]	localeNames	= {"de,Deutsch", "en,Englisch", "fi,Finisch"};
	public BOLookAndFeelHolder[] lookAndFeels;
	private int currentSelectedLookAndFeel = 0;

	public ControlSettingsTabMain(GuiTabSettings tabSettings) {
		this.setSettingsTab(tabSettings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see control.ControlTab#initialize()
	 */
	public void run() {
		this.getTab().getTfServerPort().setText(ControlMain.getSettingsRecord().getStreamingServerPort());
		this.getTab().getCbStartStreamingServer().setSelected(ControlMain.getSettingsRecord().isStartStreamingServer());
		this.getTab().getJComboBoxTheme().setSelectedItem(this.getSettings().getPlasticTheme());
		this.getTab().getJComboBoxLocale().setSelectedItem(this.getSettings().getLocale());
		this.getTab().getCbStartFullscreen().setSelected(this.getSettings().isStartFullscreen());
		this.getTab().getCbStartVlcAtStart().setSelected(this.getSettings().isStartVlcAtStart());
		this.getTab().getCbShowLogWindow().setSelected(this.getSettings().isShowLogWindow());
		this.getTab().getCbUseSysTray().setSelected(this.getSettings().isUseSysTray());
		this.getTab().getCbStartMinimized().setSelected(this.getSettings().isStartMinimized());
		this.initLookAndFeels();
		this.getTab().getJComboBoxLookAndFeel().setSelectedIndex(currentSelectedLookAndFeel);
	}
	
	public BOLookAndFeelHolder[] initLookAndFeels() {
	    if (lookAndFeels==null) {
			LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
			lookAndFeels = new BOLookAndFeelHolder[looks.length];
	
			String currentSelLFClassName = this.getSettings().getLookAndFeel();
			for (int i = 0; i < looks.length; i++) {
				lookAndFeels[i] = new BOLookAndFeelHolder(looks[i].getName(),looks[i].getClassName());
				if (lookAndFeels[i].getLookAndFeelClassName().equals(currentSelLFClassName)) {
					currentSelectedLookAndFeel = i;
				}
			}
	    }
	    return lookAndFeels;
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		while (true) {
		    if (action == "delete") {
		  			this.actionRemoveBox();
		  		}
		  		if (action == "add") {
		  			this.actionAddBox();
		  		}
		  		if (action == "launchVlc") {
			  		this.actionStartVlc();
			  	}
		  		if (action.equals("showLogWindow")) {
	  				this.getSettings().setShowLogWindow(((JCheckBox)e.getSource()).isSelected());
	  				break;
		  		}
		  		if (action.equals("startFullscreen")) {
		  			this.getSettings().setStartFullscreen(((JCheckBox)e.getSource()).isSelected());
		  			break;
		  		}
		  		if (action.equals("startVlc")) {
		  			this.getSettings().setStartVlcAtStart(((JCheckBox)e.getSource()).isSelected());
		  			break;
		  		}
		  		if (action.equals("useSysTray")) {
		  		    boolean selected = ((JCheckBox)e.getSource()).isSelected();
		  			this.getSettings().setUseSysTray(selected);
		  			this.getMainView().checkTrayMenu(selected); // Damit das Tray sofort geladen, bzw. ausgeblendet wird
		  			break;
		  		}
		  		if (action.equals("startMinimized")) {
	  				this.getSettings().setStartMinimized(((JCheckBox)e.getSource()).isSelected());
	  				break;
		  		}
				if (action.equals("startStreamingServer")) {
					ControlMain.getSettingsRecord().setStartStreamingServer(((JCheckBox) e.getSource()).isSelected());
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
		if (tf.getName().equals("serverPort")) {
			ControlMain.getSettingsRecord().setStreamingServerPort(tf.getText());
		}
	}	
	
	/**
	 * Change-Events der Combos und der Checkbox
	 * 
	 * @version 24.11.2004 12:00
	 */
	public void itemStateChanged(ItemEvent event) {
	    JComboBox comboBox = (JComboBox) event.getSource();
	    while (true) {
	        if (event.getStateChange() == 1) {
	            if (comboBox.getName().equals("theme")) {
	                setTheme((String) comboBox.getSelectedItem());
	                break;
	            }
	            if (comboBox.getName().equals("locale")) {
	                getSettings().setLocale((String) comboBox.getSelectedItem());
	                break;
	            }
	            if (comboBox.getName().equals("lookAndFeel")) {
	                String lookAndFeel = ((BOLookAndFeelHolder) this.getTab().getJComboBoxLookAndFeel().getSelectedItem()).getLookAndFeelClassName();
	                getSettings().setLookAndFeel(lookAndFeel);
	                this.enableThemeComboBox(lookAndFeel);
	                break;
	            }
	        }
	        break;
	    }
	}
	
	private void setTheme(String theme) {
	    if (getSettings().isSkinLookAndFeel()) {
	        getSettings().setSkinLFTheme(theme);
	    } else {
	        getSettings().setPlasticTheme(theme);    
	    }
        if (ControlMain.getControl() != null && ControlMain.getControl().getView() != null) {
            this.getMainView().getControl().setLookAndFeel();
        }
	}
	
	/*
	 * Theme-Auswahl nur fuer Plastic-L&F´s erlauben
	 */
	private void enableThemeComboBox(String lookAndFeel) {
	    if (lookAndFeel.equals(PlasticLookAndFeel.class.getName()) || 
	            lookAndFeel.equals(PlasticXPLookAndFeel.class.getName()) ||
	            lookAndFeel.equals(Plastic3DLookAndFeel.class.getName())) 
	    {
	        this.getTab().getJComboBoxTheme().setModel(new GuiThemesComboModel(this));
	        this.getTab().getJComboBoxTheme().setEnabled(true);
	        this.getTab().getJComboBoxTheme().setSelectedItem(getValidTheme());
	    } else {
	        this.getTab().getJComboBoxTheme().setEnabled(false);
	        this.getMainView().getControl().setLookAndFeel();
	    }
	    
	}
	
	public String[] getValidThemes() {
	    if (getSettings().isSkinLookAndFeel()) {
	        return ControlMainView.skinLFThemes;
	    } 
	    return ControlMainView.themes;
	}
	
	public String getValidTheme() {
	    if (getSettings().isSkinLookAndFeel()) {
	        return ControlMain.getSettingsMain().getSkinLFTheme();
	    }
	    return ControlMain.getSettingsMain().getPlasticTheme();
	}

	private void actionStartVlc() {
	    String[] execString={ControlMain.getSettingsPath().getVlcPath(),"-I", "http"};
	    SerExternalProcessHandler.startProcess("vlc", execString, true, true);
	}
	
	private void actionAddBox() {
		BOBox box = this.startBoxIpDialog();
		if (box!=null) {
			this.getSettingsTab().getSettingsTabMain().getModelBoxTable().addRow(box);
		}
	}
	private void actionRemoveBox() {
		int selectedRow = this.getSettingsTab().getSettingsTabMain().getJTableBoxSettings().getSelectedRow();
		this.getSettingsTab().getSettingsTabMain().getModelBoxTable().removeRow(selectedRow);
	}
	
	public void startWizard() {
		BOBox box = this.startBoxIpDialog();
		if (box!=null) {
			this.getSettingsTab().getSettingsTabMain().getModelBoxTable().addRow(box);
			
		    ControlMain.newBoxSelected((BOBox)ControlMain.getSettingsMain().getBoxList().get(0));
		    ControlMain.getControl().getView().getMainTabPane().tabProgramm=null;
		    ControlMain.getControl().getView().getMainTabPane().reInitTimerPanel();
		}
	}
	
	private BOBox startBoxIpDialog() {
		String ip = JOptionPane.showInputDialog(this.getMainView(), ControlMain.getProperty("msg_enterIp"));
		if (ip!=null) {
            try {
                String validIp=InetAddress.getByName(ip).getHostAddress();
                BOBox box = new BOBox();
                box.setDboxIp(validIp);
                return box;
            } catch (UnknownHostException e) {
                SerAlertDialog.alert(ControlMain.getProperty("err_valid_ip"), this.getMainView());
            }
		}
		return null;
	}
	
	public void refreshIpComboBox() {
		getMainView().getTabProgramm().getJComboBoxBoxIP().setModel(new GuiIpListComboModel());
		int index = ControlMain.getIndexOfActiveBox();
		getMainView().getTabProgramm().getJComboBoxBoxIP().setSelectedIndex(index);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see control.ControlTab#getMainView()
	 */
	public GuiTabSettings getSettingsTab() {
		return settingsTab;
	}
	public GuiMainView getMainView() {
		return this.getSettingsTab().getControl().getMainView();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see control.ControlTab#setMainView(presentation.GuiMainView)
	 */
	public void setSettingsTab(GuiTabSettings tabSettings) {
		settingsTab = tabSettings;

	}
	private BOSettingsMain getSettings() {
		return ControlMain.getSettings().getMainSettings();
	}

	private GuiSettingsTabMain getTab() {
		return this.getSettingsTab().getSettingsTabMain();
	}
}