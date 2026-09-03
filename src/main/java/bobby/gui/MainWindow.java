package bobby.gui;

import java.io.InputStream;
import java.util.Objects;

import bobby.Bobby;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls Bobby's main chat window.
 */
public class MainWindow extends AnchorPane {
    private final Image userImage = loadImage("/images/User_Icon.png");
    private final Image bobbyImage = loadImage("/images/Bobby_Head.png");

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    private Bobby bobby;

    /**
     * Configures the conversation pane after its FXML controls are injected.
     */
    @FXML
    private void initialize() {
        dialogContainer.heightProperty().addListener(
                observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Supplies the Bobby instance that executes user commands.
     *
     * @param bobby the application's Bobby instance.
     */
    public void setBobby(Bobby bobby) {
        this.bobby = bobby;
    }

    /**
     * Displays the user's command and Bobby's response, then clears the input.
     */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        String bobbyText = bobby.getResponse(userText);
        boolean shouldExit = bobby.isExitCommand(userText);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getBobbyDialog(bobbyText, bobbyImage));
        userInput.clear();
        if (shouldExit) {
            Platform.exit();
        }
    }

    /**
     * Loads an image resource bundled with the application.
     *
     * @param resourcePath the absolute classpath resource path.
     * @return the loaded image.
     */
    private static Image loadImage(String resourcePath) {
        InputStream imageStream = Objects.requireNonNull(
                MainWindow.class.getResourceAsStream(resourcePath),
                "Missing image resource: " + resourcePath);
        return new Image(imageStream);
    }
}
