package ultilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;


public class LoadSave {
    public static final String PLAYER_ATLAS = "Player/player_bombguy_sprites.png";
    //    public static final String PLAYER_ATLAS = "Player/player_sprites_2.png";
    public static final String SPIKE_IMG = "Object/spikes.png";
    public static final String LEVEL_ATLAS = "Map/outside_sprites.png";
    public static final String MENU_BACKGROUND = "Menu/menu_background.png";
    public static final String BACKGROUND = "Menu/beautiful-anime-landscape-cartoon-scene.jpg";
    public static final String MENU_BUTTONS = "Menu/button_atlas.png";
    public static final String PAUSE_BACKGROUND = "Menu/pause_menu.png";
    public static final String SOUND_BUTTONS = "Menu/sound_button.png";
    public static final String URM_BUTTONS = "Menu/urm_buttons.png";
    public static final String VOLUME_BUTTONS = "Menu/volume_buttons.png";
    public static final String PLAYING_BG_IMG = "Level/Obj&BackGround/playing_bg_img.png";
    public static final String BIG_CLOUDS = "Level/Obj&BackGround/big_clouds.png";
    public static final String SMALL_CLOUDS = "Level/Obj&BackGround/small_clouds.png";
    public static final String CRABBY_SPRITE = "Enemy/crabby_sprite.png";
    public static final String BIGGUY_SPRITE = "Enemy/Big_Guy.png";

    public static final String CRABBY_ATTACK_EFFECT= "Enemy/crabby_attack.png";

    public static final String CAPTAIN_SPRITE = "Enemy/Captain.png";
    public static final String STATUS_BAR = "Menu/health_power_bar.png";
    public static final String COMPLETED_IMG = "Menu/completed_sprite.png";
    public static final String WHALE_SPRITE = "Enemy/whale_sprite.png";
    public static final String BOMB_SPRITE = "Object/bomb(96x108).png";
    public static final String POTION_ATLAS = "Object/potions_sprites.png";
    public static final String CONTAINER_ATLAS = "Object/objects_sprites.png";
    public static final String CANNON_ATLAS = "Object/cannon_atlas.png";
    public static final String CANNON_ATTACK_EFFECT = "Object/cannon/cannon-fire-effect(20x28).png";
    public static final String BALL_EXPLODE = "Object/cannon/cannon-ball-explosion(54x60).png";
    public static final String BALL_IMG = "Object/ball.png";
    public static final String DEATH_SCREEN = "Menu/death_screen.png";
    public static final String OPTIONS_MENU = "Menu/options_background.png";
    public static final String RAIN_PARTICLE = "Level/Obj&BackGround/rain_particle.png";
    //    public static final String BEHAVIOR_OPENING ="Object/4-Interrogation Dialog/1-Opening";
    public static final String BEHAVIOR_CLOSING = "Object/4-Interrogation Dialog/2-Closing/1.png";
    public static final String PALM_TREE = "treasure-hunters/palm-tree-island/back-palm-tree-left(51x53).png";
    public static final String BOTTOM_TREE = "Object/front-bottom-tree(32x32).png";
    public static final String TOP_TREE = "Object/front_palm_tree(39x32).png";

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

    // doc folder
    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/Level/Lvls");//dan vao folder can doc
        File file = null;
        try {
            file = new File(url.toURI());//lay toan bo file trong folder
            File[] files = file.listFiles();//cat thanh cac file roi bo vao array
            int length = files.length;//do dai cua danh sach files
            File[] filesSorted = new File[length];//tao array chua cac file sau khi da sap xep
            for (int i = 0; i < filesSorted.length; i++)
                for (int j = 0; j < files.length; j++)
                    if (files[j].getName().equals((i + 1) + ".png"))
                        filesSorted[j] = files[i];//xep cac file vao array sau khi da sap xep theo thu tu
            BufferedImage[] imgs = new BufferedImage[filesSorted.length];//tao danh sach cac hinh anh tu file
            for (int i = 0; i < imgs.length; i++)
                imgs[i] = ImageIO.read(filesSorted[i]);// doc cac file trong danh sach va chuyen tu file thanh img
            return imgs;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}



