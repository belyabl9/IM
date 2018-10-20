package com.belyabl9.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class Message extends DomainSuperClass implements Serializable {
	private Date date;
	private User from;
	private User to;
    private boolean sent;
	
	public Message(long id, User from, User to, Date date) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.date = date;
	}
	
	public Message(User from, User to, Date date) {
		this.from = from;
		this.to = to;
		this.date = date;
	}

}
