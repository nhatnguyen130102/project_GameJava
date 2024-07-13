package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ultilz.Constants.Directions.*;
import static ultilz.Constants.EnemyConstants.*;
import static ultilz.Constants.PlayerConstants.PLAYER_DMG;

public class Crabby extends Enemy {
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, HB_CRABBY_WIDTH, HB_CRABBY_HEIGHT);
        initCanSeeRange(x, y, Game.TILE_SIZE *4 , Game.TILE_SIZE);

        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 82 * 2 * Game.SCALE, 19 * 2 * Game.SCALE);
        attackBoxOffsetX = (int) (Game.SCALE * 30 * 2);
    }

    public void update(int[][] lvlData, Player player) {
        updateFrameTick();
        updateCanSeeBox();
        updateBehavior(lvlData, player);
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x - attackBoxOffsetX;
        attackBox.y = hitBox.y;
    }

    public void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateChecked(lvlData);
        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case CRABBY_IDLE -> newState(CRABBY_RUNNING);
                case CRABBY_RUNNING -> {
                    if (canSeePlayer(lvlData, player)){
                        turnTowardsPlayer(player);
                        setStartBehaviorState(true);
                    }
                    else{
                        setStartBehaviorState(false);
                    }
                    if (isPlayerCloseForAttack(player))
                        newState(CRABBY_ATTACK);
                    move(lvlData);
                }
                case CRABBY_ATTACK -> {
                    if (frameIndex == 0)
                        attackChecked = false;
                    if (frameIndex == 3 && !attackChecked)

                        checkEnemyHit(attackBox, player);
                }
                case CRABBY_HIT -> {
                }
            }
        }
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int distance = (int) (player.hitBox.x - hitBox.x);

        if (distance <= 0) {// player left and enemy right
            if (Math.abs(hitBox.y - player.hitBox.y) < Game.TILE_SIZE){
                int distanceBetween = (int) (attackBox.x - (player.hitBox.x + player.hitBox.width));
                return distanceBetween  < 0;
            }
        }
        else {// player right and enemy left
            if (Math.abs(hitBox.y - player.hitBox.y) < Game.TILE_SIZE){
                int distanceBetween = (int) (player.hitBox.x  - (attackBox.x + attackBox.width));
                return distanceBetween < 0;
            }
        }
        return false;
    }

    public void hurt(int value) {
        currentHealth -= value;
        if (currentHealth <= 0){
            newState(CRABBY_DEAD);
        }
        else
            newState(CRABBY_HIT);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
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
}
