package entities;

import gameStates.Playing;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import static ultilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private static BufferedImage[][] crabbyArr;
    private static ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImg();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = LoadSave.GetCrabs();

    }

    private void loadEnemyImg() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        int row = temp.getHeight() / CRABBY_HEIGHT_DEFAULT;
        int col = temp.getWidth() / CRABBY_WIDTH_DEFAULT;
        crabbyArr = new BufferedImage[row][col];
        for (int i = 0; i < crabbyArr.length; i++) {
            for (int j = 0; j < crabbyArr[i].length; j++) {
                crabbyArr[i][j] = temp.getSubimage(j * CRABBY_WIDTH_DEFAULT, i * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    public static void update() {
        for (Crabby c : crabbies) {
            c.update();
        }
    }

    public static void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);

    }

    private static void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies) {
            g.drawImage(crabbyArr[c.getEnemyState()][c.getFrameIndex()], (int) c.getHitBox().x - xLvlOffset, (int) c.getHitBox().y, CRABBY_WIDTH, CRABBY_HEIGHT, null);
        }
    }
}
