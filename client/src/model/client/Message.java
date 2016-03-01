package model.client;
import java.io.Serializable;
import java.util.Date;

public class Message extends DomainSuperClass implements Serializable {
	
	private Date date;
	private User from;
	private User to;
    private boolean sent;
	
	public Message(long id, User from, User to, Date date) {
		setId(id);
		setFrom(from);
		setTo(to);
		setDate(date);
	}
	
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
