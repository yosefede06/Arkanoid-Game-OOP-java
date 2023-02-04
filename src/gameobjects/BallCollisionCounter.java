package gameobjects;

import brick_strategies.CameraStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Class that extends GameObject with no render properties and will be in charge of handling ball collisions.
 * According to our game implementation, it will be used by the CameraStrategy in order to turn off the camera
 * after numberOfCollisions.
 */
public class BallCollisionCounter extends GameObject {
    private final int initalCollisions;
    private Ball ball;
    private CameraStrategy cameraStrategy;
    private int numberOfCollisions;

    /**
     * Construct a new GameObject instance.
     * @param ball mainBall
     * @param cameraStrategy Camera strategy behaviour.
     * @param numberOfCollisions Max number of collisions from the time is initialized to turn off the camera.
     */
    public BallCollisionCounter(Ball ball, CameraStrategy cameraStrategy, int numberOfCollisions) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.ball = ball;
        this.cameraStrategy = cameraStrategy;
        this.numberOfCollisions = numberOfCollisions;
        initalCollisions = ball.getCollisionCount();
    }

    /**
     *
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
        if(ball.getCollisionCount() - initalCollisions == numberOfCollisions) {
            cameraStrategy.turnOfCamera();
        }
    }
}
