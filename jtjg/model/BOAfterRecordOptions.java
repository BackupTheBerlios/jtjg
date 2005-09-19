package model;

import java.io.Serializable;

/*
BOAfterRecordOptions.java by Geist Alexander 

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

public class BOAfterRecordOptions implements Serializable {
	
	private boolean useProjectX=false;
	private boolean useMplex=false;
	private int mplexOption=9;  //s. man mplex 9=DVD
	
	public BOAfterRecordOptions() {}

	public BOAfterRecordOptions(boolean useProjectX, boolean useMplex, int mplexOption) {
		this.setUseProjectX(useProjectX);
		this.setUseMplex(useMplex);
		this.setMplexOption(mplexOption);
	}

	/**
	 * @return Returns the mplexOption.
	 */
	public int getMplexOption() {
		return mplexOption;
	}
	/**
	 * @param mplexOption The mplexOption to set.
	 */
	public void setMplexOption(int mplexOption) {
		this.mplexOption = mplexOption;
	}
	/**
	 * @return Returns the useMplex.
	 */
	public boolean isUseMplex() {
		return useMplex;
	}
	/**
	 * @param useMplex The useMplex to set.
	 */
	public void setUseMplex(boolean useMplex) {
		this.useMplex = useMplex;
	}
	/**
	 * @return Returns the useProjectX.
	 */
	public boolean isUseProjectX() {
		return useProjectX;
	}
	/**
	 * @param useProjectX The useProjectX to set.
	 */
	public void setUseProjectX(boolean useProjectX) {
		this.useProjectX = useProjectX;
	}
}
