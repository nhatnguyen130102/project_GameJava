package objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ultilz.Constants.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;
    private float airSpeed = -3f; // van toc roi cua vat the v0 -> vN
    private final float gravity = 0.04f * Game.SCALE; // luc hap dan______gia toc trong truong

    public Projectile(int x, int y, int dir) {
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (15 * Game.SCALE);

        if (dir == 1)
            xOffset = (int) (60 * Game.SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.dir = dir;
    }

    public void updatePos() {
        hitbox.x += dir * SPEED;
//        hitbox.y += airSpeed; // thuc hien viec giam gia tri Y
//        airSpeed += gravity;
    }


    public void setPos(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
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
}
