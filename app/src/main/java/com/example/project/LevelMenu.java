package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class LevelMenu extends AppCompatActivity {
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayerm;
    MediaPlayer mediaPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
        mediaPlayerm=MediaPlayer.create(this,R.raw.playbuttonsound);
        mediaPlayers=MediaPlayer.create(this,R.raw.settingsbuttonsound);
    }
    public void startNewActivity(View v){
        Intent intent=new Intent(this, MainMenu.class);
        startActivity(intent);
        mediaPlayere.start();
    }
    public void end(View v){
        Intent intent=new Intent(this,areusure.class);
        startActivity(intent);
        mediaPlayere.start();
    }
    public void settings(View v){
        Intent intent=new Intent(this, Options.class);
        startActivity(intent);
        mediaPlayers.start();
    }
    public void LVLI(View v){
        Intent intent=new Intent(this,lvl1.class);
        startActivity(intent);
        mediaPlayerm.start();
    }
    public void LVLII(View v){
        Intent intent=new Intent(this,lvl2.class);
        startActivity(intent);
        mediaPlayerm.start();
    }
    public void LVLIII(View v){
        Intent intent=new Intent(this,lvl3.class);
        startActivity(intent);
        mediaPlayerm.start();
    }
    public void LVLIV(View v){
        Intent intent=new Intent(this,lvl4.class);
        startActivity(intent);
        mediaPlayerm.start();
    }
    public void LVLV(View v){
        Intent intent=new Intent(this,lvl5.class);
        startActivity(intent);
        mediaPlayerm.start();
    }
}