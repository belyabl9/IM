package requests.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.server.TextMessage;
import service.MessagesService;

public class ConversationHistory {

	public static model.client.Response process(Object o) {
		System.out.println("ConversationHistory was called");

		Map<String, Object> content = (Map<String, Object>) o;
		long id_from = (long) content.get("id_from");
		long id_to   = (long) content.get("id_to");
		
		List<TextMessage> messages = MessagesService.getHistory(id_from, id_to);
		List<model.client.TextMessage> clientMessages = new ArrayList<model.client.TextMessage>();
		for (TextMessage msg : messages) {
			clientMessages.add(TextMessage.convertMessage(msg));
		}
		
		return new model.client.Response(model.client.Response.SUCCESS, clientMessages);
	}
}
