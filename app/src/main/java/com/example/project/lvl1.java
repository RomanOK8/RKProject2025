package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class lvl1 extends AppCompatActivity {
    MediaPlayer mediaPlayera;
    MediaPlayer mediaPlayerud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl1);
        mediaPlayera=MediaPlayer.create(this,R.raw.pauseandbacksound);
        mediaPlayerud=MediaPlayer.create(this,R.raw.upanddownbuttonsound);

        ImageView img = (ImageView)findViewById(R.id.swing_play);
        img.setBackgroundResource(R.drawable.background);

        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        frameAnimation.start();
    }
    public void pauseButton(View v){
        Intent intent=new Intent(this,Pausemenu.class);
        startActivity(intent);
        mediaPlayera.start();
    }
    public void upButton(View v){
        mediaPlayerud.start();
    }
    public void downButton(View v){
        mediaPlayerud.start();
    }
}