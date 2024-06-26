package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SecondaryWindow extends JFrame {
    private JTextArea textArea;
    private PrintWriter out;

    public SecondaryWindow() {
        setTitle("Secondary Window - Player 2");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton sendButton = new JButton("Send Message");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("Hello from Player 2");
            }
        });
        add(sendButton, BorderLayout.SOUTH);

        setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        try {
            // Chờ một vài giây để đảm bảo rằng máy chủ đã khởi động
            Thread.sleep(2000);

            Socket socket = new Socket("localhost", 12345);
            System.out.println("Connected to server.");
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(new IncomingReader(socket)).start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private class IncomingReader implements Runnable {
        private Socket socket;

        public IncomingReader(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    textArea.append("Player 1: " + message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SecondaryWindow();
    }
}
