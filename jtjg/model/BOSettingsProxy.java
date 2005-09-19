package model;
/*
BOSettingsProxy.java by Geist Alexander 

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

public class BOSettingsProxy {
	private BOSettings settings;
    private boolean use;
    private String host;
    private String port;
    private String userPass;

    public BOSettingsProxy(BOSettings settings) {
    	this.setSettings(settings);
    }

	/**
	 * @return Returns the host.
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return Returns the port.
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(String port) {
		this.port = port;
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
	/**
	 * @return Returns the use.
	 */
	public boolean isUse() {
		return use;
	}
	/**
	 * @param use The use to set.
	 */
	public void setUse(boolean use) {
		this.use = use;
	}
	/**
	 * @return Returns the userPass.
	 */
	public String getUserPass() {
		return userPass;
	}
	/**
	 * @param userPass The userPass to set.
	 */
	public void setUserpass(String user, String passwort) {
        this.userPass = new sun.misc.BASE64Encoder().encode( (user + ":" +
            passwort).getBytes());
    }
}
