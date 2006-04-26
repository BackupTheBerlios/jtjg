package presentation.noticeBroadcast;
/*
GuiNoticeBroadcastView.java by Geist Alexander 

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

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import model.BONoticeBroadcast;
import service.SerGUIUtils;
import service.SerIconManager;
import service.SerNoticeListHandler;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.notice.ControlNoticeBroadcastView;


public class GuiNoticeBroadcastView extends JDialog {
	
	ControlNoticeBroadcastView control;
	public GuiNoticeTableModel noticeTableModel;
	private JPanel mainPanel;
	private JScrollPane jScrollPaneNoticeList;
	private JTable jTableNoticeList;
	private JButton jButtonAnlegen;
	private JButton jButtonLoeschen;
    private JButton jButtonScan;
    private JProgressBar progressScan;
	
	private SerIconManager iconManager = SerIconManager.getInstance();
	
	public GuiNoticeBroadcastView(ControlNoticeBroadcastView ctrl) {
		super(ControlMain.getControl().getView(), ControlMain.getProperty("label_broadcastList"));
        this.setModal(true);
		this.setControl(ctrl);
		initialize();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                getControl().runningScan=false;
                SerNoticeListHandler.saveNoticeList(control.getNoticeList());
            }
        });
        pack();
        SerGUIUtils.center(this);
	}
	
	private void initialize() {
		this.getContentPane().add(this.getMainPanel());
	}

	/**
	 * @return Returns the mainPanel.
	 */
	public JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			FormLayout layout = new FormLayout("pref:grow, 5, pref", //columns
					"pref, 10, pref, pref, 60, pref, 5, pref"); //rows
			PanelBuilder builder = new PanelBuilder(mainPanel, layout);
			builder.setDefaultDialogBorder();
			CellConstraints cc = new CellConstraints();

			builder.addSeparator(ControlMain.getProperty("label_notice"), 	cc.xyw(1,1,3));
			builder.add(this.getJScrollPaneNoticeList(), 					cc.xywh(1,3,1,6));
			builder.add(this.getJButtonAnlegen(), 							cc.xy(3,3));
			builder.add(this.getJButtonLoeschen(), 							cc.xy(3,4));
            builder.add(this.getProgressScan(),                             cc.xy(3,6));
            builder.add(this.getJButtonScan(),                              cc.xy(3,8));
		}
		return mainPanel;
	}
	
	/**
	 * This method initializes jButtonAnlegen	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButtonAnlegen() {
		if (jButtonAnlegen == null) {
			jButtonAnlegen = new JButton();
			jButtonAnlegen.setIcon(iconManager.getIcon("new.png"));
			jButtonAnlegen.setText(ControlMain.getProperty("button_create"));
			jButtonAnlegen.setActionCommand("add");
			jButtonAnlegen.addActionListener(control);
		}
		return jButtonAnlegen;
	}
	/**
	 * This method initializes getJButtonLoeschen	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButtonLoeschen() {
		if (jButtonLoeschen == null) {
			jButtonLoeschen = new JButton();
			jButtonLoeschen.setIcon(iconManager.getIcon("trash.png"));
			jButtonLoeschen.setText(ControlMain.getProperty("button_delete"));
			jButtonLoeschen.setActionCommand("delete");
			jButtonLoeschen.addActionListener(control);
		}
		return jButtonLoeschen;
	}
    /**
     * This method initializes jButtonScan   
     *  
     * @return javax.swing.JButton  
     */    
    private JButton getJButtonScan() {
        if (jButtonScan == null) {
            jButtonScan = new JButton();
            jButtonScan.setIcon(iconManager.getIcon("find.png"));
            jButtonScan.setText(ControlMain.getProperty("button_scan"));
            jButtonScan.setActionCommand("scan");
            jButtonScan.addActionListener(control);
        }
        return jButtonScan;
    }
	/**
	 * @return Returns the jScrollPaneNoticeList.
	 */
	public JScrollPane getJScrollPaneNoticeList() {
        if (jScrollPaneNoticeList == null) {
            jScrollPaneNoticeList = new JScrollPane();
            jScrollPaneNoticeList.setViewportView(this.getJTableNoticeList());
        }
		return jScrollPaneNoticeList;
	}
	/**
	 * @return Returns the jTableNoticeList.
	 */
	public JTable getJTableNoticeList() {
		if (jTableNoticeList == null) {
			noticeTableModel = new GuiNoticeTableModel(control);
			jTableNoticeList = new JTable(noticeTableModel);
			GuiNoticeTableRenderer renderer = new GuiNoticeTableRenderer();
		    Enumeration num = jTableNoticeList.getColumnModel().getColumns();
		    while (num.hasMoreElements()) {
		      ((TableColumn)num.nextElement()).setHeaderRenderer(renderer);
		    }   
			
			jTableNoticeList.setName("noticeTable");
			jTableNoticeList.getColumnModel().getColumn(0).setPreferredWidth(250);

			
			jTableNoticeList.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() {
				
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
						int column) {
					BONoticeBroadcast notice = (BONoticeBroadcast)getControl().getNoticeList().get(row);
                    JCheckBox checkbox = new JCheckBox();
                    checkbox.setHorizontalAlignment(SwingConstants.CENTER);
//                    if (column==1) {
//                        checkbox.setSelected(notice.isSearchEpg());
//                        return checkbox;
//                    } else if (column==2) {
//                        checkbox.setSelected(notice.isSearchMovieGuide());
//                        return checkbox;
//                    } else 
                    if (column==1) {
                        checkbox.setSelected(notice.isSearchOnlyTitle());
                        return checkbox;
                    } else if (column==2) {
                        checkbox.setSelected(notice.isBuildTimer());
                        return checkbox;
                    } 
                    return checkbox;				
				}
			});
		
            TableColumn fnc = jTableNoticeList.getTableHeader().getColumnModel().getColumn(1);
            jTableNoticeList.getTableHeader().getColumnModel().removeColumn(fnc);
            TableColumn fnc2 = jTableNoticeList.getTableHeader().getColumnModel().getColumn(1);
            jTableNoticeList.getTableHeader().getColumnModel().removeColumn(fnc2);
        }
		return jTableNoticeList;
	}
	/**
	 * @return Returns the control.
	 */
	public ControlNoticeBroadcastView getControl() {
		return control;
	}
	/**
	 * @param control The control to set.
	 */
	public void setControl(ControlNoticeBroadcastView control) {
		this.control = control;
	}
    /**
     * @return Returns the progressScan.
     */
    public JProgressBar getProgressScan() {
        if (progressScan == null){
            progressScan = new JProgressBar(SwingConstants.HORIZONTAL, 0, 0);
            progressScan.setStringPainted(true);            
        }
        return progressScan;
    }
}