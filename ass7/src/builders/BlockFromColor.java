package builders;

import geometry.Point;
import interfaces.BlockCreator;
import sprites.colliadables.Block;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * The class for creating a Block with color fill.
 */
public class BlockFromColor implements BlockCreator {
    private int width;
    private int height;
    private Color color;
    private Map<Integer, Color> colorFillK;
    private Map<Integer, BufferedImage> imageFillK;
    private int hitPoints;
    private Color stroke;

    /**
     * The constructor for a new Block from color, according several given parameters.
     *
     * @param widthCur      the block's width.
     * @param heightCur     the block's height.
     * @param color         the block's color.
     * @param colorFillKCur the block's number of life to color fill map.
     * @param imageFillKCur the block's number of life to image fill map.
     * @param hitPointsCur  the hit points of the block.
     * @param strokeCur     the color's borders color.
     */
    public BlockFromColor(int widthCur, int heightCur, Color color, Map<Integer, Color> colorFillKCur,
                          Map<Integer, BufferedImage> imageFillKCur, int hitPointsCur, Color strokeCur) {
        this.width = widthCur;
        this.height = heightCur;
        this.color = color;
        this.colorFillK = colorFillKCur;
        this.imageFillK = imageFillKCur;
        this.hitPoints = hitPointsCur;
        this.stroke = strokeCur;
    }

    @Override
    public Block create(int xpos, int ypos) {
        Block b = new Block(new Point(xpos, ypos), this.width, this.height, this.color);
        b.setImages(this.imageFillK);
        b.setColors(this.colorFillK);
        b.setStroke(this.stroke);
        b.setHits(this.hitPoints);
        return b;
    }
}
