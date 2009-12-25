package org.red5.core;

import java.util.ArrayList;
import java.util.Iterator;
import org.red5.components.ClientManager;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.ScopeUtils;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectListener;
import org.red5.server.api.so.ISharedObjectService;
import org.slf4j.Logger;

/**
 * class that does the user operations 
 * 
 * @author cem (cemosonmez@gmail.com)
 * @version $Revision$ $Date$
 */

public class UserService {
	
	Logger log= Red5LoggerFactory.getLogger(UserService.class,"fi6en");
	ClientManager clientMgr= new ClientManager("clientlist", true);
	
	ArrayList<String> arraylistUsers;
	IScope appScope;
	ISharedObject sharedObjectUserslist;
	ISharedObjectListener listenerSOUsers;
	public UserService() {
			/*Arraylist to store the connected client's usernames's*/
			arraylistUsers= new ArrayList<String>();
			listenerSOUsers= new SharedObjectListener();
	}
	
	
	/** add the given username to the arraylist and send it to the shared object
	 * @param 
	 *     username parameter will be added to the arraylist
	 */
	
	
	public void writeUsername(String username) {
		log.info("writeUsername method :"+username);
		
		IConnection conn= Red5.getConnectionLocal();
		appScope= conn.getScope();
		this.createSharedObject(appScope,"userlist",false);
		sharedObjectUserslist = this.getSharedObject(appScope,"userlist");
		sharedObjectUserslist.addSharedObjectListener(listenerSOUsers);
		sharedObjectUserslist.clear();
		
		String uname= username;
		String uid= conn.getClient().getId();
		clientMgr.addClient(appScope, uname, uid);
		arraylistUsers.add(uname);
		
		Iterator<String> iterator= arraylistUsers.iterator();
		while (iterator.hasNext()) {
			log.info("username :"+ iterator.next());
		}
		
		log.info("array : "+arraylistUsers.toString());
		sharedObjectUserslist.setAttribute("arrayUserlist", arraylistUsers);
		log.info("shared object arraylist :"+sharedObjectUserslist.getAttribute("arrayUserlist"));		
	}
	
	/**
	 * @param
	 * 		scope the scope which has the shared object
	 * @param
	 *		soName shared object name   
	 *    
	 * @return
	 *    return the shared object to use      	
	 */
	private ISharedObject getSharedObject(IScope scope, String soName) {
		ISharedObjectService service = (ISharedObjectService) ScopeUtils
				.getScopeService(scope,
						ISharedObjectService.class,
						false);
		return service.getSharedObject(scope, soName);
	}
	
	public void removeUsername () {
		String uid= Red5.getConnectionLocal().getClient().getId();
		String username= clientMgr.removeClient(appScope, uid);
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
	 * @return arralist for the usernames
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

