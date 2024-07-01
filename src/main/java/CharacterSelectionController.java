import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Controller class for the character selection scene in the game.
 * Manages the selection of characters for both players and transitions to the game scene.
 */
public class CharacterSelectionController {
    private final Character[] characters = {
            new Character("Abaddon the Despoiler", "/CharacterImages/40K-Gallery-0231.jpg"),
            new Character("Ahriman", "/CharacterImages/ahriman.jpg"),
            new Character("Farseer Eldrad Ulthran", "/CharacterImages/Eldrad_Ulthran-0.jpg"),
            new Character("Autarch Jain Zar", "/CharacterImages/Jain_Zar2_7th_Ed.jpg"),
            new Character("Ghazghkull Thraka", "/CharacterImages/ghazghkull.jpg"),
            new Character("Fabius Bile", "/CharacterImages/Fabius.jpg"),
            new Character("Yvraine, Emissary of Ynnead", "/CharacterImages/Yvraine.jpg"),
            new Character("Warboss", "/CharacterImages/7e-warboss.png"),
            new Character("Khârn the Betrayer", "/CharacterImages/Kharn.jpg"),
            new Character("Huron Blackheart", "/CharacterImages/Huron_Blackheart.jpg"),
            new Character("Typhus", "/CharacterImages/typhus.jpg"),
            new Character("Lucius the Eternal", "/CharacterImages/Lucius_the_Eternal.jpg"),
            new Character("Trazyn the Infinite", "/CharacterImages/Trazyn.jpg"),
            new Character("The Swarmlord", "/CharacterImages/The_Swarmlord.jpg"),
            new Character("Asdrubael Vect", "/CharacterImages/Dark_Eldar_Lord.jpg")
    };

    private String playerOneCharacter;
    private String playerTwoCharacter;
    private boolean isPlayerOneTurn = true;

    @FXML
    private Label instructionLabel;

    @FXML
    private GridPane characterGrid;

    @FXML
    private Button rollDiceButton;

    private final Map<String, Button> characterButtons = new HashMap<>();

    /**
     * Initializes the character selection scene.
     * Sets up the character grid and the initial instruction label.
     */
    @FXML
    private void initialize() {
        initializeCharacterGrid();
        instructionLabel.setText("Player 1, choose your character");
    }

    /**
     * Initializes the character grid with character images and names.
     * Creates buttons for each character and adds them to the grid.
     */
    private void initializeCharacterGrid() {
        int numCols = 5; // Number of columns in the grid
        int numRows = (int) Math.ceil((double) characters.length / numCols); // Number of rows based on the number of characters
        int index = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (index >= characters.length) {
                    // If there are no more characters, break out of the loop
                    break;
                }
                Character character = characters[index];

                // Load image for the character
                ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(character.getImagePath()))));
                imageView.setFitHeight(150); // Set the height of the image
                imageView.setPreserveRatio(true);

                // Create a label for the character name
                Label nameLabel = new Label(character.getName());
                nameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");

                // Create a VBox to hold the image and name label
                VBox vbox = new VBox(imageView, nameLabel);
                vbox.setAlignment(Pos.CENTER);
                vbox.setSpacing(3); // Adjust the spacing between image and text

                // Create a button and set the VBox as its graphic
                Button characterButton = new Button();
                characterButton.setGraphic(vbox);
                characterButton.setMaxWidth(Double.MAX_VALUE);
                characterButton.setStyle("-fx-background-color: transparent;"); // Make the button background transparent
                characterButton.setOnAction(event -> handleCharacterButtonClick(character.getName()));

                // Store button reference in the map
                characterButtons.put(character.getName(), characterButton);

                characterGrid.add(characterButton, j, i);
                index++;
            }
        }
    }

    /**
     * Handles the event when a character button is clicked.
     * Assigns the selected character to the current player and updates the instruction label.
     * If both players have chosen their characters, starts the game.
     *
     * @param characterName the name of the selected character
     */
    private void handleCharacterButtonClick(String characterName) {
        // Disable the button for the selected character
        characterButtons.get(characterName).setDisable(true);

        if (isPlayerOneTurn) {
            playerOneCharacter = characterName;
            instructionLabel.setText("Player 2, choose your character");
            isPlayerOneTurn = false;
        } else {
            playerTwoCharacter = characterName;
            startGame();
        }
    }

    /**
     * Starts the game by transitioning to the game scene and passing the selected characters.
     */
    private void startGame() {
        // Load the game scene and pass the selected characters
        MyApplication.getInstance().startGame(playerOneCharacter, playerTwoCharacter);
    }

    /**
     * Handles the event when the roll dice button is clicked.
     * Rolls a dice and updates the button text with the result.
     */
    @FXML
    private void handleRollDice() {
        int diceRoll = (int) (Math.random() * 6) + 1;
        rollDiceButton.setText("Roll Dice: " + diceRoll);
    }
}
