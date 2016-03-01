package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import model.client.StatusTypes;
import model.client.User;

public class RequestListener implements Runnable {

    private int            serverPort;
    private ServerSocket   serverSocket  = null;
    private boolean        isStopped     = false;
    private Thread         runningThread;

    public RequestListener(int port){
        this.serverPort = port;
    }

    public void run(){
        synchronized(this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            new Thread(new ListenRequestProcessor(clientSocket)).start();
        }
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private String getLocalIP() throws SocketException {
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
    
    private void updateUserSocketInfo() throws SocketException {
		User user = Session.getInstance().getUser();
		user.setIP(getLocalIP() != null ? getLocalIP() : "127.0.0.1");
		user.setPort(serverSocket.getLocalPort());
		user.setStatus(StatusTypes.ONLINE);
		Client.updateUserInfo(user);
    }
    
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
            updateUserSocketInfo();
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

    public static void main(String[] args) {
    	RequestListener server = new RequestListener(0);
    	new Thread(server).start();
	}
    
}
