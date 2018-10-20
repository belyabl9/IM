package com.belyabl9.server.model.server;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.belyabl9.api.StatusType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "Users" )
@Getter
@Setter
public class User extends DomainSuperClass implements Serializable {
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "surname", nullable = false)
	private String surname;
	
	@Column(name = "nickname", nullable = false)
	private String nickname;
	
	@Column(name = "login", nullable = false, unique = true)
	private String login;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "status", nullable = false)
	private StatusType status;
	
	@Column(name = "ip", nullable = false)
	private String ip;
	
	@Column(name = "port", nullable = false)
	private int port;
	
	// TODO to LocalDate
	@Column(name = "mtime", nullable = false)
	private Date mtime;

	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="i_department", nullable = false)
    private Department department;
	
	public User() { }
	
	public User(String name, String surname, String nickname, String login, String password, String ip, int port) {
		this.name = name;
		this.surname = surname;
		this.nickname = nickname;
		this.login = login;
		this.password = password;
		this.ip = ip;
		this.port = port;
		this.mtime = new Date();
		this.status = StatusType.OFFLINE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		if (user.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), user.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}
	
}
