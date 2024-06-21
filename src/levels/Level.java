package levels;

import entities.Crabby;
import entities.Whale;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import ultilz.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ultilz.HelpMethods.*;

public class Level {
    private int [][] lvlData;
    private BufferedImage img;
    private ArrayList<Crabby> crabs;
    private ArrayList<Whale> whales;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Cannon> cannons;

    private int lvlTilesWideX;// Độ dài tối đa của map hiện tại ( toạ độ )
    private int lvlTilesWideY;// Độ cao tối đa của map hiện tại ( toạ độ )
    private int maxTilesOffsetX;// Độ dài tối đa màn hình chưa hiển thị ( map tối đa - màn hình tối đa = phần thừa tối đa )
    private int maxTilesOffsetY;// Độ dài tối đa màn hình chưa hiển thị ( map tối đa - màn hình tối đa = phần thừa tối đa )
    private int maxLvlOffsetX;// Độ dài ( toạ độ x tilesize ) của phần thừa
    private int maxLvlOffsetY;// Độ dài ( toạ độ x tilesize ) của phần thừa
    private Point playerSpawn;


    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createCannons();
        createContainers();
        calcLvlOffsets();
        calcPlayerSpawn();

    }

    private void createCannons() {
        cannons = HelpMethods.GetCannons(img);
    }
    private void createContainers() {
        containers = HelpMethods.GetContainers(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }

    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calcLvlOffsets() {
        lvlTilesWideX = img.getWidth(); // lay w va h theo pixel
        lvlTilesWideY = img.getHeight();

        maxTilesOffsetX = lvlTilesWideX - Game.MAX_COL; // lay phan thua w va h theo pixel
        maxTilesOffsetY = lvlTilesWideY - Game.MAX_ROW;

        maxLvlOffsetX = Game.TILE_SIZE * maxTilesOffsetX; // lay phan thua w va h theo tilesize
        maxLvlOffsetY = Game.TILE_SIZE * maxTilesOffsetY;
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
    public int getLvlOffsetY(){
        return  maxLvlOffsetY;
    }
    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }
    public ArrayList<Whale> getWhales(){
        return whales;
    }
    public Point getPlayerSpawn(){
        return playerSpawn;
    }
    public ArrayList<Potion> getPotions(){
        return potions;
    }
    public ArrayList<GameContainer> getContainer(){
        return containers;
    }
    public ArrayList<Cannon> getCannons(){
        return cannons;
    }
}
