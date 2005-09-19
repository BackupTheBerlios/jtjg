package control;
/*
ControlSettingsTabRecord.java by Geist Alexander 

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
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.BOPatternTag;
import model.BORecordArgs;
import model.BOSettingsRecord;
import presentation.GuiUdrecOptionsDialog;
import presentation.settings.GuiSettingsTabRecord;
import presentation.settings.GuiStreamTypeComboModel;
import presentation.settings.GuiTabSettings;
import presentation.settings.GuiTagFrame;
import service.SerFormatter;
import service.SerHelper;

public class ControlSettingsTabRecord extends ControlTabSettings implements KeyListener, ActionListener, ItemListener, ChangeListener {

	GuiTabSettings settingsTab;
	public static final String[] streamTypesJGrabber = {"PES MPEG-Packetized Elementary", "TS MPEG-Transport"};
	public static final String[] streamTypesUdrec = {"PES MPEG-Packetized Elementary", "TS MPEG-Transport", "ES MPEG-Elementary"};
    public static final String[] streamTypesVlc = {"PS MPEG-Program"};
	private GuiTagFrame tagFrame;

	public ControlSettingsTabRecord(GuiTabSettings tabSettings) {
		this.setSettingsTab(tabSettings);
	}

	public void run() {		
		this.getTab().getJComboBoxStreamType().setSelectedItem(this.getSettings().getJgrabberStreamType());
		this.getTab().getCbRecordVtxt().setSelected(this.getSettings().isRecordVtxt());
		this.getTab().getJSpinnerRecordMinsBefore().setValue(Integer.valueOf(this.getSettings().getRecordTimeBefore()));
		this.getTab().getJSpinnerRecordMinsAfter().setValue(Integer.valueOf(this.getSettings().getRecordTimeAfter()));
		this.getTab().getJTextFieldUdrecOptions().setText(this.getSettings().getUdrecOptions().toString());
		this.getTab().getCbStoreEPG().setSelected(this.getSettings().isStoreEPG());
		this.getTab().getCbStoreLogAfterRecord().setSelected(this.getSettings().isStoreLogAfterRecord());
		this.getTab().getCbShutdownAfterRecord().setSelected(this.getSettings().isShutdownAfterRecord());
		this.getTab().getCbStopPlaybackAtRecord().setSelected(this.getSettings().isStopPlaybackAtRecord());
		this.getTab().getTfFilePattern().setText(getSettings().getFilePattern());
		this.getTab().getTfDirPattern().setText(getSettings().getDirPattern());
        this.initializeTimerSaveOptions();
		this.initializeAudioSettings();
		this.initializeStreamingEngine();
	}
    
    private void initializeTimerSaveOptions() {
        if (getSettings().isSaveLocal()) {
            this.getTab().getJRadioButtonSaveLocal().setSelected(true);
        } else {
            this.getTab().getJRadioButtonSaveBox().setSelected(true);
        }
    }

	private void initializeAudioSettings() {
		if (this.getSettings().isRecordAllPids()) {
			this.getTab().getJRadioButtonRecordAllPids().setSelected(true);
		}
		if (this.getSettings().isAc3ReplaceStereo()) {
			this.getTab().getJRadioButtonAC3ReplaceStereo().setSelected(true);
		}
		if (this.getSettings().isStereoReplaceAc3()) {
			this.getTab().getJRadioButtonStereoReplaceAc3().setSelected(true);
		}
	}

	private void initializeStreamingEngine() {
		if (this.getSettings().getStreamingEngine() == 0) {
			this.initializeJGrabberEngine();
		} else  if ((this.getSettings().getStreamingEngine() == 1)){
			this.initializeUdrecEngine();
		} else {
		    this.initializeVlcEngine();
        }
	}

	private void initializeJGrabberEngine() {
		this.getTab().getJRadioButtonJGrabber().setSelected(true);
		GuiStreamTypeComboModel streamTypeComboModel = new GuiStreamTypeComboModel(streamTypesJGrabber);
		String streamType = this.getSettings().getJgrabberStreamType();
		this.getTab().getJComboBoxStreamType().setModel(streamTypeComboModel);
		this.getTab().getStreamTypeComboModel().setSelectedItem(streamType);
	}

	private void initializeUdrecEngine() {
		this.getTab().getJRadioButtonUdrec().setSelected(true);
		GuiStreamTypeComboModel streamTypeComboModel = new GuiStreamTypeComboModel(streamTypesUdrec);
		String streamType = this.getSettings().getUdrecStreamType();
		this.getTab().getJComboBoxStreamType().setModel(streamTypeComboModel);
		this.getTab().getStreamTypeComboModel().setSelectedItem(streamType);
	}
    
    private void initializeVlcEngine() {
        this.getTab().getJRadioButtonVlc().setSelected(true);
        GuiStreamTypeComboModel streamTypeComboModel = new GuiStreamTypeComboModel(streamTypesVlc);
        String streamType = this.getSettings().getVlcStreamType();
        this.getTab().getJComboBoxStreamType().setModel(streamTypeComboModel);
        this.getTab().getStreamTypeComboModel().setSelectedItem(streamType);
    }

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		while (true) {
            if (action.equals("saveLocal")) {
                this.getSettings().setSaveLocal(true);
                break;
            }
            if (action.equals("saveBox")) {
                this.getSettings().setSaveLocal(false);
                break;
            }
			if (action.equals("jgrabber")) {
				this.getSettings().setStreamingEngine(0);
				this.initializeJGrabberEngine();
				break;
			}
			if (action.equals("udrec")) {
				this.getSettings().setStreamingEngine(1);
				this.initializeUdrecEngine();
				break;
			}
            if (action.equals("vlc")) {
                this.getSettings().setStreamingEngine(2);
                this.initializeVlcEngine();
                break;
            }
			if (action.equals("storeEPG")) {
				this.getSettings().setStoreEPG(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("storeLogAfterRecord")) {
				this.getSettings().setStoreLogAfterRecord(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("rbRecordAllPids")) {
				this.getSettings().setRecordAllPids(((JRadioButton) e.getSource()).isSelected());
				break;
			}
			if (action.equals("rbAC3ReplaceStereo")) {
				this.getSettings().setAc3ReplaceStereo(((JRadioButton) e.getSource()).isSelected());
				break;
			}
			if (action.equals("rbStereoReplaceAc3")) {
				this.getSettings().setStereoReplaceAc3(((JRadioButton) e.getSource()).isSelected());
				break;
			}
			if (action.equals("cbStopPlaybackAtRecord")) {
				this.getSettings().setStopPlaybackAtRecord(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("recordVtxt")) {
				this.getSettings().setRecordVtxt(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("afterRecordOptions")) {
                new ControlMuxxerView(ControlMain.getSettingsRecord().getAfterRecordOptions());
                break;
            }
			if (action.equals("shutdownAfterRecord")) {
				this.getSettings().setShutdownAfterRecord(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("Tags")) {
				openTagWindow(getTab().getTfDirPattern());
				break;
			}
			if (action.equals("TagsFile")) {
				openTagWindow(getTab().getTfFilePattern());
				break;
			}
			if (action.equals("Test")) {
				testPattern();
			}
			if (action.equals("udrecOptions")) {
				openUdrecOptions();
			}
			break;

		}
	}
	
	private void openUdrecOptions() {
		new GuiUdrecOptionsDialog(this.getSettings().getUdrecOptions(), this.getTab().getJTextFieldUdrecOptions());
		this.getTab().getJTextFieldUdrecOptions().setText(this.getSettings().getUdrecOptions().toString());
	}

	private void testPattern() {

		// test directory pattern
		String pattern = getTab().getTfDirPattern().getText();
		BORecordArgs arg = new BORecordArgs(true);
		arg.setSenderName("RTL");
		arg.setEpgTitle("JackTheMovie");
		arg.setEpgInfo1("1.Teil");

		try {
			String result = SerFormatter.removeInvalidCharacters(SerHelper.createFileName(arg, pattern));
			JOptionPane.showMessageDialog(getTab(), pattern + " ------>\n " + result, ControlMain.getProperty("filep_directory"),
					JOptionPane.INFORMATION_MESSAGE);

			pattern = getTab().getTfFilePattern().getText();
			if (pattern.length() > 0) {
				result = SerFormatter.removeInvalidCharacters(SerHelper.createFileName(arg, pattern));
				JOptionPane.showMessageDialog(getTab(), pattern + " ------>\n " + result, ControlMain.getProperty("filep_file"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(getTab(), e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * @param field
	 *  
	 */
	private void openTagWindow(JTextField field) {
		if (tagFrame == null) {
			tagFrame = new GuiTagFrame(ControlMain.getProperty("filep_tagName"));
			final JList list = new JList(BOPatternTag.getTags());
			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						Object[] val = list.getSelectedValues();
						for (int i = 0; i < val.length; i++) {
							BOPatternTag t = (BOPatternTag) val[i];
							String text = tagFrame.getField().getText();
							text += t.getName();
							tagFrame.getField().setText(text);
						}

						if (tagFrame.getField() == getTab().getTfDirPattern())
						{
							getSettings().setDirPattern(tagFrame.getField().getText());
						}
						else if (tagFrame.getField() == getTab().getTfFilePattern())
						{
							getSettings().setFilePattern(tagFrame.getField().getText());
						}
					}
				}
			});
			tagFrame.getContentPane().add(new JLabel(ControlMain.getProperty("filep_availableTags")), BorderLayout.NORTH);
			tagFrame.getContentPane().add(new JScrollPane(list));
			tagFrame.pack();
			tagFrame.setLocationRelativeTo(getTab());
		}
		tagFrame.setField(field);
		tagFrame.setVisible(true);
	}

	public void keyTyped(KeyEvent event) {
	}

	public void keyPressed(KeyEvent event) {
	}

	public void keyReleased(KeyEvent event) {
		JTextField tf = (JTextField) event.getSource();
		while (true) {
			if (tf.getName().equals("udrecPath")) {
				ControlMain.getSettingsPath().setUdrecPath(tf.getText());
				break;
			}
			if (tf.getName().equals("tfFilePattern")) {
				this.getSettings().setFilePattern(tf.getText());
				break;
			}
			if (tf.getName().equals("tfDirPattern")) {
				this.getSettings().setDirPattern(tf.getText());
				break;
			}
			break;
		}
	}

	/*
	 * ChangeEvent der Spinner
	 */
	public void stateChanged(ChangeEvent event) {
		JSpinner spinner = (JSpinner) event.getSource();
		if (spinner.getName().equals("recordBefore")) {
			this.getSettings().setRecordTimeBefore(spinner.getValue().toString());
		}
		if (spinner.getName().equals("recordAfter")) {
			this.getSettings().setRecordTimeAfter(spinner.getValue().toString());
		}
	}

	//Change-Events der Combos und der Checkbox
	public void itemStateChanged(ItemEvent event) {
		JComboBox comboBox = (JComboBox) event.getSource();
		if (event.getStateChange() == 1) {
			if (comboBox.getName().equals("streamType")) {
				this.getSettings().setStreamType((String) comboBox.getSelectedItem());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see control.ControlTab#getMainView()
	 */
	public GuiTabSettings getSettingsTab() {
		return settingsTab;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see control.ControlTab#setMainView(presentation.GuiMainView)
	 */
	public void setSettingsTab(GuiTabSettings tabSettings) {
		settingsTab = tabSettings;

	}

	private BOSettingsRecord getSettings() {
		return ControlMain.getSettingsRecord();
	}

	private GuiSettingsTabRecord getTab() {
		return this.getSettingsTab().getSettingsTabRecord();
	}
}