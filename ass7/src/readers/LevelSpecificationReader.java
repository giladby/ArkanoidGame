package readers;

import builders.BackgroundFromColor;
import builders.BackgroundFromImage;
import builders.BlocksFromSymbolsFactory;
import builders.ColorsParser;
import interfaces.LevelInformation;
import interfaces.Sprite;
import general.Velocity;
import sprites.colliadables.Block;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The Level specification reader class.
 */
public class LevelSpecificationReader {
    private List<LevelInformation> levels;
    private int paddleSpeed;
    private int paddleWidth;
    private String levelName;
    private Sprite backgroundSprite;
    private List<Velocity> velocities;
    private int startX;
    private int startY;
    private int toDestroyed;
    private int rowHeight;
    private String blockLines;

    /**
     * The constructor of a new Level specification reader.
     */
    public LevelSpecificationReader() {
        this.levels = new ArrayList<>();
    }

    /**
     * Reset the current level parameters.
     */
    private void reset() {
        this.paddleSpeed = -1;
        this.paddleWidth = -1;
        this.levelName = null;
        this.backgroundSprite = null;
        this.velocities = new ArrayList<>();
        this.startY = -1;
        this.startX = -1;
        this.toDestroyed = -1;
        this.rowHeight = -1;
        this.blockLines = null;
    }

    /**
     * Get the list contains the information of the levels according the given file's reader.
     *
     * @param reader the given reader.
     * @return the list.
     */
    public List<LevelInformation> fromReader(Reader reader) {
        List<String> levelInfo = this.getStringLevels(reader);
        for (String s : levelInfo) {
            this.reset();
            LevelInformation level = this.levelFromString(s);
            if (level != null) {
                this.levels.add(level);
            } else {
                throw new RuntimeException("Failed loading levels");
            }
        }
        return this.levels;
    }

    /**
     * The inner Level builder class.
     */
    public class LevelBuilder implements LevelInformation {
        private List<Velocity> velocities;
        private int paddleSpeed;
        private int paddleWidth;
        private String levelName;
        private Sprite backgroundSprite;
        private int toDestroyed;
        private String blockSt;
        private BlocksFromSymbolsFactory factoryBlock;
        private int startX;
        private int startY;
        private int rowHeight;

        /**
         * The constructor of a new Level builder.
         *
         * @param velocities       the balls velocities list.
         * @param paddleSpeed      the paddle speed.
         * @param paddleWidth      the paddle width.
         * @param levelName        the level name.
         * @param backgroundSprite the background.
         * @param blockSt          the string of blocks definitions.
         * @param factory          the factory of creating blocks.
         */
        public LevelBuilder(List<Velocity> velocities, int paddleSpeed, int paddleWidth,
                            String levelName, Sprite backgroundSprite,
                            String blockSt, BlocksFromSymbolsFactory factory) {
            this.paddleSpeed = paddleSpeed;
            this.paddleWidth = paddleWidth;
            this.levelName = levelName;
            this.backgroundSprite = backgroundSprite;
            this.velocities = velocities;
            this.blockSt = blockSt;
            this.factoryBlock = factory;
        }

        /**
         * Set the block sizes.
         *
         * @param x       the starting x coordinate.
         * @param y       the starting y coordinate.
         * @param height  the block's height gap.
         * @param toClear the numbers of block to clear in the level.
         */
        public void setBlockSizes(int x, int y, int height, int toClear) {
            this.startX = x;
            this.startY = y;
            this.rowHeight = height;
            this.toDestroyed = toClear;
        }

        /**
         * Add the level's block list the desired blocks,
         * by parsing the blocks definition in the file by the blocks creator factory.
         *
         * @param block   the level's blocks list.
         * @param lines   the string of blocks definitions.
         * @param factory the factory of creating blocks.
         * @param x       the starting x coordinate.
         * @param y       the starting y coordinate.
         * @param height  the block's height gap.
         */
        private void getBlocks(List<Block> block, String lines, BlocksFromSymbolsFactory factory, int y, int x,
                               int height) {
            int j;
            int curX;
            int curY;
            String[] split = lines.split("\n");
            for (j = 0; j < split.length; j++) {
                curX = x;
                for (int k = 0; k < split[j].length(); k++) {
                    String character = String.valueOf(split[j].charAt(k));
                    if (factory.isSpaceSymbol(character)) {
                        curX += factory.getSpaceWidth(character);
                    } else {
                        if (factory.isBlockSymbol(character)) {
                            curY = y + (j * height);
                            Block b = factory.getBlock(character, curX, curY);
                            block.add(b);
                            curX += b.getCollisionRectangle().getWidth();
                        } else {
                            throw new RuntimeException("Invalid character");
                        }
                    }
                }
            }
        }

        @Override
        public int numberOfBalls() {
            return this.velocities.size();
        }

        @Override
        public List<Velocity> initialBallVelocities() {
            return this.velocities;
        }

        @Override
        public int paddleSpeed() {
            return this.paddleSpeed;
        }

        @Override
        public int paddleWidth() {
            return this.paddleWidth;
        }

        @Override
        public String levelName() {
            return this.levelName;
        }

        @Override
        public Sprite getBackground() {
            return this.backgroundSprite;
        }

        @Override
        public List<Block> blocks() {
            List<Block> b = new ArrayList<>();
            this.getBlocks(b, this.blockSt, this.factoryBlock, this.startY, this.startX, this.rowHeight);
            return b;
        }

        @Override
        public int numberOfBlocksToRemove() {
            return this.toDestroyed;
        }
    }

    /**
     * Parsing the given string and creates the desire level.
     * In case that one of the parameters is missing, return null.
     *
     * @param st the given string
     * @return the desire level.
     */
    private LevelInformation levelFromString(String st) {
        String[] lines = st.split("\\n");
        String value;
        BlocksFromSymbolsFactory factory = null;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith("level_name:")) {
                this.levelName = lines[i].substring(lines[i].indexOf(":") + 1);
            }
            if (lines[i].startsWith("ball_velocities:")) {
                value = lines[i].substring(lines[i].indexOf(":") + 1);
                String[] couples = value.split(" ");
                for (String couple : couples) {
                    String[] velocityParam = couple.split(",");
                    double angle = Double.parseDouble(velocityParam[0]);
                    double speed = Double.parseDouble(velocityParam[1]);
                    Velocity vel = Velocity.fromAngleAndSpeed(angle, speed);
                    this.velocities.add(vel);
                }
                continue;
            }
            if (lines[i].startsWith("background:")) {
                value = lines[i].substring(lines[i].indexOf(":") + 1);
                if (value.startsWith("color(") && value.endsWith(")")) {
                    this.backgroundSprite = new BackgroundFromColor(new ColorsParser().getColor(
                            value.substring("color(".length(), value.length() - 1)));
                } else {
                    if (value.startsWith("image(") && value.endsWith(")")) {
                        BufferedImage imageBuffer = null;
                        try {
                            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(
                                    value.substring("image(".length(), value.length() - 1));
                            if (is != null) {
                                imageBuffer = ImageIO.read(is);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Failed loading image");
                        }
                        this.backgroundSprite = new BackgroundFromImage(imageBuffer);
                    }
                }
                continue;
            }
            if (lines[i].startsWith("paddle_speed:")) {
                this.paddleSpeed = Integer.parseInt(lines[i].substring(lines[i].indexOf(":") + 1));
                continue;
            }
            if (lines[i].startsWith("paddle_width:")) {
                this.paddleWidth = Integer.parseInt(lines[i].substring(lines[i].indexOf(":") + 1));
                continue;
            }
            if (lines[i].startsWith("block_definitions:")) {
                value = lines[i].substring(lines[i].indexOf(":") + 1);
                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(value);
                Reader reader = null;
                if (is != null) {
                    reader = new InputStreamReader(is);
                }
                if (reader == null) {
                    throw new RuntimeException("Failed reading blocks file");
                }
                factory = BlocksDefinitionReader.fromReader(new BufferedReader(reader));
                continue;
            }
            if (lines[i].startsWith("blocks_start_x:")) {
                this.startX = Integer.parseInt(lines[i].substring(lines[i].indexOf(":") + 1));
                continue;
            }
            if (lines[i].startsWith("blocks_start_y:")) {
                this.startY = Integer.parseInt(lines[i].substring(lines[i].indexOf(":") + 1));
                continue;
            }
            if (lines[i].startsWith("row_height:")) {
                this.rowHeight = Integer.parseInt(lines[i].substring(lines[i].indexOf(":") + 1));
                continue;
            }
            if (lines[i].startsWith("num_blocks:")) {
                this.toDestroyed = Integer.parseInt(lines[i].substring(lines[i].indexOf(":") + 1));
                continue;
            }
            if (lines[i].equals("START_BLOCKS")) {
                if (lines[lines.length - 1].equals("END_BLOCKS")) {
                    this.blockLines = "";
                    for (int j = i + 1; j < lines.length - 1; j++) {
                        this.blockLines = this.blockLines.concat(lines[j]);
                        this.blockLines = this.blockLines.concat("\n");
                    }
                }
                LevelBuilder level = new LevelBuilder(this.velocities, this.paddleSpeed, this.paddleWidth,
                        this.levelName, this.backgroundSprite, this.blockLines, factory);
                level.setBlockSizes(this.startX, this.startY, this.rowHeight, this.toDestroyed);
                return level;
            }
        }
        return null;
    }

    /**
     * Split the file according the file's reader to several string, which contains the information about the levels.
     *
     * @param reader the given reader.
     * @return the strings list.
     */
    private List<String> getStringLevels(Reader reader) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> levelString = new ArrayList<>();
        String newLevel = "";
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("") || line.startsWith("#")) {
                    continue;
                }
                if (line.equals("START_LEVEL")) {
                    newLevel = "";
                    continue;
                }
                if (line.equals("END_LEVEL")) {
                    levelString.add(newLevel);
                    continue;
                }
                newLevel = newLevel.concat(line);
                newLevel = newLevel.concat("\n");
            }
        } catch (IOException e) {
            System.err.println("Failed reading levels file");
            e.printStackTrace(System.err);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Failed closing levels file");
            }
        }
        return levelString;
    }
}
