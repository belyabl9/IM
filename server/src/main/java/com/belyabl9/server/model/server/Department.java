package com.belyabl9.server.model.server;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table( name = "Departments" )
@Getter
@Setter
public class Department extends DomainSuperClass implements Serializable {
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@OneToMany (fetch = FetchType.EAGER, cascade={CascadeType.ALL}, mappedBy="department")
	private Set<User> users = new HashSet<>();

	public Department() { }
	
	public Department(Department department) {
		this.id = department.getId();
		this.name = department.getName();
	}
	
	public Department(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Department department = (Department) o;
		if (department.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), department.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

}
