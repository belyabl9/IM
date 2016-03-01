package service;

import java.util.Date;
import java.util.List;

import model.server.MediaMessage;
import model.server.Message;
import model.server.TextMessage;
import model.server.User;
import dao.DaoFactory;
import dao.MessagesDAO;
import dao.UsersDAO;

public class MessagesService {
	
	public static Message addMessage(Message msg) {
		MessagesDAO msgDAO = DaoFactory.getMessagesDAO();
		return msgDAO.save(msg);
	}
	
	public static List<TextMessage> getHistory(long id_from, long id_to) {
		MessagesDAO msgDAO = DaoFactory.getMessagesDAO();
		return msgDAO.getHistory(id_from, id_to);
	}
	
	public static List<MediaMessage> getMediaMessages(long id_to, Date dateFrom) {
		MessagesDAO msgDAO = DaoFactory.getMessagesDAO();
		return msgDAO.getMediaMessages(id_to, dateFrom);
	}
	
	public static void remove(long id) {
		MessagesDAO msgDAO = DaoFactory.getMessagesDAO();
		msgDAO.remove(id);
	}
	
	public static List<TextMessage> getUnsentMessages(long id_from, long id_to) {
		MessagesDAO msgDAO = DaoFactory.getMessagesDAO();
		return msgDAO.getUnsentMessages(id_from, id_to);
	}
	
	public static void markAsReadMessages(List<Long> messagesIDs) {
		MessagesDAO msgDAO = DaoFactory.getMessagesDAO();
		msgDAO.markAsReadMessages(messagesIDs);
	}
	
	public static void updateInfo(User user) {
		UsersDAO dao = DaoFactory.getUsersDAO();
		dao.save(user);
	}
	
}
