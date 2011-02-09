package org.red5.fi6en.listeners;

import java.util.ArrayList;
import java.util.List;

import org.red5.fi6en.core.SharedObjectListener;
import org.red5.fi6en.userservice.UserService;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.ScopeUtils;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectBase;
import org.red5.server.api.so.ISharedObjectService;
import org.slf4j.Logger;
/**
 * listener class for the user shared object
 * @author cem (cemosonmez@gmail.com)
 *
 */
public class UserSharedObjectListener extends SharedObjectListener {
	Logger log = Red5LoggerFactory.getLogger(UserSharedObjectListener.class,
			"fi6en");

	@Override
	public void onSharedObjectConnect(ISharedObjectBase so) {
		log.info("UserSharedObjectListener connect");
		super.onSharedObjectConnect(so);
	}
	
	@Override
	public void onSharedObjectDisconnect(ISharedObjectBase so) {
		log.info("UserSharedObjectListener disconnect");
		this.removeUsernameFromClient();
	}

	public boolean removeUsernameFromClient() {
		log.info("user shared object : remove Client method");
		try {
			IConnection conn = Red5.getConnectionLocal();
			IScope scope = conn.getScope();
			ISharedObject sharedObject = this
					.getSharedObject(scope, "userlist");
			String username = scope
					.getStringAttribute(conn.getClient().getId());
			//getting usersevice.service bean from red5-web.xml
			UserService service = (UserService) scope.getContext().getBean(
					"userservice.service");
			service.removeUsername(username);
			log.info("so User : {}", username);
			List<String> list = new ArrayList<String>();
			list.add(username);
			//sending current username to remove it from the username list on flash application			
			sharedObject.sendMessage("removeFromUserlist", list);
			return true;
		} catch (Exception e) {
			log.error(
					"exception while removing username from the user list {}",
					e.getMessage());
			return false;
		}
	}

	/**
	 * 
	 * @param scope
	 *            scope has the shared object
	 * @param soName
	 *            shared object name
	 * @return {@link ISharedObject} or null
	 */
	private ISharedObject getSharedObject(IScope scope, String soName) {
		ISharedObjectService service = (ISharedObjectService) ScopeUtils
				.getScopeService(scope, ISharedObjectService.class, false);
		return service.getSharedObject(scope, soName);
	}

}
