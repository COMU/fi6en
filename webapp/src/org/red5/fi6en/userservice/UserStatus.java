package org.red5.fi6en.userservice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name="user_status")
public class UserStatus {
	private long id;
	private long uid;
	private Long client_id;
	private String username;
	private String roomname;
	private Boolean is_online;
	private Boolean broadcast;
	private Boolean moderator;
	private Boolean desktop;
	
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	@Column (name="id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column (name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column (name="roomname")
	public String getRoomname() {
		return roomname;
	}
	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}
	@Column (name="is_online")
	public Boolean getIs_online() {
		return is_online;
	}
	public void setIs_online(Boolean isOnline) {
		is_online = isOnline;
	}
	@Column (name="client_id", nullable=true)
	public void setClient_id(Long client_id) {
		this.client_id = client_id;
	}
	public Long getClient_id() {
		return client_id;
	}
	@Column (name="broadcast", nullable=true)
	public void setBroadcast(Boolean broadcast) {
		this.broadcast = broadcast;
	}
	public Boolean getBroadcast() {
		return broadcast;
	}
	@Column (name="moderator", nullable=true)
	public void setModerator(Boolean moderator) {
		this.moderator = moderator;
	}
	public Boolean getModerator() {
		return moderator;
	}
	@Column (name="desktop", nullable=true)
	public void setDesktop(Boolean desktop) {
		this.desktop = desktop;
	}
	public Boolean getDesktop() {
		return desktop;
	}
	@Column (name="uid", nullable=true)
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getUid() {
		return uid;
	}
}
