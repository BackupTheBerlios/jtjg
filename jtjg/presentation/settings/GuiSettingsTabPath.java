package presentation.settings;
/*
GuiSettingsTabPath.java by Geist Alexander 

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

import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import service.SerIconManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.settings.ControlSettingsTabPath;
import control.settings.ControlTabSettings;

public class GuiSettingsTabPath extends JPanel implements GuiSettingsTab {
    
    private Icon menuIcon;
	private ControlSettingsTabPath control;
	private JTextField jTextFieldRecordSavePath;
	private JTextField jTextFieldUdrecPath;
	private JTextField jTextFieldProjectXPath;
	private JTextField jTextFieldVlcPath;
	private JTextField jTextFieldShutdonwToolPath;
	private JTextField jTextFieldWorkDirectory;
	private JTextField jTextFieldBrowserPath;
	private JTextField jTextFieldMplex;
	
	private JButton jButtonRecordPathFileChooser = null;
	private JButton jButtonBrowserPathFileChooser = null;
	private JButton jButtonWorkDirChooser = null;
	private JButton jButtonUdrecPathFileChooser = null;
	private JButton jButtonProjectXPathFileChooser = null;
	private JButton jButtonVlcPathFileChooser = null;
	private JButton jButtonShutdownToolPathFileChooser = null;
	private JButton jButtonMplex = null;
	
	private SerIconManager iconManager = SerIconManager.getInstance();
       
    public GuiSettingsTabPath(ControlSettingsTabPath ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}
    
    public void initialize() {
        FormLayout layout = new FormLayout(
				  "pref, 10, f:pref:grow, 5, pref",  		// columns 
				  "pref, 10, pref, pref, pref, pref, pref, pref,pref"); 			// rows
				PanelBuilder builder = new PanelBuilder(this, layout);
				builder.setDefaultDialogBorder();
				CellConstraints cc = new CellConstraints();
				
				builder.add(new JLabel(ControlMain.getProperty("label_workDirPath")),		cc.xy	(1, 1));
				builder.add(this.getJTextFieldWorkDirectory(),								cc.xy	(3, 1));
				builder.add(this.getJButtonWorkDirChooser(),								cc.xy	(5, 1));
				builder.add(new JLabel(ControlMain.getProperty("label_recordPath")),		cc.xy	(1, 3));
				builder.add(this.getJTextFieldRecordSavePath(),								cc.xy	(3, 3));
				builder.add(this.getJButtonRecordPathFileChooser(),							cc.xy	(5, 3));
				builder.add(new JLabel(ControlMain.getProperty("label_projectXPath")),		cc.xy	(1, 4));
				builder.add(this.getJTextFieldProjectXPath(),								cc.xy	(3, 4));
				builder.add(this.getJButtonProjectXPathFileChooser(),						cc.xy	(5, 4));
				builder.add(new JLabel(ControlMain.getProperty("label_udrecPath")),			cc.xy	(1, 5));
				builder.add(this.getJTextFieldUdrecPath(),									cc.xy	(3, 5));
				builder.add(this.getJButtonUdrecPathFileChooser(),							cc.xy	(5, 5));
				builder.add(new JLabel(ControlMain.getProperty("label_vlcPath")),			cc.xy	(1, 6));
				builder.add(this.getJTextFieldVlcPath(),									cc.xy	(3, 6));
				builder.add(this.getJButtonVlcPathFileChooser(),							cc.xy	(5, 6));
				builder.add(new JLabel(ControlMain.getProperty("label_shutdownToolPath")),	cc.xy	(1, 7));
				builder.add(this.getJTextFieldShutdonwToolPath(),							cc.xy	(3, 7));
				builder.add(this.getJButtonShutdownToolPathFileChooser(),					cc.xy	(5, 7));
				builder.add(new JLabel(ControlMain.getProperty("label_browserPath")),		cc.xy	(1, 8));
				builder.add(this.getJTextFieldBrowserPath(),								cc.xy	(3, 8));
				builder.add(this.getJButtonBrowserPathFileChooser(),						cc.xy	(5, 8));
				builder.add(new JLabel(ControlMain.getProperty("label_mplex")),		       	cc.xy	(1, 9));
				builder.add(this.getJTextFieldMplex(),						    		cc.xy	(3, 9));
				builder.add(this.getJButtonMplexFileChooser(),						        cc.xy	(5, 9));
    }
	    	
	
	/**
	 * @return Returns the jButtonRecordPathFileChooser.
	 */
	public JButton getJButtonRecordPathFileChooser() {
		if (jButtonRecordPathFileChooser == null) {
			jButtonRecordPathFileChooser = new JButton(iconManager.getIcon("Open16.gif"));
			jButtonRecordPathFileChooser.setActionCommand("recordPath");
			jButtonRecordPathFileChooser.addActionListener(control);
		}
		return jButtonRecordPathFileChooser;
	}
	/**
	 * @return Returns the jButtonUdrecPathFileChooser.
	 */
	public JButton getJButtonUdrecPathFileChooser() {
		if (jButtonUdrecPathFileChooser == null) {
			jButtonUdrecPathFileChooser = new JButton(iconManager.getIcon("Open16.gif"));
			jButtonUdrecPathFileChooser.setActionCommand("udrecPath");
			jButtonUdrecPathFileChooser.addActionListener(control);
		}
		return jButtonUdrecPathFileChooser;
	}
	/**
	 * @return Returns the jButtonProjectXPathFileChooser.
	 */
	public JButton getJButtonProjectXPathFileChooser() {
		if (jButtonProjectXPathFileChooser == null) {
			jButtonProjectXPathFileChooser = new JButton(iconManager.getIcon("Open16.gif"));
			jButtonProjectXPathFileChooser.setActionCommand("projectxPath");
			jButtonProjectXPathFileChooser.addActionListener(control);
		}
		return jButtonProjectXPathFileChooser;
	}
	/**
	 * @return Returns the jTextFieldRecordSavePath.
	 */
	public JTextField getJTextFieldRecordSavePath() {
		if (jTextFieldRecordSavePath == null) {
			jTextFieldRecordSavePath = new JTextField();
			jTextFieldRecordSavePath.setPreferredSize(new Dimension(340, 19));
			jTextFieldRecordSavePath.setEditable(false);
		}
		return jTextFieldRecordSavePath;
	}
	/**
	 * @return Returns the jTextFieldUdrecPath.
	 */
	public JTextField getJTextFieldUdrecPath() {
		if (jTextFieldUdrecPath == null) {
			jTextFieldUdrecPath = new JTextField();
			jTextFieldUdrecPath.addKeyListener(control);
			jTextFieldUdrecPath.setName("udrecPath");
			jTextFieldUdrecPath.setPreferredSize(new Dimension(340, 19));
		}
		return jTextFieldUdrecPath;
	}
	
	/**
	 * @return Returns the jTextFieldProjectXPath.
	 */
	public JTextField getJTextFieldProjectXPath() {
		if (jTextFieldProjectXPath == null) {
			jTextFieldProjectXPath = new JTextField();
			jTextFieldProjectXPath.setPreferredSize(new Dimension(340, 19));
			jTextFieldProjectXPath.setEditable(false);
		}
		return jTextFieldProjectXPath;
	}
	
	/**
	 * @return Returns the jTextFieldVlcPath.
	 */
	public JTextField getJTextFieldVlcPath() {
		if (jTextFieldVlcPath == null) {
		    jTextFieldVlcPath = new JTextField();
		    jTextFieldVlcPath.addKeyListener(control);
		    jTextFieldVlcPath.setName("vlcPath");
		    jTextFieldVlcPath.setPreferredSize(new Dimension(340, 19));
		}
		return jTextFieldVlcPath;
	}
	/**
	 * @return Returns the jButtonVlcPathFileChooser.
	 */
	public JButton getJButtonVlcPathFileChooser() {
		if (jButtonVlcPathFileChooser == null) {
		    jButtonVlcPathFileChooser = new JButton(iconManager.getIcon("Open16.gif"));
		    jButtonVlcPathFileChooser.setActionCommand("vlcPath");
		    jButtonVlcPathFileChooser.addActionListener(control);
		}
		return jButtonVlcPathFileChooser;
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
    public void setControl(ControlSettingsTabPath control) {
        this.control = control;
    }
    /**
     * @return Returns the jButtonShutdownToolPathFileChooser.
     */
    public JButton getJButtonShutdownToolPathFileChooser() {
        if (jButtonShutdownToolPathFileChooser == null) {
            jButtonShutdownToolPathFileChooser = new JButton(iconManager.getIcon("Open16.gif"));
            jButtonShutdownToolPathFileChooser.setActionCommand("shutdownToolPath");
            jButtonShutdownToolPathFileChooser.addActionListener(control);
		}
        return jButtonShutdownToolPathFileChooser;
    }
    /**
     * @return Returns the jTextFieldShutdonwToolPath.
     */
    public JTextField getJTextFieldShutdonwToolPath() {
        if (jTextFieldShutdonwToolPath == null) {
            jTextFieldShutdonwToolPath = new JTextField();
            jTextFieldShutdonwToolPath.addKeyListener(control);
            jTextFieldShutdonwToolPath.setName("shutdownToolPath");
            jTextFieldShutdonwToolPath.setPreferredSize(new Dimension(340, 19));
		}
        return jTextFieldShutdonwToolPath;
    }
    /**
     * @return Returns the jButtonBrowserPathFileChooser.
     */
    public JButton getJButtonBrowserPathFileChooser() {
        if (jButtonBrowserPathFileChooser == null) {
            jButtonBrowserPathFileChooser = new JButton(iconManager.getIcon("Open16.gif"));
            jButtonBrowserPathFileChooser.setActionCommand("browserPath");
            jButtonBrowserPathFileChooser.addActionListener(control);
		}
        return jButtonBrowserPathFileChooser;
    }
    /**
     * @return Returns the jButtonWorkDirChooser.
     */
    public JButton getJButtonWorkDirChooser() {
        if (jButtonWorkDirChooser == null) {
            jButtonWorkDirChooser = new JButton(iconManager.getIcon("Open16.gif"));
            jButtonWorkDirChooser.setActionCommand("workDirPath");
            jButtonWorkDirChooser.addActionListener(control);
		}
        return jButtonWorkDirChooser;
    }
   
    public JButton getJButtonMplexFileChooser() {
        if (jButtonMplex == null) {
        	jButtonMplex = new JButton(iconManager.getIcon("Open16.gif"));
        	jButtonMplex.setActionCommand("mplex");
        	jButtonMplex.addActionListener(control);
		}
        return jButtonMplex;
    }
   
    
    /**
     * @return Returns the jTextFieldBrowserPath.
     */
    public JTextField getJTextFieldBrowserPath() {
        if (jTextFieldBrowserPath == null) {
            jTextFieldBrowserPath = new JTextField();
            jTextFieldBrowserPath.addKeyListener(control);
            jTextFieldBrowserPath.setName("browserPath");
            jTextFieldBrowserPath.setPreferredSize(new Dimension(340, 19));
		}
        return jTextFieldBrowserPath;
    }
    /**
     * @return Returns the jTextFieldWorkDirectory.
     */
    public JTextField getJTextFieldWorkDirectory() {
        if (jTextFieldWorkDirectory == null) {
            jTextFieldWorkDirectory = new JTextField();
            jTextFieldWorkDirectory.addKeyListener(control);
            jTextFieldWorkDirectory.setName("workDirPath");
            jTextFieldWorkDirectory.setEditable(false);
            jTextFieldWorkDirectory.setPreferredSize(new Dimension(340, 19));
		}
        return jTextFieldWorkDirectory;
    }
    
    /**
     * @return Returns the jTextFieldBrowserPath.
     */
    public JTextField getJTextFieldMplex() {
        if (jTextFieldMplex == null) {
        	jTextFieldMplex = new JTextField();
            jTextFieldMplex.addKeyListener(control);
            jTextFieldMplex.setName("mplexPath");
            jTextFieldMplex.setPreferredSize(new Dimension(340, 19));
		}
        return jTextFieldMplex;
    }
    
    
    
    /**
     * @return Returns the menuIcon.
     */
    public Icon getIcon() {
        if (menuIcon==null) {
            menuIcon = SerIconManager.getInstance().getIcon("floppy.png");
        }
        return menuIcon;
    }
    
    public String getMenuText() {
        return ControlMain.getProperty("tab_path");
    }
}
