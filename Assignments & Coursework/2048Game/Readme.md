# 2048 Game Readme

## Introduction

Welcome to the 2048 Game project! This Java program implements the classic 2048 game where the player combines tiles with the same number to reach the elusive 2048 tile. The project includes functionality for saving and loading game states.

## Project Overview

The project consists of a Java class named `Main2048`, which serves as the entry point for the game. The main features of the program include:

1. **Game Initialization:**
   - Checks for the existence of a save file (`save-2048.txt`).
   - If the save file exists, loads the game state; otherwise, starts a new game.

2. **Runtime Shutdown Hook:**
   - Utilizes a shutdown hook to automatically save the game state when the program is terminated.

3. **User Interface:**
   - Accepts user input for moves (up, right, down, left, quit).
   - Allows the player to restart the game.

4. **Game State Management:**
   - Utilizes the `Game` class to manage the state of the 2048 game.
   - Implements the logic for combining tiles and generating new tiles.

5. **Save and Load Functionality:**
   - Saves the game state to the `save-2048.txt` file when the program is terminated.
   - Loads the game state from the save file if it exists.

## How to Use

1. **Compile and Run:**
   - Compile the Java program using a Java compiler.
   - Run the compiled program to start the 2048 game.

2. **Gameplay:**
   - Use the following keys to make moves:
     - `u`: Move tiles up
     - `r`: Move tiles to the right
     - `d`: Move tiles down
     - `l`: Move tiles to the left
     - `n`: Restart the game
     - `q`: Quit the game

3. **Save and Load:**
   - The game state is automatically saved when the program is terminated.
   - The saved state is loaded if a save file (`save-2048.txt`) exists.

4. **Exiting the Game:**
   - To exit the game, enter `q` during your turn.

## Example

Here's a quick example of how to play the game:

```java
Enter move (u/r/d/l/n/q):
```

- Input `u` to move tiles up, `r` to move tiles to the right, `d` to move tiles down, and `l` to move tiles to the left.
- Input `n` to restart the game.
- Input `q` to quit the game.

Feel free to explore and modify the code to enhance or customize the 2048 game. Enjoy playing!