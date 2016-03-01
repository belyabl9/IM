package model.client;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Department extends DomainSuperClass implements Serializable {
	
	private String name;
	private Set<User> users = new HashSet<User>();

	public Department() { }
	
	public Department(long id, String name, Set<User> users) {
		setId(id);
		setName(name);
		setUsers(users);
	}
	
	public Department(String name) {
		setName(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public String toString() {
		return name;
	}
}
