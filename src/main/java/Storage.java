import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Saves the task list to its fixed location on disk.
 */
public class Storage {
    private static final Path SAVE_FILE = Path.of("data", "duke.txt");

    /**
     * Writes the complete current task list, replacing the previous saved copy.
     *
     * @param tasks the tasks to save
     * @throws IOException if the save location cannot be created or written
     */
    public static void save(List<Task> tasks) throws IOException {
        Files.createDirectories(SAVE_FILE.getParent());
        List<String> taskLines = tasks.stream()
                .map(Task::toFileString)
                .toList();
        Files.write(SAVE_FILE, taskLines);
    }
}
