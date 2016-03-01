package requests.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.client.MediaMessagePacket;
import model.server.MediaMessage;
import service.MessagesService;

public class MediaMessagesRequest {

	public static model.client.Response process(Object o) {
		System.out.println("MediaMessagesRequest was called");

		Map<String, Object> content = (Map<String, Object>) o;
		long id_to    = (long) content.get("id_to");
		Date dateFrom = (Date) content.get("date_from");
		
		List<model.client.MediaMessagePacket> clientMediaMessages = new ArrayList<model.client.MediaMessagePacket>();
		List<MediaMessage> messages = MessagesService.getMediaMessages(id_to, dateFrom); 
		for (MediaMessage msg : messages) {
			try {
				clientMediaMessages.add(new MediaMessagePacket(MediaMessage.convertMessage(msg), Files.readAllBytes( Paths.get("/tmp/" + msg.getPath())  )  ) );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return new model.client.Response(model.client.Response.SUCCESS, clientMediaMessages);
	}
}
