package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static ultilz.Constants.BombTiles.BOMB_DRAW_OFFSET_X;
import static ultilz.Constants.BombTiles.BOMB_DRAW_OFFSET_Y;
import static ultilz.Constants.Directions.*;
import static ultilz.Constants.EnemyConstants.*;
import static ultilz.Constants.PlayerConstants.PLAYER_DMG;

public class Bald extends Enemy {
    public Rectangle2D.Float canSeeBombBox;
    private Rectangle2D.Float attackBox;
    private Random rnd = new Random();
    private int value = -1;
    private int KICK = 1;
    private int SKIP = 0;

    private Bomb bombHandler;

    private boolean kicking;

    private Timer explodeTimer;

    public Bald(float x, float y) {
        super(x, y, BALD_WIDTH, BALD_HEIGHT, BALD);
        initHitBox(x, y, HB_BALD_WIDTH, HB_BALD_HEIGHT);
        initCanSeeRange(x, y, Game.TILE_SIZE * 4, Game.TILE_SIZE);
        initCanSeeBombBox();
        initAttackBox();
    }

    private void initCanSeeBombBox() {
        canSeeBombBox = new Rectangle2D.Float(x, y, Game.TILE_SIZE, (float) Game.TILE_SIZE / 2);
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, 25 * Game.SCALE, 30 * Game.SCALE);
    }

    public void update(int[][] lvlData, Player player, ArrayList<Bomb> bombs) {
        updateFrameTick();
        updateCanSeeBox();
        updateBehavior(lvlData, player, bombs);
        updateAttackBox();
        updateCanSeeBombBox();
    }

    private void updateCanSeeBombBox() {
        canSeeBombBox.x = hitBox.x + (float) Game.TILE_SIZE / 2;
        if (walkDir == LEFT)
            canSeeBombBox.x = hitBox.x - (float) Game.TILE_SIZE / 2;
        canSeeBombBox.y = hitBox.y + (float) Game.TILE_SIZE / 2 - 10;
    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x + 10 * Game.SCALE;
        if (walkDir == LEFT)
            attackBox.x = hitBox.x + (hitBox.width - attackBox.width) - 10 * Game.SCALE;
        attackBox.y = hitBox.y + 30 * Game.SCALE;
    }

    public void updateBehavior(int[][] lvlData, Player player, ArrayList<Bomb> bombs) {
        if (firstUpdate) {
            firstUpdateChecked(lvlData);
        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case BALD_IDLE -> newState(BALD_RUNNING);
                case BALD_RUNNING -> {
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                    }
                    if (isPlayerCloseForAttack(player))
                        newState(BALD_ATTACK);
                    if (bombs != null) {
                        for (Bomb bomb : bombs) {
                            if (bomb.isActive()) {
                                if (canSeeBomb(lvlData, bomb)) {
                                    if (bomb.baldSeeBomb(this) && !bomb.isCanSeeByEnemy()) {
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
                case BALD_ATTACK -> {
                    if (frameIndex == 0)
                        attackChecked = false;
                    if (frameIndex == 5 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    if (frameIndex == 5 && kicking) {
                        checkBombEnemyDir(bombHandler);
                        bombHandler.newBombState();
                        bombHandler.setState(-1);
                        bombHandler.setCanSeeByEnemy(false);
                        kicking = false;
                        value = -1;
                    }
                }
                case BALD_HIT -> {
                }
            }
        }
    }

    protected void checkBombEnemyDir(Bomb bomb) {
        if (walkDir == RIGHT) {
            bomb.setDir(1);
        }
        if (walkDir == LEFT) {
            bomb.setDir(-1);
        }
    }

    private void handleRndBehavior(int[][] lvlData) {
        // can duoc xu ly rieng
        if (value % 2 == 0) {
            turnTowardBomb(bombHandler);
            if (!bombHandler.inAir)
                if (isBombCloseForBaldPirateKick(bombHandler)) {
                    newState(BALD_ATTACK);
                    kicking = true;
                    bombHandler.setState(bombHandler.KICKED);
                    value = -1;
                }
        } else {
            if (!canSeeBomb(lvlData, bombHandler)) {
                value = -1;
                bombHandler.setCanSeeByEnemy(false);
            }
        }
    }

    private boolean isBombCloseForBaldPirateKick(Bomb bomb) {
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


    private void handleFirstTimeSeeBomb() {
        if (value == -1) {
            value = rnd.nextInt(100);
        }
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int distance = (int) (player.hitBox.x - hitBox.x);

        if (distance <= 0) {// player left and enemy right
            if (Math.abs(hitBox.y - player.hitBox.y) < Game.TILE_SIZE) {
                int distanceBetween = (int) (attackBox.x - (player.hitBox.x + player.hitBox.width));
                return distanceBetween < 0;
            }
        } else {// player right and enemy left
            if (Math.abs(hitBox.y - player.hitBox.y) < Game.TILE_SIZE) {
                int distanceBetween = (int) (player.hitBox.x - (attackBox.x + attackBox.width));
                return distanceBetween < 0;
            }
        }
        return false;
    }

    public void hurt(int value) {
        currentHealth -= value;
        if (currentHealth <= 0) {
            newState(BALD_DEAD_HIT);
        } else
            newState(BALD_HIT);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) (attackBox.y - yLvlOffset), (int) attackBox.width, (int) attackBox.height);
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
}
