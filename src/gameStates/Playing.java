package gameStates;

import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverLay;
import ultilz.LoadSave;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static ultilz.Constants.Enviroment.*;

public class Playing extends State implements StateMethods {
    private LevelManager levelManager;
    private Player player;
    private boolean paused = false;
    private PauseOverLay pauseOverLay;
    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.SCREEN_WIDTH);// khoảng cách với tường trái 20%
    private int rightBorder = (int) (0.8 * Game.SCREEN_WIDTH);// khoảng cách với tường phải 80%
    private int lvlTilesWide = LoadSave.getLevelData()[0].length;// Độ dài tối đa của map hiện tại ( toạ độ )
    private int maxTilesOffset = lvlTilesWide - Game.MAX_COL;// Độ dài tối đa màn hình chưa hiển thị ( map tối đa - màn hình tối đa = phần thừa tối đa )
    private int maxLvlOffsetX = maxTilesOffset * Game.TILE_SIZE;// Độ dài ( toạ độ x tilesize ) của phần thừa
    private BufferedImage backGroundImg, bigCloudImg, smallCloudImg;
    private int[] smallCloudPos;
    private Random rnd = new Random();

    public Playing(Game game) {
        super(game);
        initClasses();
        loadBackGroundImg();
        loadBigCloud();
        loadSmallCloud();
        smallCloudPos = new int[8];
        for (int i = 0; i < smallCloudPos.length; i++) {
            smallCloudPos[i] = (int) (100 * Game.SCALE) // Độ thấp nhất của mây nhỏ
                    + rnd.nextInt((int) (100 * Game.SCALE)); // Độ nhấp cộng thêm ngẫu nhiên
        }
    }

    private void loadSmallCloud() {
        smallCloudImg = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
    }

    private void loadBigCloud() {
        bigCloudImg = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
    }

    private void loadBackGroundImg() {
        backGroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
    }

    private void initClasses() {
//        levelManager = new LevelManager(game);
        levelManager = new LevelManager();
        player = new Player(100, 300, Player.imgWidth, Player.imgHeight);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        pauseOverLay = new PauseOverLay(this);
    }


    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
            CheckCloseToBorder();
        } else
            pauseOverLay.update();
    }

    private void CheckCloseToBorder() {
        int playerX = (int) player.getHitBox().x;// tạo 1 biến lấy vị trí hiện tại của nhân vật
        int diff = playerX - xLvlOffset;
        if (diff > rightBorder)// kiểm tra player đã đi qua 80% màn hình chưa
            xLvlOffset += diff - rightBorder; // tính độ chênh lệch giữa player với màn hình khi player vượt quá 80% màn hình
        else if (diff < leftBorder) // kiểm tra player đã lùi quá 20% màn hình chưa
            xLvlOffset += diff - leftBorder;// tính độ chênh lệch giữa player với màn hình khi player lùi quá 20% màn hình

        if (xLvlOffset > maxLvlOffsetX)// khi player đi đến hết phần thừa, thì phần chênh lệch = phần thừa
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0) // khi player đi đến đầu phần rìa, thì phần chênh lệch = 0
            xLvlOffset = 0;


    }

    @Override
    public void draw(Graphics g) {
//        g.drawImage(backGroundImg, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
//        drawClouds(g);
        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
            pauseOverLay.draw(g);
        }
    }

    private void drawClouds(Graphics g) {
        int getQtyCloud = (int) ((LoadSave.getCol() * Game.TILE_SIZE) / (LoadSave.getCol() * Game.TILE_SIZE) + 2);
        double bigCloudSpeed = xLvlOffset * 0.3;// biến số càng lớn thì tốc độ di chuyển càng cao khi player moving
        double smallCloudSpeed = xLvlOffset * 0.7;// biến số càng lớn thì tốc độ di chuyển càng cao khi player moving
        for (int i = 0; i < getQtyCloud; i++)
            g.drawImage(bigCloudImg, (int) (i * BIG_CLOUD_WIDTH - bigCloudSpeed), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

        int smallCloudLoop = 3;// khoảng cách giữa các đám mấy nhỏ, càng lớn thì số lượng mây hiển thị càng thưa(ít)
        for (int i = 0; i < smallCloudPos.length; i++)
            g.drawImage(smallCloudImg, (int) (SMALL_CLOUD_WIDTH * smallCloudLoop * i - smallCloudSpeed), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            player.setAttacking(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused)
            pauseOverLay.mousePressed(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused)
            pauseOverLay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused)
            pauseOverLay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_D -> player.setRight(true);
            case KeyEvent.VK_SPACE -> player.setJump(true);
            case KeyEvent.VK_BACK_SPACE -> GameState.state = GameState.MENU;
            case KeyEvent.VK_ESCAPE -> paused = !paused;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(false);
            case KeyEvent.VK_D -> player.setRight(false);
            case KeyEvent.VK_SPACE -> player.setJump(false);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (paused)
            pauseOverLay.mouseDragged(e);
    }

    @Override
    public void test() {

    }

    public Player getPlayer() {
        return player;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public void unpauseGame() {
        paused = false;
    }
}
