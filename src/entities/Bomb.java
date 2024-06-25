package entities;

import gameStates.Playing;
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
    private final boolean jump = true;
    private float bounce;
    private boolean isActive = true;
    private boolean isExplode = false;
    BufferedImage[][] temp;
    private final Rectangle2D.Float hitbox;
    private Rectangle2D.Float explodeHitbox;
    private int dir;
    private final Player player;
    private int framesTick;
    private int bombAction = BOMB_OFF;
    private final int framesSpeed = 25;
    private int framesIndex;
    private final int[][] lvlData;
    private float airSpeed = 0f; // van toc roi cua vat the v0 -> vN
    private final float gravity = 0.04f * Game.SCALE; // luc hap dan______gia toc trong truong
    private float jumpSpeed; // do cao khi nhay cua vat the -5.25f * Game.SCALE
    private final float fallSpeedAfterCollision = Game.SCALE; // toc do roi khi cham phai tile o phia tren
    boolean inAir = false; // trang thai cua vat the
    private boolean moving;
    private final int timeToExplode = 2000;// 2s
    private float previousX;
    private float previousY;
    private Timer explodeTimer;
    private float BombSpeed;
    private final Playing playing;

    public Bomb(Player player, int[][] lvlData, Playing playing, float jumpSpeed) {
        this.player = player;
        this.lvlData = lvlData;
        this.playing = playing;
        this.jumpSpeed = jumpSpeed;

        dir = player.getDir();
        hitbox = new Rectangle2D.Float(player.getHitBox().x, player.getHitBox().y, HB_BOMB_WIDTH, HB_BOMB_HEIGHT);

        loadImg();
        previousY = (int) hitbox.y + 1;
        previousX = (int) hitbox.x + 1;
        startExplodeTimer();
        BombSpeed = 0.75f;
    }

    private void startExplodeTimer() {
        explodeTimer = new Timer();
        explodeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                setExplode(true);
//                BombSpeed=0;
//                airSpeed=0;
//                jumpSpeed=0;
                explodeHitbox = new Rectangle2D.Float(hitbox.x - BOMB_DRAW_OFFSET_X, hitbox.y - BOMB_DRAW_OFFSET_Y / 2, hitbox.width * 3, hitbox.height * 2);
                checkExplode();
            }
        }, timeToExplode);  // 2000 milliseconds = 2 seconds
    }

    public void update() {
        moving = checkIfMoving();
        setAnimation();
        updatePos();
        updateFramesTick();
    }

    private void checkExplode() {
        playing.checkEmenyExplode(explodeHitbox);
        playing.checkObjectExplode(explodeHitbox);
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
        g.drawImage(temp[bombAction][framesIndex], (int) (hitbox.x - BOMB_DRAW_OFFSET_X - xLvlOffset), (int) (hitbox.y - BOMB_DRAW_OFFSET_Y - yLvlOffset), BOMB_WIDTH, BOMB_HEIGHT, null);
//        drawhitBox(g, xLvlOffset, yLvlOffset);
//        if(explodeHitbox != null)
//            drawExplodeBox(g, xLvlOffset, yLvlOffset);

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

    public void drawExplodeBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.drawRect((int) (explodeHitbox.x - xLvlOffset), (int) (explodeHitbox.y - yLvlOffset), (int) explodeHitbox.width, (int) explodeHitbox.height);
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

    private void updatePos() {
        if (!isExplode) {
            if (jump)
                jump();

            float xSpeed = 0;

            if (dir == -1)
                xSpeed -= BombSpeed;

            if (dir == 1)
                xSpeed += BombSpeed;

            if (!inAir)
                if (!IsEntityOnFloor(hitbox, lvlData))
                    inAir = true;
            if (inAir) {
                if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                    hitbox.y += airSpeed;
                    airSpeed += gravity;
                    udateXPos(xSpeed);
                } else {
                    hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                    if (airSpeed >= 0){
                        resetInAir();
                    }
                    else
                        airSpeed = fallSpeedAfterCollision;
                }
            } else
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
        bounce += 0.2;
        airSpeed = jumpSpeed + bounce ; // dat lai toc do roi cua vat the
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
