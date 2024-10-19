package com.game.ah2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.game.ah2048.sprites.EndGame;
import com.game.ah2048.sprites.Grid;
import com.game.ah2048.sprites.Score;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback, SwipeCallback, GameManagerCallback {

    private static final String APP_NAME = "2048 4x4";

    private MainThread thread;
    private Grid grid;
    private int scWidth, scHeight, standartSize;
    private TileManager tileManager;
    private boolean endGame = false;
    private EndGame endGameSprite;
    private SwipeListener swipe;
    private Score score;
    private Bitmap restartButton;
    private Bitmap frame;
    private int restartButtonX, restartButtonY, restartButtonSize;
    private boolean interstitialShown = false;
    private InterstitialAd mInterstitialAd;

    public GameManager(Context context, AttributeSet attributes) {
        super(context, attributes);
        setLongClickable(true);
        getHolder().addCallback(this);
        swipe = new SwipeListener(getContext(), this);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        scWidth = dm.widthPixels;
        scHeight = dm.heightPixels;
        standartSize = (int) (scWidth * 0.88) / 4;
        grid = new Grid(getResources(), scWidth, scHeight, standartSize);
        tileManager = new TileManager(getResources(), standartSize, scWidth, scHeight, this);
        score = new Score(getResources(), scWidth, scHeight, standartSize, getContext().getSharedPreferences(APP_NAME, Context.MODE_PRIVATE));
        endGameSprite = new EndGame(getResources(), scWidth, scHeight);

        restartButtonSize = (int) getResources().getDimension(R.dimen.restart_button_size);
        Bitmap bmpRestart = BitmapFactory.decodeResource(getResources(), R.drawable.restart);
        restartButton = Bitmap.createScaledBitmap(bmpRestart, restartButtonSize, restartButtonSize, false);
        restartButtonX = scWidth / 2 + 2 * standartSize - restartButtonSize;
        restartButtonY = scHeight / 2 - 2 * standartSize - 3 * restartButtonSize / 2;
    }

    public void initGame() {

        endGame = false;
        interstitialShown = false;
        tileManager.initGame();
        score = new Score(getResources(), scWidth, scHeight, standartSize, getContext().getSharedPreferences(APP_NAME, Context.MODE_PRIVATE));
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread = new MainThread(holder, this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceHolder(holder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                System.out.println("exception");
            }
        }
    }

    public void update() {
        if(!endGame) {
            tileManager.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRGB(80, 40, 0);
        grid.draw(canvas);
        tileManager.draw(canvas);
        score.draw(canvas);
        canvas.drawBitmap(restartButton, restartButtonX, restartButtonY, null);
        if(endGame) {
            endGameSprite.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(endGame) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                initGame();
            }
        }
        else {
            float eventX = event.getAxisValue(MotionEvent.AXIS_X);
            float eventY = event.getAxisValue(MotionEvent.AXIS_Y);
            if(event.getAction() == MotionEvent.ACTION_DOWN &&
                    eventX > restartButtonX && eventX < restartButtonX + restartButtonSize &&
                    eventY > restartButtonY && eventY < restartButtonY + restartButtonSize) {
                initGame();
            }
            else {
                swipe.onTouchEvent(event);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onSwipe(Direction direction) {
        tileManager.onSwipe(direction);
    }

    @Override
    public void gameOver() {
        endGame = true;
        if(!interstitialShown) {
            interstitialShown = true;
            loadInterstitialAd();
        }
    }

    @Override
    public void updateScore(int delta) {
        score.updateScore(delta);
    }

    @Override
    public void reached2048() {
        score.reached2048();
    }

    public void loadInterstitialAd() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InterstitialAd.load(getContext(), "ca-app-pub-3940256099942544/1033173712", new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        interstitialAd.show(((Activity) getContext()).getParent());
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
            }
        });
    }
}
