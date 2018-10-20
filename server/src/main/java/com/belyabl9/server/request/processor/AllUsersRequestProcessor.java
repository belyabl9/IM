package com.belyabl9.server.request.processor;

import java.util.ArrayList;
import java.util.List;

import com.belyabl9.api.Response;
import com.belyabl9.server.converter.UserConverter;
import com.belyabl9.server.model.server.User;
import com.belyabl9.server.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AllUsersRequestProcessor {

	@Autowired
	private UsersService usersService;
	
	public Response process(Object o) {
		log.debug("AllUsersRequest was called.");

		List<com.belyabl9.api.User> clientUsers = new ArrayList<>();
		List<User> users = usersService.findAll();
		for (User user : users) {
			clientUsers.add(UserConverter.convertToDto(user));
		}
		return new Response(Response.SUCCESS, clientUsers);
	}
}
