package model.server;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import model.client.StatusTypes;
import service.UsersService;


@Entity
@Table( name = "Users" )
public class User extends DomainSuperClass implements Serializable {

	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "surname", nullable = false)
	private String surname;
	
	@Column(name = "nickname", nullable = false)
	private String nickname;
	
	@Column(name = "login", nullable = false)
	private String login;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "status", nullable = false)
	private StatusTypes status;
	
	@Column(name = "ip", nullable = false)
	private String ip;
	
	@Column(name = "port", nullable = false)
	private int port;
	
	@Column(name = "mtime", nullable = false)
	private Date mtime;

	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="i_department", nullable = false)
    private Department department;
	
	public User() { }
	
	public User(String name, String surname, String login, String password, String ip, int port) {
		setName(name);
		setSurname(surname);
		setLogin(login);
		setPassword(password);
		setIP(ip);
		setPort(port);
		setMtime(new Date());
		this.status = StatusTypes.OFFLINE;
	}
	
	public static User createInstance(model.client.User clientUser) {
		User user = clientUser.getId() != 0 ? UsersService.findById(clientUser.getId()) : new User();
		user.setName(clientUser.getName());
		user.setSurname(clientUser.getSurname());
		user.setNickname(clientUser.getNickname());
		user.setLogin(clientUser.getLogin());
		user.setPassword(clientUser.getPassword());
		user.setStatus(clientUser.getStatus());
		user.setPort(clientUser.getPort());
		user.setIP(clientUser.getIP());
		user.setMtime(new Date());
		
		if ( clientUser.getDepartment() != null ) {
			user.setDepartment( Department.createInstance(clientUser.getDepartment()) );
		}
		
		return user;
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
	
	public static model.client.User convert(User user) {
		return new model.client.User(user.getId(), user.getName(), user.getSurname(), user.getSurname(), user.getLogin(), user.getPassword(), user.getStatus(), user.getMtime(), user.getIP(), user.getPort());
	}
	
}
