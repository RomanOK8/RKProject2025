package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class areusure extends AppCompatActivity {
    MediaPlayer mediaPlayere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areusure);
        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
        ImageView Img = (ImageView)findViewById(R.id.lol_play);
        Img.setBackgroundResource(R.drawable.backgroundareusreactivity);

        AnimationDrawable frameAnimation = (AnimationDrawable) Img.getBackground();

        frameAnimation.start();
    }
    public void end(View v){
        this.finishAffinity();
        mediaPlayere.start();
    }
    public void startNewActivity(View v){
        this.finishAffinity();
        Intent intent=new Intent(this, MainMenu.class);
        startActivity(intent);
        mediaPlayere.start();
    }
}