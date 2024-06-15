package levels;

import gameStates.GameState;
import main.Game;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffsetX());
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

    public void draw(Graphics g, int xLvlOffset) {
        for (int j = 0; j < Game.MAX_ROW; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i * Game.TILE_SIZE - xLvlOffset, j * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE, null);
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
}
