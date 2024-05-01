package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class Options extends AppCompatActivity {
    MediaPlayer mediaPlayere;
    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayerm;
    Button button;
    SeekBar volumeSeekBar;
    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
        int maxVolueme=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar=findViewById(R.id.voluemeSeekBar2);
        volumeSeekBar.setMax(maxVolueme);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        button=findViewById(R.id.Musicbutton23);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    pause();
                }else{
                    play();
                }
            }
        });
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.mainmenu1);
        mediaPlayerm=MediaPlayer.create(this,R.raw.playbuttonsound);
        mediaPlayere=MediaPlayer.create(this,R.raw.endsound);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Освобождение ресурсов MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public void play(){
        mediaPlayer.start();
        button.setText("Pause");
    }
    public void pause(){
        mediaPlayer.pause();
        button.setText("Play");
    }
    public void stop() {
        if (true) {
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
                button.setText("Pause");
                mediaPlayer.stop();
        }
    }
    public void stop2() {
        Intent intent = new Intent(this, LevelMenu.class);
        startActivity(intent);
        button.setText("Pause");
        mediaPlayer.stop();
    }public void stop3() {
        Intent intent = new Intent(this, areusure.class);
        startActivity(intent);
        button.setText("Pause");
        mediaPlayer.stop();
    }
    public void startNewActivity(View v){
        Intent intent=new Intent(this, MainMenu.class);
        startActivity(intent);
        stop();
        mediaPlayere.start();
    }
    public void startNewActivity2(View v){
        Intent intent=new Intent(this, LevelMenu.class);
        startActivity(intent);
        stop2();
        mediaPlayerm.start();
    }
    public void end(View v){
        Intent intent=new Intent(this,areusure.class);
        startActivity(intent);
        stop3();
        mediaPlayere.start();
    }
}