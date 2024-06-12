package ultilz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
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
        // dau tien kiem tra phia duoi ben trai co tile hay khong, neu co thi return true,
        // neu khong thi kiem tra phia duoi ben phai co tile hay khong, neu co thi return true neu khong thi return false
        // (nghia la nhan vat inAir, khi nay se xu li khi state == inAir)
        if (!IsSolid(hitBox.x, hitBox.y + hitBox.height + 1, lvlData))
            return IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData);
        return true;
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
        if (x < 0 || x >= LoadSave.getCol() * Game.TILE_SIZE)
            return true;
        if (y < 0 || y >= Game.SCREEN_HEIGHT)
            return true;
        // lấy col(x), row(y) để đi kiểm tra với lvlData(map, kiểm tra collision)
        float xIndex = x / Game.TILE_SIZE;
        float yIndex = y / Game.TILE_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];
        // cho phép nahan vật chạm vào phần tile chưa đc duyệt, kiểm soát lỗi thôi
        if (value >= 48 || value < 0 || value != 11)
            return true;
        return false;
    }
    public static boolean IsAllTileWalkable(int xStart, int xEnd, int y , int[][]lvlData){
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if (!IsTileSolid(xStart + i, y + i, lvlData))
                return false;
        }
        return true;
    }
    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float hbEnemy, Rectangle2D.Float hbPlayer, int tileY) {
        int enemyXTile = (int) (hbEnemy.x / Game.TILE_SIZE);
        int playerXTile = (int) (hbPlayer.x / Game.TILE_SIZE);
        if (enemyXTile > playerXTile)
               return IsAllTileWalkable(playerXTile,enemyXTile,tileY,lvlData);
        else
            return IsAllTileWalkable(enemyXTile,playerXTile,tileY,lvlData);
    }
}
