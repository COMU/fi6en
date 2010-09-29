package org.red5.fi6en.roomservice;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.red5.fi6en.userservice.UserStatus;
import org.red5.fi6en.util.HibernateUtil;
import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

public class RoomService {
	
	public RoomService() {
	}
	
	Logger log= Red5LoggerFactory.getLogger(RoomService.class,"fi6en");
	static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	public void addRoom(String name) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Room room = new Room();
			room.setName(name);
			room.setComment("no comment");
			room.setStarttime(new Timestamp(Calendar.getInstance().getTime().getTime()));
			room.setFinishtime(null);
			room.setIs_public(true);
			room.setIs_open(true);
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
		
		
		
	}
	
	public void deleteRoom(String name) {
		log.info("Room Deleted -> Name: " + name);
	}
	
	public void addUserToRoom(String userName, String roomName){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			//Set roomname in userstatus table
			Query q = session.createQuery("Update UserStatus set roomname = :roomName where username = :userName");
			q.setParameter("roomName", roomName);
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
	
	public void deleteUserFromRoom(String userName, String roomName) {
		log.info("User Deleted -> " + userName + " From Room -> " + roomName);
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
		log.info("User Connected: " + userName);
	}
	
	public void disconnectUser(String userName) {
		log.info("User Disconnected: " + userName);
		
	}
	

}
