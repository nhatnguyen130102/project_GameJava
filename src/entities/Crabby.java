package entities;

import static ultilz.Constants.EnemyConstants.*;

public class Crabby extends Enemy{
    public Crabby(float x, float y) {
        super(x, y,CRABBY_WIDTH,CRABBY_HEIGHT,CRABBY);
        initHitBox(x,y,HB_CRABBY_WIDTH,HB_CRABBY_HEIGHT);
    }

}