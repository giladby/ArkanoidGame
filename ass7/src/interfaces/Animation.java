package interfaces;

import biuoop.DrawSurface;

/**
 * The Animation interface.
 */
public interface Animation {
    /**
     * This method charges on one loop of events, which is specific to each animation.
     *
     * @param d the surface.
     */
    void doOneFrame(DrawSurface d);

    /**
     * @return if the animation has to be finished.
     */
    boolean shouldStop();
}