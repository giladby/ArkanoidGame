package builders;

import biuoop.DrawSurface;
import interfaces.Sprite;

import java.awt.Color;

/**
 * The class for creating a background from color.
 */
public class BackgroundFromColor implements Sprite {
    private Color color;

    /**
     * The constructor for a new background from color.
     * It creates a single color background from a given color.
     *
     * @param c the given color.
     */
    public BackgroundFromColor(Color c) {
        this.color = c;
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
    }

    @Override
    public void timePassed() {

    }
}
