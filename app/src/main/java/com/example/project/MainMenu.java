package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.example.my_framework.LoopFW;

public class MainMenu extends AppCompatActivity {
    MediaPlayer mediaPlayerm;
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayers;
    @Override
    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayerm=MediaPlayer.create(this,R.raw.playbuttonsound);
        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
        mediaPlayers=MediaPlayer.create(this,R.raw.settingsbuttonsound);
        // LoopFW loopFW=new LoopFW();
        //loopFW.startGame();
    }
    public void startNewActivity(View v){
        Intent intent=new Intent(this, LevelMenu.class);
        startActivity(intent);
        mediaPlayerm.start();
    }
    public void startNewActivity2(View v){
        Intent intent=new Intent(this, Options.class);
        startActivity(intent);
        mediaPlayers.start();
    }
    public void end(View v){
        Intent intent=new Intent(this,areusure.class);
        startActivity(intent);
        mediaPlayere.start();
    }
}