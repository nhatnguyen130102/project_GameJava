package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ultilz.Constants.Directions.*;
import static ultilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy {
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, HB_CRABBY_WIDTH, HB_CRABBY_HEIGHT);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,82* Game.SCALE,19*Game.SCALE);
        attackBoxOffsetX = (int) (Game.SCALE * 30);
    }

    public void update(int[][] lvlData, Player player) {
        updateFrameTick();
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
                case IDLE -> newState(RUNNING);
                case RUNNING -> {
                    if (canSeePlayer(lvlData, player))
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK);
                    move(lvlData);
                }
                case ATTACK -> {
                    if(frameIndex == 0)
                        attackChecked = false;
                    if(frameIndex == 3 && !attackChecked)
                        checkEnemyHit(attackBox,player);
                }
                case HIT -> {
                }
            }
        }
    }

    public void drawAttackBox(Graphics g, int xLvlOffset){
        g.setColor(Color.RED);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }
    public int flipX(){
        if(walkDir == RIGHT)
            return width;
        else
            return 0;
    }
    public int flipW(){
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }
}
