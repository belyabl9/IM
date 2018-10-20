package com.belyabl9.client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.belyabl9.api.*;
import com.belyabl9.client.ui.contacts.ContactListPanel;
import com.belyabl9.client.ui.nav.NavigationSide;
import com.belyabl9.client.utils.FileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    private static String SERVER_IP = "127.0.0.1";
    private static int SERVER_PORT = 6666;
 
    public Client() {    }
 
    public static void init(String serverIp, int port) {
    	SERVER_IP = serverIp;
    	SERVER_PORT = port;
    }
    
    public static Response sendRequest(Request req) {
		Response response;
    	try {
			Socket socket = connect();
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(req);
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			response = (Response) inputStream.readObject();
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return response;
    }
    
    public static void sendTextMessage(TextMessage msg) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("message", msg);
    	
        Request req = new Request(RequestType.SEND_TEXT_MESSAGE, content);
        Response resp = sendRequest(req);
    }
    
    public static boolean auth(String login, String password) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("login", login);
    	content.put("password", password);

        Request req = new Request(RequestType.AUTH, content);
        Response resp = sendRequest(req);
		if (!resp.isSuccessful()) {
			log.debug("An authentication request wasn't successful.");
			return false;
		}
		return (boolean) resp.getContent();
    }
    
    public static User findByLogin(String login) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("login", login);
    	
        Request req = new Request(RequestType.GET_USER, content);
        Response resp = sendRequest(req);
        return (User) resp.getContent();
    }
    
    public static User findById(long id) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("id", id);
    	
        Request req = new Request(RequestType.GET_USER, content);
        Response resp = sendRequest(req);
        return (User) resp.getContent();
    }
    
    public static Response sendMediaMessage(Message msg) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("message_packet", msg);
    	
        Request req = new Request(RequestType.SEND_MEDIA_MESSAGE, content);
		return sendRequest(req);
    }
    
    public static List<MediaMessage> getMediaMessages(Date fromDate) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("id_to", Session.getInstance().getUser().getId());
    	content.put("date_from", fromDate);
    	
        Request req = new Request(RequestType.GET_MEDIA_MESSAGES, content);
        Response resp = sendRequest(req);

		return (List<MediaMessage>) resp.getContent();
    }
    
    public static void removeMediaMessage(long id) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("id", id);
    	
        Request req = new Request(RequestType.REMOVE_MEDIA_MESSAGE, content);
        sendRequest(req);
    }
    
    public static List<User> getAllUsers() {
        Request req = new Request(RequestType.GET_ALL_USERS);
        Response resp = sendRequest(req);

        return (List<User>) resp.getContent();
    }
    
    public static List<TextMessage> getUnsentMessages(long id_from, long id_to) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("id_to", id_to);
    	content.put("id_from", id_from);

        Request req = new Request(RequestType.GET_UNSENT_MESSAGES, content);
        Response resp = sendRequest(req);
        
        return (List<TextMessage>) resp.getContent();
    }
    
    public static void updateStatus(StatusType status) {
    	Map<String, Object> content = new HashMap<>();
    	User user = Session.getInstance().getUser();
    	user.setStatus(status);
    	content.put("user", user);
    	
        Request req = new Request(RequestType.UPDATE_USER_INFO, content);
        Response resp = sendRequest(req);
    }

    public static void fetchAndProcessUpdates(long idTo) {
        Map<String, Object> content = new HashMap<>();
        content.put("idTo", idTo);
        
        Request req = new Request(RequestType.GET_UPDATES, content);
        Response resp = sendRequest(req);
        List<Message> messages = (List<Message>) resp.getContent();
        for (Message message : messages) {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message; 
                User from = textMessage.getFrom();
                if (!NavigationSide.addMessage(from, textMessage)) {
                    TextMessageQueue.addMessage(from, textMessage);
                    ContactListPanel.getContactPanel( from.getId() ).showNewMessagesNotification(1);
                }
            } else if (message instanceof MediaMessage) {
                MediaMessage mediaMessage = (MediaMessage) message;
                File media = new File("media");
                if (!media.exists()) {
                    media.mkdirs();
                }
                FileService.saveFile(mediaMessage.getBlob(), media.getAbsolutePath() + "/" + mediaMessage.getPath());
            }
        }
    }
    
    public static void updateUserInfo(User user) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("user", user);
    	Session.getInstance().setUser(user);
        
    	Request req = new Request(RequestType.UPDATE_USER_INFO, content);
        Response resp = sendRequest(req);
    }
    
    public static void markAsReadMessages(List<Long> messagesIDs) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("messagesIDs", messagesIDs);
        
    	Request req = new Request(RequestType.MARK_MESSAGES_AS_READ, content);
        Response resp = sendRequest(req);
    }
    
    public static List<TextMessage> conversationHistory(long id_from, long id_to) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("id_to", id_to);
    	content.put("id_from", id_from);
        
    	Request req = new Request(RequestType.CONVERSATION_HISTORY, content);
        Response resp = sendRequest(req);
        
        return (List<TextMessage>) resp.getContent();
    }
 
    public static List<Department> getDepartments() {
    	Request req = new Request(RequestType.GET_DEPARTMENTS, null);
        Response resp = sendRequest(req);
        
        return (List<Department>) resp.getContent();
    }
    
    public static User createUser(User user) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("user", user);
    	
    	Request req = new Request(RequestType.CREATE_NEW_USER, content);
        Response resp = sendRequest(req);
        
        return (User) resp.getContent();
    }
    
    public static Department createDepartment(Department department) {
    	Map<String, Object> content = new HashMap<>();
    	content.put("department", department);
    	
    	Request req = new Request(RequestType.CREATE_NEW_DEPARTMENT, content);
        Response resp = sendRequest(req);
        
        return (Department) resp.getContent();
    }
    
    private static Socket connect() throws IOException {
    	String serverIP = Session.getInstance().getServerIp() == null ? SERVER_IP : Session.getInstance().getServerIp();
    	int serverPort = Session.getInstance().getServerPort() == 0 ? SERVER_PORT : Session.getInstance().getServerPort();
    	
    	return new Socket(serverIP, serverPort);
    }

}
