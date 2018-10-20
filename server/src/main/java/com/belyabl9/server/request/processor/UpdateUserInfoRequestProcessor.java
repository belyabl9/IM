package com.belyabl9.server.request.processor;

import java.util.Map;

import com.belyabl9.api.Response;
import com.belyabl9.server.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateUserInfoRequestProcessor {

	@Autowired
	private UsersService usersService;
	
	public Response process(Object o) {
		log.debug("UpdateUserInfoRequest was called.");

		Map<String, Object> args = (Map<String, Object>) o;
		com.belyabl9.api.User user = (com.belyabl9.api.User) args.get("user");

		usersService.updateWithTimestamp(usersService.createInstanceFromDto(user));
		
		return new Response(Response.SUCCESS, null);
	}
}
