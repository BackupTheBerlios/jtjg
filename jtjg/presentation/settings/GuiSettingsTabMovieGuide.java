/*
 * GuiSettingsTabMovieGuide.java by Henneberger Ralph
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
package presentation.settings;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import service.SerIconManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;

import control.settings.ControlSettingsTabMovieGuide;
import control.settings.ControlTabSettings;

public class GuiSettingsTabMovieGuide extends JPanel implements GuiSettingsTab {

    private Icon menuIcon;
	private ControlSettingsTabMovieGuide control;
	private JPanel panelMovieguideSettings = null;
	private ButtonGroup downloadGroup = new ButtonGroup();
	private ButtonGroup dateRangeGroup = new ButtonGroup();

	private JRadioButton downloadAuto;
	private JRadioButton downloadQuestion;
	private JRadioButton completeMG;
	private JRadioButton dayMG;

	private JCheckBox storeOriginal;	
	private JList channelList;	
	private SerIconManager iconManager = SerIconManager.getInstance();

	private JPanel defaultShowPanel;
	private JPanel downloadPanel;
	
	public GuiSettingsTabMovieGuide(ControlSettingsTabMovieGuide ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}

	public void initialize() {
		FormLayout layout = new FormLayout("f:pref:grow", // columns
				"f:pref"); // rows
		PanelBuilder builder = new PanelBuilder(layout,this);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.add(this.getPanelMovieguideSettings(), cc.xy(1, 1));
	}

	private JPanel getPanelMovieguideSettings() {
		if (panelMovieguideSettings == null) {
			panelMovieguideSettings = new JPanel();
			FormLayout layout = new FormLayout("445,10,pref,pref", //columna
					"pref, 5, pref,10,pref,5,pref,15,pref,pref,10,pref,5,pref,5,pref,5,pref,pref,pref"); //rows
			PanelBuilder builder = new PanelBuilder(layout,panelMovieguideSettings);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_mgchannels"), cc.xywh(1, 1, 1, 1));
			builder.add(new JScrollPane(getChannelList()), cc.xywh(1, 3, 1, 1));

			builder.addSeparator("Download MG", cc.xywh(1, 5, 1, 1));
			builder.add(getDownloadPanel(), cc.xywh(1, 7, 1, 1));
			builder.add(getStoreOriginal(), cc.xywh(1, 9, 1, 1));
			builder.addSeparator(ControlMain.getProperty("label_mgdefaultshow"), cc.xywh(1, 12, 1, 1));
			builder.add(getDefaultShowPanel(), cc.xywh(1, 14, 1, 1));			
		}
		return panelMovieguideSettings;
	}

	/**
	 * @return
	 */
	public JCheckBox getStoreOriginal() {
		if (storeOriginal == null) {
			storeOriginal = new JCheckBox(ControlMain.getProperty("label_mgsaveoriginal"));
			storeOriginal.setName("saveOriginal");
			storeOriginal.addItemListener(control);
		}
		return storeOriginal;
	}	
	/**
	 * @return
	 */
	private JPanel getDefaultShowPanel() {

		if (defaultShowPanel == null) {
			defaultShowPanel = new JPanel(new GridLayout(0,1));
			completeMG = new JRadioButton(ControlMain.getProperty("label_mgcomplete"));
			completeMG.setActionCommand("completeMG");
			completeMG.addActionListener(control);
			dayMG = new JRadioButton(ControlMain.getProperty("label_mgcurrentday"));
			dayMG.setActionCommand("currentDayMG");
			dayMG.addActionListener(control);
			dateRangeGroup.add(completeMG);
			dateRangeGroup.add(dayMG);
			defaultShowPanel.add(completeMG);
			defaultShowPanel.add(dayMG);
		}
		return defaultShowPanel;
	}

	/**
	 * @return
	 */
	private JPanel getDownloadPanel() {
		if (downloadPanel == null) {
			downloadPanel = new JPanel(new GridLayout(0,1));
			downloadAuto = new JRadioButton(ControlMain.getProperty("label_mgdownloadauto"));
			downloadAuto.addActionListener(control);
			downloadAuto.setActionCommand("downloadAuto");
			downloadQuestion = new JRadioButton(ControlMain.getProperty("label_mgdownloadquestion"));
			downloadQuestion.setActionCommand("downloadQuestion");
			downloadQuestion.addActionListener(control);
			downloadGroup.add(downloadAuto);
			downloadGroup.add(downloadQuestion);
			downloadPanel.add(downloadAuto);
			downloadPanel.add(downloadQuestion);
		}
		return downloadPanel;
	}

	/**
	 * @return
	 */
	public JList getChannelList() {
		if (channelList == null) {
			String[] channels = new String[]{"13TH STREET", "CLASSICA", "DISNEY CHANNEL", "FOX KIDS", "HEIMATKANAL", "HIT24", "JUNIOR",
					"MGM", "PREMIERE 1", "PREMIERE 2", "PREMIERE 3", "PREMIERE 4", "PREMIERE 5", "PREMIERE 6", "PREMIERE 7",
					"PREMIERE KRIMI", "PREMIERE NOSTALGIE", "PREMIERE SERIE", "PREMIERE START", "SCI FI"};

			channelList = new JList(channels);
			channelList.setLayoutOrientation(JList.VERTICAL_WRAP);
			channelList.setFixedCellWidth(145);
			channelList.setName("channelList");
			channelList.addListSelectionListener(control);
			channelList.setCellRenderer(new DefaultListCellRenderer() {
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					if (value == null) value = "";
					JCheckBox box = new JCheckBox(value.toString());
					box.setPreferredSize(new Dimension(100,15));
					box.setOpaque(false);
					if (isSelected) {
						box.setSelected(true);
					}
					return box;
				}

			});

			channelList.setSelectionModel(new DefaultListSelectionModel() {
				public void setSelectionInterval(int index0, int index1) {
					if (super.isSelectedIndex(index0)) {
						super.removeSelectionInterval(index0, index0);
					} else {
						super.addSelectionInterval(index0, index1);
					}

				}

			});
		}
		return channelList;
	}
    /**
     * @return Returns the control.
     */
    public ControlTabSettings getControl() {
        return (ControlTabSettings)control;
    }
	/**
	 * @param control
	 *            The control to set.
	 */
	public void setControl(ControlSettingsTabMovieGuide control) {
		this.control = control;
	}
	
	public JRadioButton getCompleteMG() {
		return completeMG;
	}
	public JRadioButton getDayMG() {
		return dayMG;
	}
	public JRadioButton getDownloadAuto() {
		return downloadAuto;
	}
	public JRadioButton getDownloadQuestion() {
		return downloadQuestion;
	}	
    /**
     * @return Returns the menuIcon.
     */
    public Icon getIcon() {
        if (menuIcon==null) {
            menuIcon=SerIconManager.getInstance().getIcon("video.gif");
        }
        return menuIcon;
    }
    
    public String getMenuText() {
        return ControlMain.getProperty("tab_movieGuide");
    }    
}