package com.belyabl9.server.converter;

import com.belyabl9.server.model.server.MediaMessage;

public class MediaMessageConverter {

    public static com.belyabl9.api.MediaMessage convertToDto(MediaMessage msg) {
        return new com.belyabl9.api.MediaMessage(msg.getId(), UserConverter.convertToDto(msg.getFrom()), UserConverter.convertToDto(msg.getTo()), msg.getDate(), msg.getSubject(), msg.getPath(), msg.getBlob());
    }
    
}
