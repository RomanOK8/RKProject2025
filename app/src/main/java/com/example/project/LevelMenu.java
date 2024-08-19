package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class LevelMenu extends AppCompatActivity {
    MediaPlayer mediaPlayerp;//sadsa
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayerm;
    MediaPlayer mediaPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageView img23 = (ImageView)findViewById(R.id.levelmenubackground);
        img23.setBackgroundResource(R.drawable.levelmenubackground);
        AnimationDrawable animation = (AnimationDrawable) img23.getBackground();
        animation.setOneShot(false); // Устанавливаем флаг для бесконечной анимации
        animation.start();

        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
        mediaPlayerm=MediaPlayer.create(this,R.raw.playbuttonsound);
        mediaPlayers=MediaPlayer.create(this,R.raw.settingsbuttonsound);
        mediaPlayerp=MediaPlayer.create(this,R.raw.levelmenumusic);
        mediaPlayerp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Воспроизведение заново, когда трек заканчивается
                mp.start();
            }
        });
        mediaPlayerp.start();
        View backButton = findViewById(R.id.imageButton3);
        View startLevel1Button = findViewById(R.id.lvl1button);
        View startLevel2Button = findViewById(R.id.lvl2button);
        View startLevel3Button = findViewById(R.id.lvl3button);
        View startLevel4Button = findViewById(R.id.lvl4button);
        View startLevel5Button = findViewById(R.id.imageButton8);
        View settingsButton = findViewById(R.id.imageButton4);
        View endButton = findViewById(R.id.exbt2);
        View startLevelTraneButton = findViewById(R.id.education);
        setTouchListenerForButton(backButton, () -> startNewActivity(backButton));
        setTouchListenerForButton(startLevel1Button, () -> LVLI(startLevel1Button));
        setTouchListenerForButton(startLevel2Button, () -> LVLII(startLevel2Button));
        setTouchListenerForButton(startLevel3Button, () -> LVLIII(startLevel3Button));
        setTouchListenerForButton(startLevel4Button, () -> LVLIV(startLevel4Button));
        setTouchListenerForButton(startLevel5Button, () -> LVLV(startLevel5Button));
        setTouchListenerForButton(settingsButton, () -> settings(settingsButton));
        setTouchListenerForButton(endButton, () -> end(endButton));
        setTouchListenerForButton(startLevelTraneButton, () -> LVLT(startLevelTraneButton));
    }
    private void setTouchListenerForButton(final View button, final Runnable action) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Установка полупрозрачного цвета при нажатии
                    button.setAlpha(0.5f);
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    // Возврат к обычному виду после отпускания или отмены
                    button.setAlpha(1.0f);
                    // Выполнение действия при отпускании
                    action.run();
                }
                return true; // Мы обработали событие
            }
        });
    }
    private boolean isLevelAvailable(String level) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (level) {
            case "LVLT":
                return true;
            case "LVL1":
                return sharedPreferences.getBoolean("LVLT", false);
            case "LVL2":
                return sharedPreferences.getBoolean("LVL1", false);
            case "LVL3":
                return sharedPreferences.getBoolean("LVL2", false);
            case "LVL4":
                return sharedPreferences.getBoolean("LVL3", false);
            case "LVL5":
                return sharedPreferences.getBoolean("LVL4", false);
            default:
                return false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Освобождение ресурсов MediaPlayer
        if (mediaPlayerp != null) {
            mediaPlayerp.release();
            mediaPlayerp = null;
        }
    }
    public void startNewActivity(View v){
        Intent intent=new Intent(this, MainMenu.class);
        startActivity(intent);
        mediaPlayere.start();
        mediaPlayerp.stop();
    }
    public void end(View v){
        Intent intent=new Intent(this,areusure.class);
        startActivity(intent);
        mediaPlayere.start();
        mediaPlayerp.stop();
    }
    public void settings(View v){
        Intent intent=new Intent(this, Options.class);
        startActivity(intent);
        mediaPlayers.start();
        mediaPlayerp.stop();
    }
    public void LVLT(View V){
            Intent intent = new Intent(this, education.class);
            startActivity(intent);
            mediaPlayere.start();
            mediaPlayerp.stop();
    }

    public void LVLI(View v){
        if (isLevelAvailable("LVL1")) {
            Intent intent = new Intent(this, lvl1.class);
            startActivity(intent);
            mediaPlayerm.start();
            mediaPlayerp.stop();
        } else {
            showLevelUnavailableMessage();
        }
    }

    public void LVLII(View v){
        if (isLevelAvailable("LVL2")) {
            Intent intent = new Intent(this, lvl2.class);
            startActivity(intent);
            mediaPlayerm.start();
            mediaPlayerp.stop();
        } else {
            showLevelUnavailableMessage();
        }
    }

    public void LVLIII(View v){
        if (isLevelAvailable("LVL3")) {
            Intent intent = new Intent(this, lvl3.class);
            startActivity(intent);
            mediaPlayerm.start();
            mediaPlayerp.stop();
        } else {
            showLevelUnavailableMessage();
        }
    }

    public void LVLIV(View v){
        if (isLevelAvailable("LVL4")) {
            Intent intent = new Intent(this, lvl4.class);
            startActivity(intent);
            mediaPlayerm.start();
            mediaPlayerp.stop();
        } else {
            showLevelUnavailableMessage();
        }
    }

    public void LVLV(View v){
        if (isLevelAvailable("LVL5")) {
            Intent intent = new Intent(this, lvl5.class);
            startActivity(intent);
            mediaPlayerm.start();
            mediaPlayerp.stop();
        } else {
            showLevelUnavailableMessage();
        }
    }
    private void showLevelUnavailableMessage() {
        Toast.makeText(this, "Pass past level you haven't got an access", Toast.LENGTH_SHORT).show();
    }
}