package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class lvl1 extends AppCompatActivity {
    private AnimationDrawable starshipAnimation, coinAnimation, obstacleAnimation, backgroundAnimation;//все анимации в активности
    private MediaPlayer mediaPlayerac,mediaPlayerw, mediaPlayerf, mediaPlayerg, mediaPlayerc,mediaPlayere,mediaPlayera,  mediaPlayerud ;//тупо все звуки в активности
    private ImageView carImage, coin;
    private TextView moveCounterTextView, gameOverTextView, coinCounterTextView;
    private float screenHeight,initialX;
    private Handler moveCounterHandler, obstacleHandler, coinGenerationHandler;
    private Runnable createObstacleRunnable;
    private Button retryButton;
    private boolean isGameOver = false, moveCarFastc=false;
    private RelativeLayout relativeLayout;
    private long lastClickTime = 0;
    private int moveCounter = 0, winscore=599, coinCounter = 0;//счетчик монеток, движения, и кол.во очков, которое нужно набрать для победы
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl1);

        ImageView img = (ImageView) findViewById(R.id.swing_play);//анимация заднего фона
        img.setBackgroundResource(R.drawable.background);

        ImageView carImage = findViewById(R.id.CarImage);//анимация корабля
        carImage.setBackgroundResource(R.drawable.starship_animation);
        starshipAnimation = (AnimationDrawable) carImage.getBackground();
        starshipAnimation.setOneShot(false);//сделал бесконечную анимацию,т.к просто останавливалась :(
        starshipAnimation.start();
        backgroundAnimation = (AnimationDrawable) img.getBackground();
        backgroundAnimation.setOneShot(false);
        backgroundAnimation.start();

        ImageView coin=findViewById(R.id.coin1);
        coin.setBackgroundResource(R.drawable.coin_animation);
        coinAnimation = (AnimationDrawable) coin.getBackground();
        coinAnimation.setOneShot(false);
        coinAnimation.start();

        ImageView obstacle2=findViewById(R.id.obstacle);
        obstacle2.setBackgroundResource(R.drawable.obstacle);
        obstacleAnimation=(AnimationDrawable) coin.getBackground();
        obstacleAnimation.setOneShot(false);
        obstacleAnimation.start();
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
        retryButton.setVisibility(View.GONE);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartLevel();
            }
        });
        initialX = carImage.getX();
        ImageButton accelerator = findViewById(R.id.accelerator);


        View pauseButton = findViewById(R.id.PauseButtonlvl1);
        View upButton = findViewById(R.id.UpButtonlvl1);
        View downButton = findViewById(R.id.downbutton);
        View acceleratorButton = findViewById(R.id.accelerator);
        View retryButton = findViewById(R.id.retryButton);

        setTouchListenerForButton(pauseButton, () -> pauseButton(pauseButton));
        setTouchListenerForButton(upButton, () -> upButton(upButton));
        setTouchListenerForButton(downButton, () -> downButton(downButton));
        setTouchListenerForButton(acceleratorButton, () -> acceleratorButton (accelerator));
        setTouchListenerForButton(retryButton, () -> restartLevel());


        startObstacleCreation();
        startMoveCounter();
        startCoinCreation();
        mediaPlayerg.start();
    }
    public void acceleratorButton(View view) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime >= 10000) { // ускорение нажимается раз в 10 сек
            lastClickTime = clickTime;
            incrementMoveCounter();
            moveCarFast();
        }
    }
    private void setTouchListenerForButton(final View button, final Runnable action) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button.setAlpha(0.5f);
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    button.setAlpha(1.0f);
                    action.run();
                }
                return true;
            }
        });
    }
    private void updateMoveCounter(int moveCounter) {
        moveCounterTextView.setText(String.valueOf(moveCounter));
        if(moveCarFastc){
            int currentCount = Integer.parseInt(moveCounterTextView.getText().toString());
            // Увеличиваем его на 25(да да +12=25 без вопросов(без понятия как и почему))
            int newCount = currentCount + 12;
            moveCounterTextView.setText(String.valueOf(newCount));
        }
    }
    private void incrementMoveCounter() {
        if (!isGameOver) {
            int currentCount = Integer.parseInt(moveCounterTextView.getText().toString());
            // + 25
            int newCount = currentCount + 12;
            moveCounter += 12; // +25
            updateMoveCounter(moveCounter); // обновляем textview
        }
    }
    private void moveCarFast() {
        if (!isGameOver) {
            // использую перменную initialX для хранения координаты
            initialX = carImage.getX();
            // перемещение при нажатии carImage вправо
            carImage.animate()
                    .translationXBy(1500)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // После нажатаия на газ, будет анимация в 1,5 сек, после которой корабль вернется назад
                            carImage.animate()
                                    .x(initialX)
                                    .setDuration(500)
                                    .start();
                        }
                    })
                    .start();
        }
        mediaPlayerac.start();
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
        gameOverTextView.setTextColor(Color.GREEN);
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
        if (starshipAnimation != null) {
            starshipAnimation.stop();
        }
        if (backgroundAnimation != null) {
            backgroundAnimation.stop();
            backgroundAnimation=null;
        }
        if (coinAnimation != null) {
            coinAnimation.stop();
            coinAnimation=null;
        }
        if (obstacleAnimation != null) {
            obstacleAnimation.stop();
            obstacleAnimation=null;
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
        mediaPlayera = MediaPlayer.create(this, R.raw.pauseandbacksound);//звук кнопки паузы
        mediaPlayerud = MediaPlayer.create(this, R.raw.upanddownbuttonsound);//звук кнопок вверх и вниз
        mediaPlayere=MediaPlayer.create(this, R.raw.crashsound);//звук столкновения
        mediaPlayerc=MediaPlayer.create(this, R.raw.coinsound);//звук подбирания монетки
        mediaPlayerg=MediaPlayer.create(this, R.raw.lvl1);//музыка уровня
        mediaPlayerf=MediaPlayer.create(this, R.raw.failsound);//звук проигрыша
        mediaPlayerw=MediaPlayer.create(this, R.raw.winsound);//звук выигрыша
        mediaPlayerac=MediaPlayer.create(this, R.raw.acceleratorsound);//звук ускорения
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
                obstacleHandler.postDelayed(this, 7000);
            }
        };
        obstacleHandler.postDelayed(createObstacleRunnable, 7000);
    }

    private void startMoveCounter() {
        moveCounterHandler = new Handler();
        final Runnable moveCounterRunnable = new Runnable() {


            @Override
            public void run() {
                moveCounter++;
                updateMoveCounter(moveCounter);
                if (moveCounter >winscore) {
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
        RelativeLayout.LayoutParams params = createObstacleLayoutParams();
        obstacle.setLayoutParams(params);
        relativeLayout.addView(obstacle);

        // Установка анимации для ImageView объекта
        obstacle.setBackgroundResource(R.drawable.obstacle);
        obstacleAnimation = (AnimationDrawable) obstacle.getBackground();
        obstacleAnimation.setOneShot(false); // Устанавливаем флаг для бесконечной анимации
        obstacleAnimation.start();

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
                // Останавливаем анимацию
                explodeAnimation.stop();
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


    public void pauseButton(View v) {
        Intent intent = new Intent(this, Pausemenu.class);
        intent.putExtra("levelClass", lvl1.class.getName());
        startActivity(intent);
        mediaPlayera.start();
        mediaPlayerg.stop();
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
        if (!isGameOver) {
            Handler coinGenerationHandler = new Handler();
            Runnable createCoinRunnable = new Runnable() {
                @Override
                public void run() {
                    createCoin();
                    coinGenerationHandler.postDelayed(this, 10000);
                }
            };
            coinGenerationHandler.postDelayed(createCoinRunnable, 10000);
        }
    }

    private void createCoin() {
        if (!isGameOver) {
            coin = new ImageView(this);
            RelativeLayout.LayoutParams params = createCoinLayoutParams();
            coin.setLayoutParams(params);
            relativeLayout.addView(coin); // Добавляем монету в relativeLayout

            // Устанавливаем анимацию для ImageView монетки
            coin.setImageResource(R.drawable.coin_animation);
            coinAnimation = (AnimationDrawable) coin.getDrawable();
            coinAnimation.setOneShot(false); // Устанавливаем флаг для бесконечной анимации
            coinAnimation.start();

            animateCoin(coin);
            startCollisionCheck();
        }
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