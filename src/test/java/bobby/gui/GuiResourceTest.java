package bobby.gui;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Tests that Bobby's FXML views are available on the runtime classpath.
 */
class GuiResourceTest {
    /**
     * Verifies that the main-window view is packaged as a resource.
     */
    @Test
    void mainWindowFxml_applicationResources_resourceFound() {
        assertNotNull(Main.class.getResource("/view/MainWindow.fxml"));
    }

    /**
     * Verifies that the dialog-box view is packaged as a resource.
     */
    @Test
    void dialogBoxFxml_applicationResources_resourceFound() {
        assertNotNull(DialogBox.class.getResource("/view/DialogBox.fxml"));
    }
}
