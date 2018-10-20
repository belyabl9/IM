package com.belyabl9.server.service;

import com.belyabl9.server.model.server.Department;
import com.belyabl9.server.model.server.User;
import com.belyabl9.server.repository.DepartmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DepartmentsService {

	@Autowired
	private DepartmentsRepository repository;
	
	@Autowired
	private UsersService usersService;
	
	public List<Department> getAllDepartments() {
		return repository.findAll();
	}
	
	public Department findById(long id) {
		return repository.findOne(id);
	}
	
	public Department save(Department department) {
		return repository.save(department);
	}

	public Department createInstanceFromDto(com.belyabl9.api.Department clientDepartment) {
		Department department = clientDepartment.getId() != 0 ? findById(clientDepartment.getId()) : new Department();
		department.setName(clientDepartment.getName());
		if (clientDepartment.getUsers() != null) {
			Set<User> users = new HashSet<>();
			for (com.belyabl9.api.User user : clientDepartment.getUsers()) {
				users.add( usersService.createInstanceFromDto(user) );
			}
			department.setUsers(users);
		}
		return department;
	}
}
