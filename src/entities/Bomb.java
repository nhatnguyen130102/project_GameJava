package entities;

import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import static ultilz.Constants.BombTiles.*;
import static ultilz.Constants.Projectiles.SPEED;
import static ultilz.HelpMethods.*;


public class Bomb {
    private boolean jump = true;
    private float bounceValue;
    private boolean isActive = true;
    private boolean isExplode = false;
    BufferedImage[][] temp;
    private Rectangle2D.Float hitbox;
    private int dir;
    private Player player;
    private int framesTick;
    private int bombAction = BOMB_OFF;
    private int framesSpeed = 25;
    private int framesIndex;
    private int[][] lvlData;
    private float airSpeed = 0f; // van toc roi cua vat the v0 -> vN
    private final float gravity = 0.04f * Game.SCALE; // luc hap dan______gia toc trong truong
    private final float jumpSpeed = -3.25f * Game.SCALE; // do cao khi nhay cua vat the
    private final float fallSpeedAfterCollision = 1f * Game.SCALE; // toc do roi khi cham phai tile o phia tren
    boolean inAir = false; // trang thai cua vat the
    private boolean moving;
    private double timeToExplode = 2;// 2s
    private float previousX;
    private float previousY;
    private Timer explodeTimer;

    public Bomb(Player player, int[][] lvlData) {
        this.player = player;
        this.lvlData = lvlData;
        dir = player.getDir();
        hitbox = new Rectangle2D.Float(player.getHitBox().x, player.getHitBox().y, HB_BOMB_WIDTH, HB_BOMB_HEIGHT);
        loadImg();
        previousY = (int) hitbox.y + 1;
        previousX = (int) hitbox.x + 1;
        startExplodeTimer();
    }

    private void startExplodeTimer() {
        explodeTimer = new Timer();
        explodeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                setExplode(true);
            }
        }, 4000);  // 2000 milliseconds = 2 seconds
    }

    public void update() {
        moving = checkIfMoving();
        setAnimation();
        updatePos();
        updateFramesTick();
    }

    private void loadImg() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.BOMB_SPRITE);
        int framesRow = img.getHeight() / BOMB_HEIGHT_DEFAULT;
        int framesCol = img.getWidth() / BOMB_WIDTH_DEFAULT;
        temp = new BufferedImage[framesRow][framesCol];
        for (int i = 0; i < framesRow; i++)
            for (int j = 0; j < framesCol; j++)
                temp[i][j] = img.getSubimage(j * BOMB_WIDTH_DEFAULT, i * BOMB_HEIGHT_DEFAULT, BOMB_WIDTH_DEFAULT, BOMB_HEIGHT_DEFAULT);
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.drawImage(temp[bombAction][framesIndex], (int) (hitbox.x - BOMB_DRAW_OFFSET_X - xLvlOffset), (int) (hitbox.y - BOMB_DRAW_OFFSET_Y - yLvlOffset), (int) (BOMB_WIDTH), (int) (BOMB_HEIGHT), null);
//        drawhitBox(g, xLvlOffset, yLvlOffset);
    }

    public void changDir() {
        dir *= -1;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void drawhitBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.drawRect((int) (hitbox.x - xLvlOffset), (int) (hitbox.y - yLvlOffset), HB_BOMB_WIDTH, HB_BOMB_HEIGHT);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setExplode(boolean isExplode) {
        this.isExplode = isExplode;
    }

    public boolean isExplode() {
        return isExplode;
    }

    protected void newState(int bombAction) {
        this.bombAction = bombAction;
        framesTick = 0;
        framesIndex = 0;
    }

    private void updatePos() {
        if (jump) // kiem tra su kien tu ban phim de xem nguoi choi co yeu cau jump hay khong
            jump(); // han che viec vat the nhay nhieu lan tren khong trung va dat do cao khi bay cua vat th
        float xSpeed = 0; // dat khoang cach di duoc
        // kiem tra xem nhan vat di chuyen theo huong nao
        if (dir == -1) {
            xSpeed -= SPEED;
        }
        if (dir == 1) {
            xSpeed += SPEED;
        }
        // kiem tra vat the co dang tren khong hay khong
        if (!inAir)
            // neu vat the dang khong o tren khong trung thi kiem tra xem vat the co dang o tren mat dat hay khong
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                // neu vat the khong o tren mat dat thi dat trang thai inAir = true cho vat the
                inAir = true;
            }

        // kiem tra vat the co dang o tren khong trung
        if (inAir) {
            // kiem tra phia tren cua vat the co tile hay khong
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed; // thuc hien viec giam gia tri Y
                airSpeed += gravity; // thuc hien viec giam gia tri airSpeed den khi nhan vat roi xuong
                udateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
            }
        } else {
            udateXPos(xSpeed);
        }
    }

    private boolean checkIfMoving() {
        boolean moving = hitbox.x != previousX && hitbox.y != previousY;
        previousX = hitbox.x;
        previousY = hitbox.y;
        return moving;
    }


    private void jump() {
        if (inAir) {
            return;
        }  // kiem tra trang thai cua vat the, han che vat the co the nhay nhieu lan tren khong
        inAir = true; // dat lai trang thai cho vat the
        bounceValue += 0.5;
        airSpeed = jumpSpeed + bounceValue; // dat lai toc do roi cua vat the
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void udateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))// kiểm tra vị trí nhân vật sắp di chuyển có chạm tile collision = true ?
            hitbox.x += xSpeed;
        else {
            changDir();
        }
    }


    private void setAnimation() {
        int startFrame = bombAction;
        if (moving) {
            bombAction = BOMB_OFF;
        } else {
            bombAction = BOMB_ON;
        }
        if (isExplode) {
            bombAction = BOMB_EXPLOTION;
            if (startFrame != BOMB_EXPLOTION) {
                framesIndex = 0;
                framesTick = 0;
                return;
            }
        }
        if (startFrame != bombAction)
            resetFrameTick();
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
            if (framesIndex >= GetSpriteAmount(bombAction)) {
                framesIndex = 0;
                if (bombAction == BOMB_EXPLOTION) {
                    isActive = false;
                    isExplode = false;
                }
            }
        }
    }
}
