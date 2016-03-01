package dao;

import java.util.List;

import model.server.Department;

import org.hibernate.SessionFactory;


public class DepartmentsDAO extends GenericDAO<Department> {

	public DepartmentsDAO(SessionFactory factory) {
		super(Department.class, factory);
	}
	
	public List<Department> getAllDepartments() {
		return (List<Department>) super.findAll();
	}
	
	public Department createDepartment(Department department) {
		return save(department);
	}
	
	
}
