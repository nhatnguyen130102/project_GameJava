package entities;

import ultilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ultilz.Constants.PlayerConstants.*;
import static ultilz.Constants.PlayerConstants.PLAYER_DRAW_OFFSET_Y;

public class PlayerClone extends Entity {
    private int playerAction = IDLE;
    private int framesIndex;

    private String name;
    private int framesRow, framesCol;
    BufferedImage[][] frames;

    public PlayerClone(float x, float y, int width, int height, String name) {
        super(x, y, width, height);
        this.name = name;
        initHitBox(x, y, HB_PLAYER_WIDTH, HB_PLAYER_HEIGHT);
        loadAnimations();
    }

    private void loadAnimations() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        framesRow = image.getHeight() / PLAYER_HEIGHT_DEFAULT;
        framesCol = image.getWidth() / PLAYER_WIDTH_DEFAULT;
        frames = new BufferedImage[framesRow][framesCol];
        for (int j = 0; j < framesRow; j++)
            for (int i = 0; i < framesCol; i++)
                frames[j][i] = image.getSubimage(i * PLAYER_WIDTH_DEFAULT, j * PLAYER_HEIGHT_DEFAULT, PLAYER_WIDTH_DEFAULT, PLAYER_HEIGHT_DEFAULT);
//        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
        g.drawImage(frames[0][0],
                (int) hitBox.x,
                (int) hitBox.y,
                PLAYER_WIDTH,
                PLAYER_HEIGHT,
                null);
//        drawHitBox(g, xLvlOffset, yLvlOffset);
//        drawAttackBox(g, xLvlOffset, yLvlOffset);
//        if(name != null)
//            g.drawString(name, (int) (hitBox.x - PLAYER_DRAW_OFFSET_X) - xLvlOffset + flipX,   (int) (hitBox.y - PLAYER_DRAW_OFFSET_Y) - yLvlOffset);
//        drawUI(g);
    }

    public void setPos(int x, int y) {
        hitBox.x = x;
        hitBox.y = y;
    }
}
