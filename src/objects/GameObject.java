package objects;

import main.Game;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ultilz.Constants.BombTiles.BOMB;
import static ultilz.Constants.ObjectConstants.*;


public class GameObject {
    protected int x, y, objType;
    protected Rectangle2D.Float hitBox;
    protected boolean doAnimation, active = true;
    protected int frameTick, frameIndex;
    protected int frameSpeed = 25;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, width * Game.SCALE, height * Game.SCALE);
    }
    protected void initHitBoxBomb(int x, int y,int width, int height){
        hitBox = new Rectangle2D.Float(x, y, width * Game.SCALE, height * Game.SCALE);

    }
    protected void drawHitBox(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) (hitBox.x - xLvlOffset), (int) (hitBox.y - yLvlOffset), (int) hitBox.width, (int) hitBox.height);
    }

    protected void updateFrameTick() {
        frameTick++;
        if (frameTick >= frameSpeed) {
            frameTick = 0;
            frameIndex++;
            if (frameIndex >= GetSpriteAmount(objType)) {
                frameIndex = 0;
                if(objType == BARREL || objType == BOX){
                    doAnimation =false;
                    active = false;
                }else if(objType == CANNON_LEFT || objType == CANNON_RIGHT){
                    doAnimation = false;
                }
            }
        }
    }
    public void reset(){
        frameIndex = 0;
        frameTick = 0;
        active = true;

        if(objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT)
            doAnimation = false;
        else
            doAnimation = true;

    }

    public int getObjType() {
        return objType;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public boolean isActive() {
        return active;
    }
    public int getxDrawOffset() {
        return xDrawOffset;
    }
    public int getyDrawOffset() {
        return yDrawOffset;
    }
    public void setDoAnimation(boolean doAnimation){
        this.doAnimation = doAnimation;
    }
    public int getFrameIndex(){
        return frameIndex;
    }
    public int getFrameTick(){
        return frameTick;
    }

    public void setActive(boolean active){
        this.active = active;
    }

}
