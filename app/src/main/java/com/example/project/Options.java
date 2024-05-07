package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class Options extends AppCompatActivity {
    private MediaPlayer mediaPlayere;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerm;
    private Button button;
    private SeekBar volumeSeekBar;
    private SeekBar brightnessSeekBar;
    private AudioManager audioManager;
    private int brightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar = findViewById(R.id.voluemeSeekBar2);
        brightnessSeekBar = findViewById(R.id.brightnessSeekBar);
        brightnessSeekBar.setMax(255);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        brightness = (int) (layoutParams.screenBrightness * 255);
        brightnessSeekBar.setProgress(brightness);

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Измените яркость экрана
                adjustBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Не используется
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Не используется
            }
        });

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Не используется
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Не используется
            }
        });

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Не используется
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Не используется
            }
        });

        button = findViewById(R.id.Musicbutton23);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    pause();
                } else {
                    play();
                }
            }
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mainmenu1);
        mediaPlayerm = MediaPlayer.create(this, R.raw.playbuttonsound);
        mediaPlayere = MediaPlayer.create(this, R.raw.endsound);

        ImageButton bkbt = findViewById(R.id.bkbt);
        ImageButton exbt = findViewById(R.id.imageButton);
        ImageButton stbt = findViewById(R.id.imageButton2);

        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
        buttonClick.setDuration(0);
        buttonClick.setFillAfter(true);

        final AlphaAnimation buttonRelease = new AlphaAnimation(0.5F, 1F);
        buttonRelease.setDuration(0);
        buttonRelease.setFillAfter(true);

        View.OnTouchListener changeAlphaOnTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.startAnimation(buttonClick);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.startAnimation(buttonRelease);
                        if (v.getId() == R.id.imageButton) {
                            Intent intent = new Intent(Options.this, areusure.class);
                            startActivity(intent);
                            mediaPlayere.start();
                            mediaPlayer.stop();
                        } else if (v.getId() == R.id.bkbt) {
                            Intent intent = new Intent(Options.this, MainMenu.class);
                            startActivity(intent);
                            mediaPlayerm.start();
                            mediaPlayer.stop();
                        } else if (v.getId() == R.id.imageButton2) {
                            Intent intent = new Intent(Options.this, LevelMenu.class);
                            startActivity(intent);
                            mediaPlayerm.start();
                            mediaPlayer.stop();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        v.startAnimation(buttonRelease);
                        break;
                }
                return true;
            }
        };

        bkbt.setOnTouchListener(changeAlphaOnTouch);
        exbt.setOnTouchListener(changeAlphaOnTouch);
        stbt.setOnTouchListener(changeAlphaOnTouch);
    }

    private void adjustBrightness(int brightness) {

        float brightnessFloat = brightness / 255.0f;

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = brightnessFloat;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaPlayere != null) {
            mediaPlayere.release();
            mediaPlayere = null;
        }
        if (mediaPlayerm != null) {
            mediaPlayerm.release();
            mediaPlayerm = null;
        }
    }

    public void play() {
        mediaPlayer.start();
        button.setText("Pause");
    }

    public void pause() {
        mediaPlayer.pause();
        button.setText("Play");
    }
}