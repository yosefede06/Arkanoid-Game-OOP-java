package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Heart representing the ones falling from the bricks implemented by the HeartStrategy. If it gets out of
 * the scopes of the ground it will be removed from the gameObjectsCollection of the game.
 */
public class HeartMovement extends GameObject {

    private GameObjectCollection gameObjects;
    private Vector2 windowDimensions;
    private Counter livesCounter;
    private static final int MAXIMUM_NUM_OF_LIVES = 4;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public HeartMovement(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, GameObjectCollection gameObjects, Vector2 windowDimensions, Counter livesCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.livesCounter = livesCounter;
    }

    /**
     * Override the updates method to check on every frame of the game to remove the object if it gets out of the window
     * dimensions.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(this.getCenter().y() > windowDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
    }

    /**
     * Take care of just triggering a collision with the main paddle.
     * @param other The other GameObject.
     * @return
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Paddle && !(other instanceof AdditionalPaddle);
    }

    /**
     * Handles collision by removing the object and adding a new life to the player.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjects.removeGameObject(this);
        if(livesCounter.value() < MAXIMUM_NUM_OF_LIVES) {
            livesCounter.increment();
        }
    }

}
