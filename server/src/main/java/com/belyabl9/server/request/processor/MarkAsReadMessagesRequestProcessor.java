package com.belyabl9.server.request.processor;

import java.util.List;
import java.util.Map;

import com.belyabl9.api.Response;
import com.belyabl9.server.service.MessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MarkAsReadMessagesRequestProcessor {
	
	@Autowired
	private MessagesService messagesService;
	
	public Response process(Object o) {
		log.debug("MarkAsReadMessages was called.");
		
		Map<String, Object> content = (Map<String, Object>) o;
		List<Long> messagesIDs = (List<Long>) content.get("messagesIDs");

		messagesService.markMessagesAsRead(messagesIDs);

		return new Response(Response.SUCCESS, null);
	}
	
}
