package boxConnection.control;
/*
SerBoxControl.java by Alexander Geist

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
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import model.BOBouquet;
import model.BOEpg;
import model.BOEpgDetails;
import model.BOPids;
import model.BOSender;
import model.BOTimer;
import model.BOTimerList;

import java.util.logging.Logger;

import presentation.timer.GuiRecordTimerTableModel;
import presentation.timer.GuiSystemTimerTableModel;
import service.SerTimerHandler;
import control.ControlMain;
import control.timer.ControlTimerTab;

public abstract class SerBoxControl {
    
	protected Boolean isTvMode;
    public boolean newTimerAdded=true;
    public BOTimerList timerList;
    private BOTimer nextLocalRecordTimer;
    
    
    public BOTimer detectNextLocalRecordTimer(boolean newRead) {
        if (timerList != null && (newRead || nextLocalRecordTimer==null)) {
            nextLocalRecordTimer = timerList.getFirstLocalRecordTimer();
        }
        return nextLocalRecordTimer;
    }
    
    synchronized public BOTimerList getTimerList(boolean newRead) {
        if (newRead || timerList==null || newTimerAdded) {
            SerTimerHandler.deleteOldTimer();	
            
            timerList=SerTimerHandler.readLocalTimer();
            try {
                reReadTimerList();
            } catch (IOException e) {
                Logger.getLogger("SerBoxControl").warning(ControlMain.getProperty("err_read_timer"));
            }
            newTimerAdded=false;
        }
        return timerList;
    }

    public ArrayList senderList;
    public abstract ArrayList getAllSender() throws IOException;

	public abstract String getName();
    public abstract String getSptsStatus();
    public abstract boolean setSptsStatus(String status);
	protected abstract BOTimerList reReadTimerList() throws IOException;
	public abstract GregorianCalendar getBoxTime() throws IOException;
	public abstract BufferedReader getConnection(String request) throws IOException;
	public abstract BOPids getPids() throws IOException;
	public abstract ArrayList getBouquetList() throws IOException;
	public abstract ArrayList getSender(BOBouquet bouquet) throws IOException;
	public abstract String zapTo(String chanId) throws IOException;
	public abstract ArrayList getEpg(BOSender sender) throws IOException;
	public abstract BOEpgDetails getEpgDetail(BOEpg epg) throws IOException;
	public abstract String sendMessage(String message) throws IOException;
	public abstract String standbyBox(String modus) throws IOException;
	public abstract String shutdownBox() throws IOException;
	protected abstract BOTimerList readTimer() throws IOException;
	public abstract String writeTimer(BOTimer timer) throws IOException;
	public abstract String getChanIdOfRunningSender() throws IOException;
	public abstract boolean isTvMode() throws IOException;
	public abstract String setRadioTvMode(String mode) throws IOException;
	public abstract ArrayList getBoxVersion() throws IOException;
	public abstract BOSender getRunningSender() throws IOException;

	public abstract String setRecordModusWithPlayback() throws IOException;
	public abstract String setRecordModus() throws IOException;
	public abstract String stopRecordModus() throws IOException;
	public abstract String [] getRepeatOptions() throws IOException;
	public abstract String [][] getTimerType() throws IOException;
	public abstract GuiRecordTimerTableModel getRecordTimerTabelModel(ControlTimerTab ctrl);
	public abstract GuiSystemTimerTableModel getSystemTimerTabelModel(ControlTimerTab ctrl);
	public abstract String getIcon();

}