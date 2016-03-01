package service;

import java.util.Date;
import java.util.List;

import model.server.Department;
import model.server.User;
import utils.Digest;
import dao.DaoFactory;
import dao.DepartmentsDAO;
import dao.UsersDAO;

public class DepartmentsService {

	public static List<Department> getAllDepartments() {
		DepartmentsDAO dao = DaoFactory.getDepartmentsDAO();
		List<Department> departments = dao.getAllDepartments();

		return departments;
	}
	
	public static Department findById(long id) {
		DepartmentsDAO dao = DaoFactory.getDepartmentsDAO();
		return dao.findById(id);
	}
	
	public static Department createDepartment(Department department) {
		DepartmentsDAO dao = DaoFactory.getDepartmentsDAO();
		return dao.createDepartment(department);
	}
	
}
