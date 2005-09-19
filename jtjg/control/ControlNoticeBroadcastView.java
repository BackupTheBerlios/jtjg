package control;
/*
ControlNoticeBroadcastView.java by Geist Alexander 

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import model.BONoticeBroadcast;
import model.BOSender;
import presentation.noticeBroadcast.GuiNoticeBroadcastView;
import service.SerAlertDialog;
import service.SerNoticeListHandler;


public class ControlNoticeBroadcastView implements ActionListener {
	
	ArrayList noticeList;
    GuiNoticeBroadcastView noticeView;
    public Thread scanThread;
    public boolean runningScan=false;

	public ControlNoticeBroadcastView(ArrayList list) {
		this.setNoticeList(list);
        noticeView=new GuiNoticeBroadcastView(this);
        noticeView.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action == "delete") {
            this.actionRemoveNoticeEntry();
        }
        else if (action == "add") {
            this.actionAddNoticeEntry();
        }
        else if (action == "scan") {
            this.actionScan();
        }
     }
    
    private void actionScan() {
        if (!runningScan && noticeList.size()>0) {
            runningScan=true;
            scanThread = new Thread() {
                public void run() {
                    try {
                        ArrayList senderList = ControlMain.getBoxAccess().getAllSender();
                        getNoticeView().getProgressScan().setMaximum(senderList.size());

                        for (int i=0; i<senderList.size(); i++) {
                            if(runningScan) {
                                SerNoticeListHandler.containsNotice(((BOSender)senderList.get(i)).readEpg());
                                getNoticeView().getProgressScan().setValue(i+1);
                                System.out.println(i);
                            } else {
                                break;
                            }
                        }
                        runningScan=false;
                    } catch (IOException e) {
                        SerAlertDialog.alertConnectionLost("ControlNoticeBroadcastView", getNoticeView());
                        runningScan=false;
                    }        
                } 
            };
            scanThread.start();
        }
    }

	private void actionAddNoticeEntry() {
        this.getNoticeList().add(new BONoticeBroadcast());
        this.getNoticeView().noticeTableModel.fireTableDataChanged();
    }
    
    private void actionRemoveNoticeEntry() {
        int index = this.getNoticeView().getJTableNoticeList().getSelectedRow();
        this.getNoticeList().remove(index);
        this.getNoticeView().noticeTableModel.fireTableDataChanged();
    }
     
	/**
	 * @return Returns the noticeList.
	 */
	public ArrayList getNoticeList() {
		return noticeList;
	}
	/**
	 * @param noticeList The noticeList to set.
	 */
	public void setNoticeList(ArrayList noticeList) {
		this.noticeList = noticeList;
	}
    /**
     * @return Returns the noticeView.
     */
    public GuiNoticeBroadcastView getNoticeView() {
        return noticeView;
    }
}
