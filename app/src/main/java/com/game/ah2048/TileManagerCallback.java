package com.game.ah2048;

import android.graphics.Bitmap;

import com.game.ah2048.sprites.Tile;

public interface TileManagerCallback {
    Bitmap getBitmap(int count);
    void finishMoving(Tile t);
    void updateScore(int delta);
    void reached2048();
}
