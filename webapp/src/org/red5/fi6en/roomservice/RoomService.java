package org.red5.fi6en.roomservice;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.red5.server.Client;
import org.red5.server.Scope;
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
		
		//Client invokes
		refreshClientUserList("");
	}
	
	public void kickUser(String userName, String roomName) {
		IConnection conn = Red5.getConnectionLocal();
		IScope scope = conn.getScope();
		Set<IClient> clients = scope.getClients();
		Iterator<IClient> clis = clients.iterator();
		while (clis.hasNext()) {
			Long id = getUserClienID(userName);
			IClient client = clis.next();
			Long cid = Long.parseLong(client.getId());
			if (cid == id) {
				IServiceCapableConnection isc = (IServiceCapableConnection) client.getConnections().iterator().next();
				Object[]user  = new Object[]{""};
				isc.invoke("kickMe", user);
				break;
			}
		}
		
		log.info("User Kicked -> " + userName + " From Room -> " + roomName);
	}
	
	public void inviteUser(String userName, String roomName) {
		System.out.println("dsadsadasdsadas21312312");
		IScope scope = Red5.getConnectionLocal().getScope().getParent();
		Set<IClient> clients = scope.getClients();
		Long id = getUserClienID(userName);
		for (IClient client : clients) {
			Long cid = Long.parseLong(client.getId());
			if (cid == id) {
				IServiceCapableConnection isc = (IServiceCapableConnection) client.getConnections().iterator().next();
				Object[]user  = new Object[]{roomName};
				isc.invoke("inviteMe", user);
				break;
			}
		}
		
	}
	
	public long getUserClienID(String username) {
		Session session = sessionFactory.openSession();
		String sql = "select * from user_status where username = '" + username + "'";
		SQLQuery query = session.createSQLQuery(sql);
        query.addEntity("user_status", UserStatus.class);
        List results = query.list();
        Iterator iter = results.iterator();
        UserStatus u = (UserStatus) iter.next();
		return u.getClient_id();
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
			Query q = session.createQuery("Update UserStatus set roomname = :roomName, client_id = :clientID, is_online = :is, broadcast = :isBroadcasting where username = :userName");
			q.setParameter("roomName", "");
			q.setParameter("is", true);
			q.setParameter("clientID", Long.parseLong(Red5.getConnectionLocal().getClient().getId()));
			q.setParameter("userName", userName);
			q.setParameter("isBroadcasting", false);
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
		
		//Client invokes
		refreshClientUserList("");
		
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
		
		//Client invokes
		refreshClientUserList("");
		
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
	
	
	
	public void planAMeeting(String roomname, String comment, String hashpasswd, String time) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Room room = new Room();
			room.setName(roomname);
			room.setComment(comment);
			room.setStarttime(Timestamp.valueOf(time));
			room.setFinishtime(null);
			room.setIs_public(true);
			room.setIs_open(false);
			room.setHashpasswd(hashpasswd);
			session.save(room);
			tx.commit();
			log.info("Room Added (Planning Meeting) -> Name: " + roomname);
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
	public void setUserBroadcast(String username, Boolean b) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			//Set broadcast in userstatus table
			Query q = session.createQuery("Update UserStatus set broadcast = :isBroadcasting where username = :userName");
			q.setParameter("userName", username);
			q.setParameter("isBroadcasting", b);
			q.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for making user status as broadcasting : "
					+ e.getMessage());
		} finally {
			session.close();
		}
		/*Set<IClient> clients = Red5.getConnectionLocal().getScope().getClients();
		for (IClient c : clients) {
			for (IScope s : c.getScopes()) {
				System.out.println(c.getId() + " -> " + s.getName() + " : " + s.getPath() + " : " + c.getAttributes().toString()); 
			}
		}
		System.out.println(Red5.getConnectionLocal().getScope().getName());*/
		/////deneme2
		/*String myScopeName = Red5.getConnectionLocal().getScope().getName();
		Set<IClient> clients = Red5.getConnectionLocal().getScope().getClients();
		for (IClient c : clients) {
			IScope s = c.getScopes().iterator().next();
			if (s.getName() == myScopeName) {
				IConnection cc = c.getConnections().iterator().next();
				IServiceCapableConnection scc = (IServiceCapableConnection) cc;
				scc.invoke("deneme");
				System.out.println(c.getId()+" : "+s.getName()+" : ");
			}
		} */
		//Client invokes
		refreshClientUserList("");
		
		
	}
	public void setUserModerator(String username, Boolean b) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			//Set moderator in userstatus table
			Query q = session.createQuery("Update UserStatus set moderator = :isModerator where username = :userName");
			q.setParameter("userName", username);
			q.setParameter("isModerator", b);
			q.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			log.error("error during database operation for making user status as moderator : "
					+ e.getMessage());
		} finally {
			session.close();
		}
		
		//Client invokes
		refreshClientUserList("");
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
	
	public void refreshClientUserList(String none) {
		IConnection conn = Red5.getConnectionLocal();
		IClient client = conn.getClient();
		IScope scope = conn.getScope();
		String clientId = client.getId();
		String scopeName = scope.getName();
		Set<IClient> myclients = scope.getClients();
		for (IClient i: myclients) {
			try {
				String clientScopeName = i.getScopes().iterator().next().getName();
				if (clientScopeName == scopeName) {
					IServiceCapableConnection isc = (IServiceCapableConnection) i.getConnections().iterator().next();
					Object[]user  = new Object[]{""};
					isc.invoke("serverRefreshUserList", user);
				}
			} catch (Exception e) {
				continue;
			}
		}
	}
	public void refreshFileList(String none) {
		IConnection conn = Red5.getConnectionLocal();
		IClient client = conn.getClient();
		IScope scope = conn.getScope();
		String clientId = client.getId();
		String scopeName = scope.getName();
		Set<IClient> myclients = scope.getClients();
		for (IClient i: myclients) {
			try {
				String clientScopeName = i.getScopes().iterator().next().getName();
				if (clientScopeName == scopeName) {
					IServiceCapableConnection isc = (IServiceCapableConnection) i.getConnections().iterator().next();
					Object[]user  = new Object[]{""};
					isc.invoke("serverRefreshFileList", user);
				}
			} catch (Exception e) {
				continue;
			}
		}
	}
	
	public void addRoomFromAdmin(Map<String, Object> r) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			java.util.Date today = new java.util.Date();
			Timestamp t = new Timestamp(today.getTime());
			String isp = (String) r.get("ispublic");
			String iso = (String) r.get("isopen");
			Boolean p = true;
			Boolean o = true;
			if (isp == "false") p = false;
			if (iso == "false") o = false;
			
			Room room = new Room();
			room.setName((String) r.get("roomname"));
			room.setComment((String) r.get("comment"));
			room.setStarttime(t);
			room.setFinishtime(null);
			room.setIs_public(p);
			room.setIs_open(o);
			room.setHashpasswd((String) r.get("hash"));
			session.save(room);
			tx.commit();
			log.info("Room Added from admin panel -> Name: " + room.getName());
		} catch (Exception e) {
			log.info("Adding Exception: " + e.toString());
		} finally {
			session.close();
		}
		
	}
	public void changePassword(Integer id, String hash) {
		Session s = sessionFactory.openSession();
		String sql = "update Room set hashpasswd = :hash where id = :id";
		Query q = s.createQuery(sql);
		q.setString("hash", hash);
		q.setInteger("id", id);
		q.executeUpdate();
		s.close();
	}
	public void updateRoom(Integer id, Map<String, String> r) {
		//TODO Update Room Method
		System.out.print("Update Room invoke...");
	} 

}
