package com.belyabl9.server.request.processor;

import com.belyabl9.api.Response;
import com.belyabl9.server.converter.MediaMessageConverter;
import com.belyabl9.server.converter.TextMessageConverter;
import com.belyabl9.server.model.server.MediaMessage;
import com.belyabl9.server.model.server.Message;
import com.belyabl9.server.model.server.TextMessage;
import com.belyabl9.server.service.MessagesService;
import com.belyabl9.server.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GetUpdatesRequestProcessor {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private MessagesService messagesService;
	
	public Response process(Object o) {
		log.debug("SendTextMessageRequest was called.");
		
		Map<String, Object> content = (Map<String, Object>) o;
		long idTo = (long) content.get("idTo");

		List<MediaMessage> unsentMediaMessages = messagesService.getUnsentMediaMessages(usersService.findById(idTo));
		List<TextMessage> unsentTextMessages = messagesService.getUnsentTextMessages(usersService.findById(idTo));

		List<Long> messageIds = new ArrayList<>();
		messageIds.addAll(unsentTextMessages.stream().map(TextMessage::getId).collect(Collectors.toList()));
		messageIds.addAll(unsentMediaMessages.stream().map(MediaMessage::getId).collect(Collectors.toList()));
		
		messagesService.markMessagesAsRead(messageIds);
		
		List<com.belyabl9.api.Message> messages = new ArrayList<>();
		List<com.belyabl9.api.TextMessage> textMessages = unsentTextMessages.stream().map(TextMessageConverter::convertToDto).collect(Collectors.toList());
		List<com.belyabl9.api.MediaMessage> mediaMessages = unsentMediaMessages.stream().map(MediaMessageConverter::convertToDto).collect(Collectors.toList());
		messages.addAll(textMessages);
		messages.addAll(mediaMessages);
		
		return new Response(Response.SUCCESS, messages);
	}
}
