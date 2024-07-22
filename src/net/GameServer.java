package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameServer {
    private static final int PORT = 23;
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server is running...");
            System.out.println(serverSocket);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler excludeClient) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != excludeClient) {
//                    System.out.println(message+"Test");
                    client.sendMessage(message);
                }
            }
        }
    }
}
