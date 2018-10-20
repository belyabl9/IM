package com.belyabl9.client;

import com.belyabl9.api.TextMessage;
import com.belyabl9.api.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TextMessageQueue {

	private static Map<Long, LinkedList<TextMessage>> messageQueue = new HashMap<>();
	
	public static void addMessage(User user, TextMessage msg) {
		if ( messageQueue.get(user.getId()) == null )
			messageQueue.put( user.getId(), new LinkedList<>() );
		
		messageQueue.get(user.getId()).add(msg);
	}
	
	public static void addMessages(User user, List<TextMessage> messages) {
		if ( messageQueue.get(user.getId()) == null )
			messageQueue.put( user.getId(), new LinkedList<>() );
		
		
		LinkedList<TextMessage> queue = messageQueue.get(user.getId());
		for (TextMessage msg : messages) {
			queue.add(msg);
		}
	}
	
	public static int getMessagesCount(User from) {
		if (messageQueue.get(from.getId()) == null) {
			return 0;
		}
		return messageQueue.get(from.getId()).size();
	}
	
	public static List<TextMessage> getMessages(User user) {
		LinkedList<TextMessage> queue = messageQueue.get(user.getId());
		if (queue == null) {
			return null;
		}

		List<TextMessage> list = new LinkedList<>();
		while (!queue.isEmpty()) {
			list.add(queue.poll());
		}
		return list;
	}
	
}
