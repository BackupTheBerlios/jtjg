package service;/*
SerAlertDialog.java by Geist Alexander 

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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import control.ControlMain;

//Mit SerAlertDialog.alert(String nachricht, Compnonent parentComponent) aufrufen!!!
public class SerAlertDialog extends JFrame  {
	
	public static void alert(String message, Component comp )  { 
		JOptionPane.showMessageDialog(
			comp,
			message,
			ControlMain.getProperty("attention"),
			JOptionPane.YES_OPTION
		);
	}
	
	public static void alertConnectionLost( String loggingClass, Component comp )  { 
		JOptionPane.showMessageDialog(
			comp,
			ControlMain.getProperty("err_noConnection"),
			ControlMain.getProperty("attention"),
			JOptionPane.YES_OPTION
		);
		Logger.getLogger(loggingClass).error(ControlMain.getProperty("err_noConnection"));
	}
}

