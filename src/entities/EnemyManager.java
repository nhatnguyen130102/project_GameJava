package entities;

import gameStates.Playing;
import levels.Level;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ultilz.Constants.BehaviorClosing.*;
import static ultilz.Constants.BehaviorOpening.*;
import static ultilz.Constants.BombTiles.*;
import static ultilz.Constants.EnemyConstants.*;
import static ultilz.Constants.PlayerConstants.*;
import static ultilz.LoadSave.*;


public class EnemyManager {
    private static Playing playing;
    private static BufferedImage[][] crabbyArr;
    private static BufferedImage[][] whaleArr;
    private static ArrayList<Crabby> crabbies = new ArrayList<>();
    private static ArrayList<Whale> whales = new ArrayList<>();
    private static BufferedImage behaviorClosingImg;
    private static BufferedImage[] behaviorOpeningImg;


    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyCrabbyImg();
        loadEnemyWhaleImg();
        loadBehaviorImg();
//        updateHitBox();
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
        whales = level.getWhales();
    }

    public void checkEnemyExplode(Rectangle2D.Float explodeBox) {
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                if (explodeBox.intersects(c.getHitBox())) {
                    c.hurt(BOMB_DMG);
                    return;
                }
            }
        }
        for (Whale w : whales) {
            if (w.isActive()) {
                if (explodeBox.intersects(w.getHitBox())) {
                    w.hurt(BOMB_DMG);
                    return;
                }

            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                if (attackBox.intersects(c.getHitBox())) {
                    c.hurt(PLAYER_DMG);
                    return;
                }

            }
        }
        for (Whale w : whales) {
            if (w.isActive()) {
                if (attackBox.intersects(w.getHitBox())) {
                    w.hurt(PLAYER_DMG);
                    return;
                }

            }
        }
    }

    private void loadBehaviorImg() {
        behaviorClosingImg = GetSpriteAtlas(LoadSave.BEHAVIOR_CLOSING);

//        BufferedImage temp = GetSpriteAtlas(LoadSave.BEHAVIOR_OPENING);
//        for (int i = 0; i < 4; i++) {
//            behaviorOpeningImg[i] = temp.getSubimage(i * BEHAVIOR_OPENING_WIDTH_DEFAULT, BEHAVIOR_OPENING_WIDTH_DEFAULT, BEHAVIOR_OPENING_WIDTH_DEFAULT, BEHAVIOR_OPENING_WIDTH_DEFAULT);
//        }

    }

    private void loadEnemyCrabbyImg() {
        BufferedImage temp = GetSpriteAtlas(CRABBY_SPRITE);// lay 1 img lon
        int row = temp.getHeight() / CRABBY_HEIGHT_DEFAULT;
        int col = temp.getWidth() / CRABBY_WIDTH_DEFAULT;
        crabbyArr = new BufferedImage[row][col];// tao array lay animation
        for (int i = 0; i < crabbyArr.length; i++) {// cat tam hinh lon
            for (int j = 0; j < crabbyArr[i].length; j++) {
                crabbyArr[i][j] = temp.getSubimage(j * CRABBY_WIDTH_DEFAULT, i * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    private void loadEnemyWhaleImg() {
        BufferedImage temp = GetSpriteAtlas(WHALE_SPRITE);// lay 1 img lon
        int row = temp.getHeight() / WHALE_HEIGHT_DEFAULT;
        int col = temp.getWidth() / WHALE_WIDTH_DEFAULT;
        whaleArr = new BufferedImage[row][col];// tao array lay animation
        for (int i = 0; i < whaleArr.length; i++) {// cat tam hinh lon
            for (int j = 0; j < whaleArr[i].length; j++) {
                whaleArr[i][j] = temp.getSubimage(j * WHALE_WIDTH_DEFAULT, i * WHALE_HEIGHT_DEFAULT, WHALE_WIDTH_DEFAULT, WHALE_HEIGHT_DEFAULT);
            }
        }
    }

    public static void update(int[][] lvlData, Player player, ArrayList<Bomb> bombs) {
        boolean isAnyActive = false;
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                c.update(lvlData, player);
                isAnyActive = true;
            }
        }
        for (Whale w : whales) {
            if (w.isActive()) {
                w.update(lvlData, player, bombs);

                isAnyActive = true;
            }
        }
        if (!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public static void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        drawCrabs(g, xLvlOffset, yLvlOffset);
        drawWhales(g, xLvlOffset, yLvlOffset);
    }

    private static void drawCrabs(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getEnemyState()][c.getFrameIndex()],
                        (int) c.getHitBox().x - xLvlOffset - CRABBY_DRAW_OFFSET_X + c.flipX(),
                        (int) c.getHitBox().y - yLvlOffset - CRABBY_DRAW_OFFSET_Y,
                        CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
                if (c.currentHealth <= 0)
                    c.currentHealth = 0;
                if (c.getStartBehaviorState()) {
                    g.drawImage(behaviorClosingImg,
                            (int) c.getHitBox().x - xLvlOffset,
                            (int) c.getHitBox().y - yLvlOffset - 20,
                            BEHAVIOR_CLOSING_WIDTH, BEHAVIOR_CLOSING_HEIGHT, null
                    );
                }
//                float healthWidth = (int) ((c.currentHealth / (float) c.maxHealth) * c.getHitBox().width);
//                g.setColor(Color.RED);
//                g.fillRect((int) c.getHitBox().x - xLvlOffset, (int) c.getHitBox().y - 20, (int) healthWidth, 5);
//                c.drawHitBox(g,xLvlOffset);
//                c.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    private static void drawWhales(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Whale w : whales) {
            if (w.isActive()) {
                g.drawImage(whaleArr[w.getEnemyState()][w.getFrameIndex()],
                        (int) (w.getHitBox().x - WHALE_DRAW_OFFSET_X) - xLvlOffset + w.flipX(),
                        (int) (w.getHitBox().y - yLvlOffset - WHALE_DRAW_OFFSET_Y),
                        (int) (WHALE_WIDTH * w.flipW()), (int) (WHALE_HEIGHT), null);
                if (w.currentHealth <= 0)
                    w.currentHealth = 0;
                if (w.getStartBehaviorState()) {
                    g.drawImage(behaviorClosingImg,
                            (int) w.getHitBox().x - xLvlOffset + w.flipX(),
                            (int) w.getHitBox().y - yLvlOffset - 20,
                            BEHAVIOR_CLOSING_WIDTH , BEHAVIOR_CLOSING_HEIGHT, null
                    );
                }
//                float healthWidth = (int) ((w.currentHealth / (float) w.maxHealth) * w.getHitBox().width);
//                g.setColor(Color.RED);
//                g.fillRect((int) w.getHitBox().x - WHALE_DRAW_OFFSET_X - xLvlOffset, (int) w.getHitBox().y - 20, (int) healthWidth, 5);
//                w.drawHitBox(g, xLvlOffset,yLvlOffset);
//                w.drawAttackBox(g, xLvlOffset, yLvlOffset);
            }
        }
    }


    public void resetAllEnemies() {
        for (Crabby c : crabbies)
            c.resetEnemy();

        for (Whale w : whales)
            w.resetEnemy();
    }


}
