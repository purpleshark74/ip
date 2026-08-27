import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs Bobby's command-line task list application.
 */
public class Bobby {
    private static final String LINE = "____________________________________________________________";

    /**
     * Starts the application and processes commands until the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = "BBBB   OOO   BBBB  BBBB  Y   Y\n"
                + "B   B O   O  B   B B   B  Y Y\n"
                + "BBBB  O   O  BBBB  BBBB    Y\n"
                + "B   B O   O  B   B B   B   Y\n"
                + "BBBB   OOO   BBBB  BBBB    Y";
        System.out.println(LINE);
        System.out.println(banner);
        System.out.println(LINE);
        System.out.println("     Hello, I'm Bobby.");
        System.out.println("     What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        ArrayList<Task> tasks = new ArrayList<>();

        while (!input.trim().equalsIgnoreCase("bye")) {
            System.out.println(LINE);
            try {
                processCommand(input, tasks);
            } catch (BobbyException e) {
                System.out.println("     " + e.getMessage());
            }
            System.out.println(LINE);
            input = scanner.nextLine();
        }

        System.out.println(LINE);
        System.out.println("     Bye! Hope to see you again soon.");
        System.out.println(LINE);
    }

    /**
     * Processes one non-exit command.
     *
     * @param input the user's command
     * @param tasks the task list
     * @throws BobbyException if the command is invalid
     */
    private static void processCommand(String input, ArrayList<Task> tasks) throws BobbyException {
        String command = input.trim();
        String lowerCaseCommand = command.toLowerCase();
        if (command.equalsIgnoreCase("list")) {
            printTaskList(tasks);
            return;
        }
        if (isCommand(lowerCaseCommand, "mark")) {
            markTask(command, tasks, true);
            return;
        }
        if (isCommand(lowerCaseCommand, "unmark")) {
            markTask(command, tasks, false);
            return;
        }
        if (isCommand(lowerCaseCommand, "delete")) {
            deleteTask(command, tasks);
            return;
        }
        if (isCommand(lowerCaseCommand, "todo")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new BobbyException("You don't have a task after the todo.");
            }
            addTask(new Todo(description), tasks);
            return;
        }
        if (isCommand(lowerCaseCommand, "deadline")) {
            String[] parts = command.substring("deadline".length()).trim().split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new BobbyException("Please use: deadline DESCRIPTION /by DEADLINE");
            }
            addTask(new Deadline(parts[0].trim(), parts[1].trim()), tasks);
            return;
        }
        if (isCommand(lowerCaseCommand, "event")) {
            String eventDetails = command.substring("event".length()).trim();
            String[] fromParts = eventDetails.split(" /from ", 2);
            String[] toParts = fromParts.length == 2 ? fromParts[1].split(" /to ", 2) : new String[0];
            if (fromParts.length < 2 || toParts.length < 2 || fromParts[0].trim().isEmpty()
                    || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                throw new BobbyException("Please use: event DESCRIPTION /from START /to END");
            }
            addTask(new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim()), tasks);
            return;
        }
        throw new BobbyException("I don't understand what you said. Please use the correct commands");
    }

    /** Returns whether the input is exactly a command word or begins with that word followed by text. */
    private static boolean isCommand(String input, String commandWord) {
        return input.equals(commandWord) || input.startsWith(commandWord + " ");
    }

    /** Adds a task to the dynamically sized task list and prints confirmation. */
    private static void addTask(Task task, ArrayList<Task> tasks) throws BobbyException {
        tasks.add(task);
        saveTasks(tasks);
        printAddedTask(tasks);
    }

    /** Marks or unmarks the task identified by the command's task number. */
    private static void markTask(String command, ArrayList<Task> tasks, boolean isDone) throws BobbyException {
        String commandWord = isDone ? "mark" : "unmark";
        int index = getTaskIndex(command.substring(commandWord.length()).trim(), tasks.size());
        if (isDone) {
            tasks.get(index).markAsDone();
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            tasks.get(index).markAsNotDone();
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        saveTasks(tasks);
        System.out.println("       " + tasks.get(index));
    }

    /** Removes the task identified by the command's task number and prints confirmation. */
    private static void deleteTask(String command, ArrayList<Task> tasks) throws BobbyException {
        int index = getTaskIndex(command.substring("delete".length()).trim(), tasks.size());
        Task removedTask = tasks.remove(index);
        saveTasks(tasks);
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + removedTask);
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
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

    /** Prints all tasks, or a message when the list is empty. */
    private static void printTaskList(ArrayList<Task> tasks) {
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
     * Prints confirmation after the most recently added task.
     *
     * @param tasks the task list
     */
    private static void printAddedTask(ArrayList<Task> tasks) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + tasks.get(tasks.size() - 1));
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
    }
}
