package geometry;

import java.util.List;
import java.util.ArrayList;

/**
 * The geometry_primitives.Rectangle class.
 * It describes an popular shape of game's sprites.
 */
public class Rectangle {
    private Point upLeft;
    private double width;
    private double height;
    private Line up;
    private Line down;
    private Line left;
    private Line right;

    /**
     * The constructor of a new geometry_primitives.Rectangle.
     * It creates a new rectangle with location, width and height.
     *
     * @param upperLeft the upper left
     * @param width     the width
     * @param height    the height
     */
    public Rectangle(Point upperLeft, double width, double height) {
        this.upLeft = upperLeft;
        this.width = width;
        this.height = height;
        Point upRight = new Point(this.upLeft.getX() + this.width, this.upLeft.getY());
        Point downRight = new Point(this.upLeft.getX() + this.width, this.upLeft.getY() + this.height);
        Point downLeft = new Point(this.upLeft.getX(), this.upLeft.getY() + this.height);
        this.up = new Line(this.getUpperLeft(), upRight);
        this.down = new Line(downLeft, downRight);
        this.right = new Line(downRight, upRight);
        this.left = new Line(this.getUpperLeft(), downLeft);
    }

    /**
     * Returns a (possibly empty) list of intersection points with a given line.
     *
     * @param line the line
     * @return the list of intersection points.
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        Line[] rectangle = new Line[4];
        Point intersect;
        List<Point> intersection = new ArrayList<>();
        // creates an array of the rectangle's sides.
        rectangle[0] = this.left;
        rectangle[1] = this.right;
        rectangle[2] = this.up;
        rectangle[3] = this.down;
        // checks if the given line intersects with the rectangle's sides.
        for (int i = 0; i < rectangle.length; i++) {
            intersect = rectangle[i].intersectionWith(line);
            if (intersect != null) {
                intersection.add(intersect);
            }
        }
        return intersection;
    }

    /**
     * @return the rectangle's width.
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * @return the rectangle's height.
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * @return the upper left point of the rectangle.
     */
    public Point getUpperLeft() {
        return this.upLeft;
    }

    /**
     * @return the rectangle's up side.
     */
    public Line getUp() {
        return this.up;
    }

    /**
     * @return the rectangle's left side.
     */
    public Line getLeft() {
        return this.left;
    }

    /**
     * @return the rectangle's right side.
     */
    public Line getRight() {
        return this.right;
    }

    /**
     * @return the rectangle's down side.
     */
    public Line getDown() {
        return this.down;
    }
}