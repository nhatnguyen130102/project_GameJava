package gameStates;


import audio.AudioPlayer;
import effects.Rain;
import entities.Bomb;
import entities.EnemyManager;
import entities.Player;
import entities.Whale;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompleteOverlay;
import ui.PauseOverLay;
import ultilz.Constants;
import ultilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static ultilz.Constants.Enviroment.*;
import static ultilz.Constants.PlayerConstants.*;

public class Playing extends State implements StateMethods {
    private LevelManager levelManager;
    private Player player;
    private Bomb bomb;
    private ArrayList<Bomb> bombs;
    private EnemyManager enemyManager;
    private PauseOverLay pauseOverLay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompleteOverlay levelCompleteOverlay;
    private ObjectManager objectManager;
    private boolean paused = false;
    private boolean playerDying = false;
    private boolean drawRain;
    private Rain rain;

    private int xLvlOffset; // phẩn thừa khi player di chuyển để mức quy định
    private int yLvlOffset;
    private int leftBorder = (int) (0.2 * Game.SCREEN_WIDTH);// khoảng cách với tường trái 20%
    private int rightBorder = (int) (0.8 * Game.SCREEN_WIDTH);// khoảng cách với tường trái 80%
    private int topBorder = (int) (0.2 * Game.SCREEN_HEIGHT);
    private int bottomBorder = (int) (0.84 * Game.SCREEN_HEIGHT);
    private int maxLvlOffsetX;// Độ dài ( tilesize ) của phần thừa
    private int maxLvlOffsetY;// Độ rong ( tilesize ) của phần thừa

    private BufferedImage backGroundImg, bigCloudImg, smallCloudImg;
    private int[] smallCloudPos;
    private Random rnd = new Random();
    private boolean gameOver;
    private boolean lvlCompleted = false;
    private long startTime;
    private float jumpSpeed;
    private float bombSpeed;


    public Playing(Game game) {
        super(game);
        initClasses();
        loadBackGroundImg();
        loadBigCloud();
        loadSmallCloud();
        initEnviroments();
        setDrawRainBoolean();
        setGameState(GameState.MENU);

    }

    private void setDrawRainBoolean() {
//         This method makes it rain 20% of the time you load a level.
        if (rnd.nextFloat() >= 0.8f)
            drawRain = true;
    }

    private void initEnviroments() {
        smallCloudPos = new int[8];
        for (int i = 0; i < smallCloudPos.length; i++) {
            smallCloudPos[i] = (int) (100 * Game.SCALE) // Độ thấp nhất của mây nhỏ
                    + rnd.nextInt((int) (100 * Game.SCALE)); // Độ nhấp cộng thêm ngẫu nhiên
        }
        loadStartLevel();
        calcLvlOffset();

    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        objectManager.loadObject(levelManager.getCurrentLevel());
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffsetX(); // lay phan thua theo tilesize
        maxLvlOffsetY = levelManager.getCurrentLevel().getLvlOffsetY();
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
        levelManager = new LevelManager(game); // khởi tạo map môi trường
        objectManager = new ObjectManager(this);
        enemyManager = new EnemyManager(this);// khởi tạo quái vật
        player = new Player(100, 300, PLAYER_WIDTH, PLAYER_HEIGHT, this);// khởi tạo character
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());// khởi tạo map 1
        pauseOverLay = new PauseOverLay(this);// khởi tạo pausescreen
        gameOverOverlay = new GameOverOverlay(this);
        levelCompleteOverlay = new LevelCompleteOverlay(this);
        rain = new Rain();

    }

    @Override
    public void update() {
        if (paused) {
            pauseOverLay.update();
        } else if (lvlCompleted) {
            levelCompleteOverlay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else {
            if (drawRain)
                rain.update(xLvlOffset);
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            bombs = player.getBombs();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player,bombs);
            CheckCloseToBorderX();
            CheckCloseToBorderY();
        }
    }


    private void CheckCloseToBorderX() {
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

    private void CheckCloseToBorderY() {
        int playerY = (int) player.getHitBox().y;// tạo 1 biến lấy vị trí hiện tại của nhân vật
        int diff = playerY - yLvlOffset;
        if (diff > bottomBorder)// kiểm tra player đã đi qua 80% màn hình chưa
            yLvlOffset += diff - bottomBorder; // tính độ chênh lệch giữa player với màn hình khi player vượt quá 80% màn hình
        else if (diff < topBorder) // kiểm tra player đã lùi quá 20% màn hình chưa
            yLvlOffset += diff - topBorder;// tính độ chênh lệch giữa player với màn hình khi player lùi quá 20% màn hình

        if (yLvlOffset > maxLvlOffsetY)// khi player đi đến hết phần thừa, thì phần chênh lệch = phần thừa
            yLvlOffset = maxLvlOffsetY;
        else if (yLvlOffset < 0) // khi player đi đến đầu phần rìa, thì phần chênh lệch = 0
            yLvlOffset = 0;

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backGroundImg, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
        drawClouds(g);
        levelManager.draw(g, xLvlOffset, yLvlOffset);
        enemyManager.draw(g, xLvlOffset, yLvlOffset);
        player.draw(g, xLvlOffset, yLvlOffset);
        objectManager.draw(g, xLvlOffset, yLvlOffset);
        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
            pauseOverLay.draw(g);
        } else if (gameOver)
            gameOverOverlay.draw(g);
        else if (lvlCompleted)
            levelCompleteOverlay.draw(g);
        if (drawRain)
            rain.draw(g, xLvlOffset, yLvlOffset);

    }

    private void drawClouds(Graphics g) {
        double bigCloudSpeed = xLvlOffset * 0.3;// biến số càng lớn thì tốc độ di chuyển càng cao khi player moving
        double smallCloudSpeed = xLvlOffset * 0.7;// biến số càng lớn thì tốc độ di chuyển càng cao khi player moving
        for (int i = 0; i < 5; i++)
            g.drawImage(bigCloudImg, (int) (i * BIG_CLOUD_WIDTH - bigCloudSpeed), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        int smallCloudLoop = 3;// khoảng cách giữa các đám mấy nhỏ, càng lớn thì số lượng mây hiển thị càng thưa(ít)
        for (int i = 0; i < smallCloudPos.length; i++)
            g.drawImage(smallCloudImg, (int) (SMALL_CLOUD_WIDTH * smallCloudLoop * i - smallCloudSpeed), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        drawRain = false;

        setDrawRainBoolean();

        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObject();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        if (gameOver) getGame().getAudioPlayer().gameOver();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkEmenyExplode(Rectangle2D.Float explodeBox) {
        enemyManager.checkEnemyExplode(explodeBox);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkObjectExplode(Rectangle2D.Float explodeBox) {
        objectManager.checkObjectExplode(explodeBox);
    }

    public void checkPlayerExplode(Rectangle2D.Float explodeBox) {
        player.checkPlayerExplode(explodeBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitBox) {
        objectManager.checkObjectTouched(hitBox);
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
        if (levelCompleted) game.getAudioPlayer().lvlCompleted();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver)
            if (!paused && !lvlCompleted) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    player.setKicking(true);
                }
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverLay.mousePressed(e);
            } else if (lvlCompleted) {
                levelCompleteOverlay.mousePressed(e);
            } else if (!paused && !lvlCompleted) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    startTime = System.currentTimeMillis();
                    jumpSpeed = 0;
                    bombSpeed = 0;
                }
            }
        } else gameOverOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverLay.mouseReleased(e);
            else if (lvlCompleted)
                levelCompleteOverlay.mouseReleased(e);
            else if (!paused && !lvlCompleted) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    jumpSpeed -= elapsedTime / 100f;
                    bombSpeed += elapsedTime / 1000f;
                    if (jumpSpeed <= -2) {
                        jumpSpeed = -2;
                    }
                    if (bombSpeed >= 1.5) {
                        bombSpeed = 1.5f;
                    }
                    player.setAttacking(true);
                    player.createBomb(jumpSpeed, bombSpeed);

                }
            }
        } else gameOverOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverLay.mouseMoved(e);
            else if (lvlCompleted)
                levelCompleteOverlay.mouseMoved(e);
        } else gameOverOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else if (!player.getIsExplode())
            switch (e.getKeyCode()) {
                case KeyEvent.VK_J -> {

                }
                case KeyEvent.VK_A -> player.setLeft(true);
                case KeyEvent.VK_D -> player.setRight(true);
                case KeyEvent.VK_SPACE -> player.setJump(true);
            }
        if (!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_BACK_SPACE -> GameState.state = GameState.MENU;
                case KeyEvent.VK_ESCAPE -> paused = !paused;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver && !player.getIsExplode())
            switch (e.getKeyCode()) {
                case KeyEvent.VK_J -> {

                }
                case KeyEvent.VK_A -> player.setLeft(false);
                case KeyEvent.VK_D -> player.setRight(false);
                case KeyEvent.VK_SPACE -> player.setJump(false);
            }
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused)
                pauseOverLay.mouseDragged(e);
    }

    @Override
    public void test() {

    }

    public Player getPlayer() {
        return player;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public void unpauseGame() {
        paused = false;
    }

    public void setMaxLvlOffsetX(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void setMaxLvlOffsetY(int lvlOffset) {
        this.maxLvlOffsetY = lvlOffset;
    }


    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
        if (playerDying) getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
    }
}
