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
public final class Storage {
    private static final int TASK_TYPE_INDEX = 0;
    private static final int TASK_STATUS_INDEX = 1;
    private static final int TASK_DESCRIPTION_INDEX = 2;
    private static final int DEADLINE_DATE_TIME_INDEX = 3;
    private static final int EVENT_START_DATE_TIME_INDEX = 3;
    private static final int EVENT_END_DATE_TIME_INDEX = 4;
    private static final int TODO_FIELD_COUNT = 3;
    private static final int DEADLINE_FIELD_COUNT = 4;
    private static final int EVENT_FIELD_COUNT = 5;
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String INCOMPLETE_STATUS = "0";
    private static final String COMPLETE_STATUS = "1";
    private static final Path SAVE_FILE = Path.of("data", "bobby.txt");

    private Storage() {
    }

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
        String[] fields = getTrimmedFields(line);
        validateCommonFields(fields);

        Task task = createTask(fields);
        applyCompletionStatus(task, fields[TASK_STATUS_INDEX]);
        return task;
    }

    /**
     * Splits a saved record into trimmed fields.
     */
    private static String[] getTrimmedFields(String line) {
        String[] fields = line.split("\\|", -1);
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
        return fields;
    }

    /**
     * Validates the fields shared by every saved task type.
     */
    private static void validateCommonFields(String[] fields) throws IOException {
        boolean hasRequiredFields = fields.length >= TODO_FIELD_COUNT;
        boolean hasTaskType = hasRequiredFields && !fields[TASK_TYPE_INDEX].isEmpty();
        boolean hasTaskStatus = hasRequiredFields && !fields[TASK_STATUS_INDEX].isEmpty();
        boolean hasDescription = hasRequiredFields && !fields[TASK_DESCRIPTION_INDEX].isEmpty();
        if (!hasTaskType || !hasTaskStatus || !hasDescription) {
            throw new IOException("Invalid task data.");
        }
    }

    /**
     * Creates the task represented by validated common fields.
     */
    private static Task createTask(String[] fields) throws IOException {
        switch (fields[TASK_TYPE_INDEX]) {
            case TODO_TYPE:
                return createTodo(fields);
            case DEADLINE_TYPE:
                return createDeadline(fields);
            case EVENT_TYPE:
                return createEvent(fields);
            default:
                throw new IOException("Unknown task type.");
        }
    }

    /**
     * Creates a to-do from a record with the required number of fields.
     */
    private static Todo createTodo(String[] fields) throws IOException {
        if (fields.length != TODO_FIELD_COUNT) {
            throw new IOException("Invalid to-do data.");
        }
        return new Todo(fields[TASK_DESCRIPTION_INDEX]);
    }

    /**
     * Creates a deadline from a record with a valid deadline field.
     */
    private static Deadline createDeadline(String[] fields) throws IOException {
        boolean hasDeadline = fields.length == DEADLINE_FIELD_COUNT
                && !fields[DEADLINE_DATE_TIME_INDEX].isEmpty();
        if (!hasDeadline) {
            throw new IOException("Invalid deadline data.");
        }
        return new Deadline(fields[TASK_DESCRIPTION_INDEX],
                parseDateTime(fields[DEADLINE_DATE_TIME_INDEX]));
    }

    /**
     * Creates an event from a record with valid start and end fields.
     */
    private static Event createEvent(String[] fields) throws IOException {
        boolean hasStartAndEnd = fields.length == EVENT_FIELD_COUNT
                && !fields[EVENT_START_DATE_TIME_INDEX].isEmpty()
                && !fields[EVENT_END_DATE_TIME_INDEX].isEmpty();
        if (!hasStartAndEnd) {
            throw new IOException("Invalid event data.");
        }
        return new Event(fields[TASK_DESCRIPTION_INDEX],
                parseDateTime(fields[EVENT_START_DATE_TIME_INDEX]),
                parseDateTime(fields[EVENT_END_DATE_TIME_INDEX]));
    }

    /**
     * Restores a task's saved completion status.
     */
    private static void applyCompletionStatus(Task task, String status) throws IOException {
        switch (status) {
            case COMPLETE_STATUS:
                task.markAsDone();
                break;
            case INCOMPLETE_STATUS:
                break;
            default:
                throw new IOException("Invalid task status.");
        }
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
