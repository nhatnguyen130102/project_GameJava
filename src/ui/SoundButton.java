package ui;

import ultilz.Constants;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static ultilz.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButton {
    PauseButton pauseButton;
    private BufferedImage[][]  soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex,colIndex;
    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImgs();// cắt img lớn thành img con
    }

    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImgs = new BufferedImage[2][3];
        for(int i = 0 ; i < 2; i ++){
            for(int j = 0 ; j < 3 ; j ++){
                soundImgs[i][j] = temp.getSubimage(j * SOUND_SIZE_DEFAULT , i * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
            }
        }
    }
    public void update(){
        if(muted)
            rowIndex = 1;
        else
            rowIndex = 0;

        colIndex = 0;

        if(mouseOver)
            colIndex = 1;
        if(mousePressed)
            colIndex = 2;
    }
    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }
    public void draw(Graphics g){
        g.drawImage(soundImgs[rowIndex][colIndex],x,y,SOUND_SIZE,SOUND_SIZE,null);
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
