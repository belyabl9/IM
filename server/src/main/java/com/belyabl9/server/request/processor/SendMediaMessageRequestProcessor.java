package com.belyabl9.server.request.processor;

import com.belyabl9.api.MediaMessage;
import com.belyabl9.api.Response;
import com.belyabl9.server.model.server.Message;
import com.belyabl9.server.service.MessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SendMediaMessageRequestProcessor {

	@Autowired
	private MessagesService messagesService;

	public Response process(Object o) {
		log.debug("SendMediaMessageRequest was called.");
		
		Map<String, Object> content = (Map<String, Object>) o;
		MediaMessage clientMsg = (MediaMessage) content.get("message_packet");
		
		Message msg = messagesService.createMediaMessageFromDto(clientMsg);
		msg = messagesService.save(msg);
		if (msg == null) {
			return new Response(Response.ERROR, null);
		}
		return new Response(Response.SUCCESS, null);
	}
}
