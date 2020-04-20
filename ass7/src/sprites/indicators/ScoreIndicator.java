package sprites.indicators;

import biuoop.DrawSurface;

import geometry.Rectangle;
import general.Counter;
import interfaces.Sprite;

import java.awt.Color;

/**
 * The Score indicator class.
 */
public class ScoreIndicator implements Sprite {
    private Counter score;
    private Rectangle rect;
    private Color color;
    private int textSize;
    private double distance;

    /**
     * The constructor of a new score indicator.
     * It creates a new indicator for given parameters.
     *
     * @param score    the start number of score.
     * @param rect     the rectangle's shape of the indicator.
     * @param textSize the size of the score's value is printed.
     * @param color    the rectangle's color.
     * @param distance the ratio of the screen's wide, that adds to left border, and the sprite is displayed there.
     */
    public ScoreIndicator(Counter score, Rectangle rect, int textSize, Color color, double distance) {
        this.score = score;
        this.rect = rect;
        this.color = color;
        this.textSize = textSize;
        this.distance = distance;
    }

    @Override
    public void drawOn(DrawSurface d) {
        printRectangle(d);
        printScore(d);
    }

    /**
     * Prints the score's value on a given surface.
     *
     * @param surface the given surface.
     */
    private void printScore(DrawSurface surface) {
        surface.setColor(Color.BLACK);
        String text = "Score: " + this.score.getValue();
        surface.drawText((int) (this.rect.getUpperLeft().getX() + (this.rect.getWidth() / this.distance)),
                (int) (this.rect.getUpperLeft().getY() + this.textSize), text, this.textSize);
    }

    /**
     * Prints the rectangle's shape on a given surface.
     *
     * @param surface the given surface.
     */
    private void printRectangle(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillRectangle((int) this.rect.getUpperLeft().getX(), (int) this.rect.getUpperLeft().getY(),
                (int) this.rect.getWidth(), (int) this.rect.getHeight());
        surface.setColor(Color.BLACK);
        surface.drawRectangle((int) this.rect.getUpperLeft().getX(), (int) this.rect.getUpperLeft().getY(),
                (int) this.rect.getWidth(), (int) this.rect.getHeight());
    }

    @Override
    public void timePassed() {
    }
}
