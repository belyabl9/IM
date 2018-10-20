package com.belyabl9.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import com.belyabl9.server.config.ServerConfiguration;
import com.belyabl9.server.request.RequestProcessor;
import com.belyabl9.server.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Server implements Runnable {
    
    @Value("${im.server.port:6666}")
    private int serverPort;

    private ServerSocket   serverSocket  = null;
    private boolean        isStopped     = false;
    
    private Thread runningThread;

    private ApplicationContext ctx;
    
    public Server(ApplicationContext ctx){
        this.ctx = ctx;
    }

    public void run(){
        synchronized(this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    log.error("The server has stopped: ", e);
                    return;
                }
                log.error("Error accepting client connection: {}", e);
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(new RequestProcessor(clientSocket, ctx)).start();
        }
        log.error("The server has stopped.");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
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
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ServerConfiguration.class);

        Server server = ctx.getBean(Server.class);
        UsersService usersService = ctx.getBean(UsersService.class);

        new Thread(server).start();
    	
    	new Thread(() -> {
            while (true) {
                usersService.updateOfflineUsers();
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
	}
    
}
