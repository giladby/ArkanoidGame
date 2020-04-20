package collections;

import java.util.List;
import java.util.ArrayList;

import biuoop.DrawSurface;
import interfaces.Sprite;

/**
 * The Sprite collection class is the collection of all the game objects that can be drawn to the screen.
 */
public class SpriteCollection {
    private List<Sprite> sprites;

    /**
     * The constructor of collections.SpriteCollection.
     * It initializes the array list, which will contain the game's sprites.
     */
    public SpriteCollection() {
        this.sprites = new ArrayList<Sprite>();
    }

    /**
     * Add sprite methods.
     * It adds the given sprite to the array list.
     *
     * @param s the give sprite.
     */
    public void addSprite(Sprite s) {
        sprites.add(s);
    }

    /**
     * The method adds the given sprite to the given position of the array list.
     *
     * @param s the give sprite.
     * @param number the give number.
     */
    public void addToPosition(Sprite s, int number) {
        sprites.add(number, s);
    }

    /**
     * Remove sprite methods.
     * It removes the given sprite from the array list.
     *
     * @param s the give sprite.
     */
    public void removeSprite(Sprite s) {
        sprites.remove(s);
    }

    /**
     * The method charges to notify all the sprites in the array list to make their next move.
     */
    public void notifyAllTimePassed() {
        List<Sprite> temp = new ArrayList<>(this.sprites);
        for (Sprite s : temp) {
            s.timePassed();
        }
    }

    /**
     * The method charges to notify all the sprites in the array list to be dorwn on the given surface.
     *
     * @param d the given surface.
     */
    public void drawAllOn(DrawSurface d) {
        List<Sprite> temp = new ArrayList<>(this.sprites);
        for (Sprite s : temp) {
            s.drawOn(d);
        }
    }
}
