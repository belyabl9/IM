package com.belyabl9.server.converter;

import com.belyabl9.api.User;
import com.belyabl9.server.model.server.Department;

import java.util.HashSet;
import java.util.Set;

public class DepartmentConverter {

    public static com.belyabl9.api.Department convertToDto(Department department) {
        return new com.belyabl9.api.Department(department.getId(), department.getName(), convertListToDto( department.getUsers() ) );
    }

    private static Set<User> convertListToDto(Set<com.belyabl9.server.model.server.User> users)  {
        Set<com.belyabl9.api.User> clientUsers = new HashSet<>();
        for (com.belyabl9.server.model.server.User user : users) {
            clientUsers.add(UserConverter.convertToDto(user));
        }
        return clientUsers;
    }
    
}
