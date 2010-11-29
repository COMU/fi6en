package org.red5.fi6en.roomservice;

import java.io.IOException;
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

public class RoomGetServlet extends HttpServlet {
	
	static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static Logger log = Red5LoggerFactory.getLogger(Application.class,"fi6en");
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		String roomname = request.getParameter("roomname");
		if (roomname == null) roomname = "";
		
		StringBuilder sb = new StringBuilder();
		
		//Select * from files
		Session session = sessionFactory.openSession();
		String sql = "select * from rooms WHERE name = '" + roomname + "'";
		
		SQLQuery query = session.createSQLQuery(sql);
        query.addEntity("rooms", Room.class);
        List results = query.list();
        Iterator iter = results.iterator();
		
		
		sb.append("<room>");
		
			Room u = (Room) iter.next();
			sb.append("<id>" + u.getId() + "</id>");
			sb.append("<name>" + u.getName() + "</name>");
			sb.append("<comment>" + u.getComment() + "</comment>");
			sb.append("<finishtime>" + u.getFinishtime() + "</finishtime>");
			sb.append("<starttime>" + u.getStarttime() + "</starttime>");
			sb.append("<public>" + u.isIs_public() + "</public>");
			sb.append("<open>" + u.isIs_open() + "</open>");
			sb.append("<hash>" + u.getHashpasswd() + "</hash>");
		
		
		
		sb.append("</room>");

		PrintWriter out = response.getWriter();
		out.print(sb.toString());

	}

}