package streaming;
/*
VlcRecord.java by Geist Alexander 

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
import java.util.ArrayList;

import java.util.logging.Logger;

import model.BOExternalProcess;
import model.BOPid;
import model.BORecordArgs;
import service.SerExternalProcessHandler;
import control.ControlMain;

public class VlcRecord  extends Record {

	BORecordArgs recordArgs;
	RecordControl recordControl;
	String boxIp;
	boolean running = true;
    File recordFile;
	BOExternalProcess run;
	
	public VlcRecord(BORecordArgs args, RecordControl control){
        recordControl = control;
        recordArgs = args;
        streamType="TS";
        boxIp = ControlMain.getBoxIpOfActiveBox();
        recordFile=new File(recordControl.getDirectory(), recordControl.getFileName()+".mpg");
	}
	
	private String[] getRequestArray() {
	    String[] execString = new String[3];
        //Pfad zu VLC
        execString[0]=ControlMain.getSettingsPath().getVlcPath();
        
        //Quelle
        String pidString = new String();
        for (int i=0; i<recordArgs.getPids().getAPids().size(); i++) {
            String aPid = "0x"+((BOPid)recordArgs.getPids().getAPids().get(i)).getNumber()+",";
            pidString=pidString+aPid;
        }
        pidString=pidString.substring(0, pidString.length()-1);
        try {
            execString[1]="http://"+boxIp+":31339/0,0x"+recordArgs.getPids().getPmtPid().getNumber()+",0x";    
        } catch (NullPointerException ex) {Logger.getLogger("VlcRecord").warning("PMT-Pid not available!");}
        
        execString[1]=execString[1]+recordArgs.getPids().getVPid().getNumber()+","+pidString;
        
        //Ziel
        execString[2]=":sout=#duplicate{dst=std{access=file,mux=ps,url="+
        recordFile.getAbsolutePath()+"},}";

        return execString;
	}
	
	public void start() {
	    run=(SerExternalProcessHandler.startProcess(recordControl, "vlcRecord",  this.getRequestArray(), true));
	}	
	
	public void stop() {
	    if (run != null && run.getProcess()!=null) {
	        run.getProcess().destroy();  
	    }
	}
	/**
     * @return Returns the writeStream.
     */
    public DataWriteStream[] getWriteStream() {
        return null;
    }
	
	public ArrayList getFiles() {
	    ArrayList files = new ArrayList();
        files.add(this.recordFile);
        return files;
	}
}
