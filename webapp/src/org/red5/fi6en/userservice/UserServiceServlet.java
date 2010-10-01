package org.red5.fi6en.userservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.red5.fi6en.core.Application;
import org.red5.fi6en.util.HibernateUtil;
import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

public class UserServiceServlet extends HttpServlet {
	
	static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static Logger log = Red5LoggerFactory.getLogger(Application.class,"fi6en");
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		StringBuilder sb = new StringBuilder();
		sb.append("<users>");
		Random r = new Random();
		String[] ss = {"kozdincer","kaan","osman","sedat","orcun","mali","hakan","volkan"};
		
		
		for (int i=0; i<r.nextInt(20); i++) {
			int a = r.nextInt(7);
			sb.append("<user>");
			sb.append("<id>" + "1" + "</id>");
			sb.append("<username>" + ss[a] + "</username>");
			sb.append("</user>");
		}
		sb.append("</users>");
		
		out.print(sb.toString());

		

	}

}