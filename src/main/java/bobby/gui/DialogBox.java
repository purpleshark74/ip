package bobby.gui;

import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private static final double DISPLAY_PICTURE_SIZE = 100.0;

    private final Label text;
    private final ImageView displayPicture;

    /**
     * Creates a right-aligned dialog box for a message and profile image.
     *
     * @param message the message to display.
     * @param image the speaker's profile image.
     */
    private DialogBox(String message, Image image) {
        text = new Label(message);
        displayPicture = new ImageView(image);

        text.setWrapText(true);
        displayPicture.setFitWidth(DISPLAY_PICTURE_SIZE);
        displayPicture.setFitHeight(DISPLAY_PICTURE_SIZE);
        setAlignment(Pos.TOP_RIGHT);
        getChildren().addAll(text, displayPicture);
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
