package boxConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.URL;

import control.ControlMain;

public class SerBoxGuiDetector extends Thread {
	String boxIp;
	Thread waitThread;
	private static String NEUTRINO = "Neutrino";
	private static String ENIGMA   = "enigma";
	
	public SerBoxGuiDetector(String ip, Thread thread) {
		boxIp = ip;
		waitThread = thread;
	}

	public void run() {
		int imageType = 3; // Defaultwert!!!
		Authenticator.setDefault(new SerBoxAuthenticator());
		if (isBoxGui(boxIp,NEUTRINO)) {
			ControlMain.setBoxAccess(new SerBoxControlNeutrino());
		} else if (isBoxGui(boxIp,ENIGMA)) {
			ControlMain.setBoxAccess(new SerBoxControlEnigma());
		}
		synchronized (waitThread) {
			waitThread.notify();
		}
	}

	private synchronized void stopWaitThread() {
		waitThread.notify();
	}
	
	private static boolean isBoxGui(String boxIp,String boxGui) {
		boolean retVal = false;
		String tmpUrl = "http://"+boxIp;
		if (boxGui.toLowerCase().matches(NEUTRINO)){
			tmpUrl = tmpUrl+"/control/info";
		}		
		try {
			URL url = new URL(tmpUrl);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String input;
			while ((input = in.readLine()) != null) {
				retVal = input.toLowerCase().contains(boxGui);
			}
		} catch (Exception ioex) {
			ioex.printStackTrace();
		}		
		return retVal;
	}	
}
