package com.example.c1tappydefender;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;

public class GameActivity extends AppCompatActivity {
    private TDView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //setContentView(R.layout.activity_game);
        gameView = new TDView(this, size.x, size.y);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected  void onResume() {
        super.onResume();
        gameView.resume();
    }

    // If the player hits the back button, quit the app
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
}
