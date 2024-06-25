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

    public static final String LEVEL_ATLAS = "Map/outside_sprites.png";
    public static final String MENU_BACKGROUND = "Menu/menu_background.png";
    public static final String BACKGROUND = "Menu/beautiful-anime-landscape-cartoon-scene.jpg";
    public static final String LEVEL_ONE_DATA = "Level/level_one_data_long.png";
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
    public static final String COMPLETED_IMG = "Menu/completed_sprite.png";
    public static final String WHALE_SPRITE = "Enemy/whale_sprite.png";
    public static final String BOMB_SPRITE = "Object/bomb(96x108).png";
    public static final String POTION_ATLAS = "Object/potions_sprites.png";
    public static final String CONTAINER_ATLAS = "Object/objects_sprites.png";
    public static final String CANNON_ATLAS = "Object/cannon_atlas.png";
    public static final String BALL_IMG = "Object/ball.png";
    public static final String DEATH_SCREEN = "Menu/death_screen.png";
    public static final String OPTIONS_MENU = "Menu/options_background.png";
    public static final String RAIN_PARTICLE = "Level/Obj&BackGround/rain_particle.png";


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

//    public static BufferedImage[][] GetAllEntities(String path) {
//        File[] allFolderInPath = getAllFileOrFolderInFolder(path);// lay toan bo cac folder action cua whale
//        File[] sortFolder = new File[allFolderInPath.length];
//        int index = 0;
//        // sap xep cac folder theo thu tu tu be den lon
//        for (int i = 0; i < sortFolder.length; i++) {
//            for (int j = 0; j < allFolderInPath.length; j++) {
//                String[] sliptName = allFolderInPath[j].getName().split("-");// lay phan so trong ten cua folder action
//                int formatName = Integer.parseInt(sliptName[0]) - 1;// chuyen phan so do thanh int
//                if (formatName == i)// kiem tra neu phan so do = so thu tu hien tai thi thuc hien gan folder do vao dung vi tri trong array sortFolder
//                    sortFolder[i] = allFolderInPath[j];
//            }
//        }
//        ArrayList<Integer> array = new ArrayList<>();
//        for (File f : sortFolder) {
//            File[] allFileInPath = getAllFileOrFolderInFolder(path + "/" + f.getName());// lay toan bo cac file img trong tung folder action cua whale
//            array.add(allFileInPath.length);
//        }
//        int Max = Collections.max(array);
//        BufferedImage[][] imgs = new BufferedImage[allFolderInPath.length][Max];
//
//        for (File f : sortFolder) {
//            File[] allFileInPath = getAllFileOrFolderInFolder(path + "/" + f.getName());// lay toan bo cac file img trong tung folder action cua whale
//
//            File[] sortFile = new File[allFileInPath.length];
//            for (int i = 0; i < sortFile.length; i++) {
//                for (int j = 0; j < allFileInPath.length; j++) {
//                    String[] sliptName = allFileInPath[j].getName().split("\\.");
//
//                    int formatName = Integer.parseInt(sliptName[0]) - 1;
//                    if (formatName == i){
//                        sortFile[i] = allFileInPath[j];
//                    }
//                }
//            }
//            array.add(sortFile.length);
//            for (int i = 0; i < sortFile.length; i++) {
//                try {
//                    imgs[index][i] = ImageIO.read(sortFile[i]);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        return imgs;
//    }
//
//    public static File[] getAllFileOrFolderInFolder(String path) {
//        URL url = LoadSave.class.getResource(path);//dan vao folder can doc
//        File file = null;
//        try {
//            file = new File(url.toURI());//lay toan bo file trong folder
//            File[] files = file.listFiles();//cat thanh cac file roi bo vao array
//            return files;
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static int countActionFile(String path) {
//        URL url = LoadSave.class.getResource(path);//dan vao folder can doc
//        File file = null;
//        try {
//            file = new File(url.toURI());//lay toan bo file trong folder
//            File[] files = file.listFiles();//cat thanh cac file roi bo vao array
//            return files.length;
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static int getMaxFileInActionFolder(String path) {
//        File[] allFolderInPath = getAllFileOrFolderInFolder(path);// lay toan bo cac folder action cua whale
//
//        ArrayList<Integer> array = new ArrayList<>();
//        for (File f : allFolderInPath) {
//            File[] allFileInPath = getAllFileOrFolderInFolder(path + "/" + f.getName());// lay toan bo cac file img trong tung folder action cua whale
//            array.add(allFileInPath.length);
//        }
//        int Max = Collections.max(array);
//        return Max;
//    }
}



