package org.red5.fi6en.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.impl.CriteriaImpl.Subcriteria;
import org.red5.fi6en.listeners.ChatSharedObjectListener;
import org.red5.fi6en.traffic.Broadcast;
import org.red5.fi6en.traffic.Subscribe;
import org.red5.fi6en.userservice.User;
import org.red5.fi6en.util.HibernateUtil;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.statistics.IClientBroadcastStreamStatistics;
import org.red5.server.api.statistics.IPlaylistSubscriberStreamStatistics;
import org.red5.server.api.statistics.IStatisticsService;
import org.red5.server.api.statistics.IStreamStatistics;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IClientBroadcastStream;
import org.red5.server.api.stream.IPlaylistSubscriberStream;
import org.red5.server.api.stream.ISubscriberStream;
import org.red5.server.api.stream.ISubscriberStreamService;
import org.red5.server.stream.ClientBroadcastStream;
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

	private static Logger log = Red5LoggerFactory.getLogger(Application.class,"fi6en");

	static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
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
		final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		log.info("connection date: {}",sdf.format(cal.getTime()));
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
	@Override
	public void streamSubscriberClose(ISubscriberStream stream) {
		
		super.streamSubscriberClose(stream);
		
		
		
		//Statistics...		
		IPlaylistSubscriberStream mystream = (IPlaylistSubscriberStream) stream;
		IPlaylistSubscriberStreamStatistics istats = mystream.getStatistics();

		
		
		/*log.info("");
		log.info("********************");
		log.info("Streaming Subscriber Statistics...");
		log.info("Name: " + mystream.getName());
		log.info("Subs IP: " + stream.getConnection().getRemoteAddress());
		log.info("Creation Time: " + mystream.getStatistics().getCreationTime());
		log.info("Current Time: " + mystream.getStatistics().getCurrentTimestamp());
		log.info("ReadBytes: " + stream.getConnection().getReadBytes());
		log.info("WrittenBytes: " + stream.getConnection().getWrittenBytes());
		log.info("Stream Name: " + stream.getName());
		log.info("Stream ID: " + stream.getStreamId());
		log.info("Codec: " + stream.getCodecInfo());
		//Null Pointer exception...
		//log.info("ChannelBandwith: " + stream.getBandwidthConfigure().getChannelBandwidth());
		log.info("----->" + mystream.getStatistics().getEstimatedBufferFill());
		log.info("Connections params: " + stream.getConnection().getConnectParams());
		log.info("Client ID: " + stream.getConnection().getClient().getId());
		log.info("---");
		log.info("PublishName: " + stream.getConnection().getAttribute("pname"));
		log.info("********************");
		log.info("");*/
		log.info("#### Type: " + stream.getConnection().getType());
		
		
		
		//Statistic to Database..
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Subscribe s = new Subscribe();
			s.setIp(stream.getConnection().getRemoteAddress());
			s.setRemoteUserId(1);
			s.setDuration(istats.getCurrentTimestamp());
			s.setSize(stream.getConnection().getClientBytesRead());
			s.setDateBegin(null);
			s.setDateEnd(null);
			s.setServer_name("localhost");
			s.setClient_name(mystream.getName());
			Long streamId = (Long) session.save(s);
			log.info("Subscriber statistic saved to database. - ID: " + streamId);
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
		
		
		
		
	}
	
	
	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		super.streamBroadcastStart(stream);
		
		
	}
	
	@Override
	public void streamBroadcastClose(IBroadcastStream bstream) {
		
		super.streamBroadcastClose(bstream);
		
		ClientBroadcastStream stream = (ClientBroadcastStream) bstream;
		IClientBroadcastStreamStatistics istats = (IClientBroadcastStreamStatistics) bstream;
		
		/*//Statistics...
		IClientBroadcastStreamStatistics istats = (IClientBroadcastStreamStatistics) bstream;
		log.info("");
		log.info("*****----*****----*****");
		log.info("Stream Broadcasting Statistics...");
		log.info("Name: " + bstream.getName());
		log.info("RemoteUserID: " + istats.getPublishedName());
		log.info("IP: " + stream.getConnection().getRemoteAddress());
		log.info("Published Name:" + stream.getPublishedName());
		log.info("Creation Time: " + istats.getCreationTime());
		log.info("Current Time: " + istats.getCurrentTimestamp());
		log.info("Bytes: " + istats.getBytesReceived());
		log.info("Stream Name: " + bstream.getName());
		log.info("Codec: " + bstream.getCodecInfo());
		log.info("Stream ID: " + stream.getStreamId());
		log.info("Connections params: " + stream.getConnection().getConnectParams());
		log.info("Client ID: " + stream.getConnection().getClient().getId());
		log.info("*****----*****----*****");
		log.info("");*/
		
		//Statistic to Database..
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Broadcast b = new Broadcast();
			b.setIp(stream.getConnection().getRemoteAddress());
			b.setRemoteUserId(1);
			b.setDuration(stream.getStatistics().getCurrentTimestamp());
			b.setSize(istats.getBytesReceived());
			b.setDateBegin(null);
			b.setDateEnd(null);
			b.setServer_name("localhost");
			b.setClient_name(bstream.getPublishedName());
			Long streamId = (Long) session.save(b);
			log.info("Broadcast statistic saved to database. - ID: " + streamId);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for broadcast : "
					+ e.getMessage());
		} finally {
			session.close();
		}

	}
	
	
	
}
