package model;

import java.util.ArrayList;

import control.ControlMainView;

/*
 * BOSettingsMain.java by Geist Alexander
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
public class BOSettingsMain {

	private BOSettings settings;
	public String locale = "";
	public String plasticTheme;
	public ArrayList boxList;
	public String lookAndFeel;
	public String skinLFTheme;
	public boolean startFullscreen = false;
	public boolean useSysTray = false;
	public boolean startVlcAtStart;
	public boolean showLogWindow;

	public boolean startMinimized;

	public BOSettingsMain(BOSettings settings) {
		this.setSettings(settings);
	}

	private void setSettingsChanged(boolean value) {
		this.getSettings().setSettingsChanged(value);
	}

	public boolean isSkinLookAndFeel() {
	    return getLookAndFeel().indexOf("SkinLookAndFeel") >-1;
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

	/**
	 * @return Returns the plasticTheme.
	 */
	public String getPlasticTheme() {
	    if (plasticTheme==null) {
	        plasticTheme="ExperienceBlue";
	    }
		return plasticTheme;
	}
	/**
	 * @param plasticTheme
	 *            The plasticTheme to set.
	 */
	public void setPlasticTheme(String layout) {
		if (this.plasticTheme == null || !this.plasticTheme.equals(layout)) {
			setSettingsChanged(true);
			this.plasticTheme = layout;
		}
	}

	/**
	 * @return Returns the locale.
	 */
	public String getLocale() {
		return locale;
	}
	/**
	 * @param locale
	 *            The locale to set.
	 */
	public void setLocale(String locale) {
		if (this.locale == null || !this.locale.equals(locale)) {
			setSettingsChanged(true);
			this.locale = locale;
		}
	}
	public String getShortLocale() {
		return this.getLocale().substring(0, 2);
	}

	/**
	 * @return Returns the boxList.
	 */
	public ArrayList getBoxList() {
		return boxList;
	}
	/**
	 * @param boxList
	 *            The boxList to set.
	 */
	public void setBoxList(ArrayList box) {
		this.boxList = box;
	}
	public void removeBox(int number) {
		setSettingsChanged(true);
		getBoxList().remove(number);
	}
	public void addBox(BOBox box) {
		setSettingsChanged(true);
		getBoxList().add(box);
	}
	/**
	 * @return Returns the startFullscreen.
	 */
	public boolean isStartFullscreen() {
		return startFullscreen;
	}
	/**
	 * @param startFullscreen
	 *            The startFullscreen to set.
	 */
	public void setStartFullscreen(boolean startFullscreen) {
		if (this.startFullscreen != startFullscreen) {
			setSettingsChanged(true);
			this.startFullscreen = startFullscreen;
		}
	}
	/**
	 * @return Returns the useSysTray.
	 */
	public boolean isUseSysTray() {
		return useSysTray;
	}
	/**
	 * @param useSysTray
	 *            The useSysTray to set.
	 */
	public void setUseSysTray(boolean useSysTray) {
		if (this.useSysTray != useSysTray) {
			setSettingsChanged(true);
			this.useSysTray = useSysTray;

		}
	}
	public String getLookAndFeel() {
		return lookAndFeel;
	}
	public void setLookAndFeel(String lookAndFeel) {
		if (this.lookAndFeel == null || !this.lookAndFeel.equals(lookAndFeel)) {
			setSettingsChanged(true);
			this.lookAndFeel = lookAndFeel;
		}
	}
	/**
	 * @return Returns the startVlcAtStart.
	 */
	public boolean isStartVlcAtStart() {
		return startVlcAtStart;
	}
	/**
	 * @param startVlcAtStart
	 *            The startVlcAtStart to set.
	 */
	public void setStartVlcAtStart(boolean startVlcAtStart) {
		if (this.startVlcAtStart != startVlcAtStart) {
			setSettingsChanged(true);
			this.startVlcAtStart = startVlcAtStart;
		}
	}
	/**
	 * @return Returns the showLogWindow.
	 */
	public boolean isShowLogWindow() {
		return showLogWindow;
	}
	/**
	 * @param showLogWindow
	 *            The showLogWindow to set.
	 */
	public void setShowLogWindow(boolean showLogWindow) {
		if (this.showLogWindow != showLogWindow) {
			setSettingsChanged(true);
			this.showLogWindow = showLogWindow;
		}
	}

	public boolean isStartMinimized() {
		return startMinimized;
	}
	public void setStartMinimized(boolean startMinimized) {
		if (this.startMinimized != startMinimized) {
			this.startMinimized = startMinimized;
			setSettingsChanged(true);
		}
	}
    /**
     * @return Returns the skinLFTheme.
     */
    public String getSkinLFTheme() {
        if (skinLFTheme==null) {
            skinLFTheme=ControlMainView.skinLFThemes[0];
        }
        return skinLFTheme;
    }
    /**
     * @param skinLFTheme The skinLFTheme to set.
     */
    public void setSkinLFTheme(String skinLookAndFeel) {
        this.skinLFTheme = skinLookAndFeel;
    }
}