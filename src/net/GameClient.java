package net;

import main.Game;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 23;
    private int count = 0;
    public static void main(String[] args) {
        // Hiển thị hộp thoại yêu cầu người dùng nhập tên
        String username = JOptionPane.showInputDialog(null, "Enter your name:", "Username", JOptionPane.PLAIN_MESSAGE);

        // Kiểm tra nếu người dùng không nhập tên và nhấn Cancel hoặc đóng hộp thoại
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username is required. Exiting...");
            return; // Thoát chương trình nếu không có tên người dùng
        }
        Game game = new Game();
        game.getPlayer().setName(username);


        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            // gui tin nhan len server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // doc tin nhan tu server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // doc tin nhan tu ban phim
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connected to the server as " + username);
            // hien thi tin nhan tu cac client khac, tin nhan nay dc gui tu server
            new Thread(new ReadMessage(in, username)).start();
//            String userInput = game.getPlayer().getHitBox().x + " - " + game.getPlayer().getHitBox().y;
            String userInput;
            // doc tin nhan tu ban phim
//            while ((userInput = stdIn.readLine()) != null) {
//                // out la gui dong tin nhan qua server
//                out.println(username + ": " + userInput);
//            }

            while (true){
                userInput = game.getPlayer().getHitBox().x + " - " + game.getPlayer().getHitBox().y;
                if (userInput != null) {
                    out.println(username + " Game Client: " + userInput);
                    new Thread(new ReadMessage(in, username)).start();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
