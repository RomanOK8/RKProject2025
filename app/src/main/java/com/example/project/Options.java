package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class Options extends AppCompatActivity {
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayerm;
    Button button;
    SeekBar volumeSeekBar;
    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
        int maxVolueme=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar=findViewById(R.id.voluemeSeekBar2);
        volumeSeekBar.setMax(maxVolueme);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        button=findViewById(R.id.Musicbutton23);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    pause();
                }else{
                    play();
                }
            }
        });
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.mainmenu1);
        mediaPlayerm=MediaPlayer.create(this,R.raw.playbuttonsound);
        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
        ImageButton bkbt = findViewById(R.id.bkbt);
        ImageButton exbt = findViewById(R.id.imageButton);
        ImageButton stbt = findViewById(R.id.imageButton2);
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
                        if (v.getId() == R.id.imageButton) {
                            Intent intent = new Intent(Options.this, areusure.class);
                            startActivity(intent);
                            mediaPlayere.start();
                        } else if (v.getId() == R.id.bkbt) {
                            Intent intent = new Intent(Options.this, MainMenu.class);
                            startActivity(intent);
                            mediaPlayerm.start();
                        } else if (v.getId() == R.id.imageButton2) {
                            Intent intent = new Intent(Options.this, LevelMenu.class);
                            startActivity(intent);
                            mediaPlayerm.start();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.startAnimation(buttonRelease);
                        break;
                }
                return true;
            }
        };
       bkbt.setOnTouchListener(changeAlphaOnTouch);
       exbt.setOnTouchListener(changeAlphaOnTouch);
       stbt.setOnTouchListener(changeAlphaOnTouch);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Освобождение ресурсов MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public void play(){
        mediaPlayer.start();
        button.setText("Pause");
    }
    public void pause(){
        mediaPlayer.pause();
        button.setText("Play");
    }
}