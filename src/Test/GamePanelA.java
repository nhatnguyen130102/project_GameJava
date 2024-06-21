package Test;

import javax.swing.*;
import java.awt.*;

public class GamePanelA   extends JPanel {
    private Item item;

    public GamePanelA(Item item) {
        this.item = item;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        int itemSize = 10;
        g.fillOval((int)item.getX() - itemSize / 2, (int)item.getY() - itemSize / 2, itemSize, itemSize);
    }
}
