package service;
/*
SerGUIUtils.java by Geist Alexander 

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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;

public class SerGUIUtils {
	
	private final static Toolkit tk = Toolkit.getDefaultToolkit();
  	
	public static void center(Window w) {
		Dimension screenSize = tk.getScreenSize();
		Dimension wSize = w.getSize();
	
		int x = (screenSize.width - wSize.width) / 2;
		int y = (screenSize.height - wSize.height) / 2;
	
		w.setLocation(x, y);
	}
	
	public static void centerTop(Window w) {
		Dimension screenSize = tk.getScreenSize();
		Dimension wSize = w.getSize();
	
		int x = (screenSize.width - wSize.width) / 2;
		int y = 0;
	
		w.setLocation(x, y);
	}
	
	public static void addComponent(Container cont,
			   GridBagLayout gbl,
			   Component c,
			   int x, int y,
			   int width, int heigt,
			   double weightx, double weighty )
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x; gbc.gridy = y;
		gbc.gridwidth = width; gbc.gridheight = heigt;
		gbc.weightx = weightx; gbc.weighty = weighty;
		gbl.setConstraints( c, gbc );
		cont.add( c );
	}
}
