package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.Ball;
import gameobjects.BallCollisionCounter;
import src.BrickerGameManager;

/**
 * Class extending CollisionStrategy.
 * Changes camera focus from ground to ball until ball collides NUM_BALL_COLLISIONS_TO_TURN_OFF times.
 */
public class CameraStrategy extends CollisionStrategy {

    private BrickerGameManager brickerGameManager;
    private WindowController windowController;
    private final int LIMIT_NUMBER_OF_BALL_COLLISIONS = 4;
    Ball mainBall;

    /**
     *
     * @param gameObjects
     * @param windowController
     * @param brickerGameManager
     */
    public CameraStrategy(GameObjectCollection gameObjects, WindowController windowController, BrickerGameManager brickerGameManager) {
        super(gameObjects);
        this.windowController = windowController;
        this.brickerGameManager = brickerGameManager;
        mainBall = brickerGameManager.getMainBall();
    }

    /**
     * Turns on the camera.
     * @param ball
     */
    private void setCamera(GameObject ball) {
        brickerGameManager.setCamera(
                new Camera(
                        ball, //object to follow
                        Vector2.ZERO, //follow the center of the object
                        windowController.getWindowDimensions().mult(1.2f), //widen the frame a bit
                        windowController.getWindowDimensions() //share the window dimensions
                )
        );
    }

    /**
     * Removes the brick by calling same method on CollisionStrategy and activates the camera.
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
        if(brickerGameManager.getCamera() == null) {
            gameObjects.addGameObject(new BallCollisionCounter(this.mainBall, this, LIMIT_NUMBER_OF_BALL_COLLISIONS));
            this.setCamera(mainBall);
        }
    }

    /**
     * Turns off the camera.
     */
    public void turnOfCamera() {
        brickerGameManager.setCamera(null);
    }
}
