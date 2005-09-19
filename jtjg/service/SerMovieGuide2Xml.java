package service;
/*
 * SerMovieGuide2Xml.java by Ralph Henneberger, Alexander Geist
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JProgressBar;

import model.BOSettingsProxy;

import org.dom4j.Document;
import org.dom4j.Element;


import presentation.GuiMainView;
import service.XML.SerXMLHandling;

import control.ControlMain;
import control.ControlMovieGuideTab;
import org.apache.log4j.Logger;

public class SerMovieGuide2Xml extends Thread{
    ArrayList htToken = new ArrayList(5);
    GuiMainView mainView;
    Document doc;    
    Element movie;
    String path; 
    JProgressBar bar;
    FileWriter fw = null;    
    int xml ;
    BOSettingsProxy proxySettings = ControlMain.getSettingsProxy();
    public SerMovieGuide2Xml(String file, GuiMainView view) {   
    	mainView=view;
    	bar=view.getTabMovieGuide().getJProgressBarDownload();
    	path = file;
    	doc = SerXMLHandling.createEmptyMovieguideFile();		
		createArrayList();				  
    }
          
    private final void createArrayList() {
        htToken.add("Titel:");
        htToken.add("Episode:");
        htToken.add("Produktionsland:");
        htToken.add("Bild- und Tonformate:");
        htToken.add("Darsteller:");
    }
    
    private void createElement(String input) {
        try {        	                    	
            switch ((htToken.indexOf(input.substring(0, input.indexOf(":")+1)))) {               
                case 0:
                SerXMLHandling.setElementInElement(movie,"titel", input.substring(input.indexOf(":") + 2));                
                    break;
                case 1:
                SerXMLHandling.setElementInElement(movie,"episode",input.substring(9, input.indexOf("Genre:") - 2));
                SerXMLHandling.setElementInElement(movie,"genre", input.substring(input.indexOf("Genre:") + 7, input.indexOf("Länge:") - 2));
                SerXMLHandling.setElementInElement(movie,"dauer", input.substring(input.indexOf("Länge:") + 7, input.indexOf("Stunden") - 1));
                    break;
                case 2:
                SerXMLHandling.setElementInElement(movie,"land",input.substring(input.indexOf(":") + 2, input.indexOf("Produktionsjahr:") - 2));
                SerXMLHandling.setElementInElement(movie,"jahr", input.substring(input.indexOf("Produktionsjahr:") + 17, input.indexOf("Regie:") - 2));
                SerXMLHandling.setElementInElement(movie,"regie", input.substring(input.indexOf("Regie:") + 7));
                    break;
                case 3:
                SerXMLHandling.setElementInElement(movie,"bild",input.substring(input.indexOf(":") + 2, input.indexOf("/")));
                SerXMLHandling.setElementInElement(movie,"ton", input.substring(input.indexOf("/") + 1));
                    break;
                case 4:
                SerXMLHandling.setElementInElement(movie,"darsteller", input.substring(input.indexOf(":") + 2));
                    break;
            }     
        	} catch (StringIndexOutOfBoundsException e) {
        		System.out.println("StringIndexOutOfBoundsException: " + e.getMessage());
        	} catch (NullPointerException e) {
        		System.out.println("NullPointerException: " + e.getMessage());
        	}
        
    }
    
    private boolean[] getLineCounter(String input) {
        boolean[] value = new boolean[2];
        try {
            value[0] = false;
            value[0] = SerFormatter.isCorrectDate(input.substring(input.indexOf(":") + 2));
        } catch (StringIndexOutOfBoundsException ex) {}
        try {
            value[1] = false;
            value[1] = htToken.contains(input.substring(0, input.indexOf(":")+1));
        } catch (StringIndexOutOfBoundsException ex) {}
        return value;
    }
   
    private URLConnection getConnection() throws IOException {
    URLConnection con;    
    if (path != null) {
        con = (new File(path).toURL()).openConnection(); 
    } else {    	
        if (proxySettings.isUse()) {
            System.getProperties().put("proxyHost", proxySettings.getHost());
            System.getProperties().put("proxyPort", proxySettings.getPort());
        }
    	
	    URL url = null;
	    if(!ControlMovieGuideTab.movieGuideFile.exists()){
	    url = new URL("http://www.premiere.de/content/download/mguide_d_s_"+ SerFormatter.getAktuellDateString(0,"MM_yy")+".txt");
	    xml = 0;
	    }else{
	        url = new URL("http://www.premiere.de/content/download/mguide_d_s_"+ SerFormatter.getAktuellDateString(1,"MM_yy")+".txt");     
	        xml = 1;
	    }    
	    if(ControlMain.getSettingsMovieGuide().isMgStoreOriginal()){                                    
	        fw = new FileWriter(ControlMain.getSettingsPath().getWorkDirectory()+File.separator+url.getFile().substring(18));      
	    }	    
	    con =url.openConnection();
	    if (proxySettings.isUse()) {
            con.setRequestProperty("Proxy-Authorization",
                                  "Basic " + proxySettings.getUserPass());
        }
    }
    return con;
    }
    
    public void run()  {    
        try {        
        URLConnection con = this.getConnection();        
        int fileLength = con.getContentLength();
            bar.setMaximum(fileLength);
            BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream(),"ISO8859-1"));                        
            String input = new String();
            StringBuffer inhalt = new StringBuffer();
            boolean[] lineCounter = new boolean[2];
            int sumValue = 0;
            while ((input = in.readLine()) != null) {
            if( (ControlMain.getSettingsMovieGuide().isMgStoreOriginal()) && (path==null) ){
            	fw.write(input+"\n");
            }
            sumValue = sumValue+input.getBytes().length;
            bar.setValue(sumValue);            
            lineCounter = getLineCounter(input);
            	if (lineCounter[0]) {                
                    movie = doc.getRootElement().addElement("entry");
                    SerXMLHandling.setElementInElement(movie,"sender",input.substring(0, input.indexOf(":")));
                    SerXMLHandling.setElementInElement(movie,"datum",SerFormatter.getCorrectDate(input.substring(input.indexOf(":") + 2)));
                    SerXMLHandling.setElementInElement(movie,"start", input.substring(input.indexOf("/")+1));
                } else if (lineCounter[1]) {                                    
                    createElement(input);
                } else if ((lineCounter[0] && lineCounter[1]) == false){
                    if(input.length() > 0){
                        inhalt.append(input);
                    }else{
                    	SerXMLHandling.setElementInElement(movie,"inhalt", inhalt.toString());
                        inhalt.setLength(0);
                    }
                }               
            } 
            if (proxySettings.isUse()) {
                System.getProperties().remove("proxyHost");
                System.getProperties().remove("proxyPort");
            }
            bar.setValue(fileLength);
            if (xml == 0){
            	SerXMLHandling.saveXMLFile(ControlMovieGuideTab.movieGuideFile, doc);
            }else {
            	SerXMLHandling.saveXMLFile(ControlMovieGuideTab.movieGuideFileNext, doc);
            }
            if( (ControlMain.getSettingsMovieGuide().isMgStoreOriginal()) && (path==null)){
            	fw.close();
            }
            mainView.getTabMovieGuide().getControl().run();            
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }        
        
    }
    public static boolean checkNewMovieGuide(){    
    	BOSettingsProxy proxySettings = ControlMain.getSettingsProxy();
    	boolean value = false;
    try{
    	if (proxySettings.isUse()) {
            System.getProperties().put("proxyHost", proxySettings.getHost());
            System.getProperties().put("proxyPort", proxySettings.getPort());
        }
	    URLConnection con;   
	    URL url = new URL("http://www.premiere.de/content/download/mguide_d_s_"+ SerFormatter.getAktuellDateString(1,"MM_yy")+".txt");        
		con =url.openConnection();
		if (proxySettings.isUse()) {
            con.setRequestProperty("Proxy-Authorization",
                                  "Basic " + proxySettings.getUserPass());
        }
		if(con.getContentLength()>0){
			value = true;
		}
		if (proxySettings.isUse()) {
            System.getProperties().remove("proxyHost");
            System.getProperties().remove("proxyPort");
        }
    }catch (IOException ioex){	
         Logger.getLogger("SerMovieGuide2Xml").error(ioex.getMessage());	
    }
    return value;
    }
}