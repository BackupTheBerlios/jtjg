package model;

/*
 * BOMovieGuideContainer.java by Henneberger Ralph
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 */
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import service.SerFormatter;
import service.XML.SerXMLHandling;
import control.ControlMain;

public class BOMovieGuideContainer {
	Hashtable titelList = new Hashtable();

	ArrayList returnList = new ArrayList();

	ArrayList genreList = new ArrayList();

	ArrayList datumList = new ArrayList();

	ArrayList senderList = new ArrayList();

	ArrayList announceList = new ArrayList();

	private static final String DATE_FULL = "EEEE, dd. MMMM yyyy";

	private static final String DATE_FULL_TIME = "EEEE, dd. MMMM yyyy,HH:mm";

	private static final String TIME = "HH:mm";

	private GregorianCalendar check = new GregorianCalendar();

	public int importXML(File file, ArrayList aboList) {
		Document doc = null;
		try {
			doc = SerXMLHandling.readDocument(file);
		} catch (DocumentException DoEx) {
			DoEx.printStackTrace();
		} catch (MalformedURLException MEx) {
			Logger.getLogger("ControlMovieGuideTab").warning(
					file.getName() + " not found");
		}
		// long x = System.currentTimeMillis();

		Element root = doc.getRootElement();
		// clear();
		try {
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			for (Iterator i = root.elementIterator("entry"); i.hasNext();) {
				Element entry = (Element) i.next();
				String datum = entry.element("datum").getStringValue();
				if (!SerFormatter.compareDates(datum, today))
					continue;
				String sender = entry.element("sender").getStringValue();
				if (!(aboList.contains(sender)) && !aboList.isEmpty())
					continue;
				setDatumList(datum);
				String titel = entry.element("titel").getStringValue();
				String dauer = entry.element("dauer").getStringValue();
				setSenderList(sender);
				BOMovieGuide bomovieguide = (BOMovieGuide) titelList.get(titel);
				if (bomovieguide == null) {
					String start = entry.element("start").getStringValue();
					bomovieguide = new BOMovieGuide(sender, SerFormatter
							.convString2GreCal(datum, DATE_FULL, true),
							SerFormatter.convString2GreCal(datum + "," + start,
									DATE_FULL_TIME, true), SerFormatter
									.convString2GreCal(datum
											+ ","
											+ SerFormatter.getCorrectEndTime(
													start, dauer),
											DATE_FULL_TIME, true), titel, entry
									.element("episode").getStringValue(), entry
									.element("genre").getStringValue(), dauer,
							entry.element("land").getStringValue(), entry
									.element("jahr").getStringValue(), entry
									.element("regie").getStringValue(), entry
									.element("bild").getStringValue(), entry
									.element("ton").getStringValue(), entry
									.element("darsteller").getStringValue(),
							entry.element("inhalt").getStringValue());
					setGenreList(bomovieguide.getGenre());
					titelList.put(titel, bomovieguide);
				} else {
					String start = entry.element("start").getStringValue();
					String ende = SerFormatter.getCorrectEndTime(start, dauer);
					bomovieguide = (BOMovieGuide) titelList.get(titel);
					bomovieguide.setDatum(SerFormatter.convString2GreCal(datum,
							DATE_FULL, true));
					bomovieguide.setStart(SerFormatter.convString2GreCal(datum
							+ "," + start, DATE_FULL_TIME, true));
					bomovieguide.setDauer(dauer);
					bomovieguide.setSender(sender);
					bomovieguide
							.setBild(entry.element("bild").getStringValue());
					bomovieguide.setTon(entry.element("ton").getStringValue());
					bomovieguide.setEnde(SerFormatter.convString2GreCal(datum
							+ "," + ende, DATE_FULL_TIME, true));
				}
			}
		} catch (Exception e) {
			Logger.getLogger("ControlMovieGuideTab").warning(
					ControlMain.getProperty("error_read_mg"));
		}

		// System.out.println("Dauer: " + (System.currentTimeMillis() - x));

		Collections.sort(getSenderList());// alphabetisch geordnet
		Collections.sort(getGenreList());// alphabetisch geordnet
		// announceList =
		// createAnnounceList(ControlMain.getSettingsMovieGuide().getMgDontForgetListe());
		return titelList.size();
	}

	public ArrayList getAnnounceList() {
		return announceList;
	}

	private ArrayList createAnnounceList(ArrayList infoList) {
		ArrayList searchList = new ArrayList();
		ArrayList retVal = new ArrayList();
		int a = 0;
		while (a <= infoList.size() - 1) {
			searchList = (search(infoList.get(a++), 3));
			int b = 0;
			while (b <= searchList.size() - 1) {
				retVal.add(searchList.get(b++));
			}
		}
		return retVal;
	}

	/**
	 * @return Gibt die ArrayList für die DatumComboBox zurück
	 */
	public ArrayList getDatumList() {
		return datumList;
	}

	/**
	 * @param value
	 *            Setzt die ArrayList für die DatumComboBox, es wird geprüft ob
	 *            das Datum schon in der ArrayList ist, damit keine doppelten
	 *            Eintrage rein kommen
	 */
	private void setDatumList(String value) {
		if (!datumList.contains(value)) {
			datumList.add(value);
		}
	}

	/**
	 * @return Gibt die ArrayList für die GenreComboBox zurück
	 */
	public ArrayList getGenreList() {
		return genreList;
	}

	/**
	 * @param value
	 *            Setzt die ArrayList für die GenreComboBox, es wird geprüft ob
	 *            das Genre schon in der ArrayList ist, damit keine doppelten
	 *            Eintrage rein kommen
	 */
	private void setGenreList(String value) {
		if (!genreList.contains(value) && value.length() > 0) {
			genreList.add(value);
		}
	}

	/**
	 * @return Gibt die ArrayList für die SenderComboBox zurück
	 */
	public ArrayList getSenderList() {
		return senderList;
	}

	/**
	 * @param value
	 *            Setzt die ArrayList für die SenderComboBox, es wird geprüft ob
	 *            das Sender schon in der ArrayList ist, damit keine doppelten
	 *            Eintrage rein kommen
	 */
	private void setSenderList(String value) {
		if (!senderList.contains(value) && value.length() > 0) {
			senderList.add(value);
		}
	}

	private void clear() {
		titelList.clear();
		genreList.clear();
		datumList.clear();
		senderList.clear();
	}

	public boolean isValueInArray(ArrayList value, String search) {
		boolean retVal = false;
		for (Iterator i = value.iterator(); i.hasNext();) {
			if (i.next().toString().toLowerCase().indexOf(search) >= 0) {
				retVal = true;
			}
		}
		return retVal;
	}

	public boolean isTimeInArray(ArrayList value, GregorianCalendar search) {
		boolean retVal = false;
		for (Iterator i = value.iterator(); i.hasNext();) {
			GregorianCalendar t = (GregorianCalendar) i.next();
			if (((t.getTimeInMillis() >= search.getTimeInMillis()) && (t
					.getTimeInMillis() < check.getTimeInMillis()))) {
				retVal = true;
			}
		}
		return retVal;
	}

	public boolean getIfStringInObject(BOMovieGuide bomovieguide, String search) {
		boolean value = false;
		search = search.toLowerCase();
		if (bomovieguide.getGenre().toLowerCase().indexOf(search) >= 0) {
			value = true;
		} else if (bomovieguide.getDarsteller().toLowerCase().indexOf(search) >= 0) {
			value = true;
		} else if (bomovieguide.getEpisode().toLowerCase().indexOf(search) >= 0) {
			value = true;
		} else if (bomovieguide.getInhalt().toLowerCase().indexOf(search) >= 0) {
			value = true;
		} else if (bomovieguide.getLand().toLowerCase().indexOf(search) >= 0) {
			value = true;
		} else if (bomovieguide.getJahr().toLowerCase().indexOf(search) >= 0) {
			value = true;
		} else if (isValueInArray(bomovieguide.getBild(), search.toLowerCase())) {
			value = true;
		} else if (isValueInArray(bomovieguide.getTon(), search.toLowerCase())) {
			value = true;
		} else if (bomovieguide.getRegie().toLowerCase().indexOf(search) >= 0) {
			value = true;
		} else if (bomovieguide.getTitel().toLowerCase().indexOf(search) >= 0) {
			value = true;
		}
		return value;
	}

	public ArrayList search(Object searchValue, int value) {
		returnList.clear();
		String search = new String();
		if (value < 15) {
			search = (String) searchValue;
		}
		GregorianCalendar searchGC = new GregorianCalendar();
		GregorianCalendar searchGCTime = new GregorianCalendar();
		ArrayList searchAL;
		if (value == 1) {
			searchGC = SerFormatter.convString2GreCal(search, DATE_FULL);
		}
		if (value == 14) {
			searchGC = SerFormatter.convString2GreCal(SerFormatter
					.getFormatGreCal(), DATE_FULL);
			searchGCTime = SerFormatter.convTime2GreCal(search);
			check.set(Calendar.HOUR_OF_DAY, 23);
			check.set(Calendar.MINUTE, 59);
		}
		Iterator i = titelList.entrySet().iterator();
		while (i.hasNext()) {
			BOMovieGuide bomovieguide = (BOMovieGuide) ((Map.Entry) i.next())
					.getValue();
			switch (value) {
			case 1:
				if (bomovieguide.getDatum().contains(searchGC)) {
					returnList.add(bomovieguide);
				}
				break;
			case 2:
				if (getIfStringInObject(bomovieguide, search)) {
					returnList.add(bomovieguide);
				}
				break;
			case 3:
				if (bomovieguide.getTitel().toLowerCase().indexOf(
						search.toLowerCase()) != -1) {
					returnList.add(bomovieguide);
				}
				break;
			case 4:
				if (bomovieguide.getDarsteller().toLowerCase().indexOf(
						search.toLowerCase()) != -1) {
					returnList.add(bomovieguide);
				}
				break;
			case 5:
				if (bomovieguide.getEpisode().toLowerCase().indexOf(
						search.toLowerCase()) != -1) {
					returnList.add(bomovieguide);
				}
				break;
			case 6:
				if (isValueInArray(bomovieguide.getBild(), search.toLowerCase())) {
					returnList.add(bomovieguide);
				}
				break;
			case 7:
				if (isValueInArray(bomovieguide.getTon(), search.toLowerCase())) {
					returnList.add(bomovieguide);
				}
				break;
			case 8:
				if (bomovieguide.getLand().toLowerCase().indexOf(
						search.toLowerCase()) != -1) {
					returnList.add(bomovieguide);
				}
				break;
			case 9:
				if (bomovieguide.getJahr().toLowerCase().indexOf(
						search.toLowerCase()) != -1) {
					returnList.add(bomovieguide);
				}
				break;
			case 10:
				if (bomovieguide.getRegie().toLowerCase().indexOf(
						search.toLowerCase()) != -1) {
					returnList.add(bomovieguide);
				}
				break;
			case 11:
				if (bomovieguide.getGenre().toLowerCase().indexOf(
						search.toLowerCase()) != -1) {
					returnList.add(bomovieguide);
				}
				break;
			case 12:
				if (isValueInArray(bomovieguide.getSender(), search
						.toLowerCase())) {
					returnList.add(bomovieguide);
				}
				break;
			case 13:
				returnList.add(bomovieguide);
				break;
			case 14:
				if (isTimeInArray(bomovieguide.getStart(), searchGCTime)) {
					returnList.add(bomovieguide);
				}
				break;
			}
		}
		return returnList;
	}
}