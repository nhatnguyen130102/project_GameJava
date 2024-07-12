package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitBox;
    protected Rectangle2D.Float canSeeHitbox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void initHitBox(float x, float y, int width, int height) {
        hitBox = new Rectangle2D.Float((int) x, (int) y, width, height);
    }
    public void initCanSeeRange(float x, float y, int width, int height){
        canSeeHitbox = new Rectangle2D.Float(x,y,width,height);
    }
    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }
    public Rectangle2D.Float getCanSeeHitbox(){
        return canSeeHitbox;
    }

    public void drawHitBox(Graphics g, int lvlOffset, int ylvlOffset) {
        g.setColor(Color.pink);
        g.drawRect((int) hitBox.x - lvlOffset, (int) hitBox.y - ylvlOffset, (int) hitBox.width, (int) hitBox.height);
    }
    public void drawCanSeeHitbox(Graphics g, int xLvlOffset,int yLvlOffset){
        g.setColor(Color.pink);
        g.drawRect((int) canSeeHitbox.x - xLvlOffset, (int) canSeeHitbox.y - yLvlOffset, (int) canSeeHitbox.width, (int) canSeeHitbox.height);
    }
}
