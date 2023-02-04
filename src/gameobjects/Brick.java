package gameobjects;

import brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Class that extends GameObject and will represent each brick of the game. Each time an object collide with
 * him, it will call the strategy which identifies it.
 */
public class Brick extends GameObject {

    private final CollisionStrategy strategy;
    private final Counter counter;
    private boolean hasAlreadyCollided = false;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, CollisionStrategy strategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.strategy = strategy;
        this.counter = counter;
    }

    /**
     * Listen for new collisions and calls the method OnCollision from CollisionStrategy that was associated to it.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        //If condition in case the method is called multiple times for the same collision.
        if(!hasAlreadyCollided) {
            hasAlreadyCollided = true;
            strategy.onCollision(this, other, counter);
        }
    }
}
