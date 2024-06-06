package gameStates;

import main.Game;
import ui.MenuButton;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods {
    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backGroundImg;
    private BufferedImage backGroundImg2;

    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackGround();
        loadMenuBackGround();
    }

    private void loadMenuBackGround() {
        backGroundImg2 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND);
    }

    private void loadBackGround() {
        backGroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backGroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backGroundImg.getHeight() * Game.SCALE);
        menuX = Game.SCREEN_WIDTH / 2 - menuWidth / 2;
        menuY = Game.SCREEN_HEIGHT / 2 - menuHeight / 2;
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.SCREEN_WIDTH / 2, (int) (160 * Game.SCALE), 0, GameState.PLAYING);
        buttons[1] = new MenuButton(Game.SCREEN_WIDTH / 2, (int) (230 * Game.SCALE), 1, GameState.OPTIONS);
        buttons[2] = new MenuButton(Game.SCREEN_WIDTH / 2, (int) (300 * Game.SCALE), 2, GameState.QUIT);
    }

    @Override
    public void update() {
        // update từng button
        for (MenuButton mb : buttons)
            mb.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backGroundImg2, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
        g.drawImage(backGroundImg, menuX, menuY, menuWidth, menuHeight, null);
        // draw từng button
        for (MenuButton mb : buttons)
            mb.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed()) {
                    mb.applyGamesLate();
                    break;
                }
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButton mb : buttons) {
            mb.resetBoots();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons)
            mb.setMouseOver(false);

        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            GameState.state = GameState.PLAYING;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void test() {

    }
}
