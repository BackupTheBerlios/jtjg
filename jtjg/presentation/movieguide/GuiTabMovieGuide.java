package presentation.movieguide;
/*
GuiTabMovieGuide.java by Ralph Henneberger 

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
import java.awt.Insets;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import model.BOMovieGuide;
import service.SerFormatter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.ControlMovieGuideTab;

public class GuiTabMovieGuide extends JPanel {
	private ControlMovieGuideTab control;	
	
	private JButton jButtonNeuEinlesen= null;
	private JButton jButtonDownload = null;
	private JButton jButtonSuche = null;
	private JButton jButtonDatumAll = null;
	private JButton jButtonMovieGuideFileChooser = null;
	private JButton jButtonToTimer = null;
	
	private JTextPane taBeschreibung;
	private JTextPane taDarsteller;
	private JTextPane taEpisode;
	private JTextPane taLand;
	private JTextPane taAudioVideo;
	private JTextPane taGenre;
	
	private JComboBox comboBoxGenre = null;
	private JComboBox comboBoxDatum = null;
	private JComboBox comboBoxSender = null;
	private JComboBox comboBoxSuchenNach = null;
	
	private JTable jTableFilm = null;	
	private JTable jTableTimer = null;
	
	private JScrollPane jScrollPaneChannel = null;
	private JScrollPane jScrollPaneTimer = null;
	private JScrollPane jScrollPaneInfo = null;
	private JScrollPane jScrollPaneDarsteller = null;
	private JScrollPane jScrollPaneEpisode = null;
	private JScrollPane jScrollPaneLand = null;
	private JScrollPane jScrollPaneAudioVideo;
	private JScrollPane jPaneTitel= null;
	private JScrollPane jScrollPaneGenre= null;
	
	private JTextField tfSuche;
	private JFormattedTextField tfZeitAb;
	
	private JCheckBox  jCheckBoxAbAktuell;
	
	private JPanel jPanelSuche = null;
	private JPanel jPanelSucheErw = null;
	private JPanel jPanelChannel = null;
	private JPanel jPanelInfo = null;
	private JPanel jPanelDownload = null;
	private JPanel jPanelDatum= null;
	private JPanel jPanelProgressBar= null;	
	
	private JProgressBar jProgressBarDownload = null;
	private JLabel jLabelSearchCount = null;
	
	public GuiMovieGuideTimerTableModel mgTimerTableModel;
	public GuiMovieGuideTimerTableSorter mgTimerTableSorter = null;
	
	public GuiMovieGuideFilmTableModel filmTableModel;
	public GuiMovieGuideFilmTableSorter mgFilmTableSorter = null;	
			
	public GuiTabMovieGuide(ControlMovieGuideTab ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}

	private  void initialize() {
		FormLayout layout = new FormLayout(	
			      "320px:grow,10, 400px:grow",  				// columns
			      "pref, f:200px:grow, 10, pref, pref, pref");	// rows
		PanelBuilder builder = new PanelBuilder(layout,this);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addSeparator(ControlMain.getProperty("datum"),  cc.xywh	(1, 1, 1, 1));
		builder.add(this.getJPanelChannels(),  			cc.xywh	(1, 2, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
		
		builder.addSeparator(ControlMain.getProperty("prog_info"),	cc.xywh	(3, 1, 1, 1));						
		builder.add(this.getJPanelInfo(),				cc.xywh	(3, 2, 1, 1));
		
		builder.addSeparator(ControlMain.getProperty("suche"),	cc.xywh	(1, 4, 1, 1, CellConstraints.FILL, CellConstraints.FILL));		
		builder.add(this.getJPanelSuche(),  			cc.xywh	(1, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(this.getJPanelSucheErw(),  			cc.xywh	(1, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
		
		builder.addSeparator(ControlMain.getProperty("akt_mg"),		cc.xywh	(3, 4, 1, 1));
		builder.add(this.getJPanelDownload(),  			cc.xywh	(3, 5, 1, 1, CellConstraints.FILL, CellConstraints.FILL));			
			
		builder.add(this.getJPanelProgressBar(), 	 	cc.xywh	(3, 6, 1, 1, CellConstraints.FILL, CellConstraints.FILL));		
	}
	 
	private JPanel getJPanelChannels() {
		if (jPanelChannel == null) {
			jPanelChannel = new JPanel();
			FormLayout layout = new FormLayout(
				      "320px:grow",									//column 
				      "pref, pref, 120px:grow, pref");		//rows
			PanelBuilder builder = new PanelBuilder(layout,jPanelChannel);
			CellConstraints cc = new CellConstraints();			
			builder.add(this.getJPanelDatum(),		  		cc.xyw	(1, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
			builder.addSeparator(ControlMain.getProperty("titel"),	cc.xyw	(1, 2, 1));
			builder.add(this.getJScrollPaneChannels(), 			cc.xyw	(1, 3, 1, CellConstraints.FILL, CellConstraints.FILL));			
			builder.add(this.getJButtonSelectedToTimer(), 	    cc.xyw	(1, 4, 1, CellConstraints.FILL, CellConstraints.FILL));								
		}
		return jPanelChannel;
	}
	
	private JPanel getJPanelInfo() {
		if (jPanelInfo == null) {
			jPanelInfo = new JPanel();
			FormLayout layout = new FormLayout(
				      "400px:grow",									//column 				
						"f:115:grow, f:100px:grow, f:42px:grow, f:pref:grow, f:pref:grow, f:pref:grow, f:pref:grow");		//rows
					
			PanelBuilder builder = new PanelBuilder(layout,jPanelInfo);
			CellConstraints cc = new CellConstraints();														
			builder.add(this.getJScrollPaneTimer(),			cc.xy	(1, 1));			
			builder.add(this.getJScrollPaneInfo(), 			cc.xy	(1, 2));	
			builder.add(this.getJScrollPaneDarsteller(), 	cc.xy	(1, 3));
			builder.add(this.getJScrollPaneEpisode(), 		cc.xy	(1, 4));
			builder.add(this.getJScrollPaneLand(), 			cc.xy	(1, 5));				
			builder.add(this.getJScrollPaneGenre(),			cc.xy	(1, 6));
			builder.add(this.getJScrollPaneAudioVideo(),	cc.xy	(1, 7));
		}
		return jPanelInfo;
	}
	
	private JPanel getJPanelSuche() {
		if (jPanelSuche == null) {
			jPanelSuche = new JPanel();
			FormLayout layout = new FormLayout(
				      "230px:grow,90px:grow",	 		//columna 
				      "pref");	//rows					
			PanelBuilder builder = new PanelBuilder(layout,jPanelSuche);
			CellConstraints cc = new CellConstraints();					
			builder.add(this.getTfSuche(),			 cc.xyw	(1, 1, 1));	
			builder.add(this.getJButtonSuche(),		 cc.xyw	(2, 1, 1));			
		}
		return jPanelSuche;
	}
	/*
	private JPanel getJPanelSucheErw() {
		if (jPanelSucheErw == null) {
			jPanelSucheErw = new JPanel();
			FormLayout layout = new FormLayout(
				      "150px:grow,10,170px:grow",	 		//columna 
				      "pref,pref,pref");	//rows					
			PanelBuilder builder = new PanelBuilder(jPanelSucheErw, layout);
			CellConstraints cc = new CellConstraints();										
			builder.add(this.getComboBoxSucheNach(), 	cc.xyw	(1, 1, 1));
			builder.add(this.getJLabelSearchCount(), 	cc.xyw	(3, 1, 1));
			builder.addSeparator(ControlMain.getProperty("suche_genre"), 	cc.xyw	(1, 2, 1));
			builder.add(this.getComboBoxGenre(), 	 	cc.xyw	(1, 3, 1));
			builder.addSeparator(ControlMain.getProperty("suche_sender"), 	cc.xyw	(3, 2, 1));
			builder.add(this.getComboBoxSender(), 	 	cc.xyw	(3, 3, 1));
		}
		return jPanelSucheErw;
	}
	*/
	
	private JPanel getJPanelSucheErw() {
		if (jPanelSucheErw == null) {
			jPanelSucheErw = new JPanel();
			FormLayout layout = new FormLayout(
				      "110px:grow,10,140px:grow,10,60px:grow",	 		//columna 
				      "pref,pref,pref");	//rows					
			PanelBuilder builder = new PanelBuilder(layout,jPanelSucheErw);
			CellConstraints cc = new CellConstraints();										
			builder.add(this.getComboBoxSucheNach(), 	cc.xyw	(1, 1, 1));
			builder.add(this.getJLabelSearchCount(), 	cc.xyw	(3, 1, 1));
			builder.addSeparator(ControlMain.getProperty("suche_genre"), 	cc.xyw	(1, 2, 1));
			builder.add(this.getComboBoxGenre(), 	 	cc.xyw	(1, 3, 1));
			builder.addSeparator(ControlMain.getProperty("suche_sender"), 	cc.xyw	(3, 2, 1));
			builder.add(this.getComboBoxSender(), 	 	cc.xyw	(3, 3, 1));
			builder.addSeparator(ControlMain.getProperty("suche_time"), 	cc.xyw	(5, 2, 1));
			builder.add(this.getTfZeitab(), 	cc.xyw	(5, 3, 1));
		}
		return jPanelSucheErw;
	}
	
	private JPanel getJPanelProgressBar() {
		if (jPanelProgressBar == null) {
			jPanelProgressBar = new JPanel();
			FormLayout layout = new FormLayout(
				      "320px:grow",	 		//columna 
				      "20,pref,5,pref");	//rows					
			PanelBuilder builder = new PanelBuilder(layout,jPanelProgressBar);
			CellConstraints cc = new CellConstraints();	
			builder.addSeparator(ControlMain.getProperty("fortschritt"), cc.xyw	(1, 2, 1));			
			builder.add(this.getJProgressBarDownload(),	 cc.xyw	(1, 4, 1));												
		}
		return jPanelProgressBar;
	}
	private JPanel getJPanelDownload() {
		if (jPanelDownload == null) {
			jPanelDownload = new JPanel();
			FormLayout layout = new FormLayout(
				      "190px:grow,100px:grow,pref",	 		//columna 
				      "pref");	//rows					
			PanelBuilder builder = new PanelBuilder(layout,jPanelDownload);
			CellConstraints cc = new CellConstraints();														
			builder.add(this.getJButtonDownload(),			 cc.xyw	(1, 1, 1));	
			builder.add(this.getJButtonMovieGuideFileChooser(),	 cc.xyw	(2, 1, 1));			
			builder.add(this.getJButtonNeuEinlesen(),	 cc.xyw	(3, 1, 1));						
		}
		return jPanelDownload;
	}
	public JLabel getJLabelSearchCount(){
		if (jLabelSearchCount == null) {
			jLabelSearchCount = new JLabel("");
		}
		return jLabelSearchCount;
	}
	private JPanel getJPanelDatum() {
		if (jPanelDatum == null) {
			jPanelDatum = new JPanel();
			FormLayout layout = new FormLayout(
				      "240px:grow,80px:grow",	 		//columna 
				      "pref");	//rows					
			PanelBuilder builder = new PanelBuilder(layout,jPanelDatum);
			CellConstraints cc = new CellConstraints();														
			builder.add(this.getComboBoxDatum(),	 cc.xyw	(1, 1, 1));	
			builder.add(this.getJButtonDatumAll(),	 cc.xyw	(2, 1, 1));									
		}
		return jPanelDatum;
	}

	public JProgressBar getJProgressBarDownload(){
		if (jProgressBarDownload == null){
			jProgressBarDownload = new JProgressBar(SwingConstants.HORIZONTAL, 0, 0);
			jProgressBarDownload.setStringPainted(true);			
		}
		return jProgressBarDownload;
	}
		
	public JButton getJButtonMovieGuideFileChooser() {
		if (jButtonMovieGuideFileChooser == null) {		
			jButtonMovieGuideFileChooser = new JButton();
			ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("ico/Open16.gif"));
			jButtonMovieGuideFileChooser.setIcon(icon);			
			jButtonMovieGuideFileChooser.setText(ControlMain.getProperty("open"));
			jButtonMovieGuideFileChooser.setToolTipText(ControlMain.getProperty("open_mg_local"));
			jButtonMovieGuideFileChooser.setActionCommand("movieGuidePath");
			jButtonMovieGuideFileChooser.addActionListener(this.getControl());
		}
		return jButtonMovieGuideFileChooser;
	}
	
	public JButton getJButtonDatumAll() {
		if (jButtonDatumAll == null) {
			jButtonDatumAll = new JButton();
			jButtonDatumAll.setText(ControlMain.getProperty("button_all"));
			jButtonDatumAll.setActionCommand("allDates");
			jButtonDatumAll.setToolTipText(ControlMain.getProperty("button_all_txt"));
			jButtonDatumAll.addActionListener(this.getControl());
		}
		return jButtonDatumAll;
	}
	public JButton getJButtonSelectedToTimer() {
		if (jButtonToTimer == null) {
			jButtonToTimer = new JButton();			
			ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("ico/attach.png"));
			jButtonToTimer.setIcon(icon);	
			jButtonToTimer.setActionCommand("select2Timer");
			jButtonToTimer.setText(ControlMain.getProperty("button_toTimer"));
			jButtonToTimer.setToolTipText(ControlMain.getProperty("buttontt_toTimer"));
			jButtonToTimer.addActionListener(this.getControl());
		}
		return jButtonToTimer;
	}
	public JButton getJButtonDownload() {
		if (jButtonDownload == null) {
			jButtonDownload = new JButton();
			ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("ico/new.png"));
			jButtonDownload.setIcon(icon);						
			jButtonDownload.setActionCommand("download");
			jButtonDownload.setText(ControlMain.getProperty("button_pmg_download"));			
			jButtonDownload.setToolTipText(ControlMain.getProperty("button_pmg_download_txt"));
			jButtonDownload.addActionListener(this.getControl());
		}
		return jButtonDownload;
	}
	public JButton getJButtonNeuEinlesen() {
		if (jButtonNeuEinlesen == null) {
			jButtonNeuEinlesen = new JButton();
			ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("ico/Refresh16.gif"));
			jButtonNeuEinlesen.setIcon(icon);		
			jButtonNeuEinlesen.setText(ControlMain.getProperty("button_new_read"));			
			jButtonNeuEinlesen.setActionCommand("neuEinlesen");
			jButtonNeuEinlesen.setToolTipText(ControlMain.getProperty("button_new_read_txt"));			
			jButtonNeuEinlesen.addActionListener(this.getControl());		
			jButtonNeuEinlesen.setEnabled(false);
		}
		return jButtonNeuEinlesen;
	}
	public JButton getJButtonSuche() {
		if (jButtonSuche == null) {
			jButtonSuche = new JButton();
			//ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("ico/suche.png"));
			//jButtonSuche.setIcon(icon);						
			jButtonSuche.setText(ControlMain.getProperty("button_new_suche"));
			jButtonSuche.setActionCommand("suchen");
			jButtonSuche.setToolTipText(ControlMain.getProperty("button_new_suche_txt"));
			jButtonSuche.addActionListener(this.getControl());
			jButtonSuche.addKeyListener(this.getControl());
		}
		return jButtonSuche;
	}
	
	public JCheckBox getCheckBoxAbAktuell() {
		if (jCheckBoxAbAktuell == null) {
			jCheckBoxAbAktuell = new JCheckBox("Erst ab heutigen Datum");
			jCheckBoxAbAktuell.setName("showAbHeute");
			jCheckBoxAbAktuell.setSelected(true);
			jCheckBoxAbAktuell.addItemListener(control);
			jCheckBoxAbAktuell.addMouseListener(control);
		}
		return jCheckBoxAbAktuell;
	}
	
	private JScrollPane getJScrollPaneChannels() {
		if (jScrollPaneChannel == null) {
			jScrollPaneChannel = new JScrollPane();
			jScrollPaneChannel.setViewportView(getJTableFilm());
		}
		return jScrollPaneChannel;
	}
	
	private JScrollPane getJScrollPaneTimer() {
		if (jScrollPaneTimer == null) {
			jScrollPaneTimer = new JScrollPane();
			jScrollPaneTimer.setViewportView(getJTableTimer());
		}
		return jScrollPaneTimer;
	}
	
	private JScrollPane getJScrollPaneInfo() {
		if (jScrollPaneInfo == null) {
			jScrollPaneInfo = new JScrollPane();
			jScrollPaneInfo.setViewportView(getTaBeschreibung());
		}
		return jScrollPaneInfo;
	}
	private JScrollPane getJScrollPaneDarsteller() {
		if (jScrollPaneDarsteller == null) {
			jScrollPaneDarsteller = new JScrollPane();
			jScrollPaneDarsteller.setViewportView(getTaDarsteller());
		}
		return jScrollPaneDarsteller;
	}
	private JScrollPane getJScrollPaneEpisode() {
		if (jScrollPaneEpisode == null) {
			jScrollPaneEpisode = new JScrollPane();
			jScrollPaneEpisode.setViewportView(getTaEpisode());
		}
		return jScrollPaneEpisode;
	}
	private JScrollPane getJScrollPaneLand() {
		if (jScrollPaneLand == null) {
			jScrollPaneLand = new JScrollPane();
			jScrollPaneLand.setViewportView(getTaLand());
		}
		return jScrollPaneLand;
	}
	
	private JScrollPane getJScrollPaneAudioVideo() {
		if (jScrollPaneAudioVideo == null) {
			jScrollPaneAudioVideo = new JScrollPane();
			jScrollPaneAudioVideo.setViewportView(getTaAudioVideo());
		}
		return jScrollPaneAudioVideo;
	}	
	
	private JScrollPane getJScrollPaneGenre() {
		if (jScrollPaneGenre == null) {
			jScrollPaneGenre = new JScrollPane();
			jScrollPaneGenre.setViewportView(getTaGenre());
		}
		return jScrollPaneGenre;
	}	
	
	public GuiMovieGuideTimerTableModel getGuiMovieGuideTimerTableModel() {
		return mgTimerTableModel;
	}
	
	public JTable getJTableTimer() {
		if (jTableTimer == null) {
			mgTimerTableModel = new GuiMovieGuideTimerTableModel(control);
			mgTimerTableSorter = new GuiMovieGuideTimerTableSorter(mgTimerTableModel);
			jTableTimer = new JTable(mgTimerTableSorter);
			mgTimerTableSorter.setTableHeader(jTableTimer.getTableHeader());									 
			jTableTimer.getColumnModel().getColumn(0).setMaxWidth(200);
			jTableTimer.getColumnModel().getColumn(1).setMaxWidth(45);
			jTableTimer.getColumnModel().getColumn(2).setMaxWidth(45);
			jTableTimer.getColumnModel().getColumn(3).setMaxWidth(45);		
			jTableTimer.getColumnModel().getColumn(4).setMaxWidth(165);			
			jTableTimer.addMouseListener(control);
			jTableTimer.setName("timerTable");
			jTableTimer.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						control.timerTableChanged(jTableTimer);
					}
				}
			});
		}
		return jTableTimer;
	}
	public JComboBox getComboBoxSucheNach(){
		if (comboBoxSuchenNach == null) {						
			comboBoxSuchenNach = new JComboBox(new BOMovieGuide().getSucheList().toArray());	
			comboBoxSuchenNach.addItemListener(control);
			comboBoxSuchenNach.setName("jComboBoxSucheNach");
		}
		return comboBoxSuchenNach;
	}
	
	/**
	 * @return Returns the control.
	 */
	public ControlMovieGuideTab getControl() {
		return control;
	}
	/**
	 * @param control The control to set.
	 */
	public void setControl(ControlMovieGuideTab control) {
		this.control = control;
	}

	public JTextField getTfSuche() {
		if (tfSuche == null) {
			tfSuche = new JTextField();
			tfSuche.setEditable(true);			
			tfSuche.setAutoscrolls(true);
			tfSuche.setActionCommand("textsuche");
			tfSuche.addActionListener(this.getControl());
		}
		return tfSuche;
	}
	
	public JFormattedTextField getTfZeitab() {
		if (tfZeitAb == null) {
			tfZeitAb = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
			tfZeitAb.setEditable(true);			
			tfZeitAb.setAutoscrolls(true);
			tfZeitAb.setActionCommand("zeitab");
			tfZeitAb.setToolTipText("HH:mm");
			tfZeitAb.addActionListener(this.getControl());
		}
		return tfZeitAb;
	}
		
	public JTextPane getTaGenre() {
		if (taGenre == null) {
			taGenre = new JTextPane();
			taGenre.setEditable(false);			
			taGenre.setAutoscrolls(true);				
			SerFormatter.underScore(taGenre, ControlMain.getProperty("txt_genre"),true,0);  						
		}
		return taGenre;
	}
	
	public JTextPane getTaBeschreibung() {
		if (taBeschreibung == null) {
			taBeschreibung = new JTextPane();
			taBeschreibung.setEditable(false);
			taBeschreibung.setAutoscrolls(true);							
			SerFormatter.underScore(taBeschreibung, ControlMain.getProperty("txt_inhalt"),true,0);						
		}
		return taBeschreibung;
	}
	public JTextPane getTaLand() {
		if (taLand == null) {
			taLand = new JTextPane();
			taLand.setEditable(false);						
			taLand.setAutoscrolls(true);			
			SerFormatter.underScore(taLand, ControlMain.getProperty("txt_prod"),true,0);
		}
		return taLand;
	}
	public JTextPane getTaAudioVideo() {
		if (taAudioVideo == null) {
			taAudioVideo = new JTextPane();
			taAudioVideo.setEditable(false);			
			taAudioVideo.setAutoscrolls(true);						
			//taAudioVideo.setText(ControlMain.getProperty("txt_audio")+" / "+ControlMain.getProperty("txt_video"));	
			SerFormatter.underScore(taAudioVideo, ControlMain.getProperty("txt_audio"),true,0);
			SerFormatter.underScore(taAudioVideo," / ",false,ControlMain.getProperty("txt_audio").length());
			SerFormatter.underScore(taAudioVideo, ControlMain.getProperty("txt_video"),true,ControlMain.getProperty("txt_audio").length()+3);
		}
		return taAudioVideo;
	}
	public JTextPane getTaDarsteller() {
		if (taDarsteller == null) {
			taDarsteller = new JTextPane();
			taDarsteller.setEditable(false);			
			taDarsteller.setAutoscrolls(true);											
			SerFormatter.underScore(taDarsteller, ControlMain.getProperty("txt_darsteller"),true,0);
		}
		return taDarsteller;
	}
	public JTextPane getTaEpisode() {
		if (taEpisode == null) {
			taEpisode = new JTextPane();
			taEpisode.setEditable(false);			
			taEpisode.setAutoscrolls(true);									
			SerFormatter.underScore(taEpisode, ControlMain.getProperty("txt_episode"),true,0);
		}
		return taEpisode;
	}

	public JComboBox getComboBoxGenre(){
		if (comboBoxGenre == null) {					
			comboBoxGenre = new JComboBox();
			comboBoxGenre.setModel(new GuiMovieGuideGenreComboModel(this.getControl()));
			comboBoxGenre.addItemListener(control);
			comboBoxGenre.addActionListener(this.getControl());
			comboBoxGenre.setActionCommand("clickONGenreComboBox");
			comboBoxGenre.setName("jComboBoxGenre");			
		}
		return comboBoxGenre;
	}
	public JComboBox getComboBoxSender(){
		if (comboBoxSender == null) {						
			comboBoxSender = new JComboBox();
			comboBoxSender.setModel(new GuiMovieGuideSenderComboModel(this.getControl()));
			comboBoxSender.addItemListener(control);
			comboBoxSender.addActionListener(this.getControl());
			comboBoxSender.setActionCommand("clickONSenderComboBox");
			comboBoxSender.setName("jComboBoxSender");
		}
		return comboBoxSender;
	}
	public JComboBox getComboBoxDatum() {
		if (comboBoxDatum == null) {									
			comboBoxDatum = new JComboBox();
			comboBoxDatum.setModel(new GuiMovieGuideDatumComboModel(this.getControl()));
			comboBoxDatum.addItemListener(this.getControl());
			comboBoxDatum.addActionListener(this.getControl());
			comboBoxDatum.setActionCommand("clickONDatumComboBox");
			comboBoxDatum.setName("jComboBoxDatum");			
		}
		return comboBoxDatum;
	}
	public GuiMovieGuideFilmTableModel getMovieGuideFilmTableModel() {
		return filmTableModel;
	}
	public JTable getJTableFilm() {
		if (jTableFilm == null) {				
			filmTableModel = new GuiMovieGuideFilmTableModel(control);			
			mgFilmTableSorter = new GuiMovieGuideFilmTableSorter(filmTableModel);				 
			jTableFilm = new JTable(mgFilmTableSorter);
			jTableFilm.setDefaultRenderer(Object.class,new TableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){ 
					JTextPane p = new JTextPane();
					if (isSelected) {
						p.setForeground(table.getSelectionForeground());
						p.setBackground(table.getSelectionBackground());
					} else{
						p.setForeground(table.getForeground());
						p.setBackground(table.getBackground());
					}
				String val = "";                
                p.setMargin(new Insets(0, 0, 0, 0));
                if (value == null)
                    value = "";
                val = value.toString();
                p.setText(val);
                if(getSearch().length()>=1){
                	SerFormatter.highlight(p, getSearch());
				}else{
					SerFormatter.removeHighlights(p);
				}
                return p;
            }
				
            public String getSearch(){
                return getControl().getSearchString();
            }            
        });
			mgFilmTableSorter.setTableHeader(jTableFilm.getTableHeader());
			jTableFilm.setName("filmTable");
			jTableFilm.addMouseListener(control);		
			jTableFilm.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						control.filmTableChanged(jTableFilm);
					}
				}
			});
		}
		return jTableFilm;
	}	
}