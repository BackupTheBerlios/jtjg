/*
 * GuiSettingsTabMain.java by Geist Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 *  
 */
package presentation.settings;

import java.awt.Dimension;
import java.text.ParseException;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;

import org.apache.log4j.Logger;

import presentation.program.GuiBoxSettingsTableCellRenderer;
import service.SerIconManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.settings.ControlSettingsTabMain;
import control.settings.ControlTabSettings;

public class GuiSettingsTabMain extends JPanel implements GuiSettingsTab {

    private Icon menuIcon;
	private ControlSettingsTabMain control;
	private JPanel panelBoxSettings = null;
	private JPanel panelLayoutSettings = null;
	private JPanel panelStartOptions = null;
	private JPanel panelServerRecordSettings = null;
	private JButton jButtonAnlegen = null;
	private JButton jButtonLoeschen = null;
	private JButton jButtonStartVlc = null;
	private JFormattedTextField tfBoxIp = null;
	private JFormattedTextField tfServerPort = null;	
	private JComboBox jComboBoxTheme = null;
	private JComboBox jComboBoxLookAndFeel = null;
	private JComboBox jComboBoxLocale = null;
	private JScrollPane jScrollPaneBoxSettings = null;
	private JTable jTableBoxSettings = null;
	private GuiBoxSettingsTableModel modelBoxTable;
	private JCheckBox cbStartFullscreen;
	private JCheckBox cbShowLogWindow;
	private JCheckBox cbUseSysTray;
	private JCheckBox cbStartVlcAtStart;
	private JCheckBox cbStartMinimized;
	private JCheckBox cbStartStreamingServer;
	private SerIconManager iconManager = SerIconManager.getInstance();

	public GuiSettingsTabMain(ControlSettingsTabMain ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}

	public void initialize() {

		FormLayout layout = new FormLayout("f:pref:grow, 100", // columns
				"f:pref, 10, pref, 10, pref, 20, pref"); // rows
		PanelBuilder builder = new PanelBuilder(this, layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.add(this.getPanelBoxSettings(), cc.xy(1, 1));
		builder.add(this.getPanelLayoutSettings(), cc.xy(1, 3));
		builder.add(this.getPanelStartOptions(), cc.xy(1, 5));
		builder.add(this.getPanelServerRecordSettings(), cc.xy(1, 7));
	}

	private JPanel getPanelBoxSettings() {
		if (panelBoxSettings == null) {
			panelBoxSettings = new JPanel();
			FormLayout layout = new FormLayout("pref:grow, 5, pref", //columna
					"pref:grow, pref:grow, pref:grow, 40:grow"); //rows
			PanelBuilder builder = new PanelBuilder(panelBoxSettings, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_networkSettings"), cc.xywh(1, 1, 3, 1));
			builder.add(this.getJScrollPaneBoxSettings(), cc.xywh(1, 2, 1, 3));
			builder.add(this.getJButtonAnlegen(), cc.xy(3, 2));
			builder.add(this.getJButtonLoeschen(), cc.xy(3, 3));
		}
		return panelBoxSettings;
	}
	
	private JPanel getPanelLayoutSettings() {
		if (panelLayoutSettings == null) {
			panelLayoutSettings = new JPanel();
			FormLayout layout = new FormLayout("f:300, 10, pref", //columna
					"pref, pref, pref, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelLayoutSettings, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_guiSettings"),			cc.xywh(1, 1, 3, 1));
			builder.add(new JLabel(ControlMain.getProperty("label_lookandfeel")),	cc.xy(1, 2));
			builder.add(this.getJComboBoxLookAndFeel(), 																	cc.xy(3, 2));
			builder.add(new JLabel(ControlMain.getProperty("label_theme")), 				cc.xy(1, 3));
			builder.add(this.getJComboBoxTheme(), 																					cc.xy(3, 3));
			builder.add(new JLabel(ControlMain.getProperty("label_lang")),						cc.xy(1, 4));
			builder.add(this.getJComboBoxLocale(), 																					cc.xy(3, 4));
		}
		return panelLayoutSettings;
	}
	
	private JPanel getPanelStartOptions() {
		if (panelStartOptions == null) {
		    panelStartOptions = new JPanel();
			FormLayout layout = new FormLayout(  "pref, 10,  pref, pref:grow", //columna
					"pref, 5, pref, pref, pref, pref, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelStartOptions, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_startOptions"), 	cc.xyw(1, 1, 3));
			builder.add(this.getCbStartFullscreen(), 								cc.xyw(1, 3, 3));
			builder.add(this.getCbStartMinimized(), 								cc.xyw(1, 4, 1));

			builder.add(this.getCbShowLogWindow(),									cc.xyw(1, 5, 3));
			builder.add(this.getCbUseSysTray(), 									cc.xyw(1, 6, 4));
			builder.add(this.getCbStartVlcAtStart(), 								cc.xyw(1, 7, 1));
			builder.add(this.getJButtonStartVlc(),									cc.xy	(3, 7));
		}
		return panelStartOptions;
	}
	
	private JPanel getPanelServerRecordSettings() {
		if (panelServerRecordSettings == null) {
			panelServerRecordSettings = new JPanel();
			FormLayout layout = new FormLayout("pref, 15, pref, 5, pref", //columns
					"pref, pref, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelServerRecordSettings, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_serverRecordSettings"), cc.xyw(1, 1, 5));
			builder.add(this.getCbStartStreamingServer(), cc.xy(1, 2));
			builder.add(new JLabel(ControlMain.getProperty("label_serverPort")), cc.xy(3, 2));
			builder.add(this.getTfServerPort(), cc.xy(5, 2));
		}
		return panelServerRecordSettings;
	}
	/**
	 * This method initializes jComboBoxLocale
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getJComboBoxLocale() {
		if (jComboBoxLocale == null) {
			jComboBoxLocale = new JComboBox(control.localeNames);
			jComboBoxLocale.addItemListener(control);
			jComboBoxLocale.setName("locale");
		}
		return jComboBoxLocale;
	}

	/**
	 * This method initializes jTableBoxSettings
	 * 
	 * @return javax.swing.JTable
	 */
	public JTable getJTableBoxSettings() {
		if (jTableBoxSettings == null) {
			modelBoxTable = new GuiBoxSettingsTableModel(control);
			jTableBoxSettings = new JTable(modelBoxTable);
			jTableBoxSettings.setName("BoxSettings");
			jTableBoxSettings.getColumnModel().getColumn(0).setPreferredWidth(80);
			jTableBoxSettings.getColumnModel().getColumn(1).setPreferredWidth(80);
			jTableBoxSettings.getColumnModel().getColumn(2).setPreferredWidth(80);
			jTableBoxSettings.getColumnModel().getColumn(3).setPreferredWidth(40);
			jTableBoxSettings.getColumnModel().getColumn(3).setCellRenderer(new GuiBoxSettingsTableCellRenderer());
			TableColumn columnIp = jTableBoxSettings.getColumnModel().getColumn(0);
			columnIp.setCellEditor(new DefaultCellEditor(this.getTfBoxIp()));
		}
		return jTableBoxSettings;
	}

	/**
	 * This method initializes jComboBoxLocale
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getJComboBoxTheme() {
		if (jComboBoxTheme == null) {
			jComboBoxTheme = new JComboBox();
			jComboBoxTheme.addItemListener(control);
			jComboBoxTheme.setName("theme");
		}
		return jComboBoxTheme;
	}

	/**
	 * This method initializes jComboBoxLocale
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getJComboBoxLookAndFeel() {
		if (jComboBoxLookAndFeel == null) {
			jComboBoxLookAndFeel = new JComboBox(control.initLookAndFeels());
			jComboBoxLookAndFeel.addItemListener(control);
			jComboBoxLookAndFeel.setName("lookAndFeel");
		}
		return jComboBoxLookAndFeel;
	}

	public JFormattedTextField getTfBoxIp() {
		if (tfBoxIp == null) {
				tfBoxIp = new JFormattedTextField();
		}
		return tfBoxIp;
	}

	/**
	 * This method initializes jScrollPaneBoxSettings
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneBoxSettings() {
		if (jScrollPaneBoxSettings == null) {
			jScrollPaneBoxSettings = new JScrollPane();
			jScrollPaneBoxSettings.setPreferredSize(new Dimension(350, 100));
			jScrollPaneBoxSettings.setViewportView(getJTableBoxSettings());
		}
		return jScrollPaneBoxSettings;
	}

	/**
	 * This method initializes jButtonAnlegen
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonAnlegen() {
		if (jButtonAnlegen == null) {
			jButtonAnlegen = new JButton();
			jButtonAnlegen.setIcon(iconManager.getIcon("new.png"));
			jButtonAnlegen.setText(ControlMain.getProperty("button_create"));
			jButtonAnlegen.setActionCommand("add");
			jButtonAnlegen.addActionListener(control);
			jButtonAnlegen.setPreferredSize(new java.awt.Dimension(100, 25));
		}
		return jButtonAnlegen;
	}
	
	
	/**
	 * This method initializes jButtonStartVlc
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonStartVlc() {
		if (jButtonStartVlc == null) {
		    jButtonStartVlc = new JButton();
		    jButtonStartVlc.setIcon(iconManager.getIcon("vlc16x16.gif"));
		    jButtonStartVlc.setActionCommand("launchVlc");
		    jButtonStartVlc.addActionListener(control);
		}
		return jButtonStartVlc;
	}
	
	/**
	 * This method initializes getJButtonLoeschen
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonLoeschen() {
		if (jButtonLoeschen == null) {
			jButtonLoeschen = new JButton();
			jButtonLoeschen.setIcon(iconManager.getIcon("trash.png"));
			jButtonLoeschen.setText(ControlMain.getProperty("button_delete"));
			jButtonLoeschen.setActionCommand("delete");
			jButtonLoeschen.addActionListener(control);
			jButtonLoeschen.setPreferredSize(new java.awt.Dimension(90, 25));
		}
		return jButtonLoeschen;
	}

	/**
	 * @return Returns the cbStartFullscreen.
	 */
	public JCheckBox getCbStartFullscreen() {
		if (cbStartFullscreen == null) {
			cbStartFullscreen = new JCheckBox(ControlMain.getProperty("cbFullscreen"));
			cbStartFullscreen.setActionCommand("startFullscreen");
			cbStartFullscreen.addActionListener(control);
		}
		return cbStartFullscreen;
	}
	
	/**
	 * @return Returns the cbStartVlcAtStart.
	 */
	public JCheckBox getCbStartVlcAtStart() {
		if (cbStartVlcAtStart == null) {
		    cbStartVlcAtStart = new JCheckBox(ControlMain.getProperty("cbVlcStart"));
		    cbStartVlcAtStart.setActionCommand("startVlc");
		    cbStartVlcAtStart.addActionListener(control);
		}
		return cbStartVlcAtStart;
	}
	
	/**
	 * @return Returns the cbStartVlcAtStart.
	 */
	public JCheckBox getCbStartMinimized() {
		if (cbStartMinimized == null) {
			cbStartMinimized = new JCheckBox(ControlMain.getProperty("cbStartMinimized"));
			cbStartMinimized.setActionCommand("startMinimized");
			cbStartMinimized.addActionListener(control);
		}
		return cbStartMinimized;
	}

	/**
	 * @return Returns the cbShowLogWindow.
	 */
	public JCheckBox getCbShowLogWindow() {
		if (cbShowLogWindow == null) {
		    cbShowLogWindow = new JCheckBox(ControlMain.getProperty("cbShowLogWindow"));
		    cbShowLogWindow.setActionCommand("showLogWindow");
		    cbShowLogWindow.addActionListener(control);
		}
		return cbShowLogWindow;
	}
	/**
	 * @return Returns the cbUseSysTray.
	 */
	public JCheckBox getCbUseSysTray() {
		if (cbUseSysTray == null) {
			cbUseSysTray = new JCheckBox(ControlMain.getProperty("cbUseSystray"));
			cbUseSysTray.setActionCommand("useSysTray");
			cbUseSysTray.addActionListener(control);
		}
		return cbUseSysTray;
	}
	
	/**
	 * @return Returns the tfServerPort.
	 */
	public JFormattedTextField getTfServerPort() {
		if (tfServerPort == null) {
			try {
				tfServerPort = new JFormattedTextField(new MaskFormatter("####"));
				tfServerPort.setName("serverPort");
				tfServerPort.addKeyListener(control);
				((MaskFormatter) tfServerPort.getFormatter()).setAllowsInvalid(false);
				((MaskFormatter) tfServerPort.getFormatter()).setOverwriteMode(true);
				tfServerPort.setPreferredSize(new java.awt.Dimension(40, 19));
			} catch (ParseException e) {
			    Logger.getLogger("GuiSettingsTabRecord").error(e.getMessage());
			}
		}
		return tfServerPort;
	}

	/**
	 * @return Returns the cbStartStreamingServer.
	 */
	public JCheckBox getCbStartStreamingServer() {
		if (cbStartStreamingServer == null) {
			cbStartStreamingServer = new JCheckBox(ControlMain.getProperty("cbStartServer"));
			cbStartStreamingServer.setActionCommand("startStreamingServer");
			cbStartStreamingServer.addActionListener(control);
		}
		return cbStartStreamingServer;
	}

	/**
	 * @return Returns the control.
	 */
	public ControlTabSettings getControl() {
		return (ControlTabSettings)control;
	}
	/**
	 * @param control
	 *            The control to set.
	 */
	public void setControl(ControlSettingsTabMain control) {
		this.control = control;
	}
	/**
	 * @return Returns the modelBoxTable.
	 */
	public GuiBoxSettingsTableModel getModelBoxTable() {
		return modelBoxTable;
	}
	/**
	 * @param modelBoxTable
	 *            The modelBoxTable to set.
	 */
	public void setModelBoxTable(GuiBoxSettingsTableModel modelBoxTable) {
		this.modelBoxTable = modelBoxTable;
	}
    /**
     * @return Returns the menuIcon.
     */
    public Icon getIcon() {
        if (menuIcon==null) {
            menuIcon=SerIconManager.getInstance().getIcon("configure.png");
        }
        return menuIcon;
    }
    
    public String getMenuText() {
        return ControlMain.getProperty("label_general");
    }
}