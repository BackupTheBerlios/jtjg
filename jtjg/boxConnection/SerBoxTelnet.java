package boxConnection;
/*
SerBoxTelnet.java by Ralph Henneberger

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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.net.telnet.TelnetClient;
import java.util.logging.Logger;

import control.ControlMain;

public class SerBoxTelnet  {	
	
	static TelnetClient telnet = new TelnetClient();

	private static void createTelnetSession(String command) throws IOException, InterruptedException{		 	    
		telnet.connect(ControlMain.getActiveBox().getDboxIp());
	    OutputStream ostream = telnet.getOutputStream();
	    Writer writer = new OutputStreamWriter( ostream );
	    writer.write( ControlMain.getActiveBox().getLogin() + "\n" );
        writer.flush();
        Thread.sleep(1000);	        
        writer.write( ControlMain.getActiveBox().getPassword() + "\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write( command +"\n" );
        writer.flush();
        closeTelnetSession();        
	}
	private static void closeTelnetSession() throws IOException, InterruptedException{
		Thread.sleep(2000); 
		telnet.disconnect();		 
	}
	public static void runNhttpdReset() throws IOException, InterruptedException{
		Logger.getLogger("SerBoxTelnet").info(ControlMain.getProperty("msg_nhttpd"));
		createTelnetSession("killall nhttpd && nhttpd");						            
	}
	public static void runSectiondReset() throws IOException, InterruptedException{
		Logger.getLogger("SerBoxTelnet").info(ControlMain.getProperty("msg_sectiond"));
		createTelnetSession("killall sectionsd && sectionsd");						            
	}
	
	public static void runHalt() throws IOException, InterruptedException{	
		Logger.getLogger("SerBoxTelnet").info(ControlMain.getProperty("msg_reboot"));	
		createTelnetSession("halt");
	}
	
	public static void runReboot() throws IOException, InterruptedException{	
		Logger.getLogger("SerBoxTelnet").info(ControlMain.getProperty("msg_reboot"));		
		telnet.connect(ControlMain.getActiveBox().getDboxIp());
	    OutputStream ostream = telnet.getOutputStream();
	    Writer writer = new OutputStreamWriter( ostream );
	    writer.write( ControlMain.getActiveBox().getLogin() + "\n" );
        writer.flush();
        Thread.sleep(1000);	        
        writer.write( ControlMain.getActiveBox().getPassword() + "\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write("killall sectionsd"+"\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write("killall camd2"+"\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write("killall timerd"+"\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write("killall timerd"+"\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write("killall zapit"+"\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write("killall controld"+"\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write("killall nhttpd"+"\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write("sleep 3"+"\n" );
        writer.flush();
        Thread.sleep(1000);	 
        writer.write("busybox -reboot" +"\n" );
        writer.flush();
        Thread.sleep(1000);	 
        closeTelnetSession();      
	}
	public static void enableSPTSMode() throws IOException, InterruptedException{
		Logger.getLogger("SerBoxTelnet").info(ControlMain.getProperty("msg_EnableSPTS"));
		createTelnetSession("touch /var/etc/.spts_mode");	
		runReboot();
	}
	public static void disableSPTSMode() throws IOException, InterruptedException{
		Logger.getLogger("SerBoxTelnet").info(ControlMain.getProperty("msg_DisableSPTS"));
		createTelnetSession("rm -f /var/etc/.spts_mode");	
		runReboot();
	}
	
}