package streaming;
/*
TcpReceiver.java by Geist Alexander 

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
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import control.ControlMain;

import service.SerAlertDialog;

public class TcpReceiver extends Thread {

	Socket tcpSocket;
	UdpRecord record;
	
	public TcpReceiver(UdpRecord stream) {
		record = stream;
	}
	
	public void run() {
		try {
			tcpSocket = record.tcpSocket;
			byte[] reply = new byte[1000];
			int len;
			
			while (record.running) {
				len = tcpSocket.getInputStream().read(reply);
				if (len == 0) {
					continue;
				}
				
				String[] replyString = new String(reply, 0, len).split("\n");
				for (int i=0; i<replyString.length; i++) {
					Logger.getLogger("TcpReceiver").info("From DBox: "+replyString[i]);
					if (replyString[i].indexOf("EXIT") != -1) {
						closeSocket();
						record.udpReceiver.closeSocket();
					}
				}
			}
		} catch (IOException e) {
			if (!record.running) {
				//Do nothing, Socket wurde regulaer geschlossen
			} else {
				SerAlertDialog.alertConnectionLost("TcpReceiver", ControlMain.getControl().getView());
				record.recordControl.controlProgramTab.stopRecord();
			}
		} 
	}
	
	public void closeSocket() {
		try {
			tcpSocket.close();
			Logger.getLogger("TcpReceiver").info(ControlMain.getProperty("msg_tcpStop"));
		} catch (IOException e) {
			Logger.getLogger("TcpReceiver").error(ControlMain.getProperty("err_tcpStop"));
		}
	}

}
