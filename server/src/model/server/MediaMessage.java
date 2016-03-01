package model.server;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import service.UsersService;


@Entity  
@Table(name="MediaMessages")  
@PrimaryKeyJoinColumn(name="id")  
public class MediaMessage extends Message {

	@Column(name = "path", nullable = false)
	private String path;
	
	@Column(name = "subject", nullable = false)
	private String subject;
	
	public MediaMessage() { }
	
	public MediaMessage(User from, User to, Date date, String subject, String filename) {
		super(from, to, date);
		setPath(filename);
		setSubject(subject);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	// Creates clientModel.TextMessage from TextMessage
	public static model.client.MediaMessage convertMessage(MediaMessage msg) {
		return new model.client.MediaMessage(msg.getId(), User.convert(msg.getFrom()), User.convert(msg.getTo()), msg.getDate(), msg.getSubject(), msg.getPath());
	}
	
	// Creates TextMessage from clientModel.TextMessage
	public static MediaMessage createMessage(model.client.MediaMessage clientMsg) {
		User from = UsersService.findById(clientMsg.getFrom().getId());
		User to = UsersService.findById(clientMsg.getTo().getId());
		
		return new MediaMessage(from, to, clientMsg.getDate(), clientMsg.getSubject(), clientMsg.getPath());
	}

}
