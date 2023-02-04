package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Main ball of the game which extends the GameObject. It will be in charge of changing speed directions
 * each time it collides with an object of it same layer (borders, bricks, paddle). It will hold a counter
 * for the number of collisions made so far since initialization.
 */
public class Ball extends GameObject {
    private Counter collisionCounter;

    private Sound collisionSound;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param sound The collision sound.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = sound;
        collisionCounter = new Counter();
    }

    /**
     * Listen for new collisions and changes speed direction.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        collisionCounter.increment();
        super.onCollisionEnter(other, collision);
        Vector2 newVel = this.getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
    }

    /**
     * Public method that provides the number of collision made so far.
     * @return Counter representing number if collisions.
     */
    public int getCollisionCount() {
        return collisionCounter.value();
    }
}
