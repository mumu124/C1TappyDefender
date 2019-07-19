package com.example.c1tappydefender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);

        final Button buttonPlay = (Button) findViewById(R.id.buttonPlay);

        final TextView textFastestTime = (TextView) findViewById(R.id.textHighScore);

        buttonPlay.setOnClickListener(this);

        long fastestTime = prefs.getLong("fastestTime", 1000000);
        textFastestTime.setText("Fastest Time:" + fastestTime);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, GameActivity.class);
        view.getContext().startActivity(i);
        finish();
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
