package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.util.Random;

/**
 * Class extending CollisionStrategy.
 * Add double strategies from the ones extending CollisionStrategy including itself. It can have a maximum of
 * 3 strategies according to the implementation given.
 */
public class DoubleStrategy extends CollisionStrategy {
    private static final int STRATEGIES_NUMBER = 5;
    private StrategyFactory strategyFactory;
    CollisionStrategy[] allStrategies;
    private int currentStrategyCounter;

    private static final Random random = new Random();

    public DoubleStrategy(GameObjectCollection gameObjects, StrategyFactory strategyFactory) {
        super(gameObjects);
        this.strategyFactory = strategyFactory;
        currentStrategyCounter = 0;
        allStrategies = new CollisionStrategy[3];
        selectStrategies();
    }

    /**
     *
     */
    private void selectStrategies() {
        int strategyIndex1 = random.nextInt(STRATEGIES_NUMBER);
        int strategyIndex2 = random.nextInt(STRATEGIES_NUMBER);
        //Three strategies in total if this condition is satisfied.
        if(strategyIndex1 == 4 || strategyIndex2 == 4) {
            //Random strategy without DoubleStrategy. Probability: 1/4.
            this.addStrategy(strategyFactory.getStrategy(random.nextInt(STRATEGIES_NUMBER - 1)));
            this.addStrategy(strategyFactory.getStrategy(random.nextInt(STRATEGIES_NUMBER - 1)));
            if(strategyIndex1 == 4 && strategyIndex2 == 4) {
                this.addStrategy(strategyFactory.getStrategy(random.nextInt(STRATEGIES_NUMBER - 1)));
            }
            else if (strategyIndex1 == 4) {
                this.addStrategy(strategyFactory.getStrategy(strategyIndex2));
            }
            else {
                this.addStrategy(strategyFactory.getStrategy(strategyIndex1));
            }
        }
        else {
            this.addStrategy(strategyFactory.getStrategy(strategyIndex1));
            this.addStrategy(strategyFactory.getStrategy(strategyIndex2));
        }
    }

    /**
     *
     * @param strategyToAdd
     */
    private void addStrategy(CollisionStrategy strategyToAdd) {
        allStrategies[currentStrategyCounter++] = strategyToAdd;
    }

    /**
     * Removes the brick by colling same method on CollisionStrategy and adds new strategies behaviours.
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
        for(int i = 0; i < currentStrategyCounter; i++) {
            allStrategies[i].innerOnCollision(collidedObject, colliderObj, bricksCounter);
        }
    }
}
