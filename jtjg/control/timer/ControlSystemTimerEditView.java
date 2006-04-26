package control.timer;
/*
 * GuiSettingsTabRecord.java by Geist Alexander
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
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import control.ControlMain;

import model.*;
import presentation.timer.*;
import service.*;

public class ControlSystemTimerEditView implements ActionListener, KeyListener, ItemListener {

	BOTimer timer;
	GuiSystemTimerEditView view;
	ControlTimerTab controlTimer;

	public ControlSystemTimerEditView(ControlTimerTab controlTimer, BOTimer timer) {
		this.setControlTimer(controlTimer);
		this.setTimer(timer);
		view = new GuiSystemTimerEditView(this);

		view.setVisible(true);
		this.initialize();
	}

	public void run() {
	}

	private void initialize() {

		view.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				saveDialogPosition();
			}
		});

		this.getView().getJComboBoxBoxSender().setSelectedItem(timer.getSenderName());
		this.getView().getTfRecordTimerStartDate().setText(timer.getStartDate());
		this.getView().getTfRecordTimerStartTime().setText(timer.getShortStartTime());
		this.getView().getJComboBoxRepeatRecordTimer().setSelectedItem(controlTimer.convertShortEventRepeat(timer.getEventRepeatId()));
		this.getView().getJComboBoxTimerType().setSelectedItem(controlTimer.convertShortEventType(timer.getEventTypeId()));
		this.getView().getJTextFieldProcess().setText(timer.getProcessName());
	}

	/**
	 * speichert die aktuelle Position des Fensters
	 *  
	 */
	protected void saveDialogPosition() {
		ControlMain.getSettings().getLayoutSettings().setLocationOfSystemTimerDialog(view.getLocation());

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
			if (action.equals("processName")) {
				this.openProcessPath();
				break;
			}
			if (action == "recordTimer") {
				this.actionRecordTimerRepeatDaysChanged(e);
				break;
			}

			break;
		}
	}

	private void actionSaveRecordTimer() {
		
		if (getCurrentTimerType().equals("1000"))
		{
			this.timer.getLocalTimer().setLocal(true);
			this.timer.getLocalTimer().setProcessToStart(view.getJTextFieldProcess().getText());
			this.timer.getLocalTimer().setSystemTimer(true);
			SerTimerHandler.saveTimer(timer,false,false);
		}
		else
		{
			SerTimerHandler.saveSystemTimer(this.timer, true);
			this.getControlTimer().refreshSystemTimerTable();
		}
		this.getView().dispose();
	}

	/*
	 * wird aufgerufen wenn ein Wochentag selektiert wird. Der neue RepeatId-Wert wird dann aufgrund der selektierten Wochentage
	 * festgestellt und gesetzt
	 */
	public void actionRecordTimerRepeatDaysChanged(ActionEvent event) {
		timer.setEventRepeatId(this.getRepeatOptionValue(this.getView().jRadioButtonWhtage));
		timer.setModifiedId("modify");
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

	public void itemStateChanged(ItemEvent event) {
		JComboBox comboBox = (JComboBox) event.getSource();
		if (event.getStateChange() == 1) {
			if (comboBox.getName().equals("sender")) {
				int senderIndex = comboBox.getSelectedIndex();
				BOSender sender = (BOSender) this.getControlTimer().getSenderList().get(senderIndex);
				timer.setChannelId(sender.getChanId());
				timer.setSenderName(sender.getName());
				timer.setModifiedId("modify");
			}
			if (comboBox.getName().equals("repeat")) {
				//timer.setEventRepeatId((String)comboBox.getSelectedItem());
				this.setEventRepeatId((String) comboBox.getSelectedItem());
				timer.setModifiedId("modify");
			}
			if (comboBox.getName().equals("timerType")) {
				String timerType = controlTimer.convertLongEventType((String) comboBox.getSelectedItem());
				timer.setEventTypeId(timerType);
				timer.setModifiedId("modify");
				enableWidgets(timerType);
			}
		}
	}
	
	private String getCurrentTimerType()
	{
		String timerType = controlTimer.convertLongEventType((String) view.getJComboBoxTimerType().getSelectedItem());
		return timerType;
	}

	/**
	 * @param timerType
	 */
	private void enableWidgets(String timerType) {
		boolean boEn = !timerType.equals("1000");
		getView().getJComboBoxBoxSender().setEnabled(boEn);
		getView().getJTextFieldProcess().setEnabled(!boEn);
		getView().getJButtonOpenStart().setEnabled(!boEn);
		
	}

	private void setEventRepeatId(String repeatOption) {
		if (repeatOption.equals(ControlMain.getProperty("weekdays"))) {
			this.selectRepeatDaysForRecordTimer(timer);
			this.getView().enableRecordTimerWeekdays(true);
		} else {
			timer.setEventRepeatId(this.getControlTimer().convertLongEventRepeat(repeatOption));
			this.getView().enableRecordTimerWeekdays(false);
		}
	}

	public void selectRepeatDaysForRecordTimer(BOTimer timer) {
		ControlTimerTab.selectRepeatDaysForRecordTimer(timer, this.getView().jRadioButtonWhtage);

	}

	public void keyReleased(KeyEvent event) {
		JTextField tf = (JTextField) event.getSource();
		while (true) {
			if (tf.getName().equals("description")) {
				this.getTimer().setDescription(tf.getText());
				timer.setModifiedId("modify");
				break;
			}
			if (tf.getName().equals("startDate")) {
				GregorianCalendar newDate = SerFormatter.getDateFromString(tf.getText(), "dd.MM.yy");
				timer.setUnformattedStartDate(newDate);
				timer.setModifiedId("modify");
				break;
			}
			if (tf.getName().equals("startTime")) {
				GregorianCalendar newDate = SerFormatter.getDateFromString(tf.getText(), "HH:mm");
				newDate.set(Calendar.SECOND, 0);
				timer.setUnformattedStartTime(newDate);
				timer.setModifiedId("modify");
				break;
			}
			break;
		}
	}
	private void openProcessPath() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogType(JFileChooser.OPEN_DIALOG);

		fc.setApproveButtonText(ControlMain.getProperty("msg_choose"));
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().toString();
			this.getView().getJTextFieldProcess().setText(path);
			timer.setModifiedId("modify");
			timer.setProcessName(path);
			//this.getTimer().setSavePath(path);
		}
	}

	/**
	 * @return Returns the timer.
	 */
	public BOTimer getTimer() {
		return timer;
	}
	/**
	 * @param timer
	 *            The timer to set.
	 */
	public void setTimer(BOTimer timer) {
		this.timer = timer;
	}
	/**
	 * @return Returns the view.
	 */
	public GuiSystemTimerEditView getView() {
		return view;
	}
	/**
	 * @param view
	 *            The view to set.
	 */
	public void setView(GuiSystemTimerEditView view) {
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getComponent().getName().equals("processName")) {
			timer.setProcessName(((JTextField) e.getComponent()).getText());
			timer.setModifiedId("modify");
		}

	}
}