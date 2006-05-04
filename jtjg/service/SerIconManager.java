package service;
/*
 * SerIconManager.java by Geist Alexander
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
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import java.util.logging.Logger;

public class SerIconManager {
    private static SerIconManager instance = null;
    private Map icons;
    private static Logger logger;

    private SerIconManager() {
        icons = new HashMap();
    }

    public static SerIconManager getInstance() {
        if (instance == null) {
            instance = new SerIconManager();
            logger = Logger.getLogger(instance.getClass().getName());
        }
        return instance;
    }

    public ImageIcon getIcon(String key) {
        ImageIcon result = null;
        try {
            String hashtableKey = key;
            if (icons.containsKey(hashtableKey)) {
                result = (ImageIcon) icons.get(hashtableKey);
            }
            else {
                result = new ImageIcon(ClassLoader.getSystemResource("ico/"+key));
                icons.put(hashtableKey, result);
            }
        }
        catch (Exception e) {
            logger.warning("Icon " + key + " not found"+" "+e.getMessage());
        }
        return result;
    }
}