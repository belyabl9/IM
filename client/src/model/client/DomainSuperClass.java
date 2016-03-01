package model.client;

import java.io.Serializable;

public class DomainSuperClass implements Serializable {
	protected long id;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
}
