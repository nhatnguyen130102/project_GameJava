package ui;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import ultilz.Constants;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static ultilz.Constants.UI.UrmButton.URM_SIZE;

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage img;
    private int imgX, imgY, imgW, imgH;
    private UrmButton menu, play;

    public GameOverOverlay(Playing playing){
        this.playing = playing;
        createImg();
        createButtons();
    }

    private void createButtons() {
//        int menuX = (int) (335 * Game.SCALE);
//        int playX = (int) (440 * Game.SCALE);
//        int y = (int) (195 * Game.SCALE);
        int menuX = (int) (Game.SCREEN_WIDTH / 2 - (URM_SIZE * 1.5 * Game.SCALE));
        int playX = (int) (Game.SCREEN_WIDTH / 2 + (URM_SIZE * 0.5 * Game.SCALE));
        int y = Game.SCREEN_HEIGHT / 2 - URM_SIZE / 3;
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void createImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        imgW = (int) (img.getWidth() * Game.SCALE);
        imgH = (int) (img.getHeight() * Game.SCALE);
        imgX = Game.SCREEN_WIDTH / 2 - imgW / 2;
        imgY = Game.SCREEN_HEIGHT / 2 - imgH / 2;
//        imgY = (int) (100 * Game.SCALE);
    }

    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0, Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);
        menu.draw(g);
        play.draw(g);

//        g.setColor(Color.white);
//        g.drawString("Game Over",Game.SCREEN_WIDTH/2 - 50,150);
//        g.drawString("Press esc to enter main Menu!",Game.SCREEN_WIDTH/2 - 100,300);
    }

    public void update() {
        menu.update();
        play.update();
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }
    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        play.setMouseOver(false);
        menu.setMouseOver(false);
        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(play, e))
            play.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(GameState.MENU);
            }
        } else if (isIn(play, e)) {
            if (play.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(GameState.PLAYING);
            }
        }
        menu.resetBools();
        play.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(play, e))
            play.setMousePressed(true);
    }
}
