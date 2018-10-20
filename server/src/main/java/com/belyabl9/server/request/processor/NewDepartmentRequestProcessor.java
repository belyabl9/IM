package com.belyabl9.server.request.processor;

import com.belyabl9.api.Response;
import com.belyabl9.server.converter.DepartmentConverter;
import com.belyabl9.server.model.server.Department;
import com.belyabl9.server.service.DepartmentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class NewDepartmentRequestProcessor {

	@Autowired
	private DepartmentsService departmentsService;
	
	public Response process(Object o) {
		log.debug("NewDepartmentRequest was called.");
		
		Map<String, Object> content = (Map<String, Object>) o;
		com.belyabl9.api.Department clientDepartment = (com.belyabl9.api.Department) content.get("department");
		
		Department department = departmentsService.save(departmentsService.createInstanceFromDto(clientDepartment));

		return new Response(Response.SUCCESS, DepartmentConverter.convertToDto(department));
	}
	
}
