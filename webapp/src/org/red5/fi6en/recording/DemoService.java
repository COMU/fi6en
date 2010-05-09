package org.red5.fi6en.recording;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class DemoService {

	private static Logger log = Red5LoggerFactory.getLogger(DemoService.class, "fi6en");
	
	{
		log.info("oflaDemo DemoService created");
	}
	
	private String formatDate(Date date) {
		SimpleDateFormat formatter;
		String pattern = "dd/MM/yy";
		Locale locale = new Locale("en", "US");
		formatter = new SimpleDateFormat(pattern, locale);
		return formatter.format(date);
	}

	/**
	 * Getter for property 'listOfAvailableFLVs'.
	 *
	 * @return Value for property 'listOfAvailableFLVs'.
	 */
	//public Map<String, Map<String, Object>> getListOfAvailableFLVs() {
	public Document getListOfAvailableFLVs(IScope scope) {
		//IScope scope = Red5.getConnectionLocal().getScope();
		Map<String, Map<String, Object>> filesMap = new HashMap<String, Map<String, Object>>();
		try {
			log.debug("getting the FLV files");
			Document doc= createXML(scope.getResources("streams/*.flv"));						
			
			addToMap(filesMap, scope.getResources("streams/*.flv"));

			addToMap(filesMap, scope.getResources("streams/*.f4v"));

			addToMap(filesMap, scope.getResources("streams/*.mp3"));
			
			addToMap(filesMap, scope.getResources("streams/*.mp4"));

			addToMap(filesMap, scope.getResources("streams/*.m4a"));

			addToMap(filesMap, scope.getResources("streams/*.3g2"));			

			addToMap(filesMap, scope.getResources("streams/*.3gp"));			
		
			return doc;
		} catch (IOException e) {
			log.error("", e);
			return null;
		}
		
	}
	
	private void addToMap(Map<String, Map<String, Object>> filesMap, Resource[] files)
			throws IOException {
		if (files != null) {
			for (Resource flv : files) {
				File file = flv.getFile();
				Date lastModifiedDate = new Date(file.lastModified());
				String lastModified = formatDate(lastModifiedDate);
				String flvName = flv.getFile().getName();
				String flvBytes = Long.toString(file.length());
				if (log.isDebugEnabled()) {
					log.debug("flvName: {}", flvName);
					log.debug("lastModified date: {}", lastModified);
					log.debug("flvBytes: {}", flvBytes);
					log.debug("-------");
				}
				Map<String, Object> fileInfo = new HashMap<String, Object>();
				fileInfo.put("name", flvName);
				fileInfo.put("lastModified", lastModified);
				fileInfo.put("size", flvBytes);
				filesMap.put(flvName, fileInfo);
				
			}
		}
	}	
	
	public Document createXML(Resource[] files) {
        try {
        	/////////////////////////////
            //Creating an empty XML Document
            //We need a Document
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            ////////////////////////
            //Creating the XML tree
            //create the root element and add it to the document
            Element videos = doc.createElement("videos");
            doc.appendChild(videos);
          //create a comment and put it in the root element
            Comment comment = doc.createComment("Available videos");
            //root.appendChild(comment);
            videos.appendChild(comment);
            
        	if (files!=null) {
        		for (Resource flv: files) {
        			File file= flv.getFile();
        			String fileName= file.getName();
        			Date lastModifiedDate= new Date(file.lastModified());
        			String modifiedDate= formatDate(lastModifiedDate);
        			String fileSize= Long.toString(file.length());
        			
        			//create child element, add an attribute, and add to root
                    Element video = doc.createElement("video");
                    video.setAttribute("filename", fileName);
                    video.setAttribute("date", modifiedDate);
                    video.setAttribute("size", fileSize);
                    //video.setAttribute("name", fileName);
                    videos.appendChild(video);
                    /*
                    Element filename= doc.createElement("filename");
                    Text filenameText= doc.createTextNode(fileName);
                    
                    filename.appendChild(filenameText);
                    video.appendChild(filename);

                    Element date= doc.createElement("date");
                    Text dateText= doc.createTextNode(modifiedDate);
                    date.appendChild(dateText);
                    video.appendChild(date);
                    
                    Element size= doc.createElement("size");
                    Text sizeText= doc.createTextNode(fileSize);
                    size.appendChild(sizeText);
                    video.appendChild(size);
                    */

        		}
        		return doc;
        	}
        	else 
        		return null;
        	
            /////////////////
            //Output the XML

            //set up a transformer
            /*TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();

            //print xml
            System.out.println("Here's the xml:\n\n" + xmlString);*/

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
