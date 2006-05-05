package control;
/*
 ControlProgramTab.java by Geist Alexander 

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.BOBouquet;
import model.BOBox;
import model.BOEpg;
import model.BOExternalProcess;
import model.BOPid;
import model.BOPids;
import model.BOPlaybackOption;
import model.BOQuickRecordOptions;
import model.BORecordArgs;
import model.BOSender;
import model.BOTimer;

import java.util.logging.Logger;

import control.notice.ControlNoticeBroadcastView;

import presentation.GuiAudioPidOptionsDialog;
import presentation.GuiMainView;
import presentation.GuiQuickRecordOptionsDialog;
import presentation.program.GuiEpgTableModel;
import presentation.program.GuiSenderTableModel;
import presentation.program.GuiTabProgramm;
import service.SerAlertDialog;
import service.SerExternalProcessHandler;
import service.SerFormatter;
import service.SerNoticeListHandler;
import service.SerTimerHandler;
import streaming.RecordControl;
import boxConnection.SerStreamingServer;
import boxConnection.control.SerBoxControl;

/**
 * Controlklasse des Programmtabs.
 */
public class ControlProgramTab extends ControlTab implements Runnable, ActionListener, MouseListener, ItemListener, ChangeListener {

	ArrayList bouquetList = new ArrayList();
	BOPids pids;
	BOSender selectedSender;
	BOEpg selectedEpg;
	BORecordArgs recordArgs;
	BOBouquet selectedBouquet;
	Date dateChooserDate;
	GuiMainView mainView;
	RecordControl recordControl;
	BOExternalProcess playbackProcess;
	boolean firstStart = true;

	public ControlProgramTab(GuiMainView view) {
		this.setMainView(view);
	}

	public void run() {
		try {
			if (this.getBoxAccess().isTvMode()) {
				this.getMainView().getTabProgramm().getJRadioButtonTVMode().setSelected(true);
			} else {
				this.getMainView().getTabProgramm().getJRadioButtonRadioMode().setSelected(true);
			}
			this.setBouquetList(this.getBoxAccess().getBouquetList());
			this.selectRunningSender();
			this.getMainView().getTabProgramm().setConnectModus();
			this.setActiveBox();
			this.firstStart = false;
		} catch (IOException e) {
            Logger.getLogger("ControlProgramTab").warning(e.getMessage());
        }
	}

	private void setActiveBox() {
		int index = ControlMain.getIndexOfActiveBox();
		if (index == -1) {
			Logger.getLogger("ControlMainView").warning(ControlMain.getProperty("msg_ipError"));
		}
		this.getMainView().getTabProgramm().getJComboBoxBoxIP().setSelectedIndex(index);
	}

	/*
	 * Versetzen des Programm-Tabs in den Ausgangszustand und initialisiere diesen neu
	 */
	public void reInitialize() {
		firstStart = true;
		this.setBouquetList(new ArrayList());
		this.setSelectedBouquet(null);
		this.getSenderTableModel().fireTableDataChanged();
		selectedSender = null;
		this.getEpgTableModel().fireTableDataChanged();
		this.getMainView().getTabProgramm().getBoquetsComboModel().setSelectedItem(null);
		this.getMainView().getTabProgramm().getJTextAreaEPG().setText("");
		//Timer-Tab refreshen, da evtl anderes Box-Image
		this.getMainView().getMainTabPane().reInitTimerPanel();
		this.run();
	}

	/*
	 * Laufenden Sender in den Bouquets suchen und selektieren Wird beim Start der Anwendung benötigt.
	 */
	public void selectRunningSender() {
		try {
			int listSize = getBouquetList().size();
			String runningChanId = ControlMain.getBoxAccess().getChanIdOfRunningSender();

			for (int i = 0; i < listSize; i++) { //Schleife ueber die Bouquets
				BOBouquet bouquet = (BOBouquet) this.getBouquetList().get(i);
				bouquet.readSender();
				int senderSize = bouquet.getSender().size();
				for (int i2 = 0; i2 < senderSize; i2++) { //Schleife ueber die
					// Sender im Bouquet
					BOSender sender = (BOSender) bouquet.getSender().get(i2);
					if (sender.getChanId().equals(runningChanId) && this.getSelectedSender() == null) {
						this.setSelectedBouquet(bouquet);
						this.getMainView().getTabProgramm().getJComboBoxBouquets().setSelectedIndex(i);
						this.getMainView().getTabProgramm().getJTableChannels().setRowSelectionInterval(i2, i2);
						//this.setSelectedSender(sender);
						break;
					}
				}
			}
		} catch (IOException e) {
			this.setSelectedBouquet((BOBouquet) this.getBouquetList().get(0));
			this.getMainView().getTabProgramm().getJComboBoxBouquets().setSelectedIndex(0);
		}
	}

	/**
	 * Klick-Events der Buttons
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		while (true) {
			if (action == "record") {
				this.actionRecord();
				break;
			}
			if (action == "playback") {
				this.actionPlayback();
				break;
			}
			if (action == "toTimer") {
				this.actionAddToTimer();
				break;
			}
			if (action == "startServer") {
				this.actionStreamingServer();
				break;
			}
			if (action == "radioMode") {
				this.actionRadioMode();
				break;
			}
			if (action == "tvMode") {
				this.actionTvMode();
				break;
			}
			if (action == "refresh") {
				this.actionRefresh();
				break;
			}
            if (action == "broadcastList") {
                this.actionOpenNoticeList();
                break;
            }
			break;
		}
	}
    
    private void actionOpenNoticeList() {
        new ControlNoticeBroadcastView(SerNoticeListHandler.getNoticeList());
    }

	private void actionRefresh() {
		ControlMain.detectImage();
		this.reInitialize();
	}

	private void actionTvMode() {
		try {
			if (this.getBoxAccess().setRadioTvMode("tv").equals("ok")) {
                this.getBoxAccess().senderList=null;
				this.reInitialize();
			}
		} catch (IOException e) {
			SerAlertDialog.alertConnectionLost("ControlProgrammTab", this.getMainView());
		}
	}

	private void actionRadioMode() {
		try {
			if (this.getBoxAccess().setRadioTvMode("radio").equals("ok")) {
                this.getBoxAccess().senderList=null;
				this.reInitialize();
			}
		} catch (IOException e) {
			SerAlertDialog.alertConnectionLost("ControlProgrammTab", this.getMainView());
		}
	}

	/*
	 * Steuerung der 2 Zustaende. Aufnahme läuft bereits ->stop Aufnahme läuft nicht->start
	 * 
	 * Beim Start der Aufnahme vorher auf den selektierten Sender zappen im TV-Modus falls erwünscht auf die aufzunehmenden Pids abfragen
	 */
	private void actionRecord() {
		try {
			if (recordControl == null || !recordControl.isRunning) {
				this.zapToSelectedSender();
				if (ControlMain.getBoxAccess().isTvMode()) {
				    GuiQuickRecordOptionsDialog dialog = new GuiQuickRecordOptionsDialog(this.getPids());
				    BOQuickRecordOptions options = dialog.startPidsQuestDialog();
				    if (options != null && options.getPids().getPidCount()>0) {
				        this.setPids(options.getPids());
				        this.setRecordStopTime(options.getStopTime());
				        this.startRecord(this.buildRecordArgs());
				    } 
				} else {
			        this.startRecord(this.buildRecordArgs());
			    }
			} else {
				this.stopRecord();
			}
		} catch (IOException e) {
			SerAlertDialog.alertConnectionLost("ControlProgrammTab", this.getMainView());
		}
	}

	/*
	 * Wiedergabe des laufenden Senders
	 */
	private void actionPlayback() {
		//alte Wiedergabe beenden
		if (playbackProcess != null) {
			playbackProcess.getProcess().destroy();
		}
		BOPlaybackOption option = BOPlaybackOption.detectPlaybackOption();
		if (option != null) {
			try {
				Thread.sleep(200);
				this.zapToSelectedSender();
				BOPid audioPid = this.getPlaybackAudioPid();
				String execString = option.getPlaybackPlayer() + 
					" "+this.getPlaybackRequestString(option, audioPid);
				if (execString != null) {
					playbackProcess = SerExternalProcessHandler.startProcess(
							option.getName(), execString, option.isLogOutput());
				}
			} catch (Exception e) {
				Logger.getLogger("ControlProgramTab").warning(e.getMessage());
			}
		} 
	}
	
    private BOPid getPlaybackAudioPid() {
    	int count = this.getPids().getAPids().size();
    	int option = ControlMain.getSettingsPlayback().getAudioOption();
        if (count>1) {
        	if (option==0) {
        		GuiAudioPidOptionsDialog dlg = new GuiAudioPidOptionsDialog(this.getPids());
                return dlg.startPidsQuestDialog();
        	}
        	if (option==1) {
        		BOPid ac3Pid = this.getPids().getAc3Pid();
        		if (ac3Pid != null) {
        			return ac3Pid;	
        		}
        	}
        }
        return (BOPid)this.getPids().getAPids().get(0);
    }

	private String getPlaybackRequestString(BOPlaybackOption option, BOPid audioPid){
		String execString = option.getPlaybackOption();
        String vPid = "0x" + this.getPids().getVPid().getNumber();
        String ip = ControlMain.getBoxIpOfActiveBox();       
        
        BOPid pmtPid = this.getPids().getPmtPid();
        if (pmtPid!=null) {
            String pmt = "0x"+this.getPids().getPmtPid().getNumber();
            execString = SerFormatter.replace(execString, "$pmt", pmt);
        } 
        
        execString = SerFormatter.replace(execString, "$ip", ip);
        execString = SerFormatter.replace(execString, "$vPid", vPid);
        execString = SerFormatter.replace(execString, "$aPid", "0x"+audioPid.getNumber());
        if (execString.indexOf("$aPid")>-1) {
            execString = SerFormatter.replace(execString, "$aPid", "0x"+audioPid.getNumber());
            this.getPids().getAPids().remove(audioPid);
            for (int i=0; i<this.getPids().getAPids().size(); i++) {
                BOPid pid = (BOPid)this.getPids().getAPids().get(i);
                execString = execString+",0x"+pid.getNumber();
            }    
        }
        return execString;
	}

	/**
	 * Stop der Aufnahme und Versetzung der GUI in den Aufnahme-Warte-Modus
	 */
	public void stopRecord() {
		if (recordControl != null) {
			recordControl.stopRecord();
		}
		this.getMainView().getTabProgramm().stopRecordModus();
		this.getMainView().setSystrayDefaultIcon();
		ControlMain.initStreamingServer();
		stopRecordInInfoTab();
	}

	/**
	 * stoppt die Aufnahmeinfos im Infotab
	 *  
	 */
	private void stopRecordInInfoTab() {

		if (getMainView().getTabRecordInfo() != null) {
			getMainView().getTabRecordInfo().getControl().stopRecord();
		}
	}

	/**
	 * @param recordArgsl
	 * Start der Aufnahme und Versetzung der GUI in den Aufnahme-Modus 
	 * Setzt die EPG Informationen in den Record Args
	 */
	public void startRecord(BORecordArgs recordArgs) {
		this.setRecordArgs(recordArgs);
		try {
            recordControl = new RecordControl(recordArgs, this);
            this.getMainView().getTabProgramm().startRecordModus();
            this.getMainView().setSystrayRecordIcon();

            //      Starte Record auch im Infotab
            startRecordInInfoTab(recordArgs);

            recordControl.start();
        } catch (IOException e) {
            SerAlertDialog.alertConnectionLost("ControlProgrammTab", this.getMainView());
        }
	}

	/**
	 * setzt die Daten der Aufnahme im Aufnahmeinfo Tab
	 * 
	 * @param recordArgs
	 */
	private void startRecordInInfoTab(BORecordArgs recordArgs) {

        int stream = ControlMain.getSettings().getRecordSettings().getStreamingEngine();
        String engine = "";
        if (stream == 0) // JGrabber Engine
        {
            engine = "JGrabber " + ControlMain.getSettings().getRecordSettings().getJgrabberStreamType();
        } else if (stream == 1) {
            engine = "Udrec " + ControlMain.getSettings().getRecordSettings().getUdrecStreamType();
        }

        getMainView().getTabRecordInfo().getControl().startRecord(recordArgs, engine,
                recordControl.getDirectory());
    }

	/**
	 * @return BORecordArgs Erstellen des Objektes BORecordArgs und Setzen der Pids
	 */
	private BORecordArgs buildRecordArgs() {
		BORecordArgs args = new BORecordArgs(true);
		args.setStopTimeOfQuickRecord(this.getRecordStopTime().getTime());
		args.setPids(this.getPids());
		this.fillRecordArgsWithEpgData(args);
		return args;
	}

	/*
	 * Füllen der RecordArgs mit EPG- und Sender-Informationen
	 */
	private void fillRecordArgsWithEpgData(BORecordArgs args) {
		args.setSenderName(this.getSelectedSender().getName());

		if (args.getEpgTitle() == null) {
			BOEpg epg = getRunnigEpg(getEpgTableModel().getEpgList());
			if (epg != null) {
				args.setEpgTitle(epg.getTitle());
				if (epg.getEpgDetail() != null) {
					args.setEpgInfo1(epg.getEpgDetail().getText());
				}
			}
		}
	}

    public BOEpg getRunnigEpg(ArrayList epgList) {
        if (epgList != null) {
            GregorianCalendar now = new GregorianCalendar();
            long nowTime = now.getTimeInMillis();
            for (int i = 0; i < epgList.size(); i++) {
                BOEpg epgObj = (BOEpg) epgList.get(i);
                long epgStart = epgObj.getStartdate().getTimeInMillis();
                if (nowTime>epgStart) {
                    long epgStop = epgObj.getEndDate().getTimeInMillis();
                    if (nowTime<epgStop) {
                        return epgObj;    
                    }
                }
            }
        }
        return null;
    }

	/*
	 * Zapping zum selektierten Sender und Ermittlung der Pids
	 */
	private void zapToSelectedSender() throws IOException {
	    Logger.getLogger("ConrolProgramTab").info(ControlMain.getProperty("msg_zapTo")+this.getSelectedSender().getName());
	    if (ControlMain.getBoxAccess().zapTo(this.getSelectedSender().getChanId()).equals("ok")) {
	        Logger.getLogger("ConrolProgramTab").info(ControlMain.getProperty("msg_getPids"));
	        this.setPids(ControlMain.getBoxAccess().getPids());
	    }
	}

	/**
	 * Klick-Events der Tables
	 */
	public void mousePressed(MouseEvent me) {
		try {
			JTable table = (JTable) me.getSource();
			String tableName = table.getName();
			//Neuer Sender selektiert
			if (tableName == "Sender") {
				//this.setSelectedSender((BOSender) this.getSelectedBouquet().getSender().get(table.getSelectedRow()));
				if (me.getClickCount() == 2) { //Zapping
					this.zapToSelectedSender();
				}
			}
			//Neue Epg-Zeile selektiert
			if (tableName == "Epg") {
				if (me.getClickCount() == 2) {
					BOTimer timer = SerTimerHandler.buildTimer(this.getSelectedEpg());
					SerTimerHandler.saveTimer(timer, true, true);
				}
			}
		} catch (IOException e) {
			SerAlertDialog.alertConnectionLost("ControlProgrammTab", this.getMainView());
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

	/**
	 * Select-Events der Combobox
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == 1) {
			JComboBox comboBox = (JComboBox) e.getSource();
			if (comboBox.getName().equals("ipList")) {
				this.newBoxSelected(comboBox);
			}
			if (comboBox.getName().equals("bouquets")) {
				this.reInitBouquetList(comboBox);
			}
		}
	}

	public void stateChanged(ChangeEvent event) {
		JSpinner stopTimeSpinner = (JSpinner) event.getSource();
		Date stopTime = (Date) stopTimeSpinner.getModel().getValue();
		this.setRecordStopTime(stopTime);
	}

	/**
	 * @param boxIpComboBox
	 *            Setzen der neuen aktiven Box-IP Ermitteln des laufenden Images der neuen Box Reinitialisierung der Programm-GUI
	 *            Reinitialisierung der Timer-GUI
	 */
	private void newBoxSelected(JComboBox boxIpComboBox) {
		BOBox newSelectedBox = (BOBox) ControlMain.getSettings().getMainSettings().getBoxList().get(boxIpComboBox.getSelectedIndex());
		BOBox oldSelectedBox = ControlMain.getActiveBox();
		//      Konstellation möglich, wenn erste Box angelegt wird
		if (oldSelectedBox == null || oldSelectedBox.isSelected() != newSelectedBox.isSelected()) {
			if (oldSelectedBox != null) {
				oldSelectedBox.setSelectedBox(false); //alte Box zurücksetzen!
			}
			ControlMain.newBoxSelected(newSelectedBox);
			this.reInitialize();
		}
	}

	/**
	 * @param bouquetsComboBox
	 *            Setzen des aktuellen Bouquets, refresh der Senderlist, Selektion des 1. Senders
	 */
	public void reInitBouquetList(JComboBox bouquetsComboBox) {
		if (this.getBouquetList().size() > 0) {
			this.setSelectedBouquet((BOBouquet) this.getBouquetList().get(bouquetsComboBox.getSelectedIndex()));
			this.reInitSender();
			if (!this.firstStart) {
				this.showFirstSender();
				//			nur dann anzeigen, wenn bouquets nach dem ersten start
				// gewechselt werden
			}
		}
	}

	private void showFirstSender() {
		if (this.getSelectedBouquet() != null && this.getSelectedBouquet().getSender().size() > 0) {
			this.getMainView().getTabProgramm().getJTableChannels().setRowSelectionInterval(0, 0);
			this.setSelectedSender((BOSender) this.getSelectedBouquet().getSender().get(0));
		}
	}

	/**
	 * Setzen des Epg-Tables in den Ursprungszustand
	 */
	public void reInitEpg() {
		this.getEpgTableModel().fireTableDataChanged();
		this.selectedEpg = null;
		int indexRunningEpg = this.getEpgTableModel().getIndexRunningEpg();

		if (indexRunningEpg >= 0) {
			int modelIndex = this.getMainView().getTabProgramm().sorter.modelIndex(indexRunningEpg);
			this.getMainView().getTabProgramm().sorter.setSortingStatus(2, 0);
			this.getMainView().getTabProgramm().sorter.setSortingStatus(3, 0);
			this.getMainView().getTabProgramm().sorter.setSortingStatus(4, 0);
			this.getMainView().getTabProgramm().sorter.setSortingStatus(1, 1); //Sortierung zuruecksetzen
			this.getMainView().getTabProgramm().getJTableEPG().setRowSelectionInterval(modelIndex, modelIndex);
		} else {
		    this.reInitEpgDetail();  
		}
	}

	/**
	 * Aktualisieren des Tables Sender
	 */
	public void reInitSender() {
		if (this.getMainView().getMainTabPane().tabProgramm != null) {
			//Beim 1. Start gibt es noch keine Table zum refreshen
			this.getSenderTableModel().fireTableDataChanged();
		}
	}

	/**
	 * Aktualisieren des TextPane Epg-Datails
	 */
	public void reInitEpgDetail() {
		this.getMainView().getTabProgramm().getJTextAreaEPG().setText("");
		if (getSelectedEpg() != null) {
		    if (this.getSelectedEpg().getEpgDetail()!=null) {
                this.getJTextAreaEPG().setText(this.getSelectedEpg().getEpgDetail().getText());
                this.getJTextAreaEPG().setCaretPosition(0);    
            }
		}
	}

	/**
	 * @return Returns the mainView.
	 */
	public GuiMainView getMainView() {
		return mainView;
	}

	/**
	 * @param mainView
	 *            The mainView to set.
	 */
	public void setMainView(GuiMainView view) {
		this.mainView = view;
	}

	/**
	 * @return Returns the bouquetList.
	 */
	public ArrayList getBouquetList() {
		return bouquetList;
	}

	/**
	 * @param bouquetList
	 *            The bouquetList to set.
	 */
	public void setBouquetList(ArrayList bouquetList) {
		this.bouquetList = bouquetList;
	}

	/**
	 * @return Returns the selectedSender.
	 */
	public BOSender getSelectedSender() {
		return selectedSender;
	}

	/**
	 * Setzen des aktuellen Senders, und zeigen des richtigen EPG
	 */
	public void setSelectedSender(BOSender selectedSender) {
		this.selectedSender = selectedSender;
		if (selectedSender != null) {
			try {
				selectedSender.readEpg();
			} catch (IOException e) {
				SerAlertDialog.alertConnectionLost("ControlProgrammTab", this.getMainView());
			}
		}
		this.reInitEpg();
	}

	/**
	 * @return Returns the box.
	 */
	public SerBoxControl getBoxAccess() {
		return ControlMain.getBoxAccess();
	}

	/**
	 * @return Returns the selectedEpg.
	 */
	public BOEpg getSelectedEpg() {
		return selectedEpg;
	}

	/**
	 * Setzen des aktuellen Epg, refreshen der dazugehörigen Epg-Details.
	 */
	public void setSelectedEpg(BOEpg epg) {
		this.selectedEpg = epg;
		this.reInitEpgDetail();
	}

	private void actionAddToTimer() {
		ArrayList list = this.getEpgTableModel().getEpgList();
		int[] rows = this.getMainView().getTabProgramm().getJTableEPG().getSelectedRows(); //Selektierter EPG´s

		for (int i = 0; i < rows.length; i++) {       //      Schleife über die selektierten epg-Zeilen
            int modelIndex = this.getMainView().getTabProgramm().sorter.modelIndex(rows[i]);
            BOEpg epg = (BOEpg)this.getEpgTableModel().getEpgList().get(modelIndex);            
            BOTimer timer = SerTimerHandler.buildTimer(epg);
            SerTimerHandler.saveTimer(timer, true, i+1==rows.length);
		}
	}
	
	private void startStreamingSever() {
	    if (!SerStreamingServer.isRunning) {
            new SerStreamingServer().start();
        }
	}

	public void stopStreamingServer() {
	    SerStreamingServer.stopServer();
	    this.getMainView().getTabProgramm().stopStreamingServerModus();
	}

	/*
	 * Kontroller der 2 Zustaende Streamingserver on>off, off>on
	 */
	private void actionStreamingServer() {
		if (SerStreamingServer.isRunning) {
			this.stopStreamingServer();
		} else {
			this.startStreamingSever();
		}
	}

	public void setRecordStopTime(Date time) {
		GuiTabProgramm tabProg = this.getMainView().getTabProgramm();
		tabProg.getDateModelSpinnerStopTime().setValue(time);
	}

	public Date getRecordStopTime() {
		return this.getMainView().getTabProgramm().getDateModelSpinnerStopTime().getDate();
	}
	
	public boolean isShutdownAfterRecord() {
	    return this.getMainView().getTabProgramm().getCbShutdownAfterRecord().isSelected();
	}

	/**
	 * @return BOBouquet
	 */
	public BOBouquet getSelectedBouquet() {
		return selectedBouquet;
	}

	/**
	 * Sets the selectedBouquet.
	 * 
	 * @param selectedBouquet
	 *            The selectedBouquet to set
	 */
	public void setSelectedBouquet(BOBouquet selectedBouquet) {
		this.selectedBouquet = selectedBouquet;
	}

	/**
	 * @return Returns the dateChooserDate.
	 */
	public Date getDateChooserDate() {
		if (dateChooserDate == null) {
			Date date = (Date)getMainView().getTabProgramm().getSpinnerDateChooser().getModel().getValue();
            dateChooserDate = date;
		}
		return dateChooserDate;
	}

	/**
	 * @param dateChooserDate
	 *            Methode wird aufgerufen wenn Datum im DateChooser geaendert wurde
	 */
	public void setDateChooserDate(Date dateChooserDate) {
		this.dateChooserDate = dateChooserDate;
		//falls ein Sender selektiert ist, muss dessen EPG-Anzeige dem Datum
		// angepasst werden
		if (this.getSelectedSender() != null) {
			this.reInitEpg();
		}
	}

	private GuiEpgTableModel getEpgTableModel() {
		return this.getMainView().getTabProgramm().getEpgTableModel();
	}

	private GuiSenderTableModel getSenderTableModel() {
		return this.getMainView().getTabProgramm().getSenderTableModel();
	}

	private JTextArea getJTextAreaEPG() {
		return this.getMainView().getTabProgramm().getJTextAreaEPG();
	}

	private JTable getJTableEPG() {
		return this.getMainView().getTabProgramm().getJTableEPG();
	}

	private JTable getJTableSender() {
		return this.getMainView().getTabProgramm().getJTableChannels();
	}

	/**
	 * @return Returns the recordControl.
	 */
	public RecordControl getRecordControl() {
		return recordControl;
	}

	/**
	 * @param recordControl
	 *            The recordControl to set.
	 */
	public void setRecordControl(RecordControl recordControl) {
		this.recordControl = recordControl;
	}

	/**
	 * @return Returns the pids.
	 */
	public BOPids getPids() {
		return pids;
	}

	/**
	 * @param pids
	 *            The pids to set.
	 */
	public void setPids(BOPids pids) {
		this.pids = pids;
	}

	/**
	 * @return Returns the recordArgs.
	 */
	public BORecordArgs getRecordArgs() {
		return recordArgs;
	}

	/**
	 * @param recordArgs
	 *            The recordArgs to set.
	 */
	public void setRecordArgs(BORecordArgs recordArgs) {
		this.recordArgs = recordArgs;
	}
	
	/** called by the gui class, when selected channel has been changed
	 * 
	 */
	public void channelChanged(JTable table) {
		if (table.getSelectedRow() > -1)
		{
			this.setSelectedSender((BOSender) this.getSelectedBouquet().getSender().get(table.getSelectedRow()));
		}			
	}

	/** called by the gui class, when selected epg entry has been changed
	 * @param tableEPG
	 */
	public void epgChanged(JTable tableEPG) {
		int selectedRow = tableEPG.getSelectedRow();
		if (selectedRow > -1)
		{
			int modelIndex = this.getMainView().getTabProgramm().sorter.modelIndex(selectedRow);
			this.setSelectedEpg((BOEpg) this.getEpgTableModel().getEpgList().get(modelIndex));
		}
	}
}