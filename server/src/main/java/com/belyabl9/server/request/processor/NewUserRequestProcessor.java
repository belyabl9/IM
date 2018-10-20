package com.belyabl9.server.request.processor;

import java.util.Map;

import com.belyabl9.api.Response;
import com.belyabl9.server.converter.UserConverter;
import com.belyabl9.server.model.server.Department;
import com.belyabl9.server.model.server.User;
import com.belyabl9.server.service.DepartmentsService;
import com.belyabl9.server.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class NewUserRequestProcessor {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private DepartmentsService departmentsService;
	
	@Transactional
	public Response process(Object o) {
		log.debug("NewUserRequest was called");
		
		Map<String, Object> content = (Map<String, Object>) o;
		com.belyabl9.api.User clientUser = (com.belyabl9.api.User) content.get("user");

		User userInstance = usersService.createInstanceFromDto(clientUser);
		Department department = departmentsService.findById(clientUser.getDepartment().getId());
		userInstance.setDepartment(department);
		department.getUsers().add(userInstance);
		User user = usersService.save(userInstance);

		return new Response(Response.SUCCESS, UserConverter.convertToDto(user));
	}
	
}
