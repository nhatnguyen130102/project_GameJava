package objects;

import main.Game;

public class House extends GameObject {
    public House(int x, int y, int objType) {
        super(x, y, objType);

        initHitBox(64, 64);
        xDrawOffset = 0;
        yDrawOffset = (int) (Game.SCALE * 64);
        hitBox.y += yDrawOffset;
    }
}
