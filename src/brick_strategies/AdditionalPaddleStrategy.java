package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import gameobjects.AdditionalPaddle;
import gameobjects.Paddle;

/**
 * Class extending CollisionStrategy. Introduces extra
 * paddle to game window which remains until colliding NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE with
 * other game objects.
 */
public class AdditionalPaddleStrategy extends CollisionStrategy {
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 100;

    private Counter collisions;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;
    private final int BORDER_WIDTH = 10;
    private final int IMPACTS_TO_DELETE = 3;
    private boolean isPaddleLive;

    /**
     * Constructor of the class.
     * @param gameObjects
     * @param imageReader
     * @param inputListener
     * @param windowDimensions
     */
    public AdditionalPaddleStrategy(GameObjectCollection gameObjects,
                                    ImageReader imageReader,
                                    UserInputListener inputListener,
                                    Vector2 windowDimensions) {
        super(gameObjects);
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        collisions = new Counter();
        isPaddleLive = false;
    }

    /**
     * Removes the brick by calling same method on  CollisionStrategy and introduces new paddle.
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
        if (collisions.value() == IMPACTS_TO_DELETE) {
            collisions.reset();
            isPaddleLive = false;
        }
        else if(collisions.value() == 0 && !isPaddleLive) {
            isPaddleLive = true;
            this.createAdditionalPaddle();
        }
    }

    /**
     * Get the center of the window.
     * @return Vector2
     */
    private Vector2 getCenterWindow() {
        return windowDimensions.mult((float)0.5);
    }

    /**
     * Create new Paddle instance.
     */
    private void createAdditionalPaddle() {
        Renderable paddleImage = imageReader.readImage("assets/botGood.png", false);
        GameObject userPaddle = new AdditionalPaddle(this.getCenterWindow(),
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener,
                windowDimensions,
                gameObjects,
                BORDER_WIDTH,
                IMPACTS_TO_DELETE,
                collisions);
        gameObjects.addGameObject(userPaddle);
    }


}
