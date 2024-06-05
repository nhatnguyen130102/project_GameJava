package ui;

import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ultilz.Constants.UI.UrmButton.URM_SIZE;
import static ultilz.Constants.UI.UrmButton.URM_SIZE_DEFAULT;
import static ultilz.Constants.UI.VolumeButton.*;

public class VolumeButton extends PauseButton {
    BufferedImage[] imgs;
    BufferedImage slider;
    private int index;
    private boolean mouseOver, mousePressed;
    private int buttonX;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        buttonX = x + width / 2;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    public void draw(Graphics g) {
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX, y, VOLUME_WIDTH, height, null);
    }

    public void resetBools() {
        mousePressed = false;
        mouseOver = false;
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
}