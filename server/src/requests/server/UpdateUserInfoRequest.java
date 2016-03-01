package requests.server;

import java.util.Map;

import model.server.User;
import service.UsersService;

public class UpdateUserInfoRequest {

	public static model.client.Response process(Object o) {
		System.out.println("UpdateUserInfoRequest was called");

		Map<String, Object> args = (Map<String, Object>) o;
		model.client.User user = (model.client.User) args.get("user");
		
		UsersService.updateInfo(model.server.User.createInstance(user));
		
		return new model.client.Response(model.client.Response.SUCCESS, null);
	}
}
