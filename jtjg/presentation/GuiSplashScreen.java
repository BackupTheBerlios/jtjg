package presentation;
/*
GuiSplashScreen by Alexander Geist

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
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JProgressBar;

import java.util.logging.Logger;

public class GuiSplashScreen extends JDialog {
	private Image image;
    private JProgressBar progress;
    private JLayeredPane panel;
    private String title;

    public GuiSplashScreen(Frame parent, Image image, String text, int progressMin, int progressMax) {
        super(parent);
        this.image = image;
        title=text;
        progress = new JProgressBar(progressMin, progressMax);
        progress.setStringPainted(true);
        init();
    }

    public void setProgress(int position, String text){
        if (position >= progress.getMinimum()
            && position <= progress.getMaximum() && position>progress.getValue()){
            progress.setValue(position);
            progress.setString(text);
        }
    }

    private void init(){
        setUndecorated(true);
        int w = image.getWidth(this);
        int h = image.getHeight(this);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Image back = null;
        setBounds( (d.width - w) / 2, (d.height - h) / 3, w, h);
        try {
            back = new Robot().createScreenCapture(getBounds());
        }
        catch (AWTException e) {
            Logger.getLogger("GuiSplashScreen").warning(e.getMessage());
        }
        Graphics g = back.getGraphics();
        g.drawImage(image, 0, 0, this);
        JLabel label = new JLabel(new ImageIcon(back));
        label.setBounds(0, 0, w, h);
        progress.setBounds(0, 370, 300, 15);
        JLabel labelTitle = new JLabel(title);
        labelTitle.setBounds(90, 10, 300, 15);
        
        JLayeredPane panel = new JLayeredPane();
        panel.add(labelTitle, JLayeredPane.DEFAULT_LAYER);
        panel.add(progress, JLayeredPane.DEFAULT_LAYER);
        panel.add(label, JLayeredPane.DEFAULT_LAYER);
        getContentPane().add(panel);
    }

    public void dispose() {
        super.dispose();
        image = null;
    }
}