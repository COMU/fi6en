package org.red5.fi6en.roomservice;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.red5.fi6en.core.Application;
import org.red5.fi6en.util.HibernateUtil;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.Red5;
import org.slf4j.Logger;

public class JnlpServlet extends HttpServlet {
	
	static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static Logger log = Red5LoggerFactory.getLogger(Application.class,"fi6en");
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = request.getParameter("url");
		String name = request.getParameter("name");
		String scope = request.getParameter("scope");
		
		String s = "<?xml version='1.0' encoding='utf-8'?><jnlp spec='1.0+' ><information> <title>Fi6en Screen Sharing</title> <vendor>Fi6en</vendor> <homepage>http://figen.comu.edu.tr</homepage><description>Fi6en Screen Sharing Tool.</description> <description kind='short'>Screen Sharing.</description> <offline-allowed/> </information><security><all-permissions/></security>	<resources> <j2se version='1.4+'/> <jar href='screenshare.jar'/> </resources><application-desc main-class='org.redfire.screen.ScreenShare'><argument>Ree</argument><argument>Roo</argument><argument>1935</argument><argument>Raa</argument></application-desc></jnlp>";
		s = s.replaceFirst("Ree", url);
		s = s.replaceFirst("Raa", name);
		s = s.replace("Roo", scope);
		
		
		String attachment = "inline; filename=\"temp-unique-jnlp-"
		+ System.currentTimeMillis() + ".jnlp\"";
		 
		response.setContentType("application/x-java-jnlp-file");
		response.setHeader("Cache-Control", "max-age=30");
		response.setHeader("Content-disposition", attachment);
		 
		OutputStreamWriter out = new OutputStreamWriter(response
		.getOutputStream());
		 
		out.write(s);
		out.flush();
		out.close();

	}

}