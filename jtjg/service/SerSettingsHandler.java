package service;
/*
 * SerSettingsHandler.java by Geist Alexander
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
import java.awt.Point;
import java.awt.Toolkit;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import model.BOAfterRecordOptions;
import model.BOSettings;
import model.BOSettingsLayout;
import model.BOSettingsMain;
import model.BOSettingsMovieGuide;
import model.BOSettingsPath;
import model.BOSettingsPlayback;
import model.BOSettingsRecord;
import model.BOUdrecOptions;

import org.apache.log4j.Logger;

import com.jgoodies.plaf.plastic.Plastic3DLookAndFeel;

import control.ControlMain;
import control.settings.ControlSettingsTabMovieGuide;

public class SerSettingsHandler {

	public static void saveAllSettings() throws Exception {

		XMLEncoder dec = new XMLEncoder(new FileOutputStream(new File(ControlMain.getSettingsFilename())));
		dec.writeObject(ControlMain.getSettings());
		dec.flush();
		dec.close();
	}

	public static void readSettings() {
		try {
			XMLDecoder dec = new XMLDecoder(new FileInputStream(ControlMain.getSettingsFilename()),null,new ExceptionListener() {
				public void exceptionThrown(Exception e) {
					Logger.getLogger("ControlMain").error(e.getMessage());
				}
			});

			BOSettings settings = (BOSettings) dec.readObject();
			settings.setSettingsChanged(false);
			ControlMain.setSettings(settings);
			Logger.getLogger("ControlMain").info("Settings found");

		}catch(FileNotFoundException e)
		{
			try {
				// create default settings
				BOSettings set = createStandardSettingsFile();
				ControlMain.setSettings(set);
				SerSettingsHandler.saveAllSettings();
				
				
			} catch (Exception e1) {
				Logger.getLogger("ControlMain").error("Cant create settings: " + e1.getMessage());
			}
		}
		
		catch (Exception ex) {
			Logger.getLogger("ControlMain").info("old settings not longer supported");
			JOptionPane.showMessageDialog(null,ControlMain.getProperty("msg_oldSettingsNotSupported"));
		}
	}
	
	/**
	 * Erstellen eines neuen XML-Settingsdokumentes mit Defaultwerten
	 */
	public static BOSettings createStandardSettingsFile() {
		BOSettings settings = new BOSettings();
		BOSettingsLayout layout = new BOSettingsLayout(settings);
		BOSettingsMain main = new BOSettingsMain(settings);
		BOSettingsMovieGuide mg = new BOSettingsMovieGuide(settings);
		BOSettingsPath pathS = new BOSettingsPath(settings);
		BOSettingsPlayback play = new BOSettingsPlayback(settings);
		BOSettingsRecord rec = new BOSettingsRecord(settings);
		
		settings.setLayoutSettings(layout);
		settings.setMainSettings(main);
		settings.setMovieGuideSettings(mg);
		settings.setPathSettings(pathS);
		settings.setPlaybackSettings(play);
		settings.setRecordSettings(rec);
		settings.standardSettings=true;
		

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension wSize = new Dimension(800,565);
	
		int x = (screenSize.width - wSize.width) / 2;
		int y = (screenSize.height - wSize.height) / 2;
		layout.setLocation(new Point(x,y));
		layout.setSize(wSize);
		
		play.setAlwaysUseStandardPlayback(false);
		play.setPlaybackOptions(new ArrayList());
		play.setAudioOption(0);

		pathS.setUdrecPath( new File("udrec.exe").getAbsolutePath());
		pathS.setProjectXPath(new File("ProjectX.jar").getAbsolutePath());
		pathS.setVlcPath(new File("vlc.exe").getAbsolutePath());
        pathS.setMplexPath(new File("mplex.exe").getAbsolutePath());
		pathS.setShutdownToolPath("");
		pathS.setSavePath(ControlMain.jtjgDirectory);
		pathS.setWorkDirectory(ControlMain.jtjgDirectory);
		
		rec.setStartStreamingServer(true);
		rec.setStreamingServerPort("4000");
		rec.setStreamType("PES MPEG-Packetized Elementary");
		rec.setUdrecStreamType("PES MPEG-Packetized Elementary");
        rec.setVlcStreamType("PS MPEG-Program");
		rec.setStartPX(true);
		rec.setShutdownAfterRecord(false);
		rec.setStreamingEngine(0);
		rec.setRecordTimeAfter("0");
		rec.setRecordTimeBefore("0");
		rec.setAc3ReplaceStereo(false);
		rec.setStereoReplaceAc3(false);
		rec.setUdrecOptions(new BOUdrecOptions());
		rec.setRecordVtxt(false);
		rec.setRecordAllPids(true);
		rec.setStoreEPG(false);
		rec.setStoreLogAfterRecord(false);
		rec.setDirPattern("%DATE YY-MM-DD% %TIME% %CHANNEL% %NAME%");
		rec.setFilePattern("");
		rec.setSaveLocal(false);
		rec.setAfterRecordOptions(new BOAfterRecordOptions());
		
		main.setPlasticTheme("ExperienceBlue");
		main.setLocale("DE");
		main.setShowLogWindow(false);
		main.setStartFullscreen(false);
		main.setUseSysTray(false);
		main.setStartVlcAtStart(false);
		main.setLookAndFeel(Plastic3DLookAndFeel.class.getName());
		main.setBoxList(new ArrayList());
		
		ArrayList selChannels = new ArrayList();
		String[] channels = new String[]{"13TH STREET", "CLASSICA", "DISNEY CHANNEL", "FOX KIDS", "HEIMATKANAL", "HIT24", "JUNIOR",
				"MGM", "PREMIERE 1", "PREMIERE 2", "PREMIERE 3", "PREMIERE 4", "PREMIERE 5", "PREMIERE 6", "PREMIERE 7",
				"PREMIERE KRIMI", "PREMIERE NOSTALGIE", "PREMIERE SERIE", "PREMIERE START", "SCI FI"};
		selChannels.addAll(Arrays.asList(channels));
		mg.setMgSelectedChannels(selChannels);
		mg.setMgLoadType(ControlSettingsTabMovieGuide.MGLOADTYPE_ASK);
		mg.setMgDefault(ControlSettingsTabMovieGuide.MGDEFAULTDATE_ALL);
		mg.setMgStoreOriginal(false);
		return settings;
	}	
}