package requests.server;

import java.util.Map;

import model.server.Response;
import requests.client.ServerToClient;
import service.FileService;
import service.MessagesService;

public class SendMediaMessageRequest {

	public static model.client.Response process(Object o) {
		System.out.println("SendMediaMessageRequest was called");
		
		Map<String, Object> content = (Map<String, Object>) o;
		model.client.MediaMessagePacket clientMsgPacket = (model.client.MediaMessagePacket) content.get("message_packet");
		byte[] blob = clientMsgPacket.blob;
		
		model.server.Message msg = model.server.MediaMessage.createMessage(clientMsgPacket.msg);
		System.out.println(clientMsgPacket.msg.getPath());
		FileService.saveMediaFile(clientMsgPacket.blob, "/tmp/" + clientMsgPacket.msg.getPath());
		msg = MessagesService.addMessage(msg);
		if ( msg == null )
			new model.client.Response(Response.ERROR, null);
		
		return new model.client.Response(Response.SUCCESS, null);
	}
}
