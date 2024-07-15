package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ultilz.Constants.Directions.LEFT;
import static ultilz.Constants.Directions.RIGHT;
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
    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true;
    protected boolean attackChecked;

    protected boolean swallowed;
    protected boolean pickBomb;
    protected int behaviorState;
    protected boolean startBehaviorState;

    public Enemy(float x, float y, int width, int height, int enemyTpye) {
        super(x, y, width, height);
        this.enemyTpye = enemyTpye;
        initHitBox(x, y, width, height);
        maxHealth = GetMaxHealth(enemyTpye);
        currentHealth = maxHealth;
    }

    protected void updateFrameTick() {
        frameTick++;
        if (frameTick >= frameSpeed) {
            frameTick = 0;
            frameIndex++;
            if (frameIndex >= GetSpriteAmount(enemyTpye, enemyState)) {
                frameIndex = 0;
                switch (enemyTpye) {
                    case CRABBY -> {
                        switch (enemyState) {
                            case CRABBY_ATTACK, CRABBY_HIT -> enemyState = CRABBY_IDLE;
                            case CRABBY_DEAD -> active = false;
                        }
                    }
                    case WHALE -> {
                        switch (enemyState) {
                            case WHALE_ATTACK, WHALE_HIT, WHALE_SWALOW -> enemyState = WHALE_IDLE;
                            case WHALE_DEAD_HIT -> active = false;
                        }
                    }
                    case CAPTAIN -> {
                        switch (enemyState) {
                            case CAPTAIN_ATTACK, CAPTAIN_HIT, CAPTAIN_SCARE_RUN, CAPTAIN_DEAD_GROUND ->
                                    enemyState = CAPTAIN_IDLE;
                            case CAPTAIN_DEAD_HIT -> active = false;
                        }
                    }
                    case BIGGUY -> {
                        switch (enemyState) {
                            case BIGGUY_ATTACK, BIGGUY_HIT, BIGGUY_DEAD_GROUND, BIGGUY_THROW_BOMB ->
                                    enemyState = BIGGUY_IDLE;
                            case BIGGUY_PICK_BOMB -> enemyState = BIGGUY_IDLE_BOMB;
                            case BIGGUY_DEAD_HIT -> active = false;
                        }
                    }
                }

            }
        }
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitBox)) {
            player.changeHealth(-GetEnemyDmg(enemyTpye));
        }
        attackChecked = true;
    }


    protected void firstUpdateChecked(int[][] lvlData) {
        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        if (CanMoveHere(hitBox.x, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
            tileY = (int) (hitBox.y / Game.TILE_SIZE);
        }
    }

    protected void move(int[][] lvlData) {
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

    protected void changWalkSpeed(float value) {
        walkSpeed = value * Game.SCALE;
    }

    //temp
    protected void turnTowardBomb(Bomb bomb) {
        if (bomb.getHitbox().x > hitBox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitBox.x > hitBox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected void newState(int enemyState) {
        this.enemyState = enemyState;
        frameTick = 0;
        frameIndex = 0;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) (player.getHitBox().y / Game.TILE_SIZE);
        if (playerTileY == tileY)
            if (isPlayerInRange(player)) {
                if (isSightClear(lvlData, hitBox, player.hitBox, tileY)) {
                    startBehaviorState = true;
                    return true;
                }
            }
        return false;
    }

    //temp
    protected boolean canSeeBomb(int[][] lvlData, Bomb bomb) {
        int playerTileY = (int) (bomb.getHitbox().y / Game.TILE_SIZE);
        if (playerTileY == tileY)
            if (isBombInRange(bomb))
                return isSightClear(lvlData, hitBox, bomb.getHitbox(), tileY);
        return false;
    }

    //temp
    protected boolean isBombInRange(Bomb bomb) {
        int absValue = (int) Math.abs(bomb.getHitbox().x - hitBox.x);
        return absValue <= attackDistance * 1; // kiem tra xem khoang cach giua player va enemy co <= 5 tile khong
    }

    private boolean isPlayerInRange(Player player) {
        // lay khoang cach giua player va enemy vi nhan vat co the dung ben trai hoac ben phai nen lay ABS(tri tuyet doi)
        return canSeeHitbox.intersects(player.hitBox);
//        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
//        return absValue <= attackDistance * 2; // kiem tra xem khoang cach giua player va enemy co <= 5 tile khong
    }

    protected void updateCanSeeBox() {
        canSeeHitbox.x = hitBox.x - 2 * Game.TILE_SIZE;
        if (walkDir == RIGHT)
            canSeeHitbox.x = hitBox.x - Game.TILE_SIZE;
        canSeeHitbox.y = hitBox.y + hitBox.height - Game.TILE_SIZE;
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

    public void setEnemyState(int enemyState) {
        this.enemyState = enemyState;
    }

    public boolean isActive() {
        return active;
    }

    public int getBehaviorState() {
        return behaviorState;
    }

    public void setBehaviorState(int behaviorState) {
        this.behaviorState = behaviorState;
    }

    public boolean getStartBehaviorState() {
        return startBehaviorState;
    }

    public void setStartBehaviorState(boolean startBehaviorState) {
        this.startBehaviorState = startBehaviorState;
    }

    public void drawHitBox(Graphics g, int lvlOffset) {
        g.setColor(Color.pink);
        g.drawRect((int) hitBox.x - lvlOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    public void resetEnemy() {
        hitBox.x = x;
        hitBox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(CRABBY_IDLE);
        active = true;
        fallSpeed = 0;
    }


}
