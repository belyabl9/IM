package requests.server;

import java.util.Map;

import model.server.Department;
import model.server.Response;
import service.DepartmentsService;
import service.UsersService;

public class NewDepartmentRequest {
	
	public static model.client.Response process(Object o) {
		System.out.println("NewDepartmentRequest was called");
		
		Map<String, Object> content = (Map<String, Object>) o;
		model.client.Department clientDepartment = (model.client.Department) content.get("department");
		
		model.server.Department department = DepartmentsService.createDepartment(model.server.Department.createInstance(clientDepartment));

		return new model.client.Response(Response.SUCCESS, Department.convert(department));
	}
	
}
