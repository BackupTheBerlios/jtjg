
package model;

import control.*;

/*
 * BORecordInfo by Reinhard Achleitner
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
public class BORecordInfo {

	private String title;
	
	private String time;
	
	private String engine;
	
	private String log;
	
	private String epg;

	private String channel;
	
	private String video;
	
	private String audio;
	
	private String other;
	
	/**
	 * 
	 */
	public BORecordInfo() {
		super();
	}

	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getEpg() {
		return epg;
	}
	public void setEpg(String epg) {
		this.epg = epg;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param senderName
	 */
	public void setChannel(String senderName) {
		channel = senderName;
		
	}
	public String getChannel() {
		return channel;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Sender: " + getChannel());
		
		b.append("\n" + getEpg());
		b.append("\n\n" + ControlMain.getProperty("label_recordTime") + ": " + getTime());
		b.append("\n\n" + getEngine() + "\n");
		if (getVideo() != null)
		{
			b.append("\n" + ControlMain.getProperty("label_recordVideo")+ ":\n" + getVideo());
		}
		if (getAudio() != null)
		{
			b.append("\n" + ControlMain.getProperty("label_recordAudio")+ ":\n" + getAudio());
		}
		if (getOther() != null)
		{
			b.append("\n" + ControlMain.getProperty("label_recordOther")+ ":\n" + getOther());
		}
		b.append("\nLog:\n" + getLog());
		return b.toString();
	}
}
