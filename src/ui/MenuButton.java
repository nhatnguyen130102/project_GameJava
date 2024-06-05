package ui;

import gameStates.GameState;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ultilz.Constants.UI.Buttons.*;

// tạo 3 buttons cho menuButton
public class MenuButton {
    private final int xPos;
    private final int yPos;
    private final int rowIndex;
    private int index;
    private final int xOffsetCenter = B_WIDTH / 2; // lấy 1/2 kích thước của bottun
    private final GameState state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;// hithox button

    // tạo MenuButton
    public MenuButton(int xPos, int yPos, int rowIndex, GameState state) {
        // xPos toạ độ x tại vị trí chính giữa screen <=> toạ độ x của button
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImg();//cắt từng img để vào array
        initBounds();// khởi tạo hitbox để tương tác
    }

    // tạo hitbox cho button
    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }// tạo 1 hitbox = kích thước button
    // xử lý cắt ảnh
    private void loadImg() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }
    // xử lý vẽ lại hình ảnh button
    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update() {
        index = 0; // khi chưa hover vào button
        if (mouseOver)
            index = 1; // khi hover vào button thì hiển thị trạng thái thứ 2 của button
        if (mousePressed)
            index = 2; // khi press vào button thì hiển thị trạng thái thứ 3 của button
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

    public void applyGamesLate() {
        GameState.state = state;
    }
    // sau khi hành động với button thì button ở trạng thái thứ 2 và thứ 3, trạng thái phải cần được reset lại khi không được tương tác hoặc đã hoàn thành chức năng sau khi tương tác
    public void resetBoots() {
        mouseOver = false;
        mousePressed = false;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
