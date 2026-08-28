package bobby.task;

import bobby.storage.Storage;

/**
 * Represents one task in Bobby's task list.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task that is initially not done.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
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
     * Returns this task in the on-disk format used by {@link Storage}.
     *
     * @return a line that represents this task in the save file
     */
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns this task in the standard list format.
     *
     * @return the formatted task
     */
    @Override
    public String toString() {
        return "[?][" + getStatusIcon() + "] " + description;
    }

}
