package control;
/*
ControlMainView by Alexander Geist

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
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import presentation.GuiLogWindow;
import presentation.GuiMainTabPane;
import presentation.GuiMainView;
import snoozesoft.systray4j.SysTrayMenuEvent;
import snoozesoft.systray4j.SysTrayMenuListener;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticTheme;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

import control.settings.ControlSettingsTabMain;

/**
 * Control-Klasse des Haupt-Fensters, beinhaltet und verwaltet das MainTabPane
 * Klasse wird beim Start der Anwendung initialisiert und ist immer verfügbar
 */
public class ControlMainView implements ChangeListener, SysTrayMenuListener, ActionListener {
	
	GuiMainView view;
	public static final String[] themes = {"Silver", "BrownSugar", "DarkStar", "DesertBlue",
	        "ExperienceBlue", "SkyBluerTahoma", "SkyRed"};
	public static String[] skinLFThemes;
	public static HashMap skinLFThemesMap;
	
	public void initialize() {
	    this.initLookAndFeel();
	    this.setLookAndFeel();
	    this.setView(new GuiMainView(this));
	    Logger.getLogger("ControlMainView").info(ControlMain.getProperty("msg_app_starting"));
	}
	
	public void checkForStartWizard() {
		if (ControlMain.getSettingsMain().getBoxList().size()==0) { //First start, go to Settings-Tab
	        this.getView().getMainTabPane().setSelectedIndex(6);
	        ControlSettingsTabMain ctrl = 
	        	(ControlSettingsTabMain)this.getView().getTabSettings().getSettingsTabMain().getControl();
	        ctrl.startWizard(); 
	    }
	}
	
	public void actionPerformed(ActionEvent e) {
	    GuiLogWindow.switchLogVisiblity();
	}
	/*
	 * Installiere das jgoodies l&f
	 */
	private void initLookAndFeel() {
		try {
		    PlasticLookAndFeel l2 = new PlasticLookAndFeel();
			UIManager.LookAndFeelInfo info2 = new UIManager.LookAndFeelInfo(l2.getName(), PlasticLookAndFeel.class.getName());
			UIManager.installLookAndFeel(info2);
			
			Plastic3DLookAndFeel l3 = new Plastic3DLookAndFeel();
			UIManager.LookAndFeelInfo info3 = new UIManager.LookAndFeelInfo(l3.getName(), Plastic3DLookAndFeel.class.getName());
			UIManager.installLookAndFeel(info3);
			
			PlasticXPLookAndFeel l = new PlasticXPLookAndFeel();
			UIManager.LookAndFeelInfo info = new UIManager.LookAndFeelInfo(l.getName(), PlasticXPLookAndFeel.class.getName());
			UIManager.installLookAndFeel(info);
			
			WindowsLookAndFeel l4 = new WindowsLookAndFeel();
			UIManager.LookAndFeelInfo info4 = new UIManager.LookAndFeelInfo(l4.getName(), WindowsLookAndFeel.class.getName());
			UIManager.installLookAndFeel(info4);
		} catch (Exception e1) {
		    Logger.getLogger("ControlMainView").error(e1.getMessage());
		}
	}

	public void setLookAndFeel() {
	    String lookAndFeel = ControlMain.getSettings().getMainSettings().getLookAndFeel();
	    String current = UIManager.getLookAndFeel().getClass().getName();
		boolean lfChanged = !current.equals(lookAndFeel);

        try {
            boolean themeChanged = this.isPlasticThemeChanged();
			if (themeChanged) {
				PlasticTheme inst = (PlasticTheme) (Class.forName("com.jgoodies.plaf.plastic.theme."
						+ ControlMain.getSettings().getMainSettings().getPlasticTheme())).newInstance();
				PlasticLookAndFeel.setMyCurrentTheme(inst);
			}

			if (lfChanged || themeChanged) {
				UIManager.setLookAndFeel(lookAndFeel);
				if (lookAndFeel.indexOf("WindowsLookAndFeel") > -1 || lookAndFeel.indexOf("WindowsClassicLookAndFeel") > -1) {
					UIManager.put("TextArea.font", new Font("Tahoma", Font.PLAIN, 11));
				}
				if (this.getView()!=null) {
				    this.getView().repaintGui();    
				}
			}
	    } catch (Exception e) {
	        Logger.getLogger("ControlMainView").error(e.getMessage());
	    }    
	}
	
	private boolean isPlasticThemeChanged() {
	    String currentTheme = PlasticLookAndFeel.getMyCurrentTheme().getClass().getName();
		currentTheme = currentTheme.substring(currentTheme.lastIndexOf(".") + 1);
		return !currentTheme.equals(ControlMain.getSettings().getMainSettings().getPlasticTheme());    
	}
	
	/**
	 * Change-Events of the MainTabPane
	 */
	public void stateChanged(ChangeEvent event) {
		GuiMainTabPane pane = (GuiMainTabPane)event.getSource();
		int count = pane.getSelectedIndex(); //number of selected Tab
				
		while (true) {
			//Change-Events bei betreten neuer Tabs
		    if (count == 0) { //StartTab
				pane.setComponentAt(count, pane.getTabStart());
				new Thread(pane.getTabStart().getControl()).start();
				break;
			}
            if (count == 1) { //ProgrammTab
                pane.setComponentAt(count, pane.getTabProgramm());
                break;
            }
			if (count == 2) { //TimerTab
                pane.setComponentAt(count, pane.getTabTimer());
				new Thread(pane.getTabTimer().getControl()).start();
				break;
			}
			if (count == 3) { //MovieGuideTab
				pane.getTabMovieGuide().getControl().askToDownloadMG();
				break;
			}
			break;
		}


		pane.setIndex(count);
	}
	public void iconLeftDoubleClicked( SysTrayMenuEvent e ) {}
	
	public void iconLeftClicked( SysTrayMenuEvent e ){
	    if( this.getView().isVisible() ) {
	        this.getView().setVisible(false);
	    } else {
	    	this.getView().setState(Frame.NORMAL);
	        this.getView().setVisible(true);    
	        this.getView().toFront();
	    }
	}
	
	public void menuItemSelected( SysTrayMenuEvent e ) {
		while (true) {
			if( e.getActionCommand().equals( "exit" ) ) {
				ControlMain.endProgram();
	        	break;
	        }
	        if( e.getActionCommand().equals( "about" ) ) {
	            JOptionPane.showMessageDialog( this.getView(), ControlMain.version[0] );
	            break;
	        }
	        if( e.getActionCommand().equals( "open" ) ) {
	        	this.getView().setState(Frame.NORMAL);
		        this.getView().setVisible(true);    
		        this.getView().toFront();
		        break;
	        }	
	        break;
		}
    }
	/**
	 * @return Returns the tabSettings.
	 */
	public GuiMainView getView() {
		return view;
	}
	/**
	 * @param tabSettings The tabSettings to set.
	 */
	public void setView(GuiMainView view) {
		this.view = view;
	}	
}
