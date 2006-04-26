/*
GuiSettingsTabPlayback.java by Geist Alexander 

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
package presentation.settings;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import model.BOPlaybackOption;
import service.SerIconManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.settings.ControlSettingsTabPlayback;
import control.settings.ControlTabSettings;

public class GuiSettingsTabPlayback extends JPanel implements GuiSettingsTab {
    
    private Icon menuIcon;
    private ControlSettingsTabPlayback control;
    private JPanel panelPlaybackSettings = null;
	private JButton jButtonAnlegen = null;
	private JButton jButtonLoeschen = null;
	private JCheckBox cbUseStandardOption = null;
	private JRadioButton rbUseFirstAudioPid = null;
	private JRadioButton rbUseAc3 = null;
	private JRadioButton rbAskPids = null;
	private ButtonGroup buttonGroupAudioOptions = new ButtonGroup();
	
	private JList jListPlayer;
	public GuiPlayerListModel playerListModel;
	private JScrollPane jScrollPanePlayerList;
	private JPanel panelPlayer;
	private JButton jButtonAddPlayer = null;
	private JButton jButtonDeletePlayer = null;
	
	private JScrollPane jScrollPanePlaybackSettings = null;
	private JTable jTablePlaybackSettings = null;
	private GuiPlaybackSettingsTableModel playbackSettingsTableModel;
	private SerIconManager iconManager = SerIconManager.getInstance();
    
    public GuiSettingsTabPlayback(ControlSettingsTabPlayback ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}
    
    public void initialize() {
        FormLayout layout = new FormLayout(
				  "f:pref:grow",  		// columns 
				  "f:pref"); 			// rows
		PanelBuilder builder = new PanelBuilder(this, layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.add(this.getPanelPlaybackSettings(),		   		cc.xy(1,1));        
    }

    private JPanel getPanelPlaybackSettings() {
		if (panelPlaybackSettings == null) {
			panelPlaybackSettings = new JPanel();
			FormLayout layout = new FormLayout(
			        "pref:grow, pref:grow, 5, pref",	 		//columna 
			  "pref, 10, pref, pref, 15, pref, pref, 80, 10, pref, 10, pref, pref, pref, 30");	//rows
			PanelBuilder builder = new PanelBuilder(panelPlaybackSettings, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_playbackOptions"),			cc.xyw	(1, 1, 4));
			builder.add(new JLabel("z.B. xine http://$ip:31339/$vPid,$aPid"),				cc.xyw	(1, 3, 4));
			builder.add(new JLabel("z.B. d://programme/mplayer/mplayer.exe http://$ip:31339/$vPid,$aPid"),	cc.xywh	(1, 4, 3, 1));
			builder.add(this.getJScrollPanePlaybackSettings(),								cc.xywh	(1, 6, 2, 3));
			builder.add(this.getJButtonAnlegen(),											cc.xy	(4, 6));
			builder.add(this.getJButtonLoeschen(),											cc.xy	(4, 7));
			builder.add(this.getCbUseStandardOption(),										cc.xy	(1, 10));
			builder.add(this.getRbAskPids(),												cc.xy	(1, 12));
			builder.add(this.getRbUseAc3(),													cc.xy	(1, 13));
			builder.add(this.getRbUseFirstAudioPid(),										cc.xy	(1, 14));
			builder.addSeparator("Wiedergabe-Programme",									cc.xyw	(2, 10, 3));
			builder.add(this.getJScrollPanePlayerList(),									cc.xywh (2, 12, 1, 4));
			builder.add(this.getJButtonAddPlayer(),											cc.xy	(4, 12));
			builder.add(this.getJButtonDeletePlayer(),										cc.xy	(4, 13));
			
		}
		return panelPlaybackSettings;
	}
    
    /**
	 * This method initializes jScrollPanePlaybackSettings	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPanePlaybackSettings() {
		if (jScrollPanePlaybackSettings == null) {
			jScrollPanePlaybackSettings = new JScrollPane();
			jScrollPanePlaybackSettings.setPreferredSize(new Dimension(350, 150));
			jScrollPanePlaybackSettings.setViewportView(getJTablePlaybackSettings());
		}
		return jScrollPanePlaybackSettings;
	}
	
	/**
	 * This method initializes jTablePlaybackSettings	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	public JTable getJTablePlaybackSettings() {
		if (jTablePlaybackSettings == null) {
			playbackSettingsTableModel = new GuiPlaybackSettingsTableModel(control);
			jTablePlaybackSettings = new JTable(playbackSettingsTableModel);
			jTablePlaybackSettings.setName("playbackSettings");
			jTablePlaybackSettings.getColumnModel().getColumn(0).setPreferredWidth(100);
			jTablePlaybackSettings.getColumnModel().getColumn(0).setMaxWidth(100);
			jTablePlaybackSettings.getColumnModel().getColumn(1).setPreferredWidth(120);
			jTablePlaybackSettings.getColumnModel().getColumn(2).setMaxWidth(80);
			jTablePlaybackSettings.getColumnModel().getColumn(3).setMaxWidth(80);
			
			jTablePlaybackSettings.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() {
				
					public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
							int column) {
                        BOPlaybackOption playbackOption = (BOPlaybackOption)ControlMain.getSettingsPlayback().getPlaybackOptions().get(row);
                        JCheckBox checkbox = new JCheckBox();
                        checkbox.setHorizontalAlignment(SwingConstants.CENTER);
                        if (column==3) {
                            checkbox.setSelected(playbackOption.isStandard());
                            return checkbox;
                        } 
                        checkbox.setSelected(playbackOption.isLogOutput());
                        return checkbox;				}
				});
			
			jTablePlaybackSettings.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						int optionIndex = jTablePlaybackSettings.getSelectedRow();
						if (optionIndex>-1) {
							BOPlaybackOption option = (BOPlaybackOption)ControlMain.getSettingsPlayback().getPlaybackOptions().get(optionIndex);
							jListPlayer.setSelectedIndex(option.getModelPlayerIndex());	
						}
					}
				}
			});

		}
		return jTablePlaybackSettings;
	}
    
	/**
	 * This method initializes jButtonAnlegen	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButtonAnlegen() {
		if (jButtonAnlegen == null) {
			jButtonAnlegen = new JButton();
			jButtonAnlegen.setIcon(iconManager.getIcon("new.png"));
			jButtonAnlegen.setText(ControlMain.getProperty("button_create"));
			jButtonAnlegen.setActionCommand("add");
			jButtonAnlegen.addActionListener(control);
			jButtonAnlegen.setPreferredSize(new java.awt.Dimension(100,25));
		}
		return jButtonAnlegen;
	}
	/**
	 * This method initializes getJButtonLoeschen	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButtonLoeschen() {
		if (jButtonLoeschen == null) {
			jButtonLoeschen = new JButton();
			jButtonLoeschen.setIcon(iconManager.getIcon("trash.png"));
			jButtonLoeschen.setText(ControlMain.getProperty("button_delete"));
			jButtonLoeschen.setActionCommand("delete");
			jButtonLoeschen.addActionListener(control);
			jButtonLoeschen.setPreferredSize(new java.awt.Dimension(90,25));
		}
		return jButtonLoeschen;
	}
	
    /**
     * @return Returns the control.
     */
    public ControlTabSettings getControl() {
        return (ControlTabSettings)control;
    }
    /**
     * @param control The control to set.
     */
    public void setControl(ControlSettingsTabPlayback control) {
        this.control = control;
    }
    /**
     * @return Returns the playbackSettingsTableModel.
     */
    public GuiPlaybackSettingsTableModel getPlaybackSettingsTableModel() {
        return playbackSettingsTableModel;
    }
    /**
     * @return Returns the cbUseStandardOption.
     */
    public JCheckBox getCbUseStandardOption() {
        if (cbUseStandardOption == null) {
            cbUseStandardOption = new JCheckBox(ControlMain.getProperty("cbUseStandard"));
            cbUseStandardOption.setName("useStandard");
            cbUseStandardOption.addItemListener(control);
		}
        return cbUseStandardOption;
    }
    public JRadioButton getRbUseFirstAudioPid() {
        if (rbUseFirstAudioPid == null) {
        	rbUseFirstAudioPid = new JRadioButton(ControlMain.getProperty("rbUseFirstAudio"));
        	rbUseFirstAudioPid.setActionCommand("useFirstAudioPid");
        	rbUseFirstAudioPid.addActionListener(control);
        	buttonGroupAudioOptions.add(rbUseFirstAudioPid);
		}
        return rbUseFirstAudioPid;
    }
    public JRadioButton getRbUseAc3() {
        if (rbUseAc3 == null) {
        	rbUseAc3 = new JRadioButton(ControlMain.getProperty("rbUseAc3"));
        	rbUseAc3.setActionCommand("useAc3");
        	rbUseAc3.addActionListener(control);
        	buttonGroupAudioOptions.add(rbUseAc3);
		}
        return rbUseAc3;
    }
    public JRadioButton getRbAskPids() {
        if (rbAskPids == null) {
        	rbAskPids = new JRadioButton(ControlMain.getProperty("rbAskPids"));
        	rbAskPids.setActionCommand("askPids");
        	rbAskPids.addActionListener(control);
        	buttonGroupAudioOptions.add(rbAskPids);
		}
        return rbAskPids;
    }
    /**
     * @return Returns the menuIcon.
     */
    public Icon getIcon() {
        if (menuIcon==null) {
            menuIcon=SerIconManager.getInstance().getIcon("xeyes.png");
        }
        return menuIcon;
    }
    
    public String getMenuText() {
        return ControlMain.getProperty("label_playback");
    }
	/**
	 * @return Returns the jListPlayer.
	 */
	public JList getJListPlayer() {
		if (jListPlayer==null) {
			playerListModel = new GuiPlayerListModel();
			ArrayList player = ControlMain.getSettingsPlayback().getPlaybackPlayer();
			for (int i=0; i<player.size(); i++) {
				playerListModel.add(i, player.get(i));	
			}
			jListPlayer = new JList(playerListModel);
			jListPlayer.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
			jListPlayer.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						int playerIndex = jListPlayer.getSelectedIndex();
						int optionIndex = jTablePlaybackSettings.getSelectedRow();
						if (optionIndex>-1) {
							BOPlaybackOption option = (BOPlaybackOption)ControlMain.getSettingsPlayback()
								.getPlaybackOptions().get(optionIndex);
							option.setPlaybackPlayer((String)jListPlayer.getSelectedValue());
						}
					}
				}
			});
		}
		return jListPlayer;
	}
	/**
	 * @return Returns the jScrollPanePlayerList.
	 */
	public JScrollPane getJScrollPanePlayerList() {
		if (jScrollPanePlayerList == null) {
			jScrollPanePlayerList = new JScrollPane();
			jScrollPanePlayerList.setPreferredSize(new Dimension(300,100));
			jScrollPanePlayerList.setViewportView(this.getJListPlayer());
		}
		return jScrollPanePlayerList;
	}

	/**
	 * @return Returns the jButtonAddPlayer.
	 */
	public JButton getJButtonAddPlayer() {
		if (jButtonAddPlayer == null) {
			jButtonAddPlayer = new JButton();
			jButtonAddPlayer.setIcon(iconManager.getIcon("new.png"));
			jButtonAddPlayer.setText(ControlMain.getProperty("button_create"));
			jButtonAddPlayer.setActionCommand("addPlayer");
			jButtonAddPlayer.addActionListener(control);
		}
		return jButtonAddPlayer;
	}
	/**
	 * @return Returns the jButtonDeletePlayer.
	 */
	public JButton getJButtonDeletePlayer() {
		if (jButtonDeletePlayer == null) {
			jButtonDeletePlayer = new JButton();
			jButtonDeletePlayer.setIcon(iconManager.getIcon("trash.png"));
			jButtonDeletePlayer.setText(ControlMain.getProperty("button_delete"));
			jButtonDeletePlayer.setActionCommand("deletePlayer");
			jButtonDeletePlayer.addActionListener(control);
		}
		return jButtonDeletePlayer;
	}
}
