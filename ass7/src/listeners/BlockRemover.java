package listeners;

import sprites.Ball;
import sprites.colliadables.Block;
import interfaces.HitListener;
import animations.GameLevel;
import general.Counter;

/**
 * The Block remover class.
 */
public class BlockRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBlocks;

    /**
     * The constructor of a new blocks remover.
     * It creates a blocks remover from several given parameters.
     *
     * @param game          the blocks remover's game.
     * @param removedBlocks the remaining blocks in the game.
     */
    public BlockRemover(GameLevel game, Counter removedBlocks) {
        this.game = game;
        this.remainingBlocks = removedBlocks;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        // when the block's points are down to zero, it removed from the game and the listener is removed.
        if (beingHit.getHitPoints() == 0) {
            beingHit.removeHitListener(this);
            beingHit.removeFromGame(this.game);
            this.remainingBlocks.decrease(1);
        }
    }
}
