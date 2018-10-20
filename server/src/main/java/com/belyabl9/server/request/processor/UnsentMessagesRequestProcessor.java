package com.belyabl9.server.request.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.belyabl9.api.Response;
import com.belyabl9.server.converter.TextMessageConverter;
import com.belyabl9.server.model.server.TextMessage;
import com.belyabl9.server.service.MessagesService;
import com.belyabl9.server.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UnsentMessagesRequestProcessor {

	@Autowired
	private MessagesService messagesService;
	
	@Autowired
	private UsersService usersService;
	
	public Response process(Object o) {
		log.debug("UnsentMessagesRequest was called");
		
		Map<String, Object> content = (Map<String, Object>) o;
		long id_from = (long) content.get("id_from");
		long id_to = (long) content.get("id_to");
		
		List<TextMessage> messages = messagesService.getUnsentMessages(
				usersService.findById(id_from),
				usersService.findById(id_to)
		);
		List<com.belyabl9.api.TextMessage> clientMessages = new ArrayList<>();
		for (TextMessage msg : messages) {
			clientMessages.add(TextMessageConverter.convertToDto(msg));
		}

		return new Response(Response.SUCCESS, clientMessages);
	}
	
}
