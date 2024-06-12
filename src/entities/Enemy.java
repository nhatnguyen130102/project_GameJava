package entities;

import main.Game;

import static ultilz.Constants.Directions.*;
import static ultilz.Constants.EnemyConstants.*;
import static ultilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    protected int frameIndex, enemyState, enemyTpye;
    protected int frameTick, frameSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallSpeed;
    protected float gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 0.5f * Game.SCALE;
    protected int walkDir = RIGHT;
    protected int tileY;
    protected float attackDistance = Game.TILE_SIZE;
    public Enemy(float x, float y, int width, int height, int enemyTpye) {
        super(x, y, width, height);
        this.enemyTpye = enemyTpye;
        initHitBox(x, y, width, height);
    }

    protected void updateFrameTick() {
        frameTick++;
        if (frameTick >= frameSpeed) {
            frameTick = 0;
            frameIndex++;
            if (frameIndex >= GetSpriteAmount(enemyTpye, enemyState)) {
                frameIndex = 0;
                if(enemyState == ATTACK){
                    enemyState = IDLE;
                }
            }
        }
    }
    protected void firstUpdateChecked(int[][]lvlData){
        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][]lvlData){
        if (CanMoveHere(hitBox.x, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
            tileY = (int) (hitBox.y/Game.TILE_SIZE);
        }
    }
    protected void move(int[][]lvlData){
        float XSpeed = 0;
        if (walkDir == LEFT)
            XSpeed = -walkSpeed;
        else
            XSpeed = +walkSpeed;
        if (CanMoveHere(hitBox.x + XSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            if (IsFloor(hitBox, XSpeed, lvlData)) {
                hitBox.x += XSpeed;
                return;
            }
        }
        changeWalkDir();
    }
    protected void turnTowardsPlayer(Player player){
        if(player.hitBox.x > hitBox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected void newState(int enemyState){
        this.enemyState = enemyState;
        frameTick = 0 ;
        frameIndex = 0;
    }

    protected boolean canSeePlayer(int[][]lvlData,Player player){
        int playerTileY = (int) (player.getHitBox().y/Game.TILE_SIZE);
        if(playerTileY == tileY)
            if(isPlayerInRange(player))
                if(isSightClear(lvlData,hitBox,player.hitBox,tileY))
                    return true;
        return false;
    }

    private boolean isPlayerInRange(Player player) {
        // lay khoang cach giua player va enemy vi nhan vat co the dung ben trai hoac ben phai nen lay ABS(tri tuyet doi)
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance * 5; // kiem tra xem khoang cach giua player va enemy co <= 5 tile khong
    }
    protected boolean isPlayerCloseForAttack(Player player){
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        if(Math.abs(hitBox.y - player.hitBox.y) < Game.TILE_SIZE ) // neu player khong dung cung 1 con duong voi enemy thi enemy se khong tan cong
            return absValue <= attackDistance ; // kiem tra xem khoang cach giua player va enemy co <= 5 tile khong
        return false;
    }

    protected void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}
