package control;
/*
 ControlMain by Alexander Geist

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
import java.awt.Frame;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JOptionPane;

import model.BOBox;
import model.BOSettings;
import model.BOSettingsLayout;
import model.BOSettingsMain;
import model.BOSettingsMovieGuide;
import model.BOSettingsPath;
import model.BOSettingsPlayback;
import model.BOSettingsProxy;
import model.BOSettingsRecord;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.dom4j.Document;

import presentation.GuiLogWindow;
import presentation.GuiSplashScreen;
import presentation.GuiStartFrame;
import service.SerExternalProcessHandler;
import service.SerIconManager;
import service.SerLogAppender;
import service.SerSettingsHandler;
import streaming.LocalTimerRecordDaemon;
import streaming.RecordControl;
import boxConnection.SerBoxControl;
import boxConnection.SerBoxControlDefault;
import boxConnection.SerBoxGuiDetector;
import boxConnection.SerStreamingServer;

/**
 * Startklasse, Start der Anwendung Deklaration der globalen Variablen
 */
public class ControlMain {

	static BOSettings settings;
	static Document settingsDocument;
	static Document movieGuideDocument;
	static SerBoxControl boxAccess;
	static ControlMainView control;
	static BOBox activeBox;

	private static Properties properties = new Properties();
	public static String xmgDirectory = System.getProperty("user.home")+File.separator+".xmg";
	private static Locale locale = new Locale("");
	public static GuiSplashScreen splash = null;
	public static GuiLogWindow logWindow;

	private static String settingsFilename;

	public static String version[] = {"XMediaGrabber 0.2.9c", "06.02.2005", "User: " + System.getProperty("user.name")};

	public static void main(String args[]) {
	    startSplash();
		startLogger();
		readSettings();
		setResourceBundle();
		logWindow = new GuiLogWindow();
		logSystemInfo();	
		initLogWindow();
		
		detectActiveBox();
		detectImage();
		initStreamingServer();
		control = new ControlMainView();
		control.initialize();
		checkStartVlc();
		checkGuiSettings();
		splash.setProgress(100, ControlMain.getProperty("msg_app_starting"));
		splash.dispose();
        new LocalTimerRecordDaemon().start();
        control.checkForStartWizard();
	}
	
	private static void startSplash() {
	    GuiStartFrame connectFrame = new GuiStartFrame();
        splash = new GuiSplashScreen(connectFrame, SerIconManager.getInstance().getIcon(
            "grabber1.png").getImage(), version[0], 0, 100);
        splash.setVisible(true);
	}
	
	private static void checkGuiSettings() {
	    if (getSettings().getMainSettings().isStartMinimized()) {
			if (getSettings().getMainSettings().isUseSysTray()) {
				control.getView().setVisible(false);
			} else {
				control.getView().setVisible(true);
				control.getView().setExtendedState(Frame.ICONIFIED);
				logWindow.setExtendedState(Frame.ICONIFIED);
			}
		} else {
			control.getView().setVisible(true);
			control.getView().requestFocus();
		}
	}
	
	private static void checkStartVlc() {
	    if (ControlMain.getSettings().getMainSettings().isStartVlcAtStart()) {
	        String[] execString={ControlMain.getSettingsPath().getVlcPath(),"-I", "http"};
	        SerExternalProcessHandler.startProcess("vlc", execString, true, true);
	    }
	}
	
	public static void initStreamingServer() {
	    if (getSettingsRecord().isStartStreamingServer() && !SerStreamingServer.isRunning) {
	        new SerStreamingServer().start();
	    }
	}
	
	private static void logSystemInfo() {
		for (int i=0; i<ControlMain.version.length; i++) {
			log(version[i]);
		}
		log("java.version\t"+System.getProperty("java.version"));
		log("java.vendor\t"+System.getProperty("java.vendor"));
		log("java.home\t"+System.getProperty("java.home"));
		log("java.vm.name\t"+System.getProperty("java.vm.name"));
	}
	
	private static void log(String logtext) {
		Logger.getLogger("ControlMain").info(logtext);
	}

	private static void initLogWindow() {
		logWindow.setShouldBeVisible(ControlMain.getSettingsMain().isShowLogWindow());
		Point logLoc = getSettings().getLayoutSettings().getLogLocation();
		Dimension logSize = getSettings().getLayoutSettings().getLogSize();
		if (logLoc != null) {
			logWindow.setLocation(logLoc);
		}
		if (logSize != null) {
			logWindow.setSize(logSize);
		}
		logWindow.setVisible(ControlMain.getSettingsMain().isShowLogWindow());
	}

	public static void startLogger() {
		PatternLayout layout = new PatternLayout();

		//http://logging.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html
		layout.setConversionPattern("%d{HH:mm:ss:ms} %-5p %c - %m%n");

		try {
			SerLogAppender logAppender = new SerLogAppender(layout);
			logAppender.setMaxBackupIndex(3); //Number of max Backup-Files
			logAppender.setMaxFileSize("100KB");
			BasicConfigurator.configure(logAppender);
		} catch (IOException e) {
		}
	}

	public synchronized static boolean detectImage() {
		log(ControlMain.getProperty("msg_searchImage"));
		splash.setProgress(15, ControlMain.getProperty("msg_searchImage"));
		
		Thread waitThread = new Thread();
		synchronized( waitThread ) {
			try {
				new SerBoxGuiDetector(getBoxIpOfActiveBox(), waitThread).start();
				waitThread.wait(2000);
			} catch ( InterruptedException e ) {
				log(e.getMessage());
			}
		}
		log(ControlMain.getBoxAccess().getName() + "-" + ControlMain.getProperty("msg_accessLoaded"));
		return true;
	}

	/**
	 * Settings aus dem XML-File lesen. Falls Keines vorhanden ein leeres Dokument erstellen. Dokument wird immer gebraucht, falls neue
	 * Settings gespeichert werden.
	 */
	public static void readSettings() {
		SerSettingsHandler.readSettings();
	}

	public static String getBoxIpOfActiveBox() {
		BOBox box = getActiveBox();
		if (box == null) {
			return new String();
		} 
		return box.getDboxIp();
	}

	/**
	 * Wenn nur eine Box angelegt, diese als Standard benutzen. Wenn mehrere Boxen angelegt sind, und keine als Standard definiert ist, die
	 * 1. Box als Standard benutzen
	 */
	public static boolean detectActiveBox() {
		ArrayList boxList = getSettingsMain().getBoxList();
		BOBox box;
		if (boxList.size() == 1) { //nur eine Box vorhanden, diese als Standard benutzen
			box = (BOBox) boxList.get(0);
			box.setSelectedBox(true);
			setActiveBox(box);
			return true;
		}
		for (int i = 0; boxList.size() > i; i++) {
			box = (BOBox) boxList.get(i);
			if (box.isStandard()) { //mehrer Boxen vorhanden, die Standardbox zurückgeben
				box.setSelectedBox(true);
				setActiveBox(box);
				return true;
			}
		}
		if (boxList.size() > 0) {//mehrere Boxen vorhanden, die 1. als Standard benutzen
			box = (BOBox) boxList.get(0);
			box.setSelectedBox(true);
			setActiveBox(box);
			return true;
		}
		return false;
	}

	public static int getIndexOfActiveBox() {
		ArrayList boxList = getSettingsMain().getBoxList();
		if (getActiveBox() != null) {
			for (int i = 0; boxList.size() > i; i++) {
				BOBox box = (BOBox) boxList.get(i);
				if (box.getDboxIp().equals(getActiveBox().getDboxIp())) {
					return i;
				}
			}
		}
		return -1;
	}
	public static void newBoxSelected(BOBox box) {
		box.setSelectedBox(true);
		setActiveBox(box);
		ControlMain.detectImage();
	}
	/**
	 * @return Returns the settings.
	 */
	public static BOSettings getSettings() {
		return settings;
	}
	public static BOSettingsMain getSettingsMain() {
		return settings.getMainSettings();
	}
	public static BOSettingsProxy getSettingsProxy() {
		return settings.getProxySettings();
	}
	public static BOSettingsRecord getSettingsRecord() {
		return settings.getRecordSettings();
	}
	public static BOSettingsPlayback getSettingsPlayback() {
		return settings.getPlaybackSettings();
	}
	public static BOSettingsMovieGuide getSettingsMovieGuide() {
		return settings.getMovieGuideSettings();
	}
	public static BOSettingsPath getSettingsPath() {
		return settings.getPathSettings();
	}
	public static BOSettingsLayout getSettingsLayout() {
		return settings.getLayoutSettings();
	}
	/**
	 * @param settings
	 *            The settings to set.
	 */
	public static void setSettings(BOSettings settings) {
		ControlMain.settings = settings;
	}

	/**
	 * @return Returns the settingsDocument.
	 */
	public static Document getSettingsDocument() {
		return settingsDocument;
	}
	/**
	 * @param settingsDocument
	 *            The settingsDocument to set.
	 */
	public static void setSettingsDocument(Document settingsDocument) {
		ControlMain.settingsDocument = settingsDocument;
	}

	/**
	 * @return Returns the box.
	 */
	public static SerBoxControl getBoxAccess() {
		if (boxAccess==null) {
			boxAccess=new SerBoxControlDefault();
		}
		return boxAccess;
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static void setResourceBundle() {
		String locale = "messages_" + ControlMain.getSettingsMain().getShortLocale() + ".properties";
		URL url = ClassLoader.getSystemResource("locale/"+locale.toLowerCase());
		try {
			properties.load(url.openStream());
			splash.setProgress(5, ControlMain.getProperty("progress_settings"));
		} catch (IOException ex) {
			Logger.getLogger("ControlMain").error(ControlMain.getProperty("msg_propertyError") + locale);
		}
	}
	/**
	 * @return Returns the control.
	 */
	public static ControlMainView getControl() {
		return control;
	}
	/**
	 * @param control
	 *            The control to set.
	 */
	public static void setControl(ControlMainView control) {
		ControlMain.control = control;
	}
	/**
	 * @return Returns the activeBox.
	 */
	public static BOBox getActiveBox() {
		return activeBox;
	}
	/**
	 * @param activeBox
	 *            The activeBox to set.
	 */
	public static void setActiveBox(BOBox activeBox) {
		ControlMain.activeBox = activeBox;
	}

	public static void shutdown() {
        try {
            getBoxAccess().shutdownBox();
        } catch (IOException e) {}
		SerExternalProcessHandler.startProcess("shutdown", getSettingsPath().getShutdownToolPath(), true);
	}

	public static void endProgram() {
        int result=0;
        RecordControl record = getControl().getView().getTabProgramm().getControl().getRecordControl();
        if (record!=null && record.isRunning ) {
            result = JOptionPane.showConfirmDialog(
                    getControl().getView(),
                    getProperty("msg_warnRecord2")+": "+record.getRecordArgs().getEpgTitle(),
                    getProperty("msg_warnRecord1"),
                    JOptionPane.YES_NO_OPTION
            );
        }
        if (result==0) {
            SerExternalProcessHandler.closeAll();
            try {
                //save log layout settings
                saveLogLayout();
                if (ControlMain.getSettings().isSettingsChanged()) {
                    SerSettingsHandler.saveAllSettings();
                    log("Settings saved");
                }
            } catch (Exception e1) {
                Logger.getLogger("ControlMain").error("Error while save Settings");
            }
            System.exit(0);        
        }
	}

	private static void saveLogLayout() {
		if (logWindow != null) {
			getSettings().getLayoutSettings().setLogLocation(logWindow.getLocation());
			getSettings().getLayoutSettings().setLogSize(logWindow.getSize());
		}
	}

    /**
     * @return Returns the settingsFilename.
     */
    public static String getSettingsFilename() {
        if (settingsFilename==null) {
            File userHome = new File(xmgDirectory);
            if (!userHome.exists()) {
                userHome.mkdir();
            }
            settingsFilename = xmgDirectory+File.separator+"settings.xml";    
        }
        return settingsFilename;
    }
	/**
	 * @param boxAccess The boxAccess to set.
	 */
	public static void setBoxAccess(SerBoxControl boxAccess) {
		ControlMain.boxAccess = boxAccess;
	}
}