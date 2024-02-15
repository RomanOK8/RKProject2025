package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
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
    }
    public void startNewActivity(View v){
        Intent intent=new Intent(this,MainActivity2.class);
        startActivity(intent);
        mediaPlayerm.start();
    }
    public void startNewActivity2(View v){
        Intent intent=new Intent(this,OptionsActivity.class);
        startActivity(intent);
        mediaPlayers.start();
    }
    public void end(View v){
        Intent intent=new Intent(this,areusure.class);
        startActivity(intent);
        mediaPlayere.start();
    }
}