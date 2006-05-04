package presentation.about;
/*
GuiTabAbout.java by Geist Alexander 

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

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import service.SerHyperlinkAdapter;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.about.ControlAboutTab;

public class GuiTabAbout extends JPanel {

	private ControlAboutTab control;
	private JTextArea taVersion;
	private JTextArea taAuthors;
	private JTextArea taLicense;
	private JTextArea taOther;
	private JScrollPane scrollPaneLicense;
	private JScrollPane scrollPaneVersion;
	private JScrollPane scrollPaneAuthors;
	private JScrollPane scrollPaneOther;
	private ImageIcon imageLogo;
	private JPanel jGrabberInfo;
	private JLabel labelOther, labelJGrabber;
	private JTextPane linkHomePage, linkApache, linkProjectX, linkUdrec, linkDom4j, linkForms, linkSystray, labelJGrabber2;
	SerHyperlinkAdapter hyperlinkAdapter = new SerHyperlinkAdapter(this);
	Color background = (Color)UIManager.get("Panel.background");

	public GuiTabAbout(ControlAboutTab ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}

	private  void initialize() {
		FormLayout layout = new FormLayout(
						  "200:grow, 8dlu, pref:grow",  		// columns 
						  "pref, 4dlu, f:50:grow, 4dlu, pref, 4dlu, f:320:grow, pref"); 			// rows
				PanelBuilder builder = new PanelBuilder(layout,this);
				builder.setDefaultDialogBorder();
				CellConstraints cc = new CellConstraints();
		
				builder.addSeparator(ControlMain.getProperty("label_version"),					cc.xy	(1, 1));
				builder.add(this.getScrollPaneVersion(),															cc.xy	(1, 3));
				builder.addSeparator(ControlMain.getProperty("label_authors"),					cc.xy	(1, 5));
				builder.add(this.getScrollPaneAuthors(),															cc.xywh	(1, 7, 1, 2));
				builder.add(this.getPanelJGrabberInfo(),															cc.xywh	(3, 1, 1, 6));	
				builder.add(new JLabel(this.getImageLogo()),												cc.xywh	(3, 7, 1, 1));
				builder.add(this.getLinkHomePage(),																cc.xywh	(3, 8, 1, 1, CellConstraints.CENTER, CellConstraints.FILL ));
	}
	
	private JPanel getPanelJGrabberInfo() {
		if (jGrabberInfo == null) {
			jGrabberInfo = new JPanel();
			FormLayout layout = new FormLayout(
			        "f:pref:grow, f:20,  f:pref:grow",	 		//columns 
			  "pref, pref, 5, pref, pref, pref, pref, pref");							//rows
			PanelBuilder builder = new PanelBuilder(layout,jGrabberInfo);
			CellConstraints cc = new CellConstraints();

			builder.add(this.getLabelJgrabber(),								cc.xywh	(1, 1, 3, 1));
			builder.add(this.getLabelJgrabber2(),								cc.xywh	(1, 2, 3, 1));
			builder.add(this.getLabelOther(),									cc.xywh	(1, 4, 3, 1));
			builder.add(this.getLinkApache(),									cc.xywh	(1, 5, 1, 1));
			builder.add(this.getLinkDom4j(),									cc.xywh	(1, 6, 1, 1));
			builder.add(this.getLinkForms(),									cc.xywh	(1, 7, 1, 1));
			builder.add(this.getLinkUdrec(),									cc.xywh	(3, 5, 1, 1));
			builder.add(this.getLinkSystray(),									cc.xywh	(3, 6, 1, 1));
		}
		return jGrabberInfo;
	}
	    
	/**
	 * @return Returns the control.
	 */
	public ControlAboutTab getControl() {
		return control;
	}
	/**
	 * @param control The control to set.
	 */
	public void setControl(ControlAboutTab control) {
		this.control = control;
	}
	/**
	 * @return Returns the taAuthors.
	 */
	public JTextArea getTaAuthors() {
		if (taAuthors == null) {
			taAuthors = new JTextArea();
			taAuthors.setEditable(false);
			taAuthors.setLineWrap(true);
			taAuthors.setWrapStyleWord(true);
			taAuthors.setAutoscrolls(true);
		}
		return taAuthors;
	}
	/**
	 * @return Returns the taLicense.
	 */
	public JTextArea getTaLicense() {
		if (taLicense == null) {
			taLicense = new JTextArea();
			taLicense.setEditable(false);
			taLicense.setLineWrap(true);
			taLicense.setWrapStyleWord(true);
			taLicense.setAutoscrolls(true);
		}
		return taLicense;
	}
	/**
	 * @return Returns the taOther.
	 */
	public JTextArea getTaOther() {
		if (taOther == null) {
			taOther = new JTextArea();
			taOther.setEditable(false);
			taOther.setLineWrap(true);
			taOther.setWrapStyleWord(true);
			taOther.setAutoscrolls(true);
		}
		return taOther;
	}
	/**
	 * @return Returns the taVersion.
	 */
	public JTextArea getTaVersion() {
		if (taVersion == null) {
			taVersion = new JTextArea();
			taVersion.setEditable(false);
			taVersion.setLineWrap(true);
			taVersion.setWrapStyleWord(true);
			taVersion.setAutoscrolls(true);
		}
		return taVersion;
	}
	/**
	 * @return Returns the scrollPaneLicense.
	 */
	public JScrollPane getScrollPaneLicense() {
		if (scrollPaneLicense == null) {
			scrollPaneLicense = new JScrollPane();
			scrollPaneLicense.setViewportView(this.getTaLicense());
		}
		return scrollPaneLicense;
	}
	/**
	 * @return Returns the scrollPaneAuthors.
	 */
	public JScrollPane getScrollPaneAuthors() {
		if (scrollPaneAuthors == null) {
			scrollPaneAuthors = new JScrollPane();
			scrollPaneAuthors.setViewportView(this.getTaAuthors());
		}
		return scrollPaneAuthors;
	}
	/**
	 * @return Returns the scrollPaneOther.
	 */
	public JScrollPane getScrollPaneOther() {
		if (scrollPaneOther == null) {
			scrollPaneOther = new JScrollPane();
			scrollPaneOther.setViewportView(this.getTaOther());
		}
		return scrollPaneOther;
	}
	/**
	 * @return Returns the scrollPaneVersion.
	 */
	public JScrollPane getScrollPaneVersion() {
		if (scrollPaneVersion == null) {
			scrollPaneVersion = new JScrollPane();
			scrollPaneVersion.setViewportView(this.getTaVersion());
		}
		return scrollPaneVersion;
	}
	
	private ImageIcon getImageLogo() {
		if (imageLogo == null) {
			imageLogo = new ImageIcon(ClassLoader.getSystemResource("ico/grabber11.png"));
		}
		return imageLogo;
	}
	
	/**
	 * @return Returns the linkHomePage.
	 */
	public JTextPane getLinkApache() {
		if (linkApache == null) {
			linkApache = new JTextPane();		
			linkApache.setPreferredSize(new Dimension(100,20));
			linkApache.setBackground(background);
			linkApache.setContentType("text/html");
			linkApache.setEditable(false);
			linkApache.setText("<html><a href=\"http://www.apache.org\">commons & log4j</a></html>");			
			linkApache.addHyperlinkListener(hyperlinkAdapter);
		}
		return linkApache;
	}
	
	/**
	 * @return Returns the linkHomePage.
	 */
	public JTextPane getLinkUdrec() {
		if (linkUdrec == null) {	    
		    linkUdrec = new JTextPane();			
		    linkUdrec.setPreferredSize(new Dimension(100,20));
		    linkUdrec.setBackground(background);
		    linkUdrec.setContentType("text/html");
		    linkUdrec.setEditable(false);
		    linkUdrec.setText("<html><a href=\"http://www.haraldmaiss.de\">udrec</a></html>");			
		    linkUdrec.addHyperlinkListener(hyperlinkAdapter);
		}
		return linkUdrec;
	}
	
	/**
	 * @return Returns the linkHomePage.
	 */
	public JTextPane getLinkDom4j() {
		if (linkDom4j == null) {
		    linkDom4j = new JTextPane();	
		    linkDom4j.setPreferredSize(new Dimension(100,20));
		    linkDom4j.setBackground(background);
		    linkDom4j.setContentType("text/html");
		    linkDom4j.setEditable(false);
		    linkDom4j.setText("<html><a href=\"http://www.dom4j.org\">dom4j</a></html>");			
		    linkDom4j.addHyperlinkListener(hyperlinkAdapter);
		}
		return linkDom4j;
	}
	
	/**
	 * @return Returns the linkHomePage.
	 */
	public JTextPane getLinkForms() {
		if (linkForms == null) {
		    linkForms = new JTextPane();		
		    linkForms.setPreferredSize(new Dimension(100,20));
		    linkForms.setBackground(background);
		    linkForms.setContentType("text/html");
		    linkForms.setEditable(false);
		    linkForms.setText("<html><a href=\"http://www.jgoodies.com/freeware/forms/index.html\">forms & looks</a></html>");			
		    linkForms.addHyperlinkListener(hyperlinkAdapter);
		}
		return linkForms;
	}
	
	/**
	 * @return Returns the linkHomePage.
	 */
	public JTextPane getLinkSystray() {
		if (linkSystray == null) {
		    linkSystray = new JTextPane();		
		    linkSystray.setPreferredSize(new Dimension(100,20));
		    linkSystray.setBackground(background);
		    linkSystray.setContentType("text/html");
		    linkSystray.setEditable(false);
		    linkSystray.setText("<html><a href=\"http://sourceforge.net/projects/systray\">systray4j</a></html>");			
		    linkSystray.addHyperlinkListener(hyperlinkAdapter);
		}
		return linkSystray;
	}
	
	/**
	 * @return Returns the linkGpl.
	 */
	public JLabel getLabelOther() {
		if (labelOther == null) {
			labelOther = new JLabel("This product includes software developed by other projects");
		}
		return labelOther;
	}
	
	/**
	 * @return Returns the linkHomePage.
	 */
	public JTextPane getLinkHomePage() {
		if (linkHomePage == null) {
		    linkHomePage = new JTextPane();	
		    linkHomePage.setPreferredSize(new Dimension(160,20));
		    linkHomePage.setBackground(background);
		    linkHomePage.setContentType("text/html");
		    linkHomePage.setEditable(false);
		    linkHomePage.setText("<html><a href=\"http://www.jackthegrabber.de\">Jack the JGrabber</a></html>");			
		    linkHomePage.addHyperlinkListener(hyperlinkAdapter);
		}
		return linkHomePage;
	}
	
	/**
	 * @return Returns the linkHomePage.
	 */
	public JLabel getLabelJgrabber() {
		if (labelJGrabber == null) {
			labelJGrabber = new JLabel("Jack the JGrabber is free software, and is released under the");
			labelJGrabber.setName("www.jackthegrabber.de");
		}
		return labelJGrabber;
	}
	
	/**
	 * @return Returns the linkHomePage.
	 */
	public JTextPane getLabelJgrabber2() {
		if (labelJGrabber2 == null) {
		    labelJGrabber2 = new JTextPane();	
		    labelJGrabber2.setPreferredSize(new Dimension(100,20));
		    labelJGrabber2.setBackground(background);
		    labelJGrabber2.setContentType("text/html");
		    labelJGrabber2.setEditable(false);
		    labelJGrabber2.setText("<html><a href=\"http://www.gnu.org/licenses/gpl.txt\">GNU General Public License</a></html>");			
		    labelJGrabber2.addHyperlinkListener(hyperlinkAdapter);
		}
		return labelJGrabber2;
	}
}
