package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Rect;
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
    private MediaPlayer mediaPlayere;
    private MediaPlayer mediaPlayera;
    private MediaPlayer mediaPlayerud;
    private ImageView carImage;
    private float screenHeight;
    private TextView moveCounterTextView;
    private Handler moveCounterHandler;
    private TextView gameOverTextView;
    private Handler obstacleHandler;
    private Runnable createObstacleRunnable;
    private Button retryButton;
    private boolean isGameOver = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl1);
        ImageView img = (ImageView)findViewById(R.id.swing_play);
        img.setBackgroundResource(R.drawable.background);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
        initMediaPlayers();
        initViews();
        retryButton = findViewById(R.id.retryButton);
        // Скрываем кнопку Retry изначально
        retryButton.setVisibility(View.GONE);
        // Добавляем обработчик нажатия на кнопку Retry
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Перезапускаем уровень
                restartLevel();
            }
        });
        startObstacleCreation();
        startMoveCounter();
    }

    private void initMediaPlayers() {
        mediaPlayera = MediaPlayer.create(this, R.raw.pauseandbacksound);
        mediaPlayerud = MediaPlayer.create(this, R.raw.upanddownbuttonsound);
        mediaPlayere=MediaPlayer.create(this, R.raw.crashsound);
    }

    private void initViews() {
        carImage = findViewById(R.id.CarImage);
        moveCounterTextView = findViewById(R.id.moveCounter);
        gameOverTextView = findViewById(R.id.gameOverTextView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
    }

    private void startObstacleCreation() {
        obstacleHandler = new Handler();
        createObstacleRunnable = new Runnable() {
            @Override
            public void run() {
                createObstacle();
                obstacleHandler.postDelayed(this, 3000);
            }
        };
        obstacleHandler.postDelayed(createObstacleRunnable, 3000);
    }

    private void startMoveCounter() {
        moveCounterHandler = new Handler();
        final Runnable moveCounterRunnable = new Runnable() {
            private int moveCounter = 0;

            @Override
            public void run() {
                moveCounter++;
                updateMoveCounter(moveCounter);
                moveCounterHandler.postDelayed(this, 250);
            }
        };
        moveCounterHandler.postDelayed(moveCounterRunnable, 250);
    }

    private void createObstacle() {
        ImageView obstacle = new ImageView(this);
        obstacle.setImageResource(R.drawable.obstacle);
        RelativeLayout.LayoutParams params = createObstacleLayoutParams();
        obstacle.setLayoutParams(params);
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout2);
        relativeLayout.addView(obstacle);
        animateObstacle(obstacle, relativeLayout);
    }

    private RelativeLayout.LayoutParams createObstacleLayoutParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.topMargin = getRandomYPosition();
        return params;
    }

    private int getRandomYPosition() {
        int minY = 0;
        int maxY = (int) (screenHeight - carImage.getHeight());
        Random random = new Random();
        return random.nextInt(maxY - minY) + minY;
    }

    private void animateObstacle(ImageView obstacle, RelativeLayout relativeLayout) {
        obstacle.animate()
                .translationX(-relativeLayout.getWidth())
                .setDuration(3000)
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        checkCollisionWithObstacle(obstacle);
                    }
                })
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        relativeLayout.removeView(obstacle);
                    }
                });
    }

    private void checkCollisionWithObstacle(ImageView obstacle) {
        if (isColliding(carImage, obstacle)) {
            gameOver();
            explodeAnimation(carImage);
            mediaPlayere.start();
        }
    }
    private void explodeAnimation(ImageView view) {
        // Загружаем анимацию взрыва
        AnimationDrawable explodeAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.explosion_animation);
        // Устанавливаем анимацию как фон для view
        view.setBackground(explodeAnimation);
        // Запускаем анимацию
        explodeAnimation.start();

        // Останавливаем анимацию через определенное время
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Сбрасываем фон, чтобы удалить анимацию
                view.setBackground(null);
            }
        }, 2000); // Время, в течение которого анимация будет отображаться
    }


    private boolean isColliding(ImageView imageView1, ImageView imageView2) {
        Rect rect1 = new Rect();
        imageView1.getGlobalVisibleRect(rect1);
        Rect rect2 = new Rect();
        imageView2.getGlobalVisibleRect(rect2);
        return Rect.intersects(rect1, rect2);
    }

    private void gameOver() {
        isGameOver = true;
        obstacleHandler.removeCallbacks(createObstacleRunnable);
        if (moveCounterHandler != null) {
            moveCounterHandler.removeCallbacksAndMessages(null); // Остановить счетчик перемещений
        }
        carImage.clearAnimation();
        gameOverTextView.setVisibility(View.VISIBLE);
        gameOverTextView.setText("Game Over");
        retryButton.setVisibility(View.VISIBLE);
    }
    private void restartLevel() {
        // Завершаем текущую активность и запускаем ее заново
        finish();
        startActivity(getIntent());
    }
    private void updateMoveCounter(int moveCounter) {
        moveCounterTextView.setText(String.valueOf(moveCounter));
    }

    public void pauseButton(View v) {
        Intent intent = new Intent(this, Pausemenu.class);
        startActivity(intent);
        mediaPlayera.start();
    }

    public void upButton(View v) {
        moveCar(-100);
    }

    public void downButton(View v) {
        moveCar(100);
    }

    private void moveCar(int distance) {
        if (!isGameOver && isWithinBounds(carImage.getY() + distance)) {
            carImage.animate().translationYBy(distance).setDuration(100);
            mediaPlayerud.start();
        }
    }

    private boolean isWithinBounds(float newY) {
        return newY > 0 && newY < screenHeight - carImage.getHeight();
    }
}