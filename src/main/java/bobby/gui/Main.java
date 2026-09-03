package bobby.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import bobby.Bobby;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Displays Bobby's JavaFX graphical user interface from its FXML view.
 */
public class Main extends Application {
    private static final double WINDOW_WIDTH = 400.0;
    private static final double WINDOW_HEIGHT = 600.0;

    private final Bobby bobby = new Bobby();

    @Override
    public void start(Stage stage) {
        URL mainWindow = Objects.requireNonNull(
                Main.class.getResource("/view/MainWindow.fxml"),
                "Missing main-window FXML resource");
        FXMLLoader fxmlLoader = new FXMLLoader(mainWindow);

        try {
            AnchorPane mainLayout = fxmlLoader.load();
            MainWindow controller = fxmlLoader.getController();
            controller.setBobby(bobby);

            stage.setScene(new Scene(mainLayout));
            stage.setTitle("Bobby");
            stage.setResizable(false);
            stage.setMinHeight(WINDOW_HEIGHT);
            stage.setMinWidth(WINDOW_WIDTH);
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Bobby's main window.", e);
        }
    }
}
