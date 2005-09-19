package model;
/*
BOPidsjava by Geist Alexander 

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
public class BOPid {

	private String number, name;
	private int id; //0=video, 1=audio, 2=vtxt, 3=pmt
	
	public BOPid(String number, String name, int id) { 
		this.setName(name);
		this.setNumber(number);
		this.setId(id); 
	}
	
	public boolean isVideo() {
	    return this.getId()==0;
	}
	
	public boolean isAudio() {
	    return this.getId()==1;
	}
	
	public boolean isTeletext() {
	    return this.getId()==2;
	}
	
	public boolean isAc3() {
	    if (this.isAudio() && (this.getName().indexOf("AC3")>-1 || 
	    		this.getName().indexOf("Dolby")>-1 || this.getName().indexOf("dolby")>-1 )) {
	        return true;
	    }
	    return false;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the number.
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number The number to set.
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String toString() {
          return getNumber()+" "+getName();
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
}
