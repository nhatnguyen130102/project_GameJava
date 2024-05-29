package levels;

import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {
    private final Game game;
    private BufferedImage[] levelSprite;
    private final Level levelOne;
    public LevelManager(Game game){
        this.game = game;
//        levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        levelOne = new Level(LoadSave.getLevelData());
    }

    private void importOutsideSprites() {
        int tileWidth, tileHeight;
        tileHeight=tileWidth=32;
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for(int j = 0; j < 4; j++){
            for(int i = 0; i < 12; i++){
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * tileWidth, j * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    public void draw(Graphics g){
        for(int j = 0; j < Game.MAX_ROW; j ++){
            for (int i = 0 ; i < Game.MAX_COL; i++){
                int index = levelOne.getSpriteIndex(i,j);
                g.drawImage(levelSprite[index],i * Game.TILE_SIZE ,j * Game.TILE_SIZE,Game.TILE_SIZE,Game.TILE_SIZE,null);

            }
        }
    }
    public void update(){

    }
    public Level getCurrentLevel(){
        return levelOne;
    }
}
