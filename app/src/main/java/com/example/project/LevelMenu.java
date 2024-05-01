package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LevelMenu extends AppCompatActivity {
    MediaPlayer mediaPlayerp;
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayerm;
    MediaPlayer mediaPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageView img23 = (ImageView)findViewById(R.id.levelmenubackground);
        img23.setBackgroundResource(R.drawable.levelmenubackground);
        AnimationDrawable animation = (AnimationDrawable) img23.getBackground();
        animation.setOneShot(false); // Устанавливаем флаг для бесконечной анимации
        animation.start();

        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
        mediaPlayerm=MediaPlayer.create(this,R.raw.playbuttonsound);
        mediaPlayers=MediaPlayer.create(this,R.raw.settingsbuttonsound);
        mediaPlayerp=MediaPlayer.create(this,R.raw.levelmenumusic);
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
    public void startNewActivity(View v){
        Intent intent=new Intent(this, MainMenu.class);
        startActivity(intent);
        mediaPlayere.start();
        mediaPlayerp.stop();
    }
    public void end(View v){
        Intent intent=new Intent(this,areusure.class);
        startActivity(intent);
        mediaPlayere.start();
        mediaPlayerp.stop();
    }
    public void settings(View v){
        Intent intent=new Intent(this, Options.class);
        startActivity(intent);
        mediaPlayers.start();
        mediaPlayerp.stop();
    }
    public void LVLI(View v){
        Intent intent=new Intent(this,lvl1.class);
        startActivity(intent);
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }
    public void LVLII(View v){
        Intent intent=new Intent(this,lvl2.class);
        startActivity(intent);
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }
    public void LVLIII(View v){
        Intent intent=new Intent(this,lvl3.class);
        startActivity(intent);
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }
    public void LVLIV(View v){
        Intent intent=new Intent(this,lvl4.class);
        startActivity(intent);
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }
    public void LVLV(View v){
        Intent intent=new Intent(this,lvl5.class);
        startActivity(intent);
        mediaPlayerm.start();
        mediaPlayerp.stop();
    }
}