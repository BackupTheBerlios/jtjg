/*
BOPids.java by Geist Alexander 

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
package model;

import java.util.ArrayList;

import control.ControlMain;

public class BOPids {

    private BOPid vPid; //erste Position Pid, 2. Position Beschreibung
    private ArrayList aPids = new ArrayList(); //ArrayList von String[] Objekten, erste Position Pid, 2. Position Beschreibung
    private BOPid vtxtPid;
    private BOPid pmtPid;
    private ArrayList allPids;
   
    public int getPidCount() {
        int count = 0;
        if (this.getVPid()!= null) {
            count++;
        }
        if (this.getVtxtPid()!= null) {
            count++;
        }
        return this.getAPids().size()+count;
    }
    public String getAPidNumber(int index) {
        String[] aPid = (String[])this.getAPids().get(index);
        return aPid[0];
    }
    
    public String getAPidDescription(int index) {
        String[] aPid = (String[])this.getAPids().get(index);
        return aPid[1];
    }
    
    public BOPid getAc3Pid() {
    	for (int i=0; i<this.getAPids().size();i++) {
    		BOPid pid = (BOPid)this.getAPids().get(i);
    		if (pid.isAc3()) {
    			return pid;
    		}
    	}
    	return null;
    }
    
    public ArrayList getAllPids() {
        if (allPids==null) {
            allPids = new ArrayList();
            allPids.add(this.getVPid());
            for (int i=0; i<this.getAPids().size(); i++) {
                allPids.add(this.getAPids().get(i));
            }
            if (this.getVtxtPid()!=null) {
                allPids.add(this.getVtxtPid());
            }
        }
        return allPids;
    }
    
    public boolean includesStereoPid() {
        for (int i=0; i<this.getAPids().size(); i++) {
            BOPid pid = (BOPid)this.getAPids().get(i);
            if (!pid.isAc3()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean includesAc3Pid() {
        for (int i=0; i<this.getAPids().size(); i++) {
            BOPid pid = (BOPid)this.getAPids().get(i);
            if (pid.isAc3()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return indices of wanted pids
     */
    public int[] getIndices() {
        int[] ind = new int[this.getAllPids().size()];
        int indCount=1;
        ind[0]=0; //Videopid is always setted
        
        for (int i=1; i<this.getAllPids().size(); i++) {
            BOPid pid = (BOPid)this.getAllPids().get(i);
            while (true) {
                if (ControlMain.getSettingsRecord().isRecordAllPids() && pid.isAudio()) {
                    ind[indCount]=i;
                    indCount++;
                    break;
                }
                if (pid.isAc3() && ControlMain.getSettingsRecord().isAc3ReplaceStereo()) {
                    ind[indCount]=i;
                    indCount++;
                    break;
                }
                if (!pid.isAc3() && !pid.isTeletext() && ControlMain.getSettingsRecord().isStereoReplaceAc3()) {
                    ind[indCount]=i;
                    indCount++;
                    break;
                }
                if (!pid.isAc3() && !pid.isTeletext() && !this.includesAc3Pid()) {
                    ind[indCount]=i;
                    indCount++;
                    break;
                }
                if (pid.isTeletext() && ControlMain.getSettingsRecord().isRecordVtxt()) {
                    ind[indCount]=i;
                    indCount++;
                    break;
                }    
                break;
            }
        }
        int[] ind2 = new int[ind.length];
        System.arraycopy(ind, 0, ind2, 0, indCount );
        return ind2;
    }    
    /**
     * @return Returns the vtxtPid.
     */
    public BOPid getVtxtPid() {
        return vtxtPid;
    }
    /**
     * @param vtxtPid The vtxtPid to set.
     */
    public void setVtxtPid(BOPid vtxtPid) {
        this.vtxtPid = vtxtPid;
    }
    /**
     * @return Returns the aPids.
     */
    public ArrayList getAPids() {
        return aPids;
    }
    /**
     * @param pids The aPids to set.
     */
    public void setAPids(ArrayList pids) {
        aPids = pids;
    }
    /**
     * @return Returns the vPid.
     */
    public BOPid getVPid() {
        return vPid;
    }
    /**
     * @param pid The vPid to set.
     */
    public void setVPid(BOPid pid) {
        vPid = pid;
    }
    /**
     * @return Returns the pmtPid.
     */
    public BOPid getPmtPid() {
        return pmtPid;
    }
    /**
     * @param pmtPid The pmtPid to set.
     */
    public void setPmtPid(BOPid pmtPid) {
        this.pmtPid = pmtPid;
    }
}
