import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

/**
 * Runs Bobby's command-line task list application.
 */
public class Bobby {
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Starts the application and processes commands until the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        ArrayList<Task> tasks = loadTasks(ui);
        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            if (input.trim().equalsIgnoreCase("bye")) {
                break;
            }
            ui.showLine();
            try {
                processCommand(input, tasks, ui);
            } catch (BobbyException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
        ui.showGoodbye();
    }

    /**
     * Processes one non-exit command.
     *
     * @param input the user's command
     * @param tasks the task list
     * @param ui the console user interface
     * @throws BobbyException if the command is invalid
     */
    private static void processCommand(String input, ArrayList<Task> tasks, Ui ui) throws BobbyException {
        String command = input.trim();
        String lowerCaseCommand = command.toLowerCase();
        if (command.equalsIgnoreCase("list")) {
            ui.showTaskList(tasks);
            return;
        }
        if (isCommand(lowerCaseCommand, "mark")) {
            markTask(command, tasks, true, ui);
            return;
        }
        if (isCommand(lowerCaseCommand, "unmark")) {
            markTask(command, tasks, false, ui);
            return;
        }
        if (isCommand(lowerCaseCommand, "delete")) {
            deleteTask(command, tasks, ui);
            return;
        }
        if (isCommand(lowerCaseCommand, "todo")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new BobbyException("You don't have a task after the todo.");
            }
            addTask(new Todo(description), tasks, ui);
            return;
        }
        if (isCommand(lowerCaseCommand, "deadline")) {
            String[] parts = command.substring("deadline".length()).trim().split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new BobbyException("Please use: deadline DESCRIPTION /by YYYY-MM-DD HHMM");
            }
            addTask(new Deadline(parts[0].trim(), parseDateTime(parts[1].trim())), tasks, ui);
            return;
        }
        if (isCommand(lowerCaseCommand, "event")) {
            String eventDetails = command.substring("event".length()).trim();
            String[] fromParts = eventDetails.split(" /from ", 2);
            String[] toParts = fromParts.length == 2 ? fromParts[1].split(" /to ", 2) : new String[0];
            if (fromParts.length < 2 || toParts.length < 2 || fromParts[0].trim().isEmpty()
                    || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                throw new BobbyException("Please use: event DESCRIPTION /from YYYY-MM-DD HHMM /to YYYY-MM-DD HHMM");
            }
            addTask(new Event(fromParts[0].trim(), parseDateTime(toParts[0].trim()),
                    parseDateTime(toParts[1].trim())), tasks, ui);
            return;
        }
        throw new BobbyException("I don't understand what you said. Please use the correct commands");
    }

    /** Returns whether the input is exactly a command word or begins with that word followed by text. */
    private static boolean isCommand(String input, String commandWord) {
        return input.equals(commandWord) || input.startsWith(commandWord + " ");
    }

    /** Parses a date and time entered in {@code yyyy-MM-dd HHmm} format. */
    private static LocalDateTime parseDateTime(String dateTime) throws BobbyException {
        try {
            return LocalDateTime.parse(dateTime, INPUT_DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BobbyException("Please use dates and times in YYYY-MM-DD HHMM format.");
        }
    }

    /** Adds a task to the dynamically sized task list and prints confirmation. */
    private static void addTask(Task task, ArrayList<Task> tasks, Ui ui) throws BobbyException {
        tasks.add(task);
        saveTasks(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    /** Marks or unmarks the task identified by the command's task number. */
    private static void markTask(String command, ArrayList<Task> tasks, boolean isDone, Ui ui)
            throws BobbyException {
        String commandWord = isDone ? "mark" : "unmark";
        int index = getTaskIndex(command.substring(commandWord.length()).trim(), tasks.size());
        if (isDone) {
            tasks.get(index).markAsDone();
        } else {
            tasks.get(index).markAsNotDone();
        }
        saveTasks(tasks);
        ui.showTaskMarked(tasks.get(index), isDone);
    }

    /** Removes the task identified by the command's task number and prints confirmation. */
    private static void deleteTask(String command, ArrayList<Task> tasks, Ui ui) throws BobbyException {
        int index = getTaskIndex(command.substring("delete".length()).trim(), tasks.size());
        Task removedTask = tasks.remove(index);
        saveTasks(tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /** Converts a one-based task number to a valid zero-based task-list index. */
    private static int getTaskIndex(String taskNumber, int taskCount) throws BobbyException {
        try {
            int index = Integer.parseInt(taskNumber) - 1;
            if (index < 0 || index >= taskCount) {
                throw new BobbyException("Invalid task number.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new BobbyException("Invalid task number.");
        }
    }

    /** Saves the changed task list and converts storage errors into a user-facing message. */
    private static void saveTasks(ArrayList<Task> tasks) throws BobbyException {
        try {
            Storage.save(tasks);
        } catch (IOException e) {
            throw new BobbyException("Unable to save tasks to disk.");
        }
    }

    /** Loads saved tasks and starts with an empty list when the save file is unavailable or invalid. */
    private static ArrayList<Task> loadTasks(Ui ui) {
        try {
            return new ArrayList<>(Storage.load());
        } catch (IOException e) {
            ui.showLoadingError();
            return new ArrayList<>();
        }
    }

}
