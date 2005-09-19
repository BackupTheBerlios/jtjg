/*
Record.java by Geist Alexander 

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
package streaming;

import java.io.File;
import java.util.ArrayList;

public abstract class Record {
    public abstract void start();
    public abstract void stop();
    public abstract DataWriteStream[] getWriteStream();
    public String streamType;
    
    public ArrayList getFiles() {
    	ArrayList fileList = new ArrayList();
        for (int i=0; i<getWriteStream().length; i++) {
            ArrayList streamfileList = getWriteStream()[i].fileList;
            for (int i2=0; i2<streamfileList.size(); i2++) {
            	fileList.add( ((File)streamfileList.get(i2)));
            }
        }
        
        return fileList;
    }
}
