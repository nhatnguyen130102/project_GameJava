package ultilz;

import entities.*;
import main.Game;
import objects.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ultilz.Constants.EnemyConstants.*;
import static ultilz.Constants.ObjectConstants.*;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {// kiem tra co the di chuyen
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width , y + height, lvlData))
                if (!IsSolid(x + width , y, lvlData))
                    return !IsSolid(x, y + height, lvlData);
        return false;
    }

    // nếu nhân vật chạm vào tile collsion = true => trả về vị trí duy nhất mà nhân vật có thể đứng (bên trái bên phải)
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        int currentTile = (int) (hitBox.x / Game.TILE_SIZE); // lấy vị trí hiện tại của nhân vật (toạ độ)
        if (xSpeed > 0) { // kiểm tra nhân vật qua phải
            // right
            int tilePos = currentTile * Game.TILE_SIZE; // lấy không gian mà nhân vật chiếm
            int xOffset = (int) (Game.TILE_SIZE - hitBox.width); // lấy không gian thực hitbox chiếm
            return tilePos + xOffset - 1;
        } else {
            // left
            return currentTile * Game.TILE_SIZE;
        }
    }
    public static void BombInWall(Bomb b, int[][]lvlData){
        if(IsSolid(b.getHitbox().x +b.getHitbox().width,b.getHitbox().y,lvlData)){
            b.getHitbox().x -= 5*Game.SCALE;
        }
    }
    // nếu nhân vật chạm vào tile collsion = true => trả về vị trí duy nhất mà nhân vật có thể đứng (bên trên bên dưới)
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
        int currentTile = (int) (hitBox.y / Game.TILE_SIZE);
        if (airSpeed > 0) {
            //Falling
            int tileYPos = currentTile * Game.TILE_SIZE;
            int yOffset = (int) (Game.TILE_SIZE - hitBox.height);
            return tileYPos + yOffset - 1;
        } else {
            //Jumping
            return currentTile * Game.TILE_SIZE;
        }
    }

    // kiểm tra nhân vật có đang đứng trên mặt đất hay không
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] lvlData) {
        if (!IsSolid(hitBox.x , hitBox.y + hitBox.height + 1, lvlData))
            return IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData);
        return true;
    }

    public static boolean IsChangDir(Rectangle2D.Float hitBox, int[][] lvlData) {
        if (IsSolid(hitBox.x - 1 , hitBox.y, lvlData)) {
            return true;
        }
        if (IsSolid(hitBox.x + hitBox.width + 1, hitBox.y, lvlData)) {
            return true;
        }
        return false;
    }

    public static boolean IsFloor(Rectangle2D.Float hitBox, float XSpeed, int[][] lvlData) {
        // kiểm tra bên phải
        if (IsSolid(hitBox.x + XSpeed, hitBox.y + hitBox.height + 1, lvlData))// kiểm tra bên trái
            return IsSolid(hitBox.x + XSpeed + hitBox.width, hitBox.y + hitBox.height + 1, lvlData);
        return false;
    }


    // kiểm tra nhân vật có đứng ở tile đã được duyệt hay k và kiểm tra nhân vật có chạm vào tile có collision = true hay k
    public static boolean IsSolid(float x, float y, int[][] lvlData) {
        // Cho phép nhân vật chạm vào phần rìa main screen
        int maxWidth = lvlData[0].length * Game.TILE_SIZE;
        int maxHeight = lvlData.length;
        // qua to
        if (x < 0 || x >= maxWidth * Game.TILE_SIZE)
            return true;
        if (y < 0 || y >= maxHeight * Game.TILE_SIZE)
            return true;
        // lấy col(x), row(y) để đi kiểm tra với lvlData(map, kiểm tra collision)
        float xIndex = x / Game.TILE_SIZE;
        float yIndex = y / Game.TILE_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int maxWidth = lvlData[0].length;
        int maxHeight = lvlData.length;
        if (xTile < 0 || xTile >= maxWidth) {
            return true;
        }
        if (yTile < 0 || yTile >= maxHeight) {
            return true;
        }
        int value = lvlData[yTile][xTile];
        // cho phép nahan vật chạm vào phần tile chưa đc duyệt, kiểm soát lỗi thôi
        return value >= 48 || value < 0 || value != 11;
    }
    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (IsAllTilesClear(xStart, xEnd, y, lvlData))
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
            }
        return true;
    }

    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float hbEnemy, Rectangle2D.Float hbPlayer, int tileY) {
        int enemyXTile = (int) (hbEnemy.x / Game.TILE_SIZE);
        int playerXTile = (int) (hbPlayer.x / Game.TILE_SIZE);
        if (enemyXTile > playerXTile){
            return IsAllTilesWalkable(playerXTile, enemyXTile, tileY, lvlData);
        }
        else {
            return IsAllTilesWalkable(enemyXTile, playerXTile, tileY, lvlData);
        }
    }

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        int maxTile = 48;
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= maxTile)
                    value = 0;
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILE_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILE_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
        return IsSolid(p.getHitbox().x + p.getHitbox().width - 5 * Game.SCALE, p.getHitbox().y + p.getHitbox().height - 5 * Game.SCALE, lvlData);
    }
    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }

    // tao danh sach enemy
    public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
        ArrayList<Crabby> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == CRABBY)
                    list.add(new Crabby(i * Game.TILE_SIZE, j * Game.TILE_SIZE));
            }
        }
        return list;
    }
    public static ArrayList<Spike> GetSpikes(BufferedImage img) {
        ArrayList<Spike> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();

                if (value == SPIKE) {
                    list.add(new Spike(i * Game.TILE_SIZE, j * Game.TILE_SIZE, value));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
                }
            }
        }
        return list;
    }
    public static ArrayList<Whale> GetWhales(BufferedImage img) {
        ArrayList<Whale> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == WHALE)
                    list.add(new Whale(i * Game.TILE_SIZE, j * Game.TILE_SIZE));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
            }
        }
        return list;
    }
    public static ArrayList<Captain> GetCaptains(BufferedImage img) {
        ArrayList<Captain> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == CAPTAIN)
                    list.add(new Captain(i * Game.TILE_SIZE, j * Game.TILE_SIZE));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
            }
        }
        return list;
    }

    public static ArrayList<BigGuy> GetBigGuy(BufferedImage img) {
        ArrayList<BigGuy> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == BIGGUY)
                    list.add(new BigGuy(i * Game.TILE_SIZE, j * Game.TILE_SIZE));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
            }
        }
        return list;
    }
    public static ArrayList<Potion> GetPotions(BufferedImage img) {
        ArrayList<Potion> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == RED_POTION || value == BLUE_POTION)
                    list.add(new Potion(i * Game.TILE_SIZE, j * Game.TILE_SIZE, value));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
            }
        }
        return list;
    }
    //get front top palm trees
    public static ArrayList<FrontTopTree> GetTopTree(BufferedImage img) {
        ArrayList<FrontTopTree> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == TOP_TREE)
                    list.add(new FrontTopTree(i * Game.TILE_SIZE, j * Game.TILE_SIZE, value));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
            }
        }
        return list;
    }
    //get front bottom palm trees
    public static ArrayList<FrontBottomTree> GetBottomTree(BufferedImage img) {
        ArrayList<FrontBottomTree> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value >= 90 && value <= 96)
                    list.add(new FrontBottomTree(i * Game.TILE_SIZE, j * Game.TILE_SIZE, value));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
            }
        }
        return list;
    }
    public static ArrayList<PalmTree> GetPalmTree(BufferedImage img) {
        ArrayList<PalmTree> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == PALM_TREE)
                    list.add(new PalmTree(i * Game.TILE_SIZE, j * Game.TILE_SIZE, value));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
            }
        }
        return list;
    }

    public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
        ArrayList<GameContainer> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();

                if (value == BOX || value == BARREL) {
                    list.add(new GameContainer(i * Game.TILE_SIZE, j * Game.TILE_SIZE, value));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
                }
            }
        }
        return list;
    }


    public static ArrayList<Cannon> GetCannons(BufferedImage img) {
        ArrayList<Cannon> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();

                if (value == CANNON_LEFT || value == CANNON_RIGHT) {
                    list.add(new Cannon(i * Game.TILE_SIZE, j * Game.TILE_SIZE, value));// tao 1 doi tuong enemy tuong ung tai vi tri dc chi dinh tren map
                }
            }
        }
        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++) {//Row
            for (int i = 0; i < img.getWidth(); i++) {//Col
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100)
                    return new Point(i * Game.TILE_SIZE, j * Game.TILE_SIZE);
            }
        }
        return new Point(Game.TILE_SIZE, Game.TILE_SIZE);
    }
}
