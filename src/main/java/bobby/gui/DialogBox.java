package bobby.gui;

import javafx.geometry.Pos;
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
    public DialogBox(String message, Image image) {
        text = new Label(message);
        displayPicture = new ImageView(image);

        text.setWrapText(true);
        displayPicture.setFitWidth(DISPLAY_PICTURE_SIZE);
        displayPicture.setFitHeight(DISPLAY_PICTURE_SIZE);
        setAlignment(Pos.TOP_RIGHT);
        getChildren().addAll(text, displayPicture);
    }
}
