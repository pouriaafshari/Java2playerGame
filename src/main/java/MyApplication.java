import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main application class for the game, extending the JavaFX Application class.
 * This class manages the primary stage and transitions between different scenes.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private Stage primaryStage;

    /**
     * Gets the singleton instance of the application.
     *
     * @return the singleton instance
     */
    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * The main entry point for JavaFX applications. This method is called after
     * the application is initialized.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during loading the initial scene
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        this.primaryStage = primaryStage;
        showCharacterSelection();
    }

    /**
     * Displays the character selection scene.
     *
     * @throws Exception if an error occurs during loading the scene
     */
    public void showCharacterSelection() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterSelection.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Character Selection");
        primaryStage.show();
    }

    /**
     * Starts the game by transitioning to the game scene with the selected characters.
     *
     * @param playerOneCharacter the character chosen by player one
     * @param playerTwoCharacter the character chosen by player two
     */
    public void startGame(String playerOneCharacter, String playerTwoCharacter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
            Parent root = loader.load();

            Controller gameController = loader.getController();
            gameController.initialize(playerOneCharacter, playerTwoCharacter);

            Scene gameScene = new Scene(root);
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("Game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the scene back to the character selection scene.
     */
    public void changeCharacters() {
        try {
            showCharacterSelection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method that launches the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
