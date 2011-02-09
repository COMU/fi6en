package org.red5.fi6en.traffic;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for Annotation .
 *
 *@author kozdincer
 */

@Entity
@Table (name="tb_broadcast_log")
public class Broadcast {
	private long id;
	private String ip;
	private Integer remoteUserId;
	private Integer duration;
	private Long size;
	private Timestamp dateBegin;
	private Timestamp dateEnd;
	private String server_name;
	private String client_name;
	
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	@Column (name="id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column (name="ip")
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Column (name="remoteUserId")
	public Integer getRemoteUserId() {
		return remoteUserId;
	}
	public void setRemoteUserId(Integer remoteUserId) {
		this.remoteUserId = remoteUserId;
	}
	@Column (name="duration")
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	@Column (name="size")
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	@Column (name="dateBegin")
	public Timestamp getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Timestamp dateBegin) {
		this.dateBegin = dateBegin;
	}
	@Column (name="dateEnd")
	public Timestamp getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Timestamp dateEnd) {
		this.dateEnd = dateEnd;
	}
	@Column (name="server_name")
	public String getServer_name() {
		return server_name;
	}
	public void setServer_name(String serverName) {
		server_name = serverName;
	}
	@Column (name="client_name")
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String clientName) {
		client_name = clientName;
	}
		
}
