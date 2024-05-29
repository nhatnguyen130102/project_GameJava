package entities;

import main.Game;

import java.awt.*;

public abstract class Entity {
    protected float x,y;
    protected  int width, height;
    protected Rectangle hitBox;
    public Entity(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initHitBox();
    }

    private void initHitBox() {
        int hbWidth = width/4;
        int hbHeight = height/2;
        hitBox = new Rectangle((int) x, (int) y,width,height);
    }
    public void updateHitBox(){
        hitBox.x = (int) x;
        hitBox.y = (int) y;
    }
    public Rectangle getHitBox(){
        return hitBox;
    }
    public void drawHitBox(Graphics g){
        g.setColor(Color.pink);
        int var1 = (int) (hitBox.x + hitBox.width * 1.5);
        int var2 = (int)(hitBox.y + hitBox.height * 0.5);
        g.drawRect(hitBox.x ,hitBox.y, hitBox.width, hitBox.height);
    }
}
