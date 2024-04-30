package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class Pausemenu extends AppCompatActivity {
    MediaPlayer mediaPlayers;
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayera;
    private int moveCounter; // Переменная для сохранения счетчика перемещений
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pausemenu);
        mediaPlayers=MediaPlayer.create(this,R.raw.settingsbuttonsound);
        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
        mediaPlayera=MediaPlayer.create(this,R.raw.pauseandbacksound);
    }
    public void setting(View v){
        Intent intent=new Intent(this, Options.class);
        startActivity(intent);
        mediaPlayers.start();
    }
    public void end2(View v){
        Intent intent=new Intent(this, areusure.class);
        startActivity(intent);
        mediaPlayere.start();
    }
    public void bAck(View v){
        Intent intent = new Intent(this, lvl1.class);
        intent.putExtra("moveCounter", getIntent().getIntExtra("moveCounter", 0)); // Передаем сохраненное значение счетчика
        startActivity(intent);
        mediaPlayera.start();
    }

}