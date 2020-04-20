package sprites;

import biuoop.DrawSurface;
import interfaces.Sprite;
import interfaces.Collidable;
import sprites.colliadables.CollisionInfo;
import geometry.Point;
import geometry.Line;
import geometry.Rectangle;
import general.Velocity;
import animations.GameLevel;
import collections.GameEnvironment;

import java.awt.Color;

/**
 * Ball class.
 */
public class Ball implements Sprite {
    private int size;
    private Point location;
    private Color color;
    private Velocity vel;
    private GameEnvironment game;
    private Rectangle paddle;
    private int screenWidth;
    private int screenSide;

    /**
     * Creates a new Ball from given point, which represents the middle point of the ball,
     * the radius and the color.
     *
     * @param center the middle point of the ball.
     * @param r      the radius of the ball.
     * @param color  the color of the ball.
     * @param game   the game environment in which the ball exists.
     */
    public Ball(Point center, int r, Color color, GameEnvironment game) {
        double x = center.getX();
        double y = center.getY();
        this.location = new Point(x, y);
        this.size = r;
        this.color = color;
        this.game = game;
    }

    /**
     * Creates a new sprites.Ball from given center, specified as (x,y), the radius and the color of the ball.
     *
     * @param x     the X coordinate.
     * @param y     the Y coordinate.
     * @param r     the radius of the ball.
     * @param color the color of the ball.
     * @param game  the game environment in which the ball exists.
     */
    public Ball(double x, double y, int r, Color color, GameEnvironment game) {
        this.location = new Point(x, y);
        this.size = r;
        this.color = color;
        this.game = game;
    }

    /**
     * Sets screen sizes.
     *
     * @param width the screen's width.
     * @param side  the screen's side.
     */
    public void setScreenSizes(int width, int side) {
        this.screenWidth = width;
        this.screenSide = side;
    }

    /**
     * Gets location.
     *
     * @return the ball's location.
     */
    public Point getLocation() {
        return this.location;
    }

    /**
     * Gets x.
     *
     * @return the X coordinate.
     */
    public int getX() {
        return (int) this.location.getX();
    }

    /**
     * Gets y.
     *
     * @return the Y coordinate.
     */
    public int getY() {
        return (int) this.location.getY();
    }

    /**
     * Gets size.
     *
     * @return the radius size of the ball.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Gets color.
     *
     * @return the color of the ball.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Draw the ball on the surface.
     *
     * @param surface is the surface, the ball needs to be drown on.
     */
    @Override
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(this.getX(), this.getY(), this.size);
        surface.setColor(Color.BLACK);
        surface.drawCircle(this.getX(), this.getY(), this.size);
    }

    /**
     * Notify the ball that it should makes it's next move.
     */
    @Override
    public void timePassed() {
        moveOneStep();
    }

    /**
     * Sets the ball's velocity from a given velocity.
     *
     * @param v the new velocity of the ball.
     */
    public void setVelocity(Velocity v) {
        double dx = v.getDx();
        double dy = v.getDy();
        this.vel = new Velocity(dx, dy);
    }

    /**
     * Sets the ball's velocity from given dx and dy.
     *
     * @param dx the change of the X coordinate.
     * @param dy the change of the Y coordinate.
     */
    public void setVelocity(double dx, double dy) {
        this.vel = new Velocity(dx, dy);
    }

    /**
     * Sets the ball's location.
     *
     * @param p the new location.
     */
    private void setLocation(Point p) {
        this.location = new Point(p.getX(), p.getY());
    }

    /**
     * Gets velocity.
     *
     * @return the ball's velocity.
     */
    public Velocity getVelocity() {
        return this.vel;
    }

    /**
     * Add the ball to a given game.
     *
     * @param g is the given game.
     */
    public void addToGame(GameLevel g) {
        g.addABall(this);
    }

    /**
     * Removes the ball from a given game.
     *
     * @param g is the given game.
     */
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
    }

    /**
     * The method sets the ball to his new position.
     * It computes the ball trajectory (the trajectory is "how the ball will move
     * without any obstacles" - its a line starting at current location, and
     * ending where the velocity will take the ball if no collisions will occur).
     * <p>
     * The method checks if moving on this trajectory will hit anything.
     * If no, then move the ball to the end of the trajectory.
     * Otherwise (there is a hit): the ball is moving until the collision point, and his velocity is changed,
     * according the "hit rules" of the object, that the ball hit at.
     */
    public void moveOneStep() {
        Point newLocation = null;
        this.outside();
        // Check if the ball inside the paddle, and if so - change the ball's location to be on top the paddle.
        if (inPaddle()) {
            newLocation = new Point(this.location.getX(), this.paddle.getUpperLeft().getY() - this.getSize());
            setLocation(newLocation);
        }
        Point potential = new Point(this.vel.getDx() + this.location.getX(),
                this.vel.getDy() + this.location.getY());
        Line movement = new Line(this.location, potential);
        CollisionInfo collision = this.game.getClosestCollision(movement);
        Rectangle collisionRect = null;
        Point colPoint = null;
        double radiusY = 0;
        double radiusX = 0;
        if (collision != null) {
            colPoint = collision.collisionPoint();
            collisionRect = collision.collisionObject().getCollisionRectangle();
            // the collision occurred at the up side.
            if (collisionRect.getUp().isContain(colPoint)) {
                radiusY = -1 * this.getSize();
            }
            // the collision occurred at the down side.
            if (collisionRect.getDown().isContain(colPoint)) {
                radiusY = this.getSize();
            }
            // the collision occurred at the left side.
            if (collisionRect.getLeft().isContain(colPoint)) {
                radiusX = -1 * this.getSize();
            }
            // the collision occurred at the right side.
            if (collisionRect.getRight().isContain(colPoint)) {
                radiusX = this.getSize();
            }
            newLocation = new Point(colPoint.getX() + radiusX, colPoint.getY() + radiusY);
            setLocation(newLocation);
            this.vel = collision.collisionObject().hit(this, collision.collisionPoint(), this.vel);
        } else {
            this.location = this.vel.applyToPoint(this.location);
        }
    }

    /**
     * Checks if the ball crossed the borders.
     */
    private void outside() {
        // the ball crossed the up border.
        if (this.location.getY() < 2 * this.screenSide) {
            this.location = new Point(this.location.getX(), (this.screenSide * 2) + this.size);
            this.vel = new Velocity(this.vel.getDx(), -1 * this.vel.getDy());
        }
        // the ball crossed the right border.
        if (this.location.getX() > this.screenWidth - this.screenSide) {
            this.location = new Point(this.screenWidth - this.screenSide - this.size, this.location.getY());
            this.vel = new Velocity(-1 * this.vel.getDx(), this.vel.getDy());
        }
        // the ball crossed the left border.
        if (this.location.getX() < this.screenSide) {
            this.location = new Point(this.size + this.screenSide, this.location.getY());
            this.vel = new Velocity(-1 * this.vel.getDx(), this.vel.getDy());
        }
    }

    /**
     * Checks if a number is in the range of two other numbers.
     *
     * @param n1 the first limit.
     * @param n2 the second limit.
     * @param n  the checked number.
     * @return true if the number in the range of the numbers, and returns false otherwise.
     */
    private boolean inRange(double n1, double n2, double n) {
        return (n <= n1 && n >= n2) || (n >= n1 && n <= n2);
    }

    /**
     * Checks if the ball isn't contained in the paddle.
     *
     * @return true if the ball is contained in the paddle. Otherwise - return false.
     */
    private boolean inPaddle() {
        // the paddle is the first item in the colliadbles list.
        Collidable c = this.game.getList().get(0);
        double paddleStartX = c.getCollisionRectangle().getUpperLeft().getX();
        double paddleEndX = c.getCollisionRectangle().getUpperLeft().getX() + c.getCollisionRectangle().getWidth();
        double paddleStartY = c.getCollisionRectangle().getUpperLeft().getY();
        double paddleEndY = c.getCollisionRectangle().getUpperLeft().getY() + c.getCollisionRectangle().getHeight();
        // Check if the ball is contained in the paddle.
        // The ball will be contained in the paddle,
        // if it's center point's X and Y coordinates are in the range of the paddle's X and Y coordinates.
        if (inRange(paddleStartX, paddleEndX, this.location.getX())
                && inRange(paddleStartY, paddleEndY, this.location.getY())) {
            this.paddle = c.getCollisionRectangle();
            return true;
        }
        return false;
    }
}