package presentation.settings;
/*
GuiSettingsTabProxy.java by Geist Alexander 

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
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import service.SerIconManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.settings.ControlSettingsTabProxy;
import control.settings.ControlTabSettings;

public class GuiSettingsTabProxy extends JPanel implements GuiSettingsTab {
    
    private ControlSettingsTabProxy control;
	private JCheckBox cbUseProxy = null;
    private JTextField tfHost;
    private JTextField tfPort;
    private JTextField tfUser;
    private JPasswordField tfPassword;
    private Icon menuIcon;
    
    public GuiSettingsTabProxy(ControlSettingsTabProxy ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}
    
    public void initialize() {
        FormLayout layout = new FormLayout(
				  "pref, 10, pref:grow",  		// columns 
				  "pref, pref, pref, pref, pref "); 			// rows
		PanelBuilder builder = new PanelBuilder(layout,this);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.add(this.getCbUseProxy(),		   		cc.xyw(1, 1, 3));
        builder.addLabel("Host",                        cc.xy(1, 2));
        builder.add(this.getTfHost(),                   cc.xy(3, 2));                   
        builder.addLabel("Port",                        cc.xy(1, 3));
        builder.add(this.getTfPort(),                   cc.xy(3, 3));
        builder.addLabel("Login",                       cc.xy(1, 4));
        builder.add(this.getTfUser(),                   cc.xy(3, 4));
        JLabel label = new JLabel(ControlMain.getProperty("label_password"));
        builder.add(label,                              cc.xy(1, 5));
        builder.add(this.getTfPassword(),               cc.xy(3, 5));
    }


    /**
     * @return Returns the cbUseProxy.
     */
    public JCheckBox getCbUseProxy() {
        if (cbUseProxy == null) {
            cbUseProxy = new JCheckBox(ControlMain.getProperty("label_useProxy"));
            cbUseProxy.setName("useProxy");
            cbUseProxy.addItemListener(control);
		}
        return cbUseProxy;
    }
    /**
     * @return Returns the tfHost.
     */
    public JTextField getTfHost() {
        if (tfHost==null) {
            tfHost=new JTextField();
            tfHost.setName("host");
            tfHost.addKeyListener(control);
        }
        return tfHost;
    }
    /**
     * @return Returns the tfPassword.
     */
    public JPasswordField getTfPassword() {
        if (tfPassword==null) {
            tfPassword=new JPasswordField();
            tfPassword.setName("password");
            tfPassword.addKeyListener(control);
        }
        return tfPassword;
    }
    /**
     * @return Returns the tfPort.
     */
    public JTextField getTfPort() {
        if (tfPort==null) {
            tfPort=new JTextField();
            tfPort.setName("port");
            tfPort.addKeyListener(control);
        }
        return tfPort;
    }
    /**
     * @return Returns the tfUser.
     */
    public JTextField getTfUser() {
        if (tfUser==null) {
            tfUser=new JTextField();
            tfUser.setName("user");
            tfUser.addKeyListener(control);
        }
        return tfUser;
    }
    /**
     * @return Returns the control.
     */
    public ControlTabSettings getControl() {
        return (ControlTabSettings)control;
    }
    /**
     * @param control The control to set.
     */
    public void setControl(ControlSettingsTabProxy control) {
        this.control = control;
    }
    /**
     * @return Returns the menuIcon.
     */
    public Icon getIcon() {
        if (menuIcon==null) {
            menuIcon=SerIconManager.getInstance().getIcon("browser2.png");
        }
        return menuIcon;
    }
    
    public String getMenuText() {
        return "Proxy";
    }
}
