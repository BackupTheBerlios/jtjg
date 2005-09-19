package streaming;
/*
UdrecRecord.java by Geist Alexander 

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
import java.io.PrintWriter;
import java.util.ArrayList;

import model.BOExternalProcess;
import model.BOPid;
import model.BOPids;
import model.BORecordArgs;
import service.SerExternalProcessHandler;
import service.SerHelper;
import control.ControlMain;

public class UdrecRecord  extends Record {

	BORecordArgs recordArgs;
	int spktBufNum = 16;
	RecordControl recordControl;
	String boxIp;
	boolean running = true;
	BOExternalProcess run;
	
	public UdrecRecord(BORecordArgs args, RecordControl control){
        recordControl = control;
        recordArgs = args;
        streamType=recordArgs.getLocalTimer().getShortUdrecStreamType();
        boxIp = ControlMain.getBoxIpOfActiveBox();
	}
	
	private String[] getRequestArray() {
	    ArrayList cmd = new ArrayList();
	    BOPids pids = recordArgs.getPids();
	    String cmdReturn[];
	    if (ControlMain.getSettingsPath().getUdrecPath().substring(0,4).equalsIgnoreCase("mono")) {
	    	cmd.add("mono");
	    	cmd.add(ControlMain.getSettingsPath().getUdrecPath().substring(5));
	    } else {
	    	cmd.add(ControlMain.getSettingsPath().getUdrecPath());
	    }
	    cmd.add("-host");
	    cmd.add(boxIp);
	    cmd.add("-now");
	    cmd.add("-"+streamType);
	    cmd.add("-o");
	    cmd.add(new File(recordControl.getDirectory(), recordControl.getFileName()).getAbsolutePath());
	    cmd.addAll(recordArgs.getLocalTimer().getUdrecOptions().toStringList());
	    
	    if (recordArgs.getPids().getVPid() != null) {
		    cmd.add("-vp");
		    cmd.add(pids.getVPid().getNumber());
		}
		for (int i=0; i<recordArgs.getPids().getAPids().size(); i++){
		    String aPid = ((BOPid)pids.getAPids().get(i)).getNumber();
		    cmd.add("-ap");
		    cmd.add(aPid);
		}
		if (recordArgs.getPids().getVtxtPid() != null) {
		    cmd.add("-ap");
		    cmd.add(pids.getVtxtPid().getNumber());
		} 
        if (!streamType.equals("es")) { //-idd nur bei ES
            cmd.remove("-idd");
        }
		cmdReturn = new String[cmd.size()];
		cmd.toArray(cmdReturn);
		return cmdReturn;
	}
	
	public void start() {
	    run=(SerExternalProcessHandler.startProcess("udrec",  this.getRequestArray(), true));
	}	
	
	public void stop() {
	    if (run != null) {
	        PrintWriter out = new PrintWriter(run.getProcess().getOutputStream());
	        out.write("\n");
	    	out.flush();  
	    }
	}
	/**
     * @return Returns the writeStream.
     */
    public DataWriteStream[] getWriteStream() {
        return null;
    }
	
	public ArrayList getFiles() {
	    File[] files = recordControl.getDirectory().listFiles();
        String name = recordControl.getDirectory().getName();
        
	    ArrayList udrecFiles = new ArrayList();
        
	    udrecFiles.add(SerHelper.getVideoFile(files, name));
        udrecFiles.addAll(SerHelper.getAudioFiles(files, name));
	    return udrecFiles;
	}
}
