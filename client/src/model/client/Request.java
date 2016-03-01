package model.client;
import java.io.Serializable;
import java.util.Map;

public class Request implements Serializable {

	private RequestTypes type;
	private Map<String, Object> content;
	
	public Request(RequestTypes type, Map<String, Object> content) {
		setType(type);
		setContent(content);
	}
	
	public Request(RequestTypes type) {
		setType(type);
	}

	public RequestTypes getType() {
		return type;
	}
	public void setType(RequestTypes type) {
		this.type = type;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Map<String, Object> content) {
		this.content = content;
	}
	
}
