package animations;

import biuoop.DrawSurface;
import biuoop.Sleeper;
import collections.SpriteCollection;
import interfaces.Animation;

import java.awt.Color;

/**
 * The Countdown animation.
 */
public class CountdownAnimation implements Animation {
    private double numOfSeconds;
    private int countFrom;
    private int current;
    private SpriteCollection gameScreen;
    private boolean stop;

    /**
     * The constructor of a new Countdown animation.
     *
     * @param numOfSeconds the num of seconds
     * @param countFrom    the count from
     * @param gameScreen   the game screen
     */
    public CountdownAnimation(double numOfSeconds, int countFrom, SpriteCollection gameScreen) {
        this.numOfSeconds = numOfSeconds;
        this.countFrom = countFrom;
        this.gameScreen = gameScreen;
        this.stop = false;
        this.current = countFrom;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        biuoop.Sleeper sleeper = new Sleeper();
        // the calculation adds to countfrom + 1 for the 'Go!' string.
        long sleep = (long) ((this.numOfSeconds / (this.countFrom + 1)) * 1000);
        // this methods runs before the animation is displayed on the screen, so the sleeping has to occur after one
        // doOneFrame loop.
        if (this.current < this.countFrom) {
            sleeper.sleepFor(sleep);
        }
        this.gameScreen.drawAllOn(d);
        d.setColor(Color.BLACK);
        d.fillCircle(d.getWidth() / 2, d.getHeight() / 2, 32);
        d.setColor(Color.WHITE);
        d.drawCircle(d.getWidth() / 2, d.getHeight() / 2, 32);
        if (this.current > 0) {
            d.drawText((d.getWidth() / 2) - 8, (d.getHeight() / 2) + 8, Integer.toString(this.current), 32);
        // after displaying all the numbers.
        } else {
            if (this.current == 0) {
                d.drawText((d.getWidth() / 2) - 27, (d.getHeight() / 2) + 8, "Go!", 32);
            }
        }
        this.current--;
        // because the sleeping has to occur after one doOneFrame loop,
        // in order to display the 'Go' string for the requiring time, the loop has to be occur one extra time.
        if (this.current < -1) {
            this.stop = true;
        }
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }
}
