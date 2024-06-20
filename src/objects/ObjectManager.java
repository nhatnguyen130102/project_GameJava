package objects;

import gameStates.Playing;
import levels.Level;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ultilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void loadObject(Level newLevel) {
        potions = newLevel.getPotions();
        containers = newLevel.getContainer();


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
        int rowOBJ = potionSprite.getHeight() / POTION_HEIGHT_DEFAULT;
        int colOBJ = potionSprite.getWidth() / POTION_WIDTH_DEFAULT;
        containerImgs = new BufferedImage[rowOBJ][colOBJ];
        for (int j = 0; j < containerImgs.length; j++) {
            for (int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = containerSprite.getSubimage(i * CONTAINER_WIDTH_DEFAULT, j * CONTAINER_HEIGHT_DEFAULT, CONTAINER_WIDTH_DEFAULT, CONTAINER_HEIGHT_DEFAULT);
            }
        }
    }

    public void update() {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers) {
            if (gc.isActive())
                gc.update();
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        drawPotions(g, xLvlOffset, yLvlOffset);
        drawContainer(g, xLvlOffset, yLvlOffset);
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
                if (p.getObjType() == RED_POTION) type = 0;
                else type = 1;

                if (type != -1)
                    g.drawImage(potionImgs[type][p.getFrameIndex()],
                            (int) (p.getHitBox().x - p.getxDrawOffset() - xLvlOffset),
                            (int) (p.getHitBox().y - p.getyDrawOffset() - yLvlOffset),
                            POTION_WIDTH,
                            POTION_HEIGHT,
                            null);
            }


    }


}
