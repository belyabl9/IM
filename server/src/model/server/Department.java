package model.server;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import service.DepartmentsService;

@Entity
@Table( name = "Departments" )
public class Department extends DomainSuperClass implements Serializable {
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@OneToMany (fetch = FetchType.EAGER, cascade={CascadeType.ALL}, mappedBy="department")
	private Set<User> users = new HashSet<User>();

	public Department() { }
	
	public Department(model.client.Department department) {
		setId(department.getId());
		setName(department.getName());
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
	
	public static Set<model.client.User> convertUsers(Set<User> users)  {
		Set<model.client.User> clientUsers = new HashSet<model.client.User>();
		for ( User user : users) {
			clientUsers.add(User.convert(user));
		}
		
		return clientUsers;
	}
	
	public static Department createInstance(model.client.Department clientDepartment) {
		System.out.println(clientDepartment.getId());
		Department department = clientDepartment.getId() != 0 ? DepartmentsService.findById(clientDepartment.getId()) : new Department();
		department.setName(clientDepartment.getName());
		if ( clientDepartment.getUsers() != null ) {
			Set<User> users = new HashSet<User>();
			for ( model.client.User user : clientDepartment.getUsers() ) {
				users.add( User.createInstance(user) );
			}
			department.setUsers(users);
		}
		
		return department;
	}
	
	public static model.client.Department convert(Department department) {
		return new model.client.Department(department.getId(), department.getName(), convertUsers( department.getUsers() ) );
	}
	
}
