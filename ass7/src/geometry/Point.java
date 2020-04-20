package geometry;

/**
 * geometry_primitives.Point class.
 */
public class Point {
    private double x;
    private double y;

    /**
     * Creates a new geometry_primitives.Point from X and Y coordinates.
     *
     * @param x the X coordinate.
     * @param y the Y coordinate.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculate the distance between the points.
     *
     * @param other the compared point.
     * @return the destination between the points.
     */
// distance -- return the distance of this point to the other point
    public double distance(Point other) {
        return Math.sqrt(((this.x - other.getX()) * (this.x - other.getX()))
                + ((this.y - other.getY()) * (this.y - other.getY())));
    }

    /**
     * Checks if the points are equal.
     *
     * @param other the compared point.
     * @return the boolean value of the comparison between the points.
     */
    public boolean equals(Point other) {
        return this.x == other.getX() && this.y == other.getY();
    }

    /**
     * @return the X coordinate.
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return the Y coordinate.
     */
    public double getY() {
        return this.y;
    }
}