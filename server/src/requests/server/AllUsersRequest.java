package requests.server;

import java.util.ArrayList;
import java.util.List;

import model.server.User;
import service.UsersService;

public class AllUsersRequest {

	public static model.client.Response process(Object o) {
		System.out.println("AllUsersRequest was called");

		List<model.client.User> clientUsers = new ArrayList<model.client.User>();
		List<User> users = UsersService.getAllUsers(); 
		for (User user : users) {
			clientUsers.add(User.convert(user));
		}
		
		return new model.client.Response(model.client.Response.SUCCESS, clientUsers);
	}
}
