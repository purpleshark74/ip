import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Converts user input into commands that Bobby can execute.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm").withResolverStyle(ResolverStyle.STRICT);

    /** Identifies the operation represented by a parsed command. */
    public enum CommandType {
        LIST,
        ADD,
        MARK,
        UNMARK,
        DELETE
    }

    /**
     * Stores the data needed to execute one parsed command.
     */
    public static class Command {
        private final CommandType type;
        private final Task task;
        private final int taskIndex;

        /**
         * Creates a command with its optional task or zero-based task index.
         *
         * @param type the operation to perform
         * @param task the task to add, or {@code null} for other operations
         * @param taskIndex the task index, or {@code -1} when not applicable
         */
        private Command(CommandType type, Task task, int taskIndex) {
            this.type = type;
            this.task = task;
            this.taskIndex = taskIndex;
        }

        /**
         * Returns the operation to perform.
         *
         * @return the command type
         */
        public CommandType getType() {
            return type;
        }

        /**
         * Returns the task to add.
         *
         * @return the task for an {@link CommandType#ADD} command, otherwise {@code null}
         */
        public Task getTask() {
            return task;
        }

        /**
         * Returns the zero-based task index to operate on.
         *
         * @return the task index for mark, unmark, and delete commands, otherwise {@code -1}
         */
        public int getTaskIndex() {
            return taskIndex;
        }
    }

    /**
     * Returns whether the input is Bobby's exit command.
     *
     * @param input the user's raw input
     * @return {@code true} when the input requests exit
     */
    public static boolean isByeCommand(String input) {
        return input.trim().equalsIgnoreCase("bye");
    }

    /**
     * Parses a user command and validates any referenced task number.
     *
     * @param input the user's raw input
     * @param taskCount the number of currently stored tasks
     * @return the parsed command
     * @throws BobbyException if the command is malformed or has an invalid task number
     */
    public static Command parse(String input, int taskCount) throws BobbyException {
        String command = input.trim();
        String lowerCaseCommand = command.toLowerCase();
        if (command.equalsIgnoreCase("list")) {
            return new Command(CommandType.LIST, null, -1);
        }
        if (isCommand(lowerCaseCommand, "mark")) {
            return new Command(CommandType.MARK, null,
                    getTaskIndex(command.substring("mark".length()).trim(), taskCount));
        }
        if (isCommand(lowerCaseCommand, "unmark")) {
            return new Command(CommandType.UNMARK, null,
                    getTaskIndex(command.substring("unmark".length()).trim(), taskCount));
        }
        if (isCommand(lowerCaseCommand, "delete")) {
            return new Command(CommandType.DELETE, null,
                    getTaskIndex(command.substring("delete".length()).trim(), taskCount));
        }
        if (isCommand(lowerCaseCommand, "todo")) {
            String description = command.substring("todo".length()).trim();
            if (description.isEmpty()) {
                throw new BobbyException("You don't have a task after the todo.");
            }
            return new Command(CommandType.ADD, new Todo(description), -1);
        }
        if (isCommand(lowerCaseCommand, "deadline")) {
            String[] parts = command.substring("deadline".length()).trim().split(" /by ", 2);
            if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new BobbyException("Please use: deadline DESCRIPTION /by YYYY-MM-DD HHMM");
            }
            return new Command(CommandType.ADD,
                    new Deadline(parts[0].trim(), parseDateTime(parts[1].trim())), -1);
        }
        if (isCommand(lowerCaseCommand, "event")) {
            String eventDetails = command.substring("event".length()).trim();
            String[] fromParts = eventDetails.split(" /from ", 2);
            String[] toParts = fromParts.length == 2 ? fromParts[1].split(" /to ", 2) : new String[0];
            if (fromParts.length < 2 || toParts.length < 2 || fromParts[0].trim().isEmpty()
                    || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                throw new BobbyException("Please use: event DESCRIPTION /from YYYY-MM-DD HHMM /to YYYY-MM-DD HHMM");
            }
            return new Command(CommandType.ADD, new Event(fromParts[0].trim(),
                    parseDateTime(toParts[0].trim()), parseDateTime(toParts[1].trim())), -1);
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
}
