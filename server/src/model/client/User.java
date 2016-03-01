package model.client;
import java.io.Serializable;
import java.util.Date;


public class User extends DomainSuperClass implements Serializable {

	private String name;
	private String surname;
	private String nickname;
	private String login;
	private String password;
	private StatusTypes status;
	private String ip;
	private int port;
	private Date mtime;

	private Department department;
	
	public User(long id, String name, String surname, String nickname, String login, String password, StatusTypes status, Date mtime, String ip, int port) {
		setId(id);
		setName(name);
		setSurname(surname);
		setNickname(nickname);
		setLogin(login);
		setPassword(password);
		setStatus(status);
		setIP(ip);
		setPort(port);
		setMtime(mtime);
	}
	
	public String getName() {
		return name;
	}
	
	public StatusTypes getStatus() {
		return status;
	}
	
	public void setStatus(StatusTypes status) {
		this.status = status;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
    public Date getMtime() {
		return mtime;
	}

	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}
	
	public String toString() {
		return String.format("%s %s (%s)", name, surname, nickname);
	}
	
}
