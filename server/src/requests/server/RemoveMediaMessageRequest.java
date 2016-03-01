package requests.server;

import java.util.Map;

import service.MessagesService;

public class RemoveMediaMessageRequest {
	
	public static model.client.Response process(Object o) {
		System.out.println("RemoveMediaMessageRequest was called");

		Map<String, Object> content = (Map<String, Object>) o;
		long id = (long) content.get("id");
	
		MessagesService.remove(id);
	
		return new model.client.Response(model.client.Response.SUCCESS, null);
	}

}