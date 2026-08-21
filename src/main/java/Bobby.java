import java.util.Scanner;

/**
 * Runs Bobby's command-line task list application.
 */
public class Bobby {
    private static final int MAX_TASKS = 100;
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
        Task[] tasks = new Task[MAX_TASKS];
        int count = 0;

        while (!input.trim().equalsIgnoreCase("bye")) {
            System.out.println(LINE);
            try {
                count = processCommand(input, tasks, count);
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
     * Processes one non-exit command and returns the updated number of tasks.
     *
     * @param input the user's command
     * @param tasks the task list
     * @param count the number of tasks stored before this command
     * @return the number of tasks stored after this command
     * @throws BobbyException if the command is invalid
     */
    private static int processCommand(String input, Task[] tasks, int count) throws BobbyException {
        String command = input.trim();
        String lowerCaseCommand = command.toLowerCase();
        if (command.equalsIgnoreCase("list")) {
            printTaskList(tasks, count);
            return count;
        }
        if (isCommand(lowerCaseCommand, "mark")) {
            return markTask(command, tasks, count, true);
        }
        if (isCommand(lowerCaseCommand, "unmark")) {
            return markTask(command, tasks, count, false);
        }
        if (isCommand(lowerCaseCommand, "todo")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new BobbyException("You don't have a task after the todo.");
            }
            return addTask(new Todo(description), tasks, count);
        }
        if (isCommand(lowerCaseCommand, "deadline")) {
            String[] parts = command.substring("deadline".length()).trim().split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new BobbyException("Please use: deadline DESCRIPTION /by DEADLINE");
            }
            return addTask(new Deadline(parts[0].trim(), parts[1].trim()), tasks, count);
        }
        if (isCommand(lowerCaseCommand, "event")) {
            String eventDetails = command.substring("event".length()).trim();
            String[] fromParts = eventDetails.split(" /from ", 2);
            String[] toParts = fromParts.length == 2 ? fromParts[1].split(" /to ", 2) : new String[0];
            if (fromParts.length < 2 || toParts.length < 2 || fromParts[0].trim().isEmpty()
                    || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                throw new BobbyException("Please use: event DESCRIPTION /from START /to END");
            }
            return addTask(new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim()), tasks, count);
        }
        throw new BobbyException("I don't understand what you said. Please use the correct commands");
    }

    /** Returns whether the input is exactly a command word or begins with that word followed by text. */
    private static boolean isCommand(String input, String commandWord) {
        return input.equals(commandWord) || input.startsWith(commandWord + " ");
    }

    /** Adds a task after ensuring that the fixed-size list still has space. */
    private static int addTask(Task task, Task[] tasks, int count) throws BobbyException {
        if (count >= MAX_TASKS) {
            throw new BobbyException("Your task list is full.");
        }
        tasks[count] = task;
        count++;
        printAddedTask(tasks, count);
        return count;
    }

    /** Marks or unmarks the task identified by the command's task number. */
    private static int markTask(String command, Task[] tasks, int count, boolean isDone) throws BobbyException {
        String commandWord = isDone ? "mark" : "unmark";
        String taskNumber = command.substring(commandWord.length()).trim();
        try {
            int index = Integer.parseInt(taskNumber) - 1;
            if (index < 0 || index >= count) {
                throw new BobbyException("Invalid task number.");
            }
            if (isDone) {
                tasks[index].markAsDone();
                System.out.println("     Nice! I've marked this task as done:");
            } else {
                tasks[index].markAsNotDone();
                System.out.println("     OK, I've marked this task as not done yet:");
            }
            System.out.println("       " + tasks[index]);
            return count;
        } catch (NumberFormatException e) {
            throw new BobbyException("Invalid task number.");
        }
    }

    /** Prints all tasks, or a message when the list is empty. */
    private static void printTaskList(Task[] tasks, int count) {
        if (count == 0) {
            System.out.println("No tasks added yet.");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < count; i++) {
            System.out.println("     " + (i + 1) + "." + tasks[i]);
        }
    }

    /**
     * Prints confirmation after the most recently added task.
     *
     * @param tasks the task list
     * @param count the number of tasks stored
     */
    private static void printAddedTask(Task[] tasks, int count) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + tasks[count - 1]);
        System.out.println("     Now you have " + count + " tasks in the list.");
    }
}
