package sprites.colliadables;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import interfaces.Collidable;
import interfaces.Sprite;
import sprites.Ball;
import geometry.Rectangle;
import geometry.Point;
import general.Velocity;
import animations.GameLevel;

/**
 * The Paddle class.
 * The Paddle is the player in the game.
 * It's a rectangle that is controlled by the keyboard's arrow keys, and moves according to the user's decision.
 */
public class Paddle implements Sprite, Collidable {
    private KeyboardSensor keyboard;
    private Rectangle paddle;
    private java.awt.Color color;
    private Velocity vel;
    private GameLevel game;

    /**
     * The constructor of the paddle.
     * It creates a new paddle from rectangle's Properties, the connection of the keyboard to gui, speed,
     * the game which the paddle belongs to and it's color.
     *
     * @param keyboard the keyboard
     * @param upLeft   the up left
     * @param width    the width
     * @param height   the height
     * @param c        the c
     * @param dx       the dx
     * @param g        the g
     */
    public Paddle(KeyboardSensor keyboard, Point upLeft, int width, int height,
                  java.awt.Color c, double dx, GameLevel g) {
        this.keyboard = keyboard;
        this.paddle = new Rectangle(upLeft, width, height);
        this.color = c;
        this.vel = new Velocity(dx, 0);
        this.game = g;
    }

    /**
     * Add the paddle to a given game.
     *
     * @param g the game the paddle belongs to.
     */
    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    /**
     * Updates the paddle's location according the given parameters.
     *
     * @param upLeft the upper lest point of the paddle.
     * @param width  the paddle's width.
     * @param height the paddle's height.
     */
    public void updateLocation(Point upLeft, int width, int height) {
        this.paddle = new Rectangle(upLeft, width, height);
    }

    /**
     * @return this paddle, which the ball Collided with.
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return this.paddle;
    }

    /**
     * The methods calculates the new speed of the ball after the collision with the paddle,
     * according the position of the collision point on the paddle.
     *
     * @param hitter          is the ball which hits the block.
     * @param collisionPoint  is the point where the collision occurred.
     * @param currentVelocity is the ball's velocity before the hit.
     * @return the new ball's velocity after the collision with the paddle.
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        double section = this.paddle.getWidth() / 5;
        double speed;
        int angle = 0;
        if (collisionPoint.getX() >= section * 4 + this.paddle.getUpperLeft().getX()) {
            angle = 60;
        } else {
            if (collisionPoint.getX() >= section * 3 + this.paddle.getUpperLeft().getX()) {
                angle = 30;
            } else {
                if (collisionPoint.getX() >= section * 2 + this.paddle.getUpperLeft().getX()) {
                    return new Velocity(currentVelocity.getDx(), -1 * currentVelocity.getDy());
                } else {
                    if (collisionPoint.getX() >= section + this.paddle.getUpperLeft().getX()) {
                        angle = 330;
                        // collisionPoint's X coordinate is less than section.
                    } else {
                        angle = 300;
                    }
                }
            }
        }
        speed = getSpeed(currentVelocity);
        return Velocity.fromAngleAndSpeed(angle, speed);
    }

    /**
     * The method calculates the speed of the ball according the ball's speed in the X and Y coordinates.
     *
     * @param v is the ball's velocity.
     * @return the speed of the ball.
     */
    private double getSpeed(Velocity v) {
        double dx = v.getDx();
        double dy = v.getDy();
        // calculating according to Pythagorean theorem.
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    /**
     * The method prints the paddle on a given surface.
     *
     * @param d is the surface the paddle needs to be drawn on.
     */
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.fillRectangle((int) this.paddle.getUpperLeft().getX(), (int) this.paddle.getUpperLeft().getY(),
                (int) this.paddle.getWidth(), (int) this.paddle.getHeight());
    }

    /**
     * The method notifies the paddle that it should make it's next move.
     */
    @Override
    public void timePassed() {
        moveOneStep();
    }

    /**
     * The method moves the paddle according the user's press on the keyboard.
     */
    private void moveOneStep() {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        } else {
            if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
                moveRight();
            }
        }
    }

    /**
     * The method calculates the movement of the paddle, in case that the user presses the left arrow.
     */
    private void moveLeft() {
        Point newUpLeft;
        int minX = this.game.getRectWidth();
        newUpLeft = new Point(this.paddle.getUpperLeft().getX() - this.vel.getDx(),
                this.paddle.getUpperLeft().getY());
        if (newUpLeft.getX() < minX) {
            newUpLeft = new Point(minX, newUpLeft.getY());
        }
        setLocation(newUpLeft);
    }

    /**
     * The method calculates the movement of the paddle, in case that the user presses the right arrow.
     */
    private void moveRight() {
        Point newUpLeft;
        int maxX = this.game.getWidth() - this.game.getRectWidth() - (int) this.paddle.getWidth();
        newUpLeft = new Point(this.paddle.getUpperLeft().getX() + this.vel.getDx(),
                this.paddle.getUpperLeft().getY());
        if (newUpLeft.getX() > maxX) {
            newUpLeft = new Point(maxX, newUpLeft.getY());
        }
        setLocation(newUpLeft);
    }

    /**
     * The method updates the new location of the paddle.
     *
     * @param newP is the new location of the paddle.
     */
    private void setLocation(Point newP) {
        this.paddle = new Rectangle(newP, this.paddle.getWidth(), this.paddle.getHeight());
    }
}
