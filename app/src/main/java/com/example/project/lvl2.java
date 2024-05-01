package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class lvl2 extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    MediaPlayer mediaPlayera;
    MediaPlayer mediaPlayerp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl2);
        ImageView Img = (ImageView)findViewById(R.id.backgroundlvl2);
        Img.setBackgroundResource(R.drawable.lvl2_background);
        AnimationDrawable frameAnimation = (AnimationDrawable) Img.getBackground();
        frameAnimation.start();
        initMediaPlayers();
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
    public void pauseButton(View v) {
        Intent intent = new Intent(this, LevelMenu.class);
        startActivity(intent);
        mediaPlayera.start();
        mediaPlayerp.stop();
    }
    private void initMediaPlayers() {
        mediaPlayera = MediaPlayer.create(this, R.raw.pauseandbacksound);
        mediaPlayerp=MediaPlayer.create(this, R.raw.lvl2music);
    }
}