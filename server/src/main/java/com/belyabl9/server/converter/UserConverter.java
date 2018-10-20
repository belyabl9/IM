package com.belyabl9.server.converter;

import com.belyabl9.server.model.server.User;

public class UserConverter {

    public static com.belyabl9.api.User convertToDto(User user) {
        return new com.belyabl9.api.User(user.getId(), user.getName(), user.getSurname(), user.getSurname(), user.getLogin(), user.getPassword(), user.getStatus(), user.getMtime(), user.getIp(), user.getPort());
    }
    
}
