package objects;

import main.Game;

import static ultilz.Constants.ObjectConstants.*;

public class Cannon extends GameObject {
    private int tileY;
    private boolean isShooting;

    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILE_SIZE;
        initHitBox(40, 26);
        hitBox.x -= (int) (4 * Game.SCALE);
        hitBox.y += (int) (6 * Game.SCALE);
    }

    public void update() {
        if (doAnimation) {
            updateFrameTick();
        }
        if(isShooting)
            updateFrameTickAttack(this);
    }

    public int getTileY() {
        return tileY;
    }

    public void setShooting(boolean shooting){
        this.isShooting = shooting;
    }
    public boolean isShooting(){
        return this.isShooting;
    }
}
