package main;

import audio.AudioPlayer;
import gameStates.GameOptions;
import gameStates.GameState;
import gameStates.Menu;
import gameStates.Playing;
import ui.AudioOptions;
import ultilz.LoadSave;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

// chứa các class cần đc khởi tạo từ ban đầu
public class Game implements Runnable {
    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;
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
    private Timer explodeTimer;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Game() {
        initClasses();
        LoadSave.GetAllLevels();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
//        startClient("localhost"); // Khởi tạo kết nối máy khách
    }
    private void startClient(String serverAddress) {
        try {
            clientSocket = new Socket(serverAddress, 12345);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            new Thread(new IncomingReader()).start();
            sendMessage("Hello, Server!");

            // Ví dụ gửi dữ liệu từ game
            // sendGameData(gameData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class IncomingReader implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Server: " + message);
                    // Xử lý dữ liệu nhận được từ server
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void initClasses() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions = new GameOptions(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

//    public void update() {
//        switch (GameState.state) {
//            case MENU:
//                menu.update();
//                break;
//            case OPTIONS:
//
//            case PLAYING:
//                playing.update();
//                break;
//            case QUIT:
//            default:
//                System.exit(0);
//                break;
//        }
//    }
//
//    public void render(Graphics g) {
//        switch (GameState.state) {
//            case MENU:
//                menu.draw(g);
//                break;
//            case PLAYING:
//                playing.draw(g);
//                break;
//            default:
//                break;
//        }
//    }
public void update() {
    switch (GameState.state) {
        case MENU:
            menu.update();
            break;
        case PLAYING:
            playing.update();
            break;
        case OPTIONS:
            gameOptions.update();
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
            case OPTIONS:
                gameOptions.draw(g);
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
    public GameOptions getGameOptions() {
        return gameOptions;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
