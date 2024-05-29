package ultilz;

import main.Game;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, int width, int height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    if (!IsSolid(x, y + height, lvlData))
                        return true;
        return false;
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
