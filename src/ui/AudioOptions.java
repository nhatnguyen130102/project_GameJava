package ui;

import gameStates.GameState;
import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static ultilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static ultilz.Constants.UI.VolumeButton.SLIDER_WIDTH;
import static ultilz.Constants.UI.VolumeButton.VOLUME_HEIGHT;

public class AudioOptions {
    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;
    private Game game;

    public AudioOptions(Game game) {
        this.game = game;
        createSoundButtons();
        createVolumeButtons();
    }

    private void createVolumeButtons() {
        int vX = (int) (Game.SCREEN_WIDTH / 2 - SLIDER_WIDTH / 2 - 2);
        int vY = (int) (Game.SCREEN_HEIGHT / 2 + 60);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createSoundButtons() {
        int soundX = Game.SCREEN_WIDTH / 2 + SOUND_SIZE;
        int soundY = Game.SCREEN_HEIGHT / 2 - 80;
        int sfxY = Game.SCREEN_HEIGHT / 2 - 30;
        musicButton = new SoundButton(soundX, soundY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    public  void update() {
        //sound buttons
        musicButton.update();
        sfxButton.update();
        //volume button
        volumeButton.update();
    }

    public void draw(Graphics g) {
        //sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);
        //volume button
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            float valueBefore = volumeButton.getFloatValue();
            volumeButton.changeX(e.getX());
            float valueAfter = volumeButton.getFloatValue();
            if (valueAfter != valueBefore) {
                game.getAudioPlayer().setVolume(valueAfter);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        // kiểm tra buttom có được tương tác
        if (isIn(e, musicButton)) musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton)) sfxButton.setMousePressed(true);
        else if (isIn(e, volumeButton)) volumeButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        // kiểm tra chuột có hoàn thành hành động để đặt trạng thái cho button
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
                game.getAudioPlayer().toggleSongMute();
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                game.getAudioPlayer().toggleEffectMute();
            }
        }

        // sau khi thực hiện hoàn tất 1 tương tác thì sẽ reset toàn bộ trạng thái của button
        musicButton.resetBools();
        sfxButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        // đặt trạng thái trước khi được hover
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        // kiểm tra xem chuột có đang hover button không
        if (isIn(e, musicButton)) musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton)) sfxButton.setMouseOver(true);
        else if (isIn(e, volumeButton)) volumeButton.setMouseOver(true);
    }

    public boolean isIn(MouseEvent e, PauseButton b) {
        // kiểm tra xem hitbox của button có đang chứa toạ độ của chuột
        return (b.getBounds().contains(e.getX(), e.getY()));
    }
}
