import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main2048 {
    private static final String SAVE_FILE_NAME = "save-2048.txt";

    private static Game game;

    public Main2048() throws IOException {
        File saveFile = new File(SAVE_FILE_NAME);
        if (saveFile.exists()) {
            game = new Game();
            game.load();
        } else {
            game = new Game();
            game.restart();
        }
    }

    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    public void run() {
                        System.out.println("Saving game state...");
                        game.save();
                        System.out.println("Game state saved");
                    }
                }
        );
        Main2048 app;
        app = new Main2048();
        app.test();

    }

    public void test() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(game);
        while (true) {
            System.out.println("Enter move (u/r/d/l/q): ");
            String input = scanner.nextLine();
            if (input.equals("q")) {
                break;
            }
            if (input.equals("n")) {
                game.restart();
                System.out.println(game);
                continue;
            }
            Action action = null;
            switch (input) {
                case "u":
                    action = Action.Up;
                    break;
                case "r":
                    action = Action.Right;
                    break;
                case "d":
                    action = Action.Down;
                    break;
                case "l":
                    action = Action.Left;
                    break;
                default:
                    System.out.println("Invalid input");
                    continue;
            }
            List<Action> actions = game.getState().actions();
            if (actions.contains(action)) {
                game.doMove(action);
                System.out.println(game);
            } else {
                System.out.println("Invalid move");
            }
        }
        scanner.close();
    }
}
