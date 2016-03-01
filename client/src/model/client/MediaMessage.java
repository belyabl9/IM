package model.client;
import java.util.Date;

public class MediaMessage extends Message {

	private String path;
	private String subject;
	
	public MediaMessage(long id, model.client.User from, model.client.User to, Date date, String subject, String filename) {
		super(id, from, to, date);
		setSubject(subject);
		setPath(filename);
	}

	public MediaMessage(model.client.User from, model.client.User to, Date date, String subject, String filename) {
		super(from, to, date);
		setPath(filename);
		setSubject(subject);
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}