package general;

import geometry.Point;

/**
 * The Velocity class.
 */
public class Velocity {
    private double dx;
    private double dy;

    /**
     * creates a new velocity from given dx and dy.
     *
     * @param dx the change of the X coordinate.
     * @param dy the change of the Y coordinate.
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * creates new velocity object from angle and speed velocity.
     *
     * @param angle the angle of velocity.
     * @param speed the speed of velocity.
     * @return the new velocity object.
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double radAngle = Math.toRadians(angle);
        double sin = Math.sin(radAngle);
        double cos = Math.cos(radAngle);
        double dx = sin * speed;
        double dy = -1 * cos * speed;
        return new Velocity(dx, dy);
    }

    /**
     * Gets dx.
     *
     * @return the change of the X coordinate.
     */
    public double getDx() {
        return this.dx;
    }

    /**
     * Gets dy.
     *
     * @return the change of the Y coordinate.
     */
    public double getDy() {
        return this.dy;
    }

    /**
     * Take a point with position (x,y) and return a new point after adding the changes in the X and Y coordinates.
     *
     * @param p the previous position.
     * @return the new point.
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + dx, p.getY() + dy);
    }
}