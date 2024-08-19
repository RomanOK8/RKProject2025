package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class lvl2 extends AppCompatActivity {
    private AnimationDrawable starshipAnimation, coinAnimation, obstacleAnimation, backgroundAnimation;
    private MediaPlayer mediaPlayerac, mediaPlayerw, mediaPlayerf, mediaPlayerg, mediaPlayerc, mediaPlayere, mediaPlayera, mediaPlayerud;
    private ImageView carImage, coin;
    private TextView moveCounterTextView, gameOverTextView, coinCounterTextView;
    private float screenHeight, initialX;
    private Handler moveCounterHandler, obstacleHandler, coinGenerationHandler, shotHandler;
    private Runnable createObstacleRunnable, shotRunnable;
    private Button retryButton;
    private boolean isGameOver = false, moveCarFastc = false, canShoot = true;
    private RelativeLayout relativeLayout;
    private long lastClickTime = 0;
    private int moveCounter = 0, winscore = 50, coinCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lvl2);

        ImageView img = findViewById(R.id.swing_play);
        img.setBackgroundResource(R.drawable.lvl2_background);

        carImage = findViewById(R.id.CarImage);
        carImage.setBackgroundResource(R.drawable.starship_animation);
        starshipAnimation = (AnimationDrawable) carImage.getBackground();
        starshipAnimation.setOneShot(false);
        starshipAnimation.start();
        backgroundAnimation = (AnimationDrawable) img.getBackground();
        backgroundAnimation.setOneShot(false);
        backgroundAnimation.start();

        coin = findViewById(R.id.coin1);
        coin.setBackgroundResource(R.drawable.coin_animation);
        coinAnimation = (AnimationDrawable) coin.getBackground();
        coinAnimation.setOneShot(false);
        coinAnimation.start();

        ImageView obstacle2 = findViewById(R.id.obstacle);
        obstacle2.setBackgroundResource(R.drawable.obstacle);
        obstacleAnimation = (AnimationDrawable) obstacle2.getBackground();
        obstacleAnimation.setOneShot(false);
        obstacleAnimation.start();

        initMediaPlayers();
        initViews();

        mediaPlayerg.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });

        coinGenerationHandler = new Handler();
        relativeLayout = findViewById(R.id.relativeLayout2);
        coinCounterTextView = findViewById(R.id.coinCounterTextView);
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

        View pauseButton = findViewById(R.id.PauseButtonlvl1);
        View upButton = findViewById(R.id.UpButtonlvl1);
        View downButton = findViewById(R.id.downbutton);
        View acceleratorButton = findViewById(R.id.accelerator);
        View retryButton = findViewById(R.id.retryButton);
        View shootButton = findViewById(R.id.shootButton);
        View shieldButton = findViewById(R.id.shieldButton);
        setTouchListenerForButton(pauseButton, () -> pauseButton(pauseButton));
        setTouchListenerForButton(upButton, () -> upButton(upButton));
        setTouchListenerForButton(downButton, () -> downButton(downButton));
        setTouchListenerForButton(acceleratorButton, () -> acceleratorButton(acceleratorButton));
        setTouchListenerForButton(retryButton, () -> restartLevel());
        setTouchListenerForButton(shootButton, () -> createShot());
        setTouchListenerForButton(shieldButton, () -> activateShield());

        startObstacleCreation();
        startMoveCounter();
        startCoinCreation();
        mediaPlayerg.start();
    }
    private boolean isShieldActive = false;
    private Handler shieldHandler = new Handler();
    private Runnable deactivateShieldRunnable = new Runnable() {
        @Override
        public void run() {
            deactivateShield();
        }
    };
    private void activateShield() {
        if (!isShieldActive) {
            isShieldActive = true;
            carImage.setBackgroundResource(R.drawable.starshipwithshield_animation);
            starshipAnimation = (AnimationDrawable) carImage.getBackground();
            starshipAnimation.start();
            shieldHandler.postDelayed(deactivateShieldRunnable, 5000);
        }
    }
    private void deactivateShield() {
        isShieldActive = false;
        carImage.setBackgroundResource(R.drawable.starship_animation);
        starshipAnimation = (AnimationDrawable) carImage.getBackground();
        starshipAnimation.start();
    }

    public void acceleratorButton(View view) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime >= 10000) {
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
        if (moveCarFastc) {
            int currentCount = Integer.parseInt(moveCounterTextView.getText().toString());
            int newCount = currentCount + 12;
            moveCounterTextView.setText(String.valueOf(newCount));
        }
    }

    private void incrementMoveCounter() {
        if (!isGameOver) {
            int currentCount = Integer.parseInt(moveCounterTextView.getText().toString());
            int newCount = currentCount + 12;
            moveCounter += 12;
            updateMoveCounter(moveCounter);
        }
    }

    private void moveCarFast() {
        if (!isGameOver) {
            initialX = carImage.getX();
            carImage.animate()
                    .translationXBy(1500)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
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
    private void saveLevelCompletion(String level) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(level, true);
        editor.apply();
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
        saveLevelCompletion("LVL2");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerg != null) {
            mediaPlayerg.release();
            mediaPlayerg = null;
        }
        if (starshipAnimation != null) {
            starshipAnimation.stop();
        }
        if (backgroundAnimation != null) {
            backgroundAnimation.stop();
            backgroundAnimation = null;
        }
        if (coinAnimation != null) {
            coinAnimation.stop();
            coinAnimation = null;
        }
        if (obstacleAnimation != null) {
            obstacleAnimation.stop();
            obstacleAnimation = null;
        }
    }

    private Runnable checkCollisionRunnable = new Runnable() {
        @Override
        public void run() {
            if (coin != null) {
                checkCollisionWithCoin(coin);
            }
            coinGenerationHandler.postDelayed(this, 10);
        }
    };

    public void startCollisionCheck() {
        coinGenerationHandler.postDelayed(checkCollisionRunnable, 10);
    }

    public void stopCollisionCheck() {
        coinGenerationHandler.removeCallbacks(checkCollisionRunnable);
    }

    private void initMediaPlayers() {
        mediaPlayera = MediaPlayer.create(this, R.raw.pauseandbacksound);
        mediaPlayerud = MediaPlayer.create(this, R.raw.upanddownbuttonsound);
        mediaPlayere = MediaPlayer.create(this, R.raw.crashsound);
        mediaPlayerc = MediaPlayer.create(this, R.raw.coinsound);
        mediaPlayerg = MediaPlayer.create(this, R.raw.lvl2music);
        mediaPlayerf = MediaPlayer.create(this, R.raw.failsound);
        mediaPlayerw = MediaPlayer.create(this, R.raw.winsound);
        mediaPlayerac = MediaPlayer.create(this, R.raw.acceleratorsound);
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
                if (moveCounter > winscore) {
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
        obstacle.setBackgroundResource(R.drawable.obstacle);
        obstacleAnimation = (AnimationDrawable) obstacle.getBackground();
        obstacleAnimation.setOneShot(false);
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
            if (isShieldActive) {
                explodeAnimation(obstacle);
                relativeLayout.removeView(obstacle);
            } else {
                gameOver();
                explodeAnimation(carImage);
                mediaPlayere.start();
            }
        }
    }

    private void explodeAnimation(ImageView view) {
        AnimationDrawable explodeAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.explosion_animation);
        view.setBackground(explodeAnimation);
        explodeAnimation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                explodeAnimation.stop();
                view.setBackground(null);
            }
        }, 2000);
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
            moveCounterHandler.removeCallbacksAndMessages(null);
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
            coinGenerationHandler = new Handler();
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
            relativeLayout.addView(coin);
            coin.setImageResource(R.drawable.coin_animation);
            coinAnimation = (AnimationDrawable) coin.getDrawable();
            coinAnimation.setOneShot(false);
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
                .translationX(-relativeLayout.getWidth() - coin.getWidth())
                .setDuration(3000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        stopCollisionCheck();
                        checkCollisionWithCoin(coin);
                    }
                })
                .start();
    }

    private void checkCollisionWithCoin(ImageView coin) {
        if (isColliding(carImage, coin) && relativeLayout.indexOfChild(coin) != -1) {
            incrementCoinCounter(5);
            mediaPlayerc.start();
            relativeLayout.removeView(coin);
        }
    }

    private void incrementCoinCounter(int i) {
        coinCounter++;
        updateCoinCounter(coinCounter);
    }

    private void updateCoinCounter(int coinCounter) {
        coinCounterTextView.setText(String.valueOf(coinCounter));
    }

    // Метод для создания выстрела
    private void createShot() {
        if (!isGameOver && canShoot) {
            canShoot = false;
            ImageView shot = new ImageView(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.topMargin = (int) carImage.getY();
            shot.setLayoutParams(params);
            shot.setImageResource(R.drawable.shoti);
            relativeLayout.addView(shot);

            animateShot(shot);
            reloadShot();
        }
    }

    // Метод для анимации выстрела
    private void animateShot(final ImageView shot) {
        shot.animate()
                .translationXBy(relativeLayout.getWidth())
                .setDuration(1000)
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        checkShotCollisionWithObstacle(shot);
                    }
                })
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        relativeLayout.removeView(shot);
                    }
                })
                .start();
    }

    // Метод для перезарядки выстрела
    private void reloadShot() {
        shotHandler = new Handler();
        shotRunnable = new Runnable() {
            @Override
            public void run() {
                canShoot = true;
            }
        };
        shotHandler.postDelayed(shotRunnable, 3000);
    }

    // Метод для проверки столкновения выстрела с препятствием
    private void checkShotCollisionWithObstacle(ImageView shot) {
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            View view = relativeLayout.getChildAt(i);
            if (view instanceof ImageView && view.getBackground() instanceof AnimationDrawable) {
                AnimationDrawable animation = (AnimationDrawable) view.getBackground();
                if (animation == obstacleAnimation && isColliding(shot, (ImageView) view)) {
                    explodeAnimation((ImageView) view);
                    relativeLayout.removeView(view);
                    incrementCoinCounter(5);
                }
            }
        }
    }

    // Метод для создания выстрела из препятствия
    private void createObstacleShot(ImageView obstacle) {
        if (!isGameOver && new Random().nextDouble() < 0.66) {
            ImageView shot = new ImageView(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.topMargin = (int) obstacle.getY();
            shot.setLayoutParams(params);
            shot.setImageResource(R.drawable.shoti);
            relativeLayout.addView(shot);

            animateObstacleShot(shot);
        }
    }

    // Метод для анимации выстрела из препятствия
    private void animateObstacleShot(final ImageView shot) {
        shot.animate()
                .translationXBy(-relativeLayout.getWidth())
                .setDuration(1000)
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        checkShotCollisionWithCar(shot);
                    }
                })
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        relativeLayout.removeView(shot);
                    }
                })
                .start();
    }

    // Метод для проверки столкновения выстрела из препятствия с carImage
    private void checkShotCollisionWithCar(ImageView shot) {
        if (isColliding(shot, carImage)) {
            gameOver();
            explodeAnimation(carImage);
            mediaPlayere.start();
        }
    }
}