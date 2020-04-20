package readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * The Level set reader class.
 */
public class LevelSetReader {
    private List<LevelSet> levels;
    private String curName;
    private String curKey;
    private String curPath;

    /**
     * The constructor of a new Level set reader.
     */
    public LevelSetReader() {
        this.levels = new ArrayList<>();
    }

    /**
     * Reset the current level parameters: level's name, key to press, and the it's path.
     */
    private void reset() {
        this.curName = null;
        this.curKey = null;
        this.curPath = null;
    }

    /**
     * Get the list of levels according the given file's reader.
     *
     * @param reader the given reader.
     * @return the list.
     */
    public List<LevelSet> fromReader(Reader reader) {
        BufferedReader bufferedReader = (BufferedReader) reader;
        LineNumberReader lineReader = new LineNumberReader(bufferedReader);
        String line;
        String[] split;
        Boolean isEvenEnd = true;
        try {
            while ((line = lineReader.readLine()) != null) {
                // even line number.
                if (lineReader.getLineNumber() % 2 == 0) {
                    this.curPath = line;
                    this.levels.add(new LevelSet(this.curName, this.curKey, this.curPath));
                    this.reset();
                    isEvenEnd = true;
                    // odd line number.
                } else {
                    split = line.split(":");
                    if (split.length != 2) {
                        throw new RuntimeException("Wrong file pattern");
                    }
                    this.curKey = split[0];
                    this.curName = split[1];
                    isEvenEnd = false;
                }
            }
        } catch (IOException exp) {
            throw new RuntimeException("Failed reading levels");
        }
        if (!isEvenEnd) {
            throw new RuntimeException("Wrong file pattern");
        }
        return this.levels;
    }

    /**
     * The inner Level set class.
     */
    public class LevelSet {
        private String name;
        private String key;
        private String path;

        /**
         * The constructor of a new Level set.
         *
         * @param name the level's name.
         * @param key  the key to press.
         * @param path the level's path.
         */
        public LevelSet(String name, String key, String path) {
            this.key = key;
            this.name = name;
            this.path = path;
        }

        /**
         * @return the level's name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * @return the key to press.
         */
        public String getKey() {
            return this.key;
        }

        /**
         * @return the level's path.
         */
        public String getPath() {
            return this.path;
        }
    }
}
