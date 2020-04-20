package general;

import animations.AnimationRunner;
import animations.GameLevel;
import animations.HighScoresAnimation;
import animations.KeyPressStoppableAnimation;
import animations.screens.EndScreen;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import interfaces.LevelInformation;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The Game flow class.
 */
public class GameFlow {
    private AnimationRunner runner;
    private KeyboardSensor keyboard;
    private int width;
    private int height;
    private int rectShort;
    private Counter score;
    private Counter lives;
    private Counter blocks;
    private HighScoresTable highScores;
    private DialogManager dialog;
    private File root;
    private String win;
    private String stop;
    private String lose;
    private String winners;

    /**
     * The constructor of a new Game flow, according to several given parameters.
     *
     * @param ar         the animation's runner.
     * @param ks         the animation's keyboard.
     * @param highScores the high scores
     * @param dialog     the gui's dialog manager.
     * @param root       the root for saving the high score table.
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks,
                    HighScoresTable highScores, DialogManager dialog, File root) {
        this.runner = ar;
        this.keyboard = ks;
        this.highScores = highScores;
        this.root = root;
        this.dialog = dialog;
        this.score = new Counter(0);
        this.lives = new Counter(7);
        this.lose = null;
        this.win = null;
        this.stop = null;
        this.winners = null;
    }

    /**
     * Set the sizes, according several given parameters.
     *
     * @param w    the screen's width.
     * @param h    the screen's height.
     * @param rect the screen's length of the narrow side of the screen's borders.
     */
    public void setSize(int w, int h, int rect) {
        this.width = w;
        this.height = h;
        this.rectShort = rect;
    }

    /**
     * Set the images paths.
     *
     * @param winPath    the win screen image path.
     * @param losePath   the lose screen image path.
     * @param stopPath   the stop screen image path.
     * @param winnerPath the win high scores table image path.
     */
    public void setPath(String winPath, String losePath, String stopPath, String winnerPath) {
        this.win = winPath;
        this.stop = stopPath;
        this.lose = losePath;
        this.winners = winnerPath;
    }

    /**
     * This method runs levels, according the order, which sets by the user.
     *
     * @param levels the list of the levels.
     */
    public void runLevels(List<LevelInformation> levels) {
        // runs the given levels until no more lives left.
        for (LevelInformation levelInfo : levels) {
            this.blocks = new Counter(levelInfo.numberOfBlocksToRemove());
            GameLevel level = new GameLevel(levelInfo, this.runner, this.keyboard, this.lives, this.score, this.blocks);
            level.setSizes(this.width, this.height, this.rectShort);
            level.setStopPath(this.stop);
            level.initialize();
            // run the current level as far as no lives or blocks left.
            while (this.lives.getValue() > 0 && this.blocks.getValue() > 0) {
                level.playOneTurn();
                // exit the play method after killing all the blocks.
                if (this.blocks.getValue() == 0) {
                    this.score.increase(100);
                    // exit the play method after all the balls were dead.
                } else {
                    this.lives.decrease(1);
                }
            }
            // stop run the level if no more lives left.
            if (this.lives.getValue() == 0) {
                break;
            }
        }
        EndScreen end = new EndScreen(this.lives, this.score);
        end.setBackground(this.win, this.lose);
        this.runner.run(new KeyPressStoppableAnimation(this.keyboard, KeyboardSensor.SPACE_KEY, end));
        // check if the user set a new high-score.
        if (this.highScores.getHighScores().size() < this.highScores.size()
                || this.highScores.getRank(this.score.getValue()) <= this.highScores.size()) {
            this.highScores.add(new ScoreInfo(this.dialog.showQuestionDialog("Name", "What is your name?", ""),
                    this.score.getValue()));
            try {
                this.highScores.save(this.root);
            } catch (IOException exp) {
                System.err.println("Failed saving the high scores table");
                exp.printStackTrace(System.err);
            }
        }
        // display the end screen.
        HighScoresAnimation tableAnimation = new HighScoresAnimation(this.highScores);
        if (this.winners != null) {
            tableAnimation.setBackground(this.winners);
        }
        this.runner.run(new KeyPressStoppableAnimation(this.keyboard, KeyboardSensor.SPACE_KEY, tableAnimation));
    }
}
