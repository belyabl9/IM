package com.belyabl9.server.request.processor;

import java.util.ArrayList;
import java.util.List;

import com.belyabl9.api.Response;
import com.belyabl9.server.converter.DepartmentConverter;
import com.belyabl9.server.model.server.Department;
import com.belyabl9.server.service.DepartmentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AllDepartmentsRequestProcessor {

	@Autowired
	private DepartmentsService departmentsService;
	
	public Response process(Object o) {
	    log.debug("AllDepartmentsRequest was called.");

		List<com.belyabl9.api.Department> clientDepartments = new ArrayList<>();
		List<Department> departments = departmentsService.getAllDepartments();
		for (Department department : departments) {
			clientDepartments.add(DepartmentConverter.convertToDto(department));
		}
		return new Response(Response.SUCCESS, clientDepartments);
	}
}
