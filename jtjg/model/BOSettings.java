package model;
/*
 BOSettings.java by Geist Alexander 

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

/**
 * Klasse referenziert die Settings Settings werden beim Start gelesen und beim Beenden gespeichert Sind keine Änderungen an den Settings
 * vorgenommen worden, werden diese nicht gespeichert Werden settings geaendert muss die Variable "settingsChanged" auf true gesetzt werden.
 * Dies geschieht in den Setter-Methoden der einzelnen Settings-Optionen
 */
public class BOSettings {

	private BOSettingsMain mainSettings;
	private BOSettingsRecord recordSettings;
	private BOSettingsPlayback playbackSettings;
	private BOSettingsMovieGuide movieGuideSettings;
	private BOSettingsPath pathSettings;
	private BOSettingsLayout layoutSettings;
	private BOSettingsProxy proxySettings;
	private boolean settingsChanged = false;
	public boolean standardSettings = false;

	/**
	 * @return Returns the settingsChanged.
	 */
	public boolean isSettingsChanged() {
		return settingsChanged;
	}
	/**
	 * @param settingsChanged
	 *            The settingsChanged to set.
	 */
	public void setSettingsChanged(boolean settingsChanged) {
		this.settingsChanged = settingsChanged;
	}
	/**
	 * @return Returns the mainSettings.
	 */
	public BOSettingsMain getMainSettings() {
		if (mainSettings == null) {
			mainSettings = new BOSettingsMain(this);
		}
		return mainSettings;
	}
	/**
	 * @param mainSettings
	 *            The mainSettings to set.
	 */
	public void setMainSettings(BOSettingsMain mainSettings) {
		this.mainSettings = mainSettings;
	}
	/**
	 * @return Returns the movieGuideSettings.
	 */
	public BOSettingsMovieGuide getMovieGuideSettings() {
		if (movieGuideSettings == null) {
			movieGuideSettings = new BOSettingsMovieGuide(this);
		}
		return movieGuideSettings;
	}
	/**
	 * @param movieGuideSettings
	 *            The movieGuideSettings to set.
	 */
	public void setMovieGuideSettings(BOSettingsMovieGuide movieGuideSettings) {
		this.movieGuideSettings = movieGuideSettings;
	}
	/**
	 * @return Returns the playbackSettings.
	 */
	public BOSettingsPlayback getPlaybackSettings() {
		if (playbackSettings == null) {
			playbackSettings = new BOSettingsPlayback(this);
		}
		return playbackSettings;
	}
	/**
	 * @param playbackSettings
	 *            The playbackSettings to set.
	 */
	public void setPlaybackSettings(BOSettingsPlayback playbackSettings) {
		this.playbackSettings = playbackSettings;
	}
	/**
	 * @return Returns the recordSettings.
	 */
	public BOSettingsRecord getRecordSettings() {
		if (recordSettings == null) {
			recordSettings = new BOSettingsRecord(this);
		}
		return recordSettings;
	}
	/**
	 * @param recordSettings
	 *            The recordSettings to set.
	 */
	public void setRecordSettings(BOSettingsRecord recordSettings) {
		this.recordSettings = recordSettings;
	}
	/**
	 * @return Returns the recordSettings.
	 */
	public BOSettingsPath getPathSettings() {
		if (pathSettings == null) {
			pathSettings = new BOSettingsPath(this);
		}
		return pathSettings;
	}
	/**
	 * @param recordSettings
	 *            The recordSettings to set.
	 */
	public void setPathSettings(BOSettingsPath pathSettings) {
		this.pathSettings = pathSettings;
	}
	public BOSettingsLayout getLayoutSettings() {
		if (layoutSettings == null) {
			layoutSettings = new BOSettingsLayout(this);
		}
		return layoutSettings;
	}
	public void setLayoutSettings(BOSettingsLayout layoutSettings) {
		this.layoutSettings = layoutSettings;
	}
	/**
	 * @return Returns the proxySettings.
	 */
	public BOSettingsProxy getProxySettings() {
		if (proxySettings == null) {
			proxySettings = new BOSettingsProxy(this);
		}
		return proxySettings;
	}
	/**
	 * @param proxySettings The proxySettings to set.
	 */
	public void setProxySettings(BOSettingsProxy proxySettings) {
		this.proxySettings = proxySettings;
	}
}