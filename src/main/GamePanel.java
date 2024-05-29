package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;


// khởi tạo các nội dung của game
public class GamePanel extends JPanel {
    private final Game game;
    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();
        MouseInputs mouseInputs = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }
    private void setPanelSize() {
        Dimension size = new Dimension(game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }
    public void updateGame() {

    }
    public Game getGame(){
        return game;
    }
}
