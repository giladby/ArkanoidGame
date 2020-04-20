package sprites.colliadables;

import biuoop.DrawSurface;
import sprites.Ball;
import interfaces.Collidable;
import interfaces.HitListener;
import interfaces.Sprite;
import interfaces.HitNotifier;
import geometry.Rectangle;
import geometry.Point;
import animations.GameLevel;
import general.Velocity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Block class.
 * It an object in the game, which has rectangle shape, and implements the sprite, hit-noifier and colliadble
 * interfaces also, so block is going to be something we collide into.
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private Rectangle block;
    private Color color;
    private int hitsPoint;
    private List<HitListener> hitListeners;
    private Map<Integer, Color> colors;
    private Map<Integer, BufferedImage> images;
    private BufferedImage image;
    private Color stroke;

    /**
     * The constructor of new block with color fill.
     * It gets the rectangle's properties and the block's color.
     *
     * @param upLeft the up left.
     * @param width  the width.
     * @param height the height.
     * @param color  the color.
     */
    public Block(Point upLeft, int width, int height, Color color) {
        this.block = new Rectangle(upLeft, width, height);
        this.color = color;
        this.hitsPoint = 0;
        this.hitListeners = new ArrayList<>();
        this.colors = new TreeMap<>();
        this.images = new TreeMap<>();
        this.stroke = null;
        this.image = null;
    }

    /**
     * The constructor of new block with image fill.
     * It gets the rectangle's properties and the block's fill.
     *
     * @param upLeft the up left.
     * @param width  the width.
     * @param height the height.
     * @param image  the block's fill.
     */
    public Block(Point upLeft, int width, int height, BufferedImage image) {
        this.block = new Rectangle(upLeft, width, height);
        this.image = image;
        this.color = null;
        this.hitsPoint = 0;
        this.hitListeners = new ArrayList<>();
        this.colors = new TreeMap<>();
        this.images = new TreeMap<>();
        this.stroke = null;
    }

    /**
     * Sets the number of points to color fill map by a given map.
     *
     * @param map the given map
     */
    public void setColors(Map<Integer, Color> map) {
        this.colors = map;
    }

    /**
     * Sets the number of points to image fill map by a given map.
     *
     * @param map the the given map
     */
    public void setImages(Map<Integer, BufferedImage> map) {
        this.images = map;
    }

    /**
     * Sets the color of borders by a given color.
     *
     * @param c the given color.
     */
    public void setStroke(Color c) {
        this.stroke = c;
    }

    /**
     * Set the hit's point of the block.
     *
     * @param points the hit's points.
     */
    public void setHits(int points) {
        this.hitsPoint = points;
    }

    /**
     * @return this block, which the ball collided at.
     */
    @Override
    public Rectangle getCollisionRectangle() {
        return this.block;
    }

    /**
     * The methods updates the block's hit points, and calculates the new velocity of the ball,
     * according the location of the collision point.
     *
     * @param hitter          is the ball which hits the block.
     * @param collisionPoint  is the point where the collision occurred.
     * @param currentVelocity is the ball's velocity before the hit.
     * @return the new velocity of the ball.
     */
    @Override
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        if (this.hitsPoint > 0) {
            this.hitsPoint--;
        }
        this.notifyHit(hitter);
        double newDx = currentVelocity.getDx();
        double newDy = currentVelocity.getDy();
        if (this.block.getDown().isContain(collisionPoint) || this.block.getUp().isContain(collisionPoint)) {
            newDy *= -1;
        }
        if (this.block.getRight().isContain(collisionPoint) || this.block.getLeft().isContain(collisionPoint)) {
            newDx *= -1;
        }
        return new Velocity(newDx, newDy);
    }

    /**
     * The method prints the block on a given surface.
     *
     * @param surface is the given surface.
     */
    @Override
    public void drawOn(DrawSurface surface) {
        int x = (int) this.block.getUpperLeft().getX();
        int y = (int) this.block.getUpperLeft().getY();
        if (this.color != null) {
            surface.setColor(this.color);
            if (this.images.containsKey(this.hitsPoint)) {
                surface.drawImage(x, y, this.images.get(this.hitsPoint));
            } else {
                if (this.colors.containsKey(this.hitsPoint)) {
                    surface.setColor(this.colors.get(this.hitsPoint));
                } else {
                    surface.fillRectangle(x, y, (int) this.block.getWidth(), (int) this.block.getHeight());
                }
            }
        } else {
            if (this.image != null) {
                if (this.colors.containsKey(this.hitsPoint)) {
                    surface.setColor(this.colors.get(this.hitsPoint));
                    surface.fillRectangle(x, y, (int) this.block.getWidth(), (int) this.block.getHeight());
                } else {
                    if (this.images.containsKey(this.hitsPoint)) {
                        surface.drawImage(x, y, this.images.get(this.hitsPoint));
                    } else {
                        surface.drawImage(x, y, this.image);
                    }
                }
            } else {
                if (this.colors.containsKey(this.hitsPoint)) {
                    surface.setColor(this.colors.get(this.hitsPoint));
                    surface.fillRectangle(x, y, (int) this.block.getWidth(), (int) this.block.getHeight());
                } else {
                    if (this.images.containsKey(this.hitsPoint)) {
                        surface.drawImage(x, y, this.images.get(this.hitsPoint));
                    }
                }
            }
        }
        if (this.stroke != null) {
            surface.setColor(this.stroke);
            surface.drawRectangle(x, y, (int) this.block.getWidth(), (int) this.block.getHeight());
        }
    }

    /**
     * Notify the block that it should makes it's next move.
     */
    @Override
    public void timePassed() {
    }

    /**
     * Adds the block to a given game.
     *
     * @param g is the given game.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * Removes the block from a given game.
     *
     * @param game is the given game.
     */
    public void removeFromGame(GameLevel game) {
        game.removeSprite(this);
        game.removeCollidable(this);
    }

    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * Notifies all the listeners that the block is hit by a given ball.
     *
     * @param hitter is the given ball.
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event.
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * Gets hit points.
     *
     * @return the hit points
     */
    public int getHitPoints() {
        return this.hitsPoint;
    }
}
