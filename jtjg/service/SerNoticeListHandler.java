package service;
/*
SerNoticeListHandler.java by Geist Alexander

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
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.BOEpg;
import model.BONoticeBroadcast;
import control.ControlMain;


public class SerNoticeListHandler {
	
    private static String noticeFile = ControlMain.getSettingsPath().getWorkDirectory()+File.separator+"notice.xml";    
    private static ArrayList noticeList;
    
    public static void containsNotice(ArrayList epgList) {
        if (epgList.size()>0) { 
            for (int i=0; i<getNoticeList().size(); i++) {//Schleife ueber die EPG-Liste
                BONoticeBroadcast notice = (BONoticeBroadcast)getNoticeList().get(i);
                
                if (notice.isSearchEpg()) { 
                    for (int i2=0; i2<epgList.size(); i2++) { //Schleife über die Suchtexte
                        BOEpg epg = (BOEpg)epgList.get(i2);
                        
                        if (notice.getSearchString()!=null) {
                            //Suche im Titel
                            if (epg.getTitle().indexOf(notice.getSearchString())>=0) {
                                if (notice.isBuildTimer()) {
                                    SerTimerHandler.saveTimer(SerTimerHandler.buildTimer(epg), false, true);
                                    break;    
                                } else {
                                    askForBuildTimer(epg, notice);
                                    break;
                                }
                            }
                            
                            //Suche in den EPG-Details
                            if (!notice.isSearchOnlyTitle()) { //Suche in den EPG-Details
                                if (epg.getEpgDetail().getText().indexOf(notice.getSearchString())>=0) {
                                    if (notice.isBuildTimer()) {
                                        SerTimerHandler.saveTimer(SerTimerHandler.buildTimer(epg), false, true);
                                        break;    
                                    } else {
                                        askForBuildTimer(epg, notice);
                                        break;
                                    }
                                }    
                            }    
                        }
                    }    
                }
            }    
        }
    }
    
    private static void askForBuildTimer(BOEpg epg, BONoticeBroadcast notice) {
        int result = JOptionPane.showConfirmDialog(
                ControlMain.getControl().getView(),
                ControlMain.getProperty("msg_builTimer2")+"\n"+'"'+notice.getSearchString()+'"'+"\n\n"+epg.toString(),
                ControlMain.getProperty("msg_buildTimer"),
                JOptionPane.YES_NO_OPTION
        );
        if (result==0) {
            SerTimerHandler.saveTimer(SerTimerHandler.buildTimer(epg), false, true);
        }
    }
    
    public static void readNoticeList() {
        try {
            XMLDecoder dec = new XMLDecoder(new FileInputStream(noticeFile));
            noticeList = (ArrayList) dec.readObject();
        } catch (FileNotFoundException e) {
            noticeList=new ArrayList();
        }
    }
    
    public static void saveNoticeList(ArrayList list) {
        try {
            XMLEncoder dec = new XMLEncoder(new FileOutputStream(new File(noticeFile)));
            dec.writeObject(list);
            dec.flush();
            dec.close();
        } catch (FileNotFoundException e) {}
    }

    /**
     * @return Returns the noticeList.
     */
    public static ArrayList getNoticeList() {
        if (noticeList==null) {
            readNoticeList();
        }
        return noticeList;
    }
}
