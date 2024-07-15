package objects;

import entities.Player;
import main.Game;

import java.awt.geom.Rectangle2D;

public class FrontTopTree extends GameObject {
    public FrontTopTree(int x, int y, int objType) {
        super(x, y, objType);

        initHitBox(Game.TILE_SIZE, 20);
        xDrawOffset = 0;
        yDrawOffset = (int) (Game.SCALE * 32);
        hitBox.y += yDrawOffset;
        doAnimation = true;
    }
    public void update(){
        updateFrameTick();
    }
}
