package service;
/*
 * SerExternalProcessHandler.java by Geist Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 */
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.BOExternalProcess;
import control.ControlMain;

public class SerExternalProcessHandler {

	private static ArrayList processList;

	public static BOExternalProcess startProcess(SerProcessStopListener listener, String name, String[] execString, boolean logging) {
		BOExternalProcess process = new BOExternalProcess(listener, name, execString, logging);
		getProcessList().add(process);
		process.start();
		return process;
	}
    
    public static BOExternalProcess startProcess(SerProcessStopListener stopListener, SerInputStreamListener inputListener,
            String name, String[] execString, boolean logging) {
        BOExternalProcess process = new BOExternalProcess(stopListener, inputListener, name, execString, logging);
        getProcessList().add(process);
        process.start();
        return process;
    }

	public static BOExternalProcess startProcess(String name, String execString, boolean logging) {
		BOExternalProcess process = new BOExternalProcess(name, execString, logging);
		getProcessList().add(process);
		process.start();
		return process;
	}

	public static BOExternalProcess startProcess(String name, String[] execString, boolean logging, boolean logErrorAsInfo) {
		BOExternalProcess process = new BOExternalProcess(name, execString, logging, logErrorAsInfo);
		getProcessList().add(process);
		process.start();
		return process;
	}

	public static BOExternalProcess startProcess(String name, String[] execStringArray, boolean logging) {
		BOExternalProcess process = new BOExternalProcess(name, execStringArray, logging);
		getProcessList().add(process);
		process.start();
		return process;
	}

	public static void closeAll() {
		ArrayList list = getProcessList();
		for (int i = 0; i < list.size(); i++) {
			BOExternalProcess boProc = (BOExternalProcess) list.get(i);
			Process proc = boProc.getProcess();
			if (proc != null) {
				try {
					if (proc.exitValue() == 0) {
					} //Prozess beendet
				} catch (IllegalThreadStateException e) {
					if (boProc.isCloseWithoutPrompt()) {
						proc.destroy();
					} else {
						int ret = JOptionPane.showConfirmDialog(ControlMain.getControl().getView(), ControlMain
								.getProperty("msg_closeProcess1")
								+ boProc.getProgName() + " " + ControlMain.getProperty("msg_closeProcess2"), "", JOptionPane.YES_NO_OPTION);
						if (ret == 0) {
							proc.destroy();
						}
					}
				}
			}
		}
	}

	/**
	 * @return Returns the processList.
	 */
	private static ArrayList getProcessList() {
		if (processList == null) {
			processList = new ArrayList();
		}
		return processList;
	}
	/**
	 * @param processList
	 *            The processList to set.
	 */
	private static void setProcessList(ArrayList processList) {
		SerExternalProcessHandler.processList = processList;
	}
}