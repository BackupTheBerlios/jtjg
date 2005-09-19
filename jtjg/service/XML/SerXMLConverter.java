/*
SerXMLConverter.java by Geist Alexander 

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
package service.XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.BOPid;
import model.BOPids;
import model.BORecordArgs;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import control.ControlMain;

public class SerXMLConverter {
    /**
	 * @param XML-Document der BOX
	 * @return BORecordArgs
	 */
	public static BORecordArgs parseRecordDocument(Document document) {
		BORecordArgs recordArgs = new BORecordArgs(false);
		Element root = document.getRootElement();
		recordArgs.setPids(new BOPids());
		
		Element command = (Element)root.selectSingleNode("//record");
		Node channelname = root.selectSingleNode("//channelname");
		Node epgtitle = root.selectSingleNode("//epgtitle");
		Node channelId = root.selectSingleNode("//id");
		Node epgInfo1 = root.selectSingleNode("//info1");
		Node epgInfo2 = root.selectSingleNode("//info2");
		Node epgid = root.selectSingleNode("//epgid");
		Node mode = root.selectSingleNode("//mode");
		Node videopid = root.selectSingleNode("//videopid");
		Element selectedAudiopid = (Element)root.selectSingleNode("//audiopids selected");
		List aPidNodes = selectedAudiopid.selectNodes("//audio");
		Node vtxtpid = root.selectSingleNode("//vtxtpid");
		
		recordArgs.setCommand(command.attributeValue("command"));
		recordArgs.setSenderName(channelname.getStringValue());
		recordArgs.setEpgTitle(epgtitle.getText());
		recordArgs.setEventId(channelId.getText());
		recordArgs.setEpgInfo1(epgInfo1.getText());
		recordArgs.setEpgInfo2(epgInfo2.getText());
		recordArgs.setEpgId(epgid.getText());
		recordArgs.setMode(mode.getText());
		recordArgs.getPids().setVPid(new BOPid(Integer.toHexString(Integer.parseInt(videopid.getText())), "video", 0));
		if (ControlMain.getSettingsRecord().isRecordVtxt()) {
			recordArgs.getPids().setVtxtPid(new BOPid(Integer.toHexString(Integer.parseInt(vtxtpid.getText())), "vtxt", 2));	
		}
		
		
		ArrayList pidList = new ArrayList();
		for( int i=0; i<aPidNodes.size(); i++ ) {
			Element aPid = (Element)aPidNodes.get(i);
			String number = Integer.toHexString(Integer.parseInt(aPid.attributeValue("pid")));
			String name = aPid.attributeValue("name");
			BOPid pid = new BOPid(number, name, 1);
			pidList.add(pid);
		}
		recordArgs.getPids().setAPids(pidList);
		try {
            recordArgs.getPids().setPmtPid(ControlMain.getBoxAccess().getPids().getPmtPid());
        } catch (IOException e) {}
		recordArgs.loadLocalTimer();
		return recordArgs;
	}
}
