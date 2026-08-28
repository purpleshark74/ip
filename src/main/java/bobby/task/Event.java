package bobby.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import bobby.storage.Storage;

/**
 * A task that takes place between a start and end date and time.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd uuuu h:mm a", Locale.ENGLISH);

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event task.
     *
     * @param description the event description
     * @param from the start date and time
     * @param to the end date and time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in the on-disk format used by {@link Storage}.
     *
     * @return a line that represents this event in the save file
     */
    @Override
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description
                + " (from: " + from.format(DISPLAY_DATE_FORMAT)
                + " to: " + to.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
