package levels;

import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {
//    private final Game game;
    private BufferedImage[] levelSprite;
    private final Level levelOne;
//    public LevelManager(Game game){
         public LevelManager(){
//        this.game = game;
//        levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites(); // lấy img nhỏ
        levelOne = new Level(LoadSave.getLevelData()); // lấy toạ độ của tile
    }
    // cắt img lớn thành img nhỏ
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
    public void draw(Graphics g, int xLvlOffset){
        for(int j = 0; j < LoadSave.getRow(); j ++){
            for (int i = 0 ; i < LoadSave.getCol(); i++){
                int index = levelOne.getSpriteIndex(i,j);
                g.drawImage(levelSprite[index],i * Game.TILE_SIZE - xLvlOffset,j * Game.TILE_SIZE,Game.TILE_SIZE,Game.TILE_SIZE,null);
            }
        }
    }
    public void update(){

    }
    // lấy level hiện tại
    public Level getCurrentLevel(){
        return levelOne;
    }
}
