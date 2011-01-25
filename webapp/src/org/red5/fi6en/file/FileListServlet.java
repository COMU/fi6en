package org.red5.fi6en.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.red5.fi6en.util.HibernateUtil;
import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileListServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static Logger log = Red5LoggerFactory.getLogger(FileListServlet.class,"sodeneme");
	
	static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	protected void service(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		
		try {
			PrintWriter out = response.getWriter();
			out.print(getFiles("oda"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public String getFiles(String scopeName){
		
		StringBuilder sb = new StringBuilder();
		
		//Select * from files
		Session session = sessionFactory.openSession();
		String sql = "select * from files";
		
		SQLQuery query = session.createSQLQuery(sql);
        query.addEntity("filebean", FileBean.class);
        List results = query.list();
        Iterator iter = results.iterator();
		
		sb.append("<files>");
		while (iter.hasNext()) {
			FileBean f = (FileBean) iter.next();
			
			sb.append("<file>");
			
			sb.append("<fname>" + f.getFname() + "</fname>");
			sb.append("<rname>" + f.getRname() + "</rname>");
			sb.append("<typ>" + f.getType() + ".png" + "</typ>");
			sb.append("<type>" + f.getType() + "</type>");
			sb.append("<owner>" + f.getOwner() + "</owner>");
			sb.append("<date>" + f.getDate() + "</date>");
			
			sb.append("</file>");
		}
		sb.append("</files>");
		
		
			
		return sb.toString();
	}
}