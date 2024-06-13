package entities;

import gameStates.Playing;
import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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
    private final int playerDir = -1;
    private boolean moving = false, attacking = false;
    public float speed = Game.SCALE;
    private boolean up, left, down, jump, right;
    private boolean firstUpdate = true;
    private int[][] lvlData;
    private final float xDrawOfset = 21 * Game.SCALE; // phần thừa ngang của hitbox
    private final float yDrawOfset = 4 * Game.SCALE; // phần thừa dọc của hitbox
    // Jumping Gravity
    private float airSpeed = 0f; // van toc roi cua vat the v0 -> vN
    private final float gravity = 0.04f * Game.SCALE; // luc hap dan______gia toc trong truong
    private final float jumpSpeed = -4.25f * Game.SCALE; // do cao khi nhay cua vat the
    private final float fallSpeedAfterCollision = 0.5f * Game.SCALE; // toc do roi khi cham phai tile o phia tren
    private boolean inAir = false; // trang thai cua vat the

    private BufferedImage statusBarImg;
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);
    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flitW = 1;

    private boolean attackChecked;

    private Playing playing;
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitBox(x, y, HB_PLAYER_WIDTH, HB_PLAYER_HEIGHT);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 20 * Game.SCALE, 20 * Game.SCALE);
    }

    public void update() {
        updateHealthBar();
        if(currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        updatePos();
        if(attacking)
            checkAttack();
        updateFramesTick();
        setAnimation();
    }

    private void checkAttack() {
        if(attackChecked || framesIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if (firstUpdate) {
            attackBox.x = hitBox.x + hitBox.width + Game.SCALE * 10;
            firstUpdate = false;
        }
        if (right)
            attackBox.x = hitBox.x + hitBox.width + Game.SCALE * 10;
        else if (left)
            attackBox.x = hitBox.x - hitBox.width - Game.SCALE * 10;
        attackBox.y = hitBox.y + Game.SCALE * 10;
    }

    private void updateHealthBar() {
        healthWidth =(int)((currentHealth / (float)maxHealth )*healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(frames[playerAction][framesIndex],
                (int) (hitBox.x - xDrawOfset) - lvlOffset + flipX,
                (int) (hitBox.y - yDrawOfset),
                imgWidth * flitW, imgHeight,
                null);
//        drawHitBox(g, lvlOffset);
//        drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.RED);
        g.drawRect((int) attackBox.x - lvlOffsetX, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY,healthWidth, healthBarHeight);
    }

    public void changeHealth(int value) {
        currentHealth += value;
        if (currentHealth <= 0) {
            currentHealth = 0;
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitBox, lvlData)) // kiểm tra nhân vật có trên mặt đất hay không
            inAir = true;
    }

    private void updatePos() {
        moving = false; // neu khong co gi dien ra thi vat the se dung im
        if (jump) // kiem tra su kien tu ban phim de xem nguoi choi co yeu cau jump hay khong
            jump(); // han che viec vat the nhay nhieu lan tren khong trung va dat do cao khi bay cua vat the
        if (!inAir)
            // kiem tra su kien tu ban phim xem nguoi dung co dang nhan cung luc A D hoac la khong nhan ca 2 phim A D hay khong
            // neu true thi keu thuc func con neu false thi tiep tuc thuc hien
            if ((!left && !right) || (left && right))
                return;
        float xSpeed = 0; // dat khoang cach di duoc
        // kiem tra xem nhan vat di chuyen theo huong nao
        if (left) {
            xSpeed -= speed;
            flipX = width;
            flitW = -1;
        }
        if (right) {
            xSpeed += speed;
            flipX = 0;
            flitW = 1;
        }
        // kiem tra vat the co dang tren khong hay khong
        if (!inAir)
            // neu vat the dang khong o tren khong trung thi kiem tra xem vat the co dang o tren mat dat hay khong
            if (!IsEntityOnFloor(hitBox, lvlData))
                // neu vat the khong o tren mat dat thi dat trang thai inAir = true cho vat the
                inAir = true;

        // kiem tra vat the co dang o tren khong trung
        if (inAir) {
            // kiem tra phia tren cua vat the co tile hay khong
            if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed; // thuc hien viec giam gia tri Y
                airSpeed += gravity; // thuc hien viec giam gia tri airSpeed den khi nhan vat roi xuong
                udateXPos(xSpeed);
            } else {
                hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
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
        if (inAir)  // kiem tra trang thai cua vat the, han che vat the co the nhay nhieu lan tren khong
            return;
        inAir = true; // dat lai trang thai cho vat the
        airSpeed = jumpSpeed; // dat lai toc do roi cua vat the
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void udateXPos(float xSpeed) {
        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData))// kiểm tra vị trí nhân vật sắp di chuyển có chạm tile collision = true ?
            hitBox.x += xSpeed;
        else
            hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    // kiểm tra xem state của vật thể la gi va dat lai state cho vat the
    private void setAnimation() {
        int startFrame = playerAction;
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
        if (inAir)
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        if (attacking){
            playerAction = ATTACK;
            if(startFrame != ATTACK){
                framesIndex = 1;
                framesTick  = 0;
                return;
            }
        }
        if (startFrame != playerAction)
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
            if (framesIndex >= getSpriteAmout(playerAction)) {
                framesIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void loadAnimations() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        framesRow = 7;
        framesCol = 8;
        frames = new BufferedImage[framesRow][framesCol];
        for (int j = 0; j < framesRow; j++)
            for (int i = 0; i < framesCol; i++)
                frames[j][i] = image.getSubimage(i * imgDefaultW, j * imgDefaultH, imgDefaultW, imgDefaultH);

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);

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

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction =IDLE;
        currentHealth = maxHealth;

        hitBox.x = x;
        hitBox.y = y;

        if(!IsEntityOnFloor(hitBox,lvlData))
            inAir = true;
    }
}
