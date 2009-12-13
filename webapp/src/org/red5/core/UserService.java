package org.red5.core;

import java.util.ArrayList;
import java.util.Iterator;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.so.ISharedObject;

public class UserService extends ApplicationAdapter {
	
	
	ArrayList<String> usernameArraylist;
	IScope appScope;
	public UserService() {
		/*Arraylist to store the connected client's usernames's*/
		usernameArraylist= new ArrayList<String>();
		IConnection conn= Red5.getConnectionLocal();
		appScope= conn.getScope();
	}

	
	/*sends the connected usernames to the client method*/
	public void sendUsername(String param) {
		System.out.println("send username method ");
		usernameArraylist.add(param);
    	ISharedObject so = getSharedObject(appScope, "chat");
    	so.sendMessage("receiveUsername",usernameArraylist);
    	Iterator<String> iterator= usernameArraylist.iterator();
    	
    	while( iterator. hasNext() ) {
    		System.out.println("param : "+iterator.next());
    	}
    }
	
	public void deleteUsername(String param) {
		Iterator<String> iterator= usernameArraylist.iterator();
    	while( iterator. hasNext() ) {
    		if (iterator.next().equals(param));
    		System.out.println("disconnected username is : "+param);
    		return ;
    	}
	}
}
