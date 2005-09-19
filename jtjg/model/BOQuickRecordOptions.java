package model;

import java.util.Date;

/*
BOQuickRecordOptions.java by Geist Alexander 

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

public class BOQuickRecordOptions {

    private BOPids pids;
    private Date stopTime;
    
    public BOQuickRecordOptions(BOPids pids, Date minutes) {
        this.setPids(pids);
        this.setStopTime(minutes);
    }
    
    /**
     * @return Returns the stopTime.
     */
    public Date getStopTime() {
        return stopTime;
    }
    /**
     * @param stopTime The stopTime to set.
     */
    public void setStopTime(Date minutes) {
        this.stopTime = minutes;
    }
    /**
     * @return Returns the pids.
     */
    public BOPids getPids() {
        return pids;
    }
    /**
     * @param pids The pids to set.
     */
    public void setPids(BOPids pids) {
        this.pids = pids;
    }
}
