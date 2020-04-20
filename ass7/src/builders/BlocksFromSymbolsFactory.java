package builders;

import interfaces.BlockCreator;
import sprites.colliadables.Block;

import java.util.Map;
import java.util.TreeMap;

/**
 * The Blocks from symbols factory class.
 */
public class BlocksFromSymbolsFactory {
    private Map<String, Integer> spacerWidths;
    private Map<String, BlockCreator> blockCreators;

    /**
     * The constructor of a new Blocks from symbols factory.
     */
    public BlocksFromSymbolsFactory() {
        this.spacerWidths = new TreeMap<>();
        this.blockCreators = new TreeMap<>();
    }

    /**
     * Check if the given string is one of the factory's space symbols.
     *
     * @param s the given string
     * @return true if the given string is one of the factory's space symbols, and false otherwise.
     */
    public boolean isSpaceSymbol(String s) {
        return this.spacerWidths.containsKey(s);
    }

    /**
     * Check if the given string is one of the factory's block symbols.
     *
     * @param s the given string
     * @return true if the given string is one of the factory's block symbols, and false otherwise.
     */
    public boolean isBlockSymbol(String s) {
        return this.blockCreators.containsKey(s);
    }

    /**
     * Get the block, which the given string represents.
     *
     * @param s    the given string.
     * @param xpos the x coordinate to create the block.
     * @param ypos the y coordinate to create the block.
     * @return the block.
     */
    public Block getBlock(String s, int xpos, int ypos) {
        return this.blockCreators.get(s).create(xpos, ypos);
    }

    /**
     * Get the width of the space, which the given string represents.
     *
     * @param s the given string.
     * @return the space's width.
     */
// Returns the width in pixels associated with the given spacer-symbol.
    public int getSpaceWidth(String s) {
        return this.spacerWidths.get(s);
    }

    /**
     * Add the factory's string-to-block creator map another pair,
     * which composed from given string and s given block creator.
     *
     * @param s       the given string.
     * @param creator the given block creator.
     */
    public void putBlock(String s, BlockCreator creator) {
        this.blockCreators.put(s, creator);
    }

    /**
     * Add the factory's string-to-width of spaces map another pair,
     * which composed from given string and s given width.
     *
     * @param s     the given string.
     * @param width the given width.
     */
    public void putSpace(String s, int width) {
        this.spacerWidths.put(s, width);
    }
}
