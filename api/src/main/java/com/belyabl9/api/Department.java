package com.belyabl9.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Department extends DomainSuperClass implements Serializable {
	private String name;
	private Set<User> users = new HashSet<>();

	public Department() { }
	
	public Department(long id, String name, Set<User> users) {
		this.id = id;
		this.name = name;
		this.users = users;
	}
	
	public Department(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
