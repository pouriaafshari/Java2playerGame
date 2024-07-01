package game;

import java.util.List;

/**
 * Represents the state of the game, including the board and turn management.
 */
public class GameState implements TwoPhaseMoveState<Position> {

    private final int[][] board;
    private boolean isPlayerOneTurn = true;

    // value 99 represents Null for row and col
    private int keyTurnRow = 99;
    private int keyTurnCol = 99;

    /**
     * Constructs a GameState with a specified board size.
     *
     * @param size the size of the board (size x size)
     */
    public GameState(int size) {
        board = createBoard(size);
    }

    /**
     * Creates a new game board with all cells initialized to 1.
     *
     * @param n the size of the board (n x n)
     * @return the initialized game board
     */
    private int[][] createBoard(int n) {
        int[][] board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = 1;
            }
        }
        return board;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getNextPlayer() {
        return isPlayerOneTurn ? Player.PLAYER_1 : Player.PLAYER_2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameOver() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell != 0) return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status getStatus() {
        if (!isGameOver()) {
            return Status.IN_PROGRESS;
        }
        return isPlayerOneTurn ? Status.PLAYER_2_WINS : Status.PLAYER_1_WINS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWinner(Player player) {
        return TwoPhaseMoveState.super.isWinner(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLegalToMoveFrom(Position position) {
        return isOnBoard(position) && board[position.row()][position.col()] != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLegalMove(Position from, Position to) {
        if (!isOnBoard(from) || !isOnBoard(to)) return false;

        // Check if the move is in the same row
        if (from.row() == to.row()) {
            int row = from.row();
            int startCol = Math.min(from.col(), to.col());
            int endCol = Math.max(from.col(), to.col());

            for (int col = startCol; col <= endCol; col++) {
                if (board[row][col] == 0) {
                    return false;
                }
            }
            return true;
        }

        // Check if the move is in the same column
        if (from.col() == to.col()) {
            int col = from.col();
            int startRow = Math.min(from.row(), to.row());
            int endRow = Math.max(from.row(), to.row());

            for (int row = startRow; row <= endRow; row++) {
                if (board[row][col] == 0) {
                    return false;
                }
            }
            return true;
        }

        // If the move is neither in the same row nor the same column, it's illegal
        return false;
    }

    /**
     * Checks if a point move is legal.
     *
     * @param row the row index of the point
     * @param col the column index of the point
     * @param moveMade whether a move has already been made
     * @param isRow whether the selection is by row
     * @return true if the move is legal, false otherwise
     */
    public boolean isLegalPointMove(int row, int col, boolean moveMade, boolean isRow) {
        if (!moveMade) {
            return isInitialMoveLegal(row, col, isRow);
        }
        return isSubsequentMoveLegal(row, col, isRow);
    }

    /**
     * Checks if the initial move is legal.
     *
     * @param row the row index of the point
     * @param col the column index of the point
     * @param isRow whether the selection is by row
     * @return true if the initial move is legal, false otherwise
     */
    private boolean isInitialMoveLegal(int row, int col, boolean isRow) {
        setKeyTurn(row, col);
        return board[row][col] != 0;
    }

    /**
     * Checks if a subsequent move is legal.
     *
     * @param row the row index of the point
     * @param col the column index of the point
     * @param isRow whether the selection is by row
     * @return true if the subsequent move is legal, false otherwise
     */
    private boolean isSubsequentMoveLegal(int row, int col, boolean isRow) {
        if (board[row][col] == 0) {
            return false;
        }

        if (isRow) {
            return isHorizontalMoveLegal(row, col);
        } else {
            return isVerticalMoveLegal(row, col);
        }
    }

    /**
     * Checks if a horizontal move is legal.
     *
     * @param row the row index of the point
     * @param col the column index of the point
     * @return true if the horizontal move is legal, false otherwise
     */
    private boolean isHorizontalMoveLegal(int row, int col) {
        if (col > 0 && col - 1 == keyTurnCol) {
            setKeyTurn(row, col);
            return true;
        }
        if (col < board.length - 1 && col + 1 == keyTurnCol) {
            setKeyTurn(row, col);
            return true;
        }
        return false;
    }

    /**
     * Checks if a vertical move is legal.
     *
     * @param row the row index of the point
     * @param col the column index of the point
     * @return true if the vertical move is legal, false otherwise
     */
    private boolean isVerticalMoveLegal(int row, int col) {
        if (row > 0 && row - 1 == keyTurnRow) {
            setKeyTurn(row, col);
            return true;
        }
        if (row < board.length - 1 && row + 1 == keyTurnRow) {
            setKeyTurn(row, col);
            return true;
        }
        return false;
    }

    /**
     * Sets the key turn position.
     *
     * @param row the row index
     * @param col the column index
     */
    private void setKeyTurn(int row, int col) {
        keyTurnRow = row;
        keyTurnCol = col;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeMove(Position from, Position to) {
        if (isLegalMove(from, to)) {
            if (from.row() == to.row()) {
                int start = Math.min(from.col(), to.col());
                int end = Math.max(from.col(), to.col());
                for (int col = start; col <= end; col++) {
                    board[from.row()][col] = 0; // Remove stones in the selected range
                }
            } else if (from.col() == to.col()) {
                int start = Math.min(from.row(), to.row());
                int end = Math.max(from.row(), to.row());
                for (int row = start; row <= end; row++) {
                    board[row][from.col()] = 0; // Remove stones in the selected range
                }
            }
        }

        isPlayerOneTurn = !isPlayerOneTurn;
    }

    /**
     * Checks if the position is on the board.
     *
     * @param p the position to check
     * @return true if the position is on the board, false otherwise
     */
    private boolean isOnBoard(Position p) {
        int row = p.row();
        int col = p.col();
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    /**
     * Resets the board to its initial state.
     */
    public void resetBoard() {
        int size = board.length;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                board[i][j] = 1;
            }
        }
    }

    /**
     * Gets the current state of the board.
     *
     * @return the game board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Checks if it is Player One's turn.
     *
     * @return true if it is Player One's turn, false otherwise
     */
    public boolean isPlayerOneTurn() {
        return isPlayerOneTurn;
    }

    /**
     * Switches the turn to the next player.
     */
    public void switchTurn() {
        isPlayerOneTurn = !isPlayerOneTurn;
    }

    /**
     * Resets the key turn position to its initial state.
     */
    public void resetKeyTurn() {
        keyTurnCol = 99;
        keyTurnRow = 99;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int cell : row) {
                sb.append(cell).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        GameState state = new GameState(4);
        System.out.println(state);

        // Example moves for testing
        Position from = new Position(0, 0);
        Position to = new Position(0, 2);

        System.out.println("Move from " + from + " to " + to + " is legal: " + state.isLegalMove(from, to));
        state.makeMove(from, to);
        System.out.println(state);
    }
}
