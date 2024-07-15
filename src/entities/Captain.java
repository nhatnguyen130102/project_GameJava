package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.Color.*;
import static ultilz.Constants.Directions.*;
import static ultilz.Constants.EnemyConstants.*;

public class Captain extends Enemy {

    public Rectangle2D.Float attackBox;
    public float attackOffsetY;
    private Random rnd = new Random();
    private int value = -1;
    private int SCARE = 0;
    private int SKIP = 1;



    public Captain(float x, float y) {
        super(x, y, CAPTAIN_WIDTH, CAPTAIN_HEIGHT, CAPTAIN);
        initHitBox(x, y, HB_CAPTAIN_WIDTH, HB_CAPTAIN_HEIGHT);
        initCanSeeRange(x, y, Game.TILE_SIZE *4 , Game.TILE_SIZE);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, HB_CAPTAIN_WIDTH + 30 * Game.SCALE, HB_CAPTAIN_HEIGHT);
//        attackOffsetY = 20 * Game.SCALE;
    }

    public void update(int[][] lvlData, Player player, ArrayList<Bomb> bombs) {
        updateAttackBox();
        updateFrameTick();
        updateCanSeeBox();
        updateBehavior(lvlData, player, bombs);
    }




    private void updateAttackBox() {
        attackBox.x = hitBox.x;
        if (walkDir == LEFT)
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
                case CAPTAIN_IDLE -> newState(CAPTAIN_RUNNING);
                case CAPTAIN_SCARE_RUN ->{
                    move(lvlData);
                }
                case CAPTAIN_RUNNING -> {
                    if (canSeePlayer(lvlData, player)) {
                        changWalkSpeed(0.7f * Game.SCALE);
                        setStartBehaviorState(true);
                        turnTowardsPlayer(player);
                    } else {
                        changWalkSpeed(0.5f * Game.SCALE);
                        setStartBehaviorState(false);
                    }
                    if (isPlayerCloseForCAPTAINAttack(player)) {
                        setStartBehaviorState(false);
                        newState(CAPTAIN_ATTACK);

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
                                if (value != -1)
                                    handleRndBehavior(bomb, lvlData);
                            }
                        }
                    }
                    move(lvlData);
                }
                case CAPTAIN_ATTACK -> {
                    if (frameIndex == 0)
                        attackChecked = false;
                    if (frameIndex == 5 && !attackChecked) {
                        checkEnemyHit(attackBox, player);
                    }
                }
                case CAPTAIN_HIT -> {
                }
            }
        }
    }

    private void handleRndBehavior(Bomb bomb, int[][] lvlData) {
        if (value == SKIP) {
            if (!canSeeBomb(lvlData, bomb)) {
                value = -1;
            }
        }
        if (value == SCARE) {
            changeWalkDir();
            newState(CAPTAIN_SCARE_RUN);
            value = -1;
        }
    }

    private void handleFirstTimeSeeBomb() {
        if (value == -1) {
            value = rnd.nextInt(2);
        }
    }


    public void drawHitBox(Graphics g, int lvlOffset, int yLvlOffset) {
        g.setColor(pink);
        g.drawRect((int) hitBox.x - CAPTAIN_DRAW_OFFSET_X - lvlOffset + flipX(), (int) hitBox.y - CAPTAIN_DRAW_OFFSET_Y - yLvlOffset, (int) hitBox.width * flipW(), (int) hitBox.height);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(RED);
        g.fillRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY - yLvlOffset), (int) attackBox.width, (int) attackBox.height);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY - yLvlOffset), (int) attackBox.width, (int) attackBox.height);
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

    protected boolean isPlayerCloseForCAPTAINAttack(Player player) {
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
            newState(CAPTAIN_DEAD_HIT);
        else
            newState(CAPTAIN_DEAD_GROUND);
    }
}
