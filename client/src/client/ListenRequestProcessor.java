package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import model.client.ListenRequest;
import model.client.ListenRequestTypes;
import model.client.Response;

public class ListenRequestProcessor implements Runnable {

	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
    private static String PROCESS_METHOD = "process";
    private static final Map<ListenRequestTypes, String> classes;
    
    static
    {
    	classes = new HashMap<ListenRequestTypes, String>();
    	classes.put(ListenRequestTypes.NEW_TEXT_MESSAGE, "requests.NewMessageRequest");
    	classes.put(ListenRequestTypes.SEND_FILE, "requests.SendFileRequest");
    	classes.put(ListenRequestTypes.ACCEPT_FILE, "requests.FileAcceptRequest");
    }
	
	public ListenRequestProcessor(Socket socket) {
		this.socket = socket;
		try {
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	private void sendResponse(Response response) {
		try {
			outputStream.writeObject(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void processRequest(ListenRequest request) {
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
	
	private ListenRequest getRequest() {
		ListenRequest request = null;
		try {
			request = (ListenRequest) inputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return request;
	}
	
	
	@Override
	public void run() {
		processRequest(getRequest());
	}

}
