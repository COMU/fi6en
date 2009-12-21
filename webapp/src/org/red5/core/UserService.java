package org.red5.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectListener;
import org.slf4j.Logger;

public class UserService extends MultiThreadedApplicationAdapter{
	
	Logger log= Red5LoggerFactory.getLogger(UserService.class,"fi6en");
	ArrayList<String> usernameArraylist;
	IConnection conn;
	IScope appScope;
	ISharedObject sharedObjectUserslist;
	public UserService() {
		try {
			/*Arraylist to store the connected client's usernames's*/
			usernameArraylist= new ArrayList<String>();
			conn= Red5.getConnectionLocal();
		}
		catch (Exception e) {
			// TODO: handle exception
			log.error("Exception in "+UserService.class.getName()+e);
		}
	}
	
	public void writeMessage(String message) {
		try {
		appScope= conn.getScope();
		
		ISharedObjectListener userListener= new MyCustomListener();
		createSharedObject(appScope, "usersList", true);
		sharedObjectUserslist= getSharedObject(appScope, "usersList");
		
		log.info("userlist message : "+message);
		}
		catch (Exception e) {
			// TODO: handle exception
			log.error("exception in "+UserService.class.getName(),e);
		}
	}
	
/*public class UserService extends MultiThreadedApplicationAdapter{
	
	Logger log= Red5LoggerFactory.getLogger(UserService.class,"fi6en");
	ArrayList<String> usernameArraylist;
	IConnection conn= Red5.getConnectionLocal();
	IScope appScope= conn.getScope();
	ISharedObject sharedObjectUserslist;
	public UserService() {
		try {
			//Arraylist to store the connected client's usernames's
			usernameArraylist= new ArrayList<String>();
			conn= Red5.getConnectionLocal();
			appScope= conn.getScope();
			createSharedObject(appScope, "usersList", true);
			sharedObjectUserslist= getSharedObject(appScope, "usersList");
		}
		catch (Exception e) {
			// TODO: handle exception
			log.error("Exception in"+UserService.class.getName()+e);
		}
	}
	
	public void setSharedObjectUserslist(ISharedObject sharedObjectUserslist) {
		this.sharedObjectUserslist = sharedObjectUserslist;
	}

	public void sendToSharedObject () {
		sharedObjectUserslist.sendMessage("addToUserlist",usernameArraylist);
	}

	*/
	/*sends the connected usernames to the client method
	public void sendUsername(String param) {
		//IConnection conn= Red5.getConnectionLocal();
		//appScope= conn.getScope();
		log.info("send username method ");
		usernameArraylist.add(param);
    	ISharedObject so = get(appScope, "chat");
    	so.sendMessage("receiveUsername",usernameArraylist);
    	Iterator<String> iterator= usernameArraylist.iterator();
    	
    	while( iterator. hasNext() ) {
    		System.out.println("param : "+iterator.next());
    	}
    }*/
	
	/*public void deleteUsername(String param) {
		Iterator<String> iterator= usernameArraylist.iterator();
    	while( iterator. hasNext() ) {
    		if (iterator.next().equals(param));
    		System.out.println("disconnected username is : "+param);
    		return ;
    	}
	}*/
	
	/*
	public void updateUserlist(List<String> user) {
		try {
			IConnection conn= Red5.getConnectionLocal();
			appScope= conn.getScope();
			sharedObjectUserlist= getSharedObject(appScope, "userlist");
			log.info(user.toString());
			log.info("writing"+user.toString()+"to the user list");
			sharedObjectUserlist.sendMessage("updateUsers", user);
		}
		catch (Exception e) {
			// TODO: handle exception
			log.error("exception : "+e);
		}		
	}
	*/
	
}
