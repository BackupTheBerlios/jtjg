
package presentation.settings;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * @author Reinhard Achleitner
 * @version 17.12.2004
 *
 */
public class GuiTagFrame extends JFrame {

	private JTextField field;
	
	
	/**
	 * @throws java.awt.HeadlessException
	 */
	public GuiTagFrame(String title) throws HeadlessException {
		super(title);
	}
	
	public JTextField getField() {
		return field;
	}
	public void setField(JTextField field) {
		this.field = field;
	}

}
