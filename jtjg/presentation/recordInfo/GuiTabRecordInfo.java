package presentation.recordInfo;
/*
 * 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 *  
 */

/**
 * Zeigt Informationen zur aktuellen Aufnahme an
 * 
 * @author Reinhard Achleitner
 */
import java.awt.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import presentation.*;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.layout.*;

import control.*;
import control.record.ControlRecordInfoTab;

public class GuiTabRecordInfo extends GuiTab {

	private ControlRecordInfoTab control;

	private JTextArea engine;

	private JList video;
	private JList audio;
	private JList other;
	private JTextArea log;
	private JTextArea recordTitle;
	private JTextArea averageBitRate;
	private JTextArea minBitRate;
	private JTextArea maxBitRate;

	private JTabbedPane tab = new JTabbedPane();

	private GuiTabRecordInfoOverview guiRecordOverview;

	private JComponent recordState;

	public GuiTabRecordInfo(ControlRecordInfoTab control) {
		this.setControl(control);
		initialize();
	}

	/**
	 * erzeugt die GUI
	 *  
	 */
	protected void initialize() {

		setLayout(new BorderLayout());
		add(tab);

		guiRecordOverview = new GuiTabRecordInfoOverview(getControl());

		JPanel currentRec = new JPanel();

		tab.addTab(ControlMain.getProperty("tab_currentRecord"), currentRec);
		tab.addTab(ControlMain.getProperty("tab_oldRecords"), guiRecordOverview);

		FormLayout layout = new FormLayout("pref:grow", // columns
				"55,75,pref, f:150:grow"); // rows
		PanelBuilder builder = new PanelBuilder(layout,currentRec);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.add(initRecordPanel(), cc.xywh(1, 1, 1, 1));
		builder.add(initStatePanel(), cc.xywh(1, 2, 1, 1));
		builder.add(initFilePanel(), cc.xywh(1, 3, 1, 1));
		builder.add(initLogPanel(), cc.xywh(1, 4, 1, 1));

	}

	private JPanel initRecordPanel() {
		recordTitle = new JTextArea("");

		recordTitle.setEditable(false);
		recordTitle.setBorder(BorderFactory.createEtchedBorder());

		JPanel p = new JPanel();
		FormLayout layout = new FormLayout("710:grow", // columns
				"pref,5,pref"); // rows

		p.setLayout(layout);
		PanelBuilder builder = new PanelBuilder(layout,p);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		recordState = builder.addSeparator(ControlMain.getProperty("label_recordTitle"));

		builder.add(recordTitle, cc.xywh(1, 3, 1, 1));
		return p;
	}

	private JPanel initStatePanel() {

		engine = new JTextArea();
		averageBitRate = new JTextArea();
		minBitRate = new JTextArea();
		maxBitRate = new JTextArea();
		averageBitRate.setPreferredSize(new Dimension(75, 20));
		maxBitRate.setPreferredSize(new Dimension(75, 20));
		minBitRate.setPreferredSize(new Dimension(75, 20));

		engine.setEditable(false);
		averageBitRate.setEditable(false);
		minBitRate.setEditable(false);
		maxBitRate.setEditable(false);
		Border etchedBorder = BorderFactory.createEtchedBorder();

		engine.setBorder(etchedBorder);
		averageBitRate.setBorder(etchedBorder);
		minBitRate.setBorder(etchedBorder);
		maxBitRate.setBorder(etchedBorder);

		JPanel bitrate = new JPanel();
		FormLayout layoutBitrate = new FormLayout("350,10,pref", // columns
				"30"); // rows

		bitrate.setLayout(layoutBitrate);
		PanelBuilder builder = new PanelBuilder(layoutBitrate,bitrate);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		//builder.addSeparator(ControlMain.getProperty("label_recordBitRate"), cc.xywh(1, 1, 1, 1));

		JPanel rateP = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		rateP.add(new JLabel("Avg: "));
		rateP.add(averageBitRate);
		rateP.add(new JLabel(" Min: "));
		rateP.add(minBitRate);
		rateP.add(new JLabel(" Max: "));
		rateP.add(maxBitRate);
		builder.add(rateP, cc.xywh(1, 1, 1, 1));

		JPanel p = new JPanel();
		FormLayout layout = new FormLayout("350,10,365:grow", // columns
				"15,pref"); // rows

		p.setLayout(layout);
		builder = new PanelBuilder(layout,p);
		builder.setDefaultDialogBorder();
		cc = new CellConstraints();

		builder.addSeparator(ControlMain.getProperty("label_recordBitRate"), cc.xywh(1, 1, 1, 1));
		builder.add(bitrate, cc.xywh(1, 2, 1, 1));
		builder.addSeparator(ControlMain.getProperty("label_recordEngine"), cc.xywh(3, 1, 1, 1));
		builder.add(engine, cc.xywh(3, 2, 1, 1));
		return p;
	}

	private JPanel initFilePanel() {
		video = new JList();
		audio = new JList();
		other = new JList();

		video.addMouseListener(control);
		audio.addMouseListener(control);
		other.addMouseListener(control);

		JPanel p = new JPanel();
		FormLayout layout = new FormLayout("170:grow, 10, 170:grow, 10, 350:grow, pref", // columns
				"pref, 10,f:120:grow"); // rows

		p.setLayout(layout);
		PanelBuilder builder = new PanelBuilder(layout,p);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.addSeparator(ControlMain.getProperty("label_recordVideo"), cc.xywh(1, 1, 1, 1));
		builder.add(new JScrollPane(video), cc.xywh(1, 3, 1, 1));

		builder.addSeparator(ControlMain.getProperty("label_recordAudio"), cc.xywh(3, 1, 1, 1));
		builder.add(new JScrollPane(audio), cc.xywh(3, 3, 1, 1));

		builder.addSeparator(ControlMain.getProperty("label_recordOther"), cc.xywh(5, 1, 1, 1));
		builder.add(new JScrollPane(other), cc.xywh(5, 3, 1, 1));
		return p;
	}

	private JPanel initLogPanel() {
		log = new JTextArea() {
			/** automatisch nach unten scrollen */
			public void append(String str) {
				super.append(str);
				scrollRectToVisible(log.getBounds());
			}
		};

		log.setEditable(false);

		JPanel p = new JPanel();
		FormLayout layout = new FormLayout("710:grow", // columns
				"pref, 10,f:120:grow"); // rows

		p.setLayout(layout);
		PanelBuilder builder = new PanelBuilder(layout,p);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		builder.addSeparator(ControlMain.getProperty("label_recordLog"), cc.xywh(1, 1, 1, 1));
		builder.add(new JScrollPane(log), cc.xywh(1, 3, 1, 1));
		return p;
	}

	/**
	 * @return ControlProgramTab
	 */
	public ControlRecordInfoTab getControl() {
		return control;
	}

	/**
	 * Sets the control.
	 * 
	 * @param control
	 *            The control to set
	 */
	public void setControl(ControlRecordInfoTab control) {
		this.control = control;
	}

	/**
	 * muss aufgerufen werden wenn eine Aufnahme gestartet wird
	 * 
	 * @param title
	 *            Titel der Aufnahme (Filmtitel)
	 * @param engine
	 *            Verwendete Engine und Einstellung
	 * @param currentStartBegin
	 * @param directory
	 *            Verzeichnis in der die Dateien geschrieben werden
	 * @param timer
	 *            true, wenn es eine Timeraufnahme ist
	 */
	public void startRecord(Date start, String title, String engine) {

		clear();
		setRecordText(start, -1, -1);

		recordState.getComponent(0).setForeground(Color.red);
		recordState.getComponent(0).setFont(recordState.getComponent(0).getFont().deriveFont(Font.BOLD));

		recordTitle.setText(title);
		setEngine(engine);

	}

	/**
	 *  
	 */
	public void setRecordText(Date d, int min, int remain) {

		String title = ControlMain.getProperty("label_recordInProgress") + " " + DateFormat.getTimeInstance().format(d);

		if (min > -1) {
			title += "  ( " + min;
		}

		if (remain > -1) {
			title += " / " + remain + " )";
		}

		((JLabel) recordState.getComponent(0)).setText(title);
	}

	/**
	 *  
	 */
	public void clear() {
		// Lösche Log
		log.setText("");
		averageBitRate.setText("");
		minBitRate.setText("");
		maxBitRate.setText("");
	}

	/**
	 * setzt den Status auf Aufnahme beendet und stoppt den Dateiinfo timer Speichert bei Bedarf das Log
	 */
	public void stopRecord(String text) {

		((JLabel) recordState.getComponent(0)).setText(text);
		recordState.getComponent(0).setForeground((Color) UIManager.get("TextArea.foreground"));
		recordState.getComponent(0).setFont(recordState.getComponent(0).getFont().deriveFont(Font.PLAIN));
	}

	/**
	 * setzt den Text für die Engine
	 * 
	 * @param engineText
	 */
	public void setEngine(String engineText) {
		engine.setText(engineText);
	}

	/**
	 * @return
	 */
	public JTextArea getLogArea() {

		return log;
	}

	/**
	 * @return
	 */
	public String getTitle() {

		return recordTitle.getText();
	}

	/**
	 * @return Log für die aktuelle, bzw. beendete Aufnahme
	 */
	public String getLog() {

		return log.getText();
	}

	public void setVideo(ArrayList files) {
		DefaultListModel m = new DefaultListModel();
		Iterator iter = files.iterator();

		while (iter.hasNext()) {
			Object element = iter.next();
			m.addElement(element);
		}
		video.setModel(m);
	}

	public void setAudio(ArrayList files) {
		DefaultListModel m = new DefaultListModel();
		Iterator iter = files.iterator();

		while (iter.hasNext()) {
			Object element = iter.next();
			m.addElement(element);
		}
		audio.setModel(m);
	}
	public void setOther(ArrayList files) {
		DefaultListModel m = new DefaultListModel();
		Iterator iter = files.iterator();

		while (iter.hasNext()) {
			Object element = iter.next();
			m.addElement(element);
		}
		other.setModel(m);

	}

	/**
	 * @param string
	 * @param string2
	 * @param string3
	 */
	public void setBitrate(String average, String min, String max) {
		averageBitRate.setText(average);
		minBitRate.setText(min);
		maxBitRate.setText(max);

	}

	public JTextArea getEngine() {
		return engine;
	}
	public GuiTabRecordInfoOverview getGuiRecordOverview() {
		return guiRecordOverview;
	}
	
	public String getVideoList()
	{
		StringBuffer b = new StringBuffer();
		int iSize = video.getModel().getSize();
		for (int i = 0; i < iSize; i++) {
			b.append(video.getModel().getElementAt(i));
		}
		return b.toString();
	}

	public String getAudioList()
	{
		StringBuffer b = new StringBuffer();
		int iSize = audio.getModel().getSize();
		for (int i = 0; i < iSize; i++) {
			b.append(audio.getModel().getElementAt(i));
		}
		return b.toString();
	}

	public String getOtherList()
	{
		StringBuffer b = new StringBuffer();
		int iSize = other.getModel().getSize();
		for (int i = 0; i < iSize; i++) {
			b.append(other.getModel().getElementAt(i));
		}
		return b.toString();
	}

}