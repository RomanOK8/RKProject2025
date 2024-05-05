package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class areusure extends AppCompatActivity {
    MediaPlayer mediaPlayere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areusure);
        mediaPlayere = MediaPlayer.create(this, R.raw.endsound);
        ImageView Img = (ImageView)findViewById(R.id.lol_play);
        Img.setBackgroundResource(R.drawable.backgroundareusreactivity);

        AnimationDrawable frameAnimation = (AnimationDrawable) Img.getBackground();

        frameAnimation.start();
        // Установите слушатели нажатий для кнопок
        ImageButton endButton = findViewById(R.id.leavebt);
        ImageButton startNewActivityButton = findViewById(R.id.bkbt3);

        // Метод для установки анимации прозрачности
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
        buttonClick.setDuration(0); // Длительность анимации в миллисекундах
        buttonClick.setFillAfter(true); // Сохраняет конечное состояние после анимации

        final AlphaAnimation buttonRelease = new AlphaAnimation(0.5F, 1F);
        buttonRelease.setDuration(0); // Длительность анимации в миллисекундах
        buttonRelease.setFillAfter(true); // Сохраняет конечное состояние после анимации

        View.OnTouchListener changeAlphaOnTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(buttonClick);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.startAnimation(buttonRelease);
                        if (v.getId() == R.id.leavebt) {
                            finishAffinity();
                            mediaPlayere.start();
                        } else if (v.getId() == R.id.bkbt3) {
                            Intent intent = new Intent(areusure.this, MainMenu.class);
                            startActivity(intent);
                            mediaPlayere.start();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.startAnimation(buttonRelease);
                        break;
                }
                return true;
            }
        };

        endButton.setOnTouchListener(changeAlphaOnTouch);
        startNewActivityButton.setOnTouchListener(changeAlphaOnTouch);
    }
}