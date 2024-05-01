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

public class lvl3 extends AppCompatActivity {
    private MediaPlayer mediaPlayerw;
    private MediaPlayer mediaPlayerf;
    private MediaPlayer mediaPlayerg;
    private MediaPlayer mediaPlayerc;
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

    private int coinCounter = 0;
    private TextView coinCounterTextView;
    private ImageView coin;
    private RelativeLayout relativeLayout;
    private Handler coinGenerationHandler;
    private AnimationDrawable backgroundAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl1);
        ImageView img = (ImageView) findViewById(R.id.swing_play);
        img.setBackgroundResource(R.drawable.lvl3background);
        backgroundAnimation = (AnimationDrawable) img.getBackground();
        backgroundAnimation.setOneShot(false); // Устанавливаем флаг для бесконечной анимации
        backgroundAnimation.start();
        backgroundAnimation.start();
        initMediaPlayers();
        initViews();
        mediaPlayerg.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Воспроизведение заново, когда трек заканчивается
                mp.start();
            }
        });
        coinGenerationHandler = new Handler();
        relativeLayout = findViewById(R.id.relativeLayout2);
        coinCounterTextView = findViewById(R.id.coinCounterTextView);
        coin=findViewById(R.id.coin1);
        updateCoinCounter(coinCounter);

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
        startCoinCreation();
        mediaPlayerg.start();
    }
    private void gameWin() {
        isGameOver = true;
        obstacleHandler.removeCallbacks(createObstacleRunnable);
        coinGenerationHandler.removeCallbacks(createObstacleRunnable);
        if (moveCounterHandler != null) {
            moveCounterHandler.removeCallbacksAndMessages(null); // Остановить счетчик перемещений
        }
        if (backgroundAnimation != null) {
            backgroundAnimation.stop();
        }
        carImage.clearAnimation();
        gameOverTextView.setVisibility(View.VISIBLE);
        gameOverTextView.setText("WIN");
        retryButton.setVisibility(View.VISIBLE);
        mediaPlayerg.stop();
        mediaPlayerw.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Освобождение ресурсов MediaPlayer
        if (mediaPlayerg != null) {
            mediaPlayerg.release();
            mediaPlayerg = null;
        }
    }
    private Runnable checkCollisionRunnable = new Runnable() {
        @Override
        public void run() {
            if (coin != null) {
                checkCollisionWithCoin(coin); // Проверяем столкновение
            }
            // Запланировать следующее выполнение через 10 миллисекунд
            coinGenerationHandler.postDelayed(this, 10);
        }
    };
    public void startCollisionCheck() {
        // Запустить проверку столкновения с монеткой каждые 10 миллисекунд
        coinGenerationHandler.postDelayed(checkCollisionRunnable, 10);
    }
    public void stopCollisionCheck() {
        // Остановить проверку столкновения
        coinGenerationHandler.removeCallbacks(checkCollisionRunnable);
    }
    private void initMediaPlayers() {
        mediaPlayera = MediaPlayer.create(this, R.raw.pauseandbacksound);
        mediaPlayerud = MediaPlayer.create(this, R.raw.upanddownbuttonsound);
        mediaPlayere=MediaPlayer.create(this, R.raw.crashsound);
        mediaPlayerc=MediaPlayer.create(this, R.raw.coinsound);
        mediaPlayerg=MediaPlayer.create(this, R.raw.lvl3music);
        mediaPlayerf=MediaPlayer.create(this, R.raw.failsound);
        mediaPlayerw=MediaPlayer.create(this, R.raw.winsound);
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
                obstacleHandler.postDelayed(this, 4000);
            }
        };
        obstacleHandler.postDelayed(createObstacleRunnable, 5000);
    }

    private void startMoveCounter() {
        moveCounterHandler = new Handler();
        final Runnable moveCounterRunnable = new Runnable() {
            private int moveCounter = 0;

            @Override
            public void run() {
                moveCounter++;
                updateMoveCounter(moveCounter);
                if (moveCounter > 500) {
                    gameWin();
                } else {
                    moveCounterHandler.postDelayed(this, 250);
                }
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
        coinGenerationHandler.removeCallbacks(createObstacleRunnable);
        if (moveCounterHandler != null) {
            moveCounterHandler.removeCallbacksAndMessages(null); // Остановить счетчик перемещений
        }
        if (backgroundAnimation != null) {
            backgroundAnimation.stop();
        }
        carImage.clearAnimation();
        gameOverTextView.setVisibility(View.VISIBLE);
        gameOverTextView.setText("Game Over");
        retryButton.setVisibility(View.VISIBLE);
        mediaPlayerg.stop();
        mediaPlayerf.start();
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
        if (!isGameOver) {
            Intent intent = new Intent(this, Pausemenu.class);
            startActivity(intent);
            mediaPlayera.start();
            mediaPlayerg.stop();
        }
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



    private void startCoinCreation() {
        Handler coinGenerationHandler = new Handler();
        Runnable createCoinRunnable = new Runnable() {
            @Override
            public void run() {
                createCoin();
                coinGenerationHandler.postDelayed(this, 16000);
            }
        };
        coinGenerationHandler.postDelayed(createCoinRunnable, 16000);
    }

    private void createCoin() {
        coin = new ImageView(this);
        coin.setImageResource(R.drawable.coin1);
        RelativeLayout.LayoutParams params = createCoinLayoutParams();
        coin.setLayoutParams(params);
        relativeLayout.addView(coin); // Добавляем монету в relativeLayout
        animateCoin(coin);
        startCollisionCheck();
    }



    private RelativeLayout.LayoutParams createCoinLayoutParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.topMargin = getRandomYPosition();
        return params;
    }
    private void animateCoin(final ImageView coin) {
        coin.animate()
                .translationX(-relativeLayout.getWidth() - coin.getWidth()) // Устанавливаем конечную позицию за пределами экрана
                .setDuration(3000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        stopCollisionCheck();
                        checkCollisionWithCoin(coin); // Проверяем столкновение при окончании анимации
                    }
                })
                .start();
    }

    private void checkCollisionWithCoin(ImageView coin) {
        if (isColliding(carImage, coin) && relativeLayout.indexOfChild(coin) != -1) {
            incrementCoinCounter();
            mediaPlayerc.start();
            relativeLayout.removeView(coin); // Удаляем монету только при соприкосновении и если она еще существует в relativeLayout
        }
    }

    private void incrementCoinCounter() {
        coinCounter++;
        updateCoinCounter(coinCounter);
    }

    private void updateCoinCounter(int coinCounter) {
        coinCounterTextView.setText(String.valueOf(coinCounter));
    }

}