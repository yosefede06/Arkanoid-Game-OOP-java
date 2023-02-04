package brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.awt.*;

/**
 * General type for brick strategies. All brick strategies extends this class.
 * Factory class for creating Collision strategies.
 */
public class StrategyFactory {
    private GameObjectCollection gameObjects;
    private WindowController windowController;
    private BrickerGameManager brickerGameManager;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private Vector2 windowDimensions;
    private Counter livesCounter;
    private SoundReader soundReader;
    private CollisionStrategy heartStrategy;
    private CollisionStrategy puckStrategy;
    private CollisionStrategy cameraStrategy;
    private CollisionStrategy additionalPaddleStrategy;
    private CollisionStrategy doubleStrategy;
    private CollisionStrategy collisionStrategy;

    /**
     * Constructor of the class.
     * @param gameObjects
     * @param windowController
     * @param brickerGameManager
     * @param inputListener
     * @param imageReader
     * @param windowDimensions
     * @param livesCounter
     * @param soundReader
     */
    public StrategyFactory(GameObjectCollection gameObjects, WindowController windowController, BrickerGameManager
            brickerGameManager, UserInputListener inputListener, ImageReader imageReader, Vector2 windowDimensions,
                           Counter livesCounter, SoundReader soundReader) {
        this.gameObjects = gameObjects;
        this.windowController = windowController;
        this.brickerGameManager = brickerGameManager;
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        this.livesCounter = livesCounter;
        this.soundReader = soundReader;
        heartStrategy = new HeartStrategy(gameObjects, imageReader, windowDimensions, livesCounter);
        puckStrategy = new PuckStrategy(gameObjects, imageReader, soundReader, windowDimensions);
        cameraStrategy = new CameraStrategy(gameObjects, windowController, brickerGameManager);
        additionalPaddleStrategy = new AdditionalPaddleStrategy(gameObjects, imageReader, inputListener,
                windowDimensions);
        doubleStrategy = new DoubleStrategy(gameObjects, this);
        collisionStrategy = new CollisionStrategy(gameObjects);
    }

    /**
     * Returns a new strategy according to the number received from 0-5.
     * @param i
     * @return
     */
    public CollisionStrategy getStrategy(int i) {
        return switch (i) {
            case 0 -> heartStrategy;
            case 1 -> puckStrategy;
            case 2 -> cameraStrategy;
            case 3 -> additionalPaddleStrategy;
            case 4 -> doubleStrategy;
            default -> collisionStrategy;
        };
    }


}