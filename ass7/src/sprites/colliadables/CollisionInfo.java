package sprites.colliadables;

import geometry.Point;
import interfaces.Collidable;

/**
 * The Collision info class.
 * It holds the information about the collision.
 */
public class CollisionInfo {
    private Point collision;
    private Collidable collidable;

    /**
     * The constructor of a new Collision info.
     *
     * @param p the collision point.
     * @param c the colliadble object, which the ball hit at.
     */
    public CollisionInfo(Point p, Collidable c) {
        this.collision = p;
        this.collidable = c;
    }

    /**
     * @return the point at which the collision occurs.
     */
    public Point collisionPoint() {
        return this.collision;
    }

    /**
     * @return the collidable object involved in the collision.
     */
    public Collidable collisionObject() {
        return this.collidable;
    }
}
