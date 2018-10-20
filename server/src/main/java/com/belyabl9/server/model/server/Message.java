package com.belyabl9.server.model.server;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity  
@Table(name = "Messages")  
@Inheritance(strategy=InheritanceType.JOINED)  
@Getter
@Setter
public class Message extends DomainSuperClass implements Serializable {
	
	public Message() { }
	
	@Column(name = "date", nullable = false)
	private Date date;
	
    @ManyToOne
    @JoinColumn(name="i_user_from", nullable = false)
	private User from;
	
    @ManyToOne
    @JoinColumn(name="i_user_to", nullable = false)
	private User to;
    
    @Column(name = "sent", nullable = false)
    private boolean sent;
	
	public Message(User from, User to, Date date) {
		this.from = from;
		this.to = to;
		this.date = date;
	}
	
}
