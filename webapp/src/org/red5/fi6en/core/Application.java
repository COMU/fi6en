package org.red5.fi6en.core;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectListener;
import org.slf4j.Logger;

/**
 * fi6en application main class
 * 
 * @author cem (cemosonmez@gmail.com)
 * 
 */

public class Application extends ApplicationAdapter {
	IScope appScope;
	//shared objects for main page and room
	ISharedObject sharedObjectChat, sharedObjectRoomChat;  

	private static Logger log = Red5LoggerFactory.getLogger(Application.class,
			"fi6en");

	public boolean appStart(IScope scope) {
		log.info("fi6en Videoconference application started");
		appScope = scope;
		createSharedObject(appScope, "chat", true);
		sharedObjectChat = getSharedObject(appScope, "chat");
		ISharedObjectListener listenerSOChat = new SharedObjectListener();
		sharedObjectChat.addSharedObjectListener(listenerSOChat);
		return true;
	}

	public void appStop(IScope scope) {
		log.info("fi6en Videoconference Application stopped");
		sharedObjectChat.close();
	}

	public boolean connect(IConnection conn, IScope scope, Object[] params) {
		log.info("Connected to server application - Client : "
				+ conn.getRemoteAddress());
		/*
		 * List ipList = new ArrayList(); ipList = conn.getRemoteAddresses();
		 * Iterator iterator = ipList.iterator(); while (iterator.hasNext()) {
		 * log.info("ip {}", iterator.next()); }
		 */
		return true;
	}

	public void disconnect(IConnection conn, IScope scope) {
		log.info("Application disconnect " + conn.getRemoteAddress());
		sharedObjectChat.clear();
		super.disconnect(conn, scope);
	}

	@Override
	public boolean roomStart(IScope room) {
		appScope= room;
		log.info("started room : {}",room.getName());
		createSharedObject(room, "chatRoom", true);
		sharedObjectRoomChat = getSharedObject(room, "chatRoom");
		ISharedObjectListener listenerSOChat = new SharedObjectListener();
		sharedObjectChat.addSharedObjectListener(listenerSOChat);		
		log.info("created shared object on {} scope",room.getName());
		return super.roomStart(room);
	}
	
	@Override
	public void roomStop(IScope room) {
		log.info("stopped room : {}",room.getName());
		sharedObjectRoomChat.close();
		super.roomStop(room);
	}

	@Override
	public boolean roomJoin(IClient client, IScope room) {
		log.info("joined room : {}",room.getName());
		return super.roomJoin(client, room);
	}
	
	@Override
	public void roomLeave(IClient client, IScope room) {
		log.info("left the room : {}",room.getName());
		super.roomLeave(client, room);
	}
	
	@Override
	public boolean roomConnect(IConnection conn, Object[] params) {
		log.info("connected room : {}"+conn.getScope().getName());
		return super.roomConnect(conn, params);
	}
	
	@Override
	public void roomDisconnect(IConnection conn) {
		log.info("disconnected room : {}",conn.getScope().getName());
		super.roomDisconnect(conn);
	}

}
