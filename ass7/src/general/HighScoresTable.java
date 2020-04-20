package general;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * The High scores table class.
 */
public class HighScoresTable implements Serializable {
    private int size;
    private List<ScoreInfo> scores;

    /**
     * The constructor for a new High scores table.
     *
     * @param size the maximum of players the high scores table contains.
     */
    public HighScoresTable(int size) {
        this.size = size;
        this.scores = new ArrayList<>();
    }

    /**
     * Add a new player to the table to his accurate position by the 'get rank' position, according the given score.
     * If the table contains more than 'size' players, the player with the lowest score is removed from the table.
     *
     * @param score the given score.
     */
    public void add(ScoreInfo score) {
        this.scores.add(this.getRank(score.getScore()) - 1, score);
        // in case that the high-score list is too big, remove the worst score.
        if (this.scores.size() > this.size) {
            this.scores.remove(this.size);
        }
    }

    /**
     * @return the maximum amount of players to be in the table.
     */
    public int size() {
        return this.size;
    }

    /**
     * @return the high scores table, which is sorted according the score of the players.
     */
    public List<ScoreInfo> getHighScores() {
        return this.scores;
    }

    /**
     * Return the rank of the given score, where will it be on the list if added?
     *
     * @param score the given score.
     * @return the rank.
     */
    public int getRank(int score) {
        int rank = 1;
        for (ScoreInfo info : this.scores) {
            if (info.getScore() < score) {
                return rank;
            }
            rank++;
        }
        return rank;
    }

    /**
     * Clears the table.
     */
    public void clear() {
        this.scores.clear();
    }

    /**
     * Load the table from given file.
     *
     * @param filename the given file's name.
     * @throws IOException in case that the file loading is failed.
     */
    public void load(File filename) throws IOException {
        this.clear();
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream(filename.getName()));
            HighScoresTable table = (HighScoresTable) is.readObject();
            this.size = table.size();
            this.scores = table.getHighScores();
        } catch (ClassNotFoundException var) {
            throw new RuntimeException("Unable to find class for object in file: " + filename.getName());
            // don't do anything in case of not finding the file,
            // so that in the first time when the file doesn't exist yet no exception will be thrown.
        } catch (FileNotFoundException e) {
            return;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Save the current table to the given file.
     *
     * @param filename the name of the given file.
     * @throws IOException in case that the file's saving is failed.
     */
    public void save(File filename) throws IOException {
        ObjectOutputStream os;
        os = new ObjectOutputStream(new FileOutputStream(filename.getName()));
        os.writeObject(this);
        os.close();
    }

    /**
     * Load from file high scores table, which located in the given file.
     * This methods uses the 'load' method.
     * If the file does not exist, or there is a problem with reading it,
     * an new table with capacity of 5 players is created.
     *
     * @param filename the name of the given file.
     * @return the loaded table, or the new one.
     */
    public static HighScoresTable loadFromFile(File filename) {
        HighScoresTable table = new HighScoresTable(5);
        try {
            table.load(filename);
        } catch (IOException e) {
            System.err.println("Failed reading file: " + filename.getName() + ", message:" + e.getMessage());
            e.printStackTrace(System.err);
        }
        return table;
    }
}
