package presentation.timer;
/*
GuiTimerEditView.java by Geist Alexander 

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

import java.text.*;

import javax.swing.*;

import service.*;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import control.*;
import control.timer.ControlSystemTimerEditView;
import control.timer.ControlTimerTab;

public class GuiSystemTimerEditView extends JFrame{
    
    ControlSystemTimerEditView control;
	private JPanel panelMainOptions;
	private JPanel mainPanel;
	private JPanel jPanelDauerTimer;
	
	private JComboBox jComboBoxTimerType;
	private JComboBox jComboBoxBoxSender;
	private JComboBox jComboBoxRepeatRecordTimer;

	private JFormattedTextField tfRecordTimerStartDate;
	private JFormattedTextField tfRecordTimerStartTime;
	
	private JTextField jTextFieldProcessName;
	
	private JButton jButtonOk;
	private JButton jButtonCancel;
	private JButton jButtonOpenStart;
	
	public JRadioButton[] jRadioButtonWhtage = new JRadioButton[7];

	private SerIconManager iconManager = SerIconManager.getInstance();
    
    public GuiSystemTimerEditView(ControlSystemTimerEditView control) {
		this.setControl(control);
		initialize();
		this.setResizable(false);
		this.setTitle(control.getControlTimer().convertShortEventType(control.getTimer().getEventTypeId()) + " " + control.getTimer().getStartDate());
		pack();
		setLocation(ControlMain.getSettings().getLayoutSettings().getLocationOfSystemTimerDialog());

	}
    
    public void initialize() {
        this.getContentPane().add(this.getMainPanel());
    }
    
    
    private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			FormLayout layout = new FormLayout("pref, 25, pref:grow", //columns
					"pref, 10, pref,50"); //rows
			PanelBuilder builder = new PanelBuilder(layout,mainPanel);
			builder.setDefaultDialogBorder();
			CellConstraints cc = new CellConstraints();

			builder.add(this.getPanelMainOptions(),			cc.xyw(1, 1, 3));
			builder.add(this.getPanelDauerTimer(),				cc.xyw(1, 3, 3));
			builder.add(ButtonBarFactory.buildOKCancelBar(this.getJButtonCancel(), this.getJButtonOk()),  cc.xyw(1, 4, 3));
		}
		return mainPanel;
	}

	private JPanel getPanelMainOptions() {
	    if (panelMainOptions==null) {
	    	panelMainOptions = new JPanel();
	        FormLayout layout = new FormLayout(
					  "120, 5, 150, 3, 60, 5, 40, 5, 130, 5, pref:grow,pref",  		// columns 
					  "pref, 5, pref,5,pref,pref"); 			// rows
	        PanelBuilder builder = new PanelBuilder(layout,panelMainOptions);
	        CellConstraints cc = new CellConstraints();
					
	        builder.add(new JLabel("Timer:"),		cc.xy	(1, 1));
	        builder.add(this.getJComboBoxTimerType(),						cc.xy	(1, 3));
	       
	        builder.add(new JLabel(ControlMain.getProperty("sender")),		cc.xy	(3, 1));
	        builder.add(this.getJComboBoxBoxSender(),						cc.xy	(3, 3));
	        builder.add(new JLabel(ControlMain.getProperty("start")),		cc.xy	(4, 1));
	        builder.add(this.getTfRecordTimerStartDate(),					cc.xy	(5, 3));
	        builder.add(this.getTfRecordTimerStartTime(),					cc.xy	(7, 3));
	        builder.add(new JLabel(ControlMain.getProperty("repeat")),		cc.xy	(9, 1));
	        builder.add(this.getJComboBoxRepeatRecordTimer(),				cc.xy	(9, 3));
	        builder.add(new JLabel(ControlMain.getProperty("process")),		cc.xy	(1, 5));
	        builder.add(this.getJTextFieldProcess(),					cc.xyw	(1, 6,10));
	        builder.add(this.getJButtonOpenStart(),					cc.xy	(11, 6));
	    }
	    return panelMainOptions;
	}
	
	public JPanel getPanelDauerTimer() {
		if (jPanelDauerTimer == null) {
			jPanelDauerTimer = new JPanel();
			FormLayout layout = new FormLayout(
				      "pref, 20, pref, 20, pref, 20, pref, 20, pref, 20, pref, 20, pref",	 		//columna
				      "pref");	//rows
			PanelBuilder builder = new PanelBuilder(layout,jPanelDauerTimer);
			CellConstraints cc = new CellConstraints();
			
			int a= 1;
			for(int i = 0 ; i< 7; i++){
				if (jRadioButtonWhtage[i]== null) {
					jRadioButtonWhtage[i] = new JRadioButton();
					jRadioButtonWhtage[i].addActionListener(control);
					jRadioButtonWhtage[i].setName(Integer.toString(ControlTimerTab.weekdays_value[i]));
					jRadioButtonWhtage[i].setActionCommand("recordTimer");
					jRadioButtonWhtage[i].setEnabled(false);					
					jRadioButtonWhtage[i].setText(ControlTimerTab.weekdays[i]);
				}
				builder.add(jRadioButtonWhtage[i],cc.xy(a, 1));
				a = a+2;
			}
		}
		return jPanelDauerTimer;
	}
	
	public void enableRecordTimerWeekdays(boolean enabled) {		
		for (int i = 0; i<7; i++){
			jRadioButtonWhtage[i].setEnabled(enabled);
		}
	}
	
	
	/**
	 * @return jButtonOk
	 */
	public JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton(ControlMain.getProperty("button_ok"));
			jButtonOk.setActionCommand("ok");
			jButtonOk.addActionListener(control);
		}
		return jButtonOk;
	}
	
	/**
	 * @return jButtonCancel
	 */
	public JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton(ControlMain.getProperty("button_cancel"));
			jButtonCancel.setActionCommand("cancel");
			jButtonCancel.addActionListener(control);
		}
		return jButtonCancel;
	}

	/**
	 * @return jButtonOk
	 */
	public JButton getJButtonOpenStart() {
		if (jButtonOpenStart == null) {
			jButtonOpenStart = new JButton(iconManager.getIcon("Open16.gif"));
			jButtonOpenStart.setActionCommand("processName");
			jButtonOpenStart.addActionListener(control);
		}
		return jButtonOpenStart;
	}

	/**
	 * @return Returns the jComboBoxRepeatRecordTimer.
	 */
	public JComboBox getJComboBoxRepeatRecordTimer() {
		if (jComboBoxRepeatRecordTimer == null) {
			jComboBoxRepeatRecordTimer = new JComboBox(new GuiTimerRepeatComboModel(control.getControlTimer()));
			jComboBoxRepeatRecordTimer.addItemListener(control);
			jComboBoxRepeatRecordTimer.setName("repeat");
		}
		return jComboBoxRepeatRecordTimer;
	}
	
	/**
	 * @return Returns the jComboBoxRepeatRecordTimer.
	 */
	public JComboBox getJComboBoxTimerType() {
		if (jComboBoxTimerType == null) {
			jComboBoxTimerType = new JComboBox(new GuiTimerEventTypeComboModel(control.getControlTimer()));
			jComboBoxTimerType.addItemListener(control);
			jComboBoxTimerType.setName("timerType");
		}
		return jComboBoxTimerType;
	}
	
	/**
	 * @return Returns the jComboBoxBoxSender.
	 */
	public JComboBox getJComboBoxBoxSender() {
		if (jComboBoxBoxSender == null) {
			jComboBoxBoxSender = new JComboBox(new GuiTimerSenderComboModel(control.getControlTimer()));
			jComboBoxBoxSender.addItemListener(control);
			jComboBoxBoxSender.setName("sender");
			if (control.getTimer().getModifiedId()==null) {
			    jComboBoxBoxSender.setEnabled(false);
			}
		}
		return jComboBoxBoxSender;
	}
	/**
	 * @return Returns the tfRecordTimerStartTime.
	 */
	public JFormattedTextField getTfRecordTimerStartTime() {
		if (tfRecordTimerStartTime == null) {
			tfRecordTimerStartTime = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
			tfRecordTimerStartTime.addKeyListener(control);
			tfRecordTimerStartTime.setName("startTime");
		}
		return tfRecordTimerStartTime;
	}
	/**
     * @return Returns the tfRecordTimerStartDate.
     */
    public JFormattedTextField getTfRecordTimerStartDate() {
        if (tfRecordTimerStartDate == null) {
            tfRecordTimerStartDate = new JFormattedTextField(new SimpleDateFormat("dd.MM.yy"));
            tfRecordTimerStartDate.addKeyListener(control);
            tfRecordTimerStartDate.setName("startDate");
		}
        return tfRecordTimerStartDate;
    }

	
	/**
	 * @return Returns the tfRecordTimerStopTime.
	 */
	public JTextField getJTextFieldProcess() {
		if (jTextFieldProcessName == null) {
			jTextFieldProcessName = new JTextField();
			jTextFieldProcessName.setName("processName");
			jTextFieldProcessName.addKeyListener(control);
		}
		return jTextFieldProcessName;
	}
	
	
	/**
     * @return Returns the control.
     */
    public ControlSystemTimerEditView getControl() {
        return control;
    }
    /**
     * @param control The control to set.
     */
    public void setControl(ControlSystemTimerEditView control) {
        this.control = control;
    }
}
