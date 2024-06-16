package levels;

import entities.Crabby;
import entities.Whale;
import main.Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ultilz.HelpMethods.*;

public class Level {
    private int [][] lvlData;
    private BufferedImage img;
    private ArrayList<Crabby> crabs;
    private ArrayList<Whale> whales;
    private int lvlTilesWide;// Độ dài tối đa của map hiện tại ( toạ độ )
    private int maxTilesOffset;// Độ dài tối đa màn hình chưa hiển thị ( map tối đa - màn hình tối đa = phần thừa tối đa )
    private int maxLvlOffsetX;// Độ dài ( toạ độ x tilesize ) của phần thừa
    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffsets();
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.MAX_COL;
        maxLvlOffsetX = Game.TILE_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
        whales = GetWhales(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    // lấy tile ở toạ độ tham số và trả về giá trị của tile
    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }
    // lấy danh sách tile
    public int [][]getLevelData(){
        return lvlData;
    }
    public int getLvlOffsetX(){
        return  maxLvlOffsetX;
    }
    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }
    public ArrayList<Whale> getWhales(){
        return whales;
    }
}
