package interfaces;

import sprites.colliadables.Block;

/**
 * The Block creator interface.
 */
public interface BlockCreator {
    /**
     * Create block in the position (xpos, ypos).
     *
     * @param xpos the x coordinate of creating the block.
     * @param ypos the y coordinate of creating the block.
     * @return the new created block.
     */
    Block create(int xpos, int ypos);
}