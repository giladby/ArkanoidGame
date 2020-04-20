package animations;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import interfaces.Menu;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The Menu animation class.
 *
 * @param <T> the parameter type.
 */
public class MenuAnimation<T> implements Menu<T> {
    private String name;
    private List<String> keys;
    private List<String> messages;
    private List<String> subKeys;
    private List<String> subMessages;
    private List<T> values;
    private KeyboardSensor keyboard;
    private T status;
    private Boolean stop;
    private int counter;
    private int subCounter;
    private List<Menu<T>> subMenus;
    private AnimationRunner runner;
    private int size;
    private BufferedImage image;
    private boolean isAlreadyPressed;

    /**
     * The constructor of a new Menu animation.
     *
     * @param name   the menu name.
     * @param k      the keyboard sensor controls the animation.
     * @param runner the menu's runner.
     */
    public MenuAnimation(String name, KeyboardSensor k, AnimationRunner runner) {
        this.name = name;
        this.keys = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.subKeys = new ArrayList<>();
        this.subMessages = new ArrayList<>();
        this.values = new ArrayList<>();
        this.subMenus = new ArrayList<>();
        this.keyboard = k;
        this.restart();
        this.counter = 0;
        this.subCounter = 0;
        this.runner = runner;
        this.image = null;
    }

    @Override
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

    @Override
    public void addSelection(String key, String message, T returnVal) {
        this.keys.add(key);
        this.messages.add(message);
        this.values.add(returnVal);
        this.counter++;
    }

    @Override
    public T getStatus() {
        return this.status;
    }

    @Override
    public void doOneFrame(DrawSurface d) {
        int i;
        int addY = 50;
        int minusCenterX = 150;
        int prevY = 80;
        this.size = 35;
        int startX = 50;
        Color inside = Color.WHITE;
        Color outside = Color.RED;
        if (this.image != null) {
            d.drawImage(0, 0, this.image);
        }
        this.makeFour(d, d.getWidth() / 2 - minusCenterX, prevY, this.name, inside, outside, 2);
        prevY *= 2;
        for (i = 0; i < this.subCounter; i++) {
            this.makeFour(d, startX, prevY + (addY * (i + 1)),
                    this.subKeys.get(i) + " -  " + this.subMessages.get(i), inside, outside, 1);
        }
        for (i = 0; i < this.counter; i++) {
            this.makeFour(d, startX, prevY + (addY * (i + 1 + this.subCounter)),
                    this.keys.get(i) + " -  " + this.messages.get(i), inside, outside, 1);
        }
        for (i = 0; i < this.counter; i++) {
            if (this.keyboard.isPressed(this.keys.get(i))) {
                if (this.isAlreadyPressed) {
                    return;
                }
                this.stop = true;
                this.status = this.values.get(i);
                break;
            }
        }
        for (i = 0; i < this.subCounter; i++) {
            if (this.keyboard.isPressed(this.subKeys.get(i))) {
                if (this.isAlreadyPressed) {
                    return;
                }
                this.runner.run(this.subMenus.get(i));
                this.stop = true;
                this.status = this.subMenus.get(i).getStatus();
                this.subMenus.get(i).restart();
                break;
            }
        }
        this.isAlreadyPressed = false;
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
    private void makeFour(DrawSurface d, int x, int y, String text, Color cPrev, Color cCur, int add) {
        d.setColor(cCur);
        d.drawText(x - add, y, text, this.size * add);
        d.drawText(x + add, y, text, this.size * add);
        d.drawText(x, y - add, text, this.size * add);
        d.drawText(x, y + add, text, this.size * add);
        d.setColor(cPrev);
        d.drawText(x, y, text, this.size * add);
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }

    @Override
    public void restart() {
        this.status = null;
        this.stop = false;
        this.isAlreadyPressed = true;
    }

    @Override
    public void addSubMenu(String key, String message, Menu<T> subMenu) {
        this.subKeys.add(key);
        this.subMessages.add(message);
        this.subMenus.add(subMenu);
        this.subCounter++;
    }
}