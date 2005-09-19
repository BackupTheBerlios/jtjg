package service.XML;
/*
SerXPathHandling.java by Geist Alexander 

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

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

public class SerXPathHandling {
	
	public static void insertElementAt(Element parentElement, Element newElement, int index)  {
	//setzt das Element an der angegebenen Stelle (-1) ein
	  List list = parentElement.content();
	  list.add(index-1, newElement);
	}
	
	public static void insertElement(Element parentElement, Element newElement)  {
	//setzt das Element ein
	  List list = parentElement.content();
	  list.add(newElement);
	}
		
	public static Node getFirstDescendentNode(Node node) {
		return node.selectSingleNode("descendant::node()[1]");
	}
		
	public static Node getLastDescendentNode(Node node) {
		return node.selectSingleNode("descendant::node()[last]");
	}
		
	public static Node getFirstFollowingNode(Node node) {
		return node.selectSingleNode("following-sibling::node()[1]/*");
	}
		
	public static Node getFirstPrecedingNode(Node node) {
		return node.selectSingleNode("preceding-sibling::node()[1]/*");
	}
	
	public static List getDescendentNodes(Node node) {
		return node.selectNodes("descendant::*");
	}
	
	public static List getNodes(String xPathExpreassion, Document document) {
		return document.selectNodes(xPathExpreassion);
	}
}
