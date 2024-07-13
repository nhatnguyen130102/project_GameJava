package objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ultilz.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;
    private boolean explode;
    private Rectangle2D.Float explodeHitbox;

    public Projectile(int x, int y, int dir) {
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (15 * Game.SCALE);

        if (dir == 1)
            xOffset = (int) (60 * Game.SCALE);
        explodeHitbox = new Rectangle2D.Float();
        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.dir = dir;
    }

    public void updatePos() {
        hitbox.x += dir * SPEED;
    }
    public void setInfoExplodeBox(Projectile p){
        getExplodeHitbox().x = p.getHitbox().x;
        getExplodeHitbox().y = p.getHitbox().y;
        getExplodeHitbox().width = BALL_EXPLODE_WIDTH;
        getExplodeHitbox().height = BALL_EXPLODE_HEIGHT;
    }
    public Rectangle2D.Float getExplodeHitbox(){
        return explodeHitbox;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isExplode() {
        return explode;
    }

    public void setExplode(boolean explode) {
        this.explode = explode;
    }
}
