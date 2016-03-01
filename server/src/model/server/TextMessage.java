package model.server;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import service.UsersService;


@Entity  
@Table(name="TextMessages")  
@PrimaryKeyJoinColumn(name="id")  
public class TextMessage extends Message implements Serializable {

	public TextMessage() {	}
	
	@Column(name = "content", nullable = false)
	private String content;
	
	public TextMessage(User from, User to, Date date, String content) {
		super(from, to, date);
		setContent(content);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	// Creates clientModel.TextMessage from TextMessage
	public static model.client.TextMessage convertMessage(TextMessage msg) {
		return new model.client.TextMessage(msg.getId(), User.convert(msg.getFrom()), User.convert(msg.getTo()), msg.getDate(), msg.getContent());
	}
	
	// Creates TextMessage from clientModel.TextMessage
	public static TextMessage createMessage(model.client.TextMessage clientMsg) {
		model.client.TextMessage textMsg = (model.client.TextMessage) clientMsg;
		User from = UsersService.findById(textMsg.getFrom().getId());
		User to = UsersService.findById(textMsg.getTo().getId());
		
		return new TextMessage(from, to, textMsg.getDate(), textMsg.getContent());
	}

}
