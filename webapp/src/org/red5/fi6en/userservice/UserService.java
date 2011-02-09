package org.red5.fi6en.userservice;

import java.util.ArrayList;
import java.util.Iterator;
import org.red5.components.ClientManager;
import org.red5.fi6en.listeners.UserSharedObjectListener;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.ScopeUtils;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectService;
import org.slf4j.Logger;

/**
 * class that manages the user operations 
 * 
 * @author cem (cemosonmez@gmail.com)
 * @version 0.2 10.02.2010
 */

public class UserService {
	
	Logger log= Red5LoggerFactory.getLogger(UserService.class,"fi6en");
	ClientManager clientMgr= new ClientManager("userlist", true);
	
	ArrayList<String> arraylistUsers;
	IScope appScope;
	ISharedObject sharedObjectUserslist;
	UserSharedObjectListener listenerSOUsers;
	public UserService() {
			/*Arraylist to store the connected client's usernames's*/
			arraylistUsers= new ArrayList<String>();
			listenerSOUsers= new UserSharedObjectListener();
	}	
	
	/** add the given username to the arraylist and send it to the shared object
	 * @param 
	 *     username parameter will be added to the arraylist
	 */	
	public void writeUsername(String username) {
		log.info("writeUsername method :"+username);
		
		IConnection conn= Red5.getConnectionLocal();
		appScope= conn.getScope();
		//creating scope attribute for the client and sets it with the incoming username
		appScope.setAttribute(conn.getClient().getId(), username);
		
		this.createSharedObject(appScope,"userlist",false);
		sharedObjectUserslist = this.getSharedObject(appScope,"userlist");
		sharedObjectUserslist.addSharedObjectListener(listenerSOUsers);
		
		String uname= username;
		String uid= conn.getClient().getId();
		clientMgr.addClient(appScope, uname, uid);
		arraylistUsers.add(uname);
		
		//writing connected usernames
		Iterator<String> iterator= arraylistUsers.iterator();
		while (iterator.hasNext()) {
			log.info("username :"+ iterator.next());
		}
		
		//log.info("array : "+arraylistUsers.toString());
		sharedObjectUserslist.setAttribute("arrayUserlist", arraylistUsers);
		log.info("shared object arraylist :"+sharedObjectUserslist.getAttribute("arrayUserlist"));		
	}
	
	/**
	 * method to delete username from the username list
	 * 
	 * @param username username
	 */
	public void removeUsername (String username) {
		log.info("removeUsername method : "+username);
		if (arraylistUsers.remove(username)) {
			log.info(username+" removed from the arraylist");	
		}
		else {
			log.info("no username found to delete");
		}
	}
	
	/**
	 * @param
	 * 		scope scope which has the shared object
	 * @param
	 *		soName shared object name   
	 *    
	 * @return
	 *    return the shared object that will be used      	
	 */	
	private ISharedObject getSharedObject(IScope scope, String soName) {
		ISharedObjectService service = (ISharedObjectService) ScopeUtils
				.getScopeService(scope,
						ISharedObjectService.class,
						false);
		return service.getSharedObject(scope, soName);
	}
	
	
	/**
	 * @param
	 * 		scope shared object will be created on the given scope
	 * @param
	 * 		soName shared object name
	 * @param
	 * 		persistent persistency   
	 */
	private void createSharedObject (IScope scope, String soName, boolean persistent) {
		ISharedObjectService service= (ISharedObjectService) ScopeUtils
				.getScopeService(scope, 
						ISharedObjectService.class,
						false);
		service.createSharedObject(scope, soName, persistent);
	}
	/**
	 * 
	 * @return arraylist for the usernames
	 */
	public ArrayList<String> getUserlist() {
		return arraylistUsers;
	}
	
	public void clearSharedObject(IScope scope,String soName) {
		ISharedObjectService service= (ISharedObjectService) ScopeUtils
		.getScopeService(scope, 
				ISharedObjectService.class,
				false);
		service.clearSharedObjects(scope, soName);
	}

	
}

