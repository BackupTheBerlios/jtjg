package model;
/*
 * BORecordArgs.java by Geist Alexander
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
import service.SerTimerHandler;

public class BORecordArgs {

	String senderName;
	String epgTitle;
	String bouquetNr;
	String eventId;
	String epgInfo1;
	String epgInfo2;
	String epgId;
	String mode;
	String command;
	BOPids pids;
	BOLocalTimer localTimer;
	boolean quickRecord;
	private long stopTimeOfQuickRecord;

	public BORecordArgs(boolean quickRecord) {
		this.setQuickRecord(quickRecord);
	}
	public BORecordArgs(BOPids pids, BOLocalTimer timer, boolean qRecord) {
		this.setPids(pids);
		quickRecord = qRecord;
		this.setLocalTimer(timer);
	}
	/**
	 * @return Returns the aPids.
	 */
	/**
	 * @return Returns the channelId.
	 */
	public String getEventId() {
		return eventId;
	}
	/**
	 * @param channelId
	 *            The channelId to set.
	 */
	public void setEventId(String channelId) {
		this.eventId = channelId;
	}
	/**
	 * @return Returns the epgId.
	 */
	public String getEpgId() {
		return epgId;
	}
	/**
	 * @param epgId
	 *            The epgId to set.
	 */
	public void setEpgId(String epgId) {
		this.epgId = epgId;
	}
	/**
	 * @return Returns the epgInfo1.
	 */
	public String getEpgInfo1() {
		return epgInfo1;
	}
	/**
	 * @param epgInfo1
	 *            The epgInfo1 to set.
	 */
	public void setEpgInfo1(String epgInfo1) {
		this.epgInfo1 = epgInfo1;
	}
	/**
	 * @return Returns the epgInfo2.
	 */
	public String getEpgInfo2() {
		return epgInfo2;
	}
	/**
	 * @param epgInfo2
	 *            The epgInfo2 to set.
	 */
	public void setEpgInfo2(String epgInfo2) {
		this.epgInfo2 = epgInfo2;
	}
	/**
	 * @return Returns the epgTitle.
	 */
	public String getEpgTitle() {
		return epgTitle;
	}
	/**
	 * @param epgTitle
	 *            The epgTitle to set.
	 */
	public void setEpgTitle(String epgTitle) {
		this.epgTitle = epgTitle;
	}
	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode
	 *            The mode to set.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return Returns the senderName.
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName
	 *            The senderName to set.
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return Returns the command.
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command
	 *            The command to set.
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	/**
	 * @return Returns the bouquetNr.
	 */
	public String getBouquetNr() {
		return bouquetNr;
	}
	/**
	 * @param bouquetNr
	 *            The bouquetNr to set.
	 */
	public void setBouquetNr(String bouquetNr) {
		this.bouquetNr = bouquetNr;
	}
	/**
	 * @return Returns the quickRecord.
	 */
	public boolean isQuickRecord() {
		return quickRecord;
	}
	/**
	 * @param quickRecord
	 *            The quickRecord to set. wenn es sich um eine Timer-Aufnahme handelt, den zugehoerigen Local-Timer suchen
	 */
	private void setQuickRecord(boolean quickRecord) {
		if (!quickRecord) {
			this.setLocalTimer(SerTimerHandler.getRunningLocalBoxTimer());
		}
		this.quickRecord = quickRecord;
	}

	public void loadLocalTimer() {
		if (!quickRecord) {
			this.setLocalTimer(SerTimerHandler.getRunningLocalBoxTimer());
		}
	}
	/**
	 * @return Returns the pids.
	 */
	public BOPids getPids() {
		return pids;
	}
	/**
	 * @param pids
	 *            The pids to set.
	 */
	public void setPids(BOPids pids) {
		this.pids = pids;
	}
	public void checkTitle() {
		if (this.getLocalTimer().getDescription().length() > 0) {
			this.setEpgTitle(this.getLocalTimer().getDescription());
		}
	}

	/*
	 * Setzen der richtigen Audiopids Checken ob Videotext aufgenommen werden soll
	 */
	private void checkRecordPids() {

		if (getPids() != null) {
			if (this.getLocalTimer().isAc3ReplaceStereo()) {
				for (int i = this.getPids().getAPids().size() - 1; 0 <= i; i--) {
					BOPid aPid = (BOPid) this.getPids().getAPids().get(i);
					if (!aPid.isAc3() && this.getPids().includesAc3Pid()) {
						this.getPids().getAPids().remove(aPid);
					}
				}
			}
			if (this.getLocalTimer().isStereoReplaceAc3()) {
				for (int i = this.getPids().getAPids().size() - 1; 0 <= i; i--) {
					BOPid aPid = (BOPid) this.getPids().getAPids().get(i);
					if (aPid.isAc3()) {
						this.getPids().getAPids().remove(aPid);
					}
				}
			}
			if (!this.getLocalTimer().isRecordVtxt()) {
				this.getPids().setVtxtPid(null);
			}
		}
	}
	/**
	 * @return Returns the localTimer.
	 */
	public BOLocalTimer getLocalTimer() {
		if (localTimer == null) {
			localTimer = BOLocalTimer.getDefaultLocalTimer();
		}
		return localTimer;
	}
	/**
	 * @param localTimer
	 *            The localTimer to set.
	 */
	public void setLocalTimer(BOLocalTimer localTimer) {
		this.localTimer = localTimer;
		if (localTimer != null) {
			if (localTimer.getDescription() != null && localTimer.getDescription().length() > 0) {
				setEpgTitle(localTimer.getDescription());
			}
		}
		checkRecordPids();
	}

	public long getStopTimeOfQuickRecord() {
		return stopTimeOfQuickRecord;
	}
	public void setStopTimeOfQuickRecord(long stopTimeOfQuickRecord) {
		this.stopTimeOfQuickRecord = stopTimeOfQuickRecord;
	}
}