package bobby.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Displays a chat message beside its speaker's profile image.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    /**
     * Creates a dialog box backed by its FXML view.
     *
     * @param message the message to display.
     * @param image the speaker's profile image.
     */
    private DialogBox(String message, Image image) {
        URL dialogBoxView = Objects.requireNonNull(
                DialogBox.class.getResource("/view/DialogBox.fxml"),
                "Missing dialog-box FXML resource");
        FXMLLoader fxmlLoader = new FXMLLoader(dialogBoxView);
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load a dialog box.", e);
        }

        dialog.setText(message);
        displayPicture.setImage(image);
    }

    /**
     * Creates a right-aligned dialog box for a user message.
     *
     * @param message the user's message.
     * @param image the user's profile image.
     * @return the user dialog box.
     */
    public static DialogBox getUserDialog(String message, Image image) {
        return new DialogBox(message, image);
    }

    /**
     * Creates a left-aligned dialog box for a Bobby response.
     *
     * @param message Bobby's response.
     * @param image Bobby's profile image.
     * @return the Bobby dialog box.
     */
    public static DialogBox getBobbyDialog(String message, Image image) {
        DialogBox dialogBox = new DialogBox(message, image);
        dialogBox.flip();
        return dialogBox;
    }

    /**
     * Moves the profile image to the left and aligns the dialog box there.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
    }
}
