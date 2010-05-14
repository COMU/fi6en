package org.red5.fi6en.core;

import org.red5.fi6en.listeners.ChatSharedObjectListener;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.so.ISharedObject;
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
	ISharedObject sharedObjectChat, sharedObjectRoomChat, sharedObjectRoomList;  

	private static Logger log = Red5LoggerFactory.getLogger(Application.class,
			"fi6en");

	public boolean appStart(IScope scope) {
		log.info("fi6en Videoconference application started");
		appScope = scope;
		createSharedObject(appScope, "chat", true);
		sharedObjectChat = getSharedObject(appScope, "chat");
		ChatSharedObjectListener listenerSOChat = new ChatSharedObjectListener();
		sharedObjectChat.addSharedObjectListener(listenerSOChat);
		
		createSharedObject(appScope, "roomlist", false);
		sharedObjectRoomList= getSharedObject(appScope, "roomlist");
		SharedObjectListener listenerSORoomList = new SharedObjectListener();
		sharedObjectRoomList.addSharedObjectListener(listenerSORoomList);		
		return true;
	}

	public void appStop(IScope scope) {
		log.info("fi6en Videoconference Application stopped");
		sharedObjectChat.close();
		sharedObjectRoomList.close();
	}

	public boolean connect(IConnection conn, IScope scope, Object[] params) {
		log.info("Connected to server application - Client : "
				+ conn.getRemoteAddress());
		return true;
	}

	public void disconnect(IConnection conn, IScope scope) {
		try {
			log.info("Application disconnect " + conn.getRemoteAddress());
			sharedObjectChat.clear();
			sharedObjectRoomList.clear();
		} catch (Exception e) {
			log.error("Error while disconnecting : {}",e.getMessage());
		}
			super.disconnect(conn, scope);
	}

	@Override
	public boolean roomStart(IScope room) {
		log.info("started room : {}",room.getName());
		try {
			createSharedObject(room, "chatRoom", true);
			sharedObjectRoomChat = getSharedObject(room, "chatRoom");
			SharedObjectListener listenerSOChatRoom = new SharedObjectListener();
			sharedObjectRoomChat.addSharedObjectListener(listenerSOChatRoom);
		} catch (Exception e) {
			log.error("error while creating shared object on {} : ",room.getName(),e.getMessage());
		}		
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
		if (!hasSharedObject(room, "chatRoom")) {
			log.info("there is no shared object available : {}, creating one..", room.getName());
			createSharedObject(room, "chatRoom", true);			
			sharedObjectRoomChat = getSharedObject(room, "chatRoom");
			SharedObjectListener listenerSOChatRoom = new SharedObjectListener();
			sharedObjectRoomChat.addSharedObjectListener(listenerSOChatRoom);			
		}
		else 
			log.info("chatRoom shared object is available : {}", room.getName());
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
