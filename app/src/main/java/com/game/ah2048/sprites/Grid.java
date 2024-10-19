package com.game.ah2048.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.game.ah2048.R;

public class Grid implements Sprite{

    private Bitmap grid;
    private int screenWidth, screenHeight;
    private int standartSize;

    public Grid(Resources resources, int screenWidth, int screenHeight, int standartSize) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.standartSize = standartSize;

        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.grid);
        grid = Bitmap.createScaledBitmap(bmp, standartSize * 4, standartSize * 4, false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(grid, screenWidth / 2 - grid.getWidth() / 2, screenHeight / 2 - grid.getHeight() / 2, null);
    }

    @Override
    public void update() {

    }
}
