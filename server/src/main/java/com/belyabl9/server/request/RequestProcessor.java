package com.belyabl9.server.request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.belyabl9.api.Request;
import com.belyabl9.api.RequestType;
import com.belyabl9.api.Response;
import org.springframework.context.ApplicationContext;

public class RequestProcessor implements Runnable {

	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	private ApplicationContext ctx;
	
    private static String PROCESS_METHOD = "process";
    private static final Map<RequestType, String> classes;
    
    private static final String REQUEST_PATH_PREFIX = "com.belyabl9.server.request.processor";
    
    static
    {
    	classes = new HashMap<>();
    	classes.put(RequestType.CREATE_NEW_USER, REQUEST_PATH_PREFIX + ".NewUserRequestProcessor");
    	classes.put(RequestType.CREATE_NEW_DEPARTMENT, REQUEST_PATH_PREFIX + ".NewDepartmentRequestProcessor");
    	classes.put(RequestType.GET_DEPARTMENTS, REQUEST_PATH_PREFIX + ".AllDepartmentsRequestProcessor");
    	classes.put(RequestType.GET_UPDATES, REQUEST_PATH_PREFIX + ".GetUpdatesRequestProcessor");
    	classes.put(RequestType.SEND_TEXT_MESSAGE, REQUEST_PATH_PREFIX + ".SendTextMessageRequestProcessor");
    	classes.put(RequestType.SEND_MEDIA_MESSAGE, REQUEST_PATH_PREFIX + ".SendMediaMessageRequestProcessor");
    	classes.put(RequestType.REMOVE_MEDIA_MESSAGE, REQUEST_PATH_PREFIX + ".RemoveMediaMessageRequestProcessor");
    	classes.put(RequestType.GET_MEDIA_MESSAGES, REQUEST_PATH_PREFIX + ".MediaMessagesRequestProcessor");
    	classes.put(RequestType.GET_UNSENT_MESSAGES, REQUEST_PATH_PREFIX + ".UnsentMessagesRequestProcessor");
    	classes.put(RequestType.GET_ALL_USERS, REQUEST_PATH_PREFIX + ".AllUsersRequestProcessor");
    	classes.put(RequestType.UPDATE_USER_INFO, REQUEST_PATH_PREFIX + ".UpdateUserInfoRequestProcessor");
    	classes.put(RequestType.GET_USER, REQUEST_PATH_PREFIX + ".GetUserRequestProcessor");
    	classes.put(RequestType.AUTH, REQUEST_PATH_PREFIX + ".AuthRequestProcessor");
    	classes.put(RequestType.MARK_MESSAGES_AS_READ, REQUEST_PATH_PREFIX + ".MarkAsReadMessagesRequestProcessor");
    	classes.put(RequestType.CONVERSATION_HISTORY, REQUEST_PATH_PREFIX + ".ConversationHistoryRequestProcessor");
    }
    
	public RequestProcessor(Socket socket, ApplicationContext ctx) {
		this.socket = socket;
		try {
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.ctx = ctx;
	}
    
	private void processRequest(Request request) {
		Class<?> requestProcessorClass;
		try {
			requestProcessorClass = Class.forName(classes.get(request.getType()));

			Object requestProcessor = ctx.getBean(requestProcessorClass);
			Method processMethod = requestProcessor.getClass().getMethod(PROCESS_METHOD, Object.class);
			Response response = (Response) processMethod.invoke(requestProcessor, request.getContent());
			sendResponse(response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	private Request getRequest() {
		try {
			return (Request) inputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void sendResponse(Response response) {
		try {
			outputStream.writeObject(response);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void run() {
		processRequest(getRequest());
	}
}
