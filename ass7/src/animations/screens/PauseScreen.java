package animations.screens;

import biuoop.DrawSurface;
import interfaces.Animation;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Pause screen class.
 */
public class PauseScreen implements Animation {
    private boolean stop;
    private BufferedImage image;
    private int size;

    /**
     * The constructor of a new Pause screen.
     */
    public PauseScreen() {
        this.image = null;
        this.stop = false;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        this.size = 35;
        Color inside = Color.YELLOW;
        Color outside = Color.orange;
        if (this.image != null) {
            d.drawImage(0, 0, this.image);
            this.makeFour(d, (int) (d.getWidth() / 2) - 290, d.getHeight() - 80, "Press space to continue",
                    inside, outside, 1.5);
            return;
        }
        d.setColor(Color.RED);
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
        int pWidth = 30;
        int pHeight = 150;
        int rationHeight = 3;
        int distance = 80;
        d.setColor(Color.WHITE);
        d.fillRectangle((int) ((d.getWidth() / 2) - (distance * 1.1)), d.getHeight() / rationHeight, pWidth, pHeight);
        d.fillRectangle((d.getWidth() / 2) + (distance / 4), d.getHeight() / rationHeight, pWidth, pHeight);
        int circleNum = 6;
        for (int i = 0; i < circleNum; i++) {
            d.drawCircle((d.getWidth() / 2) - (18 + i), (d.getHeight() / 2) - 30, 130);
        }
        this.makeFour(d, (int) ((d.getWidth() / 2) - (distance * 2)), (int) ((d.getHeight() / rationHeight)
                + (distance * 3.25)), "Paused", inside, outside, 2.5);
        this.makeFour(d, (int) ((d.getWidth() / 2) - (distance * 3)), (int) ((d.getHeight() / rationHeight)
                + (distance * 3.75)), "Press space to continue", inside, outside, 1.3);
    }

    /**
     * Set the screen's background images from given paths.
     *
     * @param path the path to background image.
     */
    public void setBackground(String path) {
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
            if (is != null) {
                this.image = ImageIO.read(is);
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
