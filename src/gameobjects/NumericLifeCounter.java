package gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Class that extends GameObject, it wil not hold any renderable but will be in charge of generating new lives
 * that will be rendered in the screen each time the user lose or win a live or liveCounter get updated.
 */
public class NumericLifeCounter extends GameObject {

    private final TextRenderable textRenderable;
    private final Counter livesCounter;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     *
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions, GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.textRenderable = new TextRenderable(Integer.toString(this.livesCounter.value()));
        this.renderer().setRenderable(textRenderable);
        gameObjectCollection.addGameObject(new GameObject(topLeftCorner, dimensions, this.textRenderable), Layer.BACKGROUND);
    }

    /**
     * Maps every color with a number.
     */
    private void setColorOfNumber() {
        switch (livesCounter.value()) {
            case 3 -> textRenderable.setColor(Color.green);
            case 2 -> textRenderable.setColor(Color.yellow);
            case 1 -> textRenderable.setColor(Color.red);
        }
    }

    /**
     * Override the updates method to check on every frame of the game to update the counter of lives.
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
        this.setColorOfNumber();
        this.textRenderable.setString(Integer.toString(this.livesCounter.value()));

    }
}
