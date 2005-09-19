package boxConnection;
/*
SerStreamingServer.java by Geist Alexander 

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.BORecordArgs;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import service.XML.SerXMLConverter;
import control.ControlMain;


public class SerStreamingServer extends Thread {
	
	private static ServerSocket server;
	public static boolean isRunning = false;

	public void run() {
	    int port = Integer.parseInt(ControlMain.getSettingsRecord().getStreamingServerPort());
		try {
			server = new ServerSocket(port);
			isRunning=true;
			Logger.getLogger("SerStreamingServer").info(
			        ControlMain.getProperty("msg_sserver")+
			        ControlMain.getBoxIpOfActiveBox()+" Port: "+
			        ControlMain.getSettingsRecord().getStreamingServerPort());
			ControlMain.getBoxAccess().sendMessage("Start%20Streaming-Server");
            switchServerButton(true);
			Socket socket = server.accept();
			this.record(socket);
		} catch (IOException e) {
			if (!isRunning) {
			    isRunning=false;
			}	
		} catch (DocumentException e) {
		    isRunning=false;
			Logger.getLogger("SerStreamingServer").error("Not valid XML-Stream");	
		}
	}
    
    public static void switchServerButton(boolean status) {
        try {
            if (status) {
                ControlMain.getControl().getView().getTabProgramm().startStreamingServerModus();    
            } else {
                ControlMain.getControl().getView().getTabProgramm().stopStreamingServerModus();
            }
            
        } catch (Exception e) {
            //do nothing, Button nicht da
        }
    }
	
	public void record(Socket socket) throws IOException, DocumentException {
	    
//	    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//	    String line;
//	    while ((line=input.readLine())!=null) {
//	        System.out.println(line);
//	    }
	    
		//Pretty print the document to System.out
//		OutputFormat format = OutputFormat.createPrettyPrint();
//		XMLWriter writer = new XMLWriter( System.out, format );
//		writer.write( document );
	    
	    byte[] message = new byte[10000];
		int messageLength = 0;
		
		messageLength = socket.getInputStream().read(message);
		// Umlaute filtern
		for (int i = 0; i < messageLength; i++) {
			if (message[i] > 0x7e || (message[i] < 0x20 && message[i] != '\t' && message[i] != '\n' && message[i] != '\r')) {
			    message[i] = (byte)' ';
			}
		}

		SAXReader reader = new SAXReader();
		ByteArrayInputStream in = new ByteArrayInputStream(message, 0, messageLength);
		Document document = reader.read(in);
		
		BORecordArgs recordArgs = SerXMLConverter.parseRecordDocument(document);
		if (recordArgs.getCommand().equals("stop") ) {
		    ControlMain.getControl().getView().getTabProgramm().getControl().stopRecord();
		    Logger.getLogger("SerStreamingServer").info("From DBox: Stop record");
		}
		if (recordArgs.getCommand().equals("record")) {
			recordArgs.checkTitle();		
			ControlMain.getControl().getView().getTabProgramm().getControl().startRecord(recordArgs);
			Logger.getLogger("SerStreamingServer").info("From DBox: Start record");
		}
		server.close();  //server restart
		this.run();
	}

	public static boolean stopServer() {
		try {
			if (server != null && server.isBound()) {
			    isRunning=false;
				server.close();
                switchServerButton(false);
				Logger.getLogger("SerStreamingServer").info("StreamingServer stopped");
				return true;
			}
		} catch (IOException e) {
			Logger.getLogger("SerStreamingServer").error("StreamingServer stop failed");	
		}
		return false;
	}
	
}
