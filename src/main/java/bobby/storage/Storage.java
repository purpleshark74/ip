package bobby.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import bobby.task.Deadline;
import bobby.task.Event;
import bobby.task.Task;
import bobby.task.Todo;

/**
 * Reads and writes the task list at its fixed location on disk.
 */
public class Storage {
    private static final Path SAVE_FILE = Path.of("data", "bobby.txt");

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

    /**
     * Loads the saved task list, if a save file exists.
     *
     * @return the loaded tasks, or an empty list when no save file exists
     * @throws IOException if the save file cannot be read or contains an invalid task record
     */
    public static List<Task> load() throws IOException {
        if (Files.notExists(SAVE_FILE)) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();
        for (String line : Files.readAllLines(SAVE_FILE)) {
            if (!line.isBlank()) {
                tasks.add(parseTask(line));
            }
        }
        return tasks;
    }

    /**
     * Converts one saved line into the corresponding task object.
     */
    private static Task parseTask(String line) throws IOException {
        String[] parts = line.split("\\|", -1);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length < 3 || parts[0].isEmpty() || parts[1].isEmpty() || parts[2].isEmpty()) {
            throw new IOException("Invalid task data.");
        }

        Task task;
        switch (parts[0]) {
            case "T":
                if (parts.length != 3) {
                    throw new IOException("Invalid to-do data.");
                }
                task = new Todo(parts[2]);
                break;
            case "D":
                if (parts.length != 4 || parts[3].isEmpty()) {
                    throw new IOException("Invalid deadline data.");
                }
                task = new Deadline(parts[2], parseDateTime(parts[3]));
                break;
            case "E":
                if (parts.length != 5 || parts[3].isEmpty() || parts[4].isEmpty()) {
                    throw new IOException("Invalid event data.");
                }
                task = new Event(parts[2], parseDateTime(parts[3]), parseDateTime(parts[4]));
                break;
            default:
                throw new IOException("Unknown task type.");
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        } else if (!parts[1].equals("0")) {
            throw new IOException("Invalid task status.");
        }
        return task;
    }

    /**
     * Parses an ISO-8601 date and time saved in the task file.
     */
    private static LocalDateTime parseDateTime(String dateTime) throws IOException {
        try {
            return LocalDateTime.parse(dateTime);
        } catch (DateTimeParseException e) {
            throw new IOException("Invalid date data.", e);
        }
    }
}
