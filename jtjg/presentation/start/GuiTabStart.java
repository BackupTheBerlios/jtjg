package presentation.start;
/*
GuiTabStart.java by Geist Alexander 

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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import service.SerHyperlinkAdapter;
import service.SerIconManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.ControlStartTab;


public class GuiTabStart extends JPanel {

	private ControlStartTab control;
	private JPanel panelClient;
	private JPanel panelInfo;
	private JPanel panelWarn;
	private JPanel panelNews;
	private JLabel labelRunningSender;
	private JLabel labelNextRecord;
	private JTextPane paneClient;
	private JTextPane paneInfo;
	private JTextPane paneNews;
	private JTextPane linkWiki;
	private JTextPane paneWarns;
	private JTextPane paneVersion;
	private JButton jButtonSwitchLog;
	private ImageIcon imageLogo;
	private SerIconManager iconManager = SerIconManager.getInstance();
	Color background = (Color)UIManager.get("Panel.background");
	SerHyperlinkAdapter hyperlinkAdapter = new SerHyperlinkAdapter(this);
	
	
	public GuiTabStart(ControlStartTab ctrl) {
		super();
		this.setControl(ctrl);
		initialize();
	}
	
	/**
     * @return Returns the control.
     */
    public ControlStartTab getControl() {
        return control;
    }
    /**
     * @param control The control to set.
     */
    public void setControl(ControlStartTab control) {
        this.control = control;
    }

	private  void initialize() {
		FormLayout layout = new FormLayout(
						  "250:grow, 10, 180:grow, 30, 190",  		// columns 
						  "10, t:130, pref, pref"); 			// rows
		PanelBuilder builder = new PanelBuilder(this, layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.add(this.getPanelClient(),				cc.xy(1, 2));
		builder.add(this.getPanelInfo(), 				cc.xyw(3, 2, 3));
		builder.add(this.getPanelNews(), 				cc.xyw(1, 3, 5));
		builder.add(this.getPanelWarn(),	 			cc.xyw(1, 4, 4));
		builder.add(new JLabel(this.getImageLogo()),   cc.xywh(5, 4, 1, 1, CellConstraints.CENTER, CellConstraints.BOTTOM ));
	}
	
    /**
     * @return Returns the panelClient.
     */
    public JPanel getPanelClient() {
        if (panelClient == null) {
            panelClient = new JPanel();
			FormLayout layout = new FormLayout("40, 10, pref:grow", //columns
					"pref, 5, pref, 5, t:70, 5, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelClient, layout);
			CellConstraints cc = new CellConstraints();
			
			builder.add(new JLabel(iconManager.getIcon("penguin.png")),			cc.xy(1, 1));
			builder.addTitle("<HTML><font size=5>"+ControlMain.getProperty("label_client")+"</font><HTML>",			cc.xy(3, 1));
			builder.addLabel(ControlMain.version[0],			cc.xy(3, 3));
			JScrollPane scrollPane = new JScrollPane(this.getPaneVersion());
			scrollPane.setBorder(null);
			builder.add(scrollPane,								cc.xy(3, 5));
			builder.add(this.getLinkWiki(),						cc.xy(3, 7));
		}
        return panelClient;
    }
    /**
     * @return Returns the panelInfo.
     */
    public JPanel getPanelInfo() {
        if (panelInfo == null) {
            panelInfo = new JPanel();
			FormLayout layout = new FormLayout("40, 10, pref, 5, pref, f:default:grow", //columns
					"pref, 5, pref, 5, pref, 10, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panelInfo, layout);
			CellConstraints cc = new CellConstraints();
			
			builder.add(new JLabel(iconManager.getIcon("info2.png")),			cc.xy(1, 1));
			builder.addTitle("<HTML><font size=5>"+ControlMain.getProperty("label_info")+"</font><HTML>",			cc.xy(3, 1));
			builder.add(this.getLabelRunningSender(),							cc.xyw(3, 3, 4));
			builder.add(this.getLabelNextRecord(),								cc.xyw(3, 5, 4));
			builder.addLabel(ControlMain.getProperty("label_logWindow"),		cc.xy(3, 7));
			builder.add(this.getJButtonSwitchLog(),								cc.xy(5, 7));
		}
        return panelInfo;
    }
    /**
     * @return Returns the panelNews.
     */
    public JPanel getPanelNews() {
        if (panelNews == null) {
            panelNews = new JPanel();
			FormLayout layout = new FormLayout("40, 10, 600", //columns
					"pref, 5, f:120"); //rows
			PanelBuilder builder = new PanelBuilder(panelNews, layout);
			CellConstraints cc = new CellConstraints();
			
			builder.add(new JLabel(iconManager.getIcon("browser.png")),			cc.xy(1, 1));
			builder.addTitle("<HTML><font size=5>"+ControlMain.getProperty("label_news")+"</font><HTML>",	cc.xy(3, 1));
			
			JScrollPane scrollPane = new JScrollPane(this.getPaneNews());
			scrollPane.setBorder(null);
			builder.add(scrollPane,    cc.xy(3, 3));
		}
        return panelNews;
    }
    /**
     * @return Returns the panelWarn.
     */
    public JPanel getPanelWarn() {
        if (panelWarn == null) {
            panelWarn = new JPanel();
            FormLayout layout = new FormLayout("30, 10, pref", //columns
				"pref, 5, t:115"); //rows
			PanelBuilder builder = new PanelBuilder(panelWarn, layout);
			CellConstraints cc = new CellConstraints();
			
			builder.add(new JLabel(iconManager.getIcon("warning.png")),			cc.xy(1, 1));
			builder.addTitle("<HTML><font size=5>"+ControlMain.getProperty("label_warn")+"</font><HTML>",			cc.xy(3, 1));
			
			JScrollPane scrollPane = new JScrollPane(this.getPaneWarns());
			scrollPane.setBorder(null);
			builder.add(scrollPane,    cc.xy(3, 3));
		}
        return panelWarn;
    }
    
    /**
     * @return Returns the paneClient.
     */
    public JTextPane getPaneClient() {
        return paneClient;
    }
    /**
     * @return Returns the paneInfo.
     */
    public JTextPane getPaneInfo() {
        return paneInfo;
    }
    /**
     * @return Returns the paneNews.
     */
    public JTextPane getPaneNews() {
        if (paneNews==null) {
            paneNews = new JTextPane();
            paneNews.setEditable(false);
            paneNews.setBackground(background);
            paneNews.addHyperlinkListener(hyperlinkAdapter);
        }
        return paneNews;
    }
    
    /**
	 * @return Returns the linkHomePage.
	 */
	public JTextPane getLinkWiki() {
		if (linkWiki == null) {
		    linkWiki = new JTextPane();		
		    linkWiki.setBackground(background);
		    linkWiki.setContentType("text/html");
		    linkWiki.setEditable(false);
		    linkWiki.setText("<html><a href=\"http://wiki.tuxbox.org/Jack_the_JGrabber\">WIKI</a></html>");			
		    linkWiki.addHyperlinkListener(hyperlinkAdapter);
		}
		return linkWiki;
	}
	
	public JTextPane getPaneWarns() {
	    if (paneWarns==null) {
	        paneWarns=new JTextPane();
	        paneWarns.setBackground(background);
	        paneWarns.setContentType("text/html");
	        paneWarns.setEditable(false);	
	    }
	    return paneWarns;
	}
	
	public JTextPane getPaneVersion() {
	    if (paneVersion==null) {
	        paneVersion=new JTextPane();
	        paneVersion.setBackground(background);
	        paneVersion.setContentType("text/html");
	        paneVersion.setEditable(false);	
	    }
	    return paneVersion;
	}
	
	private ImageIcon getImageLogo() {
		if (imageLogo == null) {
			imageLogo = new ImageIcon(ClassLoader.getSystemResource("ico/jtjg_bubble.png"));
		}
		return imageLogo;
	}
	
    /**
     * @return Returns the labelNextRecord.
     */
    public JLabel getLabelNextRecord() {
        if (labelNextRecord==null) {
            labelNextRecord=new JLabel();
        }
        return labelNextRecord;
    }
    /**
     * @return Returns the labelRunningSender.
     */
    public JLabel getLabelRunningSender() {
        if (labelRunningSender==null) {
            labelRunningSender=new JLabel();
        }
        return labelRunningSender;
    }
    
    /**
	 * This method initializes jButtonPlayback
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getJButtonSwitchLog() {
		if (jButtonSwitchLog == null) {
			jButtonSwitchLog = new JButton();
			if (ControlMain.getSettingsMain().isShowLogWindow()) {
				jButtonSwitchLog.setText(ControlMain.getProperty("button_off"));
			} else {
				jButtonSwitchLog.setText(ControlMain.getProperty("button_on"));
			}
			jButtonSwitchLog.setActionCommand("switchLog");
			jButtonSwitchLog.setToolTipText(ControlMain.getProperty("buttontt_log"));
			jButtonSwitchLog.addActionListener(this.getControl());
		}
		return jButtonSwitchLog;
	}
}
