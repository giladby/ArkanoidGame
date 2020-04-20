package interfaces;

import sprites.Ball;
import geometry.Rectangle;
import geometry.Point;
import general.Velocity;

/**
 * The interface Collidable.
 * The interface will be used by things that can be collided with.
 */
public interface Collidable {

    /**
     * @return the "collision shape" of the object.
     */
    Rectangle getCollisionRectangle();

    /**
     * The method calculates the new velocity of the object, which we collided with it at collisionPoint with.
     * The calculation is based on the force the object inflicted on us.
     *
     * @param hitter          the ball which hits the object.
     * @param collisionPoint  the collision point.
     * @param currentVelocity the previous velocity of the object.
     * @return the object's new velocity.
     */
    Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
