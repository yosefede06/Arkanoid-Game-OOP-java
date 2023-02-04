<p float="left">
  <img src="https://github.com/yosefede06/Arkanoid-Game-OOP-java/blob/main/screen1.png" width="350" />
</p>
<p float="right">
 <img src="https://github.com/yosefede06/Arkanoid-Game-OOP-java/blob/main/screen2.png" width="350" />
 </p>

Download library: https://danthe1st.itch.io/danogamelab
Steps to run game using IntelliJ IDEA: 
Project Structure -> Modules -> Dependencies -> + -> JARs or Directories -> Select DanoGameLab.jar and src file inside the DanoGameLab unzipped.

## Files description:

### src:

- **BrickerGameManager**: This class is responsible for game initialization, holding references for game objects and
 calling update methods for every update iteration. Entry point for code should be in a main method in this class.

### brick_strategies:

- **CollisionStrategy**: General type for brick strategies. All brick strategies extends this class.
- **PuckStrategy**: Class extending CollisionStrategy. Introduces several pucks instead of brick once removed.
- **AdditionalPaddleStrategy**: Class extending CollisionStrategy. Introduces extra
 paddle to game window which remains until colliding NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE with
 other game objects.
- **CameraStrategy**: Class extending CollisionStrategy.
 Changes camera focus from ground to ball until ball collides NUM_BALL_COLLISIONS_TO_TURN_OFF times.
- **HeartStrategy**: Class extending CollisionStrategy.
 Throws heart to ground and if the paddle pick the heart it will add a new live to the player.
- **DoubleStrategy**: Class extending CollisionStrategy.
 Add double strategies from the ones extending CollisionStrategy including itself. It can have a maximum of
 3 strategies according to the implementation given.
- **BrickStrategyFactory**: Factory class for creating Collision strategies.

### gameobjects:

- **AdditionalPaddle**: Class that extends Paddle class and will be the object that follows AdditionalPaddleStrategy.
- **Ball**: Main ball of the game which extends the GameObject. It will be in charge of changing speed directions
each time it collides with an object of it same layer (borders, bricks, paddle). It will hold a counter
for the number of collisions made so far since initialization.
- **GraphicLifeCounter**: Class that extends GameObject, it wil not hold any renderable but will be in charge of generating new lives
that will be rendered in the screen each time the user lose or win a live or liveCounter get updated.
- **NumericLiveCounter**: Class that extends the GameObject and which will be responsible of rendering the number of lives in text.
As same as GraphicLifeCounter it will update lives each time liveCounter get updated.
- **Brick**: Class that extends GameObject and will represent each brick of the game. Each time an object collide with
him, it will call the strategy which identifies it.
- **BallCollisionCounter**: Class that extends GameObject with no render properties and will be in charge of handling ball collisions.
According to our game implementation, it will be used by the CameraStrategy in order to turnOff the camera
after numberOfCollisions.
- **Paddle**: Main paddle of the game which extends GameObject. It will be in charge of preventing the main ball of
touching the ground and the user should use it move RIGHT_LEFT and RIGHT_LEFT keys in order to break all
the bricks of the game.
- **HeartMovement**: Heart representing the ones falling from the bricks implemented by the HeartStrategy. If it gets out of
 the scopes of the ground it will be removed from the gameObjectsCollection of the game.

## Design and implementation issues:

3 main decisions have been made for the implementation of the game:

1) A class for the Puck balls wasn't implemented. Instead, each puck ball will be an instance of the Ball class.
This decision was made since there was no difference on the activity of both classes that already the Ball
class wouldn't take care of by calling there methods. Therefore, I decided to take profit of the Ball
encapsulation to save unnecessary lines of code. The only thing we need to take care of here is to be able
to identify the main Ball from the puck balls that could appear in the game. To do that we've defined a field
for the main Ball on the BrickerGameManager.

2) All the special strategies will extend the CollisionStrategy. All strategies follows the same pattern a ball
collides the brick and calls the onCollision method which is override from the CollisionStrategy. Then it
updates the number of bricks by decrementing the counter and at the end activates the power by calling the
innerOnCollision method. Is important to remark that we won't always have one only strategy for each brick of
the game. Therefore sometimes we will want to activate the strategy's power without making updates updates on
the brick, that's why innerOnCollision was defined as a public method, in order to let other strategies to use
there power without making updates on the brick counter.

3) DoubleStrategy class will received the StrategyFactory on the constructor. This decision was made since
we don't have to struggle with more parameters, StrategyFactory already provides us the parameters we need for
the initialization therefore this approach enhances the comprehension of the class.

## Answers to questions:

### 1) 
How did you limit the amount of double behaviors to 3 behaviors?

First of all, two random from 0-4 (including 4, representing the special strategies
including the DoubleStrategy) were sampled. If both sampled numbers are less then we finished.
Otherwise, there are 3 options, which in all of them we are already deciding that 3 strategies behaviors
will be returned.

i) Both numbers are equal to 4. In which case we randomly sample 3 numbers from 0-3 (3 included,
representing the special strategies not including the DoubleStrategy).

ii) If just the first number equals to 4, then we sample two more strategies without including
the DoubleStrategy and return the those two corresponding strategies with the one of the second number.

iii) If just the second number equals to 4, then we sample two more strategies without including
the DoubleStrategy and return the those two corresponding strategies with the one of the first number.

### 2)

a) - What was the design you chose so that your child could have more than one behavior?
b) - How does the solution support the extension of a larger amount of behaviors in a single brick?

a) All the special strategies will extend the CollisionStrategy. All strategies follows the same pattern a ball
collides the brick and calls the onCollision method which is override from the CollisionStrategy. Then it
updates the number of bricks by decrementing the counter and at the end activates the power by calling the
innerOnCollision method. Is important to remark that we won't always have one only strategy for each brick of
the game. Therefore sometimes we will want to activate the strategy's power without making updates updates on
the brick, that's why innerOnCollision was defined as a public method, in order to let other strategies to use
there power without making updates on the brick counter. (This question was already answered when remarking all
the design implementations).

b) This solution is useful in the sense that supports the extension of a larger amount of behaviors in a single
brick, since each time the doubleStrategy (or any other strategy which follows the same logic) add new
strategies to it's field of strategies, the class overriding the onCollision method will call the
innerOnCollision from each of the classes saved in the array of strategies and will activate the power of
each of them without updating the bricks counter.


