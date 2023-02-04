package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 *  General type for brick strategies. All brick strategies extends this class.
 */
public class CollisionStrategy {
    protected final GameObjectCollection gameObjects;

    /**
     * Constructor of the class.
     * @param gameObjects
     */
    public CollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * Removes the brick and updates the brick counter.
     * @param collidedObject Collided object.
     * @param colliderObj Object that collides.
     * @param bricksCounter Counter of bricks removed from the game.
     */
    public void onCollision(GameObject collidedObject, GameObject colliderObj, Counter bricksCounter) {
        gameObjects.removeGameObject(collidedObject);
        bricksCounter.decrement();
    }

    /**
     * Handles the power of the strategy behaviour.
     * @param collidedObject Collided object.
     * @param colliderObj Object that collides.
     * @param bricksCounter Counter of bricks removed from the game.
     */
    public void innerOnCollision(GameObject collidedObject, GameObject colliderObj, Counter bricksCounter) {}
}
