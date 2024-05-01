package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class lvl4 extends AppCompatActivity {
    MediaPlayer mediaPlayera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl2);
        ImageView Img = (ImageView)findViewById(R.id.backgroundlvl2);
        Img.setBackgroundResource(R.drawable.lvl2_background);
        AnimationDrawable frameAnimation = (AnimationDrawable) Img.getBackground();
        frameAnimation.start();
        initMediaPlayers();
    }
    public void pauseButton(View v) {
        Intent intent = new Intent(this, LevelMenu.class);
        startActivity(intent);
        mediaPlayera.start();
    }
    private void initMediaPlayers() {
        mediaPlayera = MediaPlayer.create(this, R.raw.pauseandbacksound);
    }
}