package control.timer;
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
import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import control.ControlMain;
import control.muxxer.ControlMuxxerView;
import control.settings.ControlSettingsTabRecord;

import model.BOLocalTimer;
import model.BOPatternTag;
import model.BORecordArgs;
import model.BOSender;
import model.BOTimer;
import presentation.GuiUdrecOptionsDialog;
import presentation.settings.GuiStreamTypeComboModel;
import presentation.settings.GuiTagFrame;
import presentation.timer.GuiTimerEditView;
import service.SerFormatter;
import service.SerHelper;
import service.SerTimerHandler;

public class ControlTimerEditView implements ActionListener, KeyListener, ItemListener, MouseListener {

	BOLocalTimer timer;
	GuiTimerEditView view;
	ControlTimerTab controlTimer;
	private GuiTagFrame tagFrame;

	public ControlTimerEditView(ControlTimerTab controlTimer, BOLocalTimer timer) {
		this.setControlTimer(controlTimer);
		this.setTimer(timer);
		timer.setLocked(true);
		view = new GuiTimerEditView(this);

		view.setVisible(true);
		this.initialize();
	}

	public void run() {
	}

	private void initialize() {
		
		view.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				timer.setLocked(false);
				saveDialogPosition();
			}

			public void windowClosed(WindowEvent e) {
				timer.setLocked(false);
				saveDialogPosition();				
			}
		});

		this.getView().getJComboBoxStreamType().setSelectedItem(timer.getJgrabberStreamType());
		this.getView().getCbRecordVtxt().setSelected(timer.isRecordVtxt());
		this.getView().getJTextFieldUdrecOptions().setText(timer.getUdrecOptions().toString());
		this.getView().getCbStoreEPG().setSelected(timer.isStoreEPG());
		this.getView().getCbStoreLogAfterRecord().setSelected(timer.isStoreLogAfterRecord());
		this.getView().getCbShutdownAfterRecord().setSelected(timer.isShutdownAfterRecord());
		this.getView().getCbStopPlaybackAtRecord().setSelected(timer.isStopPlaybackAtRecord());
		this.getView().getJTextFieldFilePattern().setText(timer.getFilePattern());
		this.getView().getJTextFieldDirPattern().setText(timer.getDirPattern());
		this.getView().getJTextFieldRecordSavePath().setText(timer.getSavePath());
		this.getView().getJTextFieldDescription().setText(timer.getDescription());
		this.getView().getJComboBoxBoxSender().setSelectedItem(timer.getMainTimer().getSenderName());
		this.getView().getTfRecordTimerStartDate().setText(timer.getMainTimer().getStartDate());
		this.getView().getTfRecordTimerStartTime().setText(timer.getMainTimer().getShortStartTime());
		this.getView().getTfRecordTimerStopTime().setText(timer.getMainTimer().getStopTime());
		this.getView().getJComboBoxRepeatRecordTimer().setSelectedItem(
				controlTimer.convertShortEventRepeat(timer.getMainTimer().getEventRepeatId()));
		this.initializeAudioSettings();
		this.initializeStreamingEngine();

	}
	
	/** speichert die aktuelle Position des Fensters
	 * 
	 */
	protected void saveDialogPosition() {
		ControlMain.getSettings().getLayoutSettings().setLocationOfTimerDialog(view.getLocation());
		
	}

	private void initializeAudioSettings() {
		if (timer.isRecordAllPids()) {
			this.getView().getJRadioButtonRecordAllPids().setSelected(true);
		}
		if (timer.isAc3ReplaceStereo()) {
			this.getView().getJRadioButtonAC3ReplaceStereo().setSelected(true);
		}
		if (timer.isStereoReplaceAc3()) {
			this.getView().getJRadioButtonStereoReplaceAc3().setSelected(true);
		}
	}

	private void initializeStreamingEngine() {
		if (timer.getStreamingEngine() == 0) {
			this.initializeJGrabberEngine();
		} else if (timer.getStreamingEngine() == 1) {
			this.initializeUdrecEngine();
		} else {
            this.initializeVlcEngine();
        }
	}
    
	private void initializeJGrabberEngine() {
		this.getView().getJRadioButtonJGrabber().setSelected(true);
		GuiStreamTypeComboModel streamTypeComboModel = new GuiStreamTypeComboModel(ControlSettingsTabRecord.streamTypesJGrabber);
		String streamType = timer.getJgrabberStreamType();
		this.getView().getJComboBoxStreamType().setModel(streamTypeComboModel);
		this.getView().getStreamTypeComboModel().setSelectedItem(streamType);
	}

	private void initializeUdrecEngine() {
		this.getView().getJRadioButtonUdrec().setSelected(true);
		GuiStreamTypeComboModel streamTypeComboModel = new GuiStreamTypeComboModel(ControlSettingsTabRecord.streamTypesUdrec);
		String streamType = timer.getUdrecStreamType();
		this.getView().getJComboBoxStreamType().setModel(streamTypeComboModel);
		this.getView().getStreamTypeComboModel().setSelectedItem(streamType);
	}
    
    private void initializeVlcEngine() {
        this.getView().getJRadioButtonVlc().setSelected(true);
        GuiStreamTypeComboModel streamTypeComboModel = new GuiStreamTypeComboModel(ControlSettingsTabRecord.streamTypesVlc);
        String streamType = timer.getVlcStreamType();
        this.getView().getJComboBoxStreamType().setModel(streamTypeComboModel);
        this.getView().getStreamTypeComboModel().setSelectedItem(streamType);
    }

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		while (true) {
			if (action.equals("ok")) {

				this.actionSaveRecordTimer();
				break;
			}
			if (action.equals("cancel")) {
				this.getView().dispose();

				break;
			}
			if (action.equals("recordPath")) {
				this.openRecordPathFileChooser();
				break;
			}
			if (action.equals("jgrabber")) {
				this.getTimer().setStreamingEngine(0);
				this.initializeJGrabberEngine();
				break;
			}
			if (action.equals("udrec")) {
				this.getTimer().setStreamingEngine(1);
				this.initializeUdrecEngine();
				break;
			}
            if (action.equals("vlc")) {
                this.getTimer().setStreamingEngine(2);
                this.initializeVlcEngine();
                break;
            }
			if (action.equals("storeEPG")) {
				this.getTimer().setStoreEPG(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("storeLogAfterRecord")) {
				this.getTimer().setStoreLogAfterRecord(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("rbRecordAllPids")) {
				this.getTimer().setRecordAllPids(((JRadioButton) e.getSource()).isSelected());
				break;
			}
			if (action.equals("rbAC3ReplaceStereo")) {
				this.getTimer().setAc3ReplaceStereo(((JRadioButton) e.getSource()).isSelected());
				break;
			}
			if (action.equals("rbStereoReplaceAc3")) {
				this.getTimer().setStereoReplaceAc3(((JRadioButton) e.getSource()).isSelected());
				break;
			}
			if (action.equals("cbStopPlaybackAtRecord")) {
				this.getTimer().setStopPlaybackAtRecord(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("recordVtxt")) {
				this.getTimer().setRecordVtxt(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("afterRecordOptions")) {
				new ControlMuxxerView(this.getTimer().getAfterRecordOptions());
				break;
			}
			if (action.equals("shutdownAfterRecord")) {
				this.getTimer().setShutdownAfterRecord(((JCheckBox) e.getSource()).isSelected());
				break;
			}
			if (action.equals("Tags")) {
				openTagWindow(this.getView().getJTextFieldDirPattern());
				break;
			}
			if (action.equals("TagsFile")) {
				openTagWindow(this.getView().getJTextFieldFilePattern());
				break;
			}
			if (action.equals("Test")) {
				testPattern();
				break;
			}
			if (action == "recordTimer") {
				this.actionRecordTimerRepeatDaysChanged(e);
				break;
			}
			if (action.equals("udrecOptions")) {
				openUdrecOptions();
			}
			break;
		}
	}

	private void openUdrecOptions() {
		new GuiUdrecOptionsDialog(this.getTimer().getUdrecOptions(), this.getView().getJTextFieldUdrecOptions());
	}

	private void actionSaveRecordTimer() {
		SerTimerHandler.saveTimer(this.getTimer().getMainTimer(), true, true);
		this.getControlTimer().refreshRecordTimerTable();
		this.getView().dispose();
	}

	/*
	 * wird aufgerufen wenn ein Wochentag selektiert wird. Der neue RepeatId-Wert wird dann aufgrund der selektierten Wochentage
	 * festgestellt und gesetzt
	 */
	public void actionRecordTimerRepeatDaysChanged(ActionEvent event) {
		timer.getMainTimer().setEventRepeatId(this.getRepeatOptionValue(this.getView().jRadioButtonWhtage));
		timer.getMainTimer().setModifiedId("modify");
	}

	/*
	 * Beim jeweiligen RadioButton ist als Name die RepeatId eingestellt
	 */
	private String getRepeatOptionValue(JRadioButton[] buttons) {
		int result = 0;
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isSelected()) {
				result += Integer.parseInt(buttons[i].getName());
			}
		}
		return Integer.toString(result);
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

						if (tagFrame.getField() == getView().getJTextFieldDirPattern()) {
							getTimer().setDirPattern(tagFrame.getField().getText());
						} else if (tagFrame.getField() == getView().getJTextFieldFilePattern()) {
							getTimer().setFilePattern(tagFrame.getField().getText());
						}
					}
				}
			});
			tagFrame.getContentPane().add(new JLabel(ControlMain.getProperty("filep_availableTags")), BorderLayout.NORTH);
			tagFrame.getContentPane().add(new JScrollPane(list));
			tagFrame.pack();
			tagFrame.setLocationRelativeTo(getView());
		}
		tagFrame.setField(field);
		tagFrame.setVisible(true);
	}

	private void testPattern() {

		// test directory pattern
		String pattern = getView().getJTextFieldDirPattern().getText();
		BORecordArgs arg = new BORecordArgs(true);
		arg.setSenderName("RTL");
		arg.setEpgTitle("JackTheMovie");
		arg.setEpgInfo1("1.Teil");

		try {
			String result = SerFormatter.removeInvalidCharacters(SerHelper.createFileName(arg, pattern));
			JOptionPane.showMessageDialog(getView(), pattern + " ------>\n " + result, ControlMain.getProperty("filep_directory"),
					JOptionPane.INFORMATION_MESSAGE);

			pattern = getView().getJTextFieldFilePattern().getText();
			if (pattern.length() > 0) {
				result = SerFormatter.removeInvalidCharacters(SerHelper.createFileName(arg, pattern));
				JOptionPane.showMessageDialog(getView(), pattern + " ------>\n " + result, ControlMain.getProperty("filep_file"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(getView(), e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void itemStateChanged(ItemEvent event) {
		JComboBox comboBox = (JComboBox) event.getSource();
		if (event.getStateChange() == 1) {
			if (comboBox.getName().equals("streamType")) {
				if (this.getTimer().getStreamingEngine() == 0) {
					this.getTimer().setJgrabberStreamType((String) comboBox.getSelectedItem());
				} else {
					this.getTimer().setUdrecStreamType((String) comboBox.getSelectedItem());
				}
			}
			if (comboBox.getName().equals("sender")) {
				int senderIndex = comboBox.getSelectedIndex();
				BOSender sender = (BOSender) this.getControlTimer().getSenderList().get(senderIndex);
				timer.getMainTimer().setChannelId(sender.getChanId());
				timer.getMainTimer().setSenderName(sender.getName());
			}
			if (comboBox.getName().equals("repeat")) {
				this.setEventRepeatId((String) comboBox.getSelectedItem());
			}
		}
	}

	private void setEventRepeatId(String repeatOption) {
		if (repeatOption.equals(ControlMain.getProperty("weekdays"))) {
			this.selectRepeatDaysForRecordTimer(timer.getMainTimer());
			this.getView().enableRecordTimerWeekdays(true);
		} else {
			timer.getMainTimer().setEventRepeatId(this.getControlTimer().convertLongEventRepeat(repeatOption));
			this.getView().enableRecordTimerWeekdays(false);
		}
	}

	public void selectRepeatDaysForRecordTimer(BOTimer timer) {
		ControlTimerTab.selectRepeatDaysForRecordTimer(timer, this.getView().jRadioButtonWhtage);

	}

	public void mousePressed(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Object[] val = ((JList) e.getComponent()).getSelectedValues();
			for (int i = 0; i < val.length; i++) {
				BOPatternTag t = (BOPatternTag) val[i];
				String text = getView().getJTextFieldDirPattern().getText();
				text += t.getName();
				getView().getJTextFieldDirPattern().setText(text);
			}
			this.getTimer().setDirPattern(getView().getJTextFieldDirPattern().getText());
		}
	}

	public void mouseClicked(MouseEvent me) {
	}

	public void mouseReleased(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}

	public void mouseEntered(MouseEvent me) {
	}

	public void keyTyped(KeyEvent event) {
	}

	public void keyPressed(KeyEvent event) {
	}

	public void keyReleased(KeyEvent event) {
		JTextField tf = (JTextField) event.getSource();
		while (true) {
			if (tf.getName().equals("description")) {
				this.getTimer().setDescription(tf.getText());
				break;
			}
			if (tf.getName().equals("startDate")) {
				GregorianCalendar newDate = SerFormatter.getDateFromString(tf.getText(), "dd.MM.yy");
				timer.getMainTimer().setUnformattedStartDate(newDate);
				break;
			}
			if (tf.getName().equals("startTime")) {
				GregorianCalendar newDate = SerFormatter.getDateFromString(tf.getText(), "HH:mm");
				newDate.set(Calendar.SECOND, 0);
				timer.getMainTimer().setUnformattedStartTime(newDate);
				break;
			}
			if (tf.getName().equals("stopTime")) {
				GregorianCalendar newDate = SerFormatter.getDateFromString(tf.getText(), "HH:mm");
				newDate.set(Calendar.SECOND, 0);
				timer.getMainTimer().setUnformattedStopTime(newDate);
				break;
			}
			if (tf.getName().equals("jTextFieldFilePattern")) {
				this.getTimer().setFilePattern(tf.getText());
				break;
			}
			if (tf.getName().equals("jTextFieldDirPattern")) {
				this.getTimer().setDirPattern(tf.getText());
				break;
			}
			break;
		}
	}
	private void openRecordPathFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogType(JFileChooser.SAVE_DIALOG);

		fc.setApproveButtonText(ControlMain.getProperty("msg_choose"));
		fc.setApproveButtonToolTipText(ControlMain.getProperty("msg_chooseDirectory"));
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().toString();
			this.getView().getJTextFieldRecordSavePath().setText(path);
			this.getTimer().setSavePath(path);
		}
	}

	/**
	 * @return Returns the timer.
	 */
	public BOLocalTimer getTimer() {
		return timer;
	}
	/**
	 * @param timer
	 *            The timer to set.
	 */
	public void setTimer(BOLocalTimer timer) {
		this.timer = timer;
	}
	/**
	 * @return Returns the view.
	 */
	public GuiTimerEditView getView() {
		return view;
	}
	/**
	 * @param view
	 *            The view to set.
	 */
	public void setView(GuiTimerEditView view) {
		this.view = view;
	}
	/**
	 * @return Returns the senderList.
	 */
	public ArrayList getSenderList() {
		return this.getControlTimer().getSenderList();
	}
	/**
	 * @return Returns the repeatOptions.
	 */
	public String[] getRepeatOptions() {
		return this.getControlTimer().getRepeatOptions();
	}
	/**
	 * @return Returns the controlTimer.
	 */
	public ControlTimerTab getControlTimer() {
		return controlTimer;
	}
	/**
	 * @param controlTimer
	 *            The controlTimer to set.
	 */
	public void setControlTimer(ControlTimerTab controlTimer) {
		this.controlTimer = controlTimer;
	}
}