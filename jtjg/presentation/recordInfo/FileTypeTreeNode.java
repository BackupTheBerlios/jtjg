package presentation.recordInfo;

import javax.swing.Icon;

import service.SerIconManager;

/**
 * @author Reinhard Achleitner
 * @version 02.12.2004
 *  
 */
public class FileTypeTreeNode extends BaseTreeNode {

	private int type = 0;
	
	private long createTime;

	public static final int VIDEO = 0;
	public static final int AUDIO = 1;
	public static final int OTHER = 2;
	public static final int DIRECTORY = 3;
	public static final int CREATED = 4;

	/**
	 *  
	 */
	public FileTypeTreeNode() {
		super();
	}

	/**
	 * @param userObject
	 */
	public FileTypeTreeNode(Object userObject, int type) {
		super(userObject);
		this.type = type;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public Icon getIcon() {
		switch (type) {
			case VIDEO : {
				return SerIconManager.getInstance().getIcon("video.gif");
			}
			case AUDIO : {
				return SerIconManager.getInstance().getIcon("audio.gif");
			}
		}
		return null;
	}
	
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}