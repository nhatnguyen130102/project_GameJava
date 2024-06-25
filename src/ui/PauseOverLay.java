package ui;

import gameStates.GameState;
import gameStates.Menu;
import gameStates.Playing;
import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static ultilz.Constants.UI.PauseButtons.*;
import static ultilz.Constants.UI.UrmButton.*;
import static ultilz.Constants.UI.VolumeButton.*;

public class PauseOverLay {
    private BufferedImage backGroundImg;
    private int bgX, bgY, bgW, bgH;
    private AudioOptions audioOptions;
    private UrmButton menuB, replayB, unpauseB;
    private final Playing playing;

    public PauseOverLay(Playing playing) {
        this.playing = playing;
        loadBackGround();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int margin = 20;
        int menuX = Game.SCREEN_WIDTH / 2 - URM_SIZE / 2 - URM_SIZE - margin;
        int replayX = Game.SCREEN_WIDTH / 2 - URM_SIZE / 2;
        int unpauseX = Game.SCREEN_WIDTH / 2 - URM_SIZE / 2 + URM_SIZE + margin;
        int bY = Game.SCREEN_HEIGHT / 2 + 110;

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }


    private void loadBackGround() {
        // lấy img lớn cho pausebg
        backGroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        // lấy thông số x,y,w,h
        bgW = (int) (backGroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backGroundImg.getHeight() * Game.SCALE);
        bgX = Game.SCREEN_WIDTH / 2 - bgW / 2;
        bgY = Game.SCREEN_HEIGHT / 2 - bgH / 2;
    }

    public void update() {
        // thực hiện update khi có sự thay đổi
        menuB.update();
        replayB.update();
        unpauseB.update();
        audioOptions.update();
    }

    public void draw(Graphics g) {
        // thực hiện vẽ bg chính
        g.drawImage(backGroundImg, bgX, bgY, bgW, bgH, null);
        // thực hiện vẽ các buttons thành phần
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        // kiểm tra buttom có được tương tác
        if (isIn(e, menuB)) menuB.setMousePressed(true);
        else if (isIn(e, unpauseB)) unpauseB.setMousePressed(true);
        else if (isIn(e, replayB)) replayB.setMousePressed(true);
        else audioOptions.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        // kiểm tra chuột có hoàn thành hành động để đặt trạng thái cho button
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                playing.setGameState(GameState.MENU);
//                playing.unpauseGame();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
//                System.out.println("Replay lvl!!!");
                playing.resetAll();
//                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) playing.unpauseGame();
        } else
            audioOptions.mouseReleased(e);

        // sau khi thực hiện hoàn tất 1 tương tác thì sẽ reset toàn bộ trạng thái của button
        menuB.resetBools();
        unpauseB.resetBools();
        replayB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        // đặt trạng thái trước khi được hover
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);

        // kiểm tra xem chuột có đang hover button không
        if (isIn(e, menuB)) menuB.setMouseOver(true);
        else if (isIn(e, replayB)) replayB.setMouseOver(true);
        else if (isIn(e, unpauseB)) unpauseB.setMouseOver(true);
        else audioOptions.mouseMoved(e);
    }

    public boolean isIn(MouseEvent e, PauseButton b) {
        // kiểm tra xem hitbox của button có đang chứa toạ độ của chuột
        return (b.getBounds().contains(e.getX(), e.getY()));
    }
}
