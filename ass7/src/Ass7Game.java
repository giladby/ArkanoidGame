import animations.AnimationRunner;
import animations.HighScoresAnimation;
import animations.KeyPressStoppableAnimation;
import animations.MenuAnimation;
import animations.screens.EndScreen;
import animations.screens.PauseScreen;
import biuoop.DialogManager;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import general.Counter;
import general.GameFlow;
import general.HighScoresTable;
import geometry.Point;
import interfaces.Animation;
import interfaces.LevelInformation;
import interfaces.Menu;
import interfaces.Task;
import readers.LevelSetReader;
import readers.LevelSpecificationReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The Ass6Game class.
 * It is implementation of the assignment's classes.
 * This is kind of final product.
 */
public class Ass7Game {
    /**
     * The main method of the class.
     * It initializes and runs the game according the user's choice.
     *
     * @param args the root of level's file the user wants to run.
     */
    public static void main(String[] args) {
        EndScreen e= new EndScreen(new Counter(1), new Counter(2));
        Animation a = (Animation)e;
        EndScreen e2 = (EndScreen) a;
        int width = 800;
        int height = 600;
        int rectShort = 25;
        int framesPerSecond = 60;
        String name = "highscores";
        File root = new File(name);
        HighScoresTable table;
        table = HighScoresTable.loadFromFile(root);
        GUI gui = new GUI("Arkanoid", width, height);
        KeyboardSensor keyboard = gui.getKeyboardSensor();
        DialogManager dialog = gui.getDialogManager();
        AnimationRunner runner = new AnimationRunner(framesPerSecond, gui);
        String levelPath;
        if (args.length > 0) {
            levelPath = args[0];
        } else {
            levelPath = "level_sets.txt";
        }
        Menu<Task<Void>> mainMenu = new MenuAnimation<>("Main Menu", keyboard, runner);
        String menuString = "background_images/menu.jpeg";
        String winnerString = "background_images/winner.jpeg";
        String win = "background_images/win.jpeg";
        String lose = "background_images/lose.jpeg";
        String stop = "background_images/stop.jpeg";
        mainMenu.setBackground(menuString);
        mainMenu.addSelection("h", "High scores", new Task<Void>() {
            public Void run() {
                HighScoresAnimation tableAnimation = new HighScoresAnimation(table);
                tableAnimation.setBackground(winnerString);
                runner.run(new KeyPressStoppableAnimation(keyboard, KeyboardSensor.SPACE_KEY, tableAnimation));
                return null;
            }
        });
        mainMenu.addSelection("q", "Quit game", new Task<Void>() {
            public Void run() {
                gui.close();
                System.exit(0);
                return null;
            }
        });
        Menu<Task<Void>> levelsMenu = new MenuAnimation<>("Levels set", keyboard, runner);
        levelsMenu.setBackground(menuString);
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(levelPath);
        LevelSetReader reader = new LevelSetReader();
        List<LevelSetReader.LevelSet> levels = new ArrayList<>();
        if (is != null) {
            levels = reader.fromReader(new BufferedReader(new InputStreamReader(is)));
        }
        for (LevelSetReader.LevelSet level : levels) {
            levelsMenu.addSelection(level.getKey(), level.getName(), new Task<Void>() {
                @Override
                public Void run() {
                    GameFlow flow = new GameFlow(runner, keyboard, table, dialog, root);
                    flow.setSize(width, height, rectShort);
                    flow.setPath(win, lose, stop, winnerString);
                    InputStream levelStream = ClassLoader.getSystemClassLoader().getResourceAsStream(level.getPath());
                    List<LevelInformation> levelToRun = new ArrayList<>();
                    if (levelStream != null) {
                        levelToRun =  new LevelSpecificationReader().
                                fromReader(new InputStreamReader(levelStream));
                    }
                    flow.runLevels(levelToRun);
                    return null;
                }
            });
        }
        mainMenu.addSubMenu("s", "Start a new game", levelsMenu);
        while (true) {
            runner.run(mainMenu);
            Task<Void> task = mainMenu.getStatus();
            task.run();
            mainMenu.restart();
        }
    }
}

