package objects;

import audio.AudioPlayer;
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
import static ultilz.LoadSave.*;
import static ultilz.LoadSave.PALM_TREE;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs, bombImgs;
    private BufferedImage[] cannonImgs;
    private BufferedImage[] cannonAttackEffectImgs;
    private BufferedImage[] ballExplodeImgs;
    private BufferedImage[] palmTreeImgs;
    private BufferedImage[] topTreeImgs;
    private BufferedImage[] bottomTreeImgs;
    private BufferedImage[] houseImgs;
    private BufferedImage cannonBallImg;
    private ArrayList<Potion> potions;
    private ArrayList<PalmTree> palmTrees;
    private ArrayList<FrontTopTree> topTrees;
    private ArrayList<FrontBottomTree> bottomTrees;
    private ArrayList<House> house;
    private ArrayList<GameContainer> containers;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private BufferedImage spikeImg;
    private ArrayList<Spike> spikes;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void loadObject(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainer());
        cannons = newLevel.getCannons();
        projectiles.clear();
        spikes = new ArrayList<>(newLevel.getSpikes());
        palmTrees = new ArrayList<>(newLevel.getPalmTrees());
        topTrees = new ArrayList<>(newLevel.getTopTrees());
        bottomTrees = new ArrayList<>(newLevel.getBottomTrees());
        house = new ArrayList<>(newLevel.getHouse());
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


        BufferedImage cannonAttackEffect = LoadSave.GetSpriteAtlas(CANNON_ATTACK_EFFECT);
        cannonAttackEffectImgs = new BufferedImage[6];
        for (int i = 0; i < 6; i++) {
            cannonAttackEffectImgs[i] = cannonAttackEffect.getSubimage(i * CANNON_ATTACK_EFFECT_WIDTH_DEFAULT, 0, CANNON_ATTACK_EFFECT_WIDTH_DEFAULT, CANNON_ATTACK_EFFECT_HEIGHT_DEFAULT);
        }

        BufferedImage ballExplode = LoadSave.GetSpriteAtlas(BALL_EXPLODE);
        ballExplodeImgs = new BufferedImage[7];
        for (int i = 0; i < 6; i++) {
            ballExplodeImgs[i] = ballExplode.getSubimage(i * BALL_EXPLODE_WIDTH, 0, BALL_EXPLODE_WIDTH, BALL_EXPLODE_HEIGHT);
        }

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.BALL_IMG);
        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.SPIKE_IMG);

        BufferedImage bombSprite = LoadSave.GetSpriteAtlas(LoadSave.BOMB_SPRITE);
        int colBomb = bombSprite.getWidth() / BOMB_WIDTH_DEFAULT;
        int rowBomb = bombSprite.getHeight() / BOMB_HEIGHT_DEFAULT;
        bombImgs = new BufferedImage[rowBomb][colBomb];
        for (int i = 0; i < bombImgs.length; i++)//row
            for (int j = 0; j < bombImgs[i].length; j++)//col
                bombImgs[i][j] = bombSprite.getSubimage(j * BOMB_WIDTH_DEFAULT, i * BOMB_HEIGHT_DEFAULT, BOMB_WIDTH_DEFAULT, BOMB_HEIGHT_DEFAULT);


        BufferedImage palmTreeSprite = LoadSave.GetSpriteAtlas(PALM_TREE);
        palmTreeImgs = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            palmTreeImgs[i] = palmTreeSprite.getSubimage(i * PALM_TREE_WIDTH_DEFAULT, 0, PALM_TREE_WIDTH , PALM_TREE_HEIGHT);
        }

        BufferedImage topTreeSprite = LoadSave.GetSpriteAtlas(LoadSave.TOP_TREE);
        topTreeImgs = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            topTreeImgs[i] = topTreeSprite.getSubimage(i * TOP_TREE_WIDTH_DEFAULT, 0, TOP_TREE_WIDTH , TOP_TREE_HEIGHT);
        }

        BufferedImage bottomTreeSprite = LoadSave.GetSpriteAtlas(BOTTOM_TREE);
        bottomTreeImgs = new BufferedImage[7];
        for (int i = 0; i < 7; i++) {
            bottomTreeImgs[i] = bottomTreeSprite.getSubimage(i * BOTTOM_TREE_WIDTH_DEFAULT, 0, BOTTOM_TREE_WIDTH , BOTTOM_TREE_HEIGHT);
        }

        BufferedImage houseSprite = LoadSave.GetSpriteAtlas(HOUSE);
        houseImgs = new BufferedImage[16];
        for (int i = 0; i < 4; i++) { //row
            for (int j = 0; j < 4; j++) //col
                houseImgs[4 * i + j] = houseSprite.getSubimage(j * HOUSE_WIDTH_DEFAULT, i * HOUSE_HEIGHT_DEFAULT, HOUSE_WIDTH , HOUSE_HEIGHT);
        }
    }

    public void update(int[][] lvlData, Player player) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers) {
            if (gc.isActive())
                gc.update();
        }

        for(PalmTree p : palmTrees){
            if(p != null)
                p.update();
        }

        for(FrontTopTree t : topTrees){
            if(t != null)
                t.update();
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
            if (c.getFrameIndex() == 4 && c.getFrameTick() == 0) {
                shootCannon(c);
                c.setShooting(true);
            }

            for (Projectile p : projectiles)
                if (p.isActive()) {
                    p.updatePos();
                    if (p.getHitbox().intersects(player.getHitBox())) {
                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.EXPLODE);
                        p.setInfoExplodeBox(p);
                        p.setActive(false);
                        player.changeHealth(-1);
//                        p.setExplode(true);
                    } else if (IsProjectileHittingLevel(p, lvlData)) {
                        p.setActive(false);
//                        p.setExplode(true);
                    }
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
        drawSpikes(g, xLvlOffset, yLvlOffset);
        drawTopTree(g, xLvlOffset, yLvlOffset);
        drawBottomTree(g, xLvlOffset, yLvlOffset);
        drawHouse(g, xLvlOffset, yLvlOffset);
//        drawPalmTree(g,xLvlOffset,yLvlOffset);
    }

    public void drawPalmTree(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(PalmTree p : palmTrees){
            g.drawImage(palmTreeImgs[p.getFrameIndex()], (int) p.getHitBox().x - xLvlOffset, (int) p.getHitBox().y - yLvlOffset,PALM_TREE_WIDTH * 2,PALM_TREE_HEIGHT * 2,null);
        }
    }

    public void drawTopTree(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(FrontTopTree t : topTrees){
            g.drawImage(topTreeImgs[t.getFrameIndex()], (int) t.getHitBox().x - xLvlOffset - TOP_TREE_DRAW_OFFSET_X, (int) t.getHitBox().y - yLvlOffset,TOP_TREE_WIDTH * 2,TOP_TREE_HEIGHT * 2,null);
        }
    }

    public void drawBottomTree(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(FrontBottomTree b : bottomTrees){
            g.drawImage(bottomTreeImgs[b.objType], (int) b.getHitBox().x - xLvlOffset, (int) b.getHitBox().y - yLvlOffset,BOTTOM_TREE_WIDTH * 2,BOTTOM_TREE_HEIGHT * 2,null);
        }
    }

    public void drawHouse(Graphics g, int xLvlOffset, int yLvlOffset) {
        for(House h : house){
            g.drawImage(houseImgs[h.objType], (int) h.getHitBox().x - xLvlOffset, (int) h.getHitBox().y - yLvlOffset - HOUSE_DRAW_OFFSET_Y, HOUSE_WIDTH, HOUSE_HEIGHT,null);
        }
    }

    private void drawSpikes(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Spike s : spikes) {
            int x = (int) (s.getHitBox().x - xLvlOffset);
            int y = (int) (s.getHitBox().y - yLvlOffset - SPIKE_DRAW_OFFSET_Y);

            g.drawImage(spikeImg, x, y, SPIKE_WIDTH, SPIKE_HEIGHT, null);
        }
    }

    public void checkSpikesTouched(Rectangle2D.Float hitbox) {
        for (Spike s : spikes)
            if (hitbox.intersects(s.getHitBox())) {
                if (!playing.getPlayer().isSpikeState()) {
                    playing.getPlayer().changeHealth(-SPIKE_DMG);
                    playing.getPlayer().setSpikeState(true);
                }
            }
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
            int x_attack = (int) (x - c.getHitBox().width);
            int width = CANNON_WIDTH;
            int widht_attack = CANNON_ATTACK_EFFECT_WIDTH;
            if (c.getObjType() == CANNON_RIGHT) {
                x += width;
                x_attack += (int) (82 * 2 * Game.SCALE);
                width *= -1;
                widht_attack *= -1;
            }

            g.drawImage(cannonImgs[c.getFrameIndex()], x, (int) (c.getHitBox().y - yLvlOffset + CANNON_DRAW_OFFSET_Y), width, CANNON_HEIGHT, null);

            if (c.isShooting()) {
                g.drawImage(cannonAttackEffectImgs[c.getFrameIndexA()], x_attack, (int) (c.getHitBox().y - yLvlOffset), widht_attack, CANNON_ATTACK_EFFECT_HEIGHT, null);
            }


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
                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.COLLECT_POTION);
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

    public void checkObjectExplode(Rectangle2D.Float attackbox) {
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
        for(PalmTree p : palmTrees)
            p.reset();
        for(FrontTopTree t : topTrees)
            t.reset();
    }
}
