package presentation;
/*
GuiUdrecOptionsDialog.java by Geist Alexander 

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.MaskFormatter;

import java.util.logging.Logger;

import model.BOUdrecOptions;
import service.SerGUIUtils;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;

public class GuiUdrecOptionsDialog extends JFrame implements ActionListener{

    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panelMain;
    private JList jListOptions;
    private JSpinner jSpinnerVSPlit;
    private JSpinner jSpinnerASPlit;
    private JSpinner jSpinnerBuffer;
    private JFormattedTextField jTextFieldUdp;
    private JFormattedTextField jTextFieldTcp;
    private JTextField optionsField;
    private JButton jButtonOk;
	private JButton jButtonCancel;
    private String[] allOptions = {"-idd", "-rm", "-gtc", "-log" };
    private BOUdrecOptions udrecOptions;
    
    public GuiUdrecOptionsDialog(BOUdrecOptions udrecOptions, JTextField actField) {
        optionsField=actField;
        this.setUdrecOptions(udrecOptions);
        initialize();
		this.setResizable(false);
		this.setTitle("Udrec-Optionen");
		SerGUIUtils.center(this);
		pack();
		setVisible(true);
    }
    
    public void initialize() {
        this.getContentPane().add(this.getPanelMain());
    }
    
    private JPanel getPanelMain() {
		if (panelMain == null) {
			panelMain = new JPanel();
			FormLayout layout = new FormLayout("pref, 10, pref", //columns
					"pref, 5, pref, 15, pref"); //rows
			PanelBuilder builder = new PanelBuilder(layout,panelMain);
			builder.setDefaultDialogBorder();
			CellConstraints cc = new CellConstraints();

			builder.add(this.getPanel1(),			cc.xy(1, 1));
			builder.add(this.getPanel2(),			cc.xywh(3, 1, 1, 3));
			builder.add(this.getPanel3(),			cc.xy(1, 3));
			builder.add(ButtonBarFactory.buildOKCancelBar(this.getJButtonCancel(), this.getJButtonOk()),  cc.xyw(1, 5, 3));
			

		}
		return panelMain;
	}
    
    private JPanel getPanel1() {
		if (panel1 == null) {
			panel1 = new JPanel();
			FormLayout layout = new FormLayout("pref, 5, pref", //columns
					"pref, 5, pref, 5, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panel1, layout);
			builder.setDefaultDialogBorder();
			CellConstraints cc = new CellConstraints();

			builder.add(new JLabel("VSplit"),				cc.xy(1, 1));
			builder.add(this.getJSpinnerVSPlit(),			cc.xy(3, 1));
			builder.add(new JLabel("ASplit"),				cc.xy(1, 3));
			builder.add(this.getJSpinnerASPlit(),			cc.xy(3, 3));
			builder.add(new JLabel("Buffer"),				cc.xy(1, 5));
			builder.add(this.getJSpinnerBuffer(),			cc.xy(3, 5));

		}
		return panel1;
	}
    
    private JPanel getPanel2() {
		if (panel2 == null) {
			panel2 = new JPanel();
			FormLayout layout = new FormLayout("50", //columns
					"pref"); //rows
			PanelBuilder builder = new PanelBuilder(panel2, layout);
			builder.setDefaultDialogBorder();
			CellConstraints cc = new CellConstraints();

			builder.add(new JScrollPane(this.getJListOptions()),			cc.xy(1, 1));

		}
		return panel2;
	}
    
    private JPanel getPanel3() {
		if (panel3 == null) {
			panel3 = new JPanel();
			FormLayout layout = new FormLayout("pref, 5, 50", //columns
					"pref, 5, pref"); //rows
			PanelBuilder builder = new PanelBuilder(panel3, layout);
			builder.setDefaultDialogBorder();
			CellConstraints cc = new CellConstraints();

			builder.add(new JLabel("TCP-Port"),				cc.xy(1, 1));
			builder.add(this.getJTextFieldTcp(),			cc.xy(3, 1));
			builder.add(new JLabel("UDP-Port"),				cc.xy(1, 3));
			builder.add(this.getJTextFieldUdp(),			cc.xy(3, 3));

		}
		return panel3;
	}
    
    public void actionPerformed(ActionEvent event) {
    	String action = event.getActionCommand();
    	if (action.equals("ok")) {
    	    getUdrecOptions().setVSplit(((Integer)this.getJSpinnerVSPlit().getValue()).toString());
    	    getUdrecOptions().setASplit(((Integer)this.getJSpinnerASPlit().getValue()).toString());
    	    getUdrecOptions().setBuffer(((Integer)this.getJSpinnerBuffer().getValue()).toString());
    	    getUdrecOptions().setTcpPort(this.getJTextFieldTcp().getText());
    	    getUdrecOptions().setUdpPort(this.getJTextFieldUdp().getText());
    	    
    	    Object[] values = this.getJListOptions().getSelectedValues();    	    
    	    String[] options = new String[values.length];
    	    for (int i=0; i<values.length; i++) {
    	     options[i]=(String)values[i];   
    	    }
    	    getUdrecOptions().setOptionList(options);
    	    
    	    optionsField.setText(udrecOptions.toString());
    	    this.dispose();
    	}
		if (action.equals("cancel")) {
			this.dispose();
		}
    }
    
	/**
	 * @return jButtonOk
	 */
	public JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton(ControlMain.getProperty("button_ok"));
			jButtonOk.setActionCommand("ok");
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	
	/**
	 * @return jButtonCancel
	 */
	public JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton(ControlMain.getProperty("button_cancel"));
			jButtonCancel.setActionCommand("cancel");
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
    
    
    private JList getJListOptions() {
        if (jListOptions==null) {
            jListOptions = new JList(allOptions);
            jListOptions.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            jListOptions.setSelectedIndices(this.getIndices());
        }
		return jListOptions;
    }
    
    private int[] getIndices() {
    	ArrayList ind = new ArrayList();
    	for (int i=0; i<allOptions.length; i++) {
    		for (int i2=0; i2<this.getUdrecOptions().getOptionList().length; i2++) {
    		    String option = this.getUdrecOptions().getOptionList()[i2];
    		    if (option.indexOf(allOptions[i])>-1) {
    		        ind.add(new Integer(i));
    		    }
    		}
    	}
    	int ret[] = new int[ind.size()];
    	for (int i=0; i<ret.length; i++) {
    	    ret[i]=((Integer)ind.get(i)).intValue();
    	}
    	return ret;
    }
    /**
     * @return Returns the jSpinnerVSPlit.
     */
    public JSpinner getJSpinnerVSPlit() {
        if (jSpinnerVSPlit==null) {
        	int value = Integer.parseInt(this.getUdrecOptions().getVSplit());
            SpinnerNumberModel model = new SpinnerNumberModel(value, -1, 10, 1);
            jSpinnerVSPlit = new JSpinner(model);	   
        }
        return jSpinnerVSPlit;
    }
    /**
     * @param jListOptions The jListOptions to set.
     */
    public void setJListOptions(JList pidList) {
        this.jListOptions = pidList;
    }
	/**
	 * @return Returns the jSpinnerASPlit.
	 */
	public JSpinner getJSpinnerASPlit() {
		if (jSpinnerASPlit==null) {
			int value = Integer.parseInt(this.getUdrecOptions().getASplit());
            SpinnerNumberModel model = new SpinnerNumberModel(value, -1, 10, 1);
            jSpinnerASPlit = new JSpinner(model);	   
        }
        return jSpinnerASPlit;
	}
	/**
	 * @return Returns the jSpinnerBuffer.
	 */
	public JSpinner getJSpinnerBuffer() {
		if (jSpinnerBuffer==null) {
			int value = Integer.parseInt(this.getUdrecOptions().getBuffer());
            SpinnerNumberModel model = new SpinnerNumberModel(value, 1, 128, 1);
            jSpinnerBuffer = new JSpinner(model);	   
        }
        return jSpinnerBuffer;
	}
	/**
	 * @return Returns the udrecOptions.
	 */
	public BOUdrecOptions getUdrecOptions() {
		return udrecOptions;
	}
	/**
	 * @param udrecOptions The udrecOptions to set.
	 */
	public void setUdrecOptions(BOUdrecOptions udrecOptions) {
		this.udrecOptions = udrecOptions;
	}
	/**
	 * @return Returns the jTextFieldTcp.
	 */
	public JFormattedTextField getJTextFieldTcp() {
		if (jTextFieldTcp==null) {
			try {
				jTextFieldTcp = new JFormattedTextField(new MaskFormatter("#####"));
				jTextFieldTcp.setText(this.getUdrecOptions().getTcpPort());
			} catch (ParseException e) {
			    Logger.getLogger("GuiUdrecOptionsDialog").warning(e.getMessage());
			}
		}
		return jTextFieldTcp;
	}
	/**
	 * @return Returns the jTextFieldUdp.
	 */
	public JFormattedTextField getJTextFieldUdp() {
		if (jTextFieldUdp==null) {
			try {
				jTextFieldUdp = new JFormattedTextField(new MaskFormatter("#####"));
				jTextFieldUdp.setText(this.getUdrecOptions().getUdpPort());
			} catch (ParseException e) {
			    Logger.getLogger("GuiUdrecOptionsDialog").warning(e.getMessage());
			}
		}
		return jTextFieldUdp;
	}
}
