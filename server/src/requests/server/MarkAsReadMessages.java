package requests.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.server.Response;
import service.MessagesService;

public class MarkAsReadMessages {
	
	public static model.client.Response process(Object o) {
		System.out.println("MarkAsReadMessages was called");
		
		Map<String, Object> content = (Map<String, Object>) o;
		List<Long> messagesIDs = (List<Long>) content.get("messagesIDs");
		
		MessagesService.markAsReadMessages(messagesIDs);

		return new model.client.Response(Response.SUCCESS, null);
	}
	
}
