package ultilz;

import entities.Crabby;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static ultilz.Constants.EnemyConstants.CRABBY;


public class LoadSave {
    public static final String PLAYER_ATLAS = "Player/player_sprites_2.png";
    public static final String LEVEL_ATLAS = "Map/outside_sprites.png";
    public static final String MENU_BACKGROUND = "Menu/menu_background.png";
    public static final String BACKGROUND = "Menu/beautiful-anime-landscape-cartoon-scene.jpg";
    public static final String LEVEL_ONE_DATA = "Level/level_one_data_long_2.png";
    //    public static final String LEVEL_ONE_DATA = "Level/level_one_data - Copy.png";
    public static final String MENU_BUTTONS = "Menu/button_atlas.png";
    public static final String PAUSE_BACKGROUND = "Menu/pause_menu.png";
    public static final String SOUND_BUTTONS = "Menu/sound_button.png";
    public static final String URM_BUTTONS = "Menu/urm_buttons.png";
    public static final String VOLUME_BUTTONS = "Menu/volume_buttons.png";
    public static final String PLAYING_BG_IMG = "Level/Obj&BackGround/playing_bg_img.png";
    public static final String BIG_CLOUDS = "Level/Obj&BackGround/big_clouds.png";
    public static final String SMALL_CLOUDS = "Level/Obj&BackGround/small_clouds.png";
    public static final String CRABBY_SPRITE = "Enemy/crabby_sprite.png";
    public static final String STATUS_BAR = "Menu/health_power_bar.png";


    public static BufferedImage img;

    // đọc file img trả về img lớn
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage image = null;// biến img lớn
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);// đọc file img.png(...)
        try { // kiểm tra lỗi ngoài ý muốn
            if (is != null) { // kiểm ra file img == null;
                image = ImageIO.read(is);// thực hiện đọc file img
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;// trả về img lớn
    }

    // đọc và lấy toàn bộ tile lưu vào mảng 2 chiều
    public static int[][] GetLevelData() {
        img = GetSpriteAtlas(LEVEL_ONE_DATA);
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

    public static ArrayList<Crabby> GetCrabs() {
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
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



    public static int getCol() {
        return img.getWidth();
    }

    public static int getRow() {
        return img.getHeight();
    }
}
