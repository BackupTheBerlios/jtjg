package presentation.muxxer;
/*
GuiMuxxerView.java by Geist Alexander 

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
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import service.SerGUIUtils;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;
import control.muxxer.ControlMuxxerView;


public class GuiMuxxerView extends JDialog{
	
	ControlMuxxerView control;
	JList jListFiles;
    JCheckBox cbStartPX;
    JCheckBox cbStartMplex;
	JRadioButton rbSVCD;
	JRadioButton rbDVD;
	JRadioButton rbMPEG;
    JProgressBar progressPX;
    JProgressBar progressMplex;
	JButton pbOk;
	JPanel mainPanel;
    JPanel panelProgress;
	ButtonGroup buttonGroupMuxxType = new ButtonGroup();

	public GuiMuxxerView(ControlMuxxerView control) {
		super(ControlMain.getControl().getView());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setControl(control);
        this.setTitle("Demultiplex/Multiplex Options");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int result=0;
                if (getControl().isMuxing()) {
                    result = JOptionPane.showConfirmDialog(
                            getControl().getView(),
                            ControlMain.getProperty("msg_abortMux"),
                            "",
                            JOptionPane.YES_NO_OPTION
                    );
                }
                if (result==0) {
                    getControl().stopAllProcesses();
                    dispose();
                }
            }
        });
		initialize();
        pack();    
        SerGUIUtils.center(this);
	}
	
	private void initialize() {
		this.getContentPane().add(this.getMainPanel());
	}
	    
	    
    private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			FormLayout layout = new FormLayout("pref:grow, 25, pref", //columns
					"pref, 15, pref, 5, pref, pref, pref, 5, pref, 10, pref"); //rows
			PanelBuilder builder = new PanelBuilder(layout,mainPanel);
			builder.setDefaultDialogBorder();
			CellConstraints cc = new CellConstraints();
			
            if (control.getFiles()!=null && control.getFiles().size()>0) {
                builder.add(new JScrollPane(this.getJListFiles()),                  cc.xywh(1,1,1,7));
                builder.add(this.getPanelProgress(),                  cc.xyw(1,9,3));
            }
            builder.add(this.getCbStartPX()                 ,                   cc.xy(3,1));
            builder.addSeparator("",                                            cc.xy(3,2));
            builder.add(this.getCbStartMplex(),                                 cc.xy(3,3));
            builder.add(this.getRbMPEG(),                                       cc.xy(3,5));
            builder.add(this.getRbSVCD(),                                       cc.xy(3,6));
            builder.add(this.getRbDVD(),                                        cc.xy(3,7));
            
			builder.add(this.getPbOk(),  cc.xy(3, 11));
		}
		return mainPanel;
	}
    
    private JPanel getPanelProgress() {
        if (panelProgress == null) {
            panelProgress = new JPanel();
            FormLayout layout = new FormLayout("pref, 10, pref:grow", //columns
                    "pref, 5, pref"); //rows
            PanelBuilder builder = new PanelBuilder(layout,panelProgress);
            builder.setDefaultDialogBorder();
            CellConstraints cc = new CellConstraints();
            
            builder.addLabel("ProjectX",                              cc.xy(1,1));
            builder.add(this.getProgressPX(),                         cc.xy(3,1));
            builder.addLabel("Mplex",                                 cc.xy(1,3));
            builder.add(this.getProgressMplex(),                      cc.xy(3,3));
        }
        return panelProgress;
    }

	/**
	 * @return Returns the control.
	 */
	public ControlMuxxerView getControl() {
		return control;
	}
	/**
	 * @param control The control to set.
	 */
	public void setControl(ControlMuxxerView control) {
		this.control = control;
	}
	/**
	 * @return Returns the jListFiles.
	 */
	public JList getJListFiles() {
		if (jListFiles==null) {
			jListFiles=new JList(control.getFiles().toArray());
            jListFiles.setCellRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(
                        JList list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
                        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        File file = (File)value;
                        return new JLabel(file.getName());
                    }
                });
		}
		return jListFiles;
	}
	/**
	 * @return Returns the pbOk.
	 */
	public JButton getPbOk() {
		if (pbOk == null) {
			pbOk = new JButton(ControlMain.getProperty("button_ok"));
			pbOk.setActionCommand("ok");
			pbOk.addActionListener(control);
		}
		return pbOk;
	}
	/**
	 * @return Returns the rbDVD.
	 */
	public JRadioButton getRbDVD() {
		if (rbDVD == null) {
			rbDVD = new JRadioButton("DVD");
            rbDVD.addActionListener(control);
			buttonGroupMuxxType.add(rbDVD);
            if (!control.getOptions().isUseMplex()) {
                rbDVD.setEnabled(false);
            }
		}
		return rbDVD;
	}
	/**
	 * @return Returns the rbMPEG.
	 */
	public JRadioButton getRbMPEG() {
		if (rbMPEG == null) {
			rbMPEG = new JRadioButton("Mpeg");
            rbMPEG.addActionListener(control);
			buttonGroupMuxxType.add(rbMPEG);
            if (!control.getOptions().isUseMplex()) {
                rbMPEG.setEnabled(false);
            }
		}
		return rbMPEG;
	}
	/**
	 * @return Returns the rbSVCD.
	 */
	public JRadioButton getRbSVCD() {
		if (rbSVCD == null) {
			rbSVCD = new JRadioButton("SVCD");
            rbSVCD.addActionListener(control);
			buttonGroupMuxxType.add(rbSVCD);
            if (!control.getOptions().isUseMplex()) {
                rbSVCD.setEnabled(false);
            }
		}
		return rbSVCD;
	}
    
    /**
     * @return Returns the cbStartPX.
     */
    public JCheckBox getCbStartPX() {
        if (cbStartPX == null) {
            cbStartPX = new JCheckBox(ControlMain.getProperty("cbStartPX"));
            cbStartPX.setActionCommand("cbStartPX");
            cbStartPX.addActionListener(control);
        }
        return cbStartPX;
    }
    /**
     * @return Returns the cbStartMplex.
     */
    public JCheckBox getCbStartMplex() {
        if (cbStartMplex == null) {
            cbStartMplex = new JCheckBox(ControlMain.getProperty("cbStartMplex"));
            cbStartMplex.setActionCommand("cbStartMplex");
            cbStartMplex.addActionListener(control);
        }
        return cbStartMplex;
    }
    
    public void checkMplexButtons(boolean enable) {
        this.getRbDVD().setEnabled(enable);
        this.getRbMPEG().setEnabled(enable);
        this.getRbSVCD().setEnabled(enable);
    }
    /**
     * @return Returns the progressMplex.
     */
    public JProgressBar getProgressMplex() {
        if (progressMplex == null){
            progressMplex = new JProgressBar(SwingConstants.HORIZONTAL, 0, 0);
            progressMplex.setStringPainted(true);            
        }
        return progressMplex;
    }
   
    /**
     * @return Returns the progressPX.
     */
    public JProgressBar getProgressPX() {
        if (progressPX == null){
            progressPX = new JProgressBar(SwingConstants.HORIZONTAL, 0, 0);
            progressPX.setMaximum(100);
            progressPX.setStringPainted(true);            
        }
        return progressPX;
    }
}
