package ui;

import gameStates.GameState;
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
    private SoundButton musicButton, sfxButton;
    private UrmButton menuB, replayB, unpauseB;
    private VolumeButton volumeButton;
    private final Playing playing;

    public PauseOverLay(Playing playing) {
        this.playing = playing;
        loadBackGround();
        createSoundButtons();
        createUrmButtons();
        createVolumeButtons();
    }

    private void createVolumeButtons() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (280 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createUrmButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);

    }

    private void createSoundButtons() {
        int soundX = (int) (450 * Game.SCALE);
        int soundY = (int) (145 * Game.SCALE);
        int sfxY = (int) (191 * Game.SCALE);
        musicButton = new SoundButton(soundX, soundY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
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
        musicButton.update();
        sfxButton.update();
        menuB.update();
        replayB.update();
        unpauseB.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        // thực hiện vẽ bg chính
        g.drawImage(backGroundImg, bgX, bgY, bgW, bgH, null);
        // thực hiện vẽ các buttons thành phần
        musicButton.draw(g);
        sfxButton.draw(g);

        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }
    }

    public void mouseMoved(MouseEvent e) {
        // đặt trạng thái trước khi được hover
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);
        volumeButton.setMouseOver(false);
        // kiểm tra xem chuột có đang hover button không
        if (isIn(e, musicButton)) musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton)) sfxButton.setMouseOver(true);
        else if (isIn(e, menuB)) menuB.setMouseOver(true);
        else if (isIn(e, replayB)) replayB.setMouseOver(true);
        else if (isIn(e, unpauseB)) unpauseB.setMouseOver(true);
        else if (isIn(e, volumeButton)) volumeButton.setMouseOver(true);
    }

    public void mousePressed(MouseEvent e) {
        // kiểm tra buttom có được tương tác
        if (isIn(e, musicButton)) musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton)) sfxButton.setMousePressed(true);
        else if (isIn(e, menuB)) menuB.setMousePressed(true);
        else if (isIn(e, unpauseB)) unpauseB.setMousePressed(true);
        else if (isIn(e, replayB)) replayB.setMousePressed(true);
        else if (isIn(e, volumeButton)) volumeButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        // kiểm tra chuột có hoàn thành hành động để đặt trạng thái cho button
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) musicButton.setMuted(!musicButton.isMuted());
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) sfxButton.setMuted(!sfxButton.isMuted());
        } else if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) GameState.state = GameState.MENU;
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) playing.unpauseGame();
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                System.out.println("Replay lv!!!");
                playing.resetAll();
//                playing.unpauseGame();
            }
        }
        // sau khi thực hiện hoàn tất 1 tương tác thì sẽ reset toàn bộ trạng thái của button
        musicButton.resetBools();
        sfxButton.resetBools();
        menuB.resetBools();
        unpauseB.resetBools();
        replayB.resetBools();
        volumeButton.resetBools();
    }

    public boolean isIn(MouseEvent e, PauseButton b) {
        // kiểm tra xem hitbox của button có đang chứa toạ độ của chuột
        return (b.getBounds().contains(e.getX(), e.getY()));
    }
}
