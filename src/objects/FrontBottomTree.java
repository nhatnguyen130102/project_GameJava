package objects;

import main.Game;

public class FrontBottomTree extends GameObject {
    public FrontBottomTree(int x, int y, int objType) {
        super(x, y, objType);

        initHitBox(32, 32);
        xDrawOffset = 0;
        yDrawOffset = (int) (Game.SCALE * 32);
        hitBox.y += yDrawOffset;
    }
}
