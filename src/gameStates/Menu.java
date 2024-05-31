package gameStates;

import main.Game;
import ui.MenuButton;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Menu extends State implements StateMethods {
    private MenuButton[] buttons = new MenuButton[3];

    public Menu(Game game) {
        super(game);
        loadButtons();
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.SCREEN_WIDTH / 2, (int) (150 * Game.SCALE), 0, GameState.PLAYING);
        buttons[1] = new MenuButton(Game.SCREEN_WIDTH / 2, (int) (220 * Game.SCALE), 0, GameState.OPTIONS);
        buttons[2] = new MenuButton(Game.SCREEN_WIDTH / 2, (int) (290 * Game.SCALE), 0, GameState.QUIT);

    }

    @Override
    public void update() {
        for(MenuButton mb : buttons){
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        for(MenuButton mb : buttons){
            mb.draw(g);
        }
//        g.setColor(Color.black);
//        g.drawString("Menu", Game.SCREEN_WIDTH / 2, 200);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(MenuButton mb : buttons){
            if(isIn(e,mb)){
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for(MenuButton mb : buttons){
            if(isIn(e,mb)){
                if(mb.isMousePressed()){
                    mb.applyGamesLate();
                    break;
                }
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for(MenuButton mb : buttons){
            mb.resetBoots();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons){
            mb.setMouseOver(false);
        }
        for(MenuButton mb : buttons){
            if(isIn(e,mb)){
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void test() {

    }
}
