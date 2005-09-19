package presentation;
/*
 * GuiMainView.java by Geist Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 */
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import presentation.about.GuiTabAbout;
import presentation.movieguide.GuiTabMovieGuide;
import presentation.program.GuiTabProgramm;
import presentation.recordInfo.GuiTabRecordInfo;
import presentation.settings.GuiTabSettings;
import presentation.start.GuiTabStart;
import presentation.timer.GuiBoxTimerPanel;
import service.SerIconManager;
import snoozesoft.systray4j.SysTrayMenu;
import snoozesoft.systray4j.SysTrayMenuIcon;
import snoozesoft.systray4j.SysTrayMenuItem;
import control.ControlMain;
import control.ControlMainView;
/*
 * Haupt-Gui, hier werden die einzelnen Tabs verwaltet
 */
public class GuiMainView extends JFrame {

	private GuiMainTabPane mainTabPane = null;
	private ControlMainView control;
	private SysTrayMenuIcon[] sysTrayIcon = {
	        new SysTrayMenuIcon(ClassLoader.getSystemResource("ico/grabber1.ico")), 
	        new SysTrayMenuIcon(ClassLoader.getSystemResource("ico/grabber2.ico"))
	};
	private SysTrayMenu menu;
	private SerIconManager iconManager = SerIconManager.getInstance();

	public GuiMainView(ControlMainView ctrl) {
		super("FormLayout");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ControlMain.endProgram();
			}
			public void windowIconified(WindowEvent e) {
				if (ControlMain.getSettingsMain().useSysTray) {
					ControlMain.getControl().getView().setVisible(false);
				}
			}
		});

		control = ctrl;
		initialize();
		setTitle(ControlMain.version[0] + "/" + ControlMain.version[1] + " " + ControlMain.version[2]);
		pack();
		checkSettings();
		registerKeys();

		addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) {
				ControlMain.getSettingsLayout().setLocation(GuiMainView.this.getLocation());
			}

			public void componentResized(ComponentEvent e) {
				ControlMain.getSettingsLayout().setSize(GuiMainView.this.getSize());

			}
		});
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.getContentPane().add(this.getMainTabPane());
		setIconImage(iconManager.getIcon("grabber1.gif").getImage());
		if (ControlMain.getSettingsMain().isUseSysTray()) {
			createMenu();
		}
	}
	
	private void registerKeys() {
	    final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK, true);
		getRootPane().registerKeyboardAction(control, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}
	
	private void checkSettings() {
	    if (ControlMain.getSettingsMain().isStartFullscreen()) {
			GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			device.setFullScreenWindow(this);
		}

		Point loc = ControlMain.getSettingsLayout().getLocation();
		if (loc != null) {
			setLocation(loc);
		}
		Dimension dim = ControlMain.getSettingsLayout().getSize();
		if (dim != null) {
			setSize(dim);
			super.setVisible(true);
		}
	}

	public void repaintGui() {
		dispose();
		SwingUtilities.updateComponentTreeUI(GuiMainView.this);
		setVisible(true);
	}

	/**
	 * Haupt-TabPane. Neue Tabs werden hier angemeldet.
	 */
	public GuiMainTabPane getMainTabPane() {
		if (mainTabPane == null) {
			mainTabPane = new GuiMainTabPane(this);
			mainTabPane.addChangeListener(control);

			mainTabPane.addTab("Start", iconManager.getIcon("gohome_2.png"), new JPanel());
			ControlMain.splash.setProgress(20, ControlMain.getProperty("progress_start"));
			mainTabPane.addTab(ControlMain.getProperty("tab_program"), iconManager.getIcon("main.png"), mainTabPane.getTabProgramm());
			ControlMain.splash.setProgress(30, ControlMain.getProperty("progress_programm"));
			mainTabPane.addTab(ControlMain.getProperty("tab_timerlist"), iconManager.getIcon("clock.png"), mainTabPane.getTabTimer());
			ControlMain.splash.setProgress(40, ControlMain.getProperty("progress_timer"));
			mainTabPane.addTab(ControlMain.getProperty("tab_movieGuide"), iconManager.getIcon("download.png"), mainTabPane.getTabMovieGuide());
			ControlMain.splash.setProgress(60, ControlMain.getProperty("progress_movieguide"));
			mainTabPane.addTab(ControlMain.getProperty("tab_recordInfo"), iconManager.getIcon("help.png"), mainTabPane.getTabRecordInfo());
			ControlMain.splash.setProgress(70, ControlMain.getProperty("progress_aufnahmeinfo"));
			mainTabPane.addTab(ControlMain.getProperty("tab_recordEdit"), iconManager.getIcon("recordEdit.png"), mainTabPane.getTabRecordEdit());
			mainTabPane.addTab(ControlMain.getProperty("tab_settings"), iconManager.getIcon("configure.png"), mainTabPane.getTabSettings());
			ControlMain.splash.setProgress(80, ControlMain.getProperty("progress_settingsPanel"));
			mainTabPane.addTab(ControlMain.getProperty("tab_about"), iconManager.getIcon("info.gif"), mainTabPane.getTabAbout());
			ControlMain.splash.setProgress(90, ControlMain.getProperty("progress_about"));

		}
		return mainTabPane;
	}

	void createMenu() {
		registerTrayIconListener();

		//	  create an exit item
		SysTrayMenuItem itemOpen = new SysTrayMenuItem(ControlMain.getProperty("open"), "open");
		itemOpen.addSysTrayMenuListener(control);

		// create an exit item
		SysTrayMenuItem itemExit = new SysTrayMenuItem(ControlMain.getProperty("exit"), "exit");
		itemExit.addSysTrayMenuListener(control);

		// create an about item
		SysTrayMenuItem itemAbout = new SysTrayMenuItem(ControlMain.getProperty("about"), "about");
		itemAbout.addSysTrayMenuListener(control);

		// create the main menu
		menu = new SysTrayMenu(sysTrayIcon[0], ControlMain.version[0]);

		// insert items
		menu.addItem(itemExit);
		menu.addSeparator();
		menu.addItem(itemAbout);
		menu.addSeparator();
		menu.addItem(itemOpen);
	}

	private void registerTrayIconListener() {
		sysTrayIcon[0].addSysTrayMenuListener(control);
		sysTrayIcon[1].addSysTrayMenuListener(control);
	}

	/**
	 * Setzt das Tray Menü in Abhängigkeit von useTray Wird vom Einstellungstab aufgerufen wenn die CheckBox ihren Status ändert.
	 * 
	 * @param useTray
	 * @author Reinhard Achleitner (crazyreini)
	 * @version 24.11.2004 - 12:08
	 */
	public void checkTrayMenu(boolean useTray) {
		if (useTray) {
			if (menu == null) {
				// Falls es noch kein Menü gibt, erzeuge es
				createMenu();
			} else {
				// Das Menü existiert schon, zeige das Icon
				menu.showIcon();
			}
		} else {
			if (menu != null) {
				// Verstecke Icon
				menu.hideIcon();
			}
		}
	}

	public GuiTabProgramm getTabProgramm() {
		return this.getMainTabPane().getTabProgramm();
	}

	public GuiBoxTimerPanel getTabTimer() {
		return this.getMainTabPane().getTabTimer();
	}

	public GuiTabSettings getTabSettings() {
		return this.getMainTabPane().getTabSettings();
	}
	
	public GuiTabStart getTabStart() {
		return this.getMainTabPane().getTabStart();
	}

	public GuiTabAbout getTabAbout() {
		return this.getMainTabPane().getTabAbout();
	}
	public GuiTabMovieGuide getTabMovieGuide() {
		return this.getMainTabPane().getTabMovieGuide();
	}
	public GuiTabRecordInfo getTabRecordInfo() {
		return this.getMainTabPane().getTabRecordInfo();
	}
	public void setSystrayRecordIcon() {
		if (menu != null) {
			menu.setIcon(sysTrayIcon[1]);
		}
	}
	public void setSystrayDefaultIcon() {
		if (menu != null) {
			menu.setIcon(sysTrayIcon[0]);
		}
	}
	/**
	 * @return Returns the control.
	 */
	public ControlMainView getControl() {
		return control;
	}
	/**
	 * @param control
	 *            The control to set.
	 */
	public void setControl(ControlMainView control) {
		this.control = control;
	}
	public void setVisible(boolean value) {
	    super.setVisible(value);
	    ControlMain.logWindow.setVisible(value);
	}
}