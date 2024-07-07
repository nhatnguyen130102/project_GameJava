package entities;

import gameStates.Playing;
import main.Game;
import ultilz.LoadSave;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static ultilz.Constants.EnemyConstants.CRABBY_HEIGHT_DEFAULT;
import static ultilz.Constants.EnemyConstants.CRABBY_WIDTH_DEFAULT;
import static ultilz.Constants.PlayerConstants.*;
import static ultilz.LoadSave.CRABBY_SPRITE;
import static ultilz.LoadSave.GetSpriteAtlas;

public class PlayerManager {
    private static Playing playing;
    private static BufferedImage[][] pirateArr;
    private static BufferedImage[][] bombGuyArr;
    private static ArrayList<Pirate> pirates = new ArrayList<>();
    private static ArrayList<BombGuy> bombGuys = new ArrayList<>();
    //
    private int framesRow, framesCol;
    private int framesTick;
    private int framesIndex;
    private final int framesSpeed = 10;
    private int playerAction = IDLE;
    public static int playerDir = 1;
    private boolean moving = false;
    private boolean attacking = false;
    public float speed = 2 * Game.SCALE;
    private boolean up, left, down, jump, right;
    private boolean firstUpdate = true;
    private int[][] lvlData;

    // Jumping Gravity
    private float airSpeed = 0f; // van toc roi cua vat the v0 -> vN
    private final float gravity = 0.04f * Game.SCALE; // luc hap dan______gia toc trong truong
    private final float jumpSpeed = -3.25f * Game.SCALE; // do cao khi nhay cua vat the
    private final float fallSpeedAfterCollision = 1f * Game.SCALE; // toc do roi khi cham phai tile o phia tren
    boolean inAir = false; // trang thai cua vat the
    private BufferedImage statusBarImg;
    private final int statusBarWidth = (int) (192 * Game.SCALE);
    private final int statusBarHeight = (int) (58 * Game.SCALE);
    private final int statusBarX = (int) (10 * Game.SCALE);
    private final int statusBarY = (int) (10 * Game.SCALE);
    private final int healthBarWidth = (int) (150 * Game.SCALE);
    private final int healthBarHeight = (int) (4 * Game.SCALE);
    private final int healthBarXStart = (int) (34 * Game.SCALE);
    private final int healthBarYStart = (int) (14 * Game.SCALE);
    private final int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;
    private Rectangle2D.Float attackBox;
    private int flipX = 0;
    private int flipW = 1;
    private boolean attackChecked;
    private ArrayList<Bomb> bombs = new ArrayList<>();

    public PlayerManager(Playing playing) {
        this.playing = playing;
        loadPlayerImg();
    }

    private void loadPlayerImg() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        framesRow = image.getHeight() / PLAYER_HEIGHT_DEFAULT;
        framesCol = image.getWidth() / PLAYER_WIDTH_DEFAULT;
        pirateArr = new BufferedImage[framesRow][framesCol];
        for (int j = 0; j < framesRow; j++)
            for (int i = 0; i < framesCol; i++)
                pirateArr[j][i] = image.getSubimage(i * PLAYER_WIDTH_DEFAULT, j * PLAYER_HEIGHT_DEFAULT, PLAYER_WIDTH_DEFAULT, PLAYER_HEIGHT_DEFAULT);
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }
}
