package entities;

import gameStates.Playing;
import levels.Level;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ultilz.Constants.EnemyConstants.*;
import static ultilz.LoadSave.*;


public class EnemyManager {
    private static Playing playing;
    private static BufferedImage[][] crabbyArr;
    private static BufferedImage[][] whaleArr;
    private static ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImg();
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : crabbies) {
            if (c.isActive())
                if (attackBox.intersects(c.getHitBox())) {
                    c.hurt(10);
                    return;
                }
        }

    }


    private void loadEnemyImg() {
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

    public static void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                c.update(lvlData, player);
                isAnyActive = true;
            }

            if (!isAnyActive)
                playing.setLevelCompleted(true);
        }
    }

    public static void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
    }

    private static void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getEnemyState()][c.getFrameIndex()],
                        (int) c.getHitBox().x - xLvlOffset - CRABBY_DRAW_OFFSET_X + c.flipX(),
                        (int) c.getHitBox().y - CRABBY_DRAW_OFFSET_Y,
                        CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
                if (c.currentHealth <= 0)
                    c.currentHealth = 0;
                float healthWidth = (int) ((c.currentHealth / (float) c.maxHealth) * c.getHitBox().width);
                g.setColor(Color.RED);
                g.fillRect((int) c.getHitBox().x - xLvlOffset, (int) c.getHitBox().y - 20, (int) healthWidth, 5);
//                c.drawHitBox(g,xLvlOffset);
//                c.drawAttackBox(g, xLvlOffset);
            }
        }
    }


    public void resetAllEnemies() {
        for (Crabby c : crabbies) {
            c.resetEnemy();
        }
    }
}
