public class Model {

    private final int[][] board;
    private boolean isPlayerOneTurn = true;
    // value 99 represents Null for row and col
    private int keyTurnRow = 99;
    private int keyTurnCol = 99;

    public Model(int size) {
        board = createBoard(size);
    }

    public int[][] createBoard(int n) {
        int[][] board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = 1;
            }
        }
        return board;
    }

    public boolean isMoveLegal(int row, int col, boolean moveMade, boolean isRow) {
        if (!moveMade) {
            return isInitialMoveLegal(row, col, isRow);
        }
        return isSubsequentMoveLegal(row, col, isRow);
    }

    private boolean isInitialMoveLegal(int row, int col, boolean isRow) {
        setKeyTurn(row, col);
        return board[row][col] != 0;
    }

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

    private void setKeyTurn(int row, int col) {
        keyTurnRow = row;
        keyTurnCol = col;
    }

    public void removeStone(int row, int col) {
        board[row][col] = 0;
    }

    public boolean isGameOver() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell != 0) return false;
            }
        }
        return true;
    }

    public boolean isPlayerOneTurn() {
        return isPlayerOneTurn;
    }

    public void switchTurn() {
        keyTurnCol = 99;
        keyTurnRow = 99;
        isPlayerOneTurn = !isPlayerOneTurn;
    }

    public int[][] getBoard() {
        return board;
    }

    public void resetBoard() {
        int size = board.length;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                board[i][j] = 1;
            }
        }
    }

    public void printBoard() {
        for (int[] row : board) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
