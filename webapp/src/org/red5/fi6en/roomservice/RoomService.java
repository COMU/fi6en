package org.red5.fi6en.roomservice;

import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

public class RoomService {
	
	Logger log= Red5LoggerFactory.getLogger(RoomService.class,"sodeneme");
	
	public void addRoom(String name) {
		log.info("Room Added -> Name: " + name);
	}
	public void deleteRoom(String name) {
		log.info("Room Deleted -> Name: " + name);
	}
	public void addUserToRoom(String userName, String roomName){
		log.info("User Added -> " + userName + " To Room -> " + roomName);
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
