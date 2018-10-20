package com.belyabl9.server.request.processor;

import java.util.Map;

import com.belyabl9.api.Response;
import com.belyabl9.server.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthRequestProcessor {

	@Autowired
	private UsersService usersService;
	
	public Response process(Object o) {
		log.debug("AuthRequest was called.");
		
		Map<String, Object> content = (Map<String, Object>) o;
		String login = (String) content.get("login");
		String password = (String) content.get("password");
		
		return new Response(Response.SUCCESS, usersService.authUser(login, password));
	}
}