package model;

import java.beans.*;
import java.io.*;
import java.util.*;

import org.apache.log4j.*;

import control.*;

/*
 * BORecordInfos by Reinhard Achleitner
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
public class BORecordInfos extends Vector {

	private static BORecordInfos infos;

	private boolean changed = false;

	/**
	 *  
	 */
	public BORecordInfos() {
		super();
	}

	public void addRecordInfo(BORecordInfo info) {
		addElement(info);
		setChanged(true);
		saveInfos();
	}

	public static BORecordInfos getInfos() {
		if (infos == null) {
			loadInfos();
		}
		return infos;
	}

	/**
	 *  
	 */
	private static void loadInfos() {
		File f = new File(ControlMain.getSettings().getPathSettings().getWorkDirectory(), "RecordInfos.xml");
		if (f.exists()) {
			XMLDecoder dec;
			try {
				dec = new XMLDecoder(new FileInputStream(f));
				infos = (BORecordInfos) dec.readObject();
				dec.close();
			} catch (FileNotFoundException e) {
				Logger.getLogger("ControlProgrammTab").error(e.getMessage());
				e.printStackTrace();
			}

		} else {
			infos = new BORecordInfos();
		}
	}

	public static void saveInfos() {
		if (infos.isChanged()) {

			Thread t = new Thread(new Runnable() {
				public void run() {
					File f = new File(ControlMain.getSettings().getPathSettings().getWorkDirectory(), "RecordInfos.xml");
					try {
						XMLEncoder enc = new XMLEncoder(new FileOutputStream(f));
						enc.writeObject(infos);
						enc.close();
					} catch (FileNotFoundException e) {
						Logger.getLogger("ControlProgrammTab").error(e.getMessage());
						e.printStackTrace();
					}
					infos.setChanged(false);
				}
			});
			t.start();

		}
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * @return
	 */
	public static boolean isLoaded() {
		return infos != null;
	}
}