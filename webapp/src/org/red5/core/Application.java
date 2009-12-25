package org.red5.core;


import java.util.ArrayList;
import java.util.List;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.service.ServiceUtils;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectListener;
import org.red5.server.service.ServiceInvoker;
import org.slf4j.Logger;

/**
 * fi6en application main class
 * 
 * @author cem (cemosonmez@gmail.com)
 * 
 * @version $Revision$ $Date$
 */

public class Application extends ApplicationAdapter{
		IScope appScope;
		ISharedObject sharedObjectChat;
		private static Logger log = Red5LoggerFactory.getLogger(Application.class,"fi6en");
		
	public boolean appStart (IScope scope) {
		log.info("Videoconference application started");
		return true;
	}

	public void appStop (IScope scope) {
		log.info("Videoconference Application stopped");		
	}
	
	public boolean connect (IConnection conn, IScope scope, Object []params) {
		log.info("Connected to server application - Client : "+ conn.getRemoteAddress());
		appScope= scope;	
		createSharedObject(appScope, "chat", true);		
		sharedObjectChat = getSharedObject(appScope, "chat");		
		ISharedObjectListener listenerSOChat = new SharedObjectListener();
		sharedObjectChat.addSharedObjectListener(listenerSOChat);		
		return true;
	}
	public void disconnect (IConnection conn, IScope scope) {
		log.info("Application disconnect "+conn.getRemoteAddress());
		sharedObjectChat.clear();
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
		//log.debug("NetConnection attempt from ...",conn.getRemoteAddress());
		return super.roomConnect(conn, params);
	}
	
	public void roomDisconnect(IConnection conn) {
		//log.debug("Connection closed by ...", conn.getRemoteAddress());
		super.roomDisconnect(conn);
	}
	
}
