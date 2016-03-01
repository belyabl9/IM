package model.client;
import java.io.Serializable;
import java.util.Date;

public class TextMessage extends Message implements Serializable {

	private String content;
	
	public TextMessage(long id, User from, User to, Date date, String content) {
		super(id, from, to, date);
		setContent(content);
	}
	
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

}
