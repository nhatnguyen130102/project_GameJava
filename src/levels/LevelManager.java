package levels;

import gameStates.GameState;
import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;

public class LevelManager {
        private final Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    //    public LevelManager(Game game){
    public LevelManager(Game game) {
        this.game = game;
//        levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites(); // lấy img nhỏ
        levels = new ArrayList<>();
        buildAllLevel();
    }
    public void loadNextLevel(){
        lvlIndex++;
        if(lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("No more levels");
            GameState.state = GameState.MENU;
        }
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffsetX(newLevel.getLvlOffsetX()); // lay phan thua theo tilesize
        game.getPlaying().setMaxLvlOffsetY(newLevel.getLvlOffsetY());
        game.getPlaying().getObjectManager().loadObject(newLevel);
    }
    private void buildAllLevel() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    // cắt img lớn thành img nhỏ
    private void importOutsideSprites() {
        int tileWidth, tileHeight;
        tileHeight = tileWidth = 32;
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * tileWidth, j * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (int j = 0; j < levels.get(lvlIndex).getLevelData().length; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i * Game.TILE_SIZE - xLvlOffset, j * Game.TILE_SIZE - yLvlOffset, Game.TILE_SIZE, Game.TILE_SIZE, null);

                // gridline
                g.drawLine(0,j * Game.TILE_SIZE - yLvlOffset , Game.SCREEN_WIDTH   , j * Game.TILE_SIZE - yLvlOffset);
                g.drawLine(i * Game.TILE_SIZE - xLvlOffset, 0, i *Game.TILE_SIZE - xLvlOffset,  Game.SCREEN_WIDTH);
                g.setColor(Color.RED);
//                String stringX = i * Game.TILE_SIZE +"";
//                String stringY =  j * Game.TILE_SIZE +"";
                String stringX = i +"";
                String stringY =  j +"";
                String combie = stringX +"," +stringY;
                g.drawString(combie,i*Game.TILE_SIZE  - xLvlOffset,j* Game.TILE_SIZE - yLvlOffset);
            }
        }
    }

    public void update() {

    }

    // lấy level hiện tại
    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }
    public int getAmountOfLevels(){
        return levels.size();
    }

    public int getLevelIndex() {
        return lvlIndex;
    }
}
