package com.belyabl9.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class User extends DomainSuperClass implements Serializable {
	private String name;
	private String surname;
	private String nickname;
	private String login;
	private String password;
	private StatusType status;
	private String ip;
	private int port;
	private Date mtime;

	private Department department;
	
	public User(long id, String name, String surname, String nickname, String login, String password, StatusType status, Date mtime, String ip, int port) {
		setId(id);
		setName(name);
		setSurname(surname);
		setNickname(nickname);
		setLogin(login);
		setPassword(password);
		setStatus(status);
		setIp(ip);
		setPort(port);
		setMtime(mtime);
	}
	
	public String toString() {
		return String.format("%s %s (%s)", name, surname, nickname);
	}
	
}
