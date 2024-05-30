package main;


import entities.Player;
import levels.LevelManager;

import java.awt.*;

// chứa các class cần đc khởi tạo từ ban đầu
public class Game implements Runnable {
    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private LevelManager levelManager;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private  final int UPS_SET = 200;
    private static final int DEFAULT_TILE_SIZE = 32;
    public static final float SCALE = 1f;
    public static int TILE_SIZE = (int) (SCALE * DEFAULT_TILE_SIZE);
    public final static int MAX_COL = 26;
    public final static int MAX_ROW = 14;
    public static int SCREEN_WIDTH = MAX_COL * TILE_SIZE; // 832
    public static int SCREEN_HEIGHT = MAX_ROW * TILE_SIZE; // 448
    private Player player;
    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        levelManager = new LevelManager(this);

        player = new Player(200,200,Player.imgWidth,Player.imgHeight);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void update(){
        player.update();
        levelManager.update();
    }
    public void render(Graphics g){
        levelManager.draw(g);
        player.render(g);
    }
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();
        int updates = 0;
        int frames = 0;
        long lastCheck = System.nanoTime();
        double deltaU = 0;
        double deltaF = 0;
        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime ) / timePerUpdate;
            deltaF += (currentTime - previousTime ) / timePerFrame;
            previousTime = currentTime;
            if(deltaU >= 1){
                update();
                updates++;
                deltaU--;
            }
            if(deltaF >= 1){
                gamePanel.repaint();
                frames ++;
                deltaF --;
            }
            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
//                System.out.println("FPS: "+frames +"||"+"UPS: "+updates);
                frames = 0;
                updates = 0;            }
        }
    }
    public Player getPlayer(){
        return player;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }
}
