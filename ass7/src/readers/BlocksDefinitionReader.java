package readers;

import builders.BlockFromColor;
import builders.BlockFromImage;
import builders.BlocksFromSymbolsFactory;
import builders.ColorsParser;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The Blocks definition reader class.
 */
public class BlocksDefinitionReader {
    private int width;
    private int height;
    private int hitPoints;
    private Color stroke;
    private String symbol;
    private Map<Integer, Color> colorFillK;
    private Map<Integer, BufferedImage> imageFillK;
    private Color fill;
    private BufferedImage image;

    /**
     * The constructor for a new Blocks definition reader.
     */
    public BlocksDefinitionReader() {
        this.width = -1;
        this.height = -1;
        this.hitPoints = -1;
        this.stroke = null;
        this.symbol = null;
        this.colorFillK = new TreeMap<>();
        this.imageFillK = new TreeMap<>();
        this.fill = null;
        this.image = null;
    }

    /**
     * Create factory of creating block according the given file's reader.
     *
     * @param reader the given reader.
     * @return the factory.
     */
    public static BlocksFromSymbolsFactory fromReader(Reader reader) {
        return new BlocksDefinitionReader().createFactor(reader);
    }

    /**
     * Create factory of creating block according the given file's reader.
     *
     * @param reader the given reader.
     * @return the factory.
     */
    private BlocksFromSymbolsFactory createFactor(Reader reader) {
        BlocksFromSymbolsFactory factory = new BlocksFromSymbolsFactory();
        BufferedReader bufferedReader = (BufferedReader) reader;
        String line;
        List<String> bdef = new ArrayList<>();
        List<String> sdef = new ArrayList<>();
        String defaultS = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("") || line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("bdef ")) {
                    bdef.add(line.substring("bdef ".length()));
                    continue;
                }
                if (line.startsWith("sdef ")) {
                    sdef.add(line.substring("sdef ".length()));
                    continue;
                }
                if (line.startsWith("default ")) {
                    defaultS = line;
                    continue;
                }
                // invalid format
                throw new RuntimeException("invalid parameters in blocks file");
            }
        } catch (IOException e) {
            System.err.println("Failed reading blocks file");
            e.printStackTrace(System.err);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                    if (!defaultS.equals("")) {
                        this.setDefaults(defaultS.substring("default ".length()));
                    }
                    this.putBlocks(bdef, factory);
                    this.putSpaces(sdef, factory);
                }
            } catch (IOException e) {
                System.err.println("Failed closing blocks file");
            }
        }
        return factory;
    }

    /**
     * Populate the string to space's width map of the factory,
     * according the given list contain the definitions from file.
     *
     * @param sdef    the given list.
     * @param factory the factory to populate.
     */
    private void putSpaces(List<String> sdef, BlocksFromSymbolsFactory factory) {
        for (int i = 0; i < sdef.size(); i++) {
            String symbolCur = null;
            int widthCur = -1;
            String[] words = sdef.get(i).split(" ");
            for (int j = 0; j < words.length; j++) {
                String[] pairs = words[j].split(":");
                if (pairs[0].equals("symbol")) {
                    symbolCur = pairs[1];
                    continue;
                }
                if (pairs[0].equals("width")) {
                    widthCur = Integer.parseInt(pairs[1]);
                }
            }
            if (symbolCur != null && widthCur >= 0) {
                factory.putSpace(symbolCur, widthCur);
            }
        }
    }

    /**
     * Populate the string to block creator map of the factory,
     * according the given list contain the definitions from file.
     *
     * @param bdef    the given list.
     * @param factory the factory to populate.
     */
    private void putBlocks(List<String> bdef, BlocksFromSymbolsFactory factory) {
        int widthCur, heightCur, hitPointsCur;
        Color strokeCur, fillCur;
        String symbolCur;
        Map<Integer, Color> colorFillKCur;
        Map<Integer, BufferedImage> imageFillKCur;
        BufferedImage imageCur;
        for (int i = 0; i < bdef.size(); i++) {
            String[] words = bdef.get(i).split(" ");
            widthCur = -1;
            heightCur = -1;
            hitPointsCur = -1;
            strokeCur = null;
            symbolCur = null;
            colorFillKCur = new TreeMap<>();
            imageFillKCur = new TreeMap<>();
            fillCur = null;
            imageCur = null;
            for (int j = 0; j < words.length; j++) {
                String[] pairs = words[j].split(":");
                if (pairs[0].equals("symbol")) {
                    symbolCur = pairs[1];
                    continue;
                }
                if (pairs[0].equals("height")) {
                    heightCur = Integer.parseInt(pairs[1]);
                    continue;
                }
                if (pairs[0].equals("width")) {
                    widthCur = Integer.parseInt(pairs[1]);
                    continue;
                }
                if (pairs[0].equals("hit_points")) {
                    hitPointsCur = Integer.parseInt(pairs[1]);
                    continue;
                }
                if (pairs[0].startsWith("fill-")) {
                    int k = Integer.parseInt(pairs[0].substring("fill-".length()));
                    if (pairs[1].startsWith("color(") && pairs[1].endsWith(")")) {
                        Color color = new ColorsParser().getColor(pairs[1].substring("color(".length(),
                                pairs[1].length() - 1));
                        colorFillKCur.put(k, color);
                        continue;
                    }
                    if (pairs[1].startsWith("image(") && pairs[1].endsWith(")")) {
                        String imagePath = pairs[1].substring("image(".length(), pairs[1].length() - 1);
                        BufferedImage imageBuffer = null;
                        try {
                            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(imagePath);
                            if (is != null) {
                                imageBuffer = ImageIO.read(is);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Failed loading image");
                        }
                        imageFillKCur.put(k, imageBuffer);
                        continue;
                    }
                }
                if (pairs[0].equals("stroke")) {
                    if (pairs[1].startsWith("color(") && pairs[1].endsWith(")")) {
                        strokeCur = new ColorsParser().getColor(pairs[1].substring("color(".length(),
                                pairs[1].length() - 1));
                    }
                    continue;
                }
                if (pairs[0].equals("fill")) {
                    if (pairs[1].startsWith("color(") && pairs[1].endsWith(")")) {
                        fillCur = new ColorsParser().getColor(pairs[1].substring("color(".length(),
                                pairs[1].length() - 1));
                    } else {
                        if (pairs[1].startsWith("image(") && pairs[1].endsWith(")")) {
                            String imagePath = pairs[1].substring("image(".length(), pairs[1].length() - 1);
                            try {
                                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(imagePath);
                                if (is != null) {
                                    imageCur = ImageIO.read(is);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException("Failed loading image");
                            }
                        }
                    }
                }
            }
            if (widthCur < 0) {
                if (this.width >= 0) {
                    widthCur = this.width;
                }
            }
            if (heightCur < 0) {
                if (this.height >= 0) {
                    heightCur = this.height;
                }
            }
            if (hitPointsCur < 0) {
                if (this.hitPoints >= 0) {
                    hitPointsCur = this.hitPoints;
                }
            }
            if (strokeCur == null) {
                if (this.stroke != null) {
                    strokeCur = this.stroke;
                }
            }
            if (symbolCur == null) {
                if (this.symbol != null) {
                    symbolCur = this.symbol;
                }
            }
            if (widthCur < 0 || heightCur < 0 || hitPointsCur < 0 || symbolCur == null) {
                throw new RuntimeException("Invalid Paramenters");
            }
            for (int j : this.imageFillK.keySet()) {
                if (!imageFillKCur.containsKey(j)) {
                    imageFillKCur.put(j, this.imageFillK.get(j));
                }
            }
            for (int j : this.colorFillK.keySet()) {
                if (!colorFillKCur.containsKey(j)) {
                    colorFillKCur.put(j, this.colorFillK.get(j));
                }
            }
            if (fillCur == null && imageCur == null) {
                if (this.fill == null) {
                    if (this.image == null) {
                        for (int k = 1; k <= hitPointsCur; k++) {
                            if (!colorFillKCur.containsKey(k) && !imageFillKCur.containsKey(k)) {
                                throw new RuntimeException("Invalid Paramenters");
                            }
                        }
                    } else {
                        imageCur = this.image;
                    }
                } else {
                    fillCur = this.fill;
                }
            }
            //create block with fill of color.
            if (imageCur == null) {
                factory.putBlock(symbolCur, new BlockFromColor(widthCur, heightCur, fillCur,
                        colorFillKCur, imageFillKCur, hitPointsCur, strokeCur));
                //create block with fill of image.
            } else {
                factory.putBlock(symbolCur, new BlockFromImage(widthCur, heightCur, imageCur,
                        colorFillKCur, imageFillKCur, hitPointsCur, strokeCur));
            }
        }
    }

    /**
     * Populate the default block parameters according the given string contain the default definitions from file.
     *
     * @param defaultS the given string.
     */
    private void setDefaults(String defaultS) {
        String[] words = defaultS.split(" ");
        for (int i = 0; i < words.length; i++) {
            String[] pairs = words[i].split(":");
            if (pairs[0].equals("symbol")) {
                this.symbol = pairs[1];
            }
            if (pairs[0].equals("height")) {
                this.height = Integer.parseInt(pairs[1]);
            }
            if (pairs[0].equals("width")) {
                this.width = Integer.parseInt(pairs[1]);
            }
            if (pairs[0].equals("hit_points")) {
                this.hitPoints = Integer.parseInt(pairs[1]);
            }
            if (pairs[0].startsWith("fill-")) {
                int k = Integer.parseInt(pairs[0].substring("fill-".length()));
                if (pairs[1].startsWith("color(") && pairs[1].endsWith(")")) {
                    Color color = new ColorsParser().getColor(pairs[1].substring("color(".length(),
                            pairs[1].length() - 1));
                    this.colorFillK.put(k, color);
                }
                if (pairs[1].startsWith("image(") && pairs[1].endsWith(")")) {
                    String imagePath = pairs[1].substring("image(".length(), pairs[1].length() - 1);
                    BufferedImage buffer = null;
                    try {
                        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(imagePath);
                        if (is != null) {
                            buffer = ImageIO.read(is);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed loading image");
                    }
                    this.imageFillK.put(k, buffer);
                }
            }
            if (pairs[0].equals("stroke")) {
                if (pairs[1].startsWith("color(") && pairs[1].endsWith(")")) {
                    this.stroke = new ColorsParser().getColor(pairs[1].substring("color(".length(),
                            pairs[1].length() - 1));
                }
            }
            if (pairs[0].equals("fill")) {
                if (pairs[1].startsWith("color(") && pairs[1].endsWith(")")) {
                    this.fill = new ColorsParser().getColor(pairs[1].substring("color(".length(),
                            pairs[1].length() - 1));
                } else {
                    if (pairs[1].startsWith("image(") && pairs[1].endsWith(")")) {
                        String path = pairs[1].substring("image(".length(), pairs[1].length() - 1);
                        try {
                            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
                            if (is != null) {
                                this.image = ImageIO.read(is);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Failed loading image");
                        }
                    }
                }
            }
        }
    }
}
