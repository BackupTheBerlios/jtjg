package service;
/*
SerTimerHandler.java by Geist Alexander

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import model.BOAfterRecordOptions;
import model.BOEpg;
import model.BOLocalTimer;
import model.BOTimer;
import model.BOTimerList;
import model.BOUdrecOptions;

import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import service.XML.SerXMLHandling;
import service.XML.SerXPathHandling;
import control.ControlMain;

public class SerTimerHandler {

    private static Document timerDocument;
    private static String timerFile = ControlMain.getSettingsPath().getWorkDirectory()+File.separator+"timer.xml";

    /**
     * @return LocalTimer
     * hole den ersten faelligen lokalen Box-Timer
     * vergleiche mit der Box-Zeit
     */
    public static BOLocalTimer getRunningLocalBoxTimer() {
        BOTimerList timerList = ControlMain.getBoxAccess().getTimerList(true);
        BOLocalTimer timer = timerList.getFirstLocalBoxRecordTimer();
        if (isValidTimer(timer)) {
            return timer;
        }
        return null;
    }

    private static boolean isValidTimer(BOLocalTimer timer) {
        try {
            GregorianCalendar boxTime = ControlMain.getBoxAccess().getBoxTime();
            if (boxTime.getTimeInMillis()-timer.getStartTime()>0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * entferne alle lokalen Timer deren Stopzeit kleiner Jetzt ist
     */
    public static void deleteOldTimer() {
        Element root = getTimerDocument().getRootElement();
        List nodes = SerXPathHandling.getNodes("/timerList/localTimer/stopTime", getTimerDocument());
        boolean edited = false;

        for (int i=0; i<nodes.size(); i++) {
            Node node = (Node) nodes.get(i);
            long localTimerStop=Long.parseLong(node.getText());
            long now = new GregorianCalendar().getTimeInMillis();
            
            if (now>localTimerStop) {
                Node localTimer = node.getParent();
                Element repeatId = (Element)localTimer.selectSingleNode("mainTimer/eventRepeatId");
                if (repeatId !=null && repeatId.getText().equals("0")) {
                    root.remove(localTimer); 
                    edited=true;    
                }
            }
        }
        if (edited) {
            try {
                SerXMLHandling.saveXMLFile(new File(timerFile), getTimerDocument());
            } catch (IOException e) {
                Logger.getLogger("SerTimerHandler").warning(e.getMessage());
            }
        }
    }

    private static void deleteLocalTimer(BOLocalTimer timer) {
        if (timer.getTimerNode()!=null) {
            try {
                getTimerDocument().getRootElement().remove(timer.getTimerNode());
                SerXMLHandling.saveXMLFile(new File(timerFile), getTimerDocument());
            } catch (IOException e) {
                Logger.getLogger("SerTimerHandler").warning(e.getMessage());
            }
        }
    }

    /*
     * speichere Timer lokal, lege Element "MainTimer" an
     * wenn Timer ausschliesslich lokal gespeichert wird
     */
    private static void saveNewTimer(BOLocalTimer timer) {
		Element root = getTimerDocument().getRootElement();

        //BOLocalTimer
		Element localTimer = DocumentHelper.createElement("localTimer");
		localTimer.addElement("startPX").addText(Boolean.toString(timer.getAfterRecordOptions().isUseProjectX()));
		localTimer.addElement("startMplex").addText(Boolean.toString(timer.getAfterRecordOptions().isUseMplex()));
		localTimer.addElement("mplexOption").addText(Integer.toString(timer.getAfterRecordOptions().getMplexOption()));
		localTimer.addElement("recordAllPids").addText(Boolean.toString(timer.isRecordAllPids()));
		localTimer.addElement("ac3ReplaceStereo").addText(Boolean.toString(timer.isAc3ReplaceStereo()));
		localTimer.addElement("stereoReplaceAc3").addText(Boolean.toString(timer.isStereoReplaceAc3()));
		localTimer.addElement("shutdownAfterRecord").addText(Boolean.toString(timer.isShutdownAfterRecord()));
		localTimer.addElement("description").addText(timer.getDescription());
		localTimer.addElement("udrecOptions").addText(timer.getUdrecOptions().toString());
		localTimer.addElement("savePath").addText(timer.getSavePath());
		localTimer.addElement("jgrabberStreamType").addText(timer.getJgrabberStreamType());
        localTimer.addElement("vlcStreamType").addText(timer.getVlcStreamType());
		localTimer.addElement("udrecStreamType").addText(timer.getUdrecStreamType());
		localTimer.addElement("streamingEngine").addText(Integer.toString(timer.getStreamingEngine()));
		localTimer.addElement("storeLogAfterRecord").addText(Boolean.toString(timer.isStoreLogAfterRecord()));
		localTimer.addElement("storeEpg").addText(Boolean.toString(timer.isStoreEPG()));
		localTimer.addElement("recordVtxt").addText(Boolean.toString(timer.isRecordVtxt()));
		localTimer.addElement("stopPlaybackAtRecord").addText(Boolean.toString(timer.isStopPlaybackAtRecord()));
		localTimer.addElement("dirPattern").addText(timer.getDirPattern());
		localTimer.addElement("filePattern").addText(timer.getFilePattern());
		localTimer.addElement("startTime").addText(timer.getMainTimer().getLongStartTime());
        localTimer.addElement("stopTime").addText(timer.getMainTimer().getLongStopTime());
		localTimer.addElement("local").addText(Boolean.toString(timer.isLocal()));
		
        if (timer.isLocal()) {
//          BOTimer
            Element mainTimer = DocumentHelper.createElement("mainTimer");
            mainTimer.addElement("startMainTimer").addText(timer.getMainTimer().getLongStartTime());
            mainTimer.addElement("stopMainTimer").addText(timer.getMainTimer().getLongStopTime());
            mainTimer.addElement("announceMainTimer").addText(timer.getMainTimer().getAnnounceTime());
            mainTimer.addElement("channelId").addText(timer.getMainTimer().getChannelId());
            mainTimer.addElement("eventTypeId").addText(timer.getMainTimer().getEventTypeId());
            mainTimer.addElement("eventRepeatId").addText(timer.getMainTimer().getEventRepeatId());
            mainTimer.addElement("repeatCount").addText(timer.getMainTimer().getRepeatCount());
            mainTimer.addElement("senderName").addText(timer.getMainTimer().getSenderName());

            localTimer.add(mainTimer);
        }
		root.add(localTimer);
		try {
            SerXMLHandling.saveXMLFile(new File(timerFile), getTimerDocument());
            timer.setTimerNode(localTimer);
        } catch (IOException e) {
            Logger.getLogger("SerTimerHandler").warning(e.getMessage());
        }
    }

    private static void editOldTimer(BOLocalTimer timer) {
        Node timerNode = timer.getTimerNode();
        timerNode.selectSingleNode("ac3ReplaceStereo").setText(Boolean.toString(timer.isAc3ReplaceStereo()));
        timerNode.selectSingleNode("description").setText(timer.getDescription());
        timerNode.selectSingleNode("dirPattern").setText(timer.getDirPattern());
        timerNode.selectSingleNode("filePattern").setText(timer.getFilePattern());
        timerNode.selectSingleNode("jgrabberStreamType").setText(timer.getJgrabberStreamType());
        timerNode.selectSingleNode("vlcStreamType").setText(timer.getVlcStreamType());
        timerNode.selectSingleNode("recordAllPids").setText(Boolean.toString(timer.isRecordAllPids()));
        timerNode.selectSingleNode("recordVtxt").setText(Boolean.toString(timer.isRecordVtxt()));
        timerNode.selectSingleNode("savePath").setText(timer.getSavePath());
        timerNode.selectSingleNode("shutdownAfterRecord").setText(Boolean.toString(timer.isShutdownAfterRecord()));
        timerNode.selectSingleNode("startPX").setText(Boolean.toString(timer.getAfterRecordOptions().isUseProjectX()));
        timerNode.selectSingleNode("startMplex").setText(Boolean.toString(timer.getAfterRecordOptions().isUseMplex()));
        timerNode.selectSingleNode("mplexOption").setText(Integer.toString(timer.getAfterRecordOptions().getMplexOption()));
        timerNode.selectSingleNode("startTime").setText(timer.getMainTimer().getLongStartTime());
        timerNode.selectSingleNode("stopTime").setText(timer.getMainTimer().getLongStopTime());
        timerNode.selectSingleNode("stereoReplaceAc3").setText(Boolean.toString(timer.isStereoReplaceAc3()));
        timerNode.selectSingleNode("stopPlaybackAtRecord").setText(Boolean.toString(timer.isStopPlaybackAtRecord()));
        timerNode.selectSingleNode("storeEpg").setText(Boolean.toString(timer.isStoreEPG()));
        timerNode.selectSingleNode("storeLogAfterRecord").setText(Boolean.toString(timer.isStoreLogAfterRecord()));
        timerNode.selectSingleNode("streamingEngine").setText(Integer.toString(timer.getStreamingEngine()));
        timerNode.selectSingleNode("udrecOptions").setText(timer.getUdrecOptions().toString());
        timerNode.selectSingleNode("udrecStreamType").setText(timer.getUdrecStreamType());
        timerNode.selectSingleNode("local").setText(Boolean.toString(timer.isLocal()));

        if (timer.isLocal()) {
//          BOTimer
            Element mainTimer = (Element)timerNode.selectSingleNode("mainTimer");
            mainTimer.selectSingleNode("startMainTimer").setText(timer.getMainTimer().getLongStartTime());
            mainTimer.selectSingleNode("stopMainTimer").setText(timer.getMainTimer().getLongStopTime());
            mainTimer.selectSingleNode("announceMainTimer").setText(timer.getMainTimer().getAnnounceTime());
            mainTimer.selectSingleNode("channelId").setText(timer.getMainTimer().getChannelId());
            mainTimer.selectSingleNode("eventTypeId").setText(timer.getMainTimer().getEventTypeId());
            mainTimer.selectSingleNode("eventRepeatId").setText(timer.getMainTimer().getEventRepeatId());
            mainTimer.selectSingleNode("repeatCount").setText(timer.getMainTimer().getRepeatCount());
            mainTimer.selectSingleNode("senderName").setText(timer.getMainTimer().getSenderName());
        }
		try {
            SerXMLHandling.saveXMLFile(new File(timerFile), getTimerDocument());
        } catch (IOException e) {
            Logger.getLogger("SerTimerHandler").warning(e.getMessage());
        }
    }

    /**
     * @param Main-Timer
     * @return Local-Timer
     * sucht den passenden lokalen BoxTimer in XML-Datenbank
     * wenn keiner gefunden Standard-Lokal-Timer zurückgeben
     */
    public static BOLocalTimer findLocalTimer(BOTimer timer) {
        Node timerNode = findTimerNode(timer);
        if (timerNode != null) {
            return buildLocalTimer(timerNode, new BOLocalTimer(timer));
        }
        return BOLocalTimer.getDefaultLocalTimer(timer);
    }

    /**
     * @param mainTimer
     * @return XML-Timer-Node
     * Sucht anhand des Start-Datums die passende XML-Node
     */
    public static Node findTimerNode(BOTimer mainTimer) {
        List nodes = SerXPathHandling.getNodes("/timerList/localTimer/startTime", getTimerDocument());
        long mainTimerStart=mainTimer.getUnformattedStartTime().getTimeInMillis();
		for (int i = 0; i<nodes.size(); i++) {
		    Node node = (Node) nodes.get(i);
		    long localTimerStart=Long.parseLong(node.getText());
		    if (mainTimerStart==localTimerStart) {
		        return node.getParent();
		    }
		}
		return null;
    }

    private static BOTimer buildMainTimer(Node mainTimerNode) {
        BOTimer botimer = new BOTimer();

        botimer.eventTypeId=mainTimerNode.selectSingleNode("eventTypeId").getText();
        botimer.eventRepeatId=mainTimerNode.selectSingleNode("eventRepeatId").getText();
        botimer.repeatCount=mainTimerNode.selectSingleNode("repeatCount").getText();
        botimer.channelId=mainTimerNode.selectSingleNode("channelId").getText();
        botimer.setSenderName(mainTimerNode.selectSingleNode("senderName").getText());
        botimer.announceTime=mainTimerNode.selectSingleNode("announceMainTimer").getText();

        long startMillis = Long.parseLong(mainTimerNode.selectSingleNode("startMainTimer").getText());
        GregorianCalendar startTime = new GregorianCalendar();
        startTime.setTimeInMillis(startMillis);

        long stopMillis = Long.parseLong(mainTimerNode.selectSingleNode("stopMainTimer").getText());
        GregorianCalendar stopTime = new GregorianCalendar();
        stopTime.setTimeInMillis(stopMillis);

        botimer.unformattedStartTime=startTime;
        botimer.unformattedStopTime=stopTime;

        return botimer;
    }

    public static BOLocalTimer buildLocalTimer(Node timerNode, BOLocalTimer localTimer) {
    	BOAfterRecordOptions afterRecordOptions = new BOAfterRecordOptions();
        afterRecordOptions.setUseProjectX(timerNode.selectSingleNode("startPX").getText().equals("true"));
        afterRecordOptions.setUseMplex(timerNode.selectSingleNode("startMplex").getText().equals("true"));
        afterRecordOptions.setMplexOption(Integer.parseInt(timerNode.selectSingleNode("mplexOption").getText()));
        localTimer.setAfterRecordOptions(afterRecordOptions);
    	
        localTimer.setAc3ReplaceStereo(timerNode.selectSingleNode("ac3ReplaceStereo").getText().equals("true"));
        localTimer.setDescription(timerNode.selectSingleNode("description").getText());
        localTimer.setDirPattern(timerNode.selectSingleNode("dirPattern").getText());
        localTimer.setFilePattern(timerNode.selectSingleNode("filePattern").getText());
        localTimer.setJgrabberStreamType(timerNode.selectSingleNode("jgrabberStreamType").getText());
        localTimer.setVlcStreamType(timerNode.selectSingleNode("vlcStreamType").getText());
        localTimer.setRecordAllPids(timerNode.selectSingleNode("recordAllPids").getText().equals("true"));
        localTimer.setRecordVtxt(timerNode.selectSingleNode("recordVtxt").getText().equals("true"));
        localTimer.setSavePath(timerNode.selectSingleNode("savePath").getText());
        localTimer.setShutdownAfterRecord(timerNode.selectSingleNode("shutdownAfterRecord").getText().equals("true"));
        localTimer.setStartTime(Long.parseLong(timerNode.selectSingleNode("startTime").getText()));
        localTimer.setStopTime(Long.parseLong(timerNode.selectSingleNode("stopTime").getText()));
        localTimer.setStereoReplaceAc3(timerNode.selectSingleNode("stereoReplaceAc3").getText().equals("true"));
        localTimer.setStopPlaybackAtRecord(timerNode.selectSingleNode("stopPlaybackAtRecord").getText().equals("true"));
        localTimer.setStoreEPG(timerNode.selectSingleNode("storeEpg").getText().equals("true"));
        localTimer.setStoreLogAfterRecord(timerNode.selectSingleNode("storeLogAfterRecord").getText().equals("true"));
        localTimer.setStreamingEngine(Integer.parseInt(timerNode.selectSingleNode("streamingEngine").getText()));
        localTimer.setUdrecOptions(new BOUdrecOptions(timerNode.selectSingleNode("udrecOptions").getText().split(" ")));
        localTimer.setUdrecStreamType(timerNode.selectSingleNode("udrecStreamType").getText());
        localTimer.setLocal(timerNode.selectSingleNode("local").getText().equals("true"));
        localTimer.setTimerNode(timerNode);
        return localTimer;
    }

    /**
     * @return Returns the timerDocument.
     * gibt das XML-Document der Localen Timer
     * erstellt ein leeres wenn noch keines vorhanden
     */
    private static Document getTimerDocument() {
        try {
            if (timerDocument==null) {
                File listFile = new File(timerFile);
                if (!listFile.exists()) {
                    timerDocument=SerXMLHandling.createEmptyTimerFile(listFile);
                } else {
                    timerDocument=SerXMLHandling.readDocument(new File(timerFile));
                }
            }
        } catch (Exception e) {
            Logger.getLogger("SerTimerHandler").warning(e.getMessage());
        }
        return timerDocument;
    }

    /**
     * @param reqTimer
     * @param validate
     * @param reloadList
     * 
     * zentrale Methode um neue/geänderte Timer zu speichern
     * validate=true, wenn Timer auf Überschneidungen überprüft werden soll
     * reloadList=true, wenn die TimerListe neu gelesen werden soll
     */
    public static boolean saveTimer(BOTimer reqTimer, boolean validate, boolean reloadList) {
        //prüfen neuer und modifizerter Aufnahme-Timer auf Dubletten
        if (reqTimer.getEventTypeId().equals("5") &&
                (reqTimer.getModifiedId()!=null && !reqTimer.getModifiedId().equals("remove")) && !reqTimer.getModifiedId().equals("localModify") &&
                    ControlMain.getBoxAccess().getTimerList(false).getRecordTimerList().contains(reqTimer) ) {
            reqTimer.setModifiedId(null);
            return false; //Timer ist eine Dublette, nicht speichern
        }
            
        if (validate && reqTimer.isNewOrModified()) { 
            validateTimer(reqTimer, reloadList);
            return false;
        } else {
            //lokaler Teil muss immer gespeichert werden
            saveLocalTimer(reqTimer); 
              
            if (reqTimer.getModifiedId()!=null && !reqTimer.getLocalTimer().isLocal()) {  //nur neue|modifizierte Box-Timer speichern
                saveBoxTimer(reqTimer, reloadList); 
            }

            if (reqTimer.getModifiedId() != null) {
                //nur bei lokalen Timern manuell erledigen, Box-Timer muessen automatisch nachgelesen werden
                if (reqTimer.getModifiedId().equals("new") && reqTimer.getLocalTimer().isLocal()) {
                    ControlMain.getBoxAccess().getTimerList(false).getRecordTimerList().add(reqTimer);
                } else if (reqTimer.getModifiedId().equals("remove")) {
                    ControlMain.getBoxAccess().getTimerList(false).getRecordTimerList().remove(reqTimer);
                }
            } 
            reqTimer.setModifiedId(null);
            //ermittle naechsten faelligen lokalen-RecordTimer neu
            if (reqTimer.getLocalTimer().isLocal()) {
                ControlMain.getBoxAccess().detectNextLocalRecordTimer(true);   
            }            
            reqTimer.setModifiedId(null);
            return true;
        }
    }
    
    /*
     * Überprüfe Timer auf Ueberschneidungen
     * Falls Überschneidungn vorhanden, starte Timer-Afrage-Dialog
     */
    private static boolean validateTimer(BOTimer requestedTimer, boolean reloadList) {
        ArrayList timerList = ControlMain.getBoxAccess().getTimerList(false).getRecordTimerList();
        
        if (timerList.size()>0) {
            ArrayList equalTimer = new ArrayList();

            for (int i=0; i<timerList.size(); i++) {
                BOTimer timer = (BOTimer)timerList.get(i);
                if (!timer.equals(requestedTimer) && SerHelper.compareTimerTime(timer, requestedTimer)) { //Timerueberschneidung
                    equalTimer.add(timer);
                }
            }
            if (equalTimer.size()>0) {
                equalTimer.add(0, requestedTimer);
                startTimerQuestDialag(equalTimer);
                return false;
            } else {
                saveTimer(requestedTimer, false, reloadList);
                return true;
            }
        } else {
            saveTimer(requestedTimer, false, reloadList);
            return true;
        }
	}
    
    private static void startTimerQuestDialag(ArrayList equalTimer) {
    	JList list = new JList(equalTimer.toArray());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		int ret = JOptionPane.showOptionDialog(
            ControlMain.getControl().getView(), 
            new Object[]{
                ControlMain.getProperty("msg_choosePlayback2"), 
                new JScrollPane(list)
            },
            ControlMain.getProperty("msg_choose"),
            0,
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            new String[]{
                ControlMain.getProperty("button_cancel"),
                ControlMain.getProperty("button_recordAll"),
                ControlMain.getProperty("button_ok")
            },
            ControlMain.getProperty("button_ok")
		);
		if (ret == 2) { //speichern des selektierten Timers, löschen der restlichen Timer
			if (list.getSelectedIndex()>=0) {
                BOTimer selectedTimer = (BOTimer)list.getSelectedValue();
                equalTimer.remove(list.getSelectedIndex());
                for (int i=0; i<equalTimer.size(); i++) {
                    BOTimer timer = (BOTimer)equalTimer.get(i);
                    timer.setModifiedId("remove");
                    saveTimer(timer, false, !timer.getLocalTimer().isLocal());
                }
                saveTimer(selectedTimer, false, !selectedTimer.getLocalTimer().isLocal());
			}
		} else if (ret == 1) { //speichern aller Timer
            for (int i=0; i<equalTimer.size(); i++) {
                BOTimer timer = (BOTimer)equalTimer.get(i);
                timer.setModifiedId("new");
                saveTimer(timer, false, !timer.getLocalTimer().isLocal());
            }
        }
    }

    /*
     * Speichert Box-Timer
     * Liste muss neu gelesen werden, wenn ein neuer Box-Timer gespeichert wird
     *
     * Parameter reloadList wird benoetigt damit bei mehreren neuen Timern
     * die Liste nicht sofort neu gelesen wird, sondern nur nach dem letzten
     */
    private static void saveBoxTimer (BOTimer timer, boolean reloadList) {
        try {
            if (timer.getModifiedId().equals("new")) {
                ControlMain.getBoxAccess().writeTimer(timer);
                if (reloadList) {
                    ControlMain.getBoxAccess().getTimerList(true);
                }
            } else {
                ControlMain.getBoxAccess().writeTimer(timer);
            }
        } catch (IOException e) {
            Logger.getLogger("SerTimerHandler").warning(e.getMessage());
        }
    }

    private static int saveLocalTimer(BOTimer timer) {
        if (timer.getModifiedId() !=null && timer.getModifiedId().equals("remove")) {
            if (timer.localTimer==null) {
                return 0;
            }
            deleteLocalTimer(timer.getLocalTimer());
            return 0;
        }

        if (timer.localTimer==null) {
            BOLocalTimer.getDefaultLocalTimer(timer);
        }
        if (timer.getLocalTimer().getTimerNode()==null){
            saveNewTimer(timer.getLocalTimer());
        } else {
            editOldTimer(timer.getLocalTimer());
        }
        return 1;
    }

    /*
     * liest MainTimer und LocalTimer
     */
    public static BOTimerList readLocalTimer() {
        BOTimerList list = new BOTimerList();
        Element root = getTimerDocument().getRootElement();
        List nodes = SerXPathHandling.getNodes("/timerList/localTimer/mainTimer", getTimerDocument());

        for (int i=0; i<nodes.size(); i++) {
            Node mainTimerNode = (Node) nodes.get(i);
            Node localTimerNode =  mainTimerNode.getParent();

            BOTimer timer = buildMainTimer(mainTimerNode);
            buildLocalTimer(localTimerNode, new BOLocalTimer(timer));
            list.getRecordTimerList().add(timer);
        }
        return list;
    }
    
    /*
	 * zentrale Methode um neue/geänderte Timer zu speichern
	 */
	public static void saveSystemTimer(BOTimer timer, boolean reloadList) {
		//nur veraenderte Timer speichern
		if (timer.getModifiedId() != null) {
			saveBoxTimer(timer, reloadList);
		}
		timer.setModifiedId(null);
	}
    
    /**
     * @param epg
     * @return BOTimer 
     * Erstellen eines BOTimer-Objekts aus den EPG-Informationen
     */
    public static BOTimer buildTimer(BOEpg epg) {
        BOTimer timer = new BOTimer();

        int timeBefore = Integer.parseInt(ControlMain.getSettings().getRecordSettings().getRecordTimeBefore()) * 60;
        int timeAfter = Integer.parseInt(ControlMain.getSettings().getRecordSettings().getRecordTimeAfter()) * 60;
        long unformattedStart = Long.parseLong(epg.getUnformattedStart());
        long unformattedDuration = Long.parseLong(epg.getUnformattedDuration());
        long endtime = unformattedStart + unformattedDuration;
        long announce = unformattedStart - (120 + timeBefore);

        timer.setModifiedId("new");
        timer.setChannelId(epg.getSender().getChanId());
        timer.setSenderName(epg.getSender().getName());
        timer.setAnnounceTime(Long.toString(announce)); //Vorwarnzeit
        timer.unformattedStartTime=SerFormatter.formatUnixDate(unformattedStart - timeBefore);
        timer.unformattedStopTime=SerFormatter.formatUnixDate(endtime + timeAfter);

        timer.setEventRepeatId("0");
        timer.setEventTypeId("5");
        timer.setDescription(epg.getTitle());
        return timer;
    }
}