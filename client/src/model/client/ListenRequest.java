package model.client;
import java.io.Serializable;
import java.util.Map;


public class ListenRequest implements Serializable {

	private ListenRequestTypes type;
	private Map<String, Object> content;
	
	public ListenRequest(ListenRequestTypes type, Map<String, Object> content) {
		setType(type);
		setContent(content);
	}
	
	public ListenRequest(ListenRequestTypes type) {
		setType(type);
	}

	public ListenRequestTypes getType() {
		return type;
	}
	public void setType(ListenRequestTypes type) {
		this.type = type;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Map<String, Object> content) {
		this.content = content;
	}
	
}
