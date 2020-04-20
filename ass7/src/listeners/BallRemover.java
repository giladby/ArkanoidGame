package listeners;

import interfaces.HitListener;
import animations.GameLevel;
import general.Counter;
import sprites.Ball;
import sprites.colliadables.Block;

/**
 * The Ball remover class.
 */
public class BallRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBalls;

    /**
     * The constructor of a new Ball remover.
     * It creates a ball remover from several given parameters.
     *
     * @param game           the ball remover's game.
     * @param remainingBalls the remaining balls in the game.
     */
    public BallRemover(GameLevel game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    @Override
    public void hitEvent(Block deathRegion, Ball hitter) {
        //when the ball hits the bottom block, it removes from game.
        hitter.removeFromGame(this.game);
        this.remainingBalls.decrease(1);
    }
}
