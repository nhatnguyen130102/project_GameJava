package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static java.awt.Color.RED;
import static java.awt.Color.pink;
import static ultilz.Constants.Directions.LEFT;
import static ultilz.Constants.Directions.RIGHT;
import static ultilz.Constants.EnemyConstants.*;
import static ultilz.Constants.PlayerConstants.PLAYER_DMG;

public class Whale extends Enemy {
    public Rectangle2D.Float attackBox;
    public float attackOffsetY;

    public Whale(float x, float y) {
        super(x, y, WHALE_WIDTH, WHALE_HEIGHT, WHALE);
        initHitBox(x, y, (int) (HB_WHALE_WIDTH ), HB_WHALE_HEIGHT );
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (float) (30 * 2 * Game.SCALE), (float) (20 * 1.5 * Game.SCALE));
        attackOffsetY = 20 * Game.SCALE;
    }

    public void update(int[][] lvlData, Player player) {
        updateAttackBox();
        updateFrameTick();
        updateBehavior(lvlData, player);
    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x;
        if (walkDir == RIGHT)
            attackBox.x = hitBox.x + (int) ((hitBox.width) - attackBox.width - 15);
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
                case WHALE_IDLE -> newState(WHALE_RUNNING);
                case WHALE_RUNNING -> {
                    if (canSeePlayer(lvlData, player))
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttackWhale(player))
                        newState(WHALE_ATTACK);
                    move(lvlData);
                }
                case WHALE_ATTACK -> {
                    if (frameIndex == 0)
                        attackChecked = false;
                    if (frameIndex == 6 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                }
                case WHALE_HIT -> {
                }
            }
        }
    }

    public void drawHitBox(Graphics g, int lvlOffset, int yLvlOffset) {
        g.setColor(pink);
        g.drawRect((int) hitBox.x - WHALE_DRAW_OFFSET_X - lvlOffset + flipX(), (int) hitBox.y - WHALE_DRAW_OFFSET_Y - yLvlOffset, (int) hitBox.width * flipW(), (int) hitBox.height);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(RED);
        g.fillRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY), (int) attackBox.width, (int) attackBox.height);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y + attackOffsetY), (int) attackBox.width, (int) attackBox.height);
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

    protected boolean isPlayerCloseForAttackWhale(Player player) {
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
        if (currentHealth <= 0){
            newState(WHALE_DEAD_HIT);
        }
        else
            newState(WHALE_HIT);
    }

}
