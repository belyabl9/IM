package com.belyabl9.server.request.processor;

import java.util.Map;

import com.belyabl9.api.Response;
import com.belyabl9.server.converter.UserConverter;
import com.belyabl9.server.model.server.User;
import com.belyabl9.server.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GetUserRequestProcessor {

	@Autowired
	private UsersService usersService;
	
	public Response process(Object o) {
		log.debug("GetUserRequest was called.");

		Map<String, Object> content = (Map<String, Object>) o;
		
		User user = null;
		if (content.get("id") != null) {
			user = usersService.findById((long) content.get("id"));
		} else if (content.get("login") != null) {
			user = usersService.findByLogin((String) content.get("login"));
		}
		
		return new Response(Response.SUCCESS, UserConverter.convertToDto(user));
	}
	
}
