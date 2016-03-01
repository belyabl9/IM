package requests.server;

import java.util.ArrayList;
import java.util.List;

import model.server.Department;
import service.DepartmentsService;

public class AllDepartmentsRequest {

	public static model.client.Response process(Object o) {
		System.out.println("AllDepartmentsRequest was called");

		List<model.client.Department> clientDepartments = new ArrayList<model.client.Department>();
		List<Department> departments = DepartmentsService.getAllDepartments();
		for (Department department : departments) {
			clientDepartments.add(Department.convert(department));
		}
		
		return new model.client.Response(model.client.Response.SUCCESS, clientDepartments);
	}
}
