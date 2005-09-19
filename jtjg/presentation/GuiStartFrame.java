package presentation;
/*
GuiStartFrame by Alexander Geist

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
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import service.SerGUIUtils;
import service.SerIconManager;


public class GuiStartFrame extends JFrame {

    public GuiStartFrame() {
        super();
        try {
            init();
        }
        catch (Exception e) {
            Logger.getLogger("GuiStartFrame").error(e.getMessage());
        }
    }

    private void init() {
        setTitle("Jack the JGrabber");
        SerIconManager im = SerIconManager.getInstance();
        setIconImage(im.getIcon("grabber1.gif").getImage());
        SerGUIUtils.center(this);
    }
}