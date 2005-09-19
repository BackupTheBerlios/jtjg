package service.XML;
/*
SerXMLHandling.java by Geist Alexander 

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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


public class SerXMLHandling {
	
	
	
	public static Document createEmptyTimerFile(File path) throws IOException {
	    Document doc = DocumentHelper.createDocument();
	    Element root = doc.addElement( "timerList" );
		saveXMLFile(path, doc);
		return doc;
	}
	
	public static Document createEmptyMovieguideFile() {
        Document doc = DocumentHelper.createDocument();
        doc.addElement("movieguide");
        return doc;
    }
	
	/**
	 * @param parentElement
	 * @param childElementName
	 * @param childElementValue
	 */
	public static void setElementInElement(Element parentElement, String childElementName, String childElementValue) {
		Element element = DocumentHelper.createElement(childElementName);
		element.setText(childElementValue);
		parentElement.add(element);
	}
		
	public static void saveXMLFile(File path, Document doc ) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint(); 
		XMLWriter writer = new XMLWriter( new FileWriter( path ), format );
		writer.write( doc );
		writer.close();
	}
	
	public static Document readDocument(File path)  throws DocumentException, MalformedURLException {
		SAXReader reader = new SAXReader();  
		Document doc = reader.read(path);
		return doc;
	}
}
