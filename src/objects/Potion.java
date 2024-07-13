package objects;

import main.Game;

public class Potion extends GameObject{
    public float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;
    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitBox(7,14);
        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);
        hitBox.y +=  yDrawOffset + Game.SCALE * 20;
        maxHoverOffset = (int) (20 * Game.SCALE);
    }
    public void update(){
        updateFrameTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.075f * Game.SCALE * hoverDir);
        if(hoverOffset >= maxHoverOffset)
            hoverDir = -1;
        else if(hoverOffset < 0)
            hoverDir = 1;
        hitBox.y  = y + hoverOffset;
    }
}
