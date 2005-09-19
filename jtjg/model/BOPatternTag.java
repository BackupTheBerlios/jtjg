
package model;

import control.ControlMain;

/**
 * @author Reinhard Achleitner
 * @version 10.12.2004
 *
 */
public class BOPatternTag {

	private String name;
	
	private String description;

	private static BOPatternTag[] tags;
	
	/**
	 * 
	 */
	public BOPatternTag() {
		super();
	}
	
	public BOPatternTag(String name,String description)
	{
		this.name = name;
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName() + " " + getDescription();
	}
	

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static BOPatternTag[] getTags()
	{
		if (tags == null)
		{
			BOPatternTag tagChannel = new BOPatternTag("%CHANNEL%",ControlMain.getProperty("sender"));
			BOPatternTag tagTime = new BOPatternTag("%TIME%",ControlMain.getProperty("filep_time"));
			BOPatternTag tagDate = new BOPatternTag("%DATE%",ControlMain.getProperty("filep_date"));
			BOPatternTag tagDate2 = new BOPatternTag("%DATE YY-MM-DD%",ControlMain.getProperty("filep_datespecial"));
			BOPatternTag tagName = new BOPatternTag("%NAME%",ControlMain.getProperty("filep_name"));
			BOPatternTag tagSerie = new BOPatternTag("%SERIE%",ControlMain.getProperty("filep_serie"));
			tags = new BOPatternTag[] {tagChannel,tagTime,tagDate,tagDate2,tagName,tagSerie};
		}
		return tags;
	}
}
