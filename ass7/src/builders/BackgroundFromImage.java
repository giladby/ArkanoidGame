package builders;

import biuoop.DrawSurface;
import interfaces.Sprite;

import java.awt.image.BufferedImage;

/**
 * The class for creating a background from image.
 */
public class BackgroundFromImage implements Sprite {
    private BufferedImage imageBuffer;

    /**
     * The constructor for a new Background from image, according the given image.
     *
     * @param imageBuffer the given image.
     */
    public BackgroundFromImage(BufferedImage imageBuffer) {
        this.imageBuffer = imageBuffer;
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.drawImage(0, 0, this.imageBuffer);
    }

    @Override
    public void timePassed() {
    }
}
