package net;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadMessage implements Runnable  {
    private BufferedReader in;
    private String name;

    public ReadMessage(BufferedReader in,String name) {
        this.in = in;
        this.name = name;
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                System.out.println( "readMessage"+ message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
