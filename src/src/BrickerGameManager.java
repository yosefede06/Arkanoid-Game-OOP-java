package src;

import brick_strategies.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * This class inherits from GameManager, and is the main class in the game.
 * This class is responsible for game initialization, holding references for game objects and
 * calling update methods for every update iteration. Entry point for code should be in a main method in this class.
 */
public class BrickerGameManager extends GameManager {
    private static final Random random = new Random();
    private static final int TARGET_FRAME = 40;
    private static final int BORDER_WIDTH = 10;
    private static final int STRATEGIES_NUMBER = 6;
    private static final int BORDER_HEIGHT = 30;
    private StrategyFactory strategyFactory;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int BALL_RADIUS = 20;
    private static final int BALL_SPEED = 200;
    private static final Color BORDER_COLOR = new Color(28,107,151,255);
    private static final int HEART_MARGIN_Y = 30;
    private static final int HEART_RADIUS = 20;
    private static final int MAX_LIVES = 3;
    private static final int PADDLE_MARGIN = 30;
    private static final int NUMBER_LIVES_MARGIN_X = 25;
    private static final int NUMBER_LIVES_MARGIN_Y = 60;
    private static final int MARGIN_BRICK_BORDER = 20;
    private static final int BRICK_PADDING = 3;
    private static final int BRICKS_PER_ROW = 7;
    private static final int COLS_OF_BRICKS = 8;
    private static final int BRICK_HEIGHT = 10;


    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;


    private Counter livesCounter;
    private Counter bricksCounter;
    ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;

    /**
     * Constructor of the class.
     * @param windowTitle
     * @param windowDimensions
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }


    /**
     * The function initalizeGame which initializes all the objects necessary for the game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        bricksCounter = new Counter(COLS_OF_BRICKS * BRICKS_PER_ROW);
        livesCounter = new Counter(MAX_LIVES);
        this.imageReader = imageReader;
        this.windowController = windowController;


        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(TARGET_FRAME);
        windowDimensions = windowController.getWindowDimensions();
        // createBackground
        createBackground(imageReader);
        // create ball
        createBall(imageReader, soundReader);
        //create paddle
        createPaddle(imageReader, inputListener);
        //create borders
        createBorders();
        //Initialize strategy factory
        InitializeStrategyFactory();
        //create bricks
        createBricks(imageReader);
        //create graphical life counter
        createGraphicalLifeCounter(imageReader);
        //create numerical life counter
        createNumericLifeCounter();
    }

    /**
     * Initializes the strategy factory.
     */
    private void InitializeStrategyFactory() {
        strategyFactory = new StrategyFactory(gameObjects(),
                windowController,
                this,
                inputListener,
                imageReader,
                windowDimensions,
                livesCounter,
                soundReader);
    }

    /**
     * Set the background of the game.
     * @param imageReader ImageReader instance.
     */
    private void createBackground(ImageReader imageReader) {
        GameObject backGround = new GameObject(Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage("assets/DARK_BG2_small.jpeg",
                        false
                )
        );
        gameObjects().addGameObject(backGround, Layer.BACKGROUND);
        backGround.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(backGround, Layer.BACKGROUND);
    }

    /**
     * Override the game manager update method to check if the game as finished or the ball has touched the ground.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    /**
     * Checks for the end of the game.
     */
    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if(this.bricksCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = "You win!";
        }
        if(ballHeight > windowDimensions.y()) {
            // one life lost
            livesCounter.decrement();
            ball.setCenter(this.getCenterWindow());
            setInitialBallSpeedDirection();
            if (this.livesCounter.value() <= 0) {
                prompt = "You Lose!";
            }
        }
        if(!prompt.isEmpty()) {
            prompt += " Play again?";
            if(windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * Changes the main ball speed.
     */
    private void setInitialBallSpeedDirection() {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()) {ballVelX *= -1;}
        if(rand.nextBoolean()) {ballVelY *= -1;}
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Returns the center of the window.
     * @return
     */
    private Vector2 getCenterWindow() {
        return windowDimensions.mult((float)0.5);
    }

    /**
     * Create the main ball of the game.
     * @param imageReader
     * @param soundReader
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(getCenterWindow(), new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        this.setInitialBallSpeedDirection();
        gameObjects().addGameObject(ball);
    }

    /**
     * Create the user paddle.
     * @param imageReader
     * @param inputListener
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);
        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage, inputListener, windowDimensions, BORDER_WIDTH);
        userPaddle.setCenter(new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - PADDLE_MARGIN));
        gameObjects().addGameObject(userPaddle);
    }

    /**
     * Add new bricks with the respective strategy.
     * @param imageReader
     */
    private void createBricks(ImageReader imageReader) {
        AdditionalPaddleStrategy additionalPaddleStrategy = new AdditionalPaddleStrategy(gameObjects(), imageReader, inputListener, windowDimensions);
        CameraStrategy cameraStrategy = new CameraStrategy(gameObjects(), windowController, this);
        HeartStrategy heartStrategy = new HeartStrategy(gameObjects(), imageReader, windowDimensions, livesCounter);
        CollisionStrategy basicStrategy = new CollisionStrategy(gameObjects());
        PuckStrategy puckStrategy = new PuckStrategy(gameObjects(), imageReader, soundReader, windowDimensions);
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        float brickWidth = this.getBrickWidth();
        for (int row = 0; row < BRICKS_PER_ROW ; row++) {
            for (int col = 0; col < COLS_OF_BRICKS ; col++) {
                gameObjects().addGameObject(new Brick(
                        new Vector2(MARGIN_BRICK_BORDER + BORDER_WIDTH + col * (brickWidth + BRICK_PADDING),
                                BORDER_HEIGHT + row * (BRICK_HEIGHT + BRICK_PADDING)),
                        new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage,
                        strategyFactory.getStrategy(random.nextInt(STRATEGIES_NUMBER)),
                        this.bricksCounter));
            }
        }
    }

    /**
     * Public getter method that returns the main ball of the game.
     * @return
     */
    public Ball getMainBall() {
        return ball;
    }

    /**
     * Return the brick width.
     * @return brick width.
     */
    private float getBrickWidth() {
        return (windowDimensions.x() -
                ((COLS_OF_BRICKS - 1) * BRICK_PADDING + 2 * (BORDER_WIDTH + MARGIN_BRICK_BORDER))) / COLS_OF_BRICKS;
    }

    /**
     * Create the borders of the game.
     */
    private void createBorders() {
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        new RectangleRenderable(BORDER_COLOR))
        );
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        new RectangleRenderable(BORDER_COLOR))
        );
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),
                        new RectangleRenderable(BORDER_COLOR))
        );
    }

    /**
     * Graphical lives counter
     * @param imageReader ImagerRader instance.
     */
    private void createGraphicalLifeCounter(ImageReader imageReader){
        gameObjects().addGameObject(
                new GraphicLifeCounter(
                        new Vector2(0, windowDimensions.y() - HEART_MARGIN_Y),
                        new Vector2(HEART_RADIUS, HEART_RADIUS),
                        livesCounter,
                        imageReader.readImage("assets/heart.png", true),
                        gameObjects(),
                        MAX_LIVES), Layer.BACKGROUND
        );
    }

    /**
     * Create numeric life counter.
     */
    private void createNumericLifeCounter() {
        gameObjects().addGameObject(
            new NumericLifeCounter(
                    livesCounter,
                    new Vector2(NUMBER_LIVES_MARGIN_X, windowDimensions.y() - NUMBER_LIVES_MARGIN_Y),
                    new Vector2(HEART_RADIUS, HEART_RADIUS),
                    gameObjects()
            ), Layer.BACKGROUND
        );
    }

    /**
     * Main static function.
     * @param args
     */
    public static void main(String[] args) {
        new BrickerGameManager("Bricker", new Vector2(700, 500)).run();
    }
}