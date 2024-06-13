package ui;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {
    private Playing playing;
    public GameOverOverlay(Playing playing){
        this.playing = playing;
    }
    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0, Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Game Over",Game.SCREEN_WIDTH/2 - 50,150);
        g.drawString("Press esc to enter main Menu!",Game.SCREEN_WIDTH/2 - 100,300);
    }
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }
}
