package requests.server;

import java.util.Map;

import model.server.Response;
import model.server.User;
import service.UsersService;

public class NewUserRequest {
	
	public static model.client.Response process(Object o) {
		System.out.println("NewUserRequest was called");
		
		Map<String, Object> content = (Map<String, Object>) o;
		model.client.User clientUser = (model.client.User) content.get("user");
		
		model.server.User user = UsersService.createUser(model.server.User.createInstance(clientUser));

		return new model.client.Response(Response.SUCCESS, User.convert(user));
	}
	
}
