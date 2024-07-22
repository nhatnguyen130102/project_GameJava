package net;

import entities.PlayerClone;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadMessage implements Runnable  {
    private BufferedReader in;
    private PlayerClone playerClone;
    public ReadMessage(BufferedReader in, PlayerClone playerClone) {
        this.in = in;
        this.playerClone = playerClone;
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                String[] getPosClone = message.split("-");
//                System.out.println(getPosClone[0]+getPosClone[1]);
                float x = Float.parseFloat(getPosClone[0]);
                float y = Float.parseFloat(getPosClone[1]);

                int intX = (int) x;
                int intY = (int) y;

                playerClone.setPos(intX,intY);
//                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
