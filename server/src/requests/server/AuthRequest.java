package requests.server;

import java.util.Map;

import service.UsersService;

public class AuthRequest {

	public static model.client.Response process(Object o) {
		System.out.println("AuthRequest was called");
		
		Map<String, Object> content = (Map<String, Object>) o;
		String login = (String) content.get("login");
		String password = (String) content.get("password");
		
		return new model.client.Response(model.client.Response.SUCCESS, UsersService.authUser(login, password));
	}
}