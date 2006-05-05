package presentation.timer;
/*
GuiBoxTimerPanel.java by Geist Alexander

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
import java.awt.Component;
import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DateFormatter;

import model.BOTimer;
import service.SerIconManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.timer.ControlTimerTab;

public class GuiBoxTimerPanel extends JPanel {
	
	public JRadioButton[] jRadioButtonWhtage = new JRadioButton[7];
	public JRadioButton[] jRadioButtonWhtage2 = new JRadioButton[7];
	
	private JPanel jPanelDauerTimer = null;
	private JPanel jPanelDauerTimer2 = null;
	private JPanel jPanelButtonsRecordTimer = null;
	private JPanel jPanelButtonsSystemTimer = null;
	private JPanel jPanelButtonsGui = null;
    private JPanel jPanelTimerIcons = null;
	private JButton jButtonReload = null;
	private JButton jButtonDeleteAllRecordTimer = null;
	private JButton jButtonDeleteSelectedRecordTimer = null;
	private JButton jButtonDeleteAllSystemTimer = null;
	private JButton jButtonDeleteSelectedSystemTimer = null;
	private JButton jButtonNewRecordTimer = null;
	private JButton jButtonNewSystemtimer = null;
	private JButton jButtonDeleteAll = null;
	private JComboBox comboBoxSender = null;
	private JComboBox comboBoxRepeatRecordTimer = null;
	private JComboBox comboBoxRepeatSystemTimer = null;
	private ImageIcon imageIcon = null;
	private JTable jTableRecordTimer = null;
	private JTable jTableSystemTimer = null;
	private JScrollPane jScrollPaneRecordTimerTable = null;
	private JScrollPane jScrollPaneSystemTimerTable = null;

	private JFormattedTextField tfRecordTimerStartTime = null;
	private JFormattedTextField tfRecordTimerEndTime = null;
	private JFormattedTextField tfSystemTimerStartTime = null;

	private ControlTimerTab control;
	public GuiRecordTimerTableModel recordTimerTableModel;
	public GuiSystemTimerTableModel systemTimerTableModel;
	private GuiTimerSenderComboModel senderComboModel = null;
	public GuiTimerTableSorter recordTimerSorter = null;
	public GuiTimerTableSorter systemTimerSorter = null;


	public GuiBoxTimerPanel(ControlTimerTab control) {
		this.setControl(control);
		initialize();
	}

	public GuiBoxTimerPanel() {
		initialize();
	}

	private  void initialize() {
		FormLayout layout = new FormLayout(
			      "f:320:grow, 10, 100:grow, 160:grow, 7, pref",							// columns
			      "pref, t:220:grow, pref, 10, pref, 90:grow, 90:grow, 20");				// rows
		PanelBuilder builder = new PanelBuilder(layout,this);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.addSeparator(ControlMain.getProperty("label_recordTimer"),						cc.xyw  (1, 1, 4));
		builder.add(this.getJScrollPaneRecordTimerTable(),   									cc.xyw  (1, 2, 4));
		builder.add(this.getJPanelDauerTimer(),	 												cc.xyw  (1, 3, 4, CellConstraints.CENTER, CellConstraints.TOP));
		builder.addSeparator(ControlMain.getProperty("label_systemTimer"),						cc.xyw  (1, 5, 4));
		builder.add(this.getJScrollPaneSystemTimerTable(),  									cc.xywh  (1, 6, 4, 2));
		builder.add(this.getJPanelDauerTimer2(), 												cc.xyw  (1, 8, 4, CellConstraints.CENTER, CellConstraints.TOP));
		builder.addTitle(ControlMain.getProperty("label_actRecTimer"),							cc.xy   (6, 1));
		builder.add(this.getJPanelButtonsRecordTimer(), 										cc.xywh (6, 2, 1, 1,  CellConstraints.FILL, CellConstraints.TOP));
		builder.addTitle(ControlMain.getProperty("label_actSysTimer"),							cc.xy   (6, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
		builder.add(this.getJPanelButtonsSystemTimer(),											cc.xywh (6, 6, 1, 1, CellConstraints.CENTER, CellConstraints.TOP));
        builder.add(this.getJPanelButtonsGui(),                                                 cc.xywh (6, 7, 1, 2, CellConstraints.CENTER, CellConstraints.BOTTOM));
	}

	public ControlTimerTab getControl() {
		return control;
	}

	public void setControl(ControlTimerTab control) {
		this.control = control;
	}

	private ImageIcon getImageIcon() {
		if (imageIcon == null) {
			//imageIconNeutrino = new ImageIcon(ClassLoader.getSystemResource("ico/neutrino.gif"));
			String icon = ControlMain.getBoxAccess().getIcon();
			if (icon != null && icon.length() > 0) {
				imageIcon = new ImageIcon(ClassLoader.getSystemResource(ControlMain.getBoxAccess().getIcon()));
			} else {
				imageIcon = new ImageIcon();
			}
		}
		return imageIcon;
	}

	public JTable getJTableRecordTimer() {
		if (jTableRecordTimer == null) {
			
			recordTimerTableModel = ControlMain.getBoxAccess().getRecordTimerTabelModel(control);
			recordTimerSorter = new GuiTimerTableSorter(recordTimerTableModel);
			jTableRecordTimer = new JTable(recordTimerSorter);
			recordTimerSorter.setTableHeader(jTableRecordTimer.getTableHeader());
			jTableRecordTimer.setName("recordTimerTable");
			jTableRecordTimer.addMouseListener(control);
			if (ControlMain.getBoxAccess().getName().equals("Enigma")) {
				jTableRecordTimer.getColumnModel().getColumn(0).setPreferredWidth(30);
				jTableRecordTimer.getColumnModel().getColumn(0).setMaxWidth(30);
				jTableRecordTimer.getColumnModel().getColumn(1).setPreferredWidth(40);
				jTableRecordTimer.getColumnModel().getColumn(1).setMaxWidth(40);
				jTableRecordTimer.getColumnModel().getColumn(2).setPreferredWidth(85);
				jTableRecordTimer.getColumnModel().getColumn(2).setMaxWidth(85);
				jTableRecordTimer.getColumnModel().getColumn(3).setPreferredWidth(95);
				jTableRecordTimer.getColumnModel().getColumn(3).setMaxWidth(95);
				jTableRecordTimer.getColumnModel().getColumn(4).setPreferredWidth(55);
				jTableRecordTimer.getColumnModel().getColumn(4).setMaxWidth(55);
				jTableRecordTimer.getColumnModel().getColumn(5).setPreferredWidth(85);
				jTableRecordTimer.getColumnModel().getColumn(5).setMaxWidth(85);
			} else {
				jTableRecordTimer.getColumnModel().getColumn(0).setPreferredWidth(30);
				jTableRecordTimer.getColumnModel().getColumn(0).setMaxWidth(30);
				jTableRecordTimer.getColumnModel().getColumn(1).setPreferredWidth(85);
				jTableRecordTimer.getColumnModel().getColumn(1).setMaxWidth(85);
				jTableRecordTimer.getColumnModel().getColumn(2).setPreferredWidth(95);
				jTableRecordTimer.getColumnModel().getColumn(2).setMaxWidth(95);
				jTableRecordTimer.getColumnModel().getColumn(3).setPreferredWidth(55);
				jTableRecordTimer.getColumnModel().getColumn(3).setMaxWidth(55);
				jTableRecordTimer.getColumnModel().getColumn(4).setPreferredWidth(85);
				jTableRecordTimer.getColumnModel().getColumn(4).setMaxWidth(85);
			}

            jTableRecordTimer.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                        int column) {
                    int modelIndex = recordTimerSorter.modelIndex(row);
                    BOTimer timer = (BOTimer)control.getTimerList().getRecordTimerList().get(modelIndex);
                    JLabel l;
                    if (timer.getLocalTimer().isLocal()) {
                        l = new JLabel(SerIconManager.getInstance().getIcon("localtimer.gif"));    
                    } else {
                        l = new JLabel(SerIconManager.getInstance().getIcon("boxtimer.gif"));   
                    }
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    return l;
                }
            });
		}
		return jTableRecordTimer;
	}

	public JTable getJTableSystemTimer() {
		if (jTableSystemTimer == null) {
			systemTimerTableModel =  ControlMain.getBoxAccess().getSystemTimerTabelModel(control);			
			systemTimerSorter = new GuiTimerTableSorter(systemTimerTableModel);
			jTableSystemTimer = new JTable(systemTimerSorter);
			systemTimerSorter.setTableHeader(jTableSystemTimer.getTableHeader());
			
			jTableSystemTimer.setName("systemTimerTable");
			jTableSystemTimer.addMouseListener(control);
			jTableSystemTimer.setRowHeight(20);
			if (jTableSystemTimer.getColumnCount() > 3)
			{
				jTableSystemTimer.getColumnModel().getColumn(0).setPreferredWidth(70);
				jTableSystemTimer.getColumnModel().getColumn(1).setPreferredWidth(103);
				jTableSystemTimer.getColumnModel().getColumn(2).setPreferredWidth(113);
				jTableSystemTimer.getColumnModel().getColumn(3).setPreferredWidth(87);
				jTableSystemTimer.getColumnModel().getColumn(4).setPreferredWidth(227);
			}
		}
		return jTableSystemTimer;
	}

	private JScrollPane getJScrollPaneRecordTimerTable() {
		if (jScrollPaneRecordTimerTable == null) {
			jScrollPaneRecordTimerTable = new JScrollPane();
			jScrollPaneRecordTimerTable.setViewportView(getJTableRecordTimer());
		}
		return jScrollPaneRecordTimerTable;
	}

	private JScrollPane getJScrollPaneSystemTimerTable() {
		if (jScrollPaneSystemTimerTable == null) {
			jScrollPaneSystemTimerTable = new JScrollPane();
			jScrollPaneSystemTimerTable.setViewportView(getJTableSystemTimer());
		}
		return jScrollPaneSystemTimerTable;
	}

	public GuiRecordTimerTableModel getRecordTimerTableModel() {
		return recordTimerTableModel;
	}
	/**
	 * @param senderTableModel The senderTableModel to set.
	 */
	public void setRecordTimerTableModel(GuiRecordTimerTableModel TimerTableModel) {
		this.recordTimerTableModel = TimerTableModel;
	}

    public JPanel getJPanelButtonsRecordTimer() {
        if (jPanelButtonsRecordTimer == null) {
            jPanelButtonsRecordTimer = new JPanel();
            FormLayout layout = new FormLayout(
                      "pref",           //columna
              "pref, pref, pref, 15, pref, 20, pref");    //rows
            PanelBuilder builder = new PanelBuilder(layout,jPanelButtonsRecordTimer);
            CellConstraints cc = new CellConstraints();

            builder.add(this.getJButtonNewRecordTimer(),                cc.xy   (1, 1));
            builder.add(this.getJButtonDeleteSelectedRecordTimer(),     cc.xy   (1, 2));
            builder.add(this.getJButtonDeleteAllRecordTimer(),          cc.xy   (1, 3));
            builder.add(this.getPanelTimerIcons(),                      cc.xy   (1, 5));
            builder.add(new JLabel(this.getImageIcon()),        cc.xy   (1, 7));
        }
        return jPanelButtonsRecordTimer;
    }
    
    public JPanel getPanelTimerIcons() {
        if (jPanelTimerIcons == null) {
            jPanelTimerIcons = new JPanel();
            FormLayout layout = new FormLayout(
                      "pref, pref",         //columns
              "pref, pref");    //rows
            PanelBuilder builder = new PanelBuilder(layout,jPanelTimerIcons);
            CellConstraints cc = new CellConstraints();

            JLabel l1 = new JLabel(SerIconManager.getInstance().getIcon("localtimer.gif"));
            JLabel l2 = new JLabel(SerIconManager.getInstance().getIcon("boxtimer.gif")); 
            
            builder.addLabel(ControlMain.getProperty("label_boxTimer"), cc.xy(1, 1));
            builder.add(l2, cc.xy(2, 1));
            builder.addLabel(ControlMain.getProperty("label_localTimer"), cc.xy(1, 2));
            builder.add(l1, cc.xy(2, 2));
        }
        return jPanelTimerIcons;
    }

	public JPanel getJPanelButtonsSystemTimer() {
		if (jPanelButtonsSystemTimer == null) {
			jPanelButtonsSystemTimer = new JPanel();
			FormLayout layout = new FormLayout(
				      "f:pref",	 		//columna
				      "pref, pref, pref, ");	//rows
			PanelBuilder builder = new PanelBuilder(layout,jPanelButtonsSystemTimer);
			CellConstraints cc = new CellConstraints();

			builder.add(this.getJButtonNewSystemtimer(),  				cc.xyw	(1, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
			builder.add(this.getJButtonDeleteSelectedSystemTimer(),		cc.xy	(1, 2));
			builder.add(this.getJButtonDeleteAllSystemTimer(),			cc.xy	(1, 3));
		}
		return jPanelButtonsSystemTimer;
	}

	public JPanel getJPanelButtonsGui() {
		if (jPanelButtonsGui == null) {
			jPanelButtonsGui = new JPanel();
			FormLayout layout = new FormLayout(
				      "Fill:pref",	 		//columna
		      "pref, pref ");	//rows
			PanelBuilder builder = new PanelBuilder(layout,jPanelButtonsGui);
			CellConstraints cc = new CellConstraints();

			builder.add(this.getJButtonDeleteAl(),					cc.xy(1, 1));
			builder.add(this.getJButtonReload(),					cc.xy(1, 2));
		}
		return jPanelButtonsGui;
	}

	public JPanel getJPanelDauerTimer() {
		if (jPanelDauerTimer == null) {
			jPanelDauerTimer = new JPanel();
			FormLayout layout = new FormLayout(
				      "pref, 20, pref, 20, pref, 20, pref, 20, pref, 20, pref, 20, pref",	 		//columns
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
	
	public JPanel getJPanelDauerTimer2() {
		if (jPanelDauerTimer2 == null) {
			jPanelDauerTimer2 = new JPanel();
			FormLayout layout = new FormLayout(
				      "pref, 20, pref, 20, pref, 20, pref, 20, pref, 20, pref, 20, pref",	 		//columns
				      "pref");	//rows
			PanelBuilder builder = new PanelBuilder( layout,jPanelDauerTimer2);
			CellConstraints cc = new CellConstraints();
			
			int a= 1;
			for(int i = 0 ; i< 7; i++){
				if (jRadioButtonWhtage2[i]== null) {
					jRadioButtonWhtage2[i] = new JRadioButton();
					jRadioButtonWhtage2[i].addActionListener(control);
					jRadioButtonWhtage2[i].setName(Integer.toString(ControlTimerTab.weekdays_value[i]));
					jRadioButtonWhtage2[i].setActionCommand("systemTimer");
					jRadioButtonWhtage2[i].setEnabled(false);					
					jRadioButtonWhtage2[i].setText(ControlTimerTab.weekdays[i]);
				}
				builder.add(jRadioButtonWhtage2[i],cc.xy(a, 1));
				a = a+2;
			}
		}
		return jPanelDauerTimer2;
	}
	
	
	public JButton getJButtonDeleteAllRecordTimer() {
		if (jButtonDeleteAllRecordTimer == null) {
			jButtonDeleteAllRecordTimer = new JButton(ControlMain.getProperty("button_delete"));
			jButtonDeleteAllRecordTimer.setActionCommand("deleteAllRecordTimer");
			jButtonDeleteAllRecordTimer.setPreferredSize(new Dimension(150,25));
			jButtonDeleteAllRecordTimer.addActionListener(control);
		}
		return jButtonDeleteAllRecordTimer;
	}

	public JButton getJButtonDeleteAllSystemTimer() {
		if (jButtonDeleteAllSystemTimer == null) {
			jButtonDeleteAllSystemTimer = new JButton(ControlMain.getProperty("button_delete"));
			jButtonDeleteAllSystemTimer.setActionCommand("deleteAllSystemTimer");
			jButtonDeleteAllSystemTimer.setPreferredSize(new Dimension(150,25));
			jButtonDeleteAllSystemTimer.addActionListener(control);
		}
		return jButtonDeleteAllSystemTimer;
	}

	public JButton getJButtonDeleteSelectedRecordTimer() {
		if (jButtonDeleteSelectedRecordTimer == null) {
			jButtonDeleteSelectedRecordTimer = new JButton(ControlMain.getProperty("button_deleteSelected"));
			jButtonDeleteSelectedRecordTimer.setActionCommand("deleteSelectedRecordTimer");
			jButtonDeleteSelectedRecordTimer.setPreferredSize(new Dimension(150,25));
			jButtonDeleteSelectedRecordTimer.addActionListener(control);
		}
		return jButtonDeleteSelectedRecordTimer;
	}

	public JButton getJButtonDeleteSelectedSystemTimer() {
		if (jButtonDeleteSelectedSystemTimer == null) {
			jButtonDeleteSelectedSystemTimer = new JButton(ControlMain.getProperty("button_deleteSelected"));
			jButtonDeleteSelectedSystemTimer.setActionCommand("deleteSelectedSystemTimer");
			jButtonDeleteSelectedSystemTimer.setPreferredSize(new Dimension(150,25));
			jButtonDeleteSelectedSystemTimer.addActionListener(control);
		}
		return jButtonDeleteSelectedSystemTimer;
	}


	public JButton getJButtonNewRecordTimer() {
		if (jButtonNewRecordTimer == null) {
			jButtonNewRecordTimer = new JButton(ControlMain.getProperty("button_create"));
			jButtonNewRecordTimer.setActionCommand("addRecordTimer");
			jButtonNewRecordTimer.setPreferredSize(new Dimension(150,25));
			jButtonNewRecordTimer.addActionListener(control);
		}
		return jButtonNewRecordTimer;
	}

	public JButton getJButtonDeleteAl() {
		if (jButtonDeleteAll == null) {
			jButtonDeleteAll =new JButton(ControlMain.getProperty("button_deleteAll"));
			jButtonDeleteAll.setActionCommand("deleteAll");
			jButtonDeleteAll.setPreferredSize(new Dimension(150,25));
			jButtonDeleteAll.addActionListener(control);
		}
		return jButtonDeleteAll;
	}

	public JButton getJButtonReload() {
		if (jButtonReload == null) {
			jButtonReload = new JButton(ControlMain.getProperty("button_reload"));
			jButtonReload.setActionCommand("reload");
			jButtonReload.addActionListener(control);
		}
		return jButtonReload;
	}

	public JButton getJButtonNewSystemtimer() {
		if (jButtonNewSystemtimer == null) {
			jButtonNewSystemtimer = new JButton(ControlMain.getProperty("button_create"));
			jButtonNewSystemtimer.setActionCommand("addSystemTimer");
			jButtonNewSystemtimer.setPreferredSize(new Dimension(150,25));
			jButtonNewSystemtimer.addActionListener(control);
		}
		return jButtonNewSystemtimer;
	}
	/**
	 * @return Returns the senderComboModel.
	 */
	public GuiTimerSenderComboModel getSenderComboModel() {
		return senderComboModel;
	}
	/**
	 * @param senderComboModel The senderComboModel to set.
	 */
	public void setSenderComboModel(
			GuiTimerSenderComboModel senderComboModel) {
		this.senderComboModel = senderComboModel;
	}
	/**
	 * @return Returns the systemTimerTableModel.
	 */
	public GuiSystemTimerTableModel getSystemTimerTableModel() {
		return systemTimerTableModel;
	}

	/**
	 * @return Returns the comboBoxRepeatRecordTimer.
	 */
	public JComboBox getComboBoxRepeatRecordTimer() {
		if (comboBoxRepeatRecordTimer == null) {
			comboBoxRepeatRecordTimer = new JComboBox(new GuiTimerRepeatComboModel(control));
		}
		return comboBoxRepeatRecordTimer;
	}
	/**
	 * @return Returns the comboBoxRepeatSystemTimer.
	 */
	public JComboBox getComboBoxRepeatSystemTimer() {
		if (comboBoxRepeatSystemTimer == null) {
			comboBoxRepeatSystemTimer = new JComboBox(new GuiTimerRepeatComboModel(control));
		}
		return comboBoxRepeatSystemTimer;
	}
	/**
	 * @return Returns the comboBoxSender.
	 */
	public JComboBox getComboBoxSender() {
		if (comboBoxSender == null) {
			comboBoxSender = new JComboBox(new GuiTimerSenderComboModel(control));
		}
		return comboBoxSender;
	}

	public void enableRecordTimerWeekdays(boolean enabled) {		
		for (int i = 0; i<7; i++){
			jRadioButtonWhtage[i].setEnabled(enabled);
		}
	}

	public void enableSystemTimerWeekdays(boolean enabled) {
		for (int i = 0; i<7; i++){
			jRadioButtonWhtage2[i].setEnabled(enabled);
		}
	}

	/**
	 * @return Returns the tfRecordTimerEndTime.
	 */
	public JFormattedTextField getTfRecordTimerEndTime() {
		if (tfRecordTimerEndTime == null) {
			tfRecordTimerEndTime = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
			((DateFormatter)tfRecordTimerEndTime.getFormatter()).setAllowsInvalid(false);
			((DateFormatter)tfRecordTimerEndTime.getFormatter()).setOverwriteMode(true);
		}
		return tfRecordTimerEndTime;
	}

	/**
	 * @return Returns the tfRecordTimerStartTime.
	 */
	public JFormattedTextField getTfRecordTimerStartTime() {
		if (tfRecordTimerStartTime == null) {
			tfRecordTimerStartTime = new JFormattedTextField(new SimpleDateFormat("dd.MM.yy   HH:mm"));
			((DateFormatter)tfRecordTimerStartTime.getFormatter()).setAllowsInvalid(false);
			((DateFormatter)tfRecordTimerStartTime.getFormatter()).setOverwriteMode(true);
		}
		return tfRecordTimerStartTime;
	}

	/**
	 * @return Returns the tfSystemTimerStartTime.
	 */
	public JFormattedTextField getTfSystemTimerStartTime() {
		if (tfSystemTimerStartTime == null) {
			tfSystemTimerStartTime = new JFormattedTextField(new SimpleDateFormat("dd.MM.yy   HH:mm"));
			((DateFormatter)tfSystemTimerStartTime.getFormatter()).setAllowsInvalid(false);
			((DateFormatter)tfSystemTimerStartTime.getFormatter()).setOverwriteMode(true);
		}
		return tfSystemTimerStartTime;
	}
}

