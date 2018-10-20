package com.belyabl9.server.service;

import com.belyabl9.server.model.server.MediaMessage;
import com.belyabl9.server.model.server.Message;
import com.belyabl9.server.model.server.TextMessage;
import com.belyabl9.server.model.server.User;
import com.belyabl9.server.repository.MediaMessagesRepository;
import com.belyabl9.server.repository.MessagesRepository;
import com.belyabl9.server.repository.TextMessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessagesService {

	@Autowired
	private MessagesRepository messagesRepository;
	
	@Autowired
	private TextMessagesRepository textMessagesRepository;
	
	@Autowired
	private MediaMessagesRepository mediaMessagesRepository;
	
	@Autowired
	private UsersService usersService;
	
	public Message save(Message msg) {
		return messagesRepository.save(msg);
	}
	
	public List<TextMessage> getHistory(long idFrom, long idTo) {
		return textMessagesRepository.findByFromAndToOrToAndFromOrderByDate(idFrom, idTo);
	}
	
	public List<MediaMessage> getMediaMessages(User userTo, Date dateFrom) {
		if (dateFrom != null) {
			return mediaMessagesRepository.findByToAndDateGreaterThanOrderByDate(userTo, dateFrom);
		} else {
			return mediaMessagesRepository.findByToOrderByDate(userTo);
		}
	}
	
	public void remove(long id) {
		messagesRepository.delete(id);
	}
	
	public List<TextMessage> getUnsentMessages(User idFrom, User idTo) {
		return textMessagesRepository.findByFromAndToAndSentIsFalse(idFrom, idTo);
	}

	public List<TextMessage> getUnsentTextMessages(User idTo) {
		return textMessagesRepository.findByToAndSentIsFalse(idTo);
	}

	public List<MediaMessage> getUnsentMediaMessages(User idTo) {
		return mediaMessagesRepository.findByToAndSentIsFalse(idTo);
	}
	
	public void markMessagesAsRead(List<Long> messagesIds) {
		for (long id : messagesIds) {
			Message msg = messagesRepository.findOne(id);
			if (msg == null) {
				throw new RuntimeException("The message with the specified id does not exist.");
			}
			msg.setSent(true);
			messagesRepository.save(msg);
		}
	}

	// Creates MediaMessage from clientModel.MediaMessage
	public MediaMessage createMediaMessageFromDto(com.belyabl9.api.MediaMessage clientMsg) {
		User from = usersService.findById(clientMsg.getFrom().getId());
		User to = usersService.findById(clientMsg.getTo().getId());

		return new MediaMessage(from, to, clientMsg.getDate(), clientMsg.getSubject(), clientMsg.getPath(), clientMsg.getBlob());
	}

	// Creates TextMessage from clientModel.TextMessage
	public TextMessage createTextMessageFromDto(com.belyabl9.api.TextMessage clientMsg) {
		User from = usersService.findById(clientMsg.getFrom().getId());
		User to = usersService.findById(clientMsg.getTo().getId());

		return new TextMessage(from, to, clientMsg.getDate(), clientMsg.getContent());
	}
	
}
