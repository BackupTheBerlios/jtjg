package service;

import java.io.*;
import java.text.*;
import java.util.*;

import java.util.logging.Logger;

import model.*;
import service.recordinfo.*;
import control.*;

/*
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 */

public class SerHelper {
    
    public static Object serialClone(Object o) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            
            os.writeObject(o);
            os.flush();

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream is = new ObjectInputStream(in);
            Object ret = is.readObject();
            is.close();
            os.close();
            return ret;
        } catch (IOException e) {
            Logger.getLogger("SerHelper").warning(e.getMessage());
        } catch (ClassNotFoundException e) {
            Logger.getLogger("SerHelper").warning(e.getMessage());
        }
        return null;
    }

	/**
	 * überprüft ob es sich bei der Datei um eine Audio Datei handelt
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isAudio(String file) {
		String[] audioEndings = new String[]{"mp2", "ac3", "apes"};
		file = file.toLowerCase();
		for (int i = 0; i < audioEndings.length; i++) {
			if (file.endsWith(audioEndings[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * überprüft ob es sich um eine Videodatei handelt (auch MPG)
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isVideo(String file) {
		String[] videoEndings = new String[]{"mpv", "mpg", "ts", "vpes", "vob", "mpeg","m2v"};
		file = file.toLowerCase();
		for (int i = 0; i < videoEndings.length; i++) {
			if (file.endsWith(videoEndings[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param files
	 * @return the first Video-File in the Array
	 */
	public static File getVideoFile(File[] files, String reqName) {
		for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
			if (name.indexOf(reqName)>=0 && isVideo(name)) {
				return files[i];
			}
		}
		return null;
	}

	/**
	 * @param files,
	 *            fileList
	 * @return the fileList filled with Audio-Files from files-Array
	 */
	public static ArrayList getAudioFiles(File[] files, String reqName) {
        ArrayList audiFiles = new ArrayList();
		for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
			if (name.indexOf(reqName)>=0 && isAudio(name)) {
                audiFiles.add(files[i]);
			}
		}
		return audiFiles;
	}

	/**
	 * @param file
	 * @return the ending of the given file
	 */
	public static String getEnding(File file) {
		int end = file.getName().lastIndexOf(".");
		if (end > -1) {
			return file.getName().substring(end + 1);
		}
		return "";
	}

	/**
	 * berechnet die angezeigte Größe einer Datei in der angegebenen Einheit
	 * 
	 * @param s
	 * @param type
	 *            MB for MByte and KB for Kilobyte
	 * @param div
	 *            divisor (e.g. 1024)
	 * @return
	 */
	public static String calcSize(double s, String type, int div) {

		if (type.equals("MB")) {
			s = s / div; // kb
			s = s / div; // MB
			return NumberFormat.getNumberInstance().format(s) + " " + type;
		} else if (type.equals("KB")) {
			s = s / div; // kb
			return NumberFormat.getNumberInstance().format(s) + " " + type;
		} else if (type.equals("MBit")) {
			s *= 8;
			s = s / div; // kb
			s = s / div; // MB
			return NumberFormat.getNumberInstance().format(s) + " " + type;
		}
		return NumberFormat.getNumberInstance().format(s) + " " + type;
	}

	/**
	 * berechnet die angezeigte Größe einer Datei in der angegebenen Einheit
	 * 
	 * @param s
	 * @param type
	 *            MB for MByte and KB for Kilobyte
	 * @return
	 */
	public static String calcSize(double s, String type) {

		return calcSize(s, type, 1024);
	}

	/**
	 * create a string which contains the info for a given file used by recordinfo
	 * 
	 * @param info
	 * @return
	 */
	public static String createFileInfo(File info) {

		String file = info.getAbsolutePath();
		StringBuffer fileInfo = new StringBuffer();

		ScanFile scan = new ScanFile();
		if (info.exists()) {
			fileInfo.append(ControlMain.getProperty("label_size") + ":\t" + (info.length() / 1048576) + " MB (" + info.length() + " bytes)"
					+ "\n");
			String type = ControlMain.getProperty("label_type") + ":\t" + scan.Type(file) + "\n"; // must be first when scanning
			fileInfo.append(ControlMain.getProperty("label_date") + ":\t" + scan.Date(file) + "\n");
			fileInfo.append(type);
			fileInfo.append("Video:\t" + scan.getVideo() + "\n");
			fileInfo.append("Audio:\t" + scan.getAudio() + "\n");
			fileInfo.append("Text:\t" + scan.getText() + "\n");
			fileInfo.append(ControlMain.getProperty("label_playtime") + ":\t" + scan.getPlaytime() + "\n");

		}
		return fileInfo.toString();

	}

	/**
	 * create the filename for record
	 * 
	 * @param recordArgs
	 * @return
	 */
	public static String createFileName(BORecordArgs args, String pattern) {
		if (pattern == null || pattern.length() == 0) {

			// default pattern
		/*	SimpleDateFormat f = new SimpleDateFormat("yy-MM-dd_HH-mm");
			String date = f.format(new Date());

			if (args.getEpgTitle() != null) {
				Object[] obj = {date, args.getSenderName(), args.getEpgTitle()};
				MessageFormat form = new MessageFormat("{0}_{1}_{2}");
				return form.format(obj);
			} 
			return date + "_" + args.getSenderName();
			*/
			return "";
		}
		
		
		String[] availablePattern = {"%CHANNEL%", "%TIME%", "%NAME%","%SERIE%", "%DATE%", "%DATE "};
		for (int i = 0; i < availablePattern.length; i++) {
			int index = pattern.indexOf(availablePattern[i]);
			if (index > -1) {
				// pattern found, replace
				pattern = replacePattern(availablePattern[i], pattern, args);
			}
		}
		return pattern;
	}

	/**
	 * 
	 * @param patternToReplace
	 * @param fileName
	 * @param args
	 * @return
	 */
	private static String replacePattern(String patternToReplace, String fileName, BORecordArgs args) {
		String replaceWith = null;
		if (patternToReplace.equals("%CHANNEL%")) {
			replaceWith = args.getSenderName();
		} else if (patternToReplace.equals("%TIME%")) {
			Date d = new Date();

			SimpleDateFormat f = new SimpleDateFormat("HH:mm");
			replaceWith = SerFormatter.replace(f.format(d),":","_");
		} else if (patternToReplace.equals("%NAME%")) {
			replaceWith = args.getEpgTitle();
			if (replaceWith == null || replaceWith.length() == 0) {
				replaceWith = "unavailable";
			}
		} else if (patternToReplace.equals("%SERIE%")) {
			replaceWith = args.getEpgInfo1();
			if (replaceWith.length() > 0)
			{
				replaceWith = SerFormatter.replace(replaceWith,args.getEpgTitle(),"");
				if (replaceWith.indexOf("\n") > -1)
				{
					replaceWith = replaceWith.substring(2,replaceWith.indexOf("\n",2));
				}
			}
		} else if (patternToReplace.equals("%DATE%")) {
			Date d = new Date();
			String date = DateFormat.getDateInstance().format(d);
			date = SerFormatter.replace(date,".", "-");
			replaceWith = date;
		} else if (patternToReplace.startsWith("%DATE")) {

			int start = fileName.indexOf("%DATE ");
			int end = fileName.indexOf("%", start + 1);
			patternToReplace = fileName.substring(start, end + 1);
			String datePattern = patternToReplace.replaceAll("%DATE ", "");
			datePattern = SerFormatter.replace(datePattern,"%", "");
			datePattern = SerFormatter.replace(datePattern,"YY", "yy");
			datePattern = SerFormatter.replace(datePattern,"DD", "dd");
			Date d = new Date();
			SimpleDateFormat format = new SimpleDateFormat(datePattern);
			replaceWith = format.format(d);
		}

		if (replaceWith == null) {
			replaceWith = "";
		}

		fileName = fileName.replaceAll(patternToReplace, replaceWith);

		return fileName.trim();
	}

	/*
	 * return true,
     * 1. when the startTime of the second timer is
	 * between start/stop-time of the first timer.
     * 2. when the stopTime of the second timer is
     * between start/stop-time of the first timer.
     * 3. when the startTime of the second timer is
     * before & the stopTime is after the first timer
	 */
	public static boolean compareTimerTime(BOTimer timer1, BOTimer timer2) {
		long timer1Start = timer1.getUnformattedStartTime().getTimeInMillis();
		long timer2Start = timer2.getUnformattedStartTime().getTimeInMillis();
        long timer1Stop = timer1.getUnformattedStopTime().getTimeInMillis();
        long timer2Stop = timer2.getUnformattedStopTime().getTimeInMillis();
		if (timer2Start>=timer1Start && timer2Start<=timer1Stop) {
			return true;
		}
        if (timer2Stop>=timer1Start && timer2Stop<=timer1Stop) {
            return true;
        }
        if (timer2Start<=timer1Start && timer2Stop>=timer1Stop) {
            return true;
        }
		return false;
	}
}