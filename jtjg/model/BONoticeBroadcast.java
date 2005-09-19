package model;
/*
BONoticeBroadcast.java by Geist Alexander 

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

public class BONoticeBroadcast {

	String searchString;
	boolean searchEpg=true;
	boolean searchMovieGuide=false;
	boolean searchOnlyTitle=true;
	boolean buildTimer=true;

	/**
	 * @return Returns the searchString.
	 */
	public String getSearchString() {
		return searchString;
	}
	/**
	 * @param searchString The searchString to set.
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	/**
	 * @return Returns the buildTimer.
	 */
	public boolean isBuildTimer() {
		return buildTimer;
	}
	/**
	 * @param buildTimer The buildTimer to set.
	 */
	public void setBuildTimer(boolean buildTimer) {
		this.buildTimer = buildTimer;
	}
	/**
	 * @return Returns the searchEpg.
	 */
	public boolean isSearchEpg() {
		return searchEpg;
	}
	/**
	 * @param searchEpg The searchEpg to set.
	 */
	public void setSearchEpg(boolean searchEpg) {
		this.searchEpg = searchEpg;
	}
	/**
	 * @return Returns the searchMovieGuide.
	 */
	public boolean isSearchMovieGuide() {
		return searchMovieGuide;
	}
	/**
	 * @param searchMovieGuide The searchMovieGuide to set.
	 */
	public void setSearchMovieGuide(boolean searchMovieGuide) {
		this.searchMovieGuide = searchMovieGuide;
	}
	/**
	 * @return Returns the searchOnlyTitle.
	 */
	public boolean isSearchOnlyTitle() {
		return searchOnlyTitle;
	}
	/**
	 * @param searchOnlyTitle The searchOnlyTitle to set.
	 */
	public void setSearchOnlyTitle(boolean searchOnlyTitle) {
		this.searchOnlyTitle = searchOnlyTitle;
	}
}
