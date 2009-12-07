package org.red5.core;

import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.service.IServiceCapableConnection;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectListener;
import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.red5.server.service.ServiceInvoker;
import org.red5.server.stream.ClientBroadcastStream;

public class Application extends ApplicationAdapter{
		//StreamManager streamManager= new StreamManager();
		String message="";
		private static final Logger log = LoggerFactory.getLogger(Application.class);
		

	public boolean appStart (IScope scope) {
		
		//System.out.println(scope);
		log.debug("Videoconference application started");
		System.out.println("Videoconference application started");
		createSharedObject(scope, "chat", true);
		
		ISharedObject so = getSharedObject(scope, "chat");		
		ISharedObjectListener listener = new MyCustomListener();
		
		so.addSharedObjectListener(listener);
		so.setAttribute(message, "zz");
		
		System.out.println(so.getName());
		System.out.println(so.getPath());
		System.out.println(so.getClass());
		
		return true;
	}
	
	public void appStop (IScope scope) {
		log.info("Videoconference Application stopped");
		System.out.println("Videoconference Application stopped");
		
	}
	public boolean connect (IConnection conn, IScope scope, Object []params) {
		log.debug("Connected to server application - Client : "+ conn.getRemoteAddress());
		System.out.println("Connected to server application - Client : "+ conn.getRemoteAddress());
		return true;
	}
	public void disconnect (IConnection conn, IScope scope) {
		log.debug("Application disconnect",conn.getRemoteAddress());
		System.out.println("Application disconnect"+conn.getRemoteAddress());
		super.disconnect(conn, scope);
	}
	
	public boolean roomStart(IScope scope) {
		  if (!super.roomStart(scope))
		      return false;

		  createSharedObject(scope, "chat", true);
		  ISharedObject so = getSharedObject(scope, "chat");
		  
		  System.out.println("Oda oluşturuldu"+scope.getContextPath());
		  System.out.println(so.getName());
		  System.out.println("shared object");

		  // Şimdi paylaşılan nesneyle birşeyler yapabilirsiniz...

		  return true;
	}
	public boolean roomConnect (IConnection conn, Object[] params) {
		log.debug("NetConnection attempt from ...",conn.getRemoteAddress());
		return super.roomConnect(conn, params);
	}
	
	public void roomDisconnect(IConnection conn) {
		log.debug("Connection closed by ...", conn.getRemoteAddress());
		super.roomDisconnect(conn);
	}
	
	public void showStreamNames(String param) {
		System.out.println("server side method"+param);
		log.info("info mesajı");
		IConnection conn= Red5.getConnectionLocal();
		
		log.debug("Stream for : " +conn.getScope().getContextPath());
		ArrayList<String> stream = new ArrayList<String>();
		stream= (ArrayList<String>)this.getBroadcastStreamNames(conn.getScope());
		Iterator iter= stream.iterator();
		while(iter.hasNext()) {
			try {
				System.out.println(iter.next());
			}
			catch (Exception e) {
				// TODO: handle exception
				log.error("error with the stream",e);
			}
		}
		
	}
}
