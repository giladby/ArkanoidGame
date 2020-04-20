package animations;

import biuoop.DrawSurface;
import general.HighScoresTable;
import general.ScoreInfo;
import interfaces.Animation;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The High scores animation class.
 */
public class HighScoresAnimation implements Animation {
    private List<ScoreInfo> scores;
    private Boolean stop;
    private int size;
    private BufferedImage image;

    /**
     * The constructor for a new High scores animation, according the given scores info.
     *
     * @param scores the scores info.
     */
    public HighScoresAnimation(HighScoresTable scores) {
        this.stop = false;
        this.scores = scores.getHighScores();
    }

    /**
     * Set the screen's background images from given paths.
     *
     * @param path the path to background image.
     */
    public void setBackground(String path) {
        this.image = null;
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
            if (is != null) {
                this.image = ImageIO.read(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed loading image");
        }
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        if (this.image != null) {
            d.drawImage(0, 0, this.image);
        }
        double nameX = (double) (d.getWidth() / 4);
        double scoreX = d.getWidth() * ((double) (3 / 4)) + 30;
        int prevY;
        int addY = 50;
        int minusCenter = 170;
        int y = 60;
        this.size = 30;
        Color inside = Color.WHITE;
        Color outside = Color.RED;
        this.makeFour(d, (d.getWidth() / 2) - minusCenter, y, "High Scores",
                inside, outside, 2);
        prevY = y + addY;
        this.makeFour(d, (int) nameX, prevY + addY, "Player Name", inside, outside, 1);
        this.makeFour(d, (int) scoreX, prevY + addY, "Score", inside, outside, 1);
        this.makeFourLine(d, (int) scoreX, prevY + addY + (int) (addY / 2), (int) nameX + 150,
                inside, outside, 1);
        prevY += 1.5 * addY;
        for (ScoreInfo info : this.scores) {
            this.makeFour(d, (int) nameX, prevY + addY, info.getName(), inside, outside, 1);
            this.makeFour(d, (int) scoreX, prevY + addY, Integer.toString(info.getScore()),
                    inside, outside, 1);
            prevY += addY;
        }
        this.makeFour(d, (d.getWidth() / 2) - (int) (minusCenter * 1.5), d.getHeight() - y,
                "Press space to continue", inside, outside, 1.5);
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

    /**
     * Draw a line in special way.
     * First, the "background" line is drawn four times by one color, and then the line is drawn again by other color.
     *
     * @param d      the surface the text is drawn on.
     * @param x      the x coordinate the text is drawn at.
     * @param y      the y coordinate the text is drawn at.
     * @param length the line's length.
     * @param cPrev  the main text color.
     * @param cCur   the "background" color.
     * @param add    the addition for the background line.
     */
    private void makeFourLine(DrawSurface d, int x, int y, int length, Color cPrev, Color cCur, double add) {
        d.setColor(cCur);
        d.drawLine((int) (x + add), y, (int) (x + length + add), y);
        d.drawLine((int) (x - add), y, (int) (x + length - add), y);
        d.drawLine(x, (int) (y + add), x + length, (int) (y + add));
        d.drawLine(x, (int) (y - add), x + length, (int) (y - add));
        d.setColor(cPrev);
        d.drawLine(x, y, x + length, y);
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }
}
