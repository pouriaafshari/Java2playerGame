package game;

import game.console.TwoPhaseMoveGame;

import java.util.Scanner;

/**
 * Conducts the custom game on the console.
 */
public class ConsoleGame {

    public static void main(String[] args) {
        var state = new GameState(4); // Adjust the board size if needed
        var game = new TwoPhaseMoveGame<>(state, ConsoleGame::parseMove);
        game.start();
    }

    /**
     * Converts a string containing the position of a move to a {@code Position}
     * object.
     *
     * @param s a string that should contain two space-separated integers
     * @return the {@code Position} object that was constructed from the string
     * @throws IllegalArgumentException if the format of the string is invalid,
     * i.e., its content is not two integers separated with spaces
     */
    public static Position parseMove(String s) {
        s = s.trim();
        if (!s.matches("\\d+\\s+\\d+")) {
            throw new IllegalArgumentException("Invalid input format. Please enter two space-separated integers.");
        }
        var scanner = new Scanner(s);
        return new Position(scanner.nextInt(), scanner.nextInt());
    }

}
