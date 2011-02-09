package org.red5.fi6en.file;

import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileService {

	private static Logger log = Red5LoggerFactory.getLogger(FileService.class, "sodeneme");

	public Document getFiles(String scopeName) throws ParserConfigurationException {
		
		FileBean f = new FileBean();
		f.setDate(null);
		f.setDescription("dasdas");
		f.setFname("tuts");
		f.setId(1);
		f.setMd5("itsmd5");
		f.setOwner("kozdincer");
		f.setRname("342343");
		f.setType("pdf");
		
/////////////////////////////
        //Creating an empty XML Document
        //We need a Document
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        ////////////////////////
        //Creating the XML tree
        //create the root element and add it to the document
        Element files = doc.createElement("files");
        doc.appendChild(files);
        //create a comment and put it in the root element
        Comment comment = doc.createComment("Files at Scope: " + scopeName);
        //root.appendChild(comment);
        files.appendChild(comment);
        
        //Random
        Random r = new Random();
        int i = r.nextInt(20);
        
		if (files != null) {
			for (int y=0; i<y; i++) {
				Element video = doc.createElement("file");
				video.setAttribute("fname", f.getFname());
				video.setAttribute("rname", f.getRname());
				video.setAttribute("typ", f.getType() + ".png");
				video.setAttribute("type", f.getType());
				video.setAttribute("owner", f.getOwner());
				video.setAttribute("date", "12.23.2333");
				video.appendChild(video);
			}
			return doc;
		}
		log.info("#####null döndü..");
		return null;
		
		
	}
	
	
}
