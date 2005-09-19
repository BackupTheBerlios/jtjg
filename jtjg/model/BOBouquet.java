package model;
/*
BOBouquet.java by Geist Alexander 

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

import java.io.IOException;
import java.util.ArrayList;

import control.ControlMain;

public class BOBouquet extends java.lang.Object {
    String bouquetNummer;
    String bouquetName;
    ArrayList sender;
 
    public BOBouquet(String bouquetNummer, String bouquetName) {		
		this.bouquetNummer = bouquetNummer;
		this.bouquetName   = bouquetName;		
    }
    
    public String getBouquetNummer() {
        return bouquetNummer;
    }   
	
    public void setBouquetNummer(String bouquetNummer) {
        this.bouquetNummer = bouquetNummer;
    }
    public String getBouquetName() {
        return bouquetName;
    }
	
    public void setBouquetName(String bouquetName) {
        this.bouquetName = bouquetName;
    }
	
    public void setAll(BOBouquet bobounquet){        
        this.bouquetNummer = bobounquet.getBouquetNummer();   
        this.bouquetName   = bobounquet.getBouquetName();
    }  

	/**
	 * @return ArrayList
	 */
	public ArrayList getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 * @param sender The sender to set
	 */
	public void setSender(ArrayList sender) {
		this.sender = sender;
	}
	
	public void readSender() throws IOException {
		if (this.getSender() == null || this.getSender().size()<0) {
			this.setSender(ControlMain.getBoxAccess().getSender(this));
		}
	}

}
