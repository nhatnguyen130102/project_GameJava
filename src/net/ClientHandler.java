package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;


    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //
            out = new PrintWriter(socket.getOutputStream(), true);
            // tin nhan gui tu client(out) -> server -> clientHandler(in)
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                // server cap nhat lai tin nhan tu toan bo client
                System.out.println(message);
                // gui tin nhan cua clientA cho cac client con lai
                GameServer.broadcast(message, this);
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

    public void sendMessage(String message) {
        out.println(message);
    }
}
