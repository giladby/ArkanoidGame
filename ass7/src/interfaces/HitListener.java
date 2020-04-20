package interfaces;

import sprites.Ball;
import sprites.colliadables.Block;

/**
 * The Hit listener interface.
 */
public interface HitListener {
    /**
     * This method is called whenever the beingHit object is hit.
     * The listener has unique behavior to the situation.
     *
     * @param beingHit the block which being hit.
     * @param hitter   is the Ball that's doing the hitting.
     */
    void hitEvent(Block beingHit, Ball hitter);
}
