package boxConnection;
/*
SerBoxGuiDetector.java by Alexander Geist

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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.URL;

import control.ControlMain;

public class SerBoxGuiDetector extends Thread {
	
	String connectBoxIP;
	Thread waitThread;

	public SerBoxGuiDetector(String ip, Thread thread) {
		connectBoxIP=ip;
		waitThread=thread;
	}
	
	public void run() {
		int imageType = 3; //Defaultwert!!!
		Authenticator.setDefault(new SerBoxAuthenticator());
		if (isNeutrino(connectBoxIP)){
	        ControlMain.setBoxAccess(new SerBoxControlNeutrino());
	    } else if (isEnigma(connectBoxIP)) {
	    	ControlMain.setBoxAccess(new SerBoxControlEnigma());
	    }
		synchronized( waitThread ) {	
			waitThread.notify();
		}
	}
	
	private synchronized void stopWaitThread() {
		waitThread.notify();	
	}
	
	private static boolean isEnigma(String ConnectBoxIP) {
        try {
            URL url = new URL("http://" + ConnectBoxIP);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if  (inputLine.toLowerCase().indexOf("enigma") > 0) {
                    return true;
                } 
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    private static boolean isNeutrino(String ConnectBoxIP) {
        try {
            URL url=new URL("http://"+ConnectBoxIP+"/control/info");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            if (in.readLine().equals("Neutrino")){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
