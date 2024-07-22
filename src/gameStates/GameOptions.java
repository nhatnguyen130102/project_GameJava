package gameStates;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import ultilz.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static ultilz.Constants.UI.UrmButton.URM_SIZE;

public class GameOptions  extends State implements StateMethods {
    private AudioOptions audioOptions;
    private BufferedImage bgImg, optionsBgImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB;
//    private boolean firstUpdate;
    public GameOptions(Game game) {
        super(game);
        loadBackGround();
        audioOptions = game.getAudioOptions();
        loadButton();

    }

    private void loadButton() {

        int menuX = (int) ((Game.SCREEN_WIDTH / 2 - URM_SIZE / 2) * Game.SCALE);
        int menuY = (int) ((Game.SCREEN_HEIGHT / 2 + bgH / 4 + 15) * Game.SCALE);
        menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    private void loadBackGround() {

        bgImg = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND);
        optionsBgImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);
        bgW = (int) (optionsBgImg.getWidth() * Game.SCALE);
        bgH = (int) (optionsBgImg.getHeight() * Game.SCALE);
        bgX = Game.SCREEN_WIDTH / 2 - bgW / 2;
        bgY = Game.SCREEN_HEIGHT / 2 - bgH / 2 + 10; //+10 because it uses draw method of audioOptions
    }

    @Override
    public void update() {
        menuB.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) {
//        if(firstUpdate){
//            if(GameState.state == GameState.OPTIONS)
//            {
//                String[] options = {"Lựa chọn 1", "Lựa chọn 2"};
//
//                // Hiển thị dialog với các lựa chọn
//                int choice = JOptionPane.showOptionDialog(
//                        null,
//                        "Chọn một trong các lựa chọn sau:",
//                        "Lựa chọn",
//                        JOptionPane.DEFAULT_OPTION,
//                        JOptionPane.INFORMATION_MESSAGE,
//                        null,
//                        options,
//                        options[0]
//                );
//            }
//        }
        g.drawImage(bgImg, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
        g.drawImage(optionsBgImg, bgX, bgY, bgW, bgH, null);

        menuB.draw(g);
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) menuB.setMousePressed(true);
        else audioOptions.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed())
                GameState.state = GameState.MENU;
        }
        else audioOptions.mouseReleased(e);

        menuB.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);

        if (isIn(e, menuB)) menuB.setMouseOver(true);
        else audioOptions.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            GameState.state = GameState.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void test() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public boolean isIn(MouseEvent e, PauseButton b) {
        // kiểm tra xem hitbox của button có đang chứa toạ độ của chuột
        return (b.getBounds().contains(e.getX(), e.getY()));
    }
}
