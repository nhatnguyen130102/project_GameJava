package gameStates;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverLay;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods {
    private LevelManager levelManager;
    private Player player;
    private boolean paused = false;
    private PauseOverLay pauseOverLay;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
//        levelManager = new LevelManager(game);
        levelManager = new LevelManager();
        player = new Player(100, 300, Player.imgWidth, Player.imgHeight);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        pauseOverLay = new PauseOverLay(this);
    }


    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
        } else {
            pauseOverLay.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        player.render(g);
        if (paused)
            pauseOverLay.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            player.setAttacking(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused)
            pauseOverLay.mousePressed(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused)
            pauseOverLay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused)
            pauseOverLay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_D -> player.setRight(true);
            case KeyEvent.VK_SPACE -> player.setJump(true);
            case KeyEvent.VK_BACK_SPACE -> GameState.state = GameState.MENU;
            case KeyEvent.VK_ESCAPE -> paused = !paused;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(false);
            case KeyEvent.VK_D -> player.setRight(false);
            case KeyEvent.VK_SPACE -> player.setJump(false);
        }
    }

    @Override
    public void test() {

    }

    public Player getPlayer() {
        return player;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public void unpauseGame() {
        paused = false;
    }
}
