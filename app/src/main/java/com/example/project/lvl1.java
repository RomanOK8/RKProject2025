package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;//.для анимаций
import android.animation.AnimatorListenerAdapter;//для анимаций
import android.animation.ValueAnimator;//для анимаций
import android.content.Intent;//для перехода в дургие активности
import android.graphics.Color;//для измение цвете в мтеоде gameWin
import android.graphics.Rect;//тоже для анимаций
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;//для медиаплееров
import android.os.Bundle;//
import android.os.Handler;//для генерации препятсвия и монетки
import android.util.DisplayMetrics;//для размеров экрана
import android.view.MotionEvent;//для полупрозранчости кнопок
import android.view.View;//для view
import android.widget.Button;//для кнопки retry
import android.widget.ImageButton;//для кнопок управления и кнопки паузы
import android.widget.ImageView;//для фона, корабля, препятсвия и монетки
import android.widget.RelativeLayout;
import android.widget.TextView;//для счетчиков,

import java.util.Random;

public class lvl1 extends AppCompatActivity {
    private AnimationDrawable starshipAnimation, coinAnimation, obstacleAnimation, backgroundAnimation;//все анимации в активности
    private MediaPlayer mediaPlayerac,mediaPlayerw, mediaPlayerf, mediaPlayerg, mediaPlayerc,mediaPlayere,mediaPlayera,  mediaPlayerud ;//тупо все звуки в активности
    private ImageView carImage, coin;//анимации монетки и машинки
    private TextView moveCounterTextView, gameOverTextView, coinCounterTextView;//все счетчики и текст для методов gameWin и GameOver
    private float screenHeight,initialX;//переменные для ограничения по x и y(чтобы игрок не вылетал за пределы экрана)
    private Handler moveCounterHandler, obstacleHandler, coinGenerationHandler;
    private Runnable createObstacleRunnable;//для генерации препятсвий
    private Button retryButton;//кнопка перезапуска уровня
    private boolean isGameOver = false, moveCarFastc=false;//проверка на метод GameOver и moveCarFast
    private RelativeLayout relativeLayout;//для анимаций фона, корабля и монеток
    private long lastClickTime = 0;//хранение ласт клика
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
        backgroundAnimation = (AnimationDrawable) img.getBackground();//
        backgroundAnimation.setOneShot(false);
        backgroundAnimation.start();

        ImageView coin=findViewById(R.id.coin1);
        coin.setBackgroundResource(R.drawable.coin_animation);
        coinAnimation = (AnimationDrawable) coin.getBackground();
        coinAnimation.setOneShot(false);
        coinAnimation.start();

        ImageView obstacle2=findViewById(R.id.obstacle);//анологично как у заднего фона
        obstacle2.setBackgroundResource(R.drawable.obstacle);
        obstacleAnimation=(AnimationDrawable) coin.getBackground();
        obstacleAnimation.setOneShot(false);
        obstacleAnimation.start();
        initMediaPlayers();//вызов всех медиаплееров
        initViews();//вызов всех Image,Text и.т.д views
        mediaPlayerg.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Воспроизведение заново, когда трек заканчивается(фоновая музыка)
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
    public void acceleratorButton(View view) {//метод для "ускорения"
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime >= 10000) { // ускорение нажимается раз в 10 сек
            lastClickTime = clickTime;
            incrementMoveCounter();
            moveCarFast();
        }
    }
    private void setTouchListenerForButton(final View button, final Runnable action) {//метод для реалезации полупрозначности при нажатии на кнопку
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {//если пользователь нажал, то кнопка становится полупрозрачной
                    button.setAlpha(0.5f);
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {//анологично, если пользователь отпустил кнопку, то она становится такой же
                    button.setAlpha(1.0f);
                    action.run();
                }
                return true;
            }
        });
    }
    private void updateMoveCounter(int moveCounter) {//обновление счетчика перемещения
        moveCounterTextView.setText(String.valueOf(moveCounter));
        if(moveCarFastc){
            int currentCount = Integer.parseInt(moveCounterTextView.getText().toString());
            // Увеличиваем его на 25(да да +12=25 без вопросов(без понятия как и почему))
            int newCount = currentCount + 12;
            moveCounterTextView.setText(String.valueOf(newCount));
        }
    }
    private void incrementMoveCounter() {//метод для прибавления 25 очков при нажатии на кнопку ускорения
        if (!isGameOver) {
            int currentCount = Integer.parseInt(moveCounterTextView.getText().toString());
            // + 25
            int newCount = currentCount + 12;
            moveCounter += 12; // +25
            updateMoveCounter(moveCounter); // обновляем textview
        }
    }
    private void moveCarFast() {//анимация ускорения+возвращение назад
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
    private void gameWin() {//мтеод выигрыша (различия с gameOver только в надписи и ее цвете и звуке)
        isGameOver = true;//присваиваем isGameOver true(останавливая методы для генерации всего)
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
        gameOverTextView.setText("WIN");//текст меняется с GameOver на win
        gameOverTextView.setTextColor(Color.GREEN);//с красного на зеленый
        retryButton.setVisibility(View.VISIBLE);
        mediaPlayerg.stop();
        mediaPlayerw.start();//звук выигрыша
    }
    @Override
    protected void onDestroy() {//думаю очев
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
    private Runnable checkCollisionRunnable = new Runnable() {//метод для проверки столкновения с монеткой
        @Override
        public void run() {
            if (coin != null) {
                checkCollisionWithCoin(coin);//проверка столкновения
            }
            coinGenerationHandler.postDelayed(this, 10);//следующая проверка через 10 миллисек
        }
    };
    public void startCollisionCheck() {//проверка каждые 10 миллисекунд на столкновение с монетками
        coinGenerationHandler.postDelayed(checkCollisionRunnable, 10);
    }
    public void stopCollisionCheck() {//метод для отсановки проверки столкновений с монетками(после gameWin или GameOver)
        coinGenerationHandler.removeCallbacks(checkCollisionRunnable);
    }
    private void initMediaPlayers() {//вызов медиаплееров
        mediaPlayera = MediaPlayer.create(this, R.raw.pauseandbacksound);//звук кнопки паузы
        mediaPlayerud = MediaPlayer.create(this, R.raw.upanddownbuttonsound);//звук кнопок вверх и вниз
        mediaPlayere=MediaPlayer.create(this, R.raw.crashsound);//звук столкновения
        mediaPlayerc=MediaPlayer.create(this, R.raw.coinsound);//звук подбирания монетки
        mediaPlayerg=MediaPlayer.create(this, R.raw.lvl1);//музыка уровня
        mediaPlayerf=MediaPlayer.create(this, R.raw.failsound);//звук проигрыша
        mediaPlayerw=MediaPlayer.create(this, R.raw.winsound);//звук выигрыша
        mediaPlayerac=MediaPlayer.create(this, R.raw.acceleratorsound);//звук ускорения
    }

    private void initViews() {//вызов всех анимаций, счетчиков + высота и ширина экрана
        carImage = findViewById(R.id.CarImage);
        moveCounterTextView = findViewById(R.id.moveCounter);
        gameOverTextView = findViewById(R.id.gameOverTextView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
    }

    private void startObstacleCreation() {//метод для генерации препятсвий
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

    private void startMoveCounter() {//метод для старта счетчика
        moveCounterHandler = new Handler();
        final Runnable moveCounterRunnable = new Runnable() {
            @Override
            public void run() {
                moveCounter++;
                updateMoveCounter(moveCounter);
                if (moveCounter >winscore) {
                    gameWin();
                } else {
                    moveCounterHandler.postDelayed(this, 250);//прибавляем каждые 0,25 сек по 1 очку
                }
            }
        };
        moveCounterHandler.postDelayed(moveCounterRunnable, 250);
    }

    private void createObstacle() {//метод для генерации препятсвий
        ImageView obstacle = new ImageView(this);
        RelativeLayout.LayoutParams params = createObstacleLayoutParams();
        obstacle.setLayoutParams(params);
        relativeLayout.addView(obstacle);
        obstacle.setBackgroundResource(R.drawable.obstacle);//установка анимации
        obstacleAnimation = (AnimationDrawable) obstacle.getBackground();
        obstacleAnimation.setOneShot(false); //флаг для бесконечной анимации
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

    private int getRandomYPosition() {//метод для расположения препятсвия и монетки в рандомной позиции по y
        int minY = 0;//очев что имнимальный y=0
        int maxY = (int) (screenHeight - carImage.getHeight());//максимальный исходя из высоты экран-положение машинки
        Random random = new Random();
        return random.nextInt(maxY - minY) + minY;
    }

    private void animateObstacle(ImageView obstacle, RelativeLayout relativeLayout) {//метод для анимации препятсвия
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
        // загружаем анимацию взрыва
        AnimationDrawable explodeAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.explosion_animation);
        // устанавливаем анимацию как фон для view
        view.setBackground(explodeAnimation);
        // запускаем анимацию
        explodeAnimation.start();

        // останавливаем анимацию через 2 ссекунды
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Останавливаем анимацию
                explodeAnimation.stop();
                // Сбрасываем фон, чтобы удалить анимацию
                view.setBackground(null);
            }
        }, 2000);
    }


    private boolean isColliding(ImageView imageView1, ImageView imageView2) {//проверка пересечения двух анимаций(немного некорректно работает, т.к пнг квадратная)
        Rect rect1 = new Rect();
        imageView1.getGlobalVisibleRect(rect1);
        Rect rect2 = new Rect();
        imageView2.getGlobalVisibleRect(rect2);
        return Rect.intersects(rect1, rect2);
    }

    private void gameOver() {//метод проигрыша
        isGameOver = true;//присваиваем isGameOver значение true (тем самым останавляиая другие методы)
        obstacleHandler.removeCallbacks(createObstacleRunnable);//остановка генерации препятсвий
        coinGenerationHandler.removeCallbacks(createObstacleRunnable);//остановка генерации монеток
        if (moveCounterHandler != null) {
            moveCounterHandler.removeCallbacksAndMessages(null); //остановка счетчиков
        }
        if (backgroundAnimation != null) {//остановка фона
            backgroundAnimation.stop();
        }
        carImage.clearAnimation();//уничтожение анимации корабля
        gameOverTextView.setVisibility(View.VISIBLE);
        gameOverTextView.setText("Game Over");
        retryButton.setVisibility(View.VISIBLE);
        mediaPlayerg.stop();//остановка фонового звука игры
        mediaPlayerf.start();//звук проигрыша стартует
    }
    private void restartLevel() {
        // перезапуск активности в случае выйгрыша/поражения
        finish();
        startActivity(getIntent());
    }


    public void pauseButton(View v) {//кнопка паузы
        Intent intent = new Intent(this, Pausemenu.class);
        intent.putExtra("levelClass", lvl1.class.getName());//передаем имя класса из которого мы идем в pausmenu
        startActivity(intent);
        mediaPlayera.start();
        mediaPlayerg.stop();
    }

    public void upButton(View v) {
        moveCar(-100);
    }//метод для движения вверх

    public void downButton(View v) {
        moveCar(100);
    }//мтеод для движения вниз

    private void moveCar(int distance) {//логика движений
        if (!isGameOver && isWithinBounds(carImage.getY() + distance)) {//проверка на заступ экрана(чтобы не вылетить за него)
            carImage.animate().translationYBy(distance).setDuration(100);//перемещение анимации корабля
            mediaPlayerud.start();//звук кнопок для премещения
        }
    }

    private boolean isWithinBounds(float newY) {//метод для првоерки заступа за экран
        return newY > 0 && newY < screenHeight - carImage.getHeight();
    }



    private void startCoinCreation() {//метод для генерации монеток
        if (!isGameOver) {
            Handler coinGenerationHandler = new Handler();
            Runnable createCoinRunnable = new Runnable() {
                @Override
                public void run() {
                    createCoin();
                    coinGenerationHandler.postDelayed(this, 10000);//каждые 10 сек
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
            relativeLayout.addView(coin); //добавляем монету в relativeLayout
            coin.setImageResource(R.drawable.coin_animation);//анмиация монетки
            coinAnimation = (AnimationDrawable) coin.getDrawable();
            coinAnimation.setOneShot(false); //флаг для бесконечной анимации
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
    private void animateCoin(final ImageView coin) {//метод для длительности анимаций
        coin.animate()
                .translationX(-relativeLayout.getWidth() - coin.getWidth()) //конечная позиция за пределами экрана
                .setDuration(3000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        stopCollisionCheck();
                        checkCollisionWithCoin(coin); //проверка столкновение при окончании анимации
                    }
                })
                .start();
    }

    private void checkCollisionWithCoin(ImageView coin) {
        if (isColliding(carImage, coin) && relativeLayout.indexOfChild(coin) != -1) {
            incrementCoinCounter();
            mediaPlayerc.start();
            relativeLayout.removeView(coin); // удаляем монету только при соприкосновении и если она еще есть в relativeLayout
        }
    }

    private void incrementCoinCounter() {//метод увелечения счетчика монеток
        coinCounter++;
        updateCoinCounter(coinCounter);
    }

    private void updateCoinCounter(int coinCounter) {// метод обновления счетчика монетока
        coinCounterTextView.setText(String.valueOf(coinCounter));
    }

}