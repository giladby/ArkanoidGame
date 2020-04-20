package animations;

import animations.screens.PauseScreen;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import collections.GameEnvironment;
import collections.SpriteCollection;
import general.Counter;
import general.Velocity;
import geometry.Point;
import geometry.Rectangle;
import interfaces.Animation;
import interfaces.Collidable;
import interfaces.LevelInformation;
import interfaces.Sprite;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreTrackingListener;
import sprites.Ball;
import sprites.colliadables.Block;
import sprites.colliadables.Paddle;
import sprites.indicators.LivesIndicator;
import sprites.indicators.ScoreIndicator;
import sprites.indicators.NameIndicator;

import java.awt.Color;
import java.util.List;

/**
 * The Game Level class.
 * This class holds the sprites and the collidables, and will be in charge of the level's animation.
 */
public class GameLevel implements Animation {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private int width;
    private int height;
    private int rectShort;
    private BlockRemover blockRemover;
    private ScoreTrackingListener scoreTracking;
    private BallRemover ballRemover;
    private Counter blocks;
    private Counter balls;
    private Counter score;
    private Counter lives;
    private Rectangle topRect;
    private int paddleWidth;
    private int paddleSpeed;
    private int paddleHeight;
    private Point paddleUpLeft;
    private Paddle gamePaddle;
    private AnimationRunner runner;
    private boolean running;
    private KeyboardSensor keyboard;
    private LevelInformation level;
    private List<Velocity> velocities;
    private int radiusBall;
    private String stop;

    /**
     * The constructor of a new game.
     *
     * @param level    the level to be display.
     * @param runner   the animation's runner.
     * @param keyboard the keyboard's animation.
     * @param lives    the lives counter.
     * @param score    the score counter.
     * @param blocks   the blocks counter.
     */
    public GameLevel(LevelInformation level, AnimationRunner runner, KeyboardSensor keyboard,
                     Counter lives, Counter score, Counter blocks) {
        this.level = level;
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.blocks = blocks;
        this.balls = new Counter(0);
        this.score = score;
        this.blockRemover = new BlockRemover(this, this.blocks);
        this.ballRemover = new BallRemover(this, this.balls);
        this.scoreTracking = new ScoreTrackingListener(this.score);
        this.lives = lives;
        this.paddleWidth = this.level.paddleWidth();
        this.paddleSpeed = this.level.paddleSpeed();
        this.paddleHeight = 20;
        this.keyboard = keyboard;
        this.runner = runner;
        this.radiusBall = 8;
        this.stop = null;
    }

    /**
     * Sets some sizes of the current level by given parameters.
     *
     * @param screenWidth    the screen's width.
     * @param screenHeight   the screen's height.
     * @param rectangleShort the short side of the rectangles, which are the "borders" blocks.
     */
    public void setSizes(int screenWidth, int screenHeight, int rectangleShort) {
        this.width = screenWidth;
        this.height = screenHeight;
        this.rectShort = rectangleShort;
    }

    /**
     * This method adds a given collidable to the game environment.
     *
     * @param c the given collidable.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * This method removes a given collidable from the game environment.
     *
     * @param c the given collidable.
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * This method adds a given sprite to the list of sprites.
     *
     * @param s the given sprite.
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * This method special method for adding a ball to game, it adds it to start of list,
     * in order to make the ball to be shown first.
     *
     * @param s the given sprite - ball.
     */
    public void addABall(Sprite s) {
        this.sprites.addToPosition(s, 1);
    }

    /**
     * This method removes a given sprite from the list of sprites.
     *
     * @param s the given sprite.
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * The method initializes a new game: creates the Blocks, the paddle, the background and the indicators,
     * and adds them to the game.
     */
    public void initialize() {
        int textSize = 20;
        this.topRect = new Rectangle(new Point(0, 0), this.width, this.rectShort);
        addScoreIndicator(this.score, this.topRect, textSize, Color.WHITE, 4);
        addLiveIndicator(this.lives, this.topRect, textSize, 15);
        addNameIndicator(this.level.levelName(), this.topRect, textSize, 2);
        addPaddle();
        addBorders();
        addBlock();
        addTheBackground();
    }

    /**
     * adds the background to the start of the sprite's list, to be shown in the back.
     */
    private void addTheBackground() {
        this.sprites.addToPosition(this.level.getBackground(), 0);
    }

    /**
     * @return the width of the game's screen.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return the most narrow rectangle's width.
     */
    public int getRectWidth() {
        return this.rectShort;
    }

    /**
     * This method creates a live's indicator, from several given parameters,
     * and adds it to the sprites's list.
     *
     * @param livesCount start number to initialize the indicator.
     * @param rect       the rectangle's measurements of the indicator.
     * @param size       the text's size of the indicator.
     * @param distance   the ratio of the screen's wide, that adds to left border, and the sprite is displayed there.
     */
    private void addLiveIndicator(Counter livesCount, Rectangle rect, int size, double distance) {
        LivesIndicator indicator = new LivesIndicator(livesCount, rect, size, distance);
        this.addSprite(indicator);
    }

    /**
     * This method creates a name's indicator, from several given parameters,
     * and adds it to the sprites's list.
     *
     * @param name     level's name.
     * @param rect     the rectangle's measurements of the indicator.
     * @param size     the text's size of the indicator.
     * @param distance the ratio of the screen's wide, that adds to left border, and the sprite is displayed there.
     */
    private void addNameIndicator(String name, Rectangle rect, int size, double distance) {
        NameIndicator indicator = new NameIndicator(name, rect, size, distance);
        this.addSprite(indicator);
    }

    /**
     * This method creates a score's indicator, from several given parameters,
     * and adds it to the sprites's list.
     *
     * @param scoreCount start number to initialize the indicator.
     * @param rect       the rectangle's measurements of the indicator.
     * @param size       the text's size of the indicator.
     * @param color      the rectangle's color.
     * @param distance   the ratio of the screen's wide, that adds to left border, and the sprite is displayed there.
     */
    private void addScoreIndicator(Counter scoreCount, Rectangle rect, int size, Color color, double distance) {
        ScoreIndicator indicator = new ScoreIndicator(scoreCount, rect, size, color, distance);
        this.addSprite(indicator);
    }

    /**
     * Creates the paddle and adds it to the game.
     */
    private void addPaddle() {
        this.paddleUpLeft = new Point((int) (this.getWidth() / 2 - this.paddleWidth / 2),
                this.height - this.paddleHeight);
        Paddle paddle = new Paddle(this.keyboard, this.paddleUpLeft, this.paddleWidth,
                this.paddleHeight, Color.ORANGE, this.paddleSpeed, this);
        paddle.addToGame(this);
        this.gamePaddle = paddle;
    }

    /**
     * Create the blocks, and add them to the game.
     * Each block has the remover and score listeners.
     */
    private void addBlock() {
        for (Block b : this.level.blocks()) {
            b.addHitListener(this.blockRemover);
            b.addHitListener(this.scoreTracking);
            b.addToGame(this);
        }
    }

    /**
     * Creates the ball and adds it to the game, and update the ball's counter.
     *
     * @param x      is the ball's X coordinate.
     * @param y      is the ball's Y coordinate.
     * @param radius is the ball's radius size.
     * @param v      is the ball's velocity.
     */
    private void addBall(double x, double y, int radius, Velocity v) {
        Ball ball = new Ball(x, y, radius, Color.WHITE, this.environment);
        ball.setVelocity(v);
        ball.setScreenSizes(this.width, this.rectShort);
        ball.addToGame(this);
        this.balls.increase(1);
    }

    /**
     * Create the blocks, which constitute the borders to the game, and add them to the game.
     * The bottom block is kind of death region - when the ball hits it, the ball "dies".
     * The block's remover is a listener of the bottom block.
     */
    private void addBorders() {
        Block[] blocksList = new Block[3];
        Point top = new Point(0, this.rectShort);
        blocksList[0] = new Block(top, this.width, this.rectShort, Color.GRAY);
        Point left = new Point(0, 2 * this.rectShort);
        blocksList[1] = new Block(left, this.rectShort, this.height - 2 * this.rectShort, Color.GRAY);
        Point right = new Point(this.width - this.rectShort, 2 * this.rectShort);
        blocksList[2] = new Block(right, this.rectShort, this.height - 2 * this.rectShort, Color.GRAY);
        Point bottom = new Point(this.rectShort, this.height);
        Block deathRegion = new Block(bottom, this.width - 2 * this.rectShort, 0, Color.GRAY);
        for (Block b : blocksList) {
            b.addToGame(this);
        }
        deathRegion.addToGame(this);
        deathRegion.addHitListener(this.ballRemover);
    }

    /**
     * The method which begins each turn's loop.
     * The turn continues as far as no balls or blocks left.
     * Countdown from 3 to 1 will be shown up at the beginning of each turn.
     */
    public void playOneTurn() {
        addBalls();
        updatePaddle();
        double numOfSeconds = 2;
        int countFrom = 3;
        this.runner.run(new CountdownAnimation(numOfSeconds, countFrom, this.sprites));
        this.running = true;
        this.runner.run(this);
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed();
        // ends the game if the player destroyed all the blocks, and gives him another 100 points,
        // or all balls were "dead".
        if (this.blocks.getValue() == 0 || this.balls.getValue() == 0) {
            this.running = false;
        }
        if (this.keyboard.isPressed("p")) {
            PauseScreen pause = new PauseScreen();
            pause.setBackground(this.stop);
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard,
                    KeyboardSensor.SPACE_KEY, pause));
        }
    }

    /**
     * Set the pause's screen background images from given paths.
     *
     * @param stopPath the path to pause's background image.
     */
    public void setStopPath(String stopPath) {
        this.stop = stopPath;
    }

    @Override
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * adds the level's balls to the animation.
     */
    private void addBalls() {
        this.velocities = this.level.initialBallVelocities();
        for (int i = 0; i < this.level.numberOfBalls(); i++) {
            addBall((int) (this.width / 2) - this.radiusBall, this.paddleUpLeft.getY() - (this.radiusBall * 2),
                    this.radiusBall, this.velocities.get(i));
        }
    }

    /**
     * Updates the paddle's location to the center of the screen.
     */
    private void updatePaddle() {
        this.gamePaddle.updateLocation(this.paddleUpLeft, this.paddleWidth, this.paddleHeight);
    }
}
