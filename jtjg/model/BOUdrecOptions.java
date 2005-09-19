package model;
/*
 * BOUdrecOptions.java by Geist Alexander
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

import java.io.Serializable;
import java.util.ArrayList;


public class BOUdrecOptions implements Serializable {

	private String[] optionList;
	private String buffer;
	private String aSplit;
	private String vSplit;
	private String udpPort;
	private String tcpPort;

	public BOUdrecOptions() {
	}
	
	public BOUdrecOptions(String[] options) {
		for (int i = 0; i < options.length; i++) {
			if (options[i].equals("-tcp")) {
				this.setTcpPort(options[++i]);
			} else if (options[i].equals("-udp")) {
				this.setUdpPort(options[++i]);
			} else if (options[i].equals("-vsplit")) {
				this.setVSplit(options[++i]);
			} else if (options[i].equals("-asplit")) {
				this.setASplit(options[++i]);
			} else if (options[i].equals("-buf")) {
				this.setBuffer(options[++i]);
			}
		}
		this.setOptionList(new String[options.length-10]);
		System.arraycopy(options, 10, this.getOptionList(), 0, this.getOptionList().length);
	}
	
	public String toString() {
	    String ret = "-asplit "+getASplit()+" -vsplit "+getVSplit()+" -buf "+getBuffer()+" -tcp "+getTcpPort()+" -udp "+getUdpPort();
	    for (int i=0; i<getOptionList().length; i++) {
	        ret = ret.concat(" "+getOptionList()[i]);
	    }
	    return ret;
	}
	
	public ArrayList toStringList() {
	    ArrayList ret = new ArrayList(5+optionList.length);
        if (!getASplit().equals("-1")) {
            ret.add("-asplit");
            ret.add(getASplit());   
        }
        if (!getVSplit().equals("-1")) {
            ret.add("-vsplit");
            ret.add(getVSplit());   
        }
	    ret.add("-buf");
	    ret.add(getBuffer());
	    ret.add("-tcp");
	    ret.add(getTcpPort());
	    ret.add("-udp");
	    ret.add(getUdpPort());
	    for (int i=0; i<getOptionList().length; i++) {
	        ret.add(getOptionList()[i]);
	    }
	    return ret;
	}

	/**
	 * @param split The vSplit to set.
	 */
	public void setVSplit(String split) {
		vSplit = split;
	}

	/**
	 * @return Returns the buffer.
	 */
	public String getBuffer() {
		if (buffer==null) {
			buffer="16";
		}
		return buffer;
	}

	/**
	 * @param buffer The buffer to set.
	 */
	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}

	/**
	 * @param split The aSplit to set.
	 */
	public void setASplit(String split) {
		aSplit = split;
	}

	/**
	 * @return Returns the aSplit.
	 */
	public String getASplit() {
		if (aSplit==null) {
			aSplit="-1";
		}
		return aSplit;
	}
	
	/**
	 * @return Returns the optionList.
	 */
	public String[] getOptionList() {
	    if (optionList==null) {
	        optionList=new String[0];
	    }
		return optionList;
	}

	/**
	 * @param optionList The optionList to set.
	 */
	public void setOptionList(String[] options) {
		this.optionList = options;
	}

	/**
	 * @return Returns the vSplit.
	 */
	public String getVSplit() {
		if (vSplit==null) {
			vSplit="-1";
		}
		return vSplit;
	}

	/**
	 * @return Returns the tcpPort.
	 */
	public String getTcpPort() {
		if (tcpPort==null) {
			tcpPort="31340";
		}
		return tcpPort;
	}
	/**
	 * @param tcpPort The tcpPort to set.
	 */
	public void setTcpPort(String tcpPort) {
		this.tcpPort = tcpPort;
	}
	/**
	 * @return Returns the udpPort.
	 */
	public String getUdpPort() {
		if (udpPort==null) {
			udpPort="31341";
		}
		return udpPort;
	}
	/**
	 * @param udpPort The udpPort to set.
	 */
	public void setUdpPort(String udpPort) {
		this.udpPort = udpPort;
	}
}
