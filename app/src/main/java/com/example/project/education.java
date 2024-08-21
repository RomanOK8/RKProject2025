package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class education extends AppCompatActivity {
    private AnimationDrawable starshipAnimation;
    private AnimationDrawable coinAnimation;
    private AnimationDrawable obstacleAnimation;
    private MediaPlayer mediaPlayerac;
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
    private Color gameOverColor;
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
    private long lastClickTime = 0;
    private float initialX;
    private int moveCounter = 0;
    private boolean moveCarFastc=false;
    private int winscore=199;

    private SharedPreferences sharedPreferences;

    private boolean allDialogsShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ImageView img = (ImageView) findViewById(R.id.swing_play);
        img.setBackgroundResource(R.drawable.lvl2_background);
        ImageView carImage = findViewById(R.id.CarImage);
        carImage.setBackgroundResource(R.drawable.starship_animation);
        starshipAnimation = (AnimationDrawable) carImage.getBackground();
        starshipAnimation.setOneShot(false);
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


        View pauseButton = findViewById(R.id.PauseButtonlvl2);
        View upButton = findViewById(R.id.UpButtonlvl1);
        View downButton = findViewById(R.id.downbutton);
        View acceleratorButton = findViewById(R.id.accelerator);
        View retryButton = findViewById(R.id.retryButton);

        setTouchListenerForButton(pauseButton, () -> pauseButton(pauseButton));
        setTouchListenerForButton(upButton, () -> upButton(upButton));
        setTouchListenerForButton(downButton, () -> downButton(downButton));
        setTouchListenerForButton(acceleratorButton, () -> acceleratorButton (accelerator));
        setTouchListenerForButton(retryButton, () -> restartLevel());

        showInitialDialog();

        startObstacleCreation();
        startMoveCounter();
        startCoinCreation();
        mediaPlayerg.start();

        // Проверка, была ли уже показана Тиан
        if (moveCounter >= 100 && !sharedPreferences.getBoolean("tyanIntroduced", false)) {
            showTyanIntroduction();
            sharedPreferences.edit().putBoolean("tyanIntroduced", true).apply();
        }
    }

    private void showInitialDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Предистория")
                .setMessage("Добро пожаловать в игру! Ты играешь за пилота космического корабля, который должен избегать препятствия и собирать монеты. Тян будет помогать тебе разобраться в геймплее. Удачи!")
                .setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showExplanationDialog(String buttonName, String message) {
        new AlertDialog.Builder(this)
                .setTitle("Объяснение для " + buttonName)
                .setMessage(message)
                .setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkAllDialogsShown();
                    }
                })
                .show();
    }

    private void checkAllDialogsShown() {
        if (sharedPreferences.getBoolean("acceleratorExplained", false) &&
                sharedPreferences.getBoolean("pauseExplained", false) &&
                sharedPreferences.getBoolean("upExplained", false) &&
                sharedPreferences.getBoolean("downExplained", false)) {
            allDialogsShown = true;
        }
    }

    private void showTyanIntroduction() {
        ImageView tyanImage = new ImageView(this);
        tyanImage.setImageResource(R.drawable.tyan);
        RelativeLayout.LayoutParams tyanParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tyanParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        tyanParams.topMargin = 100;
        tyanImage.setLayoutParams(tyanParams);
        relativeLayout.addView(tyanImage);

        TextView tyanText = new TextView(this);
        tyanText.setText("Привет! Я Тиан. Мы должны победить Железного Рино. Удачи!");
        tyanText.setTextColor(Color.WHITE);
        tyanText.setBackgroundColor(Color.argb(150, 0, 0, 0));
        tyanText.setPadding(20, 10, 20, 10);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        textParams.topMargin = 300;
        tyanText.setLayoutParams(textParams);
        relativeLayout.addView(tyanText);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);
        tyanImage.startAnimation(fadeIn);
        tyanText.startAnimation(fadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                fadeOut.setDuration(1000);
                fadeOut.setFillAfter(true);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        relativeLayout.removeView(tyanImage);
                        relativeLayout.removeView(tyanText);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                tyanImage.startAnimation(fadeOut);
                tyanText.startAnimation(fadeOut);
            }
        }, 5000);
    }

    public void acceleratorButton(View view) {
        if (!sharedPreferences.getBoolean("acceleratorExplained", false)) {
            showExplanationDialog("Акселератора", "Эта кнопка ускоряет твой корабль на короткое время. Используй её с умом!");
            sharedPreferences.edit().putBoolean("acceleratorExplained", true).apply();
        }

        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime >= 10000) { // Разрешаем нажать раз в =10 секунд
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
            int newCount = currentCount + 12;
            moveCounterTextView.setText(String.valueOf(newCount));
        }

        // Проверка на достижение 100 очков
        if (moveCounter >= 100 && !sharedPreferences.getBoolean("tyanIntroduced", false)) {
            showTyanIntroduction();
            sharedPreferences.edit().putBoolean("tyanIntroduced", true).apply();
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
                            // Возвращаем ImageView на исходную позицию
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
        saveLevelCompletion("LVLT");

        showFinalDialog();
    }

    private void showFinalDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Поздравляем!")
                .setMessage("Ты успешно завершил уровень! Удачи в следующих испытаниях!")
                .setPositiveButton("Спасибо", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
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
        }
        if (coinAnimation != null) {
            coinAnimation.stop();
        }
        if (obstacleAnimation != null) {
            obstacleAnimation.stop();
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
        mediaPlayere=MediaPlayer.create(this, R.raw.crashsound);
        mediaPlayerc=MediaPlayer.create(this, R.raw.coinsound);
        mediaPlayerg=MediaPlayer.create(this, R.raw.lvl2music);
        mediaPlayerf=MediaPlayer.create(this, R.raw.failsound);
        mediaPlayerw=MediaPlayer.create(this, R.raw.winsound);
        mediaPlayerac=MediaPlayer.create(this, R.raw.acceleratorsound);
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
                obstacleHandler.postDelayed(this, 6000);
            }
        };
        obstacleHandler.postDelayed(createObstacleRunnable, 6000);
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
            gameOver();
            explodeAnimation(carImage);
            mediaPlayere.start();
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
        if (!sharedPreferences.getBoolean("pauseExplained", false)) {
            showExplanationDialog("Паузы", "Эта кнопка приостанавливает игру. Ты сможешь вернуться к игре, когда будешь готов.");
            sharedPreferences.edit().putBoolean("pauseExplained", true).apply();
        }

        Intent intent = new Intent(this, Pausemenu.class);
        intent.putExtra("levelClass", education.class.getName());
        startActivity(intent);
        mediaPlayera.start();
        mediaPlayerg.stop();
    }

    public void upButton(View v) {
        if (!sharedPreferences.getBoolean("upExplained", false)) {
            showExplanationDialog("Вверх", "Эта кнопка поднимает твой корабль вверх. Используй её, чтобы избегать препятствия.");
            sharedPreferences.edit().putBoolean("upExplained", true).apply();
        }

        moveCar(-100);
    }

    public void downButton(View v) {
        if (!sharedPreferences.getBoolean("downExplained", false)) {
            showExplanationDialog("Вниз", "Эта кнопка опускает твой корабль вниз. Используй её, чтобы избегать препятствия.");
            sharedPreferences.edit().putBoolean("downExplained", true).apply();
        }

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
                    coinGenerationHandler.postDelayed(this, 10500);
                }
            };
            coinGenerationHandler.postDelayed(createCoinRunnable, 10500);
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
                    public void onAnimationEnd(android.animation.Animator animation) {
                        stopCollisionCheck();
                        checkCollisionWithCoin(coin);
                    }
                })
                .start();
    }

    private void checkCollisionWithCoin(ImageView coin) {
        if (isColliding(carImage, coin) && relativeLayout.indexOfChild(coin) != -1) {
            incrementCoinCounter();
            mediaPlayerc.start();
            relativeLayout.removeView(coin);
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