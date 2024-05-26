package Main;

import Inputs.KeyboardInputs;
import Inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static Ultilz.Constants.Directions.*;
import static Ultilz.Constants.PlayerConstants.*;
import static Ultilz.Constants.PlayerConstants.getSpriteAmout;

// khởi tạo các nội dung của game
public class GamePanel extends JPanel{
    private  int xDelta = 100;
    private  int yDelta = 200;
    public int speed = 5;
    private final int originalTileSize = 16;
    private final int scale = 4;
    public int tileSize = scale * originalTileSize;

    private final int maxCol = 20;
    private final int maxRow = 10;
    public  int screenWidth = maxCol * tileSize;
    public  int screenHeight = maxRow * tileSize;
    public int imgWidth;
    public int imgHeight;
    private BufferedImage image;
    private BufferedImage[][] frames;
    private int framesRow, framesCol;
    private int framesTick;
    private int framesIndex;
    private final int framesSpeed = 15 ;
    private  int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false;
    public GamePanel() {
        setPanelSize();
        importImage();
        loadAnimations();
        MouseInputs mouseInputs = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateFramesTick();
        setAnimation();
        updatePos();
        int scale = 2;
        g.drawImage(frames[playerAction][framesIndex],xDelta,yDelta, imgWidth * scale,imgHeight * scale,null);
    }
    private void loadAnimations() {
        framesRow = 9;
        framesCol = 6;
        imgWidth = 64;
        imgHeight = 40;
        frames = new BufferedImage[framesRow][framesCol];
        for(int j = 0; j < framesRow; j++){
            for(int i = 0; i < framesCol; i++){
                frames[j][i] = image.getSubimage(i*imgWidth,j*imgHeight,imgWidth,imgHeight);
            }
        }

    }
    private void importImage() {
        InputStream is = getClass().getResourceAsStream("/Player/player_sprites.png");
        try {
            if (is != null) {
                image = ImageIO.read(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void setPanelSize(){
        Dimension size = new Dimension(screenWidth,screenHeight);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }
    public void setDirection(int direction){
        this.playerDir = direction;
        moving = true;
    }
    public void setMoving(boolean moving){
        this.moving = moving;
    }
    private void updatePos() {
        if(moving){
            switch (playerDir){
                case LEFT:
                    xDelta -= speed;
                    break;
                case UP:
                    yDelta -= speed;
                    break;
                case RIGHT:
                    xDelta += speed;
                    break;
                case DOWN:
                    yDelta += speed;
                    break;
            }

        }
    }
    private void setAnimation() {
        if(moving){
            playerAction = RUNNING;
        }
        else{
            playerAction = IDLE;
        }
    }
    private void updateFramesTick() {
        framesTick++;
        if(framesTick >= framesSpeed){
            framesTick = 0;
            framesIndex++;
            if(framesIndex >= getSpriteAmout(playerAction)){
                framesIndex = 0;
            }
        }
    }

}
