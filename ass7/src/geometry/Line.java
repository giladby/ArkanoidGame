package geometry;

import java.util.List;

/**
 * geometry_primitives.Line class.
 */
public class Line {
    private double length;
    private Point start;
    private Point end;

    /**
     * Creates a new geometry_primitives.Line from two given points.
     *
     * @param start the start point of the line.
     * @param end   the end point of the line.
     */
    public Line(Point start, Point end) {
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());
        this.length = this.start.distance(this.end);
    }

    /**
     * Creates a new geometry_primitives.Line from two X coordinates and two Y coordinates.
     *
     * @param x1 the first X coordinate.
     * @param y1 the first Y coordinate.
     * @param x2 the second X coordinate.
     * @param y2 the second y coordinate.
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
        this.length = this.start.distance(this.end);
    }

    /**
     * Length double.
     *
     * @return the line's length.
     */
    public double length() {
        return this.length;
    }

    /**
     * Middle point.
     *
     * @return the line's middle point.
     */
    public Point middle() {
        //get the x coordinate of the middle point.
        double midX = (this.start.getX() + this.end.getX()) / 2;
        //get the y coordinate of the middle point.
        double midY = (this.start.getY() + this.end.getY()) / 2;
        return new Point(midX, midY);
    }

    /**
     * Start point.
     *
     * @return the start point of the line.
     */
    public Point start() {
        return this.start;
    }

    /**
     * End point.
     *
     * @return the end point of the line.
     */
    public Point end() {
        return this.end;
    }

    /**
     * Is intersecting boolean.
     *
     * @param other the line compared with current line.
     * @return true if the lines intersect, false otherwise.
     */
    public boolean isIntersecting(Line other) {
        if (intersectionWith(other) == null) {
            return false;
        }
        return true;
    }

    /**
     * Finds the minimum number between two numbers.
     *
     * @param n1 the first number to check.
     * @param n2 the second number to check.
     * @return the minimum number.
     */
    private double min(double n1, double n2) {
        if (n1 <= n2) {
            return n1;
        }
        return n2;
    }

    /**
     * Finds the maximum number between two numbers.
     *
     * @param n1 the first number to check.
     * @param n2 the second number to check.
     * @return the maximum number.
     */
    private double max(double n1, double n2) {
        if (n1 >= n2) {
            return n1;
        }
        return n2;
    }

    /**
     * Finds the intersection point of the lines.
     * Explanation about the variables: minuCur - the minimum x coordinate of current line.
     * maxother - the maximum x coordinate of other line.
     * maxCur - the maximum x coordinate of current line.
     * minOther - the minimum x coordinate of other line.
     * slope1 - the slope of current line.
     * slope2 - the slope of other line.
     * interX - X coordinate of intersection's point.
     * interY - Y coordinate of intersection's point.
     *
     * @param other the line compared with current line.
     * @return the intersection point of the lines. In case that the lines don't intersect, the return value is null.
     */
    public Point intersectionWith(Line other) {
        double minCur;
        double maxOther;
        double maxCur;
        double minOther;
        double slope1;
        double slope2;
        double interX;
        double interY;
        if (isPoint(this)) {
            if (isPoint(other)) {
                if (this.equals(other)) {
                    return new Point(this.start().getX(), this.start().getX());
                } else {
                    return null;
                }
            }
        }
        if (isVertical(this)) {
            if (isVertical(other)) {
                //current is vertical, other is a point.
                if (isPoint(other)) {
                    // other's X coordinate in the range of current's X coordinate.
                    if (inRange(this.start().getX(), this.end().getX(), other.start().getX())) {
                        Point inter = new Point(other.start().getX(), other.start().getY());
                        if (this.isContain(inter)) {
                            return inter;
                        }
                    }
                    return null;
                    // other isn't a point.
                } else {
                    // other and current have the same x coordinate.
                    if (this.start().getX() == other.start().getX()) {
                        //both current and other are vertical lines.
                        minCur = min(this.end().getY(), this.start().getY());
                        maxOther =
                                max(other.end().getY(), other.start().getY());
                        // special case: both lines are vertical, the bottom of current line is the top of other.
                        if (minCur == maxOther) {
                            return new Point(other.start().getX(), maxOther);
                        }
                        maxCur = max(this.end().getY(), this.start().getY());
                        minOther = min(other.end().getY(), other.start().getY());
                        // special case: both lines are vertical, the top of current line is the bottom of other.
                        if (maxCur == minOther) {
                            return new Point(other.start().getX(), minOther);
                        }
                    }
                    // the lines are parallel or one of the line contains the other.
                    return null;
                }
                // current is vertical, other isn't.
            } else {
                slope2 = getSlope(other.start(), other.end());
                interX = this.end().getX();
                interY = getInterY(slope2, other.start(), interX);
            }
            // current isn't vertical.
        } else {
            slope1 = getSlope(this.start, this.end);
            // other is a point or vertical.
            if (isVertical(other)) {
                interX = other.end().getX();
                interY = getInterY(slope1, this.start(), interX);
                // current and other aren't vertical.
            } else {
                slope2 = getSlope(other.start(), other.end());
                // the lines are parallel or the same line.
                if (slope1 == slope2) {
                    return null;
                }
                interX = getInterX(slope1, slope2, this.start, other.start());
                interY = getInterY(slope1, this.start, interX);
            }
        }
        Point inter = new Point(interX, interY);
        // the points is contained in both of lines.
        if (this.isContain(inter) && other.isContain(inter)) {
            return inter;
        }
        return null;
    }

    /**
     * Check if the line is vertical.
     *
     * @param l the line to check.
     * @return if the line is vertical returns true, otherwise returns false.
     */
    private boolean isVertical(Line l) {
        if (l.end().getX() == l.start().getX()) {
            return true;
        }
        return false;
    }

    /**
     * Calculate the slope of the line.
     * Explanation of the calculation: y1-y2=m(x1-x2) -> m=(y1-y2)/(x1-x2).
     *
     * @param p1 first point of the line.
     * @param p2 second point of the line.
     * @return the line's slope.
     */
    private double getSlope(Point p1, Point p2) {
        // x1-x2
        double dx = p1.getX() - p2.getX();
        // y1-y2
        double dy = p1.getY() - p2.getY();
        return dy / dx;
    }

    /**
     * Calculate the intersection's X coordinate.
     * Explanation of the calculation:
     * geometry_primitives.Line 1's equation: y-y1= m1(x-x1) -> y=m1(x-x1)+y1.
     * geometry_primitives.Line 2's equation: y-y2= m2(x-x2) -> y=m2(x-x2)+y2.
     * If the lines intersect, the equations are equal:
     * m1(x-x1)+y1=m2(x-x2)+y2 -> x=(m2x2-y2-(m1x1-y1))/(m2-m1).
     *
     * @param slope1 the current line's slope.
     * @param slope2 the compared line's slope.
     * @param start1 point of the current line.
     * @param start2 point of the compared line.
     * @return the intersection's X coordinate.
     */
    private double getInterX(double slope1, double slope2, Point start1, Point start2) {
        // m2-m1
        double dm = slope2 - slope1;
        // equation2: m2x2-y2
        double eq2 = slope2 * start2.getX() - start2.getY();
        // equation1: m1x1-y1.
        double eq1 = slope1 * start1.getX() - start1.getY();
        return (eq2 - eq1) / dm;
    }

    /**
     * Calculate the intersection's Y coordinate.
     * Explanation of the calculation:
     * line's equation: y-y1= m1(x-x1) -> y=m1(x-x1)+y1
     *
     * @param slope1 the line's slope.
     * @param start1 a point of the line.
     * @param xInter intersection's X coordinate.
     * @return the intersection's Y coordinate.
     */
    private double getInterY(double slope1, Point start1, double xInter) {
        // x-x1
        double dx = xInter - start1.getX();
        return slope1 * dx + start1.getY();
    }

    /**
     * Checks if the point in the coordinate range of the line.
     *
     * @param p the checked point.
     * @return true if the point in the coordinate range of the line, and returns false otherwise.
     */
    public boolean isContain(Point p) {
        // check that point's coordinates is in geometry_primitives.Line's coordinates range.
        if (inRange(this.end().getX(), this.start().getX(), p.getX())) {
            if (inRange(this.end().getY(), this.start().getY(), p.getY())) {
                return true;
            }
        }
        return false;
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
        if (n <= n1 && n >= n2) {
            return true;
        } else {
            if (n >= n1 && n <= n2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given line is a point.
     * geometry_primitives.Line is considered a point if it's start and end points are equal.
     *
     * @param l the checked line.
     * @return true if the line in a point, and return false otherwise.
     */
    private boolean isPoint(Line l) {
        if (l.start().getX() == l.end().getX()) {
            if (l.start().getY() == l.end().getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if two given lines are equal each other.
     *
     * @param other the compared line.
     * @return true if the lines are equal, and returns false otherwise.
     */
    public boolean equals(Line other) {
        if (this.start.equals(other.start())) {
            if (this.end.equals(other.end())) {
                // start's and end's points of current line
                // equal to matching points of other.
                return true;
            }
        } else {
            if (this.start.equals(other.end())) {
                if (this.end.equals(other.start())) {
                    // start's point of current equals to end's point of other,
                    // and end's point of current equals to start's point of other.
                    return true;
                }
            }
        }
        // there isn't matching between points.
        return false;
    }

    /**
     * The method calculates the closest intersection of a given rectangle with the start of line point.
     * If this line does not intersect with the rectangle, it returns null.
     * Otherwise, it returns the closest intersection point to the start of the line.
     *
     * @param rect the given rectangle.
     * @return the closest intersection.
     */
    public Point closestIntersectionToStartOfLine(Rectangle rect) {
        double minlength = 0;
        Point closest = null;
        int flag = 0;
        List<Point> intersection = rect.intersectionPoints(this);
        for (Point intersect : intersection) {
            if (flag == 0) {
                minlength = this.start.distance(intersect);
                closest = intersect;
                flag = 1;
            } else {
                if (this.start.distance(intersect) < minlength) {
                    minlength = this.start.distance(intersect);
                    closest = intersect;
                }
            }
        }
        return closest;
    }
}