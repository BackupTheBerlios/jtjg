package control;
/*
ControlStartTab.java by Geist Alexander 

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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import model.BOSender;
import model.BOTimer;
import model.BOTimerList;
import presentation.GuiLogWindow;
import presentation.GuiMainView;
import service.SerNewsHandler;


public class ControlStartTab extends ControlTab implements ActionListener {
	
	GuiMainView mainView;
	
	public ControlStartTab(GuiMainView view ) {
		this.setMainView(view);
	}

	public void run() {
	    this.getMainView().getTabStart().getPaneVersion().setText(this.getVersion());
	    this.getMainView().getTabStart().getPaneWarns().setText(this.checkWarns());
	    this.getMainView().getTabStart().getLabelRunningSender().setText("Sender: "+getRunningSender());
	    new SerNewsHandler(this.getMainView().getTabStart().getPaneNews()).start();
        this.getMainView().getTabStart().getLabelNextRecord().setText(
                ControlMain.getProperty("label_nextTimer")+getNextTimerInfo());
	}
	
	/**
	 * Klick-Events der Buttons
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action == "switchLog") {
		    GuiLogWindow.switchLogVisiblity();
		}
	}
	
	public String getVersion() {
	    String version = new String("<html>");
	    try {
            ArrayList result = ControlMain.getBoxAccess().getBoxVersion();
            for (int i=0; i<result.size(); i++) {
                version=version+this.getHtmlString((String)result.get(i), "black");
            }
        } catch (IOException e) {
            return version;
        }
        return version+"</html>";
	}
	
	public String checkWarns() {
	    File udrec;
	    String warnText=new String("<html>");
	    File save = new File(ControlMain.getSettingsPath().getSavePath());
	    try {
			if (ControlMain.getSettingsPath().getUdrecPath().substring(0,4).equalsIgnoreCase("mono")) {
			    udrec = new File(ControlMain.getSettingsPath().getUdrecPath().substring(5));
			} else {
			    udrec = new File(ControlMain.getSettingsPath().getUdrecPath());
			}
		} catch (StringIndexOutOfBoundsException e) {
			udrec=null;
		}
	    File px = new File(ControlMain.getSettingsPath().getProjectXPath());
	    File shutdown = new File(ControlMain.getSettingsPath().getShutdownToolPath());
	    File vlc = new File(ControlMain.getSettingsPath().getVlcPath());
        File mplex = new File(ControlMain.getSettingsPath().getMplexPath());
	    int boxCount = ControlMain.getSettingsMain().getBoxList().size();
	    int playerCount = ControlMain.getSettingsPlayback().getPlaybackOptions().size();
	    
	    if (!save.exists()) {
	        warnText=warnText+this.getHtmlString(ControlMain.getProperty("warn_save"), "red");
	    }

        if (udrec==null || !udrec.exists()) {
            warnText=warnText+this.getHtmlString(ControlMain.getProperty("warn_udrec"), "red");
	    }
        
        if (!vlc.exists()) {
            warnText=warnText+this.getHtmlString(ControlMain.getProperty("warn_vlc"), "red");
	    }
	    
	    if (mplex!= null && !mplex.exists()) {
	        warnText=warnText+this.getHtmlString(ControlMain.getProperty("warn_mplex"), "red");
	    }
	    
        if (!px.exists()) {
            warnText=warnText+this.getHtmlString(ControlMain.getProperty("warn_px"), "red");
        }
        
	    if (!shutdown.exists()) {
	        warnText=warnText+this.getHtmlString(ControlMain.getProperty("warn_shutdown"), "red");
	    }
	    
	    if (boxCount==0) {
	        warnText=warnText+this.getHtmlString(ControlMain.getProperty("warn_box"), "red");
	    }
	    
	    if (playerCount==0) {
	        warnText=warnText+this.getHtmlString(ControlMain.getProperty("warn_player"), "red");
	    }
  
	    return warnText+"</html>";
	}
	
	public String getHtmlString(String string, String color) {
	    return "<font color="+color+">"+string+"</font><br>";
	}
	
	public String getRunningSender() {
	    try {
	        BOSender sender = ControlMain.getBoxAccess().getRunningSender();
	        if (sender !=null) {
	            return sender.getName();    
	        }
        } catch (IOException e) {
            return new String();
        }
        return new String();
	}
	
	public String getNextTimerInfo() {
	    BOTimerList list = ControlMain.getBoxAccess().getTimerList(false);
	    BOTimer timer = list.getFirstRecordTimer();
	    if (timer!=null) {
	        return timer.getStartTime()+"    Sender:"+timer.getSenderName();    
	    }
        return new String();
	}
	
	/**
	 * @return Returns the mainView.
	 */
	public GuiMainView getMainView() {
		return mainView;
	}
	/**
	 * @param mainView The mainView to set.
	 */
	public void setMainView(GuiMainView mainView) {
		this.mainView = mainView;
	}
}
