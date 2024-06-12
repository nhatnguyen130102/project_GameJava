package entities;

import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x,y;
    protected  int width, height;
    protected Rectangle2D.Float hitBox;
    public Entity(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void initHitBox(float x, float y, int width, int height) {
        hitBox = new Rectangle2D.Float((int) x, (int) y,width,height);
    }
    public Rectangle2D.Float getHitBox(){
        return hitBox;
    }
//    public void drawHitBox(Graphics g, int lvlOffset){
//        g.setColor(Color.pink);
//        g.drawRect((int) hitBox.x - lvlOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
//    }
}
