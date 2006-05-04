package streaming;
/*
UdpReceiver.java by Geist Alexander 

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
import java.net.DatagramSocket;
import java.net.SocketException;

import java.util.logging.Logger;


import service.SerAlertDialog;
import control.ControlMain;


public class UdpReceiver extends Thread {
	
	public long packetCount = 0;
	DatagramSocket udpSocket;
	UdpRecord record; 

	
	public UdpReceiver(UdpRecord stream) {
		record = stream;
		try {
            udpSocket = new DatagramSocket(record.udpPort);
        } catch (SocketException e) {
            Logger.getLogger("UdpReceiver").warning(ControlMain.getProperty("err_udpSocket")+" "+record.udpPort);
            record.recordControl.controlProgramTab.stopRecord();
        }
	}
	
	public void run() {
		try {		
			UdpPacket udpPacket = new UdpPacket();
			int curStatus = 0;
			
			do {			
				if (!record.running) break;
				udpSocket.receive( udpPacket.packet);
				curStatus = udpPacket.getPacketStatus();					
				record.writeStream[udpPacket.getStream()].write(udpPacket);
				packetCount++;
			} while (curStatus != 2 && record.running);		
		} catch (IOException e) {
			if (!record.running) {
				//Do nothing, regulaerer Stop
			} else {
				SerAlertDialog.alertConnectionLost("UdpReceiver", ControlMain.getControl().getView());
			    record.recordControl.controlProgramTab.stopRecord();
			}
		}
	}
	
	public boolean closeSocket() {
		udpSocket.disconnect();
		udpSocket.close();
		Logger.getLogger("UdpReceiver").info(ControlMain.getProperty("msg_updStop"));
		return true;
	}
}
