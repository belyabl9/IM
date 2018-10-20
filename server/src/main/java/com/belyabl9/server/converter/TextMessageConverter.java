package com.belyabl9.server.converter;

import com.belyabl9.server.model.server.TextMessage;

public class TextMessageConverter {

    public static com.belyabl9.api.TextMessage convertToDto(TextMessage msg) {
        return new com.belyabl9.api.TextMessage(msg.getId(), UserConverter.convertToDto(msg.getFrom()), UserConverter.convertToDto(msg.getTo()), msg.getDate(), msg.getContent());
    }
    
}
