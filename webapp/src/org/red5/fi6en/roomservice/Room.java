package org.red5.fi6en.roomservice;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name="rooms")
public class Room {

	private long id;
	private String name;
	private String comment;
	private Timestamp starttime;
	private Timestamp finishtime;
	private Boolean is_public;
	private Boolean is_open;
	private String hashpasswd;
	
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	@Column (name="id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column (name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column (name="comment")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Column (name="starttime")
	public Timestamp getStarttime() {
		return starttime;
	}
	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}
	@Column (name="finishtime")
	public Timestamp getFinishtime() {
		return finishtime;
	}
	public void setFinishtime(Timestamp finishtime) {
		this.finishtime = finishtime;
	}
	@Column (name="is_public")
	public Boolean isIs_public() {
		return is_public;
	}
	public void setIs_public(Boolean isPublic) {
		is_public = isPublic;
	}
	@Column (name="is_open")
	public Boolean isIs_open() {
		return is_open;
	}
	public void setIs_open(Boolean isOpen) {
		is_open = isOpen;
	}
	@Column (name="hashpasswd")
	public void setHashpasswd(String hashpasswd) {
		this.hashpasswd = hashpasswd;
	}
	public String getHashpasswd() {
		return hashpasswd;
	}
	
	
}
