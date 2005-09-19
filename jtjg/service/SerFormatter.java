package service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.util.regex.*;
import javax.swing.text.*;
import java.awt.Color;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
/**
 * @author ralix
 */
public class SerFormatter {

	private static final String DATE_FULL = "EEEE, dd. MMMM yyyy";
	private static final String DATE_SHORT_TIME = "dd.MM./HH:mm";
	private static final String TIME = "HH:mm";
	
	private static HashMap calCache = new HashMap();

	/** entferne alle unerlaubten Zeichen
	 *  und ersetzt diese durch "_"
	 * @param input
	 * @return
	 */
	public static String removeInvalidChars(String value){
		String search ="\\p{Punct}"; // alle SonderZeichen
        Pattern find = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
        Matcher m = find.matcher(value);
        while(m.find()) {
            value = value.replaceAll(m.group(0),"_");
        }
        return value;    
	}
	
	/** entferne alle unerlaubten Zeichen
	 * 
	 * @param input
	 * @return
	 */
	public static String removeInvalidCharacters(String input) {

		StringBuffer umlBuf = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			switch (c) {
				case 'ä' : {
					umlBuf.append("ae");
					break;
				}
				case 'ö' : {
					umlBuf.append("oe");
					break;
				}
				case 'ü' : {
					umlBuf.append("ue");
					break;
				}
				case 'ß' : {
					umlBuf.append("ss");
					break;
				}
				case 'Ü' : {
					umlBuf.append("Ue");
					break;
				}
				case 'Ö' : {
					umlBuf.append("Oe");
					break;
				}
				case 'Ä' : {
					umlBuf.append("Ae");
					break;
				}
				case '?':
				case '\\' :
				case ':' :
				case '/' : {
					break;
				}
				default : {
					umlBuf.append(c);
				}
			}
		}
		String s = umlBuf.toString();
		s = s.replaceAll("'", "");
		s = s.replaceAll("\"","");
		return s;
	}
	/**
	 * @param timeInMillis
	 * @return short-Time String "HH:mm"
	 */
	public static String getShortTime(long timeInMillis) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(new Date(timeInMillis));
	}

	private static String out(int i) {
		return (i >= 10) ? Integer.toString(i) : "0" + i;
	}

	/**
	 * @param unix-date
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar formatUnixDate(String date) {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
		cal.setTimeInMillis(Long.parseLong(date) * 1000);
		return cal;
	}

	/**
	 * @param unix-date
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar formatUnixDate(long date) {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
		cal.setTimeInMillis(date * 1000);
		return cal;
	}

	/**
	 * @param timeInMillis
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar formatTimeInMillisToCal(long timeInMillis) {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
		cal.setTimeInMillis(timeInMillis);
		return cal;
	}

	/**
	 * @param sourcestring
	 * @param stringToReplace
	 * @param newString
	 * @return formattedString
	 * replaces in sourceString the stringToReplace with newString
	 */
	public static String replace(String sourcestring, String stringToReplace,String newString) {
		int i = -1;
		while ((i = sourcestring.indexOf(stringToReplace, i + 1)) != -1)
			sourcestring = sourcestring.substring(0, i) + newString + sourcestring.substring(i + stringToReplace.length());
		return sourcestring;
	}

	public static Date setCorrectYear(Date datum) {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
		int i = cal.get(Calendar.YEAR);
		cal.setTimeInMillis(datum.getTime());		
		GregorianCalendar caltoday = new GregorianCalendar(TimeZone.getTimeZone("ECT"));		
		if (cal.get(Calendar.MONTH) <  caltoday.get(Calendar.MONTH)){
			i = i + 1;
			cal.set(Calendar.YEAR, i);
		}else{					
			cal.set(Calendar.YEAR, i);
		}
		return cal.getTime();
	}
/*
		public static Date setCorrectYear(Date datum) {
			GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("ECT"));			
			int i = cal.get(GregorianCalendar.YEAR);
			cal.setTimeInMillis(datum.getTime());
			cal.set(GregorianCalendar.YEAR, i);	
			return cal.getTime();
		}
			*/
	public static String getCorrectEndTime(String start, String ende) {
	    GregorianCalendar cal = SerFormatter.convString2GreCal(start, TIME,true);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
	    cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+ Integer.parseInt(ende.substring(0, 2)));
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+ Integer.parseInt(ende.substring(3, 5)));
		String val = getFormatGreCal(cal, TIME);
	    cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		return val;
	}

	public static String getCorrectDate(String datum) {	   
		return getFormatGreCal(convString2GreCal(datum,DATE_SHORT_TIME),DATE_FULL);
	}

	public static boolean isCorrectDate(String datum) {
		boolean value = true;
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_SHORT_TIME);
		try {
			datum = SerFormatter.setCorrectYear(formatter.parse(datum)).toString();
		} catch (ParseException pex) {
			value = false;
		}
		return value;
	}

	public static String stripOff(String input) {
		String delim = new String("<>'*?|/\":");
		StringTokenizer t = new StringTokenizer(input, delim);
		StringBuffer b = new StringBuffer(input.length());
		while (t.hasMoreTokens())b.append(t.nextToken());
		return b.toString();
	}
		
	public static String getAktuellDateString(int monat,String format) {
		GregorianCalendar calmg = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
		calmg.set(Calendar.MONTH, (calmg.get(Calendar.MONTH)+monat));
		return getFormatGreCal(calmg,format);			
	}

	public static String getFormatGreCal() {
		GregorianCalendar caltoday = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
		return getFormatGreCal(caltoday,DATE_FULL);
	}
	
	public static String getFormatGreCal(GregorianCalendar value, String format){
		SimpleDateFormat fmt = new SimpleDateFormat(format);		       
        return fmt.format(value.getTime());                           
	}
	
    public static GregorianCalendar getDateFromString (String date, String format) {
    	return convString2GreCal(date, format);
    }

    /** liefert ein Calendar Objekt für das gegeben Datum als String. 
     * 
     * @param date
     * @param format
     * @param useCache wenn true wird der Cache benützt! Achtung: Das Calendar Objekt darf in diesem Fall
     * nicht verändert werden!
     * @return
     */
    public static GregorianCalendar convString2GreCal (String date, String format,boolean useCache) {	
        
    	GregorianCalendar cal = null;
    	if (useCache)
    	{
	    	cal = (GregorianCalendar) calCache.get(date + "|" + format);
	    	if (cal == null)
	    	{
		    	SimpleDateFormat formatter  = new SimpleDateFormat(format);
		        cal = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
		        try {
		            cal.setTimeInMillis(SerFormatter.setCorrectYear(formatter.parse(date)).getTime());
		            calCache.put(date + "|" + format,cal);
		        }catch(ParseException pex){
                    Logger.getLogger("SerFormatter").error(pex.getMessage());
		        }
	    	}
    	}
    	else
    	{
    		SimpleDateFormat formatter  = new SimpleDateFormat(format);
	        cal = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
	        try {
	            cal.setTimeInMillis(SerFormatter.setCorrectYear(formatter.parse(date)).getTime());
	        }catch(ParseException pex){
                Logger.getLogger("SerFormatter").error(pex.getMessage());
	        }
    	}
        return cal;
    }

    public static GregorianCalendar convString2GreCal (String date, String format) {	
    	return convString2GreCal(date,format,false);
    }
    
    
    public static GregorianCalendar getGC(GregorianCalendar gc, int value){
        GregorianCalendar newCal = (GregorianCalendar)gc.clone();
        newCal.set(Calendar.MINUTE, gc.get(Calendar.MINUTE)+value);
    	return newCal;
    }
    public static GregorianCalendar convTime2GreCal (String time) {	        
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("ECT"));
        cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time.substring(0,time.indexOf(":"))));
        cal.set(Calendar.MINUTE,Integer.parseInt(time.substring(time.indexOf(":")+1)));        
        return cal;
    }
    
    public static boolean compareDates(String date1, String date2){    	
    	if( date2.equals("today")){
    		date2=getFormatGreCal();
    	}    	  
    	return convString2GreCal(date1, DATE_FULL,true).getTimeInMillis() >= convString2GreCal(date2, DATE_FULL,true).getTimeInMillis();
    }

    public static boolean compareDates(String date1, Calendar date2){  
    	return convString2GreCal(date1, DATE_FULL,true).getTimeInMillis() >= date2.getTimeInMillis();
    }
    
    /**
     * @param cal1
     * @param cal2
     * @return the value 0 if the time represented by the argument
     *  is equal to the time represented by first Calendar; a value less
     *  than 0 if the time of first Calendar is before the time represented
     *  by the argument; and a value greater than 0 if the time of first
     *  Calendar is after the time represented by the argument.
     */
    public static int compareDates (Calendar cal1, Calendar cal2) {
        long date1 = cal1.getTimeInMillis();
        long date2 = cal2.getTimeInMillis();
        if (date1==date2) {
            return 0;
        }
        if (date1<date2) {
            return -1;
        } 
        return 1;
    }
    public static void insertPicture(JTextComponent textComp,String path) {
        try {
         StyledDocument doc = (StyledDocument)textComp.getDocument();
         Style style = doc.addStyle("StyleName", null);
         StyleConstants.setIcon(style, new ImageIcon(path));
         doc.insertString(doc.getLength(), " ", style);
        } catch (BadLocationException e) {
            Logger.getLogger("SerFormatter").error(e.getMessage());
        }
    }
    
    public static void underScore(JTextComponent textComp, String pattern, boolean value, int pos) {                      	
    	StyledDocument doc = (StyledDocument)textComp.getDocument();        
        Style style = doc.addStyle("Underline", null);
        StyleConstants.setUnderline(style, value);
        try {                                    
            	doc.insertString(pos, pattern, style);            	
            	StyleConstants.setUnderline(style, false);
        } catch (BadLocationException e) {
            Logger.getLogger("SerFormatter").error(e.getMessage());
        }    
    }
    
    public static void highlight(JTextComponent textComp, String pattern) {
    	Pattern find = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);              
        Matcher m = find.matcher(textComp.getText());      
    	removeHighlights(textComp);    
    	 try {
            Highlighter hilite = textComp.getHighlighter();            
            while(m.find()) {                       		
                hilite.addHighlight(m.start(0), m.start(0)+pattern.length(), myHighlightPainter);        
    	    }              
        } catch (BadLocationException e) {
            Logger.getLogger("SerFormatter").error(e.getMessage());
        }
    }
    public static void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();    
        for (int i=0; i<hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }
        
	static Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(new Color(255,204,51));
    
    static class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }   
}