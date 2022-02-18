package faron;

import faron.map.DefaultBattleshipGenerator;
import faron.map.PlayerBattleshipMap2D;
import java.io.IOException;
import java.net.*;

public class CommandExecutor {
    static public void execute(String[] args) throws IOException {
        switch (args[0]) {
            case "-mode":
                if (args[1].equals("server")) {
                    startServer(args);
                } else if (args[1].equals("client")) {
                    startClient(args);
                }
                break;
            case "-generate":
                PlayerBattleshipMap2D map = new DefaultBattleshipGenerator().generateMap();

                map.printToFile(args[1]);
                break;
            case "-help":
            case "-h":
                Help.displayHelp();
                break;
            default:
                System.out.println("Incorrect command. Please use -help or -h for help.");
                break;
        }
    }

    static private void startServer(String[] args) throws IOException {
        int port = 0;
        String mapPath = null;
        String networkInterfaceName = "wlp3s0";
        for (int i = 2; i < 6; i++) {
            switch (args[i]) {
                case "-port" -> port = Integer.parseInt(args[++i]);
                case "-map" -> mapPath = args[++i];
                case "-interface" -> networkInterfaceName = args[++i];
            }
        }


        NetworkInterface networkInterface = NetworkInterface.getByName(networkInterfaceName);
        InetAddress inetAddress = networkInterface.inetAddresses()
                .filter(a -> a instanceof Inet4Address)
                .findFirst()
                .orElse(InetAddress.getLocalHost());

        ServerSocket serverSocket = new ServerSocket(port, 1, inetAddress);

        System.out.println("Server IP: " + serverSocket.getInetAddress().getHostAddress() + ", port: " + serverSocket.getLocalPort());

        Socket socket = serverSocket.accept();
        socket.setSoTimeout(1000);

        Thread thread = new Thread(new ServerSession(socket, mapPath, serverSocket));
        thread.start();

    }

    static private void startClient(String[] args) throws IOException {
        int port = 0;
        String mapPath = null;
        String address = null;
        for (int i = 2; i < 8; i++) {
            switch (args[i]) {
                case "-port" -> port = Integer.parseInt(args[++i]);
                case "-map" -> mapPath = args[++i];
                case "-address" -> address = args[++i];
            }
        }


        Socket socket = new Socket(address, port);
        socket.setSoTimeout(1000);

        Thread thread = new Thread(new ClientSession(socket, mapPath));
        thread.start();


    }
}