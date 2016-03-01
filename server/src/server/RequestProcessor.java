package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import model.client.Request;
import model.client.RequestTypes;
import model.client.Response;

public class RequestProcessor implements Runnable {

	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
    private static String PROCESS_METHOD = "process";
    private static final Map<RequestTypes, String> classes;
    
    static
    {
    	classes = new HashMap<RequestTypes, String>();
    	classes.put(RequestTypes.NEW_USER, "requests.server.NewUserRequest");
    	classes.put(RequestTypes.NEW_DEPARTMENT, "requests.server.NewDepartmentRequest");
    	classes.put(RequestTypes.DEPARTMENTS, "requests.server.AllDepartmentsRequest");
    	classes.put(RequestTypes.TEXT_MESSAGE, "requests.server.SendTextMessageRequest");
    	classes.put(RequestTypes.MEDIA_MESSAGE, "requests.server.SendMediaMessageRequest");
    	classes.put(RequestTypes.REMOVE_MEDIA_MESSAGE, "requests.server.RemoveMediaMessageRequest");
    	classes.put(RequestTypes.MEDIA_MESSAGES, "requests.server.MediaMessagesRequest");
    	classes.put(RequestTypes.UNSENT_MESSAGES, "requests.server.UnsentMessagesRequest");
    	classes.put(RequestTypes.ALL_USERS, "requests.server.AllUsersRequest");
    	classes.put(RequestTypes.UPDATE_USER_INFO, "requests.server.UpdateUserInfoRequest");
    	classes.put(RequestTypes.GET_USER, "requests.server.GetUserRequest");
    	classes.put(RequestTypes.SEND_FILE, "requests.server.SendFileRequest");
    	classes.put(RequestTypes.AUTH, "requests.server.AuthRequest");
    	classes.put(RequestTypes.MARK_AS_READ_MESSAGES, "requests.server.MarkAsReadMessages");
    	classes.put(RequestTypes.CONVERSATION_HISTORY, "requests.server.ConversationHistory");
    }
	
	public RequestProcessor(Socket socket) {
		this.socket = socket;
		try {
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	public void processRequest(Request request) {
		Class<?> content_class;
		try {
			content_class = Class.forName(classes.get(request.getType()));
			Method method = content_class.getMethod(PROCESS_METHOD, Object.class);
			Response response = (Response) method.invoke(null, request.getContent());
			sendResponse(response);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}
	
	private Request getRequest() {
		Request request = null;
		try {
			request = (Request) inputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return request;
	}
	
	private void sendResponse(Response response) {
		try {
			outputStream.writeObject(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		processRequest(getRequest());
	}
}
