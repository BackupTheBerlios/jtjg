package control;
/*

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

/**
 * Kontrollklasse für die Aufnahme Infos
 * @author Reinhard Achleitner
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import model.*;

import org.apache.log4j.*;

import presentation.*;
import presentation.recordInfo.*;
import service.*;

/**
 * Controlklasse des Programmtabs.
 */
public class ControlRecordInfoTab extends ControlTab implements MouseListener, ListSelectionListener {

	GuiMainView mainView;

	private GuiTabRecordInfo guiTabRecordInfo;
	private File directory;
	private javax.swing.Timer fileInfoTimer;

	private Date currentStartForBitrate; // Starttime when the first file has been written (for bitrate calculation)
	private Date currentStartBegin;

	private double maxBitRateValue;
	private double minBitRateValue = Double.MAX_VALUE;

	private static final int REFRESH_TIME = 1000; // Refresh time of fileinfos
	private static final int BITRATECALCSTART = 15; // seconds after record start the birate will be calculated

	private static final String LOGFILENAME = "log.txt";

	private AbstractAction refreshAction;

	private boolean currentlyDirectoryRefresh;

	private String currentSavePath = "";

	private BORecordArgs currentRecArgs;

	public ControlRecordInfoTab(GuiMainView view) {
		this.setMainView(view);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see control.ControlTab#getMainView()
	 */
	public GuiMainView getMainView() {
		return mainView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see control.ControlTab#setMainView(presentation.GuiMainView)
	 */
	public void setMainView(GuiMainView view) {
		mainView = view;

	}

	public void setRecordView(GuiTabRecordInfo tabRecordInfo) {
		guiTabRecordInfo = tabRecordInfo;
	}

	/**
	 * Setzt die Aufnahmeinfos
	 * 
	 * @param title
	 * @param engine
	 * @param directory
	 * @param timer
	 */
	public void startRecord(BORecordArgs recArgs, String engine, File directory) {

		currentRecArgs = recArgs;
		String title = recArgs.getEpgTitle();
		boolean timer = !recArgs.isQuickRecord();

		this.directory = directory;
		SerLogAppender.getTextAreas().add(guiTabRecordInfo.getLogArea());

		if (timer) {
			title = ControlMain.getProperty("label_recordTimerInfo") + ": " + title;
		} else {
			title = ControlMain.getProperty("label_recordDirect") + ": " + title;
		}

		//		 Erzeuge Timer der periodisch die Dateiinfos aktualisiert
		if (fileInfoTimer == null) {
			fileInfoTimer = new javax.swing.Timer(REFRESH_TIME, new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					reloadFileInfos();

					refreshTime();
				}

			});
		}
		fileInfoTimer.start();
		currentStartBegin = new Date();
		guiTabRecordInfo.startRecord(currentStartBegin, title, engine);

	}

	/**
	 *  
	 */
	protected void refreshTime() {
		Date now = new Date();
		int time = (int) ((now.getTime() - currentStartBegin.getTime()) / 1000 / 60);

		long stopTime = currentRecArgs.getLocalTimer().getStopTime();
		if (stopTime == 0) {
			stopTime = currentRecArgs.getStopTimeOfQuickRecord();
		}
		int remain = (int) ((stopTime - now.getTime()) / 60000) + 1;

		guiTabRecordInfo.setRecordText(currentStartBegin, time, remain);

	}

	/**
	 * stoppt die Erstellung der Aufnahmeinfos
	 * 
	 *  
	 */
	public void stopRecord() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (currentStartBegin == null) {
			return;
		}

		if (fileInfoTimer != null && fileInfoTimer.isRunning()) {
			fileInfoTimer.stop();
		}

		SerLogAppender.getTextAreas().remove(guiTabRecordInfo.getLogArea());

		Date now = new Date();
		long minutes = (now.getTime() - currentStartBegin.getTime()) / 1000 / 60;

		String text = ControlMain.getProperty("label_recordStopped") + " (" + DateFormat.getTimeInstance().format(currentStartBegin);
		text += " - " + DateFormat.getTimeInstance().format(new Date()) + " Dauer: " + minutes + " min)";

		currentStartForBitrate = null;
		currentStartBegin = null;

		guiTabRecordInfo.stopRecord(text);

		if (ControlMain.getSettingsRecord().isStoreLogAfterRecord()) {
			saveLog();
		}

		String time = text.substring(text.indexOf("(") + 1);

		if (time.lastIndexOf(")") > -1) {
			time = time.substring(0, time.lastIndexOf(")"));
		}
		BORecordInfo info = new BORecordInfo();
		info.setTitle(currentRecArgs.getEpgTitle());
		info.setTime(time);
		info.setEngine(guiTabRecordInfo.getEngine().getText());
		info.setEpg(currentRecArgs.getEpgInfo1());
		info.setChannel(currentRecArgs.getSenderName());
		info.setLog(guiTabRecordInfo.getLog());
		info.setVideo(guiTabRecordInfo.getVideoList());
		info.setAudio(guiTabRecordInfo.getAudioList());
		info.setOther(guiTabRecordInfo.getOtherList());
		
		BORecordInfos.getInfos().addRecordInfo(info);
		guiTabRecordInfo.getGuiRecordOverview().getTableModel().fireTableDataChanged();
	}

	/**
	 * speichert das Log der Aufnahme
	 *  
	 */
	private void saveLog() {
		PrintStream print = null;
		try {

			String file = directory.getAbsolutePath() + File.separatorChar + LOGFILENAME;

			File f = new File(file);

			print = new PrintStream(new FileOutputStream(file));

			StringBuffer log = new StringBuffer();
			log.append(guiTabRecordInfo.getTitle() + "\n");
			log.append(guiTabRecordInfo.getLog());

			StringTokenizer tok = new StringTokenizer(log.toString(), "\n");
			while (tok.hasMoreTokens()) {
				print.println(tok.nextToken());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger("RecordControl").error(e);
		} finally {
			if (print != null) {
				print.close();
			}
		}
	}

	/**
	 * aktualisiert die Dateiinformationen (wird vom Timer aufgerufen)
	 *  
	 */
	protected void reloadFileInfos() {

		// Lade alle Files
		File[] aFiles = directory.listFiles();

		ArrayList video = new ArrayList();
		ArrayList audio = new ArrayList();
		ArrayList other = new ArrayList();
		int videoCount = 0;
		int audioCount = 0;
		int otherCount = 0;

		double videoSize = 0;

		for (int i = 0; i < aFiles.length; i++) {
			String size = SerHelper.calcSize(aFiles[i].length(), "MB");

			String end = getEnd(aFiles[i]);
			if (SerHelper.isVideo(aFiles[i].getName())) {
				if (currentStartForBitrate == null) {
					currentStartForBitrate = new Date();
				}
				videoCount++;
				video.add(new BOFileWrapper(aFiles[i], "Video " + end + " (" + videoCount + ")  : " + size + "\n"));
				videoSize += aFiles[i].length();

			} else if (SerHelper.isAudio(aFiles[i].getName())) {
				audioCount++;
				audio.add(new BOFileWrapper(aFiles[i], "Audio " + " " + end + " (" + audioCount + ")  : " + size + "\n"));
			} else {
				size = SerHelper.calcSize(aFiles[i].length(), "KB");
				otherCount++;
				other.add(new BOFileWrapper(aFiles[i], aFiles[i].getName() + ":      " + size + "\n"));
			}
		}

		guiTabRecordInfo.setVideo(video);
		guiTabRecordInfo.setAudio(audio);
		guiTabRecordInfo.setOther(other);

		refreshBitrate(videoSize);

		//Date c = new Date();
		//int min = (int) ((c.getTime() - currentStartBegin.getTime()) / 60000);
		//guiTabRecordInfo.setRecordText(currentStartBegin,min,-1);

	}

	/**
	 * @param videoSize
	 */
	private void refreshBitrate(double videoSize) {
		// Calc average videorate
		if (currentStartForBitrate != null) {
			Date now = new Date();
			long seconds = (now.getTime() - currentStartForBitrate.getTime()) / 1000;
			if (seconds > BITRATECALCSTART) {
				double videoSizePerSecond = videoSize / seconds;
				if (minBitRateValue > videoSizePerSecond && videoSizePerSecond > 0) {
					minBitRateValue = videoSizePerSecond;
				}
				if (maxBitRateValue < videoSizePerSecond) {
					maxBitRateValue = videoSizePerSecond;
				}

				guiTabRecordInfo.setBitrate(SerHelper.calcSize(videoSizePerSecond, "MBit", 1000), SerHelper.calcSize(minBitRateValue,
						"MBit", 1000), SerHelper.calcSize(maxBitRateValue, "MBit", 1000));
			}

		}
	}

	/**
	 * @param file
	 * @return
	 */
	private String getEnd(File file) {

		String fileName = file.getAbsolutePath();
		int end = fileName.lastIndexOf(".");
		if (end > -1) {
			return fileName.substring(end + 1);
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof JList) {
			if (e.getClickCount() == 2) {
				Object sel = ((JList) e.getSource()).getSelectedValue();
				if (sel instanceof BOFileWrapper) {
					openFile(((BOFileWrapper) sel).getAbsoluteFile());

				}
			}
		} else if (e.getSource() instanceof JTable) {
			if (SwingUtilities.isRightMouseButton(e)) {
				showRecordOverviewPopup((JTable) e.getSource(),e);
			}
			else if (e.getClickCount() == 2)
			{
				showDetail();
			}
		}
	}

	/**
	 * @param e
	 *  
	 */
	private void showRecordOverviewPopup(final JTable source, MouseEvent e) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem open = new JMenuItem("Details");
		JMenuItem delete = new JMenuItem("Löschen");
		if (source.getSelectedRowCount() == 1) {
			menu.add(open);
		}
		menu.add(delete);

		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector vToRemove = new Vector();
				int[] aiRows = source.getSelectedRows();
				for (int i = 0; i < aiRows.length; i++) {
					vToRemove.addElement(BORecordInfos.getInfos().elementAt(aiRows[i]));
				}
				BORecordInfos.getInfos().removeAll(vToRemove);
				guiTabRecordInfo.getGuiRecordOverview().getTableModel().fireTableDataChanged();
				BORecordInfos.getInfos().setChanged(true);
				BORecordInfos.saveInfos();
				
			}
		});
		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showDetail();
			}
		});
		
		menu.show(source,e.getX(),e.getY());

	}

	/**
	 * 
	 */
	protected void showDetail() {
		JTable source = guiTabRecordInfo.getGuiRecordOverview().getRecordInfoTable();
		int iRow = source.getSelectedRow();
		BORecordInfo info = (BORecordInfo) BORecordInfos.getInfos().elementAt(iRow);
		
		JTextArea a = new JTextArea();
		a.setEditable(false);
		a.setLineWrap(true);
		a.setWrapStyleWord(true);
		
		a.setText(info.toString());
		a.setCaretPosition(0);
		JScrollPane p = new JScrollPane(a);
		p.setPreferredSize(new Dimension(500,400));
		a.scrollRectToVisible(new Rectangle(0,0,10,10));
		JOptionPane.showMessageDialog(guiTabRecordInfo,p,"",JOptionPane.PLAIN_MESSAGE,null);
	}

	/**
	 * @param file
	 */
	private void openFile(File file) {
		if (SerHelper.isVideo(file.getName()) || SerHelper.isAudio(file.getName())) {
			ArrayList player = ControlMain.getSettingsPlayback().getPlaybackOptions();

			if (player.size() > 0) {
				BOPlaybackOption play = BOPlaybackOption.detectPlaybackOption();
				if (play != null) {
					String exec = play.getPlaybackPlayer() + " " + play.getPlaybackPlayer();
					exec = getExecStringWithoutParam(exec);

					if (file.getAbsolutePath().indexOf(" ") > -1) {
						exec += " \"" + file.getAbsolutePath() + "\"";
					} else {
						exec += " " + file.getAbsolutePath();
					}
					SerExternalProcessHandler.startProcess(play.getName(), exec, play.isLogOutput());
				}
			}

		} else {
			try {

				if (file.length() > 50000) {
					JOptionPane.showMessageDialog(guiTabRecordInfo, ControlMain.getProperty("err_fileTolarge"));
					return;
				}

				//read the file
				FileInputStream in = new FileInputStream(file.getAbsoluteFile());
				BufferedInputStream inS = new BufferedInputStream(in);
				byte[] bytes = new byte[inS.available()];
				inS.read(bytes);
				String text = new String(bytes);
				JTextArea textArea = new JTextArea();
				textArea.setWrapStyleWord(true);
				textArea.setLineWrap(true);
				textArea.setEditable(false);
				textArea.setText(text);
				JScrollPane scroll = new JScrollPane(textArea);
				scroll.setPreferredSize(new Dimension(400, 400));
				JOptionPane.showMessageDialog(guiTabRecordInfo, scroll, "File", JOptionPane.INFORMATION_MESSAGE);

			} catch (Exception e) {
				Logger.getLogger("ControlProgramTab").error(e.getMessage());
			}
		}
	}

	/**
	 * @param exec
	 * @return
	 */
	private String getExecStringWithoutParam(String exec) {
		StringTokenizer tok = new StringTokenizer(exec);
		return tok.nextToken().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

}