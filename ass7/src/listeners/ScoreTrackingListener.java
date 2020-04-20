package listeners;

import interfaces.HitListener;
import sprites.Ball;
import sprites.colliadables.Block;
import general.Counter;

/**
 * The Score tracking listener class.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * The constructor of a new score tracking.
     * It creates a score tracking from given start number of the score.
     *
     * @param scoreCounter the given start number of the score.
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // if the ball hits a block, the player gets 5 points.
        this.currentScore.increase(5);
        // if the block is destroyed, the player gets 10 more points.
        if (beingHit.getHitPoints() == 0) {
            this.currentScore.increase(10);
        }
    }
}