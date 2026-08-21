/**
 * Represents one task in Bobby's task list.
 */
public class Task {
    /** The supported kinds of task. */
    public enum Type {
        TODO, DEADLINE, EVENT
    }

    protected String description;
    protected boolean isDone;
    private final Type type;
    private final String by;
    private final String from;
    private final String to;

    /**
     * Creates a task that is initially not done.
     *
     * @param description the task description
     */
    private Task(String description, Type type, String by, String from, String to) {
        this.description = description;
        this.isDone = false;
        this.type = type;
        this.by = by;
        this.from = from;
        this.to = to;
    }

    /**
     * Creates a task without a date or time.
     *
     * @param description the task description
     * @return the new to-do task
     */
    public static Task createTodo(String description) {
        return new Task(description, Type.TODO, null, null, null);
    }

    /**
     * Creates a task to be completed by a specified time.
     *
     * @param description the task description
     * @param by the deadline text
     * @return the new deadline task
     */
    public static Task createDeadline(String description, String by) {
        return new Task(description, Type.DEADLINE, by, null, null);
    }

    /**
     * Creates a task that occurs between a start and end time.
     *
     * @param description the event description
     * @param from the start time text
     * @param to the end time text
     * @return the new event task
     */
    public static Task createEvent(String description, String from, String to) {
        return new Task(description, Type.EVENT, null, from, to);
    }

    /**
     * Returns the task's status icon for display.
     *
     * @return {@code X} when done, otherwise a blank space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns this task in the standard list format.
     *
     * @return the formatted task
     */
    @Override
    public String toString() {
        switch (type) {
        case TODO:
            return "[T][" + getStatusIcon() + "] " + description;
        case DEADLINE:
            return "[D][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
        case EVENT:
            return "[E][" + getStatusIcon() + "] " + description
                    + " (from: " + from + " to: " + to + ")";
        default:
            throw new IllegalStateException("Unsupported task type: " + type);
        }
    }

}
