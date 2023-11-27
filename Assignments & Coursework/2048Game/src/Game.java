import graphics.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game extends JPanel implements KeyListener {
    private State state;
    private String saveFileName = "save-2048.txt";
    private int highestScore;
    static JFrame frame = new JFrame("2048");

    private static final int WINDOW_WIDTH = 435;
    private static final int WINDOW_HEIGHT = 600;
    private static final int GRID_ROWS = 4;
    private static final int GRID_COLS = 4;
    private static final int GRID_CELL_SIZE = 100;
    private static final int GRID_MARGIN = 10;
    private static final int SCORE_MARGIN = 30;
    private boolean aiMode = false;

    private final Timer animationTimer;

    private static final int ANIMATION_STEPS = 10;
    private int animationStepsCompleted;
    private final Map<Integer, Point> tilePositions;

    private final Timer fadeInTimer = new Timer(50, new ActionListener() {
        int fadeInStepsCompleted = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            // Increment the number of fade-in steps completed
            fadeInStepsCompleted++;

            // Repaint the screen
            frame.repaint();

            // If the animation is complete, stop the timer
            if (fadeInStepsCompleted == ANIMATION_STEPS) {
                fadeInTimer.stop();
            }
        }
    });

    Timer aiTimer = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // If the AI mode is active and the game is not over, execute the next move
            if (aiMode && !state.isGameOver()) {
                makeAIMove();
                updateUI();
            } else {
                // Stop the Timer if the AI mode is deactivated or the game is over
                aiTimer.stop();
            }
        }
    });
    private int fadeInStepsCompleted;
    private int TILE_PADDING = 10;


    /**
     * The constructor initializes instance variables and sets up the GUI for the game.
     * It also creates an ActionListener for the animation timer,
     * which is responsible for updating the positions of moving tiles and repainting the screen.
     * When the animation is complete, the ActionListener checks if the game is over, places a new tile on the board,
     * and checks if the player has won the game.
     */
    public Game() {
        // Initialize instance variables
        setUpGUI();


        animationStepsCompleted = 0;
        tilePositions = new HashMap<>();
        animationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Increment the number of animation steps completed
                animationStepsCompleted++;

                // Calculate the current positions of each moving tile
                for (Map.Entry<Integer, Point> entry : tilePositions.entrySet()) {
                    int tileValue = entry.getKey();
                    Point startPos = entry.getValue();
                    Point endPos = getTileScreenPosition(tileValue);
                    int currentX = startPos.x + (endPos.x - startPos.x) * (animationStepsCompleted / ANIMATION_STEPS);
                    int currentY = startPos.y + (endPos.y - startPos.y) * (animationStepsCompleted / ANIMATION_STEPS);
                    tilePositions.put(tileValue, new Point(currentX, currentY));
                }

                // Repaint the screen
                frame.repaint();

                // If the animation is complete, stop the timer and check whether the game is over
                if (animationStepsCompleted == ANIMATION_STEPS) {
                    // Stop the timer and reset the animation steps completed
                    animationTimer.stop();
                    animationStepsCompleted = 0;

                    // Clear the tile positions
                    tilePositions.clear();

                    if (state.isGameOver()) {
                        JOptionPane.showMessageDialog(null, "Game Over!");
                    } else {

                        // Place a new tile on the board
                        state.placeNewTile();
                        fadeInTimer.start();

                        // Check if the player has won the game
                        int highestTile = getHighestTile();
                        if (highestTile == 2048) {
                            JOptionPane.showMessageDialog(null, "You win!");
                        }
                    }

                    // Repaint the screen to show the updated state of the game
                    frame.repaint();
                }
            }
        });
    }


    public int getHighestTile() {
        int[][] grid = state.getGrid();
        int highestTile = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int tileValue = grid[i][j];
                if (tileValue > highestTile) {
                    highestTile = tileValue;
                }
            }
        }
        return highestTile;
    }


    /**
     * sets up the GUI with appropriate sizes and adds a Key Listener
     */
    public void setUpGUI() {
        frame.setTitle("2048");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.getContentPane().add(this);
        // register key listener for window
        frame.addKeyListener(this);
    }


    @Override
    public void keyPressed(KeyEvent e) {
            // If the aiMode flag is false, handle player input as before
            switch (e.getKeyCode()) {

                case KeyEvent.VK_Q: // quit the app
                    System.exit(0);

                    break;
                case KeyEvent.VK_N: // start a new game
                    aiMode = false;
                    aiTimer.stop();
                    restart();
                    frame.repaint();
                    break;
                case KeyEvent.VK_M: // make a random move
                    List<Action> actions = getState().actions();
                    if (!actions.isEmpty()) {
                        doMove(actions.get(0));
                    }
                    frame.repaint();
                    break;
                case KeyEvent.VK_A:
                    // Make an AI move if the 'a' key is pressed
                    makeAIMove();
                    frame.repaint();

                case KeyEvent.VK_SPACE:
                    // Toggle AI mode on or off if the space bar is pressed
                    toggleAIMode();
                    if (aiMode) {
                        System.out.println("AI Mode ON");
                    } else {
                        System.out.println("AI Mode OFF");
                    }
                    frame.repaint();

                case KeyEvent.VK_UP: // move up
                    if (isLegalMove(Action.Up)) {
                        doMove(Action.Up);
                        frame.repaint();
                    } else {
                        System.out.println("Illegal move!");
                    }
                    break;

                case KeyEvent.VK_RIGHT: // move right
                    if (isLegalMove(Action.Right)) {
                        doMove(Action.Right);
                        frame.repaint();
                    } else {
                        System.out.println("Illegal move!");
                    }

                    break;
                case KeyEvent.VK_DOWN: // move down
                    if (isLegalMove(Action.Down)) {
                        doMove(Action.Down);
                        frame.repaint();
                    } else {
                        System.out.println("Illegal move!");
                    }
                    break;
                case KeyEvent.VK_LEFT: // move left
                    if (isLegalMove(Action.Left)) {
                        doMove(Action.Left);
                        frame.repaint();
                    } else {
                        System.out.println("Illegal move!");
                    }
                    break;
                default:
                    break;
            }
            frame.repaint(); // redraw window with updated game state
        }

    /**
     * This code reduces the number of simulations that are run from 1,000 to 100
     * it also limits the number of steps that are taken in each simulation to 10.
     * This should reduce the time and processing power required to determine the best move
     * while still providing a good approximation of the best move.
     */
    private void makeAIMove() {
        // Determine the legal actions for the current state
        List<Action> legalActions = state.getLegalActions();

        // Create a map to store the total scores for each action
        Map<Action, Integer> actionScores = new HashMap<>();

        // Loop through each legal action
        for (Action action : legalActions) {
            // Create a copy of the current state
            State stateCopy = state.copy();

            // Apply the action to the copy of the state
            stateCopy.apply(action);

            // Initialize the total score for this action to 0
            int totalScore = 0;

            // Run 100 simulations by doing random playouts for a certain number of steps
            for (int i = 0; i < 100; i++) {
                // Create a copy of the state after applying the action
                State simulationState = stateCopy.copy();

                // Do a random playout from the current state for 10 steps
                totalScore += doRandomPlayout(simulationState, 10);
            }

            // Add the total score for this action to the map
            actionScores.put(action, totalScore);
        }

        // Choose the move with the highest total score
        Action bestAction = null;
        int bestScore = Integer.MIN_VALUE;
        for (Map.Entry<Action, Integer> entry : actionScores.entrySet()) {
            Action action = entry.getKey();
            int score = entry.getValue();
            if (score > bestScore) {
                bestAction = action;
                bestScore = score;
            }
        }

        // Apply the best action to the current state
        state.apply(bestAction);

        // Update the UI to reflect the new state of the game
        updateUI();
    }

    /**
     * This method takes a State object and a number of steps as parameters
     * Does a random playout for the specified number of steps.
     * It returns the total score for the playout.
     * Used in the makeAIMove method to do a limited number of random playouts to approximate the best move.
     *
     * @param simulationState
     * @param numSteps
     * @return totalScore
     */
    private int doRandomPlayout(State simulationState, int numSteps) {
        // Initialize the total score to 0
        int totalScore = 0;

        // Do a random playout for the specified number of steps
        for (int i = 0; i < numSteps; i++) {
            // If the game is over, return the total score
            if (simulationState.isGameOver()) {
                return totalScore;
            }

            // Determine the legal actions for the current state
            List<Action> legalActions = simulationState.getLegalActions();

            // Choose a random action from the legal actions
            Random random = new Random();
            Action action = legalActions.get(random.nextInt(legalActions.size()));

            // Apply the action to the current state
            simulationState.apply(action);

            // Update the total score
            totalScore += simulationState.getScore();
        }

        // Return the total score
        return totalScore;
    }


    private void toggleAIMode() {
        this.aiMode = !this.aiMode;
        if (aiMode) {aiTimer.start();}
        else {aiTimer.stop();}
    }


    private boolean isLegalMove(Action action) {
        List<Action> actions = getState().actions();
        return actions.contains(action);
    }

    // required by KeyListener interface, but not used in this example
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Paints the GUI with a series of strings, the board, the tiles and ensures
     * they are repainted when the game is over
     *
     * @param g Graphics parameter
     */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

// Cast the Graphics object to a Graphics2D object
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

// draw the background
        g2d.setColor(Palette.BACKGROUND.colour());
        g2d.fillRect(0, 0, getWidth(), getHeight());

// draw the frame
        g2d.setColor(Palette.FRAME.colour());
        g2d.fillRoundRect(GRID_MARGIN, GRID_MARGIN, GRID_CELL_SIZE * GRID_COLS, GRID_CELL_SIZE * GRID_ROWS, 30, 30);

// draw the tiles
        for (int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLS; c++) {
                int value = getState().getGrid()[r][c];
                if (value != 0) {
                    // draw a tile with a number on it
                    drawTile(g2d, value, GRID_MARGIN + c * GRID_CELL_SIZE, GRID_MARGIN + r * GRID_CELL_SIZE);
                } else if (value == 0) {
                    g2d.setColor(Palette.EMPTY.colour());
                    g2d.fillRoundRect(
                            GRID_MARGIN + c * GRID_CELL_SIZE + TILE_PADDING,
                            GRID_MARGIN + r * GRID_CELL_SIZE + TILE_PADDING,
                            GRID_CELL_SIZE - 2 * TILE_PADDING,
                            GRID_CELL_SIZE - 2 * TILE_PADDING,
                            30, 30);

                }
            }
        }

// Draw the game score
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g2d.setColor(Palette.TEXT.colour());
        g2d.drawString("Score ",
                80 + GRID_MARGIN,
                70 + GRID_CELL_SIZE * GRID_ROWS + SCORE_MARGIN);
        g2d.drawString("Best ",
                80 + 2 * GRID_CELL_SIZE,
                70 + GRID_CELL_SIZE * GRID_ROWS + SCORE_MARGIN);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("" + state.getScore(),
                80+GRID_MARGIN,
                100 + GRID_CELL_SIZE * GRID_ROWS + SCORE_MARGIN);

        g2d.drawString("" + getHighestScore(),
                80+2 * GRID_CELL_SIZE,
                100 + GRID_CELL_SIZE * GRID_ROWS + SCORE_MARGIN);


// Check if the game is over
        if (getState().isGameOver()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            g2d.setColor(Palette.TEXT.colour());
            g2d.drawString("Game Over!", GRID_MARGIN, 100 - 50);

        }
// If there are any moving tiles, draw them at heir current positions
        if (!tilePositions.isEmpty()) {
            for (Map.Entry<Integer, Point> entry : tilePositions.entrySet()) {
                int tileValue = entry.getKey();
                Point pos = entry.getValue();
                drawTile(g2d, tileValue, pos.x, pos.y);
            }
        }

            // If the fade-in animation is in progress, draw the new tile with the current alpha value
        if (fadeInTimer.isRunning()) {
        // Calculate the current alpha value for the new tile

            if (fadeInStepsCompleted != 0) {
                float alpha = (float) fadeInStepsCompleted / ANIMATION_STEPS;

                // Set the alpha value for the Graphics2D object
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

                // Draw the new tile
                int newTileValue = state.placeNewTile();
                Point newTilePos = getTileScreenPosition(newTileValue);
                drawTile(g2d, newTileValue, state.newTileRow,state.newTileCol);


                // Reset the alpha value for the Graphics2D object
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        }
    }

    private void drawTile(Graphics2D g2d, int tileValue, int x, int y) {
        // Set the font and color of the Graphics2D object based on the tile value
        g2d.setFont(new Font("SansSerif", Font.BOLD, 35));

                  // For all other tiles, use the color provided by the Palette class
            g2d.setColor(Palette.tileColour(tileValue));
        g2d.fillRoundRect(
                x + TILE_PADDING,
                y + TILE_PADDING,
                GRID_CELL_SIZE - 2 * TILE_PADDING,
                GRID_CELL_SIZE - 2 * TILE_PADDING,
                30, 30);




        if (tileValue == 2 || tileValue == 4) {
            // For the '2' and '4' tiles, use the dark text color
            g2d.setColor(Palette.TEXT.colour());
        } else {
            // For all other tiles, use white
            g2d.setColor(Color.white);
        }
// First, get the font metrics for the current font
        FontMetrics fontMetrics = g2d.getFontMetrics();

// Then, get the width of the tile value as a string
        int textWidth = fontMetrics.stringWidth(String.valueOf(tileValue));

// Calculate the x position of the text by subtracting half the width of the text from the center x position of the tile
        int textX = x + (GRID_CELL_SIZE - textWidth) / 2;

// Calculate the y position of the text by adding the ascent of the font to the center y position of the tile
// (the ascent is the distance from the baseline of the font to the top of the font)
        int textY = y + (GRID_CELL_SIZE + fontMetrics.getAscent()) / 2;

// Draw the tile value as a string at the calculated position
        g2d.drawString(String.valueOf(tileValue), textX, textY);

    }


    public String getSaveFileName() {
        return saveFileName;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void restart() {
        state.start();
        if (state.getScore() > highestScore) {
            highestScore = state.getScore();
        }
    }

    @Override
    public String toString() {
        return state.toString();
    }

    public void doMove(final Action action) {
        if (state.actions().contains(action)) {
            state.apply(action);
            // Update the highest score if necessary
            if (state.getScore() > highestScore) {
                highestScore = state.getScore();
            }

            // Record the positions of each moving tile before the move
            int[][] grid = state.getGrid();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    int tileValue = grid[i][j];
                    if (tileValue != 0) {
                        Point startPos = getTileScreenPosition(tileValue);
                        tilePositions.put(tileValue, startPos);
                    }
                }
            }

            // Start the animation timer
            animationTimer.start();


        } else {
            System.out.println("Invalid move");
        }
    }

    public Point getTileScreenPosition(int tileValue) {
        int[] gridPosition = getTileGridPosition(tileValue);
        int gridRow = gridPosition[0];
        int gridCol = gridPosition[1];
        int x = GRID_MARGIN + gridCol * GRID_CELL_SIZE;
        int y = GRID_MARGIN + gridRow * GRID_CELL_SIZE;
        return new Point(x, y);
    }

    public int[] getTileGridPosition(int tileValue) {
        int[][] grid = state.getGrid();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] == tileValue) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFileName))) {
            if (state.getScore() > highestScore) {
                highestScore = state.getScore();
            }
            writer.write(String.valueOf(highestScore));
            writer.newLine();
            writer.write(state.toString());
        } catch (IOException e) {
            System.out.println("Error saving game state");
        }
    }

    public void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFileName))) {
            highestScore = Integer.parseInt(reader.readLine());
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            state = new State(sb.toString());
            // Read the highest score from the file
        } catch (IOException e) {
            System.out.println("Error loading game state");
        }

    }


    public State getState() {
        return state;
    }
}

