package com.belyabl9.client;

import com.belyabl9.api.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Session {
	private static Session session;
	
	private User user;
	private String serverIp;
	private int serverPort;
	
	private Session() { }
	
	public static Session getInstance() {
		if (session == null) {
			session = new Session();
		}
		return session;
	}

	public static void clear() {
		session = null;
	}

}
