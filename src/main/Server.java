package main;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 4;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private CopyOnWriteArrayList<PrintWriter> clientWriters;

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            pool = Executors.newFixedThreadPool(MAX_PLAYERS);
            clientWriters = new CopyOnWriteArrayList<>();
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                clientWriters.add(out);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcast(String message) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
