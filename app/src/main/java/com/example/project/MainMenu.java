package com.example.project;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {
    MediaPlayer mediaPlayerm;
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayers;
    MediaPlayer mediaPlayerp;

    @Override
    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayerm = MediaPlayer.create(this, R.raw.playbuttonsound);
        mediaPlayere = MediaPlayer.create(this, R.raw.endsound);
        mediaPlayers = MediaPlayer.create(this, R.raw.settingsbuttonsound);
        mediaPlayerp = MediaPlayer.create(this, R.raw.mainmusic);
        ImageButton stbt = findViewById(R.id.stbt);
        ImageButton exbt = findViewById(R.id.exbt); // Предполагается, что у вас есть еще одна кнопка
        ImageButton setbt = findViewById(R.id.setbt); // Предполагается, что у вас есть еще одна кнопка

        setTouchListenerForButton(stbt, new Runnable() {
            @Override
            public void run() {
                startNewActivity(stbt);
            }
        });

        setTouchListenerForButton(setbt, new Runnable() {
            @Override
            public void run() {
                startNewActivity2(setbt);
            }
        });

        setTouchListenerForButton(exbt, new Runnable() {
            @Override
            public void run() {
                end(exbt);
            }
        });

        mediaPlayerp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Воспроизведение заново, когда трек заканчивается
                mp.start();
            }
        });
        mediaPlayerp.start();

    }
    private void setTouchListenerForButton(final ImageButton button, final Runnable action) {
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Освобождение ресурсов MediaPlayer
        if (mediaPlayerp != null) {
            mediaPlayerp.release();
            mediaPlayerp = null;
        }
    }

    public void startNewActivity(View v) {
        Intent intent = new Intent(this, LevelMenu.class);
        startActivity(intent);
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }

    public void startNewActivity2(View v) {
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
        mediaPlayers.start();
        mediaPlayerp.stop();
    }

    public void end(View v) {
        Intent intent = new Intent(this, areusure.class);
        startActivity(intent);
        mediaPlayere.start();
        mediaPlayerp.stop();
    }
}