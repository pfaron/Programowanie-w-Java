package faron;

import java.io.*;
import java.net.Socket;

public class ClientSession extends Session {

    public ClientSession(Socket socket, String mapPath) throws IOException {
        super(socket, mapPath);
    }

    private void sendStart() throws IOException {
        lastAttackSent = cg.generate();

        previousAttacks.add(lastAttackSent);

        latestCorrectSentMessage = "start;" + lastAttackSent.column() + lastAttackSent.row() + "\n";

        //sendDOS();
        sendBW();
    }

    @Override
    public void run() {
        try {
            sendStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
