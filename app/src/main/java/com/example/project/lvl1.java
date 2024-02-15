package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class lvl1 extends AppCompatActivity {
    MediaPlayer mediaPlayera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl1);
        mediaPlayera=MediaPlayer.create(this,R.raw.pauseandbacksound);
    }
    public void pauseButton(View v){
        Intent intent=new Intent(this,Pausemenu.class);
        startActivity(intent);
        mediaPlayera.start();
    }
}