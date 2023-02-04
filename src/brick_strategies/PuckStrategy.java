package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Ball;

import java.util.Random;

/**
 * Class extending CollisionStrategy. Introduces several pucks instead of brick once removed.
 */
public class PuckStrategy extends CollisionStrategy {
    private static final int NUMBER_OF_PUCK_BALLS = 3;
    private static final int BALL_SPEED = 200;
//    private Ball[] pucks;
    private Ball puck;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private Vector2 windowDimensions;
    Renderable ballImage;
    Sound collisionSound;

    /**
     * Constructor of the class.
     * @param gameObjects
     * @param imageReader
     * @param soundReader
     * @param windowDimensions
     */
    public PuckStrategy(GameObjectCollection gameObjects, ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions) {
        super(gameObjects);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowDimensions = windowDimensions;
        ballImage = imageReader.readImage("assets/mockBall.png", true);
        collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
//        pucks = new Ball[NUMBER_OF_PUCK_BALLS];
    }

    /**
     * Removes the brick by colling same method on  CollisionStrategy and introduces several pucks instead of brick.
     * @param collidedObject Collided object.
     * @param colliderObj Object that collides.
     * @param bricksCounter Counter of bricks removed from the game.
     */
    @Override
    public void onCollision(GameObject collidedObject, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObject, colliderObj, bricksCounter);
        innerOnCollision(collidedObject, colliderObj, bricksCounter);
    }

    /**
     * Handles the power of the strategy behaviour.
     * @param collidedObject Collided object.
     * @param colliderObj Object that collides.
     * @param bricksCounter Counter of bricks removed from the game.
     */
    @Override
    public void innerOnCollision(GameObject collidedObject, GameObject colliderObj, Counter bricksCounter) {
        for(int i = 0; i < NUMBER_OF_PUCK_BALLS; i++) {
            puck = new Ball(collidedObject.getCenter(),
                    new Vector2(collidedObject.getDimensions().x() / 3,
                    collidedObject.getDimensions().x()/ 3),
                    ballImage,
                    collisionSound);
            setInitialBallSpeedDirection(puck);
            this.gameObjects.addGameObject(puck);
        }
    }

    /**
     * Update speed direction of the balls.
     * @param ball
     */
    private void setInitialBallSpeedDirection(Ball ball) {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()) {ballVelX *= -1;}
        if(rand.nextBoolean()) {ballVelY *= -1;}
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }
}
