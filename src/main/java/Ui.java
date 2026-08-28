import java.util.List;
import java.util.Scanner;

/**
 * Handles all console input and output for Bobby.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER = "BBBB   OOO   BBBB  BBBB  Y   Y\n"
            + "B   B O   O  B   B B   B  Y Y\n"
            + "BBBB  O   O  BBBB  BBBB    Y\n"
            + "B   B O   O  B   B B   B   Y\n"
            + "BBBB   OOO   BBBB  BBBB    Y";

    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays the welcome banner. */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println(LINE);
        System.out.println("     Hello, I'm Bobby.");
        System.out.println("     What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Returns whether another command is available from the user.
     *
     * @return {@code true} when another input line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the command line
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays a separator before or after a command response. */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays an error caused by an invalid command.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println("     " + message);
    }

    /** Displays an error when saved tasks cannot be loaded. */
    public void showLoadingError() {
        showLine();
        System.out.println("     Unable to load tasks from disk. Starting with an empty list.");
        showLine();
    }

    /**
     * Displays all tasks, or an empty-list message when no tasks exist.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks added yet.");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation after a task is added.
     *
     * @param task the task that was added
     * @param taskCount the new number of tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation after a task's completion state changes.
     *
     * @param task the task whose state changed
     * @param isDone whether the task is now done
     */
    public void showTaskMarked(Task task, boolean isDone) {
        if (isDone) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + task);
    }

    /**
     * Displays confirmation after a task is deleted.
     *
     * @param task the task that was deleted
     * @param taskCount the number of remaining tasks
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays Bobby's farewell message. */
    public void showGoodbye() {
        showLine();
        System.out.println("     Bye! Hope to see you again soon.");
        showLine();
    }
}
