package Test;

import Test.Item;

import javax.swing.*;
import java.awt.*;

public class GameA {
    private Item item;
    private GamePanelA gamePanel;
    private long lastTime;

    public GameA() {
        item = new Item(200, 200, 100, Math.PI / 1); // Tạo item tại tâm (200, 200) với bán kính 100 và tốc độ góc π/4 radian mỗi giây
        gamePanel = new GamePanelA(item);
        lastTime = System.nanoTime();
    }

    public void start() {
        JFrame frame = new JFrame("Item Moving in Semi-Circle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(gamePanel);
        frame.setVisible(true);

        Timer timer = new Timer(16, e -> gameLoop());
        timer.start();
    }

    private void gameLoop() {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastTime) / 1_000_000_000.0; // Chuyển đổi từ nano giây sang giây
        lastTime = currentTime;

        item.update(deltaTime);
        gamePanel.repaint();
    }

    public static void main(String[] args) {
        GameA game = new GameA();
        game.start();
    }
}
