package org.red5.fi6en.roomservice;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.red5.fi6en.userservice.User;
import org.red5.fi6en.userservice.UserStatus;
import org.red5.fi6en.util.HibernateUtil;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.ScopeUtils;
import org.red5.server.api.service.IServiceCapableConnection;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectService;
import org.slf4j.Logger;

public class RoomService {
	
	public RoomService() {
	}
	
	Logger log= Red5LoggerFactory.getLogger(RoomService.class,"fi6en");
	static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	public void addRoom(String name, String hashpasswd, Boolean isPublic) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Room room = new Room();
			room.setName(name);
			room.setComment("no comment");
			room.setStarttime(new Timestamp(Calendar.getInstance().getTime().getTime()));
			room.setFinishtime(null);
			room.setIs_public(isPublic);
			room.setIs_open(true);
			room.setHashpasswd(hashpasswd);
			session.save(room);
			tx.commit();
			log.info("Room Added -> Name: " + name);
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for room : "
					+ e.getMessage());
		} finally {
			session.close();
		}
		
		//Client Invoke.
		refreshClientRoomList("roomlist");
		
		
		
	}
	

	public void deleteRoom(String name) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			//Set roomname in userstatus table
			Query q = session.createQuery("UPDATE Room SET is_open = :isOpen, is_public = :isPublic, finishtime = :finishTime WHERE name = :name");
			q.setParameter("isOpen", false);
			q.setParameter("isPublic", false);
			q.setParameter("finishTime", new Timestamp(Calendar.getInstance().getTime().getTime()));
			q.setParameter("name", name);
			q.executeUpdate();
			log.info("Room Deleted -> Name: " + name);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for adding user to room : "
					+ e.getMessage());
		} finally {
			session.close();
		}
		
		//Client Invoke.
		refreshClientRoomList("roomlist");
		
		
	}
	
	public void addUserToRoom(String userName, String roomName){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			//Set roomname in userstatus table
			Query q = session.createQuery("Update UserStatus set roomname = :roomName, client_id = :clientID, is_online = :isOnline where username = :userName");
			q.setParameter("roomName", roomName);
			q.setParameter("clientID", Long.parseLong(Red5.getConnectionLocal().getClient().getId()));
			q.setParameter("isOnline", true);
			q.setParameter("userName", userName);
			q.executeUpdate();
			log.info("User Added -> " + userName + " To Room -> " + roomName);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for adding user to room : "
					+ e.getMessage());
		} finally {
			session.close();
		}
	}
	
	public void kickUser(String userName, String roomName) {
		log.info("User Kicked -> " + userName + " From Room -> " + roomName);
	}
	
	public void getRooms() {
		log.info("Rooms Listed");
	}
	
	public void getUsers(String roomName) {
		log.info("Users Listed");
	}
	
	public void connectUser(String userName) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			//Set roomname in userstatus table
			Query q = session.createQuery("Update UserStatus set roomname = :roomName, client_id = :clientID, is_online = :is where username = :userName");
			q.setParameter("roomName", "");
			q.setParameter("is", true);
			q.setParameter("clientID", Long.parseLong(Red5.getConnectionLocal().getClient().getId()));
			q.setParameter("userName", userName);
			q.executeUpdate();
			log.info("User Connected: " + userName);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for adding user to room : "
					+ e.getMessage());
		} finally {
			session.close();
		}
		
	}
	
	public void disconnectUser(long client_id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			//Set roomname in userstatus table
			Query q = session.createQuery("UPDATE UserStatus SET roomname = :roomName, client_id = :clientID, is_online = :isOnline WHERE client_id = :c");
			q.setParameter("roomName", "");
			q.setParameter("clientID", null);
			q.setParameter("isOnline", false);
			q.setParameter("c", client_id);
			q.executeUpdate();
			log.info("User DisConnected: " + client_id);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for adding user to room : "
					+ e.getMessage());
		} finally {
			session.close();
		}
		
	}
	public boolean roomAuth(String roomname, String hashpasswd) {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Room.class);
		criteria.add(Restrictions.eq("name", roomname));
		criteria.add(Restrictions.eq("hashpasswd", hashpasswd));
		List rooms = criteria.list();
		if (rooms.size() > 0) return true;
		else return false;
	}
	
	private ISharedObject getSharedObject(IScope scope, String soName) {
		ISharedObjectService service = (ISharedObjectService) ScopeUtils
				.getScopeService(scope,
						ISharedObjectService.class,
						false);
		return service.getSharedObject(scope, soName);
	}
	
	public void refreshClientRoomList(String soName) {
		//ISharedObject so = this.getSharedObject(Red5.getConnectionLocal().getScope(), soName);
		//List<String> args = new ArrayList<String>();
		//args.add("Server ");
		//so.sendMessage("serverRefresh", args);
		IConnection conn= Red5.getConnectionLocal();
		Object[] user  = new Object[]{""};
		IServiceCapableConnection service = (IServiceCapableConnection)conn;
		service.invoke("serverRefreshRoomList", user);
	}

}
