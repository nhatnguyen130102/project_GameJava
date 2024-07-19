package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.Color.*;
import static ultilz.Constants.Directions.*;
import static ultilz.Constants.EnemyConstants.*;

public class BigGuy extends Enemy {

    public Rectangle2D.Float attackBox;
    public Rectangle2D.Float canSeeBombBox;

    public Rectangle2D.Float pickBombBox;
    public float attackOffsetY;
    private Random rnd = new Random();
    private int value = -1;
    private Bomb bombHandler;
    private boolean throwBomb = false;

    public BigGuy(float x, float y) {
        super(x, y, BIGGUY_WIDTH, BIGGUY_HEIGHT, BIGGUY);
        initHitBox(x, y, HB_BIGGUY_WIDTH, HB_BIGGUY_HEIGHT);
        initCanSeeRange(x, y, Game.TILE_SIZE * 4, Game.TILE_SIZE);
        initPickBombBox();
        initCanSeeBombBox();
        initAttackBox();
    }

    private void initCanSeeBombBox() {
        canSeeBombBox = new Rectangle2D.Float(x, y, Game.TILE_SIZE, (float) Game.TILE_SIZE / 2);
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 35, 40);
//        attackOffsetY = 20 * Game.SCALE;
    }

    private void initPickBombBox() {
        pickBombBox = new Rectangle2D.Float(x, y, 20 * Game.SCALE, 20 * Game.SCALE);
    }

    public void update(int[][] lvlData, Player player, ArrayList<Bomb> bombs) {
        updateAttackBox();
        updateFrameTick();
        updateCanSeeBox();
        updatePickBombBox();
        updateCanSeeBombBox();
        updateBehavior(lvlData, player, bombs);
    }

    private void updateCanSeeBombBox() {
        canSeeBombBox.x = hitBox.x + (float) Game.TILE_SIZE / 2;
        if (walkDir == LEFT)
            canSeeBombBox.x = hitBox.x - (float) Game.TILE_SIZE / 2;
        canSeeBombBox.y = hitBox.y + (float) Game.TILE_SIZE / 2 - 10;
    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x + 30;
        if (walkDir == LEFT)
            attackBox.x = hitBox.x;
        attackBox.y = hitBox.y + 10;
    }

    private void updatePickBombBox() {
        pickBombBox.x = hitBox.x + 10;
        if (walkDir == LEFT)
            pickBombBox.x = hitBox.x + 40;
        pickBombBox.y = hitBox.y + 10;
    }

    public void updateBehavior(int[][] lvlData, Player player, ArrayList<Bomb> bombs) {
        if (firstUpdate) {
            firstUpdateChecked(lvlData);
        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case BIGGUY_IDLE -> newState(BIGGUY_RUNNING);
                case BIGGUY_IDLE_BOMB -> {
                    newState(BIGGUY_RUN_BOMB);
                }
                case BIGGUY_RUN_BOMB -> {
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        newState(BIGGUY_THROW_BOMB);
                    }
                    if (bombHandler.isActive() && !bombHandler.isExplode()) {
                        if (bombHandler.getState() == bombHandler.PICKED) {
                            bombHandler.getHitbox().x = pickBombBox.x;
                            bombHandler.getHitbox().y = pickBombBox.y;
                        }
                    }

                    move(lvlData);

                }
                case BIGGUY_RUNNING -> {
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                    }
                    if (isPlayerCloseForBIGGUYAttack(player)) {
                        setStartBehaviorState(false);
                        newState(BIGGUY_ATTACK);
                    }
                    if (bombs != null) {
                        for (Bomb bomb : bombs) {
                            if (bomb.isActive()) {
                                if (canSeeBomb(lvlData, bomb)) {
                                    if (bomb.bigGuySeeBomb(this) && !bomb.isCanSeeByEnemy()) {
                                        bombHandler = bomb;
                                        bombHandler.setCanSeeByEnemy(true);
                                        handleFirstTimeSeeBomb();
                                    }
                                }
                                if (value != -1) {
                                    if (bomb.isActive() && !bomb.isExplode() && bomb.isCanSeeByEnemy()) {
                                        handleRndBehavior(lvlData);
                                    }
                                }
                            }
                        }
                    }
                    move(lvlData);

                }
                case BIGGUY_ATTACK -> {
                    if (frameIndex == 0)
                        attackChecked = false;
                    if (frameIndex == 4 && !attackChecked) {
                        checkEnemyHit(attackBox, player);
                    }
                }
                case BIGGUY_THROW_BOMB -> {
                    if (frameIndex == 0)
                        throwBomb = false;
                    if (frameIndex == 3 && !throwBomb) {
                        throwBomb();
                    }
                }
                case BIGGUY_PICK_BOMB -> {
                    if (frameIndex == 0)
                        pickBomb = false;
                    if (frameIndex == 6 && !pickBomb) {
                        bombHandler.setState(bombHandler.PICKED);
                        checkEnemyHitBomb(attackBox, bombHandler);
                    }
                }
                case BIGGUY_HIT -> {
                }
            }
        }
    }

    private void throwBomb() {
        checkEnemyBombDir(bombHandler);
        bombHandler.newBombStateThrow();
        bombHandler.setState(-1);
        bombHandler.setCanSeeByEnemy(false);
        throwBomb = true;
        value = -1;
    }

    protected void checkEnemyBombDir(Bomb bomb) {
        if (walkDir == RIGHT) {
            bomb.setDir(1);
        }
        if (walkDir == LEFT) {
            bomb.setDir(-1);
        }

    }

    protected void checkEnemyHitBomb(Rectangle2D.Float attackBox, Bomb bomb) {
        if (attackBox.intersects(bomb.getHitbox())) {
            checkBombDir(bomb);
        }
        pickBomb = true;

    }

    private void handleRndBehavior(int[][] lvlData) {

        if (enemyState != BIGGUY_PICK_BOMB) {
            if (value % 2 == 0) {
                turnTowardBomb(bombHandler);
                if (!bombHandler.inAir)
                    if (isBombCloseForBigGuyPick(bombHandler)) {
                        newState(BIGGUY_PICK_BOMB);
                        bombHandler.setState(bombHandler.PICKED);
                        value = -1;
                    }
            }
        }
        else {
            if (!canSeeBomb(lvlData, bombHandler)) {
                value = -1;
                bombHandler.setCanSeeByEnemy(false);
            }
        }
    }

    protected boolean isBombCloseForBigGuyPick(Bomb bomb) {
        int distance = (int) (bomb.getHitbox().x - hitBox.x);
        if (distance <= 0) {//bomb left
            if (Math.abs(hitBox.y - bomb.getHitbox().y) < Game.TILE_SIZE) {
                float distanceBetween = pickBombBox.x - (bomb.getHitbox().x + bomb.getHitbox().width);
                return distanceBetween < -10;
            }
        } else if (distance >= 0) {//bomb right
            if (Math.abs(hitBox.y - bomb.getHitbox().y) < Game.TILE_SIZE) {
                float distanceBetween = bomb.getHitbox().x - (pickBombBox.x + pickBombBox.width);
                return distanceBetween < -10;
            }
        }
        return false;
    }

    // lay hanh dong rnd
    private void handleFirstTimeSeeBomb() {
        if (value == -1) {
            value = rnd.nextInt(100);
        }
    }

    public void checkBombDir(Bomb bomb) {
        bomb.getHitbox().x = pickBombBox.x;
        bomb.getHitbox().y = pickBombBox.y;
    }

    public void drawHitBox(Graphics g, int lvlOffset, int yLvlOffset) {
        g.setColor(pink);
        g.drawRect((int) hitBox.x - BIGGUY_DRAW_OFFSET_X - lvlOffset + flipX(), (int) hitBox.y - BIGGUY_DRAW_OFFSET_Y - yLvlOffset, (int) hitBox.width * flipW(), (int) hitBox.height);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(RED);
//        g.fillRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY - yLvlOffset), (int) attackBox.width, (int) attackBox.height);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY - yLvlOffset), (int) attackBox.width, (int) attackBox.height);
    }

    public void drawPickBombBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(RED);
//        g.fillRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY - yLvlOffset), (int) attackBox.width, (int) attackBox.height);
        g.drawRect((int) (pickBombBox.x - xLvlOffset), (int) (pickBombBox.y + attackOffsetY - yLvlOffset), (int) pickBombBox.width, (int) pickBombBox.height);
    }

    public void drawCanSeeBombBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(RED);
//        g.fillRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY - yLvlOffset), (int) attackBox.width, (int) attackBox.height);
        g.drawRect((int) (canSeeBombBox.x - xLvlOffset), (int) (canSeeBombBox.y + attackOffsetY - yLvlOffset), (int) canSeeBombBox.width, (int) canSeeBombBox.height);
    }

    public int flipX() {
        if (walkDir == LEFT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkDir == LEFT)
            return -1;
        else
            return 1;
    }

    protected boolean isPlayerCloseForBIGGUYAttack(Player player) {
        int distance = (int) (player.hitBox.x - hitBox.x);
        if (distance <= 0) {// player left and enemy right
            if (Math.abs(hitBox.y - player.hitBox.y) < Game.TILE_SIZE) {
                int distanceBetween = (int) (attackBox.x - (player.hitBox.x + player.hitBox.width));
                return distanceBetween < 0;
            }
        } else if (distance >= 0) {// player right and enemy left
            if (Math.abs(hitBox.y - player.hitBox.y) < Game.TILE_SIZE) {
                int distanceBetween = (int) (player.hitBox.x - (attackBox.x + attackBox.width));
                return distanceBetween < 0;
            }
        }
        return false;
    }

    public void hurt(int value) {
        currentHealth -= value;
        if (currentHealth <= 0)
            newState(BIGGUY_DEAD_HIT);
        else
            newState(BIGGUY_DEAD_GROUND);
    }
}
