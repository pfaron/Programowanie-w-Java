package faron;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSession extends Session {

    private final ServerSocket serverSocket;

    public ServerSession(Socket socket, String mapPath, ServerSocket serverSocket) throws IOException {
        super(socket, mapPath);
        this.serverSocket = serverSocket;
    }

    @Override
    protected void closeSockets() throws IOException {
        super.closeSockets();
        serverSocket.close();
    }

    @Override
    public void run() {
        while(true){
            try {
                receive();
                send();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-2);
            }
        }
    }
}
