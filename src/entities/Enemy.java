package entities;

import static ultilz.Constants.EnemyConstants.*;

public abstract class Enemy extends Entity {
    private int frameIndex, enemyState, enemyTpye;
    private int frameTick, frameSpeed = 25;

    public Enemy(float x, float y, int width, int height, int enemyTpye) {
        super(x, y, width, height);
        this.enemyTpye = enemyTpye;
        initHitBox(x, y, width, height);
    }

    private void uploadFrameTick() {
        frameTick++;
        if (frameTick >= frameSpeed) {
            frameTick = 0;
            frameIndex++;
            if(frameIndex >= GetSpriteAmount(enemyTpye,enemyState) ){
                frameIndex = 0;
            }
        }
    }
    public void update(){
        uploadFrameTick();
    }
    public int getFrameIndex(){
        return frameIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }


}
