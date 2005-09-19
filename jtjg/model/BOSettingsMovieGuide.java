package model;

import java.util.ArrayList;

/*
BOSettingsMovieGuide.java by Geist Alexander 

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
public class BOSettingsMovieGuide {
	
	private BOSettings settings;
	public ArrayList mgSelectedChannels;
	public ArrayList mgDontForgetListe;
	public int mgLoadType;
	public int mgDefault;
	public boolean mgStoreOriginal;
	public boolean mgInfoDontForget;

	public BOSettingsMovieGuide(BOSettings settings) {
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
	 * @param settings The settings to set.
	 */
	public void setSettings(BOSettings settings) {
		this.settings = settings;
	}
	public int getMgDefault() {
		return mgDefault;
	}
	public void setMgDefault(int mgDefault) {
			if (this.mgDefault != mgDefault) {
				setSettingsChanged(true);
				this.mgDefault = mgDefault;
			}
	}
	public int getMgLoadType() {
		return mgLoadType;
	}
	public void setMgLoadType(int mgLoadType) {
			if (this.mgLoadType != mgLoadType) {
				setSettingsChanged(true);
				this.mgLoadType = mgLoadType;
			}
	}
	public ArrayList getMgSelectedChannels() {
		return mgSelectedChannels;
	}
	public void setMgSelectedChannels(ArrayList mgSelectedChannels) {
			if (mgSelectedChannels != null && !mgSelectedChannels.equals(this.mgSelectedChannels)) {
				setSettingsChanged(true);
				this.mgSelectedChannels = mgSelectedChannels;
			}
	}
	public ArrayList getMgDontForgetListe() {
		if (mgDontForgetListe==null) {
			mgDontForgetListe=new ArrayList();
		}
		return mgDontForgetListe;
	}
	public void setMgDontForgetListe(ArrayList mgDontForgetListe) {
			if (mgDontForgetListe != null && !mgDontForgetListe.equals(this.mgDontForgetListe)) {
				setSettingsChanged(true);
				this.mgDontForgetListe = mgDontForgetListe;
			}
	}
	
	public boolean isMgStoreOriginal() {
		return mgStoreOriginal;
	}
	public void setMgStoreOriginal(boolean mgStoreOriginal) {
			if (this.mgStoreOriginal != mgStoreOriginal) {
				setSettingsChanged(true);
				this.mgStoreOriginal = mgStoreOriginal;
			}
	}
	public boolean isMgInfoDontForget() {
		return mgInfoDontForget;
	}
	public void setMgInfoDontForget(boolean mgInfoDontForget) {
			if (this.mgInfoDontForget != mgInfoDontForget) {
				setSettingsChanged(true);
				this.mgInfoDontForget = mgInfoDontForget;
			}
	}
}
