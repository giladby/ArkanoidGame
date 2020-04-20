package interfaces;

import general.Velocity;
import sprites.colliadables.Block;

import java.util.List;

/**
 * The Level information interface.
 */
public interface LevelInformation {
    /**
     * @return the amount of balls the level starts with.
     */
    int numberOfBalls();

    /**
     * This is the list of velocities, which the balls initialize with.
     *
     * @return the list of the velocities.
     */
    List<Velocity> initialBallVelocities();

    /**
     * @return the paddle's speed.
     */
    int paddleSpeed();

    /**
     * @return the paddle's width.
     */
    int paddleWidth();

    /**
     * @return the level's name, which will be displayed at the top of the screen.
     */
    String levelName();

    /**
     * Each level has unique background.
     *
     * @return the background of the current level.
     */
    Sprite getBackground();

    /**
     * This is the list of the blocks, the level starts with.
     * Each block contains the necessary information.
     *
     * @return the list of blocks, which the level starts with.
     */
    List<Block> blocks();

    /**
     * Number of blocks to remove int.
     *
     * @return the amount of blocks that the user has to 'kill' in order to win the level.
     */
    int numberOfBlocksToRemove();
}
