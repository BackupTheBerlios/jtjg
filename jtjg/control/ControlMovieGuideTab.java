package control;
/*
 * ControlMovieGuideTab by Henneberger Ralph
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import model.BOMovieGuide;
import model.BOMovieGuideContainer;
import model.BOSender;
import model.BOSettingsMovieGuide;
import model.BOTimer;

import java.util.logging.Logger;

import presentation.GuiMainView;
import presentation.movieguide.GuiMovieGuideFilmTableModel;
import presentation.movieguide.GuiMovieGuideTimerTableModel;
import presentation.movieguide.GuiTabMovieGuide;
import service.SerAlertDialog;
import service.SerFormatter;
import service.SerMovieGuide2Xml;
import service.SerTimerHandler;

public class ControlMovieGuideTab extends ControlTab implements ActionListener,ItemListener, MouseListener, Runnable, KeyListener {
	
	GuiMainView mainView;
	GuiTabMovieGuide tab;
	BOMovieGuide boMovieGuide4Timer;
	boolean searchAbHeute = true;

	BOMovieGuideContainer movieList = new BOMovieGuideContainer();
	ArrayList titelListAktuell;
	ArrayList infoListAktuell;
		
	ArrayList boxSenderList;
	
	String searchString = "";
	private static String homePath;
	
	public static File movieGuideFile = new File(ControlMain.getSettingsPath().getWorkDirectory()+File.separator+"movieguide_"+SerFormatter.getAktuellDateString(0,"MM_yy")+".xml");
	public static File movieGuideFileNext = new File(ControlMain.getSettingsPath().getWorkDirectory()+File.separator+"movieguide_"+SerFormatter.getAktuellDateString(1,"MM_yy")+".xml");	
	
	private static final String DATE_FULL = "EEEE, dd. MMMM yyyy";
	private static final String GENRE  = ControlMain.getProperty("txt_genre2");
	private static final String SENDER = ControlMain.getProperty("txt_sender2");
	private static final String GET_FORMAT_GRE_CAL = SerFormatter.getFormatGreCal();
	private static final String GET_AKTUELL_DATE_STRING_0 = SerFormatter.getAktuellDateString(0,"MMMM");
	private static final String GET_AKTUELL_DATE_STRING_1 = SerFormatter.getAktuellDateString(1,"MMMM"); 
		
	String SelectedItemJComboBox;
	int SelectedItemJComboBoxSucheNach;
    int timerTableSize;    
	public ControlMovieGuideTab(GuiMainView view) {		
		this.setMainView(view);				
	}
	
	public void run() {
      this.setTab(this.getMainView().getTabMovieGuide());     
      this.getTab().getComboBoxDatum().setEnabled(false);
      this.getTab().getComboBoxGenre().setEnabled(false);
      this.getTab().getComboBoxSender().setEnabled(false);
      this.getTab().getComboBoxSucheNach().setEnabled(false);          
  
      if(getSettings().getMgLoadType()==0 && (!movieGuideFile.exists())){       
          try{
              new SerMovieGuide2Xml(null, this.getMainView()).start();
          }catch (Exception ex){
              Logger.getLogger("ControlMovieGuideTab").warning(ControlMain.getProperty("error_not_download"));
          }              	
      	}
      	if(getSettings().getMgLoadType()==0 && (!movieGuideFileNext.exists()) && (SerMovieGuide2Xml.checkNewMovieGuide()) && (movieGuideFile.exists())){       
      	    try{
      	        new SerMovieGuide2Xml(null, this.getMainView()).start();
      	    }catch (Exception ex){
      	        Logger.getLogger("ControlMovieGuideTab").warning(ControlMain.getProperty("error_not_download"));
      	    }              	
      	}
      	this.buildMG();
      	if ( (movieList.getAnnounceList().size()> 0) && (getSettings().isMgInfoDontForget()) ){      		
	    	infoNewMovieGuide("Es kommen Filme aus deiner Liste.\n Zur Auswahl auf den RecordListeButton drücken.");	    	
	    }	  	 
	}
	
	public void askToDownloadMG() {
	    if(getSettings().getMgLoadType()==1 && (!movieGuideFile.exists())){                 		
      		if (askToDownload(ControlMain.getProperty("txt_mg_info1")+GET_AKTUELL_DATE_STRING_0+" "+ControlMain.getProperty("txt_mg_info2"))){
          		try{
    				new SerMovieGuide2Xml(null, this.getMainView()).start();
    			}catch (Exception ex){
    				Logger.getLogger("ControlMovieGuideTab").warning(ControlMain.getProperty("error_not_download"));
    			}
          	}
      	}     
      	if(getSettings().getMgLoadType()==1 && (!movieGuideFileNext.exists()) && (SerMovieGuide2Xml.checkNewMovieGuide()) && (movieGuideFile.exists())){                 		
      		if (askToDownload(ControlMain.getProperty("txt_mg_info1")+GET_AKTUELL_DATE_STRING_1+" "+ControlMain.getProperty("txt_mg_info2"))){
          		try{
    				new SerMovieGuide2Xml(null, this.getMainView()).start();
    			}catch (Exception ex){
    				Logger.getLogger("ControlMovieGuideTab").warning(ControlMain.getProperty("error_not_download"));
    			}
          	}else{
          		movieList.importXML(movieGuideFile,getSettings().getMgSelectedChannels());	
          		beautifyGui(); 
          	}
      	}  
      	this.buildMG();
	}
	
	private void buildMG() {
	    if(this.getTitelMap()==null && (movieGuideFile.exists())){	
          	movieList.importXML(movieGuideFile,getSettings().getMgSelectedChannels());	  
          	if(!movieGuideFileNext.exists() && (!SerMovieGuide2Xml.checkNewMovieGuide())){
          		beautifyGui(); 
          	}          
          	if(movieGuideFileNext.exists() && (movieGuideFile.exists()) ){					    	
	          	setMovieGuideFile(movieGuideFileNext);                  
	          	movieList.importXML(movieGuideFileNext,getSettings().getMgSelectedChannels());                            	
	          	beautifyGui(); 
          	}          	
	    }	   
	}
	/*
	 * private void buildMG() {
	    if(this.getTitelMap()==null && (movieGuideFile.exists())){	
	    	System.out.println("1");
          	movieList.importXML(movieGuideFile,getSettings().getMgSelectedChannels());	  
          	if(!movieGuideFileNext.exists() && (!SerMovieGuide2Xml.checkNewMovieGuide())){
          		beautifyGui(); 
          	}
	   // }           
	    if(movieGuideFileNext.exists() && (movieGuideFile.exists()) ){			
	    	System.out.println("2");
          	setMovieGuideFile(movieGuideFileNext);                  
          	movieList.importXML(movieGuideFileNext,getSettings().getMgSelectedChannels());                            	
          	beautifyGui(); 
	    }
	}
	}

	 */
	/** 
	 * @param keine
	 * Es wird mit setTitelMapSelected die titelListAktuell für den heutigen Tag gebaut,
	 * es werden die Sender/Genre-ArrayListen mittel Collections alphabetisch sortiert.
	 * Alle ComboBoxen auf das 1 Element gesetz, weiter hin wird bei der FilmTable die 
	 * erste Row selectiert.
	 */
	private void beautifyGui(){
		this.getTab().getComboBoxDatum().setEnabled(true);	
		  this.getTab().getComboBoxGenre().setEnabled(true);
          this.getTab().getComboBoxSender().setEnabled(true);
          this.getTab().getComboBoxSucheNach().setEnabled(true);                           
          
		if(getSettings().getMgDefault()==0){
			setTitelMapSelected(GET_FORMAT_GRE_CAL,13);   // TitelMap Alles      			
		}else{
			setTitelMapSelected(GET_FORMAT_GRE_CAL,1);  // TitelMap für den heutigen Tag   
			this.getTab().getComboBoxDatum().setSelectedItem(GET_FORMAT_GRE_CAL);	 
		}              
        this.getTab().getComboBoxGenre().setSelectedIndex(0);          
        this.getTab().getComboBoxSender().setSelectedIndex(0);        
        try{
        	this.getTab().mgFilmTableSorter.setSortingStatus(0,2); //alphabetisch geordnet
        	getJTableFilm().getSelectionModel().setSelectionInterval(0,0); //1 Row selected
        }catch(ArrayIndexOutOfBoundsException ex){System.out.println(ex);}                        
   }
	
	
	private void downloadMovieGuide(){
		if(getMovieGuideFile().exists()){
			if(SerMovieGuide2Xml.checkNewMovieGuide()){				
				if (movieGuideFileNext.exists()){
					infoNewMovieGuide(ControlMain.getProperty("txt_mg_info3")+GET_AKTUELL_DATE_STRING_0+".\n"+ControlMain.getProperty("txt_mg_info3")+GET_AKTUELL_DATE_STRING_1);
				}else{
					infoNewMovieGuide(ControlMain.getProperty("txt_mg_info1")+GET_AKTUELL_DATE_STRING_1+ControlMain.getProperty("txt_mg_info2"));
				}
          	}else{          		
          		infoNewMovieGuide(ControlMain.getProperty("txt_mg_info3")+GET_AKTUELL_DATE_STRING_0+".\n"+ControlMain.getProperty("txt_mg_info4")+GET_AKTUELL_DATE_STRING_1+" "+ControlMain.getProperty("txt_mg_info5"));
          	}
		}else{				
			try{
				new SerMovieGuide2Xml(null, this.getMainView()).start();
			}catch (Exception ex){
				Logger.getLogger("ControlMovieGuideTab").warning(ControlMain.getProperty("error_not_download"));
			}		
		}
		if( (SerMovieGuide2Xml.checkNewMovieGuide()) && (!movieGuideFileNext.exists())){
			try{
				new SerMovieGuide2Xml(null, this.getMainView()).start();
			}catch (Exception ex){
				Logger.getLogger("ControlMovieGuideTab").warning(ControlMain.getProperty("error_not_download"));
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {	
		String action = e.getActionCommand();		
		if (action == "download") {
			downloadMovieGuide();
		}
		if (action == "neuEinlesen") {
			reInitFilmTable(15);
			getJTableFilm().getSelectionModel().setSelectionInterval(0,0);		
			getJTableFilm().scrollRectToVisible(getJTableFilm().getCellRect(1,1,true));
		}
		if (action == "select2Timer") {
		    if (this.getJTableTimer().getSelectedRow()>=0) {
				getTimerTableSelectToTimer();   
		    } else {
		        SerAlertDialog.alert(ControlMain.getProperty("error_no_timer_sel"), this.getMainView());
		    }
		}
		if ( (action == "suchen") || (action == "textsuche") ) {		
			if(this.getTitelMap()!=null){
				setSelectedItemJComboBox(this.getTab().getTfSuche().getText());
				searchString = getSelectedItemJComboBox();
				if(getSelectedItemJComboBoxSucheNach()==0) {								
					reInitFilmTable(2);
				}else{					
					reInitFilmTable(getSelectedItemJComboBoxSucheNach());
				}		
				getJTableFilm().getSelectionModel().setSelectionInterval(0,0);		
				getJTableFilm().scrollRectToVisible(getJTableFilm().getCellRect(1,1,true));
				findAndReplaceGui(getSelectedItemJComboBox());
				this.getTab().getJLabelSearchCount().setText("Treffer: "+getTitelMap().size());
			}
		}
		if ( (action == "zeitab") ) {				
			if(this.getTitelMap()!=null){										
				setSelectedItemJComboBox(this.getTab().getTfZeitab().getText());
				reInitFilmTable(14);					
				getJTableFilm().getSelectionModel().setSelectionInterval(0,0);		
				getJTableFilm().scrollRectToVisible(getJTableFilm().getCellRect(1,1,true));				
				this.getTab().getJLabelSearchCount().setText("Treffer: "+getTitelMap().size());
			}
		}
		if (action == "allDates") {	
			if(this.getTitelMap()!=null){
				this.getTab().getTfSuche().setText("");
				this.getTab().getTfZeitab().setText("");
				searchString = "";
				this.getTab().getComboBoxGenre().setSelectedIndex(0);          
				this.getTab().getComboBoxSender().setSelectedIndex(0); 
				reInitFilmTable(13);
				this.getTab().getJLabelSearchCount().setText("");
			}
		}
		if (action == "movieGuidePath") {
			this.openFileChooser();
		}
		if (action == "clickONDatumComboBox"){
			JComboBox comboBox = this.getTab().getComboBoxDatum();
			if(comboBox.getItemCount()>=1){
				this.getTab().getJLabelSearchCount().setText("");
				this.getTab().getComboBoxGenre().setSelectedIndex(0);          
				this.getTab().getComboBoxSender().setSelectedIndex(0);
				setGui4SelectionSearch(1, "",comboBox);			
			}
		}
		if (action == "clickONGenreComboBox"){
			JComboBox comboBox = this.getTab().getComboBoxGenre();
			if(comboBox.getItemCount()>=1){
				setGui4SelectionSearch(11, GENRE,comboBox);							
			}
		}
		if (action == "clickONSenderComboBox"){
			JComboBox comboBox = this.getTab().getComboBoxSender();
			if(comboBox.getItemCount()>=1){				
				setGui4SelectionSearch(12, SENDER,comboBox);				
			}
		}
	}
	
	private void findAndReplaceGui(String search){			
		if(search.length()>=1){
			SerFormatter.highlight(this.getTab().getTaEpisode(),search);								
			SerFormatter.highlight(this.getTab().getTaGenre(),search);		
			SerFormatter.highlight(this.getTab().getTaAudioVideo(),search);
			SerFormatter.highlight(this.getTab().getTaLand(),search);													
			SerFormatter.highlight(this.getTab().getTaDarsteller(),search);
			this.getTab().getTaDarsteller().setCaretPosition(0);		
			SerFormatter.highlight(this.getTab().getTaBeschreibung(),search);		
			this.getTab().getTaBeschreibung().setCaretPosition(0);								
		}else{
			SerFormatter.removeHighlights(this.getTab().getTaEpisode());								
			SerFormatter.removeHighlights(this.getTab().getTaGenre());		
			SerFormatter.removeHighlights(this.getTab().getTaAudioVideo());
			SerFormatter.removeHighlights(this.getTab().getTaLand());													
			SerFormatter.removeHighlights(this.getTab().getTaDarsteller());
			this.getTab().getTaDarsteller().setCaretPosition(0);		
			SerFormatter.removeHighlights(this.getTab().getTaBeschreibung());		
			this.getTab().getTaBeschreibung().setCaretPosition(0);
		}
	}
	
	/**
	 * Change-Events of the GuiTabMovieGuide
	 */
	public void itemStateChanged (ItemEvent event) {
		String comp = event.getSource().getClass().getName();		
		if (comp.equals("javax.swing.JCheckBox")) {
			JCheckBox checkBox = (JCheckBox)event.getSource();
			if (checkBox.getName().equals("showAbHeute")) {
				searchAbHeute = checkBox.isSelected();		
			}
		}else{	
			JComboBox comboBox = (JComboBox)event.getSource();			
			if (comboBox.getName().equals("jComboBoxDatum")) {	
				setGui4SelectionSearch(1, "",comboBox);				
			}else	
			if (comboBox.getName().equals("jComboBoxGenre")) {		
				setGui4SelectionSearch(11, GENRE,comboBox);				
			}else
			if (comboBox.getName().equals("jComboBoxSucheNach")) {		
				SelectedItemJComboBoxSucheNach = (comboBox.getSelectedIndex()+2);					
			}else
			if (comboBox.getName().equals("jComboBoxSender")) {	
				setGui4SelectionSearch(12, SENDER,comboBox);				
			}
		}
	}
    private void setGui4SelectionSearch(int reInitTable, String selectedItem, JComboBox comboBox){
    	this.getTab().getTfSuche().setText("");
		this.getTab().getTfZeitab().setText("");
		searchString = "";
		if(selectedItem.length()<1){
			setSelectedItemJComboBox(comboBox.getSelectedItem().toString());
			reInitFilmTable(reInitTable);
			getJTableFilm().getSelectionModel().setSelectionInterval(0,0);	
			getJTableFilm().scrollRectToVisible(getJTableFilm().getCellRect(1,1,true));
			this.getTab().getJLabelSearchCount().setText("Treffer: "+getTitelMap().size());
		}else{
			if(!comboBox.getSelectedItem().toString().equals(selectedItem)){
				setSelectedItemJComboBox(comboBox.getSelectedItem().toString());
				reInitFilmTable(reInitTable);		
				getJTableFilm().getSelectionModel().setSelectionInterval(0,0);
				getJTableFilm().scrollRectToVisible(getJTableFilm().getCellRect(1,1,true));
				this.getTab().getJLabelSearchCount().setText("Treffer: "+getTitelMap().size());
			}			
		}				
    }
	/**
	 * @return int
	 * Gib den selectierten Eintrag der SucheNach-ComboBox zurück
	 */
	public int getSelectedItemJComboBoxSucheNach(){
		return SelectedItemJComboBoxSucheNach;
	}
	
	/**
	 * @param value
	 * Setz den selectierten Eintrag der SucheNach-ComboBox
	 */
	private void setSelectedItemJComboBox(String value){
		this.SelectedItemJComboBox = value;
	}
	
	/**
	 * @return String 
	 * Gibt das selectierte Element der ComboBox zurück
	 */
	public String getSelectedItemJComboBox(){
		return SelectedItemJComboBox;
	}
	
	/**
	 * @return Selectierte Row der FilmTable
	 * 
	 */
	/*
	 * public Integer getSelectRowFilmTable(){		
		Integer retVal = new Integer(0);
		try{
		if (this.getTab().mgFilmTableSorter.modelIndex(this.getJTableFilm().getSelectedRow())<=0){
			return new Integer(0);
		}	
		}catch(ArrayIndexOutOfBoundsException e){}
		return new Integer(this.getTab().mgFilmTableSorter.modelIndex(this.getJTableFilm().getSelectedRow()));
	}
	 */
	public Integer getSelectRowFilmTable(){		
		Integer retVal = new Integer(0);
		try{
			retVal = new Integer(this.getTab().mgFilmTableSorter.modelIndex(this.getJTableFilm().getSelectedRow()));
		}catch(ArrayIndexOutOfBoundsException e){}
		return retVal;
	}
	/**
	 * @return Selectierte Row der TimerTable
	 * 
	 */
	public int getSelectRowTimerTable(){		    
		return this.getTab().mgTimerTableSorter.modelIndex(this.getJTableTimer().getSelectedRow());
	}
	
	/**
	 * @param bomovieguide
	 * Setzt das aktuelle BOMovieGuide Object, das was in der FilmTable selectiert würde
	 */
	public void setBOMovieGuide4Timer(BOMovieGuide bomovieguide){
		boMovieGuide4Timer = bomovieguide;
	}
	
	/**
	 * @return
	 * Gibt aktuelle BOMovieGuide Object, das was in der FilmTable selectiert würde zurück
	 */
	public BOMovieGuide getBOMovieGuide4Timer(){
		return boMovieGuide4Timer;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent me) {		
		JTable table = (JTable) me.getSource();
		String tableName = table.getName();								
		if (tableName == "filmTable") {				
			reInitTable(new Integer(this.getTab().mgFilmTableSorter.modelIndex(table.getSelectedRow())));	
			if(this.getTab().getTfSuche().getText().length()>=0){
				findAndReplaceGui(this.getTab().getTfSuche().getText());
			}
		}
		if (tableName == "timerTable") {							
			this.getTab().getTaAudioVideo().setText("");			
			SerFormatter.underScore(this.getTab().getTaAudioVideo()," "+getBOMovieGuide4Timer().getTon().get(getSelectRowTimerTable())+" ",false,0);			
			SerFormatter.underScore(this.getTab().getTaAudioVideo(), ControlMain.getProperty("txt_video"),true,this.getTab().getTaAudioVideo().getText().length());
			SerFormatter.underScore(this.getTab().getTaAudioVideo()," "+getBOMovieGuide4Timer().getBild().get(getSelectRowTimerTable()),false,this.getTab().getTaAudioVideo().getText().length());
			SerFormatter.underScore(this.getTab().getTaAudioVideo(), ControlMain.getProperty("txt_audio"),true,0);
			if(me.getClickCount()>=2){ 						
				getTimerTableSelectToTimer();
		 	}
		}		
	}
	
	/**
	 * @param modelIndex
	 * Aktuallisiert die TimerTable und die entsprechen Textfelder (Inhalt, Genre...) für
	 * die selectierte Row(Titel) der FilmTable
	 */
	private void reInitTable(Integer modelIndex){
		setBOMovieGuide4Timer((BOMovieGuide)getTitelMap().get(modelIndex.intValue()));					
		clearAllTextArea();
		
		SerFormatter.underScore(this.getTab().getTaEpisode()," "+getBOMovieGuide4Timer().getEpisode(),false,0);
		SerFormatter.underScore(this.getTab().getTaEpisode(),ControlMain.getProperty("txt_episode"),true,0);		
								
		SerFormatter.underScore(this.getTab().getTaGenre()," "+getBOMovieGuide4Timer().getGenre(),false,0);
		SerFormatter.underScore(this.getTab().getTaGenre(),ControlMain.getProperty("txt_genre"),true,0);		
				
		SerFormatter.underScore(this.getTab().getTaDarsteller()," "+getBOMovieGuide4Timer().getDarsteller(),false,0);
		SerFormatter.underScore(this.getTab().getTaDarsteller(),ControlMain.getProperty("txt_darsteller"),true,0);
		this.getTab().getTaDarsteller().setCaretPosition(0);				
				
		SerFormatter.underScore(this.getTab().getTaBeschreibung()," "+getBOMovieGuide4Timer().getInhalt(),false,0);
		SerFormatter.underScore(this.getTab().getTaBeschreibung(), ControlMain.getProperty("txt_inhalt"),true,0);
		this.getTab().getTaBeschreibung().setCaretPosition(0);
				
		SerFormatter.underScore(this.getTab().getTaAudioVideo(), ControlMain.getProperty("txt_audio"),true,0);
		SerFormatter.underScore(this.getTab().getTaAudioVideo()," / ",false,ControlMain.getProperty("txt_audio").length());
		SerFormatter.underScore(this.getTab().getTaAudioVideo(), ControlMain.getProperty("txt_video"),true,ControlMain.getProperty("txt_audio").length()+3);
						
		SerFormatter.underScore(this.getTab().getTaLand()," "+getBOMovieGuide4Timer().getLand()+" / "+getBOMovieGuide4Timer().getJahr()+" / ",false,0);
		SerFormatter.underScore(this.getTab().getTaLand(), ControlMain.getProperty("txt_prod"),true,0);				
		SerFormatter.underScore(this.getTab().getTaLand(), ControlMain.getProperty("txt_regie"),true,this.getTab().getTaLand().getText().length());
		SerFormatter.underScore(this.getTab().getTaLand()," "+getBOMovieGuide4Timer().getRegie(),false,this.getTab().getTaLand().getText().length());																
		
		setTimerTableSize(getBOMovieGuide4Timer().getDatum().size());
		reInitTimerTable();
	}
	
	/**
	 * @param senderName
	 * @return BOSender
	 * Gibts das aktuelle BOSender für den seletierter Timer zurück
	 * @throws IOException
	 */
	private BOSender getSenderObject(String senderName) throws IOException{
	    BOSender sender;
        ArrayList senderList = this.getBoxSenderList();
        for (int i=0; i<senderList.size(); i++) {
            sender = (BOSender)senderList.get(i);
            String boxSenderName = SerFormatter.replace(sender.getName()," ","");
            String mgSenderName = SerFormatter.replace(senderName," ","");
            if (boxSenderName.equals(mgSenderName)) {
                return sender;
            }
        }
        throw new IOException();
	}
	
	/**
	 * Erzeugt einen neuen Timer (BOTimer) aus der Selectierten Row(Titel) der TimerTable
	 */
	private void getTimerTableSelectToTimer(){
		try {
            int modelIndexTimer=getSelectRowTimerTable();
            String senderName = (String)getBOMovieGuide4Timer().getSender().get(modelIndexTimer);
            BOSender sender = this.getSenderObject(senderName);
            
            BOTimer botimer = new BOTimer();  	
            int timeBefore = Integer.parseInt(ControlMain.getSettingsRecord().getRecordTimeBefore())*-1;
            int timeAfter = Integer.parseInt(ControlMain.getSettingsRecord().getRecordTimeAfter());
            int timeAnnounce = (Integer.parseInt(ControlMain.getSettingsRecord().getRecordTimeBefore())+2)*60000;
            
            botimer.setModifiedId("new");
            botimer.channelId=sender.getChanId();
            botimer.setSenderName(sender.getName());		
            botimer.unformattedStartTime=SerFormatter.getGC((GregorianCalendar)getBOMovieGuide4Timer().getStart().get(modelIndexTimer),timeBefore);
            botimer.unformattedStopTime=SerFormatter.getGC((GregorianCalendar)getBOMovieGuide4Timer().getEnde().get(modelIndexTimer),timeAfter);			
            botimer.setAnnounceTime( String.valueOf(new GregorianCalendar().getTimeInMillis()-timeAnnounce));
            botimer.setEventRepeatId("0");
            botimer.setEventTypeId("5");
            botimer.setDescription(getBOMovieGuide4Timer().getTitel());
            
            SerTimerHandler.saveTimer(botimer, true, true);
        } catch (IOException e) {            
            Logger.getLogger("ControlMovieGuideTab").warning(ControlMain.getProperty("error_sender"));
        }		
	}
	
    /**
     * @param size
     * Setzt die Größe für die TimerTable, die Größe kommt vom BOMovieGuide Object, wird bestimt
     * durch die Anzahl der SendeDaten des Films der in der FilmTable selectiert wurde.
     */
    private void setTimerTableSize(int size){
    	timerTableSize = size;
    }
    
    /**
     * @return
     * Gibt die Größe zurück die Aktuallisierung der TimerTable gebraucht wird
     */
    public int getTimerTableSize(){
    	return timerTableSize;
    }
    
	public void mouseClicked(MouseEvent me) {
	}

	public void mouseReleased(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}

	public void mouseEntered(MouseEvent me) {
	}

	public GuiMainView getMainView() {
		return mainView;
	}

	/**
	 * @param mainView
	 *            The mainView to set.
	 */
	public void setMainView(GuiMainView mainView) {
		this.mainView = mainView;
	}

	private void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setApproveButtonText("Auswählen");
		fc.setApproveButtonToolTipText("Datei auswählen");
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().toString();		
			try{
				new SerMovieGuide2Xml(path, this.getMainView()).start();
			}catch (Exception e) {
				Logger.getLogger("ControlMovieGuideTab").warning(ControlMain.getProperty("error_read_mg"));	
			}
		}
	}
	
	/**
	 * @return
	 * Gibt den Namen des aktuellen MovieGuides-Documents zurück.
	 */
	private File getMovieGuideFile(){
		return movieGuideFile;
	}
	
	/**
	 * @param filename
	 * Setzt den Namen des aktuellen MovieGuides-Documents
	 */
	private void setMovieGuideFile(File filename){
		movieGuideFile = filename;		
	}
	public ArrayList getSenderList(){
		ArrayList senderList = new ArrayList();
		senderList.add(SENDER);
		senderList.addAll(movieList.getSenderList());
    	return senderList;
    }
	public ArrayList getGenreList(){
		ArrayList genreList = new ArrayList();
		genreList.add(GENRE);
		genreList.addAll(movieList.getGenreList());
    	return genreList;	
    }
	public ArrayList getDatumList(){    	
    	return movieList.getDatumList();
    }
    /**
     * @return
     * TitelListMap : Zähler, BOMovieGuide-Object
     * Gibt die aktulle TitelListMap zurück, die enthält immer die daten die entweder gesucht,
     * oder per DatumComboBox ausgewählt würden.
     */
    public ArrayList getTitelMap(){
    	return titelListAktuell;		    	
    }    	           
    public ArrayList getInfoList(){
    	return infoListAktuell;		    	
    }   
    
    /**
     * @param searchValue
     * @param value
     * bauen der AnzeigeMap nach Suchkriterien  
     */
    public void setTitelMapSelected(Object searchValue,int value){    	    	
		if(value == 15){
			titelListAktuell=movieList.getAnnounceList();					
		}else {
			titelListAktuell=movieList.search(searchValue,value);
		}
    }
  
    public JTable getJTableFilm() {
		return this.getTab().getJTableFilm();
	}
    
    /**
     * @return
     */
    public JTable getJTableTimer() {
		return this.getTab().getJTableTimer();
	}
    
    private GuiMovieGuideTimerTableModel getMovieGuideTimerTableModel() {
		return this.getTab().getGuiMovieGuideTimerTableModel();
	}
    
    /**
     * 
     */
    public void reInitTimerTable() {
		if (this.getMainView().getMainTabPane().tabMovieGuide != null) { 
			this.getMovieGuideTimerTableModel().fireTableDataChanged();
		}
	}
    
	/**
	 * @return
	 */
	private GuiMovieGuideFilmTableModel getMovieGuideFilmTableModel() {
		return this.getTab().getMovieGuideFilmTableModel();
	}
	
	/**
	 * @param value
	 */
	public void reInitFilmTable(int value) {
		if (this.getMainView().getMainTabPane().tabMovieGuide != null) { 
			this.getMovieGuideFilmTableModel().fireTableDataChanged(value);
		}
	}
	public GuiTabMovieGuide getTab() {
		return tab;
	}
	/**
	 * @param tab The tab to set.
	 */
	public void setTab(GuiTabMovieGuide tab) {
		this.tab = tab;
	}
	
    /**
     * @return Returns the boxSenderList.
     */
    public ArrayList getBoxSenderList() throws IOException {
        if (boxSenderList==null) {
            boxSenderList=ControlMain.getBoxAccess().getAllSender();
        }
        return boxSenderList;
    }
    
    /**
     * @param table
     * Anzeige aktualisieren der Inhalte, wenn in der TimerTable mit der Tastutur selectiert wurde
     */
    public void  timerTableChanged(JTable table) {
		if (table.getSelectedRow() > -1){			
			this.getTab().getTaAudioVideo().setText("");			
			SerFormatter.underScore(this.getTab().getTaAudioVideo()," "+getBOMovieGuide4Timer().getTon().get(getSelectRowTimerTable())+" ",false,0);			
			SerFormatter.underScore(this.getTab().getTaAudioVideo(), ControlMain.getProperty("txt_video"),true,this.getTab().getTaAudioVideo().getText().length());
			SerFormatter.underScore(this.getTab().getTaAudioVideo()," "+getBOMovieGuide4Timer().getBild().get(getSelectRowTimerTable()),false,this.getTab().getTaAudioVideo().getText().length());
			SerFormatter.underScore(this.getTab().getTaAudioVideo(), ControlMain.getProperty("txt_audio"),true,0);
		}			
    }
    
    /**
     * @param table
     * Neu bauen der FilmTable bei Events neues Datum , Sorter usw.
     */
    public void filmTableChanged(JTable table) {
		if (table.getSelectedRow() > -1){
			reInitTable(new Integer(this.getTab().mgFilmTableSorter.modelIndex(table.getSelectedRow())));
			if(this.getTab().getTfSuche().getText().length()>=0){
				findAndReplaceGui(this.getTab().getTfSuche().getText());
			}
		}			
	}
    
    /* (non-Javadoc)
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke){   
    	if(ke.getKeyCode()==KeyEvent.VK_ENTER){
    		setSelectedItemJComboBox(this.getTab().getTfSuche().getText());
			if(getSelectedItemJComboBoxSucheNach()==0) {								
				reInitFilmTable(2);
			}else{					
				reInitFilmTable(getSelectedItemJComboBoxSucheNach());
			}
			getJTableFilm().getSelectionModel().setSelectionInterval(0,0);
    	}
    }
    public void keyTyped(KeyEvent ke){     
    }
    public void keyReleased(KeyEvent ke){     	
    }
    private BOSettingsMovieGuide getSettings() {
        return ControlMain.getSettingsMovieGuide();
    }
    
    private void clearAllTextArea(){
    	this.getTab().getTaLand().setText("");	
		this.getTab().getTaAudioVideo().setText("");
		this.getTab().getTaBeschreibung().setText("");
		this.getTab().getTaDarsteller().setText("");
		this.getTab().getTaGenre().setText("");
		this.getTab().getTaEpisode().setText("");
    }    
    public String getSearchString(){
    	return searchString;
    }

    private boolean askToDownload(String value) {
		int res = JOptionPane.showOptionDialog(this.getTab(), value, ControlMain.getProperty("button_pmg_download"), 0, JOptionPane.QUESTION_MESSAGE, null, new String[]{
				ControlMain.getProperty("button_pmg_download"), ControlMain.getProperty("button_cancel")}, "");
		return res == 0;
	}
    private void infoNewMovieGuide(String value){
    	JOptionPane.showMessageDialog(this.getTab(),value);
    }   
}