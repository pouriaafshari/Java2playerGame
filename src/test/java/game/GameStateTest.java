package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private GameState gameState;

    @BeforeEach
    public void setUp() {
        gameState = new GameState(4);
    }

    @Test
    public void testInitialBoardSetup() {
        int[][] board = gameState.getBoard();
        for (int[] row : board) {
            for (int cell : row) {
                assertEquals(1, cell, "Initial board cell should be 1");
            }
        }
    }

    @Test
    public void testInitialPlayerTurn() {
        assertTrue(gameState.isPlayerOneTurn(), "Initial turn should be Player One's turn");
    }

    @Test
    public void testSwitchTurn() {
        gameState.switchTurn();
        assertFalse(gameState.isPlayerOneTurn(), "After switching, it should be Player Two's turn");
        gameState.switchTurn();
        assertTrue(gameState.isPlayerOneTurn(), "After switching again, it should be Player One's turn");
    }

    @Test
    public void testLegalMove() {
        Position from = new Position(0, 0);
        Position to = new Position(0, 2);
        assertTrue(gameState.isLegalMove(from, to), "Move in the same row should be legal");

        from = new Position(0, 0);
        to = new Position(2, 0);
        assertTrue(gameState.isLegalMove(from, to), "Move in the same column should be legal");

        from = new Position(0, 0);
        to = new Position(2, 2);
        assertFalse(gameState.isLegalMove(from, to), "Diagonal move should be illegal");
    }

    @Test
    public void testMakeMove() {
        Position from = new Position(0, 0);
        Position to = new Position(0, 2);

        assertTrue(gameState.isLegalMove(from, to), "Move in the same row should be legal");
        gameState.makeMove(from, to);

        int[][] board = gameState.getBoard();
        for (int col = 0; col <= 2; col++) {
            assertEquals(0, board[0][col], "Cells in the move range should be 0 after the move");
        }

        assertFalse(gameState.isPlayerOneTurn(), "Turn should switch to Player Two after move");
    }

    @Test
    public void testGameOver() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                gameState.makeMove(new Position(row, col), new Position(row, col));
            }
        }
        assertTrue(gameState.isGameOver(), "Game should be over when all cells are 0");
    }

    @Test
    public void testResetBoard() {
        gameState.resetBoard();
        int[][] board = gameState.getBoard();
        for (int[] row : board) {
            for (int cell : row) {
                assertEquals(1, cell, "Board cells should be reset to 1");
            }
        }
    }
}
