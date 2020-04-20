package builders;

import geometry.Point;
import interfaces.BlockCreator;
import sprites.colliadables.Block;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * The class for creating a block with image fill.
 */
public class BlockFromImage implements BlockCreator {
    private int width;
    private int height;
    private BufferedImage image;
    private Map<Integer, Color> colorFillK;
    private Map<Integer, BufferedImage> imageFillK;
    private int hitPoints;
    private Color stroke;

    /**
     * The constructor for a new Block from image, according several given parameters.
     *
     * @param widthCur      the block's width.
     * @param heightCur     the block's height.
     * @param imageCur      the block's image.
     * @param colorFillKCur the block's number of life to color fill map.
     * @param imageFillKCur the block's number of life to image fill map.
     * @param hitPointsCur  the hit points of the block.
     * @param strokeCur     the color's borders color.
     */
    public BlockFromImage(int widthCur, int heightCur, BufferedImage imageCur, Map<Integer, Color> colorFillKCur,
                          Map<Integer, BufferedImage> imageFillKCur, int hitPointsCur, Color strokeCur) {
        this.width = widthCur;
        this.height = heightCur;
        this.image = imageCur;
        this.colorFillK = colorFillKCur;
        this.imageFillK = imageFillKCur;
        this.hitPoints = hitPointsCur;
        this.stroke = strokeCur;
    }

    @Override
    public Block create(int xpos, int ypos) {
        Block b = new Block(new Point(xpos, ypos), this.width, this.height, this.image);
        b.setImages(this.imageFillK);
        b.setColors(this.colorFillK);
        b.setStroke(this.stroke);
        b.setHits(this.hitPoints);
        return b;
    }
}
