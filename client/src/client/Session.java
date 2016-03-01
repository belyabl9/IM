package client;

import model.client.User;

public class Session {
	
	private static Session session;
	
	private User user;
	private String serverIP;
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
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	public String getServerIP() {
		return serverIP;
	}
	
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	public int getServerPort() {
		return serverPort;
	}
	

}
