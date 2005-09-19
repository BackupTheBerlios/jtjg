package model;

import control.ControlMain;

/*
 * BOBox.java by Geist Alexander
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
public class BOBox {

	public String dboxIp; //Defaultwert
	public String login = "root"; //Defaultwert
	public String password = "dbox2"; //Defaultwert
	public boolean standard = false;
	private boolean selected;

	/**
	 * @return Returns the dboxIp.
	 */
	public String getDboxIp() {
		return dboxIp;
	}
	/**
	 * @param dboxIp
	 *            The dboxIp to set.
	 */
	public void setDboxIp(String dboxIp) {
		if (this.dboxIp == null || !this.dboxIp.equals(dboxIp)) {
			this.dboxIp = dboxIp;
			this.setSettingsChanged(true);
		}
	}
	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login
	 *            The login to set.
	 */
	public void setLogin(String login) {
		if (this.login == null || !this.login.equals(login)) {
			this.login = login;
			this.setSettingsChanged(true);
		}
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		if (this.password == null || !this.password.equals(password)) {
			this.password = password;
			this.setSettingsChanged(true);
		}
	}
	/**
	 * @return Returns the standard.
	 */
	public boolean isStandard() {
		return standard;
	}
	/**
	 * @param standard
	 *            The standard to set.
	 */
	public void setStandard(boolean standard) {
		if (this.standard != standard) {
			this.standard = standard;
			this.setSettingsChanged(true);
		}
	}
	/**
	 * @return Returns the selected.
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * @param selected
	 *            The selected to set.
	 */
	public void setSelectedBox(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @param settingsChanged
	 *            The settingsChanged to set.
	 */
	public void setSettingsChanged(boolean settingsChanged) {
		if (ControlMain.getSettings() != null) {
			ControlMain.getSettings().setSettingsChanged(settingsChanged);
		}
	}
}