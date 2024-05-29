package entities;

import main.Game;
import ultilz.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;

import static ultilz.Constants.PlayerConstants.*;
import static ultilz.HelpMethods.CanMoveHere;

public class Player extends Entity {

    BufferedImage[][] frames;
    private int framesRow, framesCol;

    public static int imgDefaultW = 64;
    public static int imgDefaultH = 40;
    public static int imgWidth = (int) (imgDefaultW * Game.SCALE);
    public static int imgHeight = (int) (imgDefaultH * Game.SCALE);
    private int framesTick;
    private int framesIndex;
    private final int framesSpeed = 30;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false, attacking = false;
    public float speed = 1f;
    private boolean up, right, left, down;
    private int[][] lvlData;

    public Player(float x, float y, int width, int hight) {
        super(x, y, width, hight);
        loadAnimations();
    }

    public void update() {
        updatePos();
        updateHitBox();
        updateFramesTick();
        setAnimation();

    }

    public void render(Graphics g) {
        g.drawImage(frames[playerAction][framesIndex], (int) x, (int) y, imgWidth, imgHeight, null);
        drawHitBox(g);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    private void updatePos() {
        moving = false;
        if(!left  && !right && !up && !down){
            return;
        }
        float xSpeed = 0, ySpeed = 0;
        if (left && !right) {
            xSpeed -= speed;

        } else if (right && !left) {
            xSpeed += speed;
        }
        if (up && !down) {
            ySpeed -= speed;
        } else if (down && !up) {
            ySpeed += speed;
        }
        if(CanMoveHere(x+xSpeed,y+ySpeed,width,height,lvlData)){
            this.x += xSpeed;
            this.y += ySpeed;
            moving = true;
        }
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;

    }

    private void setAnimation() {
        int starFrame = playerAction;

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if (attacking) {
            playerAction = ATTACK_1;
        }

        if (starFrame != playerAction) {
            resetFrameTick();
        }
    }

    private void resetFrameTick() {
        framesTick = 0;
        framesIndex = 0;
    }

    private void updateFramesTick() {
        framesTick++;
        if (framesTick >= framesSpeed) {
            framesTick = 0;
            framesIndex++;
            if (framesIndex >= getSpriteAmout(playerAction)) {
                framesIndex = 0;
                attacking = false;
            }
        }
    }


    private void loadAnimations() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        framesRow = 9;
        framesCol = 6;
        frames = new BufferedImage[framesRow][framesCol];
        for (int j = 0; j < framesRow; j++) {
            for (int i = 0; i < framesCol; i++) {
                frames[j][i] = image.getSubimage(i * imgDefaultW, j * imgDefaultH, imgDefaultW, imgDefaultH);
            }
        }

    }


    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
}
