package com.belyabl9.server.request.processor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.belyabl9.api.Response;
import com.belyabl9.server.converter.MediaMessageConverter;
import com.belyabl9.server.model.server.MediaMessage;
import com.belyabl9.server.service.MessagesService;
import com.belyabl9.server.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MediaMessagesRequestProcessor {

	@Autowired
	private MessagesService messagesService;
	
	@Autowired
	private UsersService usersService;
	
	public Response process(Object o) {
		log.debug("MediaMessagesRequest was called.");

		Map<String, Object> content = (Map<String, Object>) o;
		long id_to    = (long) content.get("id_to");
		Date dateFrom = (Date) content.get("date_from");
		
		List<MediaMessage> messages = messagesService.getMediaMessages(
				usersService.findById(id_to),
				dateFrom
		);

		List<com.belyabl9.api.MediaMessage> clientMediaMessages = messages.stream().
				map(MediaMessageConverter::convertToDto)
				.collect(Collectors.toList());
		
		return new Response(Response.SUCCESS, clientMediaMessages);
	}
}
