package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.Color.RED;
import static java.awt.Color.pink;
import static ultilz.Constants.Directions.RIGHT;
import static ultilz.Constants.EnemyConstants.*;

public class Whale extends Enemy {
    public Rectangle2D.Float attackBox;
    public float attackOffsetY;
    private Bomb bomb;
    private Random rnd = new Random();
    private boolean firstTimeSeeBomb = false;
    private int value = -1;
//    private int SCARE = 0;
    private int SWALLOW = 0;
    private int SKIP = 1;


    public Whale(float x, float y) {
        super(x, y, WHALE_WIDTH, WHALE_HEIGHT, WHALE);
        initHitBox(x, y, (int) (HB_WHALE_WIDTH), HB_WHALE_HEIGHT);
        initCanSeeRange(x, y, Game.TILE_SIZE *4 , Game.TILE_SIZE);

        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, HB_WHALE_WIDTH - 20 * Game.SCALE, HB_WHALE_HEIGHT - 20 * Game.SCALE);
        attackOffsetY = 20 * Game.SCALE;
    }

    public void update(int[][] lvlData, Player player, ArrayList<Bomb> bombs) {
        updateAttackBox();
        updateFrameTick();
        updateCanSeeBox();
        updateBehavior(lvlData, player, bombs);
    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x;
        if (walkDir == RIGHT)
            attackBox.x = hitBox.x + (hitBox.width - attackBox.width) + 10 * Game.SCALE;
        attackBox.y = hitBox.y;
    }

    public void updateBehavior(int[][] lvlData, Player player, ArrayList<Bomb> bombs) {
        if (firstUpdate) {
            firstUpdateChecked(lvlData);
        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case WHALE_IDLE -> newState(WHALE_RUNNING);
                case WHALE_RUNNING -> {
                    if (canSeePlayer(lvlData, player)) {
                        changWalkSpeed(0.7f * Game.SCALE);
                        setStartBehaviorState(true);
                        turnTowardsPlayer(player);
                    } else {
                        changWalkSpeed(0.5f * Game.SCALE);
                        setStartBehaviorState(false);
                    }
                    if (isPlayerCloseForWhaleAttack(player)) {
                        setStartBehaviorState(false);
                        newState(WHALE_ATTACK);

                    }
                    if (bombs != null) {
                        for (Bomb bomb : bombs) {
                            if (bomb.isActive()) {
                                if (canSeeBomb(lvlData, bomb)) {
                                    handleFirstTimeSeeBomb();
                                    setStartBehaviorState(true);
                                } else {
                                    setStartBehaviorState(false);
                                }
                                handleRndBehavior(bomb, lvlData);
                            }
                        }
                    }
                    move(lvlData);
                }
                case WHALE_ATTACK -> {

                    if (frameIndex == 0)
                        attackChecked = false;
                    if (frameIndex == 6 && !attackChecked) {
                        checkEnemyHit(attackBox, player);
                    }
                }
                case WHALE_SWALOW -> {
                    checkBombDir(this.bomb);
                    if (frameIndex == 0)
                        swallowed = false;
                    if (frameIndex == 4 && !swallowed) {
                        for (Bomb b : bombs) {
                            checkEnemyHitBomb(attackBox, b);
                        }
                    }
                }
                case WHALE_HIT -> {
                }
            }
        }
    }
    protected void checkEnemyHitBomb(Rectangle2D.Float attackBox, Bomb bomb) {
        if (attackBox.intersects(bomb.getHitbox()))
            bomb.setActive(false);
        swallowed = true;
    }
    private void handleRndBehavior(Bomb bomb, int[][] lvlData) {
        if (value == SWALLOW) {
            turnTowardBomb(bomb);
            if (isBombCloseForWhaleAttack(bomb)) {
                newState(WHALE_SWALOW);
                this.bomb = bomb;
                value = -1;
//                System.out.println(value);
            }
        }
        if (value == SKIP) {
            if (!canSeeBomb(lvlData, bomb)) {
                value = -1;
            }
        }

    }

    private void handleFirstTimeSeeBomb() {
        if (value == -1) {
            value = rnd.nextInt(2);
        }
    }

    public void checkBombDir(Bomb bomb) {
        if (hitBox.intersects(bomb.getHitbox())) {
            float distance = bomb.getHitbox().x - hitBox.x;
            if (distance > 0) {
                this.bomb.getHitbox().x -= 0.2f;
            } else {
                this.bomb.getHitbox().x += 0.2f;
            }
        }

    }

    public void drawHitBox(Graphics g, int lvlOffset, int yLvlOffset) {
        g.setColor(pink);
        g.drawRect((int) hitBox.x - WHALE_DRAW_OFFSET_X - lvlOffset + flipX(), (int) hitBox.y - WHALE_DRAW_OFFSET_Y - yLvlOffset, (int) hitBox.width * flipW(), (int) hitBox.height);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(RED);
        g.fillRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY - yLvlOffset), (int) attackBox.width, (int) attackBox.height);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY - yLvlOffset), (int) attackBox.width, (int) attackBox.height);
    }

    public int flipX() {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }

    protected boolean isPlayerCloseForWhaleAttack(Player player) {
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

    //temp
    protected boolean isBombCloseForWhaleAttack(Bomb bomb) {
        int distance = (int) (bomb.getHitbox().x - hitBox.x);
        if (distance <= 0) {//bomb left
            if (Math.abs(hitBox.y - bomb.getHitbox().y) < Game.TILE_SIZE) {
                float distanceBetween = attackBox.x - (bomb.getHitbox().x + bomb.getHitbox().width);
                return distanceBetween < -10;
            }
        } else if (distance >= 0) {//bomb right
            if (Math.abs(hitBox.y - bomb.getHitbox().y) < Game.TILE_SIZE) {
                float distanceBetween = bomb.getHitbox().x - (attackBox.x + attackBox.width);
                return distanceBetween < -10;
            }
        }
        return false;
    }

    public void hurt(int value) {
        currentHealth -= value;
        if (currentHealth <= 0)
            newState(WHALE_DEAD_HIT);
        else
            newState(WHALE_HIT);
    }
}
