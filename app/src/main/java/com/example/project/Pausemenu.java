package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;

public class Pausemenu extends AppCompatActivity {
    MediaPlayer mediaPlayers;
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pausemenu);

        mediaPlayers = MediaPlayer.create(this, R.raw.settingsbuttonsound);
        mediaPlayere = MediaPlayer.create(this, R.raw.endsound);
        mediaPlayera = MediaPlayer.create(this, R.raw.pauseandbacksound);

        ImageButton options = findViewById(R.id.settingsbt1);
        ImageButton resume = findViewById(R.id.resumebutton2);
        ImageButton exbt = findViewById(R.id.exbt2);

        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
        buttonClick.setDuration(0);
        buttonClick.setFillAfter(true);

        final AlphaAnimation buttonRelease = new AlphaAnimation(0.5F, 1F);
        buttonRelease.setDuration(0);
        buttonRelease.setFillAfter(true);

        View.OnTouchListener changeAlphaOnTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(buttonClick);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.startAnimation(buttonRelease);
                        if (v.getId() == R.id.settingsbt1) {
                            Intent intent = new Intent(Pausemenu.this, Options.class);
                            startActivity(intent);
                            mediaPlayers.start();
                        } else if (v.getId() == R.id.exbt2) {
                            Intent intent = new Intent(Pausemenu.this, areusure.class);
                            startActivity(intent);
                            mediaPlayere.start();
                        } else if (v.getId() == R.id.resumebutton2) {
                            // Переход обратно в уровень
                            Intent intent = getIntent();
                            String levelClassName = intent.getStringExtra("levelClass");
                            if (levelClassName != null) {
                                try {
                                    Class<?> levelClass = Class.forName(levelClassName);
                                    startLevelActivity(levelClass);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.startAnimation(buttonRelease);
                        break;
                }
                return true;
            }
        };

        options.setOnTouchListener(changeAlphaOnTouch);
        exbt.setOnTouchListener(changeAlphaOnTouch);
        resume.setOnTouchListener(changeAlphaOnTouch);
    }

    private void startLevelActivity(Class<?> levelClass) {
        Intent intent = new Intent(Pausemenu.this, levelClass);
        startActivity(intent);
        mediaPlayera.start();
    }
}