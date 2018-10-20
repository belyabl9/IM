package com.belyabl9.server.request.processor;

import java.util.Map;

import com.belyabl9.api.Response;
import com.belyabl9.server.service.MessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoveMediaMessageRequestProcessor {

	@Autowired
	private MessagesService messagesService;
	
	public Response process(Object o) {
		log.debug("RemoveMediaMessageRequest was called.");

		Map<String, Object> content = (Map<String, Object>) o;
		long id = (long) content.get("id");

		messagesService.remove(id);
	
		return new Response(Response.SUCCESS, null);
	}

}