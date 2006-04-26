package streaming;
/*
RecordControl.java by Geist Alexander 

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import model.BOAfterRecordOptions;
import model.BORecordArgs;
import model.BOTimer;

import org.apache.log4j.Logger;

import service.SerFormatter;
import service.SerHelper;
import service.SerProcessStopListener;
import service.SerTimerHandler;
import control.ControlMain;
import control.ControlProgramTab;
import control.muxxer.ControlMuxxerView;

public class RecordControl extends Thread implements SerProcessStopListener {
	Record record;
	public boolean isRunning = true;
	public boolean tvMode;
	ControlProgramTab controlProgramTab;
	private BORecordArgs recordArgs;
	String fileName;
	File directory;
    public String initSptsStatus;
	private static final String EPGFILENAME = "epg.txt";

	public RecordControl(BORecordArgs args, ControlProgramTab control) throws IOException {
	    tvMode = ControlMain.getBoxAccess().isTvMode();
        this.setRecordArgs(args);
		controlProgramTab = control;
		this.detectRecord();
        this.initializeSptsStatus();
	}

	private void detectRecord() {
		if (tvMode) {
            if (getRecordArgs().getLocalTimer().getStreamingEngine() == 0) {
                record = new UdpRecord(getRecordArgs(), this);
            } else if (getRecordArgs().getLocalTimer().getStreamingEngine() == 1) {
                record = new UdrecRecord(getRecordArgs(), this);
            } else if (getRecordArgs().getLocalTimer().getStreamingEngine() == 2) {
                record = new VlcRecord(getRecordArgs(), this);
            }
		} else {
            record = new TcpRecord(getRecordArgs(), this); 
        }
	}
    
    private void initializeSptsStatus() {
        initSptsStatus=ControlMain.getBoxAccess().getSptsStatus();
        if (initSptsStatus.equals("pes") && record.streamType.equalsIgnoreCase("TS")) {
            ControlMain.getBoxAccess().setSptsStatus("spts");
        } else if (initSptsStatus.equals("spts") && record.streamType.equalsIgnoreCase("PES")) {
            ControlMain.getBoxAccess().setSptsStatus("pes");
        }
    }

    private void setOldSptsStatus() {
        if (!initSptsStatus.equals(ControlMain.getBoxAccess().getSptsStatus())) {
            ControlMain.getBoxAccess().setSptsStatus(initSptsStatus);
        }
    }
    
	/*
	 * Kontrolle der Stopzeit einer Sofortaufnahme
	 */
	public void run() {

		if (getRecordArgs().getLocalTimer().isStoreEPG()) {
			saveEPGInfos();
		}

		try {
			if (getRecordArgs().getLocalTimer().isStopPlaybackAtRecord()) {
				ControlMain.getBoxAccess().setRecordModusWithPlayback();
			} else {
				ControlMain.getBoxAccess().setRecordModus();
			}
		} catch (IOException e) {
			Logger.getLogger("RecordControl").error(e.getMessage());
		}
		record.start();

		if (getRecordArgs().isQuickRecord()) {
			waitForStop();
		}
	}

	/**
	 * speichert die EPG Informationen in einer Datei
	 *  
	 */
	private void saveEPGInfos() {

		PrintStream print = null;
		try {

			StringBuffer epg = new StringBuffer();

			String title = getRecordArgs().getEpgTitle();
			if (title != null) {
				
				String info1 = getRecordArgs().getEpgInfo1();
				
				if (info1 == null) {
					info1 = "";
				}
				if (!info1.startsWith(title)) {
					epg.append(title);
					epg.append("\n");
				}
			}

			String info1 = getRecordArgs().getEpgInfo1();
			if (info1 != null && !info1.equals(title)) {
				epg.append(info1);
				epg.append("\n");
			}
			String info2 = getRecordArgs().getEpgInfo2();
			if (info2 != null) {
				epg.append(info2);
			}

			String file = getDirectory().toString() + File.separatorChar + EPGFILENAME;
			print = new PrintStream(new FileOutputStream(file));

			StringTokenizer tok = new StringTokenizer(epg.toString(), "\n");
			while (tok.hasMoreTokens()) {
				print.println(tok.nextToken());
			}

		} catch (Exception e) {
			Logger.getLogger("RecordControl").error(e);
		} finally {
			if (print != null) {
				print.close();
			}
		}
	}

	public void processStopped(int exitCode, String processName) {
        if (processName.equals("vlcRecord")) {
            this.controlProgramTab.stopRecord();
        } else {
            this.checkForShutdown(); //ende des PX-Prozesses    
        }
	}

	private void waitForStop() {
		while (isRunning) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}
            
			if (new Date().getTime() - controlProgramTab.getRecordStopTime().getTime() > 0) {
                isRunning = false;
                controlProgramTab.stopRecord();
			}
		}
	}

	public void stopRecord() {
        isRunning = false;
        BOTimer timer = getRecordArgs().getLocalTimer().getMainTimer();
        if (timer != null && timer.getEventRepeatId().equals("0")) {  //Lokal-Timer Aufnahme
        	timer.setModifiedId("remove");
            SerTimerHandler.saveTimer(timer, false, true);
            LocalTimerRecordDaemon.running=false;   
        }

		record.stop();
        BOAfterRecordOptions options = getRecordArgs().getLocalTimer().getAfterRecordOptions(); 
		if (options.isUseProjectX() || options.isUseMplex()) {
            ArrayList files = record.getFiles(); 
            if (files != null && files.size() > 0) {
                new ControlMuxxerView(options, this, files);
            }			
		} else {
			this.checkForShutdown();
		}
		
		try {
			ControlMain.getBoxAccess().stopRecordModus();
		} catch (IOException e) {
			Logger.getLogger("RecordControl").error(e.getMessage());
		}
        this.setOldSptsStatus();
	}

	private void checkForShutdown() {
		if (getRecordArgs().isQuickRecord() && controlProgramTab.isShutdownAfterRecord()) {
			ControlMain.shutdown();
		}
        else if (!getRecordArgs().isQuickRecord() && getRecordArgs().getLocalTimer().isShutdownAfterRecord()) {
			ControlMain.shutdown();
		}
	}

	public String getFileName() {
		if (this.fileName == null) {
			String pattern = getRecordArgs().getLocalTimer().getFilePattern();

			if (pattern == null || pattern.length() == 0) {
				pattern = getRecordArgs().getLocalTimer().getDirPattern();
			}

			fileName = SerHelper.createFileName(getRecordArgs(), pattern);

			// create directory
			String dirName = SerHelper.createFileName(getRecordArgs(), getRecordArgs().getLocalTimer().getDirPattern());

			directory = new File(getRecordArgs().getLocalTimer().getSavePath(), SerFormatter.removeInvalidCharacters(dirName));
			directory.mkdir();
		}
		return SerFormatter.removeInvalidCharacters(fileName);
	}

	public File getDirectory() {
		if (directory == null) {
			getFileName();
		}
		return directory;
	}
    /**
     * @return Returns the recordArgs.
     */
    public BORecordArgs getRecordArgs() {
        return recordArgs;
    }
    /**
     * @param recordArgs The recordArgs to set.
     */
    public void setRecordArgs(BORecordArgs recordArgs) {
        this.recordArgs = recordArgs;
    }
}