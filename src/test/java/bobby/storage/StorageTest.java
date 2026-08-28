package bobby.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bobby.task.Todo;

/** Tests persistence at Bobby's configured save-file location. */
class StorageTest {
    private static final Path SAVE_FILE = Path.of("data", "bobby.txt");
    private byte[] originalSaveFile;

    /** Backs up a user's existing save file before the test writes its own task data. */
    @BeforeEach
    void backUpExistingSaveFile() throws IOException {
        if (Files.exists(SAVE_FILE)) {
            originalSaveFile = Files.readAllBytes(SAVE_FILE);
        }
    }

    /** Restores a user's existing save file after the test completes. */
    @AfterEach
    void restoreOriginalSaveFile() throws IOException {
        Files.deleteIfExists(SAVE_FILE);
        if (originalSaveFile != null) {
            Files.write(SAVE_FILE, originalSaveFile);
        }
    }

    /** Saving tasks creates the Bobby-named save file and makes the tasks available to a new load operation. */
    @Test
    void saveAndLoad_tasksSaved_tasksLoadedFromBobbyFile() throws IOException {
        Todo task = new Todo("read book");
        task.markAsDone();

        Storage.save(List.of(task));

        assertTrue(Files.exists(SAVE_FILE));
        assertEquals("T | 1 | read book", Files.readString(SAVE_FILE).strip());
        assertEquals("[T][X] read book", Storage.load().getFirst().toString());
    }
}
