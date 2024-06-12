package entities;

import static ultilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy {
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, HB_CRABBY_WIDTH, HB_CRABBY_HEIGHT);
    }

    public void update(int[][] lvlData) {
        uploadFrameTick();
        updateMove(lvlData);
    }

    public void updateMove(int[][] lvlData) {
        if (firstUpdate) {
            firstUpdateChecked(lvlData);
        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case IDLE -> newState(RUNNING);
                case RUNNING -> {
                    move(lvlData);
                }
            }
        }
    }

}
