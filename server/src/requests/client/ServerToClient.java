package requests.client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.MessagesService;
import model.client.ListenRequest;
import model.client.ListenRequestTypes;
import model.server.MediaMessage;
import model.server.Message;
import model.server.Request;
import model.server.RequestTypes;
import model.server.Response;
import model.server.TextMessage;
import model.server.User;
 
public class ServerToClient {
    
    private static String SERVER_IP = "127.0.0.1";
    private static int SERVER_PORT = 7777;
 
    private String ip;
    private int port;
    
    public ServerToClient(String ip, int port) {
    	this.ip   = ip;
    	this.port = port;
    }
    
    public model.client.Response sendRequest(ListenRequest req) {
    	try {
			Socket socket = connect();
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(req);
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			model.client.Response response = (model.client.Response) inputStream.readObject();
			socket.close();
			return response;
    	} catch (Exception e) {
    		if ( e instanceof ConnectException )
    			System.out.println("Can't connect to " + ip + ":" + port);
    		return new model.client.Response(Response.ERROR, false);
    	}
    }
    
    public model.client.Response sendTextMessage(model.client.TextMessage msg) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("message", msg);
    	
        ListenRequest req = new ListenRequest(ListenRequestTypes.NEW_TEXT_MESSAGE, content);
        model.client.Response resp = sendRequest(req);
        if ( (boolean) resp.getContent() ) {
        	List<Long> sentMessages = new ArrayList<Long>();
        	sentMessages.add(msg.getId());
        	MessagesService.markAsReadMessages(sentMessages);
        }
        
		return resp;
    }
    
    public void sendMediaMessage() {
    	
    	Map<String, Object> content = new HashMap<String, Object>();
   // 	content.put("message", msg);
    	
    	
        //Request req = new Request(RequestTypes.MESSAGE, content);
     //   sendRequest(req);
    }
    
    public model.client.Response sendFile(byte[] ar, String filename) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("blob", ar);
    	content.put("filename", filename);
    	
        ListenRequest req = new ListenRequest(ListenRequestTypes.SEND_FILE, content);
        return sendRequest(req);
    }
    
    public model.client.Response acceptFile(model.client.User user, String filename) {
    	Map<String, Object> content = new HashMap<String, Object>();
    	content.put("user", user);
    	content.put("filename", filename);
    	
        ListenRequest req = new ListenRequest(ListenRequestTypes.ACCEPT_FILE, content);
        return sendRequest(req);
    }
    
    private Socket connect() throws UnknownHostException, IOException {
    	return new Socket(ip, port);
    }
    
    private static String getLocalIP() throws SocketException {
    	Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    	while (interfaces.hasMoreElements()){
    	    NetworkInterface current = interfaces.nextElement();
    	    if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
    	    Enumeration<InetAddress> addresses = current.getInetAddresses();
    	    while (addresses.hasMoreElements()) {
    	        InetAddress current_addr = addresses.nextElement();
    	        if (current_addr.isLoopbackAddress()) continue;
    	        if (current_addr.isLinkLocalAddress()) continue;
    	        return current_addr.getHostAddress();
    	    }
    	}
    	return null;
    }
    
}