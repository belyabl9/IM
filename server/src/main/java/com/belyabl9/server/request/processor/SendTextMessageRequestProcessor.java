package com.belyabl9.server.request.processor;

import com.belyabl9.api.Response;
import com.belyabl9.server.model.server.Message;
import com.belyabl9.server.model.server.TextMessage;
import com.belyabl9.server.service.MessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SendTextMessageRequestProcessor {

	@Autowired
	private MessagesService messagesService;
	
	public Response process(Object o) {
		log.debug("SendTextMessageRequest was called.");
		
		Map<String, Object> content = (Map<String, Object>) o;
		com.belyabl9.api.TextMessage clientMsg = (com.belyabl9.api.TextMessage) content.get("message");
		
		TextMessage textMessage = messagesService.createTextMessageFromDto(clientMsg);
		Message msg = messagesService.save(textMessage);
		if (msg == null) {
			return new Response(Response.ERROR, null);
		}
		return new Response(Response.SUCCESS, null);
	}
}
