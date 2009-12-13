package org.red5.core;

import java.util.ArrayList;
import java.util.Iterator;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectListener;
import org.slf4j.Logger;

public class Application extends ApplicationAdapter{
		String message="";
		IScope appScope;
		ISharedObject sharedObjectChat, sharedObjectUserlist;
		ArrayList<String> arraylistUsers= new ArrayList<String>();
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
		createSharedObject(appScope, "userlist", true);
		sharedObjectChat = getSharedObject(appScope, "chat");
		ISharedObjectListener listener = new MyCustomListener();
		sharedObjectChat.addSharedObjectListener(listener);
		sharedObjectUserlist.addSharedObjectListener(listener);		
		return true;
	}
	public void disconnect (IConnection conn, IScope scope) {
		log.info("Application disconnect "+conn.getRemoteAddress());
		sharedObjectChat.clear();
		sharedObjectUserlist.clear();
		super.disconnect(conn, appScope);
	}
	
	public IScope getScope() {
		return this.appScope;
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
	
	public String getStreamName(String stream) {
		System.out.println("server side method "+stream);
		//log.info("info mesajı");
		IConnection conn= Red5.getConnectionLocal();
		//log.debug("Stream for : " +conn.getScope().getContextPath());
		ArrayList<String> streamList = new ArrayList<String>();
		streamList= (ArrayList<String>)this.getBroadcastStreamNames(conn.getScope());
		Iterator iterator= streamList.iterator();
		while(iterator.hasNext()) {
			try {
				String streamName= iterator.next().toString();
				//System.out.println(streamName);
				if (streamName!=stream) {
					System.out.println(streamName);
					return streamName;
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				//log.error("error with the stream",e);
			}
		}
		return "";
	}
}
