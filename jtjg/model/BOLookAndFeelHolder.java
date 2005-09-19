package model;

/**This program is free software; you can redistribute it and/or modify
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
 * 
 * 
 * @author Reinhard Achleitner
 * @version 24.11.2004
 *
 */
public class BOLookAndFeelHolder {

	private String lookAndFeelName;
	
	
	private String lookAndFeelClassName;
	/**
	 * 
	 */
	public BOLookAndFeelHolder() {
		super();
	}
	
	/**
	 * @param name
	 * @param className
	 */
	public BOLookAndFeelHolder(String name, String className) {		
		super();
		setLookAndFeelName(name);
		setLookAndFeelClassName(className);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getLookAndFeelName();
	}
	
	public String getLookAndFeelClassName() {
		return lookAndFeelClassName;
	}
	public void setLookAndFeelClassName(String lookAndFeelClassName) {
		this.lookAndFeelClassName = lookAndFeelClassName;
	}
	public String getLookAndFeelName() {
		return lookAndFeelName;
	}
	public void setLookAndFeelName(String lookAndFeelName) {
		this.lookAndFeelName = lookAndFeelName;
	}

}
