/*
TcpRecord.java by Geist Alexander
 
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
package streaming;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

import service.SerAlertDialog;

import control.ControlMain;

import model.BOPid;
import model.BORecordArgs;

public class TcpRecord extends Record{
    Socket socket;
    int port = 31338;
    byte[] buffer = null;
    String boxIp;
    String aPid;
    boolean running = true;
    
    RecordControl recordControl;
    BORecordArgs recordArgs;
    DataWriteStream[] writeStream = new DataWriteStream[1];
    
    public TcpRecord(BORecordArgs args, RecordControl control) {
        recordControl = control;
        recordArgs = args;
        streamType="PES";
        boxIp = ControlMain.getBoxIpOfActiveBox();
        BOPid pid = (BOPid)recordArgs.getPids().getAPids().get(0);
        aPid = pid.getNumber();
        Logger.getLogger("TcpRecord").info(ControlMain.getProperty("msg_recordPid")+aPid);
        writeStream[0] = new DataWriteStream(recordControl, ".mp2");
    }
    
    public void start() {
        PrintWriter out;
        try {
            socket = new Socket(boxIp,port);
            Logger.getLogger("TcpRecord").info(ControlMain.getProperty("msg_tcpStart"));
            out = new PrintWriter(socket.getOutputStream());
            
            String requestString = "GET /0x"+aPid+" HTTP/1.1\r\n\r\n";
            out.write(requestString);
            out.flush();
            
            this.readStream(socket.getInputStream());
        } catch (IOException e) {
            if (!running) {
                //Do nothing, Socket wurde regulaer geschlossen
            } else {
                SerAlertDialog.alertConnectionLost("TcpRecord", ControlMain.getControl().getView());
                recordControl.controlProgramTab.stopRecord();
            }
        }
    }
    
    public void readStream(InputStream inStream) throws IOException {
        BufferedInputStream in = new  BufferedInputStream(inStream);
        Logger.getLogger("TcpRecord").info(ControlMain.getProperty("msg_receiveData"));
        in.read(new byte[42]);
        int length = 0;
        do {
            byte[] temp = new byte[65535];
            length = in.read(temp, 0, 65535);
            buffer = new byte[length];
            System.arraycopy(temp, 0, buffer, 0, length );
            
            writeStream[0].write(buffer);
        } while (running);
        recordControl.controlProgramTab.stopRecord();
    }
    
    public void stop() {
        running = false;
        try {
            if(socket!=null){
                socket.close();
            }
            Logger.getLogger("TcpRecord").info(ControlMain.getProperty("msg_tcpStop"));
            for (int i=0; i<writeStream.length; i++) {
                writeStream[i].stop();
            }
        } catch (IOException e) {
            Logger.getLogger("TcpRecord").error(ControlMain.getProperty("err_tcpStop"));
        } catch (NullPointerException e) {
            //doNothing Aufnahmeabbruch vor dem Start
        }
    }
    
    /**
     * @return Returns the writeStream.
     */
    public DataWriteStream[] getWriteStream() {
        return writeStream;
    }
}
