package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.HeartMovement;

import java.awt.event.KeyEvent;

/**
 * Class extending CollisionStrategy.
 * Throws heart to ground and if the paddle pick the heart it will add a new live to the player.
 */
public class HeartStrategy extends CollisionStrategy{
    private final int HEART_RADIUS = 20;
    private final int HEART_SPEED = 100;
    private ImageReader imageReader;
    private Vector2 windowDimensions;
    private Counter livesCounter;


    /**
     * Constructor of the class.
     * @param gameObjects
     * @param imageReader
     * @param windowDimensions
     * @param livesCounter
     */
    public HeartStrategy(GameObjectCollection gameObjects, ImageReader imageReader, Vector2 windowDimensions, Counter livesCounter) {
        super(gameObjects);
        this.imageReader = imageReader;
        this.windowDimensions = windowDimensions;
        this.livesCounter = livesCounter;
    }

    /**
     *
     * @param initPosition
     */
    private void launchHeart(Vector2 initPosition) {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        GameObject heart = new HeartMovement(initPosition,
                new Vector2(HEART_RADIUS, HEART_RADIUS),
                heartImage, gameObjects, windowDimensions, livesCounter);
        this.gameObjects.addGameObject(heart);
        heart.setVelocity(new Vector2(0, HEART_SPEED));
    }

    /**
     * Removes the brick by colling same method on  CollisionStrategy and introduces heart instead of brick.
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
        this.launchHeart(collidedObject.getCenter());
    }

}
