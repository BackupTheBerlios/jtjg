package presentation.recordInfo;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Reinhard Achleitner
 * @version 02.12.2004
 *  
 */
public class BaseTreeNode extends DefaultMutableTreeNode {

	private String ident;

	/**
	 *  
	 */
	public BaseTreeNode() {
		super();
	}

	/**
	 * @param userObject
	 */
	public BaseTreeNode(Object userObject) {
		super(userObject);
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public BaseTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public String getIdent() {
		if (ident == null)
		{
			return getUserObject().toString();
		}
		return ident;
	}
	public void setIdent(String ident) {
		this.ident = ident;
	}

}