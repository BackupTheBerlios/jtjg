package presentation.settings;
/*
GuiSettingsTabRecord.java by Geist Alexander 

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

import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import service.SerIconManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.ControlSettingsTabRecord;
import control.ControlTabSettings;

public class GuiSettingsTabRecord extends JPanel implements GuiSettingsTab {

    private Icon menuIcon;
	private ControlSettingsTabRecord control;
	private JPanel panelEngineSettings = null;
	private JPanel panelRecordSettings = null;	
	private JPanel panelQuickRecordSettings = null;
	private JPanel panelRecordtimeSettings = null;
	private JPanel panelFileNameSettings;
    private JPanel panelSaveOptions;
	private JPanel panelNorth = null;
	private JPanel panelSouth = null;
	private JComboBox jComboBoxStreamType = null;
	private JTextField jTextFieldUdrecOptions = null;
    private JRadioButton jRadioButtonSaveLocal;
    private JRadioButton jRadioButtonSaveBox;
	private JRadioButton jRadioButtonUdrec;
	private JRadioButton jRadioButtonJGrabber;
    private JRadioButton jRadioButtonVlc;
	private JRadioButton jRadioButtonRecordAllPids;
	private JRadioButton jRadioButtonAC3ReplaceStereo;
	private JRadioButton jRadioButtonStereoReplaceAc3;
	private ButtonGroup buttonGroupStreamingEngine = new ButtonGroup();
	private ButtonGroup buttonGroupAudioOptions = new ButtonGroup();
    private ButtonGroup buttonGroupSaveOptions = new ButtonGroup();
	private JCheckBox cbRecordVtxt;
	private JCheckBox cbShutdownAfterRecord;
	private JCheckBox cbStopPlaybackAtRecord;
	private JCheckBox cbStoreEPG;
	private JCheckBox cbStoreLogAfterRecord;
	private JSpinner recordMinsBefore, recordMinsAfter;

	private JButton jButtonTest;
	private JButton jButtonDirTag;
	private JButton jButtonFileTag;
	private JButton jButtonUdrecOptions;
	private JButton jButtonAfterRecordOptions;
	
	private JTextField tfDirPattern;
	private JTextField tfFilePattern;
	
	private SerIconManager iconManager = SerIconManager.getInstance();

	public GuiSettingsTabRecord(ControlSettingsTabRecord ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}

	public void initialize() {
		FormLayout layout = new FormLayout("f:pref:grow, 10 f:pref:grow", // columns
				"pref, 15, pref, 25, pref, 25, pref, 15, t:pref"); // rows
		PanelBuilder builder = new PanelBuilder(this, layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.addSeparator("<HTML><font size=5>"+ControlMain.getProperty("label_recordSettings1")+"</font><HTML>", cc.xyw(1, 1, 3));
		builder.add(this.getPanelNorth(), cc.xyw(1, 3, 3));
		builder.add(this.getPanelFileNameSettings(), cc.xy(1, 5));	
	}

	private JPanel getPanelNorth() {
		if (panelNorth == null) {
			panelNorth = new JPanel();
			FormLayout layout = new FormLayout("pref, 25, pref:grow", //columns
					"pref, 10, t:pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelNorth, layout);
			CellConstraints cc = new CellConstraints();

            builder.add(this.getPanelSaveOptions(), cc.xyw(1, 1, 3));
			builder.add(this.getPanelRecordSettings(), cc.xy(1, 3));
			builder.add(this.getPanelEngineSettings(), cc.xy(3, 3));
		}
		return panelNorth;
	}

    private JPanel getPanelSaveOptions() {
        if (panelSaveOptions == null) {
            panelSaveOptions = new JPanel();
            FormLayout layout = new FormLayout("pref, 20, pref, 10, pref", //columns
                    "t:pref"); //rows
            PanelBuilder builder = new PanelBuilder(panelSaveOptions, layout);
            CellConstraints cc = new CellConstraints();

            builder.addLabel(ControlMain.getProperty("label_saveOption"),   cc.xy(1, 1));
            builder.add(this.getJRadioButtonSaveLocal(), cc.xy(3, 1));
            builder.add(this.getJRadioButtonSaveBox(), cc.xy(5, 1));
        }
        return panelSaveOptions;
    }
	
	private JPanel getPanelRecordSettings() {
		if (panelRecordSettings == null) {
			panelRecordSettings = new JPanel();
			FormLayout layout = new FormLayout("pref:grow, 5, pref", //columns
					"pref, pref, pref, pref, pref, pref, 10, pref, pref, pref, 15, pref, 5, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelRecordSettings, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_recordSettings"), cc.xyw(1, 1, 3));
			builder.add(this.getCbStoreEPG(), cc.xyw(1, 2, 3));
			builder.add(this.getCbStoreLogAfterRecord(), cc.xyw(1, 3, 3));
			builder.add(this.getCbStopPlaybackAtRecord(), cc.xyw(1, 4, 3));

			builder.add(this.getCbRecordVtxt(), cc.xyw(1, 5, 3));
			builder.add(this.getJRadioButtonRecordAllPids(), cc.xyw(1, 8, 3));
			builder.add(this.getJRadioButtonAC3ReplaceStereo(), cc.xyw(1, 9, 3));
			builder.add(this.getJRadioButtonStereoReplaceAc3(), cc.xyw(1, 10, 3));
			
			builder.add(this.getCbShutdownAfterRecord(), cc.xyw(1, 12, 3));
            
            builder.addLabel(ControlMain.getProperty("label_afterRecord"), cc.xy(1, 14));
            builder.add(this.getJButtonAfterRecordOptions(), cc.xy(3, 14));
		}
		return panelRecordSettings;
	}

	private JPanel getPanelQuickRecordSettings() {
		if (panelQuickRecordSettings == null) {
			panelQuickRecordSettings = new JPanel();
			FormLayout layout = new FormLayout("pref", //columns
					"pref, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelQuickRecordSettings, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_quickRecordSettings"), cc.xywh(1, 1, 1, 1));
			builder.add(this.getJRadioButtonRecordAllPids(), cc.xy(1, 2));
		}
		return panelQuickRecordSettings;
	}

	private JPanel getPanelEngineSettings() {
		if (panelEngineSettings == null) {
			panelEngineSettings = new JPanel();
			FormLayout layout = new FormLayout("pref, 5, pref, 5, pref:grow", //columns
					"pref, pref, pref, pref, pref, 10, pref, 20, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelEngineSettings, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_engine"), cc.xywh(1, 1, 5, 1));
			builder.add(this.getJRadioButtonJGrabber(), cc.xy(1, 2));
            builder.add(this.getJRadioButtonVlc(), cc.xy(1, 3));
			builder.add(this.getJRadioButtonUdrec(), cc.xy(1, 4));            
            builder.add(this.getJButtonUdrecOptions(), cc.xyw(1, 5, 1));
            builder.add(this.getJTextFieldUdrecOptions(), cc.xyw(3, 5, 3));
            
            builder.addLabel(ControlMain.getProperty("label_recordType"), cc.xywh(1, 7, 3, 1));
			builder.add(this.getJComboBoxStreamType(), cc.xy(5, 7));
			builder.add(this.getPanelRecordtimeSettings(), cc.xyw(1, 9, 5));
		}
		return panelEngineSettings;
	}

	private JPanel getPanelRecordtimeSettings() {
		if (panelRecordtimeSettings == null) {
			panelRecordtimeSettings = new JPanel();
			FormLayout layout = new FormLayout("pref, 5, pref", //columns
					"pref, pref, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelRecordtimeSettings, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_timerSettings"), cc.xyw(1, 1, 3));
			builder.add(this.getJSpinnerRecordMinsBefore(), cc.xy(1, 2));
			builder.add(new JLabel(ControlMain.getProperty("label_RecordBefore")), cc.xy(3, 2));
			builder.add(this.getJSpinnerRecordMinsAfter(), cc.xy(1, 3));
			builder.add(new JLabel(ControlMain.getProperty("label_RecordAfter")), cc.xy(3, 3));
		}
		return panelRecordtimeSettings;
	}

	private JPanel getPanelFileNameSettings() {
		if (panelFileNameSettings == null) {
			panelFileNameSettings = new JPanel();
			FormLayout layout = new FormLayout("pref,10,350:grow,10,pref,5,pref", //columns
					"pref, pref,pref,pref,pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelFileNameSettings, layout);
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("filep_filepattern"), cc.xyw(1, 1, 7));
			builder.add(new JLabel(ControlMain.getProperty("filep_directory")), cc.xy(1, 2));
			builder.add(getTfDirPattern(), cc.xy(3, 2));
			builder.add(getJButtonDirTag(), cc.xy(5, 2));
			builder.add(getJButtonTest(), cc.xy(7, 2));

			builder.add(new JLabel(ControlMain.getProperty("filep_file")), cc.xy(1, 3));
			builder.add(getTfFilePattern(), cc.xy(3, 3));
			builder.add(getJButtonFileTag(), cc.xy(5, 3));

		}
		return panelFileNameSettings;
	}

	/**
	 * @return
	 */
	public JButton getJButtonTest() {
		if (jButtonTest == null) {
			jButtonTest = new JButton("Test");
			jButtonTest.setName("TestPattern");
			jButtonTest.addActionListener(control);
		}
		return jButtonTest;
	}

	/**
	 * @return
	 */
	public JButton getJButtonDirTag() {
		if (jButtonDirTag == null) {
			jButtonDirTag = new JButton(ControlMain.getProperty("filep_tagName"));
			jButtonDirTag.setActionCommand("Tags");
			jButtonDirTag.addActionListener(control);
		}
		return jButtonDirTag;
	}

	/**
	 * @return jButtonFileTag
	 */
	public JButton getJButtonFileTag() {
		if (jButtonFileTag == null) {
			jButtonFileTag = new JButton(ControlMain.getProperty("filep_tagName"));
			jButtonFileTag.setActionCommand("TagsFile");
			jButtonFileTag.addActionListener(control);
		}
		return jButtonFileTag;
	}
	
	/**
	 * @return jButtonFileTag
	 */
	public JButton getJButtonUdrecOptions() {
		if (jButtonUdrecOptions == null) {
			jButtonUdrecOptions = new JButton(ControlMain.getProperty("label_udrecOptions"));
			jButtonUdrecOptions.setActionCommand("udrecOptions");
			jButtonUdrecOptions.addActionListener(control);
		}
		return jButtonUdrecOptions;
	}
	
	/**
	 * @return tfDirPattern
	 */
	public JTextField getTfDirPattern() {

		if (tfDirPattern == null) {
			tfDirPattern = new JTextField();
			tfDirPattern.setName("tfDirPattern");
			tfDirPattern.addKeyListener(control);
		}
		return tfDirPattern;
	}

	/**
	 * @return
	 */
	public JTextField getTfFilePattern() {

		if (tfFilePattern == null) {
			tfFilePattern = new JTextField();
			tfFilePattern.setName("tfFilePattern");
			tfFilePattern.addKeyListener(control);
		}
		return tfFilePattern;
	}

	/**
	 * This method initializes jComboBoxStreamType
	 * 
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getJComboBoxStreamType() {
		if (jComboBoxStreamType == null) {
			jComboBoxStreamType = new JComboBox();
			jComboBoxStreamType.addItemListener(control);
			jComboBoxStreamType.setName("streamType");
		}
		return jComboBoxStreamType;
	}

	/**
	 * @return Returns the checkbox for storing epg.
	 */
	public JCheckBox getCbStoreEPG() {
		if (cbStoreEPG == null) {
			cbStoreEPG = new JCheckBox(ControlMain.getProperty("cbStoreEPG"));
			cbStoreEPG.setActionCommand("storeEPG");
			cbStoreEPG.addActionListener(control);
		}
		return cbStoreEPG;
	}

	/**
	 * @return Returns the checkbox for storing epg.
	 */
	public JCheckBox getCbStoreLogAfterRecord() {
		if (cbStoreLogAfterRecord == null) {
			cbStoreLogAfterRecord = new JCheckBox(ControlMain.getProperty("cbStoreLogAfterRecord"));
			cbStoreLogAfterRecord.setActionCommand("storeLogAfterRecord");
			cbStoreLogAfterRecord.addActionListener(control);
		}
		return cbStoreLogAfterRecord;
	}

	/**
	 * @return Returns the checkbox for storing epg.
	 */
	public JCheckBox getCbStopPlaybackAtRecord() {
		if (cbStopPlaybackAtRecord == null) {
			cbStopPlaybackAtRecord = new JCheckBox(ControlMain.getProperty("cbStopPlaybackAtRecord"));
			cbStopPlaybackAtRecord.setActionCommand("cbStopPlaybackAtRecord");
			cbStopPlaybackAtRecord.addActionListener(control);
		}
		return cbStopPlaybackAtRecord;
	}

	/**
	 * @return Returns the cbShutdownAfterRecord.
	 */
	public JCheckBox getCbShutdownAfterRecord() {
		if (cbShutdownAfterRecord == null) {
			cbShutdownAfterRecord = new JCheckBox(ControlMain.getProperty("cbShutdownAfterRecord"));
			cbShutdownAfterRecord.setActionCommand("shutdownAfterRecord");
			cbShutdownAfterRecord.addActionListener(control);
		}
		return cbShutdownAfterRecord;
	}
	/**
	 * @return Returns the jTextFieldUdrecOptions.
	 */
	public JTextField getJTextFieldUdrecOptions() {
		if (jTextFieldUdrecOptions == null) {
			jTextFieldUdrecOptions = new JTextField();
			jTextFieldUdrecOptions.addKeyListener(control);
			jTextFieldUdrecOptions.setEditable(false);
			jTextFieldUdrecOptions.setName("udrecOptions");
            jTextFieldUdrecOptions.setPreferredSize(new Dimension(50, 23));
		}
		return jTextFieldUdrecOptions;
	}

	public JSpinner getJSpinnerRecordMinsBefore() {
		if (recordMinsBefore == null) {
			SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 60, 1);
			recordMinsBefore = new JSpinner(model);
			recordMinsBefore.setToolTipText(ControlMain.getProperty("buttonRecordBefore"));
			recordMinsBefore.setName("recordBefore");
			recordMinsBefore.addChangeListener(control);
		}
		return recordMinsBefore;
	}
	public JSpinner getJSpinnerRecordMinsAfter() {
		if (recordMinsAfter == null) {
			SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 60, 1);
			recordMinsAfter = new JSpinner(model);
			recordMinsAfter.setToolTipText(ControlMain.getProperty("buttonRecordAfter"));
			recordMinsAfter.setName("recordAfter");
			recordMinsAfter.addChangeListener(control);
		}
		return recordMinsAfter;
	}
	/**
	 * @return Returns the cbStartPX.
	 */
	public JButton getJButtonAfterRecordOptions() {
		if (jButtonAfterRecordOptions == null) {
			jButtonAfterRecordOptions = new JButton(ControlMain.getProperty("label_options"));
			jButtonAfterRecordOptions.setActionCommand("afterRecordOptions");
			jButtonAfterRecordOptions.addActionListener(control);
		}
		return jButtonAfterRecordOptions;
	}
	/**
	 * @return Returns the jRadioButtonAC3ReplaceStereo.
	 */
	public JRadioButton getJRadioButtonAC3ReplaceStereo() {
		if (jRadioButtonAC3ReplaceStereo == null) {
			jRadioButtonAC3ReplaceStereo = new JRadioButton(ControlMain.getProperty("rbAC3ReplaceStereo"));
			jRadioButtonAC3ReplaceStereo.setActionCommand("rbAC3ReplaceStereo");
			jRadioButtonAC3ReplaceStereo.addActionListener(control);
			buttonGroupAudioOptions.add(jRadioButtonAC3ReplaceStereo);
		}
		return jRadioButtonAC3ReplaceStereo;
	}
	/**
	 * @return Returns the jRadioButtonStereoReplaceAc3.
	 */
	public JRadioButton getJRadioButtonStereoReplaceAc3() {
		if (jRadioButtonStereoReplaceAc3 == null) {
			jRadioButtonStereoReplaceAc3 = new JRadioButton(ControlMain.getProperty("rbStereoReplaceAc3"));
			jRadioButtonStereoReplaceAc3.setActionCommand("rbStereoReplaceAc3");
			jRadioButtonStereoReplaceAc3.addActionListener(control);
			buttonGroupAudioOptions.add(jRadioButtonStereoReplaceAc3);
		}
		return jRadioButtonStereoReplaceAc3;
	}
	/**
	 * @return Returns the jRadioButtonRecordAllPids.
	 */
	public JRadioButton getJRadioButtonRecordAllPids() {
		if (jRadioButtonRecordAllPids == null) {
			jRadioButtonRecordAllPids = new JRadioButton(ControlMain.getProperty("rbRecordAllPids"));
			jRadioButtonRecordAllPids.setActionCommand("rbRecordAllPids");
			jRadioButtonRecordAllPids.addActionListener(control);
			buttonGroupAudioOptions.add(jRadioButtonRecordAllPids);
		}
		return jRadioButtonRecordAllPids;
	}

	/**
	 * @return Returns the jRadioButtonRecordAllPids.
	 */
	public JCheckBox getCbRecordVtxt() {
		if (cbRecordVtxt == null) {
			cbRecordVtxt = new JCheckBox(ControlMain.getProperty("cbRecordVtxt"));
			cbRecordVtxt.setActionCommand("recordVtxt");
			cbRecordVtxt.addActionListener(control);
		}
		return cbRecordVtxt;
	}
	/**
	 * @return Returns the jRadioButtonJGrabber.
	 */
	public JRadioButton getJRadioButtonJGrabber() {
		if (jRadioButtonJGrabber == null) {
			jRadioButtonJGrabber = new JRadioButton("JGrabber");
			jRadioButtonJGrabber.addActionListener(control);
			jRadioButtonJGrabber.setActionCommand("jgrabber");
			buttonGroupStreamingEngine.add(jRadioButtonJGrabber);
		}
		return jRadioButtonJGrabber;
	}
    /**
     * @return Returns the jRadioButtonVlc.
     */
    public JRadioButton getJRadioButtonVlc() {
        if (jRadioButtonVlc == null) {
            jRadioButtonVlc = new JRadioButton("VLC");
            jRadioButtonVlc.addActionListener(control);
            jRadioButtonVlc.setActionCommand("vlc");
            buttonGroupStreamingEngine.add(jRadioButtonVlc);
        }
        return jRadioButtonVlc;
    }
	/**
	 * @return Returns the jRadioButtonUdrec.
	 */
	public JRadioButton getJRadioButtonUdrec() {
		if (jRadioButtonUdrec == null) {
			jRadioButtonUdrec = new JRadioButton("udrec");
			jRadioButtonUdrec.addActionListener(control);
			jRadioButtonUdrec.setActionCommand("udrec");
			buttonGroupStreamingEngine.add(jRadioButtonUdrec);
		}
		return jRadioButtonUdrec;
	}
    /**
     * @return Returns the jRadioButtonSaveLocal.
     */
    public JRadioButton getJRadioButtonSaveLocal() {
        if (jRadioButtonSaveLocal == null) {
            jRadioButtonSaveLocal = new JRadioButton(ControlMain.getProperty("button_local"));
            jRadioButtonSaveLocal.addActionListener(control);
            jRadioButtonSaveLocal.setActionCommand("saveLocal");
            buttonGroupSaveOptions.add(jRadioButtonSaveLocal);
        }
        return jRadioButtonSaveLocal;
    }
    /**
     * @return Returns the jRadioButtonSaveBox.
     */
    public JRadioButton getJRadioButtonSaveBox() {
        if (jRadioButtonSaveBox == null) {
            jRadioButtonSaveBox = new JRadioButton("Box");
            jRadioButtonSaveBox.addActionListener(control);
            jRadioButtonSaveBox.setActionCommand("saveBox");
            buttonGroupSaveOptions.add(jRadioButtonSaveBox);
        }
        return jRadioButtonSaveBox;
    }
	/**
	 * @return Returns the streamTypeComboModel.
	 */
	public GuiStreamTypeComboModel getStreamTypeComboModel() {
		GuiStreamTypeComboModel model = (GuiStreamTypeComboModel) this.getJComboBoxStreamType().getModel();
		return model;
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
	public void setControl(ControlSettingsTabRecord control) {
		this.control = control;
	}
    /**
     * @return Returns the menuIcon.
     */
    public Icon getIcon() {
        if (menuIcon==null) {
            menuIcon=SerIconManager.getInstance().getIcon("xine.png");
        }
        return menuIcon;
    }
    
    public String getMenuText() {
        return ControlMain.getProperty("label_record");
    }
}