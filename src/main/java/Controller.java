import game.GameState;
import game.Position;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Controller class for managing the game state and user interactions in the JavaFX application.
 */
public class Controller {
    private final GameState model = new GameState(4);
    private boolean isRowSelected = false;
    private boolean isColSelected = false;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean moveMade = false;
    private String playerOneCharacter;
    private String playerTwoCharacter;

    @FXML
    private GridPane board;

    @FXML
    private GridPane rowButtons;

    @FXML
    private GridPane colButtons;

    @FXML
    private Label playerTurnLabel;

    private final Image aliveUnit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SM.png")));
    private final Image deadUnit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/DeadSM.png")));

    /**
     * Initializes the controller with player character names and sets up the board.
     *
     * @param playerOneCharacter the character representing Player One
     * @param playerTwoCharacter the character representing Player Two
     */
    public void initialize(String playerOneCharacter, String playerTwoCharacter) {
        this.playerOneCharacter = playerOneCharacter;
        this.playerTwoCharacter = playerTwoCharacter;
        playerTurnLabel.setText(playerOneCharacter + "'s Turn");
        initializeBoard();
        initializeRowButtons();
        initializeColButtons();
    }

    /**
     * Initializes the game board with buttons.
     */
    private void initializeBoard() {
        int size = model.getBoard().length;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Button square = createBoardButton(i, j);
                board.add(square, j, i);
            }
        }
    }

    /**
     * Initializes the row selection buttons.
     */
    private void initializeRowButtons() {
        int size = model.getBoard().length;
        for (int i = 0; i < size; ++i) {
            Button rowButton = createRowButton(i);
            rowButtons.add(rowButton, 0, i);
        }
    }

    /**
     * Initializes the column selection buttons.
     */
    private void initializeColButtons() {
        int size = model.getBoard().length;
        for (int j = 0; j < size; ++j) {
            Button colButton = createColButton(j);
            colButtons.add(colButton, j, 0);
        }
    }

    /**
     * Creates a button for the game board.
     *
     * @param row the row index of the button
     * @param col the column index of the button
     * @return the created button
     */
    private Button createBoardButton(int row, int col) {
        Button btn = new Button();
        btn.setPrefSize(100, 100); // Adjust the size of the buttons as needed
        btn.setBackground(new Background(new BackgroundImage(aliveUnit, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, false))));
        btn.setOnMouseClicked(event -> handleBoardButtonClick(row, col));
        return btn;
    }

    /**
     * Creates a button for row selection.
     *
     * @param row the row index of the button
     * @return the created button
     */
    private Button createRowButton(int row) {
        Button btn = new Button("Row " + (row + 1));
        btn.setOnMouseClicked(event -> handleRowButtonClick(row));
        return btn;
    }

    /**
     * Creates a button for column selection.
     *
     * @param col the column index of the button
     * @return the created button
     */
    private Button createColButton(int col) {
        Button btn = new Button("Col " + (col + 1));
        btn.setOnMouseClicked(event -> handleColButtonClick(col));
        return btn;
    }

    /**
     * Handles the event when a board button is clicked.
     *
     * @param row the row index of the clicked button
     * @param col the column index of the clicked button
     */
    private void handleBoardButtonClick(int row, int col) {
        Position pos = new Position(row, col);
        if ((isRowSelected || isColSelected) && model.isLegalPointMove(pos.row(), pos.col(), moveMade, isRowSelected)) {
            model.makeMove(pos, pos); // Making the move from and to the same position to remove the stone
            updateBoard(pos);
            moveMade = true;
            checkGameOver();
            model.switchTurn();
        }
    }

    /**
     * Handles the event when a row button is clicked.
     *
     * @param row the index of the clicked row button
     */
    private void handleRowButtonClick(int row) {
        if (!moveMade) {
            isRowSelected = true;
            isColSelected = false;
            selectedRow = row;
            selectedCol = -1;
            disableOtherButtons();
        }
    }

    /**
     * Handles the event when a column button is clicked.
     *
     * @param col the index of the clicked column button
     */
    private void handleColButtonClick(int col) {
        if (!moveMade) {
            isRowSelected = false;
            isColSelected = true;
            selectedCol = col;
            selectedRow = -1;
            disableOtherButtons();
        }
    }

    /**
     * Handles the event to end the current turn.
     */
    @FXML
    private void handleEndTurn() {
        model.switchTurn();
        model.resetKeyTurn();
        isRowSelected = false;
        isColSelected = false;
        selectedRow = -1;
        selectedCol = -1;
        moveMade = false;
        enableAllButtons();
        updateTurnLabel();
    }

    /**
     * Disables buttons that are not in the selected row or column.
     */
    private void disableOtherButtons() {
        int size = model.getBoard().length;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Button btn = (Button) getNodeByRowColumnIndex(i, j, board);
                if (btn != null) {
                    btn.setDisable(!(isRowSelected && i == selectedRow) && !(isColSelected && j == selectedCol));
                }
            }
        }
    }

    /**
     * Enables all buttons on the board.
     */
    private void enableAllButtons() {
        int size = model.getBoard().length;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Button btn = (Button) getNodeByRowColumnIndex(i, j, board);
                if (btn != null) {
                    btn.setDisable(false);
                }
            }
        }
    }

    /**
     * Updates the turn label to reflect the current player's turn.
     */
    private void updateTurnLabel() {
        playerTurnLabel.setText(model.isPlayerOneTurn() ? playerOneCharacter + "'s Turn" : playerTwoCharacter + "'s Turn");
    }

    /**
     * Updates the board to reflect the move made at the given position.
     *
     * @param pos the position where the move was made
     */
    private void updateBoard(Position pos) {
        Button btn = (Button) getNodeByRowColumnIndex(pos.row(), pos.col(), board);
        btn.setBackground(new Background(new BackgroundImage(deadUnit, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, false))));
    }

    /**
     * Checks if the game is over and displays a message if so.
     */
    private void checkGameOver() {
        if (model.isGameOver()) {
            String winner = model.isPlayerOneTurn() ? playerTwoCharacter : playerOneCharacter;
            showAlert(winner + " wins!");
            System.out.println("Game Over! " + winner + " wins!");
        }
    }

    /**
     * Displays an alert with the given message.
     *
     * @param message the message to display
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retrieves the node at the specified row and column in the given GridPane.
     *
     * @param row the row index
     * @param col the column index
     * @param gridPane the GridPane to search
     * @return the node at the specified position, or null if not found
     */
    private javafx.scene.Node getNodeByRowColumnIndex(final int row, final int col, GridPane gridPane) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row
                    && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }

    @FXML
    private void handleChangeCharacters() {
        MyApplication.getInstance().changeCharacters();
    }

    @FXML
    private void handleRestartGame() {
        model.resetBoard();
        resetBoardButtons();
        moveMade = false;
        updateTurnLabel();
    }

    private void resetBoardButtons() {
        int size = model.getBoard().length;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Button btn = (Button) getNodeByRowColumnIndex(i, j, board);
                if (btn != null) {
                    btn.setBackground(new Background(new BackgroundImage(aliveUnit, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, false))));
                    btn.setDisable(false);
                }
            }
        }
    }
}
