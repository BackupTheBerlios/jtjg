package model;
/*
BOTimer.java by Geist Alexander

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

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import service.SerTimerHandler;
import control.ControlTimerTab;

public class BOTimer extends java.lang.Object {
	public String channelId;
	public String timerNumber;
	private String modifiedId;
	public String eventTypeId;
	public String eventRepeatId;
	public String repeatCount = "0";
	public String announceTime;
	private String senderName;
	public String description;
	public String processName;

	public BOLocalTimer localTimer;

	public GregorianCalendar unformattedStartTime, unformattedStopTime;
    
    public boolean equals (Object timer) {
        if (((BOTimer)timer).getUnformattedStartTime().getTimeInMillis()==
            this.getUnformattedStartTime().getTimeInMillis() &&
                ((BOTimer)timer).getSenderName().equals(this.getSenderName())) {
            return true;
        }
        return false;
    }

	public String getRepeatCount() {
		return this.repeatCount;
	}

	public void setRepeatCount(String count) {
		if (repeatCount != null && !repeatCount.equals(count)) {
			this.setModifiedId("modify");
		}
		this.repeatCount = count;
	}

	public String getTimerNumber() {
		return this.timerNumber;
	}

	public void setTimerNumber(String eventId) {
		this.timerNumber = eventId;
	}

	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventType) {
		this.eventTypeId = eventType;
	}

	public String getEventRepeatId() {
		return this.eventRepeatId;
	}

	public void setEventRepeatId(String id) {
		if (eventRepeatId != null && !eventRepeatId.equals(id)) {
			this.setModifiedId("modify");
		}
		this.eventRepeatId = id;
	}

	public String getAnnounceTime() {
		return this.announceTime;
	}

	public void setAnnounceTime(String time) {
		if (announceTime != null && !announceTime.equals(time)) {
			this.setModifiedId("modify");
		}
		this.announceTime = time;
	}

	public String getStartDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
		return sdf.format(this.getUnformattedStartTime().getTime());
	}

	public String getLongStartTime() {
		long time = this.getUnformattedStartTime().getTimeInMillis();
		return Long.toString(time / 1000) + "000";
	}

	public String getLongStopTime() {
		return Long.toString(this.getUnformattedStopTime().getTimeInMillis());
	}

	public String getStartTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy   HH:mm");
		return sdf.format(this.getUnformattedStartTime().getTime());
	}

	public String getShortStartDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
		return sdf.format(this.getUnformattedStartTime().getTime());
	}

	public String getShortStartTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(this.getUnformattedStartTime().getTime());
	}

	public String getStopDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
		return sdf.format(this.getUnformattedStopTime().getTime());
	}

	public String getStopTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(this.getUnformattedStopTime().getTime());
	}

	public String getInfo() {
		String outputString = "";
		if (this.getSenderName() == null) {
			int type = Integer.parseInt(this.getEventTypeId()) - 1;

			String[][] timer = ControlTimerTab.timerType;
			for (int i = 0; i < timer.length; i++) {
				if (timer[i][1].equals(this.getEventTypeId())) {
					outputString = timer[i][0];
				}
			}
			

		} else {
			outputString = this.getSenderName();
		}
		Object[] args = {this.getStartTime(), this.getModifiedId(), outputString, this.getDescription()};
		MessageFormat form = new MessageFormat("-{1}-  {0} -{2}- {3}");
		return form.format(args);
	}

	public String getSenderName() {
		return this.senderName;
	}

	public void setSenderName(String sender) {
		if (sender.startsWith("0 ")) {
			sender = sender.substring(2);
		}
		this.senderName = sender;
	}
	/**
	 * @return Returns the unformattedStartTime.
	 */
	public GregorianCalendar getUnformattedStartTime() {
		return unformattedStartTime;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		if (description == null) {
			return "";
		}
		return description;
	}
	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the unformattedStopTime.
	 */
	public GregorianCalendar getUnformattedStopTime() {
		return unformattedStopTime;
	}

	/**
	 * @return Returns the modifiedId.
	 */
	public String getModifiedId() {
		return modifiedId;
	}
	/**
	 * @param modifiedId
	 *            The modifiedId to set. Beu neuen Timern keine modified-Id setzen!!
	 */
	public void setModifiedId(String id) {
		if (id == null || modifiedId == null || modifiedId.equals("modify")) {
			this.modifiedId = id;
		}
	}
	/**
	 * @return Returns the channelId.
	 */
	public String getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId
	 *            The channelId to set.
	 */
	public void setChannelId(String id) {
		if (channelId != null && !channelId.equals(id)) {
			this.setModifiedId("modify");
		}
		this.channelId = id;
	}
	/**
	 * @return Returns the localTimer.
	 */
	public BOLocalTimer getLocalTimer() {
		if (localTimer == null) {
			localTimer = SerTimerHandler.findLocalTimer(this);
		}
		return localTimer;
	}
	/**
	 * @param localTimer
	 *            The localTimer to set.
	 */
	public void setLocalTimer(BOLocalTimer localeTimer) {
		this.localTimer = localeTimer;
	}

	public void setUnformattedStartDate(GregorianCalendar date) {
		this.setModifiedId("modify");
		this.getUnformattedStartTime().set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
		this.getUnformattedStartTime().set(Calendar.MONTH, date.get(Calendar.MONTH));
		this.getUnformattedStartTime().set(Calendar.YEAR, date.get(Calendar.YEAR));
		this.getLocalTimer().setStartTime(this.getUnformattedStartTime());
		this.checkStopTime();
	}

	/**
	 * @param unformattedStartTime
	 *            The unformattedStartTime to set.
	 */
	public void setUnformattedStartTime(GregorianCalendar date) {
		this.setModifiedId("modify");
		this.getUnformattedStartTime().set(Calendar.MINUTE, date.get(Calendar.MINUTE));
		this.getUnformattedStartTime().set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
		this.getLocalTimer().setStartTime(this.getUnformattedStartTime());
		this.checkStopTime();
	}

	private void checkStopTime() {
		long startMillis = this.getUnformattedStartTime().getTimeInMillis();
		long stopMillis = this.getUnformattedStopTime().getTimeInMillis();

		while ((stopMillis - startMillis) < 0) {
			stopMillis = stopMillis + 86400000;
		}
		this.getUnformattedStopTime().setTimeInMillis(stopMillis);
		this.getLocalTimer().setStopTime(this.getUnformattedStopTime());
	}

	/**
	 * DEPRECATED
	 * 
	 * @param time
	 *            in milliseconds Bei Setzen eines neuen Start-Datum, muss das Stop-Datum angepasst werden
	 */
	public void setUnformattedStartTime(long startMillis) {
		this.getUnformattedStartTime().setTimeInMillis(startMillis);
		this.getLocalTimer().setStartTime(startMillis);

		int startDay = this.getUnformattedStartTime().get(Calendar.DAY_OF_MONTH);
		this.getUnformattedStopTime().set(Calendar.DAY_OF_MONTH, startDay);

		long stopMillis = this.getUnformattedStopTime().getTimeInMillis();
		if ((stopMillis - startMillis) < 0) {
			this.getUnformattedStopTime().set(Calendar.DAY_OF_MONTH, startDay + 1);
			this.getLocalTimer().setStopTime(this.getUnformattedStopTime().getTimeInMillis());
		}
		this.setModifiedId("modify");
	}

	/**
	 * @param unformattedStopTime
	 *            The unformattedStopTime to set.
	 */
	public void setUnformattedStopTime(GregorianCalendar endDate) {
		GregorianCalendar stopCal = (GregorianCalendar) this.getUnformattedStartTime().clone();
		stopCal.set(Calendar.HOUR_OF_DAY, endDate.get(Calendar.HOUR_OF_DAY));
		stopCal.set(Calendar.MINUTE, endDate.get(Calendar.MINUTE));
		this.unformattedStopTime = stopCal;
		this.getLocalTimer().setStopTime(this.getUnformattedStopTime().getTimeInMillis());
		this.checkStopTime();
		this.setModifiedId("modify");
	}

	public String toString() {
		return this.getShortStartTime() + " " + this.getLocalTimer().getDescription();
	}
    
    public boolean isNewOrModified() {
        return this.getModifiedId()!=null && !this.getModifiedId().equals("remove");
    }

	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}

}