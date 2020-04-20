package animations.screens;

import biuoop.DrawSurface;
import general.Counter;
import interfaces.Animation;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * The End screen class.
 */
public class EndScreen implements Animation {
    private boolean stop;
    private Counter lives;
    private Counter score;
    private BufferedImage imageWin;
    private BufferedImage imageLose;
    private int size;

    /**
     * The constructor a new End screen.
     *
     * @param lives the lives counter.
     * @param score the score counter.
     */
    public EndScreen(Counter lives, Counter score) {
        this.stop = false;
        this.lives = lives;
        this.score = score;
        this.imageWin = null;
        this.imageLose = null;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        this.size = 35;
        Color inside = Color.YELLOW;
        Color outside = Color.RED;
        d.setColor(Color.BLACK);
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
        String s;
        int baseWidth = 300;
        int baseHeight = 30;
        int leftCorner = (d.getWidth() - baseWidth) / 2;
        int distance = d.getHeight() / 2;
        int locationHeight = d.getHeight() - baseHeight - distance;
        int sideWidth = baseHeight;
        int sideLength = baseWidth / 2;
        double dis = 2;
        int sideHeight = locationHeight - sideLength;
        int rightCornet = leftCorner + baseWidth - sideWidth;
        int addition = 0;
        int addition2 = 0;
        // there are no lives lest.
        if (lives.getValue() == 0) {
            if (this.imageLose != null) {
                d.drawImage(0, 0, this.imageLose);
                this.makeFour(d, d.getWidth() / 2 - 260, d.getHeight() - 30,
                        "Your score is " + this.score.getValue(), inside, outside, 2);
                return;
            }
            s = "Game Over";
            dis = 2.65;
            locationHeight -= 50;
            sideLength /= 2;
            addition = 100;
            addition2 = 50;
            sideHeight = locationHeight + baseHeight;
            // the user wins all the levels.
        } else {
            if (this.imageWin != null) {
                d.drawImage(0, 0, this.imageWin);
                this.makeFour(d, d.getWidth() / 2 - 260, d.getHeight() - 30,
                        "Your score is " + this.score.getValue(), inside, outside, 2);
                return;
            }
            s = "You Win! ";
        }
        d.setColor(Color.YELLOW);
        d.fillRectangle(leftCorner, locationHeight, baseWidth, baseHeight);
        d.fillRectangle(leftCorner, sideHeight, sideWidth, sideLength);
        d.fillRectangle(rightCornet, sideHeight, sideWidth, sideLength);
        d.fillOval(leftCorner + 50, locationHeight - 200 + addition2, 80, 120);
        d.fillOval(leftCorner + 160, locationHeight - 200 + addition2, 80, 120);
        for (int i = 0; i < 6; i++) {
            if (i < 3) {
                d.drawOval(leftCorner - 50 + i, sideHeight - 100 - addition, 400, 350);
            } else {
                d.drawOval(leftCorner - 50, sideHeight - 100 - addition, 400, 350 + (i - 2));
            }
        }
        int rationHeight = 3;
        distance = 80;
        this.makeFour(d, (int) ((d.getWidth() / 2) - (distance * dis) - 60),
                (int) ((d.getHeight() / rationHeight) + (distance * 3.75)), s, inside, outside, 3);
        this.makeFour(d, (int) ((d.getWidth() / 2) - (distance * 2.5)), (int) ((d.getHeight() / rationHeight)
                + (distance * 4.5)), "Your score is " + this.score.getValue(), inside, outside, 1.5);
        d.setColor(Color.BLACK);
        d.fillRectangle(leftCorner, locationHeight, baseHeight, baseHeight);
        d.fillRectangle(rightCornet, locationHeight, baseHeight, baseHeight);
        d.fillOval(leftCorner + 65, locationHeight - 150 + addition2, 50, 60);
        d.fillOval(leftCorner + 175, locationHeight - 150 + addition2, 50, 60);

    }

    /**
     * Set the screen's background images for win and lose situations from given paths.
     *
     * @param pathWin  the path to win background image.
     * @param pathLose the path to lose background image.
     */
    public void setBackground(String pathWin, String pathLose) {
        InputStream is = null;
        try {
            is = ClassLoader.getSystemClassLoader().getResourceAsStream(pathWin);
            if (is != null) {
                this.imageWin = ImageIO.read(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed loading image");
        }
        try {
            is = ClassLoader.getSystemClassLoader().getResourceAsStream(pathLose);
            if (is != null) {
                this.imageLose = ImageIO.read(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed loading image");
        }
    }

    /**
     * Draw a text in special way.
     * First, the "background" text is drawn four times by one color, and then the text is drawn again by other color.
     *
     * @param d     the surface the text is drawn on.
     * @param x     the x coordinate the text is drawn at.
     * @param y     the y coordinate the text is drawn at.
     * @param text  the text to draw.
     * @param cPrev the main text color.
     * @param cCur  the "background" color.
     * @param add   the addition for the background text.
     */
    private void makeFour(DrawSurface d, int x, int y, String text, Color cPrev, Color cCur, double add) {
        d.setColor(cCur);
        d.drawText((int) (x - add), y, text, (int) (this.size * add));
        d.drawText((int) (x + add), y, text, (int) (this.size * add));
        d.drawText(x, (int) (y - add), text, (int) (this.size * add));
        d.drawText(x, (int) (y + add), text, (int) (this.size * add));
        d.setColor(cPrev);
        d.drawText(x, y, text, (int) (this.size * add));
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }
}
