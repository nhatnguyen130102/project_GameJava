package main;

import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String serverAddress) {
        try {
            socket = new Socket(serverAddress, 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(new IncomingReader()).start();
            sendMessage("Hello, Server!");

            // Ví dụ gửi dữ liệu từ game
            // sendGameData(gameData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Server: " + message);
                    // Xử lý dữ liệu nhận được từ server
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public static void main(String[] args) {
        new Client("localhost");
    }
}
