package model.server;
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
		setFrom(from);
		setTo(to);
		setDate(date);
	}
		
	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public User getTo() {
		return to;
	}

	public void setTo(User to) {
		this.to = to;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public boolean isSent() {
		return sent;
	}
	
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
}
