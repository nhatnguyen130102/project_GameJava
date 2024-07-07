package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainWindow extends JFrame {
    private JTextArea textArea;
    private PrintWriter out;

    public MainWindow() {
        setTitle("Main Window - Player 1");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton sendButton = new JButton("Send Message");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("Hello from Player 1");
            }
        });
        add(sendButton, BorderLayout.SOUTH);

        setVisible(true);

        startServer();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            Socket socket = serverSocket.accept();
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(new IncomingReader(socket)).start();
        } catch (IOException e) {
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
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    textArea.append("Player 2: " + message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new MainWindow();
        new SecondaryWindow();
    }
}
