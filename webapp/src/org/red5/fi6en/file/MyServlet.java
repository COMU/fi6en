package org.red5.fi6en.file;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.red5.fi6en.core.Application;
import org.red5.fi6en.util.HibernateUtil;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.service.IServiceCapableConnection;
import org.slf4j.Logger;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;


public class MyServlet extends HttpServlet {
	
	static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static Logger log = Red5LoggerFactory.getLogger(Application.class,"sodeneme");
	private String roomname = "";
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		roomname = request.getParameter("roomname");
		response.setContentType("text/html;charset=UTF-8");
		log.info("#başladı.");

		try {
			MultipartRequest multipartRequest = new MultipartRequest(request, getServletContext().getRealPath("/"), 1024 * 2048, new DefaultFileRenamePolicy()); //1MB
			//if (multipartRequest.getParameter("save") != null) {
				
				upload(request, response, multipartRequest);
			//}
			//else {
			//	throw new IOException("Display Upload Dialogue");
			//}
			
		} catch (IOException ex) {
			System.out.print(ex.toString());
		}
		
		log.info("#bitti");
		

	}

	private void upload(HttpServletRequest request,
			HttpServletResponse response, MultipartRequest multipartRequest)
			throws IOException {
		
		log.info("#upload start");
		
		
		
		
		//Get uploaded file
		//File tmpFile = multipartRequest.getFile("uploaded");
		//Move temporary file to actual destination
		
		//String newFileName = "uploaded" + tmpFile.getName().substring(tmpFile.getName().lastIndexOf('.'));
		//File fileToMove = new File(dirToMove, newFileName);
		//tmpFile.renameTo(fileToMove);
		//tmpFile.delete();
				//File information save to DB
		
		
		File uploadedFile = multipartRequest.getFile(multipartRequest.getFileNames().nextElement().toString());
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Long id = null;
		try {
			FileBean f = new FileBean();
			f.setFname(uploadedFile.getName());
			f.setRname(roomname);
			f.setType("pdf");
			f.setOwner("kozdincer");
			f.setDescription("description");
			f.setMd5("md5");
			f.setDate(null);
			id = (Long) session.save(f);
			log.info("#File information saved to database. - ID: " + id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for stream : "
					+ e.getMessage());
		} finally {
			session.close();
		}
		
		log.info("#upload finish");
		
		//File Renaming for id.
		
		uploadedFile.renameTo(new File(getServletContext().getRealPath("/") + id.toString()));
		

	}

}