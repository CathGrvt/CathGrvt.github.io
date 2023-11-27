import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the 2048 board and provides methods for moving up, down, left and
 * right. Also provides information about the game including score, the highest tile
 * and whether the game is over or not
 */
public class State {
    private int[][] grid;
    private int score;
    private boolean gameOver;
    int newTileRow;
    int newTileCol;

    public State() {
        this.grid = new int[4][4];

        start();
    }
    /**
     Constructs a new State object with the specified state represented as a string.
     @param stateString a string representing the state of a game of 2048, with the score on the first line,
     Copy code
     a boolean indicating whether the game is over on the second line, and the grid on the
     Copy code
     following four lines, with each line containing four space-separated integers.
     */
    public State(String stateString) {
        String[] lines = stateString.split("\n");
        score = Integer.parseInt(lines[0]);
        gameOver = Boolean.parseBoolean(lines[1]);
        grid = new int[4][4];
        for (int i = 0; i < 4; i++) {
            String[] numbers = lines[i + 2].split(" ");
            for (int j = 0; j < 4; j++) {
                grid[i][j] = Integer.parseInt(numbers[j]);
            }
        }
    }







    public void start() {
        clearGrid();
        score = 0;
        gameOver = false;
        placeNewTile();
        placeNewTile();
    }

    public void clearGrid() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j] = 0;
            }
        }
    }

    /**
     * Places a new tile with a value of 2 or 4 in a random empty spot on the grid.
     * @return the value of the tile that was placed (either 2 or 4)
     */
    public int placeNewTile() {
        Random random = new Random();
        int value = random.nextInt(10) == 0 ? 4 : 2; // 10% chance of a 4, 90% chance of a 2
        int x, y;
        do {
            x = random.nextInt(4);
            y = random.nextInt(4);
        } while (grid[x][y] != 0);

        grid[x][y] = value;
        newTileRow = x;
        newTileCol = y;

        return value;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(score).append("\n");
        sb.append(gameOver).append("\n");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sb.append(grid[i][j]).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Returns a list of all possible actions that can be taken in the current state of the game.
     * @return a list of Action objects representing the actions that can be taken in the current state.
     */
    public List<Action> actions()
    {
        List<Action> actions = new ArrayList<>();
        for (Action action : Action.values())
        {
            int drow = action.drow;
            int dcol = action.dcol;
            for (int row = 0; row < 4; row++)
            {
                for (int col = 0; col < 4; col++)
                {
                    int newRow = row + drow;
                    int newCol = col + dcol;
                    if (newRow >= 0 && newRow < 4 && newCol >= 0 && newCol < 4)
                    {
                        if (grid[row][col] != 0 && (grid[newRow][newCol] == 0 || grid[newRow][newCol] == grid[row][col]))
                        {
                            actions.add(action);
                            break;
                        }
                    }
                }
            }
        }
        return actions;
    }

    public void apply(final Action action) {
        packTiles(action);
        mergeTiles(action);
        packTiles(action);
        placeNewTile();
        checkGameOver();
    }

    /**
     *Packs the tiles on the grid towards the specified direction by moving non-zero tiles as far as possible
     * in that direction, then merging tiles with the same value that are next to each other in that direction.
     * @param action the direction in which to pack the tiles
     */
    private void packTiles(final Action action) {
        switch (action) {
            case Up:
                for (int j = 0; j < 4; j++) {
                    int nextPos = 0;
                    for (int i = 0; i < 4; i++) {
                        if (grid[i][j] != 0) {
                            grid[nextPos][j] = grid[i][j];
                            if (nextPos != i) {
                                grid[i][j] = 0;
                            }
                            nextPos++;
                        }
                    }
                }
                break;
            case Right:
                for (int i = 0; i < 4; i++) {
                    int nextPos = 3;
                    for (int j = 3; j >= 0; j--) {
                        if (grid[i][j] != 0) {
                            grid[i][nextPos] = grid[i][j];
                            if (nextPos != j) {
                                grid[i][j] = 0;
                            }
                            nextPos--;
                        }
                    }
                }
                break;
            case Down:
                for (int j = 0; j < 4; j++) {
                    int nextPos = 3;
                    for (int i = 3; i >= 0; i--) {
                        if (grid[i][j] != 0) {
                            grid[nextPos][j] = grid[i][j];
                            if (nextPos != i) {
                                grid[i][j] = 0;
                            }
                            nextPos--;
                        }
                    }
                }
                break;
            case Left:
                for (int i = 0; i < 4; i++) {
                    int nextPos = 0;
                    for (int j = 0; j < 4; j++) {
                        if (grid[i][j] != 0) {
                            grid[i][nextPos] = grid[i][j];
                            if (nextPos != j) {
                                grid[i][j] = 0;
                            }
                            nextPos++;
                        }
                    }
                }
                break;
        }
    }

    /**
     *Merges tiles with the same value that are next to each other in the specified direction, adding their values
     * to the score and updating the grid accordingly.
     * @param action the direction in which to merge the tiles
     */
    private void mergeTiles(final Action action) {
        switch (action) {
            case Up:
                for (int j = 0; j < 4; j++) {
                    for (int i = 0; i < 3; i++) {
                        if (grid[i][j] != 0 && grid[i][j] == grid[i + 1][j]) {
                            grid[i][j] *= 2;
                            score += grid[i][j];
                            grid[i + 1][j] = 0;
                        }
                    }
                }
                break;
            case Right:
                for (int i = 0; i < 4; i++) {
                    for (int j = 3; j >= 1; j--) {
                        if (grid[i][j] != 0 && grid[i][j] == grid[i][j - 1]) {
                            grid[i][j] *= 2;
                            score += grid[i][j];
                            grid[i][j - 1] = 0;
                        }
                    }
                }
                break;
            case Down:
                for (int j = 0; j < 4; j++) {
                    for (int i = 3; i >= 1; i--) {
                        if (grid[i][j] != 0 && grid[i][j] == grid[i - 1][j]) {
                            grid[i][j] *= 2;
                            score += grid[i][j];
                            grid[i - 1][j] = 0;
                        }
                    }
                }
                break;
            case Left:
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (grid[i][j] != 0 && grid[i][j] == grid[i][j + 1]) {
                            grid[i][j] *= 2;
                            score += grid[i][j];
                            grid[i][j + 1] = 0;
                        }
                    }
                }
                break;
        }
    }

    private void checkGameOver() {
        List<Action> possibleActions = actions();
        if (possibleActions.size() == 0) {
            gameOver = true;
        }
    }


    public List<Action> getLegalActions() {
        return actions();
    }

    public State copy() {
        State copy = new State();
        copy.setScore(this.getScore());
        copy.setGameOver(this.isGameOver());
        int[][] grid = this.getGrid();
        int[][] gridCopy = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gridCopy[i][j] = grid[i][j];
            }
        }
        copy.setGrid(gridCopy);
        return copy;
    }

    public void setGrid(int[][] gridCopy) {
        this.grid = gridCopy;
    }



}
