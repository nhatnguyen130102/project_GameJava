package main;

import gameStates.GameState;
import gameStates.Menu;
import gameStates.Playing;
import ultilz.LoadSave;

import java.awt.*;

// chứa các class cần đc khởi tạo từ ban đầu
public class Game implements Runnable {
    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private Playing playing;
    private Menu menu;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private static final int DEFAULT_TILE_SIZE = 64;
    public static final float SCALE = 1f;
    public static int TILE_SIZE = (int) (SCALE * DEFAULT_TILE_SIZE);
    public final static int MAX_COL = 26;
    public final static int MAX_ROW = 14;
    public static int SCREEN_WIDTH = MAX_COL * TILE_SIZE; // 832
    public static int SCREEN_HEIGHT = MAX_ROW * TILE_SIZE; // 448

    public Game() {
        initClasses();
        LoadSave.GetAllLevels();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.state) {
            case MENU:
                menu.update();
                break;
            case OPTIONS:

            case PLAYING:
                playing.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
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
//            System.out.println(previousTime+","+currentTime+","+ (currentTime - previousTime));
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }
            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
//                System.out.println("FPS: "+frames +"||"+"UPS: "+updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetDirBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
}
