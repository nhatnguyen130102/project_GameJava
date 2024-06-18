package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static java.awt.Color.RED;
import static java.awt.Color.pink;
import static ultilz.Constants.Directions.RIGHT;
import static ultilz.Constants.EnemyConstants.*;
import static ultilz.Constants.PlayerConstants.PLAYER_DMG;

public class Whale extends Enemy {
    public Rectangle2D.Float attackBox;

    public Whale(float x, float y) {
        super(x, y, WHALE_WIDTH, WHALE_HEIGHT, WHALE);
        initHitBox(x, y, HB_WHALE_WIDTH, HB_WHALE_HEIGHT);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 40 * Game.SCALE, 20 * Game.SCALE);
    }

    public void update(int[][] lvlData, Player player) {
        updateAttackBox();
        updateFrameTick();
        updateBehavior(lvlData, player);

    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x;
        if (walkDir == RIGHT)
            attackBox.x = hitBox.x + attackBox.width;
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

    public void drawHitBox(Graphics g, int lvlOffset) {
        g.setColor(pink);
        g.drawRect((int) hitBox.x - lvlOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(RED);
        g.fillRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
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

    protected boolean isPlayerCloseForAttackWhale(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - attackBox.x);
        if (Math.abs(hitBox.y - player.hitBox.y) < Game.TILE_SIZE)// neu player khong dung cung 1 con duong voi enemy thi enemy se khong tan cong
            return absValue < player.hitBox.width; // kiem tra xem khoang cach giua player va enemy co <= 5 tile khong
        return false;
    }

    public void hurt() {
        currentHealth -= PLAYER_DMG;
        if (currentHealth <= 0)
            newState(WHALE_DEAD_HIT);
        else
            newState(WHALE_HIT);
    }

}
