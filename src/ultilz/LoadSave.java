package ultilz;

import main.Game;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {
    public static final String PLAYER_ATLAS = "Player/player_sprites.png";
    public static final String LEVEL_ATLAS = "Map/outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "Level/level_one_data.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            if (is != null) {
                image = ImageIO.read(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    public static int[][] getLevelData() {
        int[][] lvlData = new int[Game.MAX_ROW][Game.MAX_COL];
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                 Color color = new Color(img.getRGB(i, j));
                int value =  color.getRed();
                if(value >= 48)
                    value = 0;
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
}
