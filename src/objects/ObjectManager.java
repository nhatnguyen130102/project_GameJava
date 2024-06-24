package objects;

import entities.Player;
import gameStates.Playing;
import levels.Level;
import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.PrimitiveIterator;

import static ultilz.Constants.BombTiles.*;
import static ultilz.Constants.ObjectConstants.*;
import static ultilz.Constants.Projectiles.*;
import static ultilz.HelpMethods.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs, bombImgs;
    private BufferedImage[] cannonImgs;
    private BufferedImage cannonBallImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();


    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void loadObject(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainer());
        cannons = newLevel.getCannons();
        projectiles.clear();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        int rowPotion = potionSprite.getHeight() / POTION_HEIGHT_DEFAULT;
        int colPotion = potionSprite.getWidth() / POTION_WIDTH_DEFAULT;
        potionImgs = new BufferedImage[rowPotion][colPotion];
        for (int j = 0; j < potionImgs.length; j++) {
            for (int i = 0; i < potionImgs[j].length; i++) {
                potionImgs[j][i] = potionSprite.getSubimage(i * POTION_WIDTH_DEFAULT, j * POTION_HEIGHT_DEFAULT, POTION_WIDTH_DEFAULT, POTION_HEIGHT_DEFAULT);
            }
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        int rowOBJ = containerSprite.getHeight() / CONTAINER_HEIGHT_DEFAULT;
        int colOBJ = containerSprite.getWidth() / CONTAINER_WIDTH_DEFAULT;

        containerImgs = new BufferedImage[rowOBJ][colOBJ];
        for (int j = 0; j < containerImgs.length; j++) {
            for (int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = containerSprite.getSubimage(i * CONTAINER_WIDTH_DEFAULT, j * CONTAINER_HEIGHT_DEFAULT, CONTAINER_WIDTH_DEFAULT, CONTAINER_HEIGHT_DEFAULT);
            }
        }

        BufferedImage cannonSprite = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        int colCannon = cannonSprite.getWidth() / CANNON_WIDTH_DEFAULT;
        cannonImgs = new BufferedImage[colCannon];
        for (int i = 0; i < cannonImgs.length; i++) {
            cannonImgs[i] = cannonSprite.getSubimage(i * CANNON_WIDTH_DEFAULT, 0, CANNON_WIDTH_DEFAULT, CANNON_HEIGHT_DEFAULT);
        }

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.BALL_IMG);

        BufferedImage bombSprite = LoadSave.GetSpriteAtlas(LoadSave.BOMB_SPRITE);
        int colBomb = bombSprite.getWidth() / BOMB_WIDTH_DEFAULT;
        int rowBomb = bombSprite.getHeight() / BOMB_HEIGHT_DEFAULT;
        bombImgs = new BufferedImage[rowBomb][colBomb];
        for (int i = 0; i < bombImgs.length; i++)//row
            for (int j = 0; j < bombImgs[i].length; j++)//col
                bombImgs[i][j] = bombSprite.getSubimage(j * BOMB_WIDTH_DEFAULT, i * BOMB_HEIGHT_DEFAULT, BOMB_WIDTH_DEFAULT, BOMB_HEIGHT_DEFAULT);
    }

    public void update(int[][] lvlData, Player player) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers) {
            if (gc.isActive())
                gc.update();
        }
        updateCannon(lvlData, player);
//        updateProjectiles(lvlData, player);
    }



    //    private void updateProjectiles(int[][] lvlData, Player player) {
//        for (Projectile p : projectiles)
//            if (p.isActive()) {
////                p.updatePos();
//                if (p.getHitbox().intersects(player.getHitBox())) {
//                    player.changeHealth(-25);
//                    p.setActive(false);
//                } else if (IsProjectileHittingLevel(p, lvlData))
//                    p.setActive(false);
//            }
//    }


    private void updateCannon(int[][] lvlData, Player player) {
        for (Cannon c : cannons) {
            if (!c.doAnimation) {
                if (c.getTileY() == (int) (player.getHitBox().y / Game.TILE_SIZE)) {
                    if (isPlayerInRange(c, player)) {
                        if (isPlayerInfrontOfCannon(c, player)) {
                            if (CanCannonSeePlayer(lvlData, player.getHitBox(), c.getHitBox(),
                                    c.getTileY())) {
                                c.setDoAnimation(true);
                            }
                        }
                    }
                }
            }
            c.update();
            if (c.getFrameIndex() == 4 && c.getFrameTick() == 0)
                shootCannon(c);
            for (Projectile p : projectiles)
                if (p.isActive()) {
                    p.updatePos();
                    if (p.getHitbox().intersects(player.getHitBox())) {
                        player.changeHealth(-25);
                        p.setActive(false);
                    } else if (IsProjectileHittingLevel(p, lvlData))
                        p.setActive(false);
                }
        }
    }

    private void shootCannon(Cannon c) {
        c.setDoAnimation(true);
        int dir = 1;
        if (c.getObjType() == CANNON_LEFT)
            dir = -1;
        projectiles.add(new Projectile((int) c.getHitBox().x, (int) c.getHitBox().y, dir));
    }


    private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getHitBox().x > player.getHitBox().x)
                return true;

        } else if (c.getHitBox().x < player.getHitBox().x)
            return true;
        return false;
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) Math.abs(player.getHitBox().x - c.getHitBox().x);
        return absValue <= Game.TILE_SIZE * 5;
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        drawPotions(g, xLvlOffset, yLvlOffset);
        drawContainer(g, xLvlOffset, yLvlOffset);
        drawCannon(g, xLvlOffset, yLvlOffset);
        drawProjectile(g, xLvlOffset, yLvlOffset);
    }


    private void drawProjectile(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Projectile pr : projectiles)
            if (pr.isActive()) {
                g.drawImage(cannonBallImg, (int) (pr.getHitbox().x - xLvlOffset), (int) (pr.getHitbox().y - yLvlOffset), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
            }
    }

    private void drawCannon(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Cannon c : cannons) {
            int x = (int) (c.getHitBox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }

            g.drawImage(cannonImgs[c.getFrameIndex()], x, (int) (c.getHitBox().y - yLvlOffset + CANNON_DRAW_OFFSET_Y), width, CANNON_HEIGHT, null);

        }
    }

    private void drawContainer(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                int type = -1;
                if (gc.getObjType() == BARREL) type = 0;
                else type = 1;
                if (type != -1)
                    g.drawImage(containerImgs[type][gc.getFrameIndex()],
                            (int) (gc.getHitBox().x - gc.getxDrawOffset() - xLvlOffset),
                            (int) (gc.getHitBox().y - gc.getyDrawOffset() - yLvlOffset),
                            CONTAINER_WIDTH,
                            CONTAINER_HEIGHT,
                            null);
            }
    }

    private void drawPotions(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int type = -1;
                if (p.getObjType() == RED_POTION)
                    type = 1;
                else
                    type = 0;

                if (type != -1)
                    g.drawImage(potionImgs[type][p.getFrameIndex()],
                            (int) (p.getHitBox().x - p.getxDrawOffset() - xLvlOffset),
                            (int) (p.getHitBox().y - p.getyDrawOffset() - yLvlOffset),
                            POTION_WIDTH,
                            POTION_HEIGHT,
                            null);
            }
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive())
                if (hitbox.intersects(p.getHitBox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
    }

    public void applyEffectToPlayer(Potion potion) {
        if (potion.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                if (gc.getHitBox().intersects(attackbox)) {
                    gc.setDoAnimation(true);
                    int type = 0;
                    if (gc.getObjType() == BARREL)
                        type = 1;
                    potions.add(new Potion((int) (gc.getHitBox().x + gc.getHitBox().width / 2), (int) (gc.getHitBox().y + gc.getHitBox().height / 4), type));
                    return;
                }
            }
    }

    public void resetAllObject() {
        loadObject(playing.getLevelManager().getCurrentLevel());
        for (Potion p : potions)
            p.reset();
        for (GameContainer gc : containers)
            gc.reset();
        for (Cannon c : cannons)
            c.reset();
    }
}
