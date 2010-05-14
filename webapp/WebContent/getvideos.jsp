
<%@page import="org.red5.logging.Red5LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.red5.fi6en.core.Application"%>
<%@page import="org.red5.fi6en.recording.DemoService"%>
<%@page import="javax.xml.transform.dom.DOMSource"%>
<%@page import="javax.xml.transform.stream.StreamResult"%>
<%@page import="java.io.StringWriter"%>
<%@page import="javax.xml.transform.OutputKeys"%>
<%@page import="javax.xml.transform.Transformer"%>
<%@page import="javax.xml.transform.TransformerFactory"%><%@page import="org.red5.server.api.IScope"%>
<%@page import="org.red5.server.api.Red5"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/xml; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
WebApplicationContext rootCtx = WebApplicationContextUtils.getWebApplicationContext(application);
Application myApp = (Application)rootCtx.getBean("web.handler");
IScope scope = myApp.getScope();
DemoService demoService= (DemoService)rootCtx.getBean("recordingservice.service");
%>
<%
/////////////////
//Output the XML

//set up a transformer
TransformerFactory transfac = TransformerFactory.newInstance();
Transformer trans = transfac.newTransformer();
trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
trans.setOutputProperty(OutputKeys.INDENT, "yes");

//create string from xml tree
StringWriter sw = new StringWriter();
StreamResult result = new StreamResult(sw);
DOMSource source = new DOMSource(demoService.getListOfAvailableFLVs(scope));
trans.transform(source, result);
String xmlString = sw.toString();
demoService.writeXML(xmlString);
//print xml
//System.out.println("Here's the xml:\n\n" + xmlString);

%>
<%=xmlString%>
	 