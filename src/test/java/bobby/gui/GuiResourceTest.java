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

    /**
     * Verifies that both profile images are packaged as resources.
     */
    @Test
    void profileImages_applicationResources_resourcesFound() {
        assertNotNull(MainWindow.class.getResource("/images/User_Icon.png"));
        assertNotNull(MainWindow.class.getResource("/images/Bobby_Head.png"));
    }
}
