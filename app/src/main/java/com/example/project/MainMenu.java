package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

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
        mediaPlayerm=MediaPlayer.create(this,R.raw.playbuttonsound);
        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
        mediaPlayers=MediaPlayer.create(this,R.raw.settingsbuttonsound);
        mediaPlayerp=MediaPlayer.create(this,R.raw.mainmusic);
        mediaPlayerp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Воспроизведение заново, когда трек заканчивается
                mp.start();
            }
        });
        mediaPlayerp.start();
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
        Intent intent=new Intent(this, LevelMenu.class);
        startActivity(intent);
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }
    public void startNewActivity2(View v){
        Intent intent=new Intent(this, Options.class);
        startActivity(intent);
        mediaPlayers.start();
        mediaPlayerp.stop();
    }
    public void end(View v){
        Intent intent=new Intent(this,areusure.class);
        startActivity(intent);
        mediaPlayere.start();
        mediaPlayerp.stop();

    }
}