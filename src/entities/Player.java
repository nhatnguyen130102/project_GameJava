package entities;

import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ultilz.Constants.PlayerConstants.*;
import static ultilz.HelpMethods.*;

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
    public float speed = 3f * Game.SCALE;
    private boolean up, right, left, down, jump;
    private int[][] lvlData;
    private float xDrawOfset = 21 * Game.SCALE;
    private float yDrawOfset = 4 * Game.SCALE;
    // Jumping Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -4f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitBox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE));
    }

    public void update() {
        updatePos();
        updateFramesTick();
        setAnimation();
    }

    public void render(Graphics g, int xLvlOffset) {
        g.drawImage(frames[playerAction][framesIndex], (int) (hitBox.x - xDrawOfset) - xLvlOffset, (int) (hitBox.y - yDrawOfset), imgWidth, imgHeight, null);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
    }

    private void updatePos() {
        moving = false;
        if (jump)
            jump();
//        if (!left && !right && !inAir)
//            return;
        if(!inAir)
            if((!left && !right)||(left && right))
                return;
        float xSpeed = 0;
        if (left)
            xSpeed -= speed;
        if (right)
            xSpeed += speed;
        if (!inAir)
            if (!IsEntityOnFloor(hitBox, lvlData))
                inAir = true;


        if (inAir) {
            if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
                udateXPos(xSpeed);
            } else {
                hitBox.y = GetEntityYPosUnderRootOrAboveFloor(hitBox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
            }
        } else
            udateXPos(xSpeed);
        moving = true;

    }

    private void jump() {
        if (inAir) {
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;

    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void udateXPos(float xSpeed) {
        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += xSpeed;
        } else {
            hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
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
        if (inAir) {
            if (airSpeed < 0) {
                playerAction = JUMP;
            } else {
                playerAction = FALLING;
            }
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

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
}
