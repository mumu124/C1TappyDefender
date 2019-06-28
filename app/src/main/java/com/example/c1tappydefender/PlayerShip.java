package com.example.c1tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PlayerShip {
    private Bitmap bitmap;
    private int x,y;
    private int speed = 0;

    private boolean boosting;

    public PlayerShip (Context context) {
        x = 50;
        y = 50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        boosting = false;
    }

    public void update() {
        x++;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return  y;
    }

    public void setBoosting() {
        this.boosting = true;
    }

    public void stopBoosting() {
        this.boosting = false;
    }

}
