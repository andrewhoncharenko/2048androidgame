package com.game.ah2048.sprites;

import android.graphics.Canvas;

import com.game.ah2048.TileManagerCallback;

import java.util.Random;

public class Tile implements Sprite {

    private int screenWidth, screenHeight, standartSize;
    private TileManagerCallback callback;
    private int count = 1;
    private int currentX, currentY;
    private int destX, destY;
    private boolean moving = false;
    private int speed = 10;
    private boolean increment;

    public Tile(int standartSize, int screenWidth, int screenHeight, TileManagerCallback callback, int matrixX, int matrixY) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.standartSize = standartSize;
        this.callback = callback;
        currentX = destX = screenWidth / 2 - 2 * standartSize + matrixY * standartSize;
        currentY = destY = screenHeight / 2 - 2 * standartSize + matrixX * standartSize;
        int chance = new Random().nextInt(100);
        if(chance >= 90) {
            count = 2;
        }
    }

    public void move(int matrixX, int matrixY) {
        moving = true;
        destX = screenWidth / 2 - 2 * standartSize + matrixY * standartSize;
        destY = screenHeight / 2 - 2 * standartSize + matrixX * standartSize;
    }

    public int getValue() {
        return count;
    }

    public Tile increment() {
        increment = true;
        return this;
    }

    public boolean toIncrement() {
        return increment;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(callback.getBitmap(count), currentX, currentY, null);
        if(moving && currentX == destX && currentY == destY) {
            moving = false;
            if(increment) {
                count++;
                increment = false;
                int amount = (int) Math.pow(2, count);
                callback.updateScore(amount);
                if(count == 11) {
                    callback.reached2048();
                }
            }
            callback.finishMoving(this);
        }
    }

    @Override
    public void update() {
        if(currentX < destX) {
            if(currentX + speed > destX) {
                currentX = destX;
            }
            else {
                currentX += speed;
            }
        }
        else if (currentX > destX) {
            if(currentX - speed < destX) {
                currentX = destX;
            }
            else {
                currentX -= speed;
            }
        }
        if(currentY < destY) {
            if(currentY + speed > destY) {
                currentY = destY;
            }
            else {
                currentY += speed;
            }
        } else if (currentY > destY) {
            if(currentY - speed < destY) {
                currentY = destY;
            }
            else {
                currentY -= speed;
            }
        }
    }
}
