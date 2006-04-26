package boxConnection.control;
/*
SerBoxControlNeutrino.java by Geist Alexander 

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import model.BOBouquet;
import model.BOEpg;
import model.BOEpgDetails;
import model.BOPid;
import model.BOPids;
import model.BOSender;
import model.BOTimer;
import model.BOTimerList;

import org.apache.log4j.Logger;


import presentation.timer.GuiNeutrinoRecordTimerTableModel;
import presentation.timer.GuiNeutrinoSystemTimerTableModel;
import presentation.timer.GuiRecordTimerTableModel;
import presentation.timer.GuiSystemTimerTableModel;
import service.SerFormatter;
import control.ControlMain;
import control.timer.ControlTimerTab;

/**
 * Schnittstelle zum NeutrinoImage
 */
public class SerBoxControlNeutrino extends SerBoxControl{
           
    public BOTimerList reReadTimerList() throws IOException {
        timerList=this.readTimer();
        return timerList;
    }
    
	public String getName() {
		return "Neutrino";
	}
	
	public ArrayList getBoxVersion() throws IOException {
	    ArrayList version = new ArrayList();
	    BufferedReader input = getConnection("/control/info?version");
		
		String line;
		while((line=input.readLine())!=null) {
		    version.add(line);
		}
		return version;
	}
	
	public String getChanIdOfRunningSender() throws IOException {
		BufferedReader input = getConnection("/control/zapto");
		
		String line;
		while((line=input.readLine())!=null) {
			return line;
		}
		return line;
	}
	
	public BOSender getRunningSender() throws IOException {
	    ArrayList senderList = getAllSender();
	    String runningChanId = getChanIdOfRunningSender();

	    for (int i = 0; i < senderList.size(); i++) { 
	        BOSender sender = (BOSender) senderList.get(i);
			if (sender.getChanId().equals(runningChanId)) {
				    return sender;
			}
		}
	    return null;
	}
	
	public BufferedReader getConnection(String request) throws IOException {
		return new BufferedReader(new InputStreamReader(new URL("http://"+ControlMain.getBoxIpOfActiveBox()+request).openStream(),"ISO-8859-1"));
	}
		
	public BOPids getPids() throws IOException {
	    BOPids pids = new BOPids();
		String line;
	
		BufferedReader input = getConnection("/control/zapto?getallpids");

	    pids.setVPid(new BOPid(Integer.toHexString(Integer.parseInt(input.readLine())), "video", 0));
 
		while((line=input.readLine())!=null) {
			StringTokenizer st = new StringTokenizer(line);
		  	
			String number = Integer.toHexString(Integer.parseInt(st.nextToken()));
			String name = new String();
			while (st.hasMoreElements()) {
		  	 	name += " "+st.nextToken();    
			}
			name=name.trim();
		  	 
			if (name!= null && name.equals("vtxt")) {
		  	 	pids.setVtxtPid(new BOPid(number, name, 2));
		  	} else if (name!= null && name.equals("pmt")) {
                pids.setPmtPid(new BOPid(number, name, 3));
            } else {
			    pids.getAPids().add(new BOPid(number, name, 1));  
		  	}
		}
		return pids;
	}	 
	
	public ArrayList getBouquetList() throws IOException {
		ArrayList bouquetList = new ArrayList();
		String line;
		
		BufferedReader input = getConnection("/control/getbouquets");
			
		while((line=input.readLine())!=null) {
        	StringTokenizer st = new StringTokenizer(line);
			String nummer = st.nextToken();
			String name = new String();
			while (st.hasMoreTokens()) {
				name += st.nextToken();
				name += " ";
			}
			bouquetList.add(new BOBouquet(nummer, name.trim()));
		}
		return bouquetList;
	}
	
	public ArrayList getAllSender() throws IOException {
        if (senderList==null) {
            senderList = new ArrayList();
            String line;
        
            BufferedReader in = getConnection("/control/channellist");
            while ((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                String chanId = st.nextToken();
                
                String name = new String(); 
                while (st.hasMoreTokens()) {
                    name += st.nextToken();
                    name += " ";
                }
                senderList.add(new BOSender("1",chanId, name.trim())); 
            }    
        }
        return senderList;
	}
	
	public boolean isTvMode() throws IOException{
        if (isTvMode==null) {
            BufferedReader input = getConnection("/control/getmode");
            
            String line;
            while((line=input.readLine())!=null) {
                isTvMode=new Boolean(line.equalsIgnoreCase("tv"));
            }
        }
		return isTvMode.booleanValue();
	}
	
	public ArrayList getSender(BOBouquet bouquet) throws IOException {
		ArrayList senderList = new ArrayList();
		String line;
		String mode;
		if (isTvMode()) {
		    mode = "TV";
		} else {
		    mode = "RADIO";
		}
	
		BufferedReader input = getConnection("/control/getbouquet?bouquet="+bouquet.getBouquetNummer()+"&mode="+mode);
		
		while((line=input.readLine())!=null) {
			StringTokenizer st = new StringTokenizer(line);
			String nummer = st.nextToken();
			String channelId = st.nextToken();
			
			String name = new String();
			while (st.hasMoreTokens()) {
				name += st.nextToken();
				name += " ";
			}
			senderList.add(new BOSender(nummer,channelId, name.trim()));
		}
		return senderList;
	}	 
	
	public String zapTo(String channelId) throws IOException {
		//BufferedReader input = SerBoxControl.getConnection("/fb/switch.dbox2?zapto="+channelId);
		BufferedReader input = getConnection("/control/zapto?"+channelId);
		
		String line;
		while((line=input.readLine())!=null) {
			return line;
		}
		return line;
	}	
	public String setRecordModusWithPlayback() throws IOException {
		BufferedReader input = getConnection("/control/setmode?record=start&stopplayback=true");
		
		String line;
		while((line=input.readLine())!=null) {
			return line;
		}
		return line;
	} 
	public String setRecordModus() throws IOException {
		BufferedReader input = getConnection("/control/setmode?record=start");
		
		String line;
		while((line=input.readLine())!=null) {
			return line;
		}
		return line;
	}
	 public String stopRecordModus() throws IOException {
		BufferedReader input = getConnection("/control/setmode?record=stop");
		
		String line;
		while((line=input.readLine())!=null) {
			return line;
		}
		return line;
	} 
	public String setRadioTvMode(String mode) throws IOException {
        isTvMode=null;
		BufferedReader input = getConnection("/control/setmode?"+mode);
		
		String line;
		while((line=input.readLine())!=null) {
			return line;
		}
		return line;
	}
	
	public ArrayList getEpg(BOSender sender) throws IOException {
		ArrayList epgList=sender.getEpg();
		BufferedReader input = getConnection("/control/epg?"+sender.getChanId());
		String line, eventId, duration, title, valueStart, valueDuration;
		GregorianCalendar startDate, endDate;
                
		while((line=input.readLine())!=null) {
            BOEpg epgObj = new BOEpg();
			StringTokenizer st = new StringTokenizer(line);
			
            epgObj.setEventId(st.nextToken()); //check if epgObj exists
            if (!epgList.contains(epgObj)) {
                epgObj.setUnformattedStart(st.nextToken());
                epgObj.setUnformattedDuration(st.nextToken());
                
                title = new String();
                while (st.hasMoreTokens()) {
                    title += st.nextToken();
                    title += " ";
                }
                epgObj.setTitle(title.trim());

                epgObj.setStartDate(SerFormatter.formatUnixDate(epgObj.getUnformattedStart()));
                epgObj.setDuration(Integer.toString(Integer.parseInt(epgObj.getUnformattedDuration())/60) +" Min");
                epgObj.setEndDate(SerFormatter.formatUnixDate(Long.parseLong(epgObj.getUnformattedStart()) 
                        + Long.parseLong(epgObj.getUnformattedDuration())));
                epgObj.setSender(sender);
                epgList.add(epgObj);     
            }                        
		}
		return epgList;
	}     
    
	public BOEpgDetails getEpgDetail(BOEpg epg) throws IOException {
		BOEpgDetails epgDetail = new BOEpgDetails();
		BufferedReader input = getConnection("/control/epg?eventid="+epg.getEventId());
		
		String text = new String();
		String line;
		while((line=input.readLine())!=null) {
			text+=line+" \n";
		}
		epgDetail.setText(text);
		return epgDetail;
	}

	private String sendCommand (String message) throws IOException {
        String line;          
        BufferedReader input = getConnection("/control/"+message);          
        while ((line = input.readLine()) != null) {  
        	StringTokenizer st = new StringTokenizer(line);    
        	message = (st.nextToken());
        }         
       return message;  
	}
  
	public String sendMessage(String message) throws IOException {                                         
		Logger.getLogger("SerBoxControlNeutrino").info(ControlMain.getProperty("msg_boxSend"));
		return sendCommand("message?popup="+message); 
	}
	
	public String standbyBox(String modus) throws IOException {
		Logger.getLogger("SerBoxControlNeutrino").info(ControlMain.getProperty("msg_standby"));
		return sendCommand("standby?"+modus);
	}
	
	public String shutdownBox() throws IOException {
		Logger.getLogger("SerBoxControlNeutrino").info(ControlMain.getProperty("msg_shutdown"));         
		return sendCommand("shutdown");
	}

	public BOTimerList readTimer() throws IOException {
		BufferedReader inputNhttpd = getConnection("/control/timer");
		String line;
		while ((line = inputNhttpd.readLine()) != null) {
            int index=0;
			String valueStart, valueStop, valueSenderName = new String();
			BOTimer boTimer = new BOTimer();
			
			String[] result=line.split(" ");
	        
            boTimer.timerNumber=result[0];
            boTimer.eventTypeId=result[1];
            boTimer.eventRepeatId=result[2];
            System.out.println("Event Repeat Type:" + boTimer.eventRepeatId);
            //Abwärtskompatibilität gewährleisten. repeatCount erst in Images seit 12.2004 vorhanden
            try {
                long stop = Long.parseLong(result[6])*1000;
                long now = new GregorianCalendar().getTimeInMillis(); 
                if (stop>now) {
                    boTimer.repeatCount=result[3];
                    boTimer.announceTime=result[4];
                    index=1;
                } else {
                    boTimer.announceTime=result[3];
                }
            } catch (Exception ex) {
                boTimer.announceTime=result[3];
            }
            
            valueStart=result[4+index];
		    valueStop=result[5+index];
		    if (!valueStop.equals("0")) {
                for (int i=6+index; i<result.length; i++) {
                    valueSenderName += result[i];
                    valueSenderName += " ";
                }
		    	boTimer.setSenderName(valueSenderName.trim());
		    }

		    boTimer.unformattedStartTime=SerFormatter.formatUnixDate(valueStart);  
			boTimer.unformattedStopTime=SerFormatter.formatUnixDate(valueStop);
            boTimer.getLocalTimer().setLocal(false);
		    
		    if (boTimer.getEventTypeId().equals("5")) {
		    	timerList.getRecordTimerList().add(boTimer);
		    } else {
		        timerList.getSystemTimerList().add(boTimer);   	
		    }
		}
		setTimerDesctiptionName(timerList.getRecordTimerList());
		return timerList;
	}
	
	public GregorianCalendar getBoxTime() throws IOException {
	    BufferedReader input = getConnection("/control/gettime?rawtime");
	    return SerFormatter.formatUnixDate(input.readLine());
	}
	
	/**
	 * Passenden Titel im Web-Interface-Aufruf finden (dirty!)
	 */
	private void setTimerDesctiptionName(ArrayList timerList) throws IOException {
		BufferedReader inputWebInf = getConnection("/fb/timer.dbox2");
		
		String line;
		String searchString="&startzeit=";
		int indexSearchString, indexIdentifier;
		
		while ((line = inputWebInf.readLine()) != null) {
			if ((indexSearchString = line.indexOf(searchString))>0) {
				for (int i=0; i<timerList.size(); i++) {
					BOTimer timer = (BOTimer)timerList.get(i);
					if (timer.getSenderName() != null && 
					        line.indexOf(timer.getSenderName())>0 &&
					        line.indexOf(timer.getShortStartDate())>0 &&
					        line.indexOf(timer.getShortStartTime())>0) {
						indexIdentifier= line.indexOf(">", indexSearchString);
						timer.description=line.substring(indexIdentifier+1, line.length()-4);
					}
				}
			}
		}	
	} 
	
	public String writeTimer(BOTimer timer) throws IOException {
		StringBuffer buffer = this.fillRequestString(timer);
		BufferedReader input = getConnection(buffer.toString());
		String line;
		while((line=input.readLine())!=null) {
			Logger.getLogger("ControlProgramTab").info(ControlMain.getProperty("msg_sentTimer")+timer.getInfo());
			return line;
		}
		return line;
	}
	
	private StringBuffer fillRequestString(BOTimer timer) {
		String modifiedId = timer.getModifiedId();
		String alarm = Long.toString(timer.getUnformattedStartTime().getTimeInMillis()/1000);
		String stop = Long.toString(timer.getUnformattedStopTime().getTimeInMillis()/1000);
		StringBuffer buffer = new StringBuffer();
		if (modifiedId.equals("remove")) {
			buffer.append("/fb/timer.dbox2?action="+modifiedId);
			buffer.append("&id="+timer.getTimerNumber());
		}
        else if (modifiedId.equals("modify")) {
			buffer.append("/fb/timer.dbox2?action="+modifiedId);
			//buffer.append("/control/timer?action="+modifiedId);
			buffer.append("&id="+timer.getTimerNumber());
			
			buffer.append("&ad="+timer.getUnformattedStartTime().get(Calendar.DAY_OF_MONTH));
			buffer.append("&amo="+(timer.getUnformattedStartTime().get(Calendar.MONTH)+1));
			buffer.append("&ay="+timer.getUnformattedStartTime().get(Calendar.YEAR));
			buffer.append("&ah="+timer.getUnformattedStartTime().get(Calendar.HOUR_OF_DAY));
			buffer.append("&ami="+timer.getUnformattedStartTime().get(Calendar.MINUTE));
			
			buffer.append("&sd="+timer.getUnformattedStopTime().get(Calendar.DAY_OF_MONTH));
			buffer.append("&smo="+(timer.getUnformattedStopTime().get(Calendar.MONTH)+1));
			buffer.append("&sy="+timer.getUnformattedStopTime().get(Calendar.YEAR));
			buffer.append("&sh="+timer.getUnformattedStopTime().get(Calendar.HOUR_OF_DAY));
			buffer.append("&smi="+timer.getUnformattedStopTime().get(Calendar.MINUTE));
			
			buffer.append("&announce="+timer.getAnnounceTime());
			buffer.append("&type="+timer.getEventTypeId());
			buffer.append("&rep="+timer.getEventRepeatId());
            buffer.append("&repcount=999");
			buffer.append("&channel_id="+timer.getChannelId());
		} 
        else if (modifiedId.equals("new")) {
            this.newTimerAdded=true;
			buffer.append("/control/timer?action="+modifiedId);
			buffer.append("&alarm="+alarm);
			buffer.append("&stop="+stop);
			buffer.append("&announce="+timer.getAnnounceTime());
			buffer.append("&type="+timer.getEventTypeId());
			buffer.append("&rep="+timer.getEventRepeatId());
            buffer.append("&repcount=999");
			buffer.append("&channel_id="+timer.getChannelId());
		}
		return buffer;
	}
    
     public String getSptsStatus() {
            try {
                BufferedReader input = getConnection("/control/system?getAViAExtPlayBack");
                
                String status = input.readLine(); 
                if (status.equals("0")) {
                    return "pes";
                } else if (status.equals("1")) {
                    return "spts";
                }
                return new String();
            } catch (IOException e) {
                return new String();
            }
        }
    public boolean setSptsStatus(String status) {
        try {
            BufferedReader input = getConnection("/control/system?setAViAExtPlayBack="+status);
            return (input.readLine().equals("ok"));
        } catch (IOException e) {
            return false;
        }  
    }
    
	public String [] getRepeatOptions() throws IOException {
		return new String [] {ControlMain.getProperty("once"), ControlMain.getProperty("dayly"), ControlMain.getProperty("weekly"), 
			ControlMain.getProperty("2-weekly"), ControlMain.getProperty("4-weekly"), ControlMain.getProperty("weekdays")};
	}
	
	public String [][] getTimerType() throws IOException {
		return new String [][] { {"Shutdown","1"},{"ZapTo","3"}, {"Standby","4"}, {"Remind","6"},{"SleepTimer","7"},{"User","1000"}};
	}
	public GuiRecordTimerTableModel getRecordTimerTabelModel(ControlTimerTab ctrl) {
		return new GuiNeutrinoRecordTimerTableModel(ctrl);
	}
	public GuiSystemTimerTableModel getSystemTimerTabelModel(ControlTimerTab ctrl) {
		return new GuiNeutrinoSystemTimerTableModel(ctrl);
	}
	public String getIcon() {
		return ("ico/neutrino-logo.jpg");	
	}
}
