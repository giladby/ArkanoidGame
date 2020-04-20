package collections;

import geometry.Line;
import geometry.Point;
import interfaces.Collidable;
import sprites.colliadables.CollisionInfo;

import java.util.List;
import java.util.ArrayList;

/**
 * The Game environment class.
 * This class holds a collection of objects a sprites.Ball can collides with.
 */
public class GameEnvironment {
    private List<Collidable> collidables;

    /**
     * @return the collidabble's list.
     */
    public List<Collidable> getList() {
        return collidables;
    }

    /**
     * The constructor of the game environment.
     * It initializes game environment's array list, which is the list of objects a Ball can collides with.
     */
    public GameEnvironment() {
        this.collidables = new ArrayList<Collidable>();
    }

    /**
     * Add a given objects, which a Ball can collides with, to the game environment's collection.
     *
     * @param c the given object.
     */
    public void addCollidable(Collidable c) {
        this.collidables.add(c);
    }

    /**
     * Removes a given objects, which a Ball can collides with, from the game environment's collection.
     *
     * @param c the given object.
     */
    public void removeCollidable(Collidable c) {
        this.collidables.remove(c);
    }

    /**
     * The method checks all the ball's collisions with the colliadable objects,
     * and return the info about the closest one.
     * The return info is the collision point and the object the ball Collided with.
     * If the ball didn't collide with any of the collidables, in this collection, return null.
     *
     * @param trajectory the movement of the ball.
     * @return the information about the closest collision.
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        // when flag is 0, it means that no collision found so far.
        int flag = 0;
        Point collision = null;
        CollisionInfo colInfo = null;
        double minLength = 0;
        double currentLength = 0;
        List<Collidable> temp = new ArrayList<>(this.collidables);
        // In the loop, we search the ball's collisions with the colliadable objects.
        for (Collidable c : temp) {
            collision = trajectory.closestIntersectionToStartOfLine(c.getCollisionRectangle());
            // there was a collision between the ball and the current colliadable object.
            if (collision != null) {
                // calculates the distance the ball made from his start position to the collision point.
                currentLength = collision.distance(trajectory.start());
                // no collision found yet.
                if (flag == 0) {
                    flag = 1;
                    minLength = currentLength;
                    colInfo = new CollisionInfo(collision, c);
                } else {
                    // the current collision is closer than the previous closest collision found until now.
                    if (currentLength < minLength) {
                        minLength = currentLength;
                        colInfo = new CollisionInfo(collision, c);
                    }
                }
            }
        }
        return colInfo;
    }
}
