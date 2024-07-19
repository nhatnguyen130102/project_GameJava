package entities;

import gameStates.Playing;
import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import static main.Game.SCREEN_WIDTH;
import static ultilz.Constants.BombTiles.*;
import static ultilz.HelpMethods.*;


public class Bomb {
    private boolean jump = true;
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
    protected float airSpeed = 0f;
    private final float gravity = 0.04f * Game.SCALE;
    public float jumpSpeed;
    private final float fallSpeedAfterCollision = Game.SCALE;
    boolean inAir = false;
    private final int timeToExplode = 50000;
    private Timer explodeTimer;
    private float BombSpeed;
    private final Playing playing;
    private boolean picked = false;
    public boolean kicked = false;
    protected int PICKED = 0;
    protected int KICKED = 1;
    private int state = -1;
    private boolean canSeeByEnemy;
    public Bomb(Player player, int[][] lvlData, Playing playing, float jumpSpeed, float bombSpeed) {
        this.player = player;
        this.lvlData = lvlData;
        this.playing = playing;
        this.jumpSpeed = jumpSpeed;
        dir = player.getDir();
        hitbox = new Rectangle2D.Float(player.getHitBox().x, player.getHitBox().y, HB_BOMB_WIDTH, HB_BOMB_HEIGHT);
        loadImg();
        startExplodeTimer();
        BombSpeed = bombSpeed;
    }

    private void startExplodeTimer() {
        explodeTimer = new Timer();
        explodeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isActive) {
                    setExplode(true);
                    explodeHitbox = new Rectangle2D.Float(hitbox.x - BOMB_DRAW_OFFSET_X, hitbox.y - BOMB_DRAW_OFFSET_Y / 2, hitbox.width * 3, hitbox.height * 2);
                    checkExplode();
                }
            }
        }, timeToExplode);  // 2000 milliseconds = 2 seconds
    }


    public void update() {
        setAnimation();
        updatePos();
        updateFramesTick();
    }


    public void newBombState() {
        inAir = false;
        bounce = 0;
        jumpSpeed = -1.5f;
        BombSpeed = 1f;// bombSpeed la do dai x bom bay dc
    }

    public void newBombStateThrow() {
        inAir = false;
        bounce = 0;
        jumpSpeed = -1.5f;
        BombSpeed = 2f;
    }

    private void checkExplode() {
        playing.checkEmenyExplode(explodeHitbox);
        playing.checkObjectExplode(explodeHitbox);
        playing.checkPlayerExplode(explodeHitbox);
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
        g.drawString(canSeeByEnemy+"",(int) (hitbox.x - BOMB_DRAW_OFFSET_X - xLvlOffset),(int) (hitbox.y - BOMB_DRAW_OFFSET_Y - yLvlOffset));
    }

    public void changDir() {
        dir *= -1;
    }

    public void setDir(int value) {
        dir = value;
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
        if (!isExplode && state != PICKED) {
            if (jump) {
                jump();
            }
            float xSpeed = 0;
            if (dir == -1)//left
                xSpeed -= BombSpeed;
            if (dir == 1)//right
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
                    if (airSpeed >= 0)
                        resetInAir();
                    else
                        airSpeed = fallSpeedAfterCollision;
                }
            } else {
                udateXPos(xSpeed);
            }
        }
    }



    private void jump() {
        if (inAir) {
            return;
        }  // kiem tra trang thai cua vat the, han che vat the co the nhay nhieu lan tren khong
        inAir = true; // dat lai trang thai cho vat the
        airSpeed = jumpSpeed; // dat lai toc do roi cua vat the
        if (airSpeed < 0) {
            bounce += 0.3f;
            if (bounce >= 2) {
                bounce = 4;
            }
            airSpeed += bounce;
        }

    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void udateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {// kiểm tra vị trí nhân vật sắp di chuyển có chạm tile collision = true ?
            hitbox.x += xSpeed;
        } else {
            changDir();
        }
    }


    private void setAnimation() {
        int startFrame = bombAction;
//        if (moving) {
//            bombAction = BOMB_OFF;
//        } else {
//            bombAction = BOMB_ON;
//        }
        bombAction = BOMB_ON;
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

    protected Rectangle2D.Float getExplodeHitbox() {
        return explodeHitbox;
    }

    protected boolean canKickBomb() {
        return player.getAttackBox().intersects(hitbox);
    }

    protected void checkBombDir() {
        if (player.getDir() == -1) {
            dir = -1;
        }
        if (player.getDir() == 1) {
            dir = 1;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    protected boolean bigGuySeeBomb(BigGuy bigGuy){
        return (hitbox.intersects(bigGuy.canSeeBombBox));
    }
    protected boolean whaleSeeBomb(Whale whale){
        return (hitbox.intersects(whale.canSeeBombBox));
    }
    protected boolean baldSeeBomb(Bald bald){
        return (hitbox.intersects(bald.canSeeBombBox));
    }
    protected boolean captainSeeBomb(Captain captain){
        return (hitbox.intersects(captain.canSeeBombBox));
    }
    protected void setCanSeeByEnemy(boolean canSeeByEnemy){
        this.canSeeByEnemy = canSeeByEnemy;
    }

    protected boolean isCanSeeByEnemy(){
        return canSeeByEnemy;
    }
}
