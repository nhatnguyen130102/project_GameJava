package ultilz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))// kiểm tra vị trí nhân vật đang không nằm trong tile có collision = true
            if (!IsSolid(x + width, y + height, lvlData)) // kiểm tra vị trí phía bên phải & phía dưới của nhân vật có bị dính tile có collision = true
                if (!IsSolid(x + width, y, lvlData)) // kiểm tra vị trí phía bên phải của nhân vật có bị dính tile có collision = true
                    return !IsSolid(x, y + height, lvlData); // kiểm tra vị trí bên dưới của nhân vật có bị dính tile có collision = true
        return false;
    }


    // nếu nhân vật chạm vào tile collsion = true => trả về vị trí duy nhất mà nhân vật có thể đứng (bên trái bên phải)
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        int currentTile = (int) (hitBox.x / Game.TILE_SIZE); // lấy vị trí hiện tại của nhân vật
        if (xSpeed > 0) { // kiểm tra nahan vật qua phải
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
    public static float GetEntityYPosUnderRootOrAboveFloor(Rectangle2D.Float hitBox, float airSpped) {
        int currentTile = (int) (hitBox.y / Game.TILE_SIZE);
        if (airSpped > 0) {
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
        // check the pel below bottomleft and bottomright
        if (!IsSolid(hitBox.x, hitBox.y + hitBox.height + 1, lvlData)) // kiểm tra phía dưới của nhân vật có tile collsion = true hay không
            return IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData); // kiểm tra nhân vật có đang đứng trên tile collision = true hay k
        return true;
    }

    // kiểm tra nhân vật có đứng ở tile đã được duyệt hay k và kiểm tra nhân vật có chạm vào tile có collision = true hay k
    public static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILE_SIZE;
//        int maxHeight = lvlData[0]
        // Cho phép nhân vật chạm vào phần rìa main screen
        if (x < 0 || x >= maxWidth)
                return true;
        if (y < 0 || y >= Game.SCREEN_HEIGHT)
            return true;
        // lấy col(x), row(y) để đi kiểm tra với lvlData(map, kiểm tra collision)
        float xIndex = x / Game.TILE_SIZE;
        float yIndex = y / Game.TILE_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];
        // cho phép nahan vật chạm vào phần tile chưa đc duyệt, kiểm soát lỗi thôi
        if (value >= 48 || value < 0 || value != 11)
            return true;
        return false;
    }
}
