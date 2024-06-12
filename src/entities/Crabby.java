package entities;

import static ultilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy {
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, HB_CRABBY_WIDTH, HB_CRABBY_HEIGHT);
    }

    public void update(int[][] lvlData, Player player) {
        updateFrameTick();
        updateMove(lvlData, player);
    }

    public void updateMove(int[][] lvlData, Player player) {
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
            }
        }
    }

}
