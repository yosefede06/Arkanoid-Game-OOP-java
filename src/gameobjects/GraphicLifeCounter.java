package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.collisions.LayerManager;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * Class that extends GameObject, it wil not hold any renderable but will be in charge of generating new lives
 * that will be rendered in the screen each time the user lose or win a live or liveCounter get updated.
 */
public class GraphicLifeCounter extends GameObject {
    private static final int PADDING_LIVES = 3;
    private static final int MAXIMUM_NUM_OF_LIVES = 4;
    private Vector2 topLeftCorner;
    private Vector2 dimensions;
    private Renderable widgetRenderable;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;
    private final Counter livesCounter;
    private final GameObject[] hearts;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param widgetRenderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Counter livesCounter, Renderable widgetRenderable, GameObjectCollection gameObjectCollection, int numOfLives) {
        super(topLeftCorner, new Vector2(livesCounter.value() * dimensions.x() + topLeftCorner.x() + PADDING_LIVES * livesCounter.value(), dimensions.y()), null);
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.widgetRenderable = widgetRenderable;
        this.gameObjectCollection = gameObjectCollection;
        this.livesCounter = livesCounter;
        this.hearts = new GameObject[MAXIMUM_NUM_OF_LIVES];
        this.numOfLives = 0;
        while(this.numOfLives < numOfLives) {
            this.addNewLive();
        }
    }

    /**
     * Takes care of adding new hearts (representing new lives) to the game.
     */
    private void addNewLive() {
            this.hearts[this.numOfLives] = new GameObject(
                    new Vector2((this.numOfLives + 1) * dimensions.x() + topLeftCorner.x() + PADDING_LIVES * (this.numOfLives + 1), topLeftCorner.y()),
                    dimensions,
                    widgetRenderable);
            this.gameObjectCollection.addGameObject(this.hearts[this.numOfLives++], Layer.BACKGROUND);
    }

    /** Override the updates method to check on every frame of the game to remove or add the heart in case there was
     * an update on the lives counter.
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
        if(livesCounter.value() < numOfLives) {
            this.gameObjectCollection.removeGameObject(hearts[--numOfLives], Layer.BACKGROUND);
        }
        else if(livesCounter.value() > numOfLives) {
            this.addNewLive();
        }
    }
}
