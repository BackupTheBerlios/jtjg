package presentation;
/*
 * GuiLogWindow.java by Geist Alexander
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import service.SerLogAppender;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import control.ControlMain;

public class GuiLogWindow extends JFrame implements ActionListener{

	private JTextArea logArea;
	private JScrollPane jScrollPaneLogArea;
	private JButton jButtonClearLogArea;
	private JPanel jPanelOutput;
	private boolean shouldBeVisible;

	public GuiLogWindow() {
		super("Log XMediaGrabber");
		setContentPane(getJPanelOutput());
		setSize(600, 150);

		final ActionListener listener = new ActionListener() {
			public final void actionPerformed(final ActionEvent e) {
				switchLogVisiblity();
			}
		};
		final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK, true);
		getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JButton switchButton = ControlMain.getControl().getView().getTabStart().getJButtonSwitchLog();
				switchButton.setText(ControlMain.getProperty("button_on"));
				ControlMain.logWindow.setVisible(false);
				ControlMain.logWindow.setShouldBeVisible(false);

			}
		});
	}
	
	/**
	 * Klick-Events der Buttons
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action == "clearLog") {
			getLogArea().setText(null);
		}
	}
	

	/**
	 * This method initializes jPanelOutput
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelOutput() {
		if (jPanelOutput == null) {
			jPanelOutput = new JPanel();
			FormLayout layout = new FormLayout("f:d:grow", //columna
					"f:d:grow, pref"); //rows
			PanelBuilder builder = new PanelBuilder(jPanelOutput, layout);
			CellConstraints cc = new CellConstraints();

			builder.add(this.getJScrollPaneLogArea(), cc.xy(1, 1));
			builder.add(this.getJButtonClearLogArea(), cc.xy(1, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
		}
		return jPanelOutput;
	}

	/**
	 * This method initializes logArea
	 * 
	 * @return javax.swing.JTextPane
	 */
	public JTextArea getLogArea() {
		if (logArea == null) {
			logArea = new JTextArea();
			logArea.setEditable(false);
			SerLogAppender.getTextAreas().add(logArea);
		}
		return logArea;
	}

	/**
	 * @return Returns the jScrollPaneLogArea.
	 */
	public JScrollPane getJScrollPaneLogArea() {
		if (jScrollPaneLogArea == null) {
			jScrollPaneLogArea = new JScrollPane();
			jScrollPaneLogArea.setViewportView(getLogArea());
		}
		return jScrollPaneLogArea;
	}

	/**
	 * This method initializes jButtonClearLogArea
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getJButtonClearLogArea() {
		if (jButtonClearLogArea == null) {
			jButtonClearLogArea = new JButton();
			jButtonClearLogArea.setText(ControlMain.getProperty("button_clearLog"));
			jButtonClearLogArea.setActionCommand("clearLog");
			jButtonClearLogArea.setToolTipText(ControlMain.getProperty("buttontt_clearLog"));
			jButtonClearLogArea.addActionListener(this);
		}
		return jButtonClearLogArea;
	}
	
	/**
	 * @return Returns the shouldBeVisible.
	 */
	public boolean isShouldBeVisible() {
		return shouldBeVisible;
	}
	/**
	 * @param shouldBeVisible
	 *            The shouldBeVisible to set.
	 */
	public void setShouldBeVisible(boolean shouldBeVisible) {
		this.shouldBeVisible = shouldBeVisible;
	}
	public void setVisible(boolean value) {
		if (isShouldBeVisible()) {
			super.setVisible(value);
			int pos = getLogArea().getDocument().getLength() - 1;
			if (pos > -1) {
				this.getLogArea().setCaretPosition(pos);
			}
		}
	}
	public static void switchLogVisiblity() {
		JButton switchButton = ControlMain.getControl().getView().getTabStart().getJButtonSwitchLog();
		if (ControlMain.logWindow.isVisible()) {
			switchButton.setText(ControlMain.getProperty("button_on"));
			ControlMain.logWindow.setVisible(false);
			ControlMain.logWindow.setShouldBeVisible(false);
		} else {
			switchButton.setText(ControlMain.getProperty("button_off"));
			ControlMain.logWindow.setShouldBeVisible(true);
			ControlMain.logWindow.setVisible(true);
		}
	}
}

