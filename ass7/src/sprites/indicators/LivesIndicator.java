package sprites.indicators;

import biuoop.DrawSurface;
import interfaces.Sprite;
import general.Counter;
import geometry.Rectangle;

import java.awt.Color;

/**
 * The Lives indicator class.
 */
public class LivesIndicator implements Sprite {
    private Counter lives;
    private Rectangle rect;
    private int textSize;
    private double distance;

    /**
     * The constructor of a new Lives indicator.
     * It creates a new indicator for given parameters.
     *
     * @param lives    the start number of lives.
     * @param rect     the rectangle's shape of the indicator.
     * @param textSize the size of the lives's value is printed.
     * @param distance the ratio of the screen's wide, that adds to left border, and the sprite is displayed there.
     */
    public LivesIndicator(Counter lives, Rectangle rect, int textSize, double distance) {
        this.lives = lives;
        this.rect = rect;
        this.textSize = textSize;
        this.distance = distance;
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.BLACK);
        String text = "Lives: " + this.lives.getValue();
        d.drawText((int) (this.rect.getUpperLeft().getX() + (this.rect.getWidth() / this.distance)),
                (int) (this.rect.getUpperLeft().getY() + this.textSize), text, this.textSize);
    }

    @Override
    public void timePassed() {
    }
}
