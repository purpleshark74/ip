/**
 * A task without a date or time.
 */
public class Todo extends Task {
    /**
     * Creates a to-do task.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + description;
    }
}
