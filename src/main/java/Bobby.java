import java.io.IOException;

/**
 * Runs Bobby's command-line task list application.
 */
public class Bobby {
    /**
     * Starts the application and processes commands until the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        TaskList tasks = loadTasks(ui);
        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            if (Parser.isByeCommand(input)) {
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
    private static void processCommand(String input, TaskList tasks, Ui ui) throws BobbyException {
        Parser.Command command = Parser.parse(input, tasks.size());
        if (command.getType() == Parser.CommandType.LIST) {
            ui.showTaskList(tasks.asList());
        } else if (command.getType() == Parser.CommandType.ADD) {
            addTask(command.getTask(), tasks, ui);
        } else if (command.getType() == Parser.CommandType.MARK) {
            markTask(command.getTaskIndex(), tasks, true, ui);
        } else if (command.getType() == Parser.CommandType.UNMARK) {
            markTask(command.getTaskIndex(), tasks, false, ui);
        } else if (command.getType() == Parser.CommandType.DELETE) {
            deleteTask(command.getTaskIndex(), tasks, ui);
        }
    }

    /** Adds a task to the dynamically sized task list and prints confirmation. */
    private static void addTask(Task task, TaskList tasks, Ui ui) throws BobbyException {
        tasks.add(task);
        saveTasks(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    /** Marks or unmarks the task selected by the parser. */
    private static void markTask(int index, TaskList tasks, boolean isDone, Ui ui)
            throws BobbyException {
        if (isDone) {
            tasks.markAsDone(index);
        } else {
            tasks.markAsNotDone(index);
        }
        saveTasks(tasks);
        ui.showTaskMarked(tasks.get(index), isDone);
    }

    /** Removes the task selected by the parser and prints confirmation. */
    private static void deleteTask(int index, TaskList tasks, Ui ui) throws BobbyException {
        Task removedTask = tasks.remove(index);
        saveTasks(tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /** Saves the changed task list and converts storage errors into a user-facing message. */
    private static void saveTasks(TaskList tasks) throws BobbyException {
        try {
            Storage.save(tasks.asList());
        } catch (IOException e) {
            throw new BobbyException("Unable to save tasks to disk.");
        }
    }

    /** Loads saved tasks and starts with an empty list when the save file is unavailable or invalid. */
    private static TaskList loadTasks(Ui ui) {
        try {
            return new TaskList(Storage.load());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

}
