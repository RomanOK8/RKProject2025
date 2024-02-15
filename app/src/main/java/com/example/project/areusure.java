package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class areusure extends AppCompatActivity {
    MediaPlayer mediaPlayere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areusure);
        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
    }
    public void end(View v){
        this.finishAffinity();
        mediaPlayere.start();
    }
    public void startNewActivity(View v){
        this.finishAffinity();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        mediaPlayere.start();
    }
}