package requests.server;

import java.util.Map;

import model.server.User;
import service.UsersService;

public class GetUserRequest {
	
	public static model.client.Response process(Object o) {
		System.out.println("GetUserRequest was called");

		Map<String, Object> content = (Map<String, Object>) o;
		
		User user = null;
		if (content.get("id") != null)
			user = UsersService.findById((long) content.get("id")); 
		else if (content.get("login") != null)
			user = UsersService.findByLogin( (String) content.get("login"));
		
		return new model.client.Response(model.client.Response.SUCCESS, User.convert(user));
	}
	
}
