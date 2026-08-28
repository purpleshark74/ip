import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages the tasks currently used by Bobby.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks the tasks to place in this list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of this list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index the zero-based index
     * @return the selected task
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Marks the task at a zero-based index as done.
     *
     * @param index the zero-based index
     */
    public void markAsDone(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at a zero-based index as not done.
     *
     * @param index the zero-based index
     */
    public void markAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index the zero-based index
     * @return the removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether this list contains no tasks.
     *
     * @return {@code true} when no tasks are stored
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns an unmodifiable snapshot of the current tasks.
     *
     * @return the tasks in their current order
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
