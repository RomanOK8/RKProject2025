package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class LevelMenu extends AppCompatActivity {
    MediaPlayer mediaPlayerp;
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayerm;
    MediaPlayer mediaPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Анимация фона
        ImageView img23 = (ImageView)findViewById(R.id.levelmenubackground);
        img23.setBackgroundResource(R.drawable.levelmenubackground);
        AnimationDrawable animation = (AnimationDrawable) img23.getBackground();
        animation.setOneShot(false);
        animation.start();

        // Инициализация медиаплееров
        mediaPlayere = MediaPlayer.create(this, R.raw.endsound);
        mediaPlayerm = MediaPlayer.create(this, R.raw.playbuttonsound);
        mediaPlayers = MediaPlayer.create(this, R.raw.settingsbuttonsound);
        mediaPlayerp = MediaPlayer.create(this, R.raw.levelmenumusic);

        mediaPlayerp.setOnCompletionListener(mp -> mp.start());
        mediaPlayerp.start();

        // Назначение обработчиков для кнопок
        setTouchListenerForButton(findViewById(R.id.imageButton3), () -> startNewActivity());
        setTouchListenerForButton(findViewById(R.id.lvl1button), () -> LVLI());
        setTouchListenerForButton(findViewById(R.id.lvl2button), () -> LVLII());
        setTouchListenerForButton(findViewById(R.id.lvl3button), () -> LVLIII());
        setTouchListenerForButton(findViewById(R.id.lvl4button), () -> LVLIV());
        setTouchListenerForButton(findViewById(R.id.imageButton8), () -> LVLV());
        setTouchListenerForButton(findViewById(R.id.imageButton4), () -> settings());
        setTouchListenerForButton(findViewById(R.id.exbt2), () -> end());
        setTouchListenerForButton(findViewById(R.id.education), () -> LVLT());
    }

    private void setTouchListenerForButton(View button, Runnable action) {
        button.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                button.setAlpha(0.5f);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                button.setAlpha(1.0f);
                action.run();
            }
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerp != null) {
            mediaPlayerp.release();
            mediaPlayerp = null;
        }
    }

    public void startNewActivity() {
        startActivity(new Intent(this, MainMenu.class));
        mediaPlayere.start();
        mediaPlayerp.stop();
    }

    public void end() {
        startActivity(new Intent(this, areusure.class));
        mediaPlayere.start();
        mediaPlayerp.stop();
    }

    public void settings() {
        startActivity(new Intent(this, Options.class));
        mediaPlayers.start();
        mediaPlayerp.stop();
    }

    public void LVLT() {
        startActivity(new Intent(this, education.class));
        mediaPlayere.start();
        mediaPlayerp.stop();
    }

    public void LVLI() {
        startActivity(new Intent(this, lvl1.class));
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }

    public void LVLII() {
        startActivity(new Intent(this, lvl2.class));
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }

    public void LVLIII() {
        startActivity(new Intent(this, lvl3.class));
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }

    public void LVLIV() {
        startActivity(new Intent(this, lvl4.class));
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }

    public void LVLV() {
        startActivity(new Intent(this, lvl5.class));
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }
}