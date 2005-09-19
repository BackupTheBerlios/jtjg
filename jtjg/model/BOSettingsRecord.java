package model;
/*
BOSettingsRecord.java by Geist Alexander 

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
public class BOSettingsRecord {

	private BOSettings settings;
	private boolean saveLocal;
	
	public String streamingServerPort;
	public boolean startStreamingServer;
	public boolean startPX;
	public boolean recordAllPids;
	public boolean ac3ReplaceStereo;
	public boolean stereoReplaceAc3;
	public boolean shutdownAfterRecord;
	public BOUdrecOptions udrecOptions;

	public String jgrabberStreamType; //PES, TS, ES
	public String udrecStreamType; //PES, TS
    public String vlcStreamType; //PS
	public int streamingEngine; //0=JGrabber, 1=udrec, 2=vlc
	public String recordTimeBefore;
	public String recordTimeAfter;

	public boolean storeEPG;
	public boolean storeLogAfterRecord;
	public boolean recordVtxt;
	public boolean stopPlaybackAtRecord;

	public String dirPattern;
	public String filePattern;

	private BOAfterRecordOptions afterRecordOptions;
	
	public BOSettingsRecord(BOSettings settings) {
		this.setSettings(settings);
	}

	private void setSettingsChanged(boolean value) {
		this.getSettings().setSettingsChanged(value);
	}

	/**
	 * @return Returns the settings.
	 */
	public BOSettings getSettings() {
		return settings;
	}
	/**
	 * @param settings
	 *            The settings to set.
	 */
	public void setSettings(BOSettings settings) {
		this.settings = settings;
	}

	/**
	 * @return Returns the streamingServerPort.
	 */
	public String getStreamingServerPort() {
		return streamingServerPort;
	}
	/**
	 * @param streamingServerPort
	 *            The streamingServerPort to set.
	 */
	public void setStreamingServerPort(String streamingServerPort) {
		if (this.streamingServerPort == null || !this.streamingServerPort.equals(streamingServerPort)) {
			setSettingsChanged(true);
			this.streamingServerPort = streamingServerPort;
		}
	}
	/**
	 * @return Returns the startStreamingServer.
	 */
	public boolean isStartStreamingServer() {
		return startStreamingServer;
	}
	/**
	 * @param startStreamingServer
	 *            The startStreamingServer to set.
	 */
	public void setStartStreamingServer(boolean startServer) {
		if (this.startStreamingServer != startServer) {
			setSettingsChanged(true);
			this.startStreamingServer = startServer;
		}
	}
	/**
	 * @return int
	 */
	public String getJgrabberStreamType() {
		return jgrabberStreamType;
	}

	/**
	 * Sets the streamType.
	 * 
	 * @param streamType
	 *            The streamType to set
	 */
	public void setJgrabberStreamType(String streamType) {
		if (this.jgrabberStreamType == null || !this.jgrabberStreamType.equals(streamType)) {
			setSettingsChanged(true);
			this.jgrabberStreamType = streamType;
		}
	}
	public void setStreamType(String streamType) {
		if (this.getStreamingEngine() == 0) {
			this.setJgrabberStreamType(streamType);
		} else if (this.getStreamingEngine() == 1){
			this.setUdrecStreamType(streamType);
		} else {
            this.setVlcStreamType(streamType);
        }
	}
	/**
	 * @return Returns the startPX.
	 */
	public boolean isStartPX() {
		return startPX;
	}
	/**
	 * @param startPX
	 *            The startPX to set.
	 */
	public void setStartPX(boolean startPX) {
		if (this.startPX != startPX) {
			setSettingsChanged(true);
			this.startPX = startPX;
		}
	}
	/**
	 * @return Returns the streamingEngine.
	 */
	public int getStreamingEngine() {
		return streamingEngine;
	}
	/**
	 * @param streamingEngine
	 *            The streamingEngine to set.
	 */
	public void setStreamingEngine(int engine) {
		if (this.streamingEngine != engine) {
			setSettingsChanged(true);
			this.streamingEngine = engine;
		}
	}
	/**
	 * @return Returns the jUdrecStreamType.
	 */
	public String getUdrecStreamType() {
		return udrecStreamType;
	}
	/**
	 * @param udrecStreamType
	 *            The jUdrecStreamType to set.
	 */
	public void setUdrecStreamType(String streamType) {
		if (this.udrecStreamType == null || !this.udrecStreamType.equals(streamType)) {
			setSettingsChanged(true);
			this.udrecStreamType = streamType;
		}
	}

	/**
	 * @return Returns the recordAllPids.
	 */
	public boolean isRecordAllPids() {
		return recordAllPids;
	}
	/**
	 * @param recordAllPids
	 *            The recordAllPids to set.
	 */
	public void setRecordAllPids(boolean recordPids) {
		setSettingsChanged(true);
		recordAllPids = recordPids;
		if (recordPids == true) {
			this.setAc3ReplaceStereo(false);
			this.setStereoReplaceAc3(false);
		}
	}
	/**
	 * @return Returns the ac3ReplaceStereo.
	 */
	public boolean isStereoReplaceAc3() {
		return stereoReplaceAc3;
	}
	/**
	 * @param ac3ReplaceStereo
	 *            The ac3ReplaceStereo to set.
	 */
	public void setStereoReplaceAc3(boolean value) {
		setSettingsChanged(true);
		stereoReplaceAc3 = value;
		if (stereoReplaceAc3 == true) {
			this.setAc3ReplaceStereo(false);
			this.setRecordAllPids(false);
		}
	}
	/**
	 * @return Returns the recordTimeAfter.
	 */
	public String getRecordTimeAfter() {
		return recordTimeAfter;
	}
	/**
	 * @param recordTimeAfter
	 *            The recordTimeAfter to set.
	 */
	public void setRecordTimeAfter(String recordTimeAfter) {
		if (this.recordTimeAfter == null || !this.recordTimeAfter.equals(recordTimeAfter)) {
			setSettingsChanged(true);
			this.recordTimeAfter = recordTimeAfter;
		}
	}
	/**
	 * @return Returns the recordTimeBefore.
	 */
	public String getRecordTimeBefore() {
		return recordTimeBefore;
	}
	/**
	 * @param recordTimeBefore
	 *            The recordTimeBefore to set.
	 */
	public void setRecordTimeBefore(String recordTimeBefore) {
		if (this.recordTimeBefore == null || !this.recordTimeBefore.equals(recordTimeBefore)) {
			setSettingsChanged(true);
			this.recordTimeBefore = recordTimeBefore;
		}
	}
	/**
	 * @return Returns the ac3ReplaceStereo.
	 */
	public boolean isAc3ReplaceStereo() {
		return ac3ReplaceStereo;
	}
	/**
	 * @param ac3ReplaceStereo
	 *            The ac3ReplaceStereo to set.
	 */
	public void setAc3ReplaceStereo(boolean value) {
		setSettingsChanged(true);
		ac3ReplaceStereo = value;
		if (ac3ReplaceStereo == true) {
			this.setStereoReplaceAc3(false);
			this.setRecordAllPids(false);
		}
	}
	/**
	 * @return Returns the udrecOptions.
	 */
	public BOUdrecOptions getUdrecOptions() {
		if (udrecOptions==null) {
			udrecOptions=new BOUdrecOptions();
		}
		return udrecOptions;
	}

	/**
	 * @param udrecOptions
	 *            The udrecOptions to set.
	 */
	public void setUdrecOptions(BOUdrecOptions udrecOptions) {
		if (this.udrecOptions == null || !this.udrecOptions.equals(udrecOptions)) {
			setSettingsChanged(true);
			this.udrecOptions = udrecOptions;
		}
	}
	
	public boolean isStoreEPG() {
		return storeEPG;
	}
	public void setStoreEPG(boolean storeEPG) {
		this.storeEPG = storeEPG;
	}
	public boolean isStoreLogAfterRecord() {
		return storeLogAfterRecord;
	}
	public void setStoreLogAfterRecord(boolean storeLogAfterRecord) {
		this.storeLogAfterRecord = storeLogAfterRecord;
	}

	/**
	 * @return Returns the recordVtxt.
	 */
	public boolean isRecordVtxt() {
		return recordVtxt;
	}
	/**
	 * @param recordVtxt
	 *            The recordVtxt to set.
	 */
	public void setRecordVtxt(boolean recordVtxt) {
		if (this.recordVtxt != recordVtxt) {
			setSettingsChanged(true);
			this.recordVtxt = recordVtxt;
		}
	}

	/**
	 * @return Returns the shutdownAfterRecord.
	 */
	public boolean isShutdownAfterRecord() {
		return shutdownAfterRecord;
	}
	/**
	 * @param shutdownAfterRecord
	 *            The shutdownAfterRecord to set.
	 */
	public void setShutdownAfterRecord(boolean shutdownAfterRecord) {
		if (this.shutdownAfterRecord != shutdownAfterRecord) {
			setSettingsChanged(true);
			this.shutdownAfterRecord = shutdownAfterRecord;
		}
	}

	public void setFilePattern(String filePattern) {
		if (this.filePattern == null || !filePattern.equals(this.filePattern)) {
			this.filePattern = filePattern;
			setSettingsChanged(true);
		}
	}

	public String getDirPattern() {
		return dirPattern;
	}

	public void setDirPattern(String dirPattern) {
		if (this.dirPattern == null || !dirPattern.equals(this.dirPattern)) {
			this.dirPattern = dirPattern;
			setSettingsChanged(true);
		}
	}

	public String getFilePattern() {
		return filePattern;
	}

	public boolean isStopPlaybackAtRecord() {
		return stopPlaybackAtRecord;
	}
	public void setStopPlaybackAtRecord(boolean stopPlaybackAtRecord) {
		if (this.stopPlaybackAtRecord != stopPlaybackAtRecord) {
			this.stopPlaybackAtRecord = stopPlaybackAtRecord;
			setSettingsChanged(true);
		}
	}
    /**
     * @return Returns the saveLocal.
     */
    public boolean isSaveLocal() {
        return saveLocal;
    }
    /**
     * @param saveLocal The saveLocal to set.
     */
    public void setSaveLocal(boolean saveLocal) {
        this.saveLocal = saveLocal;
    }
    /**
     * @return Returns the vlcStreamType.
     */
    public String getVlcStreamType() {
        if (vlcStreamType==null) {
            vlcStreamType="PS MPEG-Program";
        }
        return vlcStreamType;
    }
    /**
     * @param vlcStreamType The vlcStreamType to set.
     */
    public void setVlcStreamType(String vlcStreamType) {
        if (this.vlcStreamType == null || !this.vlcStreamType.equals(vlcStreamType)) {
            setSettingsChanged(true);
            this.vlcStreamType = vlcStreamType;
        }
    }
	/**
	 * @return Returns the afterRecordOptions.
	 */
	public BOAfterRecordOptions getAfterRecordOptions() {
		if (afterRecordOptions==null) {
			afterRecordOptions = new BOAfterRecordOptions();
		}
		return afterRecordOptions;
	}
	/**
	 * @param afterRecordOptions The afterRecordOptions to set.
	 */
	public void setAfterRecordOptions(BOAfterRecordOptions afterRecordOption) {
		this.afterRecordOptions = afterRecordOption;
	}
}