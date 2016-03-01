package client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.client.TextMessage;
import model.client.User;

public class TextMessageQueue {

	private static Map<Long, LinkedList<TextMessage>> messageQueue = new HashMap<Long, LinkedList<TextMessage> >();
	
	public static void addMessage(User user, TextMessage msg) {
		if ( messageQueue.get(user.getId()) == null )
			messageQueue.put( user.getId(), new LinkedList<TextMessage>() );
		
		messageQueue.get(user.getId()).add(msg);
	}
	
	public static void addMessages(User user, List<TextMessage> messages) {
		if ( messageQueue.get(user.getId()) == null )
			messageQueue.put( user.getId(), new LinkedList<TextMessage>() );
		
		
		LinkedList<TextMessage> queue = messageQueue.get(user.getId());
		for ( TextMessage msg : messages )
			queue.add(msg);
	}
	
	public static int getMessagesCount(User from) {
		if ( messageQueue.get(from.getId()) == null )
			return 0;
		
		return messageQueue.get(from.getId()).size();
	}
	
	public static List<TextMessage> getMessages(User user) {
		LinkedList<TextMessage> queue = messageQueue.get(user.getId());
		if ( queue == null )
			return null;
		
		List<TextMessage> list = new LinkedList<TextMessage>();
		while ( !queue.isEmpty() ) {
			list.add( queue.poll() );
		}
		
		return list;
	}
	
}
