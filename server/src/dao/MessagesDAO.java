package dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;

import model.server.MediaMessage;
import model.server.Message;
import model.server.TextMessage;
import model.server.User;

public class MessagesDAO extends GenericDAO<Message> {

	public MessagesDAO(SessionFactory factory) {
		super(Message.class, factory);
	}
	
	public List<MediaMessage> getMediaMessages(long id_to, Date dateFrom) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id_to", id_to);
		
		if (dateFrom != null) {
			map.put("dateFrom", dateFrom);
			return executeQuery("from MediaMessage where i_user_to = :id_to and date > :dateFrom order by date", false, map);
		}
		
		return executeQuery("from MediaMessage where i_user_to = :id_to order by date", false, map);
	}
	
	public void remove(long id) {
		delete(findById(id));
	}
	
	public List<TextMessage> getUnsentMessages(long id_from, long id_to) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id_from", id_from);
		map.put("id_to", id_to);
		
		return executeQuery("from TextMessage where i_user_from = :id_from and i_user_to = :id_to and sent is false", false, map);
	}
	
	public void markAsReadMessages(List<Long> messagesIDs) {
		for (long id : messagesIDs) {
			Message msg = findById(id);
			msg.setSent(true);
			save(msg);
		}
	}
	
	public List<TextMessage> getHistory(long id_from, long id_to) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id_from", id_from);
		map.put("id_to", id_to);
		
		return executeQuery("from TextMessage where ( i_user_from = :id_from and i_user_to = :id_to ) or ( i_user_from = :id_to and i_user_to = :id_from ) order by date", false, map);
	}
	
}
