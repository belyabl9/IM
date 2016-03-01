package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import service.UsersService;

public class Server implements Runnable {

    private int            serverPort    = 6666;
    private ServerSocket   serverSocket  = null;
    private boolean        isStopped     = false;
    private Thread         runningThread = null;

    public Server(int port){
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
            new Thread(new RequestProcessor(clientSocket)).start();
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

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

    public static void main(String[] args) {
    	Server server = new Server(6666);
    	new Thread(server).start();
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					UsersService.processOfflineUsers();
					try {
						Thread.sleep(10000 * 60 * 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
    
}
