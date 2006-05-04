package service;
/*
SerNewsHandler.java by Geist Alexander 

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
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import java.util.logging.Logger;


public class SerNewsHandler extends Thread {
	private JTextPane nachrichten;
	
	public SerNewsHandler(JTextPane nachrichten){
		this.nachrichten = nachrichten;
	}
	
	public void run() {
		try {        
	        String htmlText = SerWebsiteContentLoader.getWebsiteContent(
					"http://www.jackthegrabber.de", 80,
					"/jtjg/news.htm");
		        
			int pos = htmlText.toLowerCase().indexOf("<html>");
			if (pos != -1) {
				htmlText = htmlText.substring(pos);
			} else {
				htmlText = "<html>" + htmlText + "</html>";
			}
			StringBuffer buffer = new StringBuffer(htmlText);
			int index;
			while ((index = buffer.indexOf(". ")) != -1) {
				buffer.replace(index, index + 1, ".<br>");
			}
			htmlText = buffer.toString();
			final String htmlContent = htmlText;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					nachrichten.setContentType("text/html");
					nachrichten.setText(htmlContent);
				}
			});
		} catch (Exception e) {
			Logger.getLogger("SerNewsHandler").warning(e.getMessage());
		}
	}

}
