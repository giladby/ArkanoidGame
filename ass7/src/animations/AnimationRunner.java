package animations;

import biuoop.DrawSurface;
import biuoop.GUI;
import interfaces.Animation;

/**
 * The Animation runner.
 * It takes an Animation object and runs it.
 */
public class AnimationRunner {
    private GUI gui;
    private int framesPerSecond;
    private biuoop.Sleeper sleeper;

    /**
     * The constructor of a new Animation runner.
     *
     * @param framesPerSecond the frames which display per second.
     * @param gui             the gui.
     */
    public AnimationRunner(int framesPerSecond, GUI gui) {
        this.framesPerSecond = framesPerSecond;
        this.gui = gui;
        this.sleeper = new biuoop.Sleeper();
    }

    /**
     * Runs the given animation object.
     *
     * @param animation the given animation.
     */
    public void run(Animation animation) {
        int millisecondsPerFrame = 1000 / this.framesPerSecond;
        while (true) {
            long startTime = System.currentTimeMillis();
            DrawSurface d = gui.getDrawSurface();
            animation.doOneFrame(d);
            if (animation.shouldStop()) {
                break;
            }
            gui.show(d);
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                this.sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }
}
