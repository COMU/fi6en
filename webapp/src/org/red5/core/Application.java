package org.red5.core;

import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application extends ApplicationAdapter{
		/*Arraylist to store the connected client's usernames's*/
		ArrayList<String> usernameArraylist= new ArrayList<String>();
		String message="";
		IScope appScope;

	public boolean appStart (IScope scope) {
		System.out.println("Videoconference application started");
		return true;
	}
	
	public void appStop (IScope scope) {
		System.out.println("Videoconference Application stopped");
		
	}
	public boolean connect (IConnection conn, IScope scope, Object []params) {
		System.out.println("Connected to server application - Client : "+ conn.getRemoteAddress());
		appScope= scope;
		createSharedObject(appScope, "chat", true);
		ISharedObject so = getSharedObject(appScope, "chat");
		ISharedObjectListener listener = new MyCustomListener();
		so.addSharedObjectListener(listener);
		return true;
	}
	public void disconnect (IConnection conn, IScope scope) {
		System.out.println("Application disconnect"+conn.getRemoteAddress());
		ISharedObject so = getSharedObject(appScope,"chat");
		so.clear();
		super.disconnect(conn, scope);
	}
	
	/*sends the connected usernames to the client method*/
	public void sendUsername(String param) {
		usernameArraylist.add(param);
    	ISharedObject so = getSharedObject(appScope, "chat");
    	so.sendMessage("receiveUsername",usernameArraylist);
    	Iterator<String> iterator= usernameArraylist.iterator();
    	while( iterator. hasNext() ) {
    		System.out.println("param : "+iterator.next());
    	}
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
	
	public String getStreamName(String stream) {
		System.out.println("server side method "+stream);
		//log.info("info mesajı");
		IConnection conn= Red5.getConnectionLocal();
		log.debug("Stream for : " +conn.getScope().getContextPath());
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
				log.error("error with the stream",e);
			}
		}
		return "";
	}
}
