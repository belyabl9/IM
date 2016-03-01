package client;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.FileService;
import model.client.Department;
import model.client.MediaMessage;
import model.client.MediaMessagePacket;
import model.client.Message;
import model.client.Request;
import model.client.RequestTypes;
import model.client.Response;
import model.client.StatusTypes;
import model.client.TextMessage;
import model.client.User;
 
public class Client {
    
    private static String SERVER_IP = "127.0.0.1";
    private static int SERVER_PORT = 6666;
 
    public Client() {    }
 
    public static void init(String serverIP, int port) {
    	SERVER_IP = serverIP;
    	SERVER_PORT = port;
    }
    
    public static Response sendRequest(Request req) {
		Response response = null;
    	try {
			Socket socket = connect();
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(req);
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			response = (Response) inputStream.readObject();
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return response;
    }
    
    public static void sendTextMessage(TextMessage msg) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("message", msg);
    	
        Request req = new Request(RequestTypes.TEXT_MESSAGE, content);
        Response resp = sendRequest(req);
		if (!resp.isSuccessful())
			System.out.println("Request wasn't successful");
    }
    
    public static boolean auth(String login, String password) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("login", login);
    	content.put("password", password);
    	
        Request req = new Request(RequestTypes.AUTH, content);
        Response resp = sendRequest(req);
		if (!resp.isSuccessful()) {
			System.out.println("Request wasn't successful");
			return false;
		}
		return (boolean) resp.getContent();
    }
    
    public static User findByLogin(String login) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("login", login);
    	
        Request req = new Request(RequestTypes.GET_USER, content);
        Response resp = sendRequest(req);
        return (User) resp.getContent();
    }
    
    public static User findById(long id) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("id", id);
    	
        Request req = new Request(RequestTypes.GET_USER, content);
        Response resp = sendRequest(req);
        return (User) resp.getContent();
    }
    
    public static Response sendMediaMessage(MediaMessagePacket msgPacket) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("message_packet", msgPacket);
    	
        Request req = new Request(RequestTypes.MEDIA_MESSAGE, content);
        Response resp = sendRequest(req);
		if (!resp.isSuccessful())
			System.out.println("Request wasn't successful");
		
		return resp;
    }
    
    public static List<MediaMessagePacket> getMediaMessages(Date fromDate) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("id_to", Session.getInstance().getUser().getId());
    	content.put("date_from", fromDate);
    	
        Request req = new Request(RequestTypes.MEDIA_MESSAGES, content);
        Response resp = sendRequest(req);
		if (!resp.isSuccessful()) {
			System.out.println("Request wasn't successful");
			return null;
		}
			
		return (List<MediaMessagePacket>) resp.getContent();
    }
    
    public static void saveMessages(List<MediaMessagePacket> packets) {
    	for ( MediaMessagePacket packet : packets) {
    		String filename = packet.msg.getPath();
    		File f = new File(filename);
    		if(!f.exists() && !f.isDirectory()) { 
    			FileService.saveFile(packet.blob, filename);
    		}
    	}
    }
    
    public static void removeMediaMessage(long id) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("id", id);
    	
        Request req = new Request(RequestTypes.REMOVE_MEDIA_MESSAGE, content);
        sendRequest(req);
    }
    
    public static Response sendFile(User from, User to, byte[] ar, String filename) {
    	
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("from", from);
    	content.put("to", to);
    	content.put("blob", ar);
    	content.put("filename", filename);
    	
        Request req = new Request(RequestTypes.SEND_FILE, content);
        return sendRequest(req);
    }
    
    public static List<User> getAllUsers() {
        Request req = new Request(RequestTypes.ALL_USERS);
        Response resp = sendRequest(req);
        List<User> users = (List<User>) resp.getContent();

        return users;
    }
    
    public static List<TextMessage> getUnsentMessages(long id_from, long id_to) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("id_to", id_to);
    	content.put("id_from", id_from);
    	
        Request req = new Request(RequestTypes.UNSENT_MESSAGES, content);
        Response resp = sendRequest(req);
        List<TextMessage> messages = (List<TextMessage>) resp.getContent();
        
        return messages;
    }
    
    public static void updateStatus(StatusTypes status) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	User user = Session.getInstance().getUser();
    	user.setStatus(status);
    	content.put("user", user);
    	
        Request req = new Request(RequestTypes.UPDATE_USER_INFO, content);
        Response resp = sendRequest(req);
    }
    
    public static void updateUserInfo(User user) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("user", user);
    	Session.getInstance().setUser(user);
        
    	Request req = new Request(RequestTypes.UPDATE_USER_INFO, content);
        Response resp = sendRequest(req);
    }
    
    public static void markAsReadMessages(List<Long> messagesIDs) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("messagesIDs", messagesIDs);
        
    	Request req = new Request(RequestTypes.MARK_AS_READ_MESSAGES, content);
        Response resp = sendRequest(req);
    }
    
    public static List<TextMessage> conversationHistory(long id_from, long id_to) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("id_to", id_to);
    	content.put("id_from", id_from);
        
    	Request req = new Request(RequestTypes.CONVERSATION_HISTORY, content);
        Response resp = sendRequest(req);
        List<TextMessage> messages = (List<TextMessage>) resp.getContent();
        
        return messages;
    }
 
    public static List<Department> getDepartments() {
    	Request req = new Request(RequestTypes.DEPARTMENTS, null);
        Response resp = sendRequest(req);
        List<Department> departments = (List<Department>) resp.getContent();
        
        return departments;
    }
    
    public static User createUser(User user) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("user", user);
    	
    	Request req = new Request(RequestTypes.NEW_USER, content);
        Response resp = sendRequest(req);
        
        return (User) resp.getContent();
    }
    
    public static Department createDepartment(Department department) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("department", department);
    	
    	Request req = new Request(RequestTypes.NEW_DEPARTMENT, content);
        Response resp = sendRequest(req);
        
        return (Department) resp.getContent();
    }
    
    private static Socket connect() throws UnknownHostException, IOException {
    	String serverIP = Session.getInstance().getServerIP() == null ? SERVER_IP : Session.getInstance().getServerIP();
    	int serverPort = Session.getInstance().getServerPort() == 0 ? SERVER_PORT : Session.getInstance().getServerPort();
    	
    	return new Socket(serverIP, serverPort);
    }
    
    public static void main(String[] args) {

    	Department department = new Department("createdDepartment");
    	department = createDepartment(department);
    	
    	User user = new User(0, "createdName", "createdSurname", "createdNickname", "cL", "c4ca4238a0b923820dcc509a6f75849b", StatusTypes.OFFLINE, new Date(), "", 0);
    	user.setDepartment(department);
    	createUser(user);
    	
	}

}
