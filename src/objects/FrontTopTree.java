package objects;

import main.Game;

public class FrontTopTree extends GameObject {
    public FrontTopTree(int x, int y, int objType) {
        super(x, y, objType);

        initHitBox(39, 32);
        xDrawOffset = 0;
        yDrawOffset = (int) (Game.SCALE * 32);
        hitBox.y += yDrawOffset;

        doAnimation = true;
    }

    public void update(){
        updateFrameTick();
    }
}
