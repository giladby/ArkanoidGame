package builders;

import java.awt.Color;
import java.lang.reflect.Field;

/**
 * The Colors parser class.
 */
public class ColorsParser {

    /**
     * Convert the given color's string to the specified color.
     *
     * @param s the given string.
     * @return the color.
     */
    private Color colorFromString(String s) {
        try {
            Field field = Color.class.getField(s);
            return (Color) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed parsing the color");
        }
    }

    /**
     * Parsing the definition of the given color's string and returns the asking color.
     *
     * @param s the given string.
     * @return the color.
     */
    public Color getColor(String s) {
        if (s.startsWith("RGB(") && s.endsWith(")")) {
            String[] colorParam = (s.substring("RGB(".length(), s.length() - 1)).split(",");
            if (colorParam.length == 3) {
                return new Color(Integer.parseInt(colorParam[0]), Integer.parseInt(colorParam[1]),
                        Integer.parseInt(colorParam[2]));
            }
        }
        return this.colorFromString(s);
    }
}