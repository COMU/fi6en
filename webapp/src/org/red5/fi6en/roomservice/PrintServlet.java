package org.red5.fi6en.roomservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

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
import org.slf4j.Logger;

public class PrintServlet extends HttpServlet {

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		
		String msg = request.getParameter("msg");

		PrintWriter out = response.getWriter();
		out.print(msg);

		

	}

}