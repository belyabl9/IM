package requests.server;

import java.util.Map;

import model.server.Message;
import model.server.Response;
import model.server.TextMessage;
import requests.client.ServerToClient;
import service.MessagesService;

public class SendTextMessageRequest {

	public static model.client.Response process(Object o) {
		System.out.println("SendTextMessageRequest was called");
		
		Map<String, Object> content = (Map<String, Object>) o;
		model.client.TextMessage clientMsg = (model.client.TextMessage) content.get("message");
		
		model.server.Message msg = model.server.TextMessage.createMessage(clientMsg);
		msg = MessagesService.addMessage(msg);
		if ( msg == null )
			new model.client.Response(Response.ERROR, null);
		
		clientMsg.setId(msg.getId());
		
		ServerToClient serverToClient = new ServerToClient(msg.getTo().getIP(), msg.getTo().getPort());
		serverToClient.sendTextMessage(clientMsg);

		return new model.client.Response(Response.SUCCESS, null);
	}
}
