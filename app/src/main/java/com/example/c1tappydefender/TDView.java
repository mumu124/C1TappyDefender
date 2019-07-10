package com.example.c1tappydefender;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class TDView extends SurfaceView implements Runnable {
    private boolean gameEnded;
    volatile boolean playing;
    Thread gameThread = null;
    private PlayerShip player;
    public EnemyShip enemy1;
    public EnemyShip enemy2;
    public EnemyShip enemy3;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    private int screenX;
    private int screenY;
    private Context context;

    // Make some random space dust
    public ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();

    public TDView(Context context, int x, int y) {
        super(context);
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        startGame();

        player.update();
    }

    private void startGame() {
        player = new PlayerShip(context, screenX,screenY);
        enemy1 = new EnemyShip(context, screenX,screenY);
        enemy2 = new EnemyShip(context, screenX,screenY);
        enemy3 = new EnemyShip(context, screenX,screenY);

        int numSpecs = 200;
        for (int i = 0; i < numSpecs; i++) {
            SpaceDust spec = new SpaceDust(screenX,screenY);
            dustList.add(spec);
        }

        // Reset time and distance
        distanceRemaining = 10000;// 10 km
        timeTaken = 0;
        // Get start time
        timeStarted = System.currentTimeMillis();
        gameEnded = false;
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update(){
        boolean hitDetected = false;
        if (Rect.intersects(player.getHitbox(), enemy1.getHitbox())) {
            hitDetected = true;
            enemy1.setX(-100);
        }
        if (Rect.intersects(player.getHitbox(), enemy2.getHitbox())) {
            hitDetected = true;
            enemy2.setX(-100);
        }
        if (Rect.intersects(player.getHitbox(), enemy3.getHitbox())) {
            hitDetected = true;
            enemy3.setX(-100);
        }
        if (hitDetected) {
            player.reduceShieldStrength();
            if (player.getShieldStrength() < 0) {
                gameEnded = true;
            }
        }

        player.update();
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());

        for (SpaceDust sd: dustList) {
            sd.update(player.getSpeed());
        }

        if(!gameEnded) {
            //subtract distance to home planet based on current speed
            distanceRemaining -= player.getSpeed();
            //How long has the player been flying
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        //Completed the game!
        if(distanceRemaining < 0){
            //check for new fastest time
            if(timeTaken < fastestTime) {
                fastestTime = timeTaken;
            }
            // avoid ugly negative numbers
            // in the HUD
            distanceRemaining = 0;
            // Now end the game
            gameEnded = true;
        }
    }
    private void draw(){
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255,0,0,0));

            // For debugging
            // Switch to white pixels
            paint.setColor(Color.argb(255, 255, 255, 255));
            // Draw Hit boxes
/*
            canvas.drawRect(player.getHitbox().left,
                    player.getHitbox().top,
                    player.getHitbox().right,
                    player.getHitbox().bottom,
                    paint);
            canvas.drawRect(enemy1.getHitbox().left,
                    enemy1.getHitbox().top,
                    enemy1.getHitbox().right,
                    enemy1.getHitbox().bottom,
                    paint);
            canvas.drawRect(enemy2.getHitbox().left,
                    enemy2.getHitbox().top,
                    enemy2.getHitbox().right,
                    enemy2.getHitbox().bottom,
                    paint);
            canvas.drawRect(enemy3.getHitbox().left,
                    enemy3.getHitbox().top,
                    enemy3.getHitbox().right,
                    enemy3.getHitbox().bottom,
                    paint);
*/
            paint.setColor(Color.argb(255,255,255,255));
            for (SpaceDust sd : dustList) {
                canvas.drawPoint(sd.getX(), sd.getY(), paint);
            }

            canvas.drawBitmap(player.getBitmap(),player.getX(),player.getY(),paint);
            canvas.drawBitmap(enemy1.getBitmap(),enemy1.getX(),enemy1.getY(),paint);
            canvas.drawBitmap(enemy2.getBitmap(),enemy2.getX(),enemy2.getY(),paint);
            canvas.drawBitmap(enemy3.getBitmap(),enemy3.getX(),enemy3.getY(),paint);

            // Draw the hud
            if (!gameEnded) {
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
                canvas.drawText("Fastest:" + fastestTime + "s", 10, 20, paint);
                canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 20,
                        paint);
                canvas.drawText("Distance:" + distanceRemaining / 1000 + " KM", screenX / 3, screenY - 20, paint);
                canvas.drawText("Shield:" + player.getShieldStrength(), 10, screenY - 20, paint);
                canvas.drawText("Speed:" + player.getSpeed() * 60 + " MPS", (screenX / 3) * 2, screenY - 20, paint);
            } else {
                // Show pause screen
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX/2, 100, paint);
                paint.setTextSize(25);
                canvas.drawText("Fastest:"+
                        fastestTime + "s", screenX/2, 160, paint);
                canvas.drawText("Time:" + timeTaken +
                        "s", screenX / 2, 200, paint);
                canvas.drawText("Distance remaining:" +
                        distanceRemaining/1000 + " KM",screenX/2, 240, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", screenX/2, 350, paint);
            }

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }
    private void control(){
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {

        }
    }

// Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }
    // Make a new thread and start it
// Execution moves to our R
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent (MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                if (gameEnded) {
                    startGame();
                }
                break;
        }
        return true;
    }


}
