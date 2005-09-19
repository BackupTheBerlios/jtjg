package streaming;
/*
 * LocalTimerRecordDaemon.java by Geist Alexander
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
import java.io.IOException;
import java.util.*;
import java.util.GregorianCalendar;

import model.*;
import model.BOLocalTimer;
import model.BOPids;
import model.BORecordArgs;
import model.BOTimer;

import org.apache.log4j.Logger;

import service.*;

import control.ControlMain;
import control.ControlProgramTab;

public class LocalTimerRecordDaemon extends Thread {

	public static boolean running = false;

	public void run() {
		while (!running) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
	
			BOTimer timer = ControlMain.getBoxAccess().detectNextLocalRecordTimer(false);

			if (timer != null && !timer.getLocalTimer().isLocked()) {
				//long now = new GregorianCalendar().getTimeInMillis();

				//Einmal Timer
				long now = Calendar.getInstance().getTimeInMillis();
				if (now > timer.getLocalTimer().getStartTime() && now < timer.getLocalTimer().getStopTime()) {
					Logger.getLogger("LocalTimerRecordDaemon").info(
							ControlMain.getProperty("msg_startRecord") + " " + timer.getLocalTimer().getDescription());
					this.startRecord(timer.getLocalTimer());
					running = true;

					this.waitForStop(timer.getLocalTimer());
				}
			}
		}
	}

	/**
	 * überprüft ob der Timer ausgeführt werden muss
	 * 
	 * @param timer
	 * @param calendar
	 * @return
	 */
	private void recalcTimer(BOTimer timer) {

		String strRepeatType = timer.getEventRepeatId();
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();

		boolean save = false;
		if (strRepeatType.equals("1")) {
			// Täglich
			// Füge beim Start und Enddatum einen Tag dazu
			start.setTimeInMillis(timer.getLocalTimer().getStartTime());
			start.add(Calendar.DAY_OF_YEAR, 1);

			end.setTimeInMillis(timer.getLocalTimer().getStopTime());
			end.add(Calendar.DAY_OF_YEAR, 1);
			save = true;

		} else if (strRepeatType.equals("2")) {
			// Wöchentlich
			// Füge beim Start und Enddatum eine Woche dazu
			start.setTimeInMillis(timer.getLocalTimer().getStartTime());
			start.add(Calendar.WEEK_OF_YEAR, 1);

			end.setTimeInMillis(timer.getLocalTimer().getStopTime());
			end.add(Calendar.WEEK_OF_YEAR, 1);

			save = true;
		} else if (strRepeatType.equals("3")) {
			// 2-Wöchentlich
			start.setTimeInMillis(timer.getLocalTimer().getStartTime());
			start.add(Calendar.WEEK_OF_YEAR, 2);

			end.setTimeInMillis(timer.getLocalTimer().getStopTime());
			end.add(Calendar.WEEK_OF_YEAR, 2);
			save = true;
		} else if (strRepeatType.equals("4")) {
			// 4-Wöchentlich
			start.setTimeInMillis(timer.getLocalTimer().getStartTime());
			start.add(Calendar.WEEK_OF_YEAR, 4);

			end.setTimeInMillis(timer.getLocalTimer().getStopTime());
			end.add(Calendar.WEEK_OF_YEAR, 4);
			save = true;

		} else if (Integer.parseInt(strRepeatType) > 5) {
			// Wochentage
			Vector vDays = getWeekDays(strRepeatType);

			// Ermittle aktuellen Wochentag
			start.setTimeInMillis(timer.getLocalTimer().getStartTime());
			end.setTimeInMillis(timer.getLocalTimer().getStopTime());

			int iCurrentDay = start.get(Calendar.DAY_OF_WEEK);
			if (iCurrentDay == Calendar.SUNDAY) {
				iCurrentDay = Calendar.SUNDAY + 1;
			}

			//Ermittle den nächsten Tag für eine Aufnahme
			// wird in dieser Woche keiner gefunden fang wieder von vorne an
			Iterator iter = vDays.iterator();

			boolean found = false;
			while (iter.hasNext() && !found) {
				Integer day = (Integer) iter.next();
				if (iCurrentDay < day.intValue()) {
					start.set(Calendar.DAY_OF_WEEK, day.intValue());
					end.set(Calendar.DAY_OF_WEEK, day.intValue());
					found = true;
				}
			}
			if (!found && vDays.size() > 0) {
				// Nimm ersten Tag der nächsten Woche
				start.add(Calendar.WEEK_OF_YEAR, 1);
				end.add(Calendar.WEEK_OF_YEAR, 1);
				start.set(Calendar.DAY_OF_WEEK, ((Integer) vDays.firstElement()).intValue());
				end.set(Calendar.DAY_OF_WEEK, ((Integer) vDays.firstElement()).intValue());
				found = true;
			}

			if (found) {
				save = true;
			}
		}

		if (save) {
			timer.getLocalTimer().setStartTime(start);
			timer.getLocalTimer().setStopTime(end);
			timer.setUnformattedStartDate(start);
			timer.setUnformattedStopTime(end);
			timer.setModifiedId("localModify");
		}
	}

	/**
	 * @param strRepeatType
	 */
	private Vector getWeekDays(String strRepeatType) {
		int[] aiDays = new int[7];
		aiDays[0] = 32768; //Sonntag
		aiDays[1] = 16384; //Samstag
		aiDays[2] = 8192; //Freitag
		aiDays[3] = 4096; //Donnerstag
		aiDays[4] = 2048; //Mittwoch
		aiDays[5] = 1024; //Dienstag
		aiDays[6] = 512; //Montag

		Vector vDays = new Vector();

		boolean[] days = new boolean[7];
		int rep = Integer.parseInt(strRepeatType);
		for (int i = 0; i < aiDays.length; i++) {
			if (rep - aiDays[i] >= 0) {
				days[i] = true;
				vDays.insertElementAt(new Integer(getDay(i)), 0);
				rep = rep - aiDays[i];
			}
		}

		return vDays;

	}

	/**
	 * @param i
	 * @return
	 */
	private int getDay(int i) {
		switch (i) {
			case 0 :
				return Calendar.SATURDAY + 1;
			case 1 :
				return Calendar.SATURDAY;
			case 2 :
				return Calendar.FRIDAY;
			case 3 :
				return Calendar.THURSDAY;
			case 4 :
				return Calendar.WEDNESDAY;
			case 5 :
				return Calendar.TUESDAY;
			case 6 :
				return Calendar.MONDAY;
		}
		return 0;
	}

	private void waitForStop(BOLocalTimer timer) {
		while (running) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			long now = new GregorianCalendar().getTimeInMillis();
			if (now > timer.getStopTime()) {

				// Bei einem wiederkehrenden Timer das Datum neu setzen
				recalcTimer(timer.getMainTimer());
				SerTimerHandler.saveTimer(timer.getMainTimer(), false, false);

				ControlMain.getControl().getView().getTabProgramm().getControl().stopRecord();
				ControlMain.getBoxAccess().getTimerList(true);
				running = false;
				ControlMain.getBoxAccess().detectNextLocalRecordTimer(true);
			}
		}
		this.run();
	}

	private void startRecord(BOLocalTimer timer) {
		try {
			//umschalten auf den gewuenschten Sender
			ControlMain.getBoxAccess().zapTo(timer.getMainTimer().getChannelId());
			//ermitteln der Pids
			BOPids pids = ControlMain.getBoxAccess().getPids();
			//starte Aufnahme
			ControlProgramTab ctrl = ControlMain.getControl().getView().getTabProgramm().getControl();
			BORecordArgs args = new BORecordArgs(pids, timer, false);
			Calendar now = Calendar.getInstance();
			Iterator it = ControlMain.getBoxAccess().getEpg(new BOSender("1", timer.getMainTimer().getChannelId(), "")).iterator();
			while (it.hasNext()) {
			    BOEpg element = (BOEpg) it.next();
			    if (SerFormatter.compareDates(element.getStartdate(), now) <= 0 && SerFormatter.compareDates(element.getEndDate(), now) >= 0) {
			        args.setEpgInfo1(element.getEpgDetail().getText()
			        );
			    }
			}
			ctrl.startRecord(args);
		} catch (IOException e) {

		}
	}

}