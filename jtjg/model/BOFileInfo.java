package model;

import java.io.File;
import java.util.Date;

import service.SerHelper;

/**
 * create all infos of the given file
 * 
 * @author Reinhard Achleitner
 * @version 06.12.2004
 *  
 */
public class BOFileInfo {

	private File file;

	private String size;

	private Date lastChange;

	private String typ;

	private String videoInfo;

	private String audioInfo;

	private String videoTextInfo;

	private String estimatedPlaytime;

	/**
	 *  
	 */
	public BOFileInfo(File file) {
		super();
		this.file = file;
		readInfos();
	}

	/**
	 * read the file infos
	 *  
	 */
	private void readInfos() {

		// Größe
		setSize(SerHelper.calcSize(file.length(), "MB"));

		// Letzte Änderung
		setLastChange(new Date(file.lastModified()));
		// Typ

		// Video

		//Audio

		//Videotext

		//est. playtime

	}

	public String getAudioInfo() {
		return audioInfo;
	}
	public void setAudioInfo(String audioInfo) {
		this.audioInfo = audioInfo;
	}
	public String getEstimatedPlaytime() {
		return estimatedPlaytime;
	}
	public void setEstimatedPlaytime(String estimatedPlaytime) {
		this.estimatedPlaytime = estimatedPlaytime;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Date getLastChange() {
		return lastChange;
	}
	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	public String getVideoInfo() {
		return videoInfo;
	}
	public void setVideoInfo(String videoInfo) {
		this.videoInfo = videoInfo;
	}
	public String getVideoTextInfo() {
		return videoTextInfo;
	}
	public void setVideoTextInfo(String videoTextInfo) {
		this.videoTextInfo = videoTextInfo;
	}

}