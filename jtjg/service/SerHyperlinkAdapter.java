package service;

import java.awt.Component;
import java.awt.Cursor;
import java.io.IOException;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import control.ControlMain;


public class SerHyperlinkAdapter implements HyperlinkListener{
	private Component parent;
	
	public SerHyperlinkAdapter(Component parent){
		this.parent = parent;
	}
	
	public void hyperlinkUpdate(HyperlinkEvent e) {
		HyperlinkEvent.EventType type = e.getEventType();
		if (type == HyperlinkEvent.EventType.ENTERED) {
			parent.setCursor(Cursor
					.getPredefinedCursor(Cursor.HAND_CURSOR));

		} else if (type == HyperlinkEvent.EventType.EXITED) {
			parent.setCursor(Cursor.getDefaultCursor());
		}
		else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			if (e.getURL() != null) {
				String url = e.getURL().toString();
				if (url.length() != 0) {
				    try {
						BrowserLauncher.openURL(url);
					} catch (IOException ex) {
						SerAlertDialog.alert(ControlMain.getProperty("msg_browserError"), parent);
					}
				}
			}
		}
	}	
}
