package entities;

import main.Game;

import java.awt.*;

import static ultilz.Constants.Directions.*;
import static ultilz.Constants.EnemyConstants.*;
import static ultilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    private int frameIndex, enemyState, enemyTpye;
    private int frameTick, frameSpeed = 25;
    private boolean firstUpdate = true;
    private boolean inAir;
    private float fallSpeed;
    private float gravity = 0.04f * Game.SCALE;
    private float walkSpeed = 0.5f * Game.SCALE;
    private int walkDir = RIGHT;

    public Enemy(float x, float y, int width, int height, int enemyTpye) {
        super(x, y, width, height);
        this.enemyTpye = enemyTpye;
        initHitBox(x, y, width, height);
    }

    private void uploadFrameTick() {
        frameTick++;
        if (frameTick >= frameSpeed) {
            frameTick = 0;
            frameIndex++;
            if (frameIndex >= GetSpriteAmount(enemyTpye, enemyState)) {
                frameIndex = 0;
            }
        }
    }

    public void update(int[][] lvlData) {
        uploadFrameTick();
        updateMove(lvlData);
    }

    public void updateMove(int[][] lvlData) {
        if (firstUpdate) {
            if (!IsEntityOnFloor(hitBox, lvlData))
                inAir = true;
            firstUpdate = false;
        }
        if (inAir) {
            if (CanMoveHere(hitBox.x, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
            }
        } else {
            switch (enemyState) {
                case IDLE -> enemyState = RUNNING;
                case RUNNING -> {
                    float XSpeed = 0;
                    if (walkDir == LEFT)
                        XSpeed = -walkSpeed;
                    else
                        XSpeed = +walkSpeed;
                    if (CanMoveHere(hitBox.x + XSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
                        if (IsFloor(hitBox, XSpeed, lvlData)) {
                            hitBox.x += XSpeed;
                            return;
                        }
                    }
                    changeWalkDir();
                }
            }
        }
    }

    private void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }
}
