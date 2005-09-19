package model;

import java.awt.Dimension;
import java.awt.Point;

/*
 * BOSettingsLayout by Achleitner Reinhard
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
public class BOSettingsLayout {

	private BOSettings settings;

	private Dimension size;

	private Point location;

	private Dimension logSize;

	private Point logLocation;

	private int recordInfoDirectorySplitPos;

	private Point locationOfTimerDialog = new Point(0, 0);

	private Point locationOfSystemTimerDialog = new Point(0, 0);

	public BOSettingsLayout(BOSettings settings) {
		this.setSettings(settings);
	}

	private void setSettingsChanged(boolean value) {
		this.getSettings().setSettingsChanged(value);
	}

	/**
	 * @return Returns the settings.
	 */
	public BOSettings getSettings() {
		return settings;
	}
	/**
	 * @param settings
	 *            The settings to set.
	 */
	public void setSettings(BOSettings settings) {
		this.settings = settings;
	}

	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		if (this.location == null || !location.equals(this.location)) {
			this.location = location;
			setSettingsChanged(true);
		}
	}
	public int getRecordInfoDirectorySplitPos() {
		return recordInfoDirectorySplitPos;
	}
	public void setRecordInfoDirectorySplitPos(int recordInfoDirectorySplitPos) {
		if (recordInfoDirectorySplitPos != this.recordInfoDirectorySplitPos) {
			this.recordInfoDirectorySplitPos = recordInfoDirectorySplitPos;
			setSettingsChanged(true);
		}

	}
	public Dimension getSize() {
		return size;
	}
	public void setSize(Dimension size) {
		if (this.size == null || !size.equals(this.size)) {
			this.size = size;
			setSettingsChanged(true);
		}
	}

	public Point getLogLocation() {
		return logLocation;
	}
	public void setLogLocation(Point logPosition) {
		if (this.logLocation == null || !logPosition.equals(this.logLocation)) {
			this.logLocation = logPosition;
			setSettingsChanged(true);
		}
	}
	public Dimension getLogSize() {
		return logSize;
	}
	public void setLogSize(Dimension logSize) {
		if (this.logSize == null || !logSize.equals(this.logSize)) {
			this.logSize = logSize;
			setSettingsChanged(true);
		}
	}

	/**
	 * @param location2
	 */
	public void setLocationOfTimerDialog(Point location) {
		
		if (locationOfTimerDialog == null || !locationOfTimerDialog.equals(location))
		{
			setSettingsChanged(true);
			locationOfTimerDialog = location;
		}
		

	}

	/**
	 * @return Returns the locationOfTimerDialog.
	 */
	public Point getLocationOfTimerDialog() {
		if (locationOfTimerDialog == null) {
			return new Point(10, 10);
		}
		return locationOfTimerDialog;
	}

	/**
	 * @param location2
	 */
	public void setLocationOfSystemTimerDialog(Point systemLocation) {
		if (locationOfSystemTimerDialog == null || !locationOfSystemTimerDialog.equals(systemLocation))
		{
			setSettingsChanged(true);
			locationOfSystemTimerDialog = systemLocation;
		}
	}
	/**
	 * @return Returns the locationOfTimerDialog.
	 */
	public Point getLocationOfSystemTimerDialog() {
		if (locationOfSystemTimerDialog == null) {
			return new Point(10, 10);
		}
		return locationOfSystemTimerDialog;
	}

}