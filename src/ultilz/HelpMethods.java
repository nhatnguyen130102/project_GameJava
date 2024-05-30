package ultilz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    if (!IsSolid(x, y + height, lvlData))
                        return true;
        return false;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        int currentTile = (int) (hitBox.x / Game.TILE_SIZE);
        if (xSpeed > 0) {
            // right
            int tilePos = currentTile * Game.TILE_SIZE;
            int xOffset = (int) (Game.TILE_SIZE - hitBox.width);
            return tilePos + xOffset - 1;
        } else {
            // left
            return currentTile * Game.TILE_SIZE;
        }
    }

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

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] lvlData) {
        // check the pel below bottomleft and bottomright
        if (!IsSolid(hitBox.x, hitBox.y + hitBox.height + 1, lvlData))
            if (!IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData))
                return false;
        return true;
    }

    public static boolean IsSolid(float x, float y, int[][] lvlData) {
        if (x < 0 || x >= Game.SCREEN_WIDTH) {
            return true;
        }
        if (y < 0 || y >= Game.SCREEN_HEIGHT) {
            return true;
        }
        float xIndex = x / Game.TILE_SIZE;
        float yIndex = y / Game.TILE_SIZE;

        int value = lvlData[(int) yIndex][(int) xIndex];
        if (value >= 48 || value < 0 || value != 11) {
            return true;
        }
        return false;
    }
}
