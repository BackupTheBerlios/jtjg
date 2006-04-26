/*
ControlSettingsTabPlayback.java by Geist Alexander 

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
package control.settings;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import control.ControlMain;

import model.BOSettingsMovieGuide;
import presentation.GuiMainView;
import presentation.settings.GuiSettingsTabMovieGuide;
import presentation.settings.GuiTabSettings;

public class ControlSettingsTabMovieGuide extends ControlTabSettings implements ActionListener, ItemListener,ListSelectionListener {
    
    GuiTabSettings settingsTab;
	
    public static final int MGLOADTYPE_AUTO = 0;
	public static final int MGLOADTYPE_ASK = 1;
	
	public static final int MGDEFAULTDATE_ALL = 0;
	public static final int MGDEFAULTDATE_CURRENT = 1;
    
    public ControlSettingsTabMovieGuide (GuiTabSettings tabSettings) {
		this.setSettingsTab(tabSettings);
	}
    
    /* (non-Javadoc)
     * @see control.ControlTab#initialize()
     */
    public void run() {
       
    	getTab().getStoreOriginal().setSelected(getSettings().isMgStoreOriginal());    	
    	int down = getSettings().getMgLoadType();
    	if (down == MGLOADTYPE_AUTO)
    	{
    		getTab().getDownloadAuto().setSelected(true);
    	}
    	else if (down == MGLOADTYPE_ASK)
    	{
    		getTab().getDownloadQuestion().setSelected(true);
    	}
    	
    	int def = getSettings().getMgDefault();
    	if (def == MGDEFAULTDATE_ALL)
    	{
    		getTab().getCompleteMG().setSelected(true);
    	}
    	else if (def == MGDEFAULTDATE_CURRENT)
    	{
    		getTab().getDayMG().setSelected(true);
    	}
    	
    	ArrayList channels = getSettings().getMgSelectedChannels();
    	JList list = getTab().getChannelList();
    	if (channels == null)
    	{
    		// select all
    		list.addSelectionInterval(0,list.getModel().getSize() - 1);
    	}
    	else
		{
    		Iterator it = channels.iterator();
    		while (it.hasNext()) {
				Object element = it.next();
				list.setSelectedValue(element,false);
			}
		}
    
    }

    public void actionPerformed(ActionEvent e) {
    	String action = e.getActionCommand();
    	if (action.equals("completeMG"))
    	{
    		getSettings().setMgDefault(MGDEFAULTDATE_ALL);
    	}
    	else if (action.equals("currentDayMG"))
    	{
    		getSettings().setMgDefault(MGDEFAULTDATE_CURRENT);
    	}
    	else if (action.equals("downloadAuto"))
    	{
    		getSettings().setMgLoadType(MGLOADTYPE_AUTO);
    	}
    	else if (action.equals("downloadQuestion"))
    	{
    		getSettings().setMgLoadType(MGLOADTYPE_ASK);
    	}    	
	}
    
//  Change-Events der der Checkbox
	public void itemStateChanged (ItemEvent event) {
		Component comp = (Component) event.getSource();
		if (comp.getName().equals("saveOriginal")){
			getSettings().setMgStoreOriginal(((JCheckBox)comp).isSelected());
		}		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		Component comp = (Component) e.getSource();
		if (comp.getName().equals("channelList"))
		{
			Object[] sel = ((JList)comp).getSelectedValues();
			ArrayList l = new ArrayList();
			l.addAll(Arrays.asList(sel));
			getSettings().setMgSelectedChannels(l);
		}
		
	}	

    
    public GuiMainView getMainView() {
        return this.getSettingsTab().getControl().getMainView();
    }
    
    /* (non-Javadoc)
     * @see control.ControlTab#getMainView()
     */
    public GuiTabSettings getSettingsTab() {
        return settingsTab;
    }

    /* (non-Javadoc)
     * @see control.ControlTab#setMainView(presentation.GuiMainView)
     */
    public void setSettingsTab(GuiTabSettings tabSettings) {
        settingsTab = tabSettings;
    }
    private BOSettingsMovieGuide getSettings() {
        return ControlMain.getSettingsMovieGuide();
    }
    
    private GuiSettingsTabMovieGuide getTab() {
        return this.getSettingsTab().getSettingsTabMovieGuide();
    }
}
