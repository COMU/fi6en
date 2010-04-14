<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Enumeration"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.xml.sax.InputSource"%>
<%@page import="java.io.StringReader"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Budur</title>
</head>
<body>
<%
	String mesaj = "mehaba dunya";
%>
<%=mesaj%>
<%
	try {
		Enumeration parameters = request.getParameterNames();
		DocumentBuilderFactory dbf = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(parameters.nextElement().toString()));
		Document doc = db.parse(is);
		doc.getDocumentElement().normalize();
		System.out.println("Root element "
				+ doc.getDocumentElement().getNodeName());
		NodeList nodeLst = doc.getElementsByTagName("user");
		System.out.println("User informations ");

		for (int s = 0; s < nodeLst.getLength(); s++) {

			Node fstNode = nodeLst.item(s);

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;

				NodeList fstNmElmntLst = fstElmnt
						.getElementsByTagName("firstname");
				Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
				NodeList fstNm = fstNmElmnt.getChildNodes();
				System.out.println("First Name : "
						+ ((Node) fstNm.item(0)).getNodeValue());

				NodeList lstNmElmntLst = fstElmnt
						.getElementsByTagName("lastname");
				Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
				NodeList lstNm = lstNmElmnt.getChildNodes();
				System.out.println("Last Name : "
						+ ((Node) lstNm.item(0)).getNodeValue());

				NodeList locationElmntLst = fstElmnt
						.getElementsByTagName("location");
				Element locationElmnt = (Element) locationElmntLst
						.item(0);
				NodeList location = locationElmnt.getChildNodes();
				System.out.println("Location : "
						+ ((Node) location.item(0)).getNodeValue());

				NodeList ageElmntLst = fstElmnt
						.getElementsByTagName("age");
				Element ageElmnt = (Element) ageElmntLst
						.item(0);
				NodeList age = ageElmnt.getChildNodes();
				System.out.println("Age : "
						+ ((Node) age.item(0)).getNodeValue());

			}

		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
<%
	Enumeration params = request.getParameterNames();
	//Enumeration parameters=request.getAttributeNames();
	//while (parameters.hasMoreElements()) {
	//	String parametername= parameters.nextElement().toString();
	//	System.out.println(parametername +" : "+request.getAttribute(parametername));	
	//}

	System.out.println(params.nextElement().toString());
%>

</body>
</html>