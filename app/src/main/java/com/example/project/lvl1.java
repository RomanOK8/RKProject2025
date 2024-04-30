package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class lvl1 extends AppCompatActivity {
    MediaPlayer mediaPlayera;
    MediaPlayer mediaPlayerud;
    private ImageView carImage;
    private float screenHeight;

    //test
    private Handler obstacleHandler;
    private Runnable createObstacleRunnable;
    //test
    private int moveCounter = 0; // Счётчик перемещений
    private TextView moveCounterTextView;//counter

    @SuppressLint("WrongViewCast")
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
        //Изображение объекта
        carImage = findViewById(R.id.CarImage);
        //Изображение объекта
        //Для ограничения по y
        DisplayMetrics displayMetrics = new DisplayMetrics();// Получаем высоту экрана
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        //Для ограничения по y
        // Запускаем таймер для увеличения счётчика каждую секунду
        moveCounterTextView = findViewById(R.id.moveCounter);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveCounter++;
                updateMoveCounter(); // Обновляем счётчик на экране
                handler.postDelayed(this, 250);
            }
        }, 250);
        //
        //test
        // Создаем препятствия каждые 3 секунды
        obstacleHandler = new Handler();
        createObstacleRunnable = new Runnable() {
            @Override
            public void run() {
                createObstacle();
                obstacleHandler.postDelayed(this, 3000);
            }
        };
        obstacleHandler.postDelayed(createObstacleRunnable, 3000);
        //test
    }
    //test
    private void createObstacle() {
        // Создаем новый ImageView для препятствия
        ImageView obstacle = new ImageView(this);
        obstacle.setImageResource(R.drawable.obstacle); // Используем ресурс препятствия

        // Устанавливаем размеры и позицию препятствия
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); // Начальное положение препятствия справа

        // Определяем верхнюю и нижнюю границы по y для генерации препятствия
        int minY = 0; // Нижняя граница
        int maxY = (int) (screenHeight - carImage.getHeight()); // Верхняя граница

        // Генерируем случайное число в диапазоне от minY до maxY
        Random random = new Random();
        int randomYPosition = random.nextInt(maxY - minY) + minY;

        // Устанавливаем случайную позицию по y
        params.topMargin = randomYPosition;
        obstacle.setLayoutParams(params);

        // Добавляем препятствие на экран
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout2);
        relativeLayout.addView(obstacle);

        // Анимируем препятствие по горизонтали влево
        obstacle.animate()
                .translationX(-relativeLayout.getWidth())
                .setDuration(3000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Удаляем препятствие после анимации
                        relativeLayout.removeView(obstacle);
                    }
                });
    }
    //test
    private void updateMoveCounter() {
        moveCounterTextView.setText(String.valueOf(moveCounter));
    }
    public void pauseButton(View v){
        Intent intent=new Intent(this,Pausemenu.class);
        startActivity(intent);
        mediaPlayera.start();
    }
    public void upButton(View v){//метод для управления по y вверх+ограничение движения вверх
        if (carImage.getY() > 0) {
            carImage.animate().translationYBy(-100).setDuration(100);
            mediaPlayerud.start();
        }

    }
    public void downButton(View v){//метод для управления по y вниз +ограничение движения вниз
        if (carImage.getY() < screenHeight - carImage.getHeight()) {
            carImage.animate().translationYBy(100).setDuration(100);
            mediaPlayerud.start();
        }
    }
}