package interfaces;

import biuoop.DrawSurface;

/**
 * The interface Sprite.
 * Sprite is a game object that can be drawn to the screen.
 */
public interface Sprite {
    /**
     * The games object is drawn on a given surface.
     *
     * @param d the given surface.
     */
    void drawOn(DrawSurface d);

    /**
     * Notify the sprite that time has passed, and it should make it's next move.
     */
    void timePassed();
}
