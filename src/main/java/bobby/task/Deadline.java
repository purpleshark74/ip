package bobby.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import bobby.storage.Storage;

/**
 * A task that must be completed by a specified date and time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd uuuu h:mm a", Locale.ENGLISH);

    private final LocalDateTime by;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param by the deadline date and time
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline in the on-disk format used by {@link Storage}.
     *
     * @return a line that represents this deadline in the save file
     */
    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }

    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: "
                + by.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
