package faron;

import faron.map.EnemyBattleshipMap2D;
import faron.map.PlayerBattleshipMap2D;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;

public class Session implements Runnable {

    protected PlayerBattleshipMap2D myMap;
    protected EnemyBattleshipMap2D enemyMap;

    protected final BufferedWriter out;
    protected final BufferedReader in;

    protected final DataOutputStream writer;
    protected final DataInputStream reader;

    protected CoordinatesGenerator cg;
    protected Set<Coordinates> previousAttacks;
    protected Coordinates lastAttackSent;
    protected Coordinates lastAttackReceived;
    protected String latestCorrectSentMessage;
    protected String latestCorrectReceivedMessage;
    protected Commands lastFeedback;
    protected Socket socket;

    protected Session(Socket socket, String mapPath) throws IOException {

        writer = new DataOutputStream(socket.getOutputStream());
        reader = new DataInputStream(socket.getInputStream());

        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.myMap = new PlayerBattleshipMap2D(10, 10);
        this.myMap.readFromFile(mapPath);
        this.enemyMap = new EnemyBattleshipMap2D(10, 10);

        this.cg = new CoordinatesGenerator(10, 10);

        this.previousAttacks = new HashSet<>();

        this.lastAttackSent = null;
        this.lastAttackReceived = null;

        this.latestCorrectSentMessage = "";
        this.latestCorrectReceivedMessage = "";

        this.lastFeedback = Commands.NONE;

        this.socket = socket;
    }

    protected void sendBW() throws IOException {
        System.out.println("Sending message: " + latestCorrectSentMessage);
        out.write(latestCorrectSentMessage);
        out.flush();
    }

    protected void sendDOS() throws IOException {
        System.out.println("Sending message: " + latestCorrectSentMessage);
        writer.writeUTF(latestCorrectSentMessage);
        writer.flush();
    }

    protected void closeSockets() throws IOException {
        socket.close();
    }

    protected String receiveMessage(int tryNo) throws IOException {
        String message = "";
        try {
            //message = reader.readUTF();
            message = in.readLine();
        } catch (SocketTimeoutException e) {
            if (tryNo == 4) {
                System.out.println("Błąd komunikacji");
                endWithStatus(-1);
            }
            resendMessage();
            message = receiveMessage(tryNo + 1);
        }
        return message;
    }

    protected void resendMessage() throws IOException {
        System.out.print("Resending: ");

        //sendDOS();
        sendBW();
    }

    protected void receive() throws IOException {

        String message;
        //do {
            message = receiveMessage(1);
        //} while (message.equals(latestCorrectReceivedMessage));
        latestCorrectReceivedMessage = message.replaceAll("\n","");

        System.out.println("Receiving message: " + latestCorrectReceivedMessage);

        String[] commands = latestCorrectReceivedMessage.split(";");

        boolean correctFormatting = true;

        switch (commands[0]) {
            case "start" -> {

            }
            case "pudło" -> {
                enemyMap.markAsMiss(lastAttackSent.row() - 1, CoordinatesUtil.changeToIndex(lastAttackSent.column()));
            }
            case "trafiony" -> {
                enemyMap.markAsHit(lastAttackSent.row() - 1, CoordinatesUtil.changeToIndex(lastAttackSent.column()));
            }
            case "trafiony zatopiony" -> {
                enemyMap.markAsHitAndSunk(lastAttackSent.row() - 1, CoordinatesUtil.changeToIndex(lastAttackSent.column()));
            }
            case "ostatni zatopiony" -> {
                gameWon();
            }
            default -> {
                resendMessage();
                receive();
                correctFormatting = false;
            }
        }

        if (correctFormatting) {
            lastAttackReceived = CoordinatesUtil.extractCoords(commands[1]);

            lastFeedback = myMap.shoot(lastAttackReceived.row() - 1, CoordinatesUtil.changeToIndex(lastAttackReceived.column()));
        }
    }

    protected void send() throws IOException {

        StringBuilder toBeSent = new StringBuilder();

        switch (lastFeedback) {
            case MISS -> {
                toBeSent.append("pudło;");
            }
            case HIT -> {
                toBeSent.append("trafiony;");
            }
            case HIT_SUNK -> {
                toBeSent.append("trafiony zatopiony;");
            }
            case LAST_SUNK -> {
                toBeSent.append("ostatni zatopiony");
                toBeSent.append("\n");

                latestCorrectSentMessage = toBeSent.toString();

                //sendDOS();
                sendBW();

                gameLost();
            }
        }

        do {
            lastAttackSent = cg.generate();
        } while (previousAttacks.contains(lastAttackSent));

        previousAttacks.add(lastAttackSent);
        toBeSent.append(lastAttackSent.column());
        toBeSent.append(lastAttackSent.row());
        toBeSent.append("\n");

        latestCorrectSentMessage = toBeSent.toString();

        //sendDOS();
        sendBW();

    }

    protected void gameWon() throws IOException {
        System.out.println("Wygrana");
        System.out.println(enemyMap.printAsStringWithoutQuestionMarks());
        System.out.println();
        System.out.println(myMap.printAsStringWithBattle());
        endWithStatus(0);
    }

    protected void gameLost() throws IOException {
        System.out.println("Przegrana");
        System.out.println(enemyMap.printAsStringWithQuestionMarks());
        System.out.println();
        System.out.println(myMap.printAsStringWithBattle());
        endWithStatus(0);
    }

    protected void endWithStatus(int status) throws IOException {
        closeSockets();
        System.exit(status);
    }

    @Override
    public void run() {

    }
}
