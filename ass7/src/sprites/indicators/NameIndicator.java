package sprites.indicators;

import biuoop.DrawSurface;
import geometry.Rectangle;
import interfaces.Sprite;

import java.awt.Color;

/**
 * The Name indicator class.
 */
public class NameIndicator implements Sprite {
    private String name;
    private Rectangle rect;
    private int textSize;
    private double distance;

    /**
     * The constructor of a new Name indicator.
     * It creates a new indicator for given parameters.
     *
     * @param name     the level's name.
     * @param rect     the rectangle's shape of the indicator.
     * @param textSize the size of the lives's value is printed.
     * @param distance the ratio of the screen's wide, that adds to left border, and the sprite is displayed there.
     */
    public NameIndicator(String name, Rectangle rect, int textSize, double distance) {
        this.name = name;
        this.rect = rect;
        this.textSize = textSize;
        this.distance = distance;
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(Color.BLACK);
        String text = "Level Name: " + this.name;
        d.drawText((int) (this.rect.getUpperLeft().getX() + (this.rect.getWidth() / this.distance)),
                (int) (this.rect.getUpperLeft().getY() + this.textSize), text, this.textSize);
    }

    @Override
    public void timePassed() {
    }
}
